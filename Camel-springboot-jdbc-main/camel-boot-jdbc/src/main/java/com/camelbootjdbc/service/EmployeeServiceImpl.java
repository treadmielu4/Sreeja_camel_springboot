package com.camelbootjdbc.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.camelbootjdbc.model.Employee;

@Service
public class EmployeeServiceImpl extends RouteBuilder {

	@Autowired
	DataSource dataSource;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		}

	@Override
	public void configure() throws Exception {
        
	       	 //Insert Route
		from("direct:insert").process(new Processor() {
			public void process(Exchange xchg) throws Exception {
			//Take the Employee object from the exchange and create the insert query
				Employee employee = xchg.getIn().getBody(Employee.class);
				String query = "INSERT INTO employee(empId,empName)values('" + employee.getEmpId() + "','"
						+ employee.getEmpName() + "')";
			// Set the insert query in body and call camel jdbc
				xchg.getIn().setBody(query);
			}
		}).to("jdbc:dataSource");
		
		//Update Route
		from("direct:update").process(new Processor() {
			public void process(Exchange xchg) throws Exception {
			//Take the Employee object from the exchange and create the insert query
				Employee employee = xchg.getIn().getBody(Employee.class);
				String query = "update employee set empName='" + employee.getEmpName() + "' where empId='"
						+ employee.getEmpId() + "'";
			// Set the insert query in body and call camel jdbc
				xchg.getIn().setBody(query);
			}
		}).to("jdbc:dataSource");
		
		//Update Route
				from("direct:delete").process(new Processor() {
					public void process(Exchange xchg) throws Exception {
					//Take the Employee object from the exchange and create the insert query
						String empId = xchg.getIn().getBody(String.class);
						String query = "delete from employee where empId='"
								+ empId + "'";
					// Set the insert query in body and call camel jdbc
						xchg.getIn().setBody(query);
					}
				}).to("jdbc:dataSource");
		
		// Select Route
		from("direct:select").setBody(constant("select * from Employee")).to("jdbc:dataSource")
				.process(new Processor() {
					public void process(Exchange xchg) throws Exception {
					   //the camel jdbc select query has been executed. We get the list of employees.
						@SuppressWarnings("unchecked")
						ArrayList<Map<String, String>> dataList = (ArrayList<Map<String, String>>) xchg.getIn()
								.getBody();
						List<Employee> employees = new ArrayList<Employee>();
						for (Map<String, String> data : dataList) {
							Employee employee = new Employee();
							employee.setEmpId(data.get("empId"));
							employee.setEmpName(data.get("empName"));
							employees.add(employee);
						}
						xchg.getIn().setBody(employees);
					}
				});

		
	}
}
