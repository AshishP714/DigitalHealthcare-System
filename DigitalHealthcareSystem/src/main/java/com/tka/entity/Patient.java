package com.tka.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tka.entity.type.BloodGroupType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(
		name = "patient_Details",
		uniqueConstraints = {
				@UniqueConstraint(name = "unique_patient_name_and_column", columnNames = {"patientName" , "patientBirthDate"})
		},
		indexes = {
				@Index(name = "index_patient_birth_date", columnList = "patientBirthDate")
		}
	)
public class Patient {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long patientId;
	
	@Column(nullable = false , length = 50)
	private String patientName;
	
	@Column(nullable = false)
	private LocalDate patientBirthDate;
	
	@Column(nullable = false , unique = true)
	private String patientEmail;
	
	private String patientGender;
	
	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime createdAt;
	
	@Enumerated(EnumType.STRING)
	private BloodGroupType bloodGroup;
	
	@OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;
	
	@OneToOne
	@JoinColumn(name = "patient_inturence_id")
	@JsonIgnore
	private Insurance insurance;
	
	@OneToMany(mappedBy = "patient")
	@JsonIgnore
	private List<Appointment> appointment;
}