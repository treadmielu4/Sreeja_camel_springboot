package com.camelbootjdbc.controller;

import java.util.List;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.camelbootjdbc.model.Employee;


@RestController
public class EmployeeController {

	@Autowired
	ProducerTemplate producerTemplate;
	
	@GetMapping("/home")
	public String home(){
		return "Hi";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/employees", method = RequestMethod.GET)
	public List<Employee> getAllEmployees() {
		List<Employee> employees = producerTemplate.requestBody("direct:select", null, List.class);
		return employees;

	}

	@RequestMapping(value = "/employees", consumes = "application/json", method = RequestMethod.POST)
	public boolean insertEmployee(@RequestBody Employee emp) {
		producerTemplate.requestBody("direct:insert", emp, List.class);
		return true;
	}
	
	@PutMapping("/update")
	public boolean updateEmployee(@RequestBody Employee emp) {
		producerTemplate.requestBody("direct:update", emp, List.class);
		return true;
	}
	
	@DeleteMapping("/delete/{id}")
	public boolean deleteEmployee(@PathVariable("id") String empId) {
		producerTemplate.requestBody("direct:delete", empId, List.class);
		return true;
	}
}