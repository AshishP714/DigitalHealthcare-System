package com.tka.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tka.entity.Department;
import com.tka.entity.Doctor;
import com.tka.repository.DepartmentRepository;
import com.tka.repository.DoctorRepository;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DoctorRepository doctorRepository;

    public DepartmentService(DepartmentRepository departmentRepository, 
                           DoctorRepository doctorRepository) {
        this.departmentRepository = departmentRepository;
        this.doctorRepository = doctorRepository;
    }

    // Get all departments
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    // Get department by ID
    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
    }

    // Create department (Admin)
    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }

    // Update department (Admin)
    public Department updateDepartment(Long id, Department department) {
        Department existing = getDepartmentById(id);
        existing.setName(department.getName());
        return departmentRepository.save(existing);
    }

    // Delete department (Admin)
    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new RuntimeException("Department not found");
        }
        departmentRepository.deleteById(id);
    }

    // Get doctors by department
    public List<Doctor> getDoctorsByDepartment(Long departmentId) {
        Department department = getDepartmentById(departmentId);
        return doctorRepository.findByDepartmentsContaining(department);
    }
}