package com.epam.rd.autotasks.springemployeecatalog.sevices;

import com.epam.rd.autotasks.springemployeecatalog.ConnectionSource;
import com.epam.rd.autotasks.springemployeecatalog.domain.Department;
import com.epam.rd.autotasks.springemployeecatalog.domain.Employee;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceFactory {

    private final Paging TOTAL_PAGE = new Paging(1, Integer.MAX_VALUE);

    private final static Statement statement;

    static {
        try {
            statement = ConnectionSource.getInstance().createConnection().createStatement();
        } catch (Exception exception) {
            throw new UnsupportedOperationException();
        }
    }

    private static ResultSet executeQueryWithTry(String query) {
        try {
            return statement.executeQuery(query);
        } catch (Exception exception) {
            throw new UnsupportedOperationException();
        }
    }

    public static EmployeeService employeeService() {

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

            private List<Employee> getEmployeesByDepartment(String query, String department) {
                if (department.matches("\\d+")) {
                    return getAllEmployees(query)
                            .stream()
                            .filter(employee -> employee.getDepartment() != null &&
                                    employee.getDepartment().getId().equals(Long.parseLong(department)))
                            .collect(Collectors.toList());
                } else {
                    return getAllEmployees(query)
                            .stream()
                            .filter(employee -> employee.getDepartment() != null &&
                                    employee.getDepartment().getName().equals(department))
                            .collect(Collectors.toList());
                }
            }

            private List<Employee> getEmployeesByManager(String query, Long managerId) {
                return getAllEmployees(query)
                        .stream()
                        .filter(employee -> employee.getManager() != null && employee.getManager().getId().equals(managerId))
                        .collect(Collectors.toList());
            }

            private List<Employee> employeePaging(List<Employee> employees, Paging paging) {
                List<Employee> employeesPage = new ArrayList<>();
                for (int index = paging.page * paging.itemPerPage;
                     index < employees.size() && index < (paging.page + 1) * paging.itemPerPage;
                     ++index) {
                    employeesPage.add(employees.get(index));
                }
                return employeesPage;
            }

            @Override
            public List<Employee> getAll() {
                return getAllEmployees(DEFAULT_SELECTION + "ORDER BY E.LASTNAME");
            }

            @Override
            public List<Employee> getAllSortByLastname(Paging paging) {
                return employeePaging(getAllEmployees(DEFAULT_SELECTION + "ORDER BY E.LASTNAME"), paging);
            }

            @Override
            public List<Employee> getAllSortByHireDate(Paging paging) {
                return employeePaging(getAllEmployees(DEFAULT_SELECTION + "ORDER BY E.HIREDATE"), paging);
            }

            @Override
            public List<Employee> getAllSortByPosition(Paging paging) {
                return employeePaging(getAllEmployees(DEFAULT_SELECTION + "ORDER BY E.POSITION"), paging);
            }

            @Override
            public List<Employee> getAllSortBySalary(Paging paging) {
                return employeePaging(getAllEmployees(DEFAULT_SELECTION + "ORDER BY E.SALARY"), paging);
            }

            @Override
            public Employee getById(Long employeeId) {
                return getAllEmployees(DEFAULT_SELECTION).stream()
                        .filter(item -> item.getId().equals(employeeId))
                        .findAny()
                        .orElse(null);
            }

            @Override
            public Employee getByIdWithFullChain(Long employeeId) {
                return getAllEmployeesWithChain(DEFAULT_SELECTION).stream()
                        .filter(item -> item.getId().equals(employeeId))
                        .findAny()
                        .orElse(null);
            }

            @Override
            public List<Employee> getByManagerSortByLastname(Long managerId, Paging paging) {
                return employeePaging(getEmployeesByManager(DEFAULT_SELECTION + "ORDER BY E.LASTNAME", managerId), paging);
            }

            @Override
            public List<Employee> getByManagerSortByHireDate(Long managerId, Paging paging) {
                return employeePaging(getEmployeesByManager(DEFAULT_SELECTION + "ORDER BY E.HIREDATE", managerId), paging);
            }

            @Override
            public List<Employee> getByManagerSortByPosition(Long managerId, Paging paging) {
                return employeePaging(getEmployeesByManager(DEFAULT_SELECTION + "ORDER BY E.POSITION", managerId), paging);
            }

            @Override
            public List<Employee> getByManagerSortBySalary(Long managerId, Paging paging) {
                return employeePaging(getEmployeesByManager(DEFAULT_SELECTION + "ORDER BY E.SALARY", managerId), paging);
            }

            @Override
            public List<Employee> getByDepartmentSortByLastname(String department, Paging paging) {
                return employeePaging(getEmployeesByDepartment(DEFAULT_SELECTION + "ORDER BY E.LASTNAME", department), paging);
            }

            @Override
            public List<Employee> getByDepartmentSortByHireDate(String department, Paging paging) {
                return employeePaging(getEmployeesByDepartment(DEFAULT_SELECTION + "ORDER BY E.HIREDATE", department), paging);
            }

            @Override
            public List<Employee> getByDepartmentSortByPosition(String department, Paging paging) {
                return employeePaging(getEmployeesByDepartment(DEFAULT_SELECTION + "ORDER BY E.POSITION", department), paging);
            }

            @Override
            public List<Employee> getByDepartmentSortBySalary(String department, Paging paging) {
                return employeePaging(getEmployeesByDepartment(DEFAULT_SELECTION + "ORDER BY E.SALARY", department), paging);
            }
        }

        return new EmployeeServiceImplementation();
    }
}
