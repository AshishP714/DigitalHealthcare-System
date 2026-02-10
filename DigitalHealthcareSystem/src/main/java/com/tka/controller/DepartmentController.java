package com.tka.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tka.entity.Department;
import com.tka.service.DepartmentService;

@RestController
@RequestMapping("/department")
public class DepartmentController {

	private final DepartmentService departmentService;

	public DepartmentController(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	@PostMapping("/add-department")
	public Department createDepartment(@RequestBody Department department) {
		return departmentService.createDepartment(department);
	}

	@GetMapping("/{id}")
	public Department getDepartmentById(@PathVariable("id") Long departmentId) {
		return departmentService.getDepartmentById(departmentId);
	}

	@GetMapping("/get-All-department")
	public List<Department> getAllDepartment() {
		return departmentService.getAllDepartment();
	}

	@PutMapping("/{id}")
	public Department updateDepartment(@PathVariable("id") Long departmentId,
			@RequestBody Department updatedDepartment) {
		return departmentService.updateDepartment(departmentId, updatedDepartment);
	}

	@DeleteMapping("/{id}")
	public void deleteDepartment(@PathVariable("id") long departmentId) {
		departmentService.deleteDepartment(departmentId);
	}
}