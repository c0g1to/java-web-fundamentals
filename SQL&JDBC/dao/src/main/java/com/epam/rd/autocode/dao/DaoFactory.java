package com.epam.rd.autocode.dao;

import com.epam.rd.autocode.ConnectionSource;
import com.epam.rd.autocode.domain.Department;
import com.epam.rd.autocode.domain.Employee;
import com.epam.rd.autocode.mapping.ListMapperFactory;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DaoFactory {

    private final Statement statement;

    public DaoFactory() {
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

    public EmployeeDao employeeDAO() {

        class EmployeeDaoImplementation implements EmployeeDao {

            private List<Employee> getEmployees(String query) {
                return new ListMapperFactory().employeesListMapper().mapList(executeQueryWithTry(query));
            }

            @Override
            public List<Employee> getByDepartment(Department department) {
                return getEmployees("SELECT * FROM EMPLOYEE WHERE DEPARTMENT = " + department.getId());
            }

            @Override
            public List<Employee> getByManager(Employee employee) {
                return getEmployees("SELECT * FROM EMPLOYEE WHERE MANAGER = " + employee.getId());
            }

            @Override
            public Optional<Employee> getById(BigInteger Id) {
                return getEmployees("SELECT * FROM EMPLOYEE WHERE ID = " + Id).stream().findAny();
            }

            @Override
            public List<Employee> getAll() {
                return getEmployees("SELECT * FROM EMPLOYEE");
            }

            @Override
            public Employee save(Employee employee) {
                executeQueryWithTry("INSERT INTO EMPLOYEE VALUES (" +
                        employee.getId() + ", '" +
                        employee.getFullName().getFirstName() + "', '" +
                        employee.getFullName().getLastName() + "', '" +
                        employee.getFullName().getMiddleName() + "', '" +
                        employee.getPosition() + "', " +
                        employee.getManagerId() + ", TO_DATE('" +
                        employee.getHired() + "', 'YYYY-MM-DD'), " +
                        employee.getSalary() + ", " +
                        employee.getDepartmentId() +
                        ")");
                return employee;
            }

            @Override
            public void delete(Employee employee) {
                executeQueryWithTry("DELETE FROM EMPLOYEE WHERE ID = " + employee.getId());
            }
        }

        return new EmployeeDaoImplementation();
    }

    public DepartmentDao departmentDAO() {

        class DepartmentDaoImplementation implements DepartmentDao {

            private List<Department> getDepartments(String query) {
                return new ListMapperFactory().departmentsListMapper().mapList(executeQueryWithTry(query));
            }

            @Override
            public Optional<Department> getById(BigInteger Id) {
                return getDepartments("SELECT * FROM DEPARTMENT WHERE ID = " + Id).stream().findAny();
            }

            @Override
            public List<Department> getAll() {
                return getDepartments("SELECT * FROM DEPARTMENT");
            }

            @Override
            public Department save(Department department) {
                if (getById(department.getId()).orElse(null) == null) {
                    executeQueryWithTry("INSERT INTO DEPARTMENT VALUES (" +
                            department.getId() + ", '" +
                            department.getName() + "', '" +
                            department.getLocation() +
                            "')");
                }
                else {
                    executeQueryWithTry("UPDATE DEPARTMENT\n" +
                            "SET NAME = '" + department.getName() +
                            "', LOCATION = '" + department.getLocation() +
                            "'\nWHERE ID = " + department.getId());
                }
                return department;
            }

            @Override
            public void delete(Department department) {
                executeQueryWithTry("DELETE FROM DEPARTMENT WHERE ID = " + department.getId());
            }
        }

        return new DepartmentDaoImplementation();
    }
}
