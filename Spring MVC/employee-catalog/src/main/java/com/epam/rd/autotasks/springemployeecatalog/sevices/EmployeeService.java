package com.epam.rd.autotasks.springemployeecatalog.sevices;

import com.epam.rd.autotasks.springemployeecatalog.domain.Department;
import com.epam.rd.autotasks.springemployeecatalog.domain.Employee;

import java.util.List;

public interface EmployeeService {

    List<Employee> getAll();

    List<Employee> getAllSortByLastname(Paging paging);

    List<Employee> getAllSortByHireDate(Paging paging);

    List<Employee> getAllSortByPosition(Paging paging);

    List<Employee> getAllSortBySalary(Paging paging);

    Employee getById(Long employeeId);

    Employee getByIdWithFullChain(Long employeeId);

    List<Employee> getByManagerSortByLastname(Long managerId, Paging paging);

    List<Employee> getByManagerSortByHireDate(Long managerId, Paging paging);

    List<Employee> getByManagerSortBySalary(Long managerId, Paging paging);
    
    List<Employee> getByManagerSortByPosition(Long managerId, Paging paging);

    List<Employee> getByDepartmentSortByLastname(String department, Paging paging);

    List<Employee> getByDepartmentSortByHireDate(String department, Paging paging);

    List<Employee> getByDepartmentSortByPosition(String department, Paging paging);
    
    List<Employee> getByDepartmentSortBySalary(String department, Paging paging);
}
