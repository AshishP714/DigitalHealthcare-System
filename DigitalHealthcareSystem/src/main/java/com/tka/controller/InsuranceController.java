package com.tka.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tka.entity.Insurance;
import com.tka.entity.Patient;
import com.tka.service.InsuranceService;

@RestController
@RequestMapping("insurance")
public class InsuranceController {

	private final InsuranceService insuranceService;

	public InsuranceController(InsuranceService insuranceService) {
		this.insuranceService = insuranceService;
	}

	@PostMapping("/create-insurance/{patientId}/insurance")
	public Patient addInsuranceToPatient(@PathVariable("patientId") Long patientId, @RequestBody Insurance insurance) {
		return insuranceService.addInsuranceToPatient(patientId, insurance);
	}

	@GetMapping("get-insurance/{id}")
	public Insurance getInsuranceById(@PathVariable("id") Long insuranceId) {
		return insuranceService.getInsuranceById(insuranceId);
	}

	@DeleteMapping("delete-insurance/{id}")
	public void deleteInsurance(@PathVariable("id") Long insuranceId) {
		insuranceService.deleteInsurance(insuranceId);
	}
}