package com.tka.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Department {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false , unique = true , length = 100)
	private String name;
	
	@OneToOne
	private Doctor headDoctor;
	
	@ManyToMany
	@JoinTable(
			name = "my_dept_doctors",
			joinColumns = @JoinColumn(name = "dpt_id"),
			inverseJoinColumns = @JoinColumn(name = "doctor_id")
			)
	private Set<Doctor> doctors = new HashSet<>();
}
