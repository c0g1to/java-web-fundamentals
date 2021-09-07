package com.epam.rd.autocode.service;

import com.epam.rd.autocode.ConnectionSource;
import com.epam.rd.autocode.mapping.ListMapperFactory;
import com.epam.rd.autocode.domain.Department;
import com.epam.rd.autocode.domain.Employee;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceFactory {

    private final Paging TOTAL_PAGE = new Paging(1, Integer.MAX_VALUE);

    private final Statement statement;

    public ServiceFactory() {
        try {
            statement = ConnectionSource.instance().createConnection().createStatement();
        } catch (Exception exception) {
            throw new UnsupportedOperationException();
        }
    }

    private ResultSet executeQueryWithTry(String query) {
        try {
            return statement.executeQuery(query);
        } catch (Exception exception) {
            throw new UnsupportedOperationException();
        }
    }

    public EmployeeService employeeService() {

        class EmployeeServiceImplementation implements EmployeeService {

            private final String DEFAULT_SELECTION = "SELECT * FROM EMPLOYEE E LEFT JOIN DEPARTMENT D ON E.DEPARTMENT = D.ID ";

            private List<Employee> getAllEmployees(String query) {
                return new ListMapperFactory().employeesListMapper(false).mapList(
                        executeQueryWithTry(query)
                );
            }

            private List<Employee> getAllEmployeesWithChain(String query) {
                return new ListMapperFactory().employeesListMapper(true).mapList(
                        executeQueryWithTry(query)
                );
            }

            private List<Employee> getEmployeesByDepartment(String query, BigInteger departmentId) {
                return getAllEmployees(query)
                        .stream()
                        .filter(employee -> employee.getDepartment() != null && employee.getDepartment().getId().equals(departmentId))
                        .collect(Collectors.toList());
            }

            private List<Employee> getEmployeesByManager(String query, BigInteger managerId) {
                return getAllEmployees(query)
                        .stream()
                        .filter(employee -> employee.getManager() != null && employee.getManager().getId().equals(managerId))
                        .collect(Collectors.toList());
            }

            private List<Employee> employeePaging(List<Employee> employees, Paging paging) {
                List<Employee> employeesPage = new ArrayList<>();
                for (int index = (paging.page - 1) * paging.itemPerPage;
                     index < employees.size() && index < paging.page * paging.itemPerPage;
                     ++index) {
                    employeesPage.add(employees.get(index));
                }
                return employeesPage;
            }

            @Override
            public List<Employee> getAllSortByHireDate(Paging paging) {
                return employeePaging(getAllEmployees(DEFAULT_SELECTION + "ORDER BY E.HIREDATE"), paging);
            }

            @Override
            public List<Employee> getAllSortByLastname(Paging paging) {
                return employeePaging(getAllEmployees(DEFAULT_SELECTION + "ORDER BY E.LASTNAME"), paging);
            }

            @Override
            public List<Employee> getAllSortBySalary(Paging paging) {
                return employeePaging(getAllEmployees(DEFAULT_SELECTION + "ORDER BY E.SALARY"), paging);
            }

            @Override
            public List<Employee> getAllSortByDepartmentNameAndLastname(Paging paging) {
                return employeePaging(getAllEmployees(DEFAULT_SELECTION + "ORDER BY D.NAME, E.LASTNAME"), paging);
            }

            @Override
            public List<Employee> getByDepartmentSortByHireDate(Department department, Paging paging) {
                return employeePaging(getEmployeesByDepartment(DEFAULT_SELECTION + "ORDER BY E.HIREDATE", department.getId()), paging);
            }

            @Override
            public List<Employee> getByDepartmentSortBySalary(Department department, Paging paging) {
                return employeePaging(getEmployeesByDepartment(DEFAULT_SELECTION + "ORDER BY E.SALARY", department.getId()), paging);
            }

            @Override
            public List<Employee> getByDepartmentSortByLastname(Department department, Paging paging) {
                return employeePaging(getEmployeesByDepartment(DEFAULT_SELECTION + "ORDER BY E.LASTNAME", department.getId()), paging);
            }

            @Override
            public List<Employee> getByManagerSortByLastname(Employee manager, Paging paging) {
                return employeePaging(getEmployeesByManager(DEFAULT_SELECTION + "ORDER BY E.LASTNAME", manager.getId()), paging);
            }

            @Override
            public List<Employee> getByManagerSortByHireDate(Employee manager, Paging paging) {
                return employeePaging(getEmployeesByManager(DEFAULT_SELECTION + "ORDER BY E.HIREDATE", manager.getId()), paging);
            }

            @Override
            public List<Employee> getByManagerSortBySalary(Employee manager, Paging paging) {
                return employeePaging(getEmployeesByManager(DEFAULT_SELECTION + "ORDER BY E.SALARY", manager.getId()), paging);
            }

            @Override
            public Employee getWithDepartmentAndFullManagerChain(Employee employee) {
                return getAllEmployeesWithChain("SELECT * FROM EMPLOYEE E LEFT JOIN DEPARTMENT D ON E.DEPARTMENT = D.ID").stream()
                        .filter(item -> item.getId().equals(employee.getId()))
                        .findAny()
                        .orElse(null);
            }

            @Override
            public Employee getTopNthBySalaryByDepartment(int salaryRank, Department department) {
                return getEmployeesByDepartment(DEFAULT_SELECTION + "ORDER BY E.SALARY DESC", department.getId()).get(salaryRank - 1);
            }
        }

        return new EmployeeServiceImplementation();
    }
}
