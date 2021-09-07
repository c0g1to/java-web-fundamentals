package com.epam.rd.autotasks.springemployeecatalog.controllers;

import com.epam.rd.autotasks.springemployeecatalog.domain.Employee;
import com.epam.rd.autotasks.springemployeecatalog.sevices.Paging;
import com.epam.rd.autotasks.springemployeecatalog.sevices.ServiceFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {


    @GetMapping("")
    public List<Employee> getAllEmployees(@RequestParam(value = "page", required = false) String page,
                                          @RequestParam(value = "size", required = false) String size,
                                          @RequestParam(value = "sort", required = false) String sort) {
        if (page == null) {
            return ServiceFactory.employeeService().getAll();
        }
        Paging paging = new Paging(Integer.parseInt(page), Integer.parseInt(size));
        switch (sort) {
            case "lastName":
                return ServiceFactory.employeeService().getAllSortByLastname(paging);

            case "hired":
                return ServiceFactory.employeeService().getAllSortByHireDate(paging);

            case "position":
                return ServiceFactory.employeeService().getAllSortByPosition(paging);

            default:
                return ServiceFactory.employeeService().getAllSortBySalary(paging);
        }
    }

    @GetMapping("/{employee_id}")
    public Employee getEmployeeById(@PathVariable Long employee_id,
                                    @RequestParam(value = "full_chain", defaultValue = "false") Boolean full_chain) {
        if (full_chain) {
            return ServiceFactory.employeeService().getByIdWithFullChain(employee_id);
        } else {
            return ServiceFactory.employeeService().getById(employee_id);
        }
    }

    @GetMapping("/by_manager/{manager_id}")
    public List<Employee> getEmployeesByManager(@PathVariable Long manager_id,
                                                @RequestParam("page") String page,
                                                @RequestParam("size") String size,
                                                @RequestParam("sort") String sort) {
        Paging paging = new Paging(Integer.parseInt(page), Integer.parseInt(size));
        switch (sort) {
            case "lastName":
                return ServiceFactory.employeeService().getByManagerSortByLastname(manager_id, paging);

            case "hired":
                return ServiceFactory.employeeService().getByManagerSortByHireDate(manager_id, paging);

            case "position":
                return ServiceFactory.employeeService().getByManagerSortByPosition(manager_id, paging);

            default:
                return ServiceFactory.employeeService().getByManagerSortBySalary(manager_id, paging);
        }

    }

    @GetMapping("/by_department/{department}")
    public List<Employee> getEmployeesByDepartment(@PathVariable String department,
                                                   @RequestParam("page") String page,
                                                   @RequestParam("size") String size,
                                                   @RequestParam("sort") String sort) {
        Paging paging = new Paging(Integer.parseInt(page), Integer.parseInt(size));
        switch (sort) {
            case "lastName":
                return ServiceFactory.employeeService().getByDepartmentSortByLastname(department, paging);

            case "hired":
                return ServiceFactory.employeeService().getByDepartmentSortByHireDate(department, paging);

            case "position":
                return ServiceFactory.employeeService().getByDepartmentSortByPosition(department, paging);

            default:
                return ServiceFactory.employeeService().getByDepartmentSortBySalary(department, paging);
        }
    }
}
