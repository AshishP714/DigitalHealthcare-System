package com.tka.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tka.dto.TimeSlotDTO;
import com.tka.entity.Appointment;
import com.tka.entity.Doctor;
import com.tka.entity.DoctorAvailability;
import com.tka.repository.AppointmentRepostory;
import com.tka.repository.DoctorAvailabilityRepository;
import com.tka.repository.DoctorRepository;

@Service
public class DoctorAvailabilityService {

    private final DoctorAvailabilityRepository availabilityRepository;
    private final AppointmentRepostory appointmentRepository;
    private final DoctorRepository doctorRepository;

    public DoctorAvailabilityService(DoctorAvailabilityRepository availabilityRepository,
                             AppointmentRepostory appointmentRepository,
                             DoctorRepository doctorRepository) {
        this.availabilityRepository = availabilityRepository;
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
    }

    // Admin: Set doctor availability
    public DoctorAvailability setDoctorAvailability(DoctorAvailability availability) {
        return availabilityRepository.save(availability);
    }

    // Get doctor's availability for a specific day
    public List<DoctorAvailability> getDoctorAvailability(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        return availabilityRepository.findByDoctor(doctor);
    }

    // Get available time slots for a doctor on a specific date
    public List<TimeSlotDTO> getAvailableSlots(Long doctorId, LocalDate date) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        DayOfWeek dayOfWeek = date.getDayOfWeek();
        
        // Get doctor's availability for this day
        List<DoctorAvailability> availabilities = availabilityRepository
                .findByDoctorAndDayOfWeek(doctor, dayOfWeek);

        if (availabilities.isEmpty()) {
            return new ArrayList<>(); // Doctor not available on this day
        }

        // Get all booked appointments for this doctor on this date
        List<Appointment> bookedAppointments = appointmentRepository
                .findByDoctorAndAppointmentDate(doctor, date);

        List<LocalTime> bookedTimes = bookedAppointments.stream()
                .map(Appointment::getAppointmentTime)
                .collect(Collectors.toList());

        // Generate time slots
        List<TimeSlotDTO> slots = new ArrayList<>();
        
        for (DoctorAvailability availability : availabilities) {
            LocalTime currentTime = availability.getStartTime();
            LocalTime endTime = availability.getEndTime();
            int duration = availability.getSlotDurationMinutes();

            while (currentTime.plusMinutes(duration).isBefore(endTime) 
                   || currentTime.plusMinutes(duration).equals(endTime)) {
                
                LocalTime slotEnd = currentTime.plusMinutes(duration);
                boolean isAvailable = !bookedTimes.contains(currentTime);
                
                slots.add(new TimeSlotDTO(currentTime, slotEnd, isAvailable));
                currentTime = slotEnd;
            }
        }

        return slots;
    }

    // Delete availability
    public void deleteAvailability(Long availabilityId) {
        if (!availabilityRepository.existsById(availabilityId)) {
            throw new RuntimeException("Availability not found");
        }
        availabilityRepository.deleteById(availabilityId);
    }
}