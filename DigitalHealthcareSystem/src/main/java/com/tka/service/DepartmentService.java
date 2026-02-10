package com.tka.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tka.entity.Department;
import com.tka.repository.DepartmentRepository;

@Service
public class DepartmentService {

	private final DepartmentRepository departmentRepository;

	public DepartmentService(DepartmentRepository departmentRepository) {
		this.departmentRepository = departmentRepository;
	}

	public Department createDepartment(Department department) {
		return departmentRepository.save(department);
	}

	public Department getDepartmentById(Long departmentId) {
		return departmentRepository.findById(departmentId)
				.orElseThrow(() -> new RuntimeException("Department is not found on id " + departmentId));
	}

	public List<Department> getAllDepartment() {
		return departmentRepository.findAll();
	}

	public Department updateDepartment(Long departmentId, Department updatedDepartment) {
		Department existingDepartment = getDepartmentById(departmentId);
		existingDepartment.setName(updatedDepartment.getName());
		return departmentRepository.save(existingDepartment);
	}

	public void deleteDepartment(Long departmentId) {
		try {
			departmentRepository.existsById(departmentId);
			departmentRepository.deleteById(departmentId);
		} catch (Exception e) {
			throw new IllegalArgumentException("Department is not exists on id " + departmentId);
		}	
	}
}