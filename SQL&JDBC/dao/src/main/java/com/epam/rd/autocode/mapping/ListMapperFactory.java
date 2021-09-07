package com.epam.rd.autocode.mapping;


import com.epam.rd.autocode.domain.Department;
import com.epam.rd.autocode.domain.Employee;
import com.epam.rd.autocode.domain.FullName;
import com.epam.rd.autocode.domain.Position;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.*;

public class ListMapperFactory {

    private BigInteger bigIntegerOrZeroOf(BigDecimal bigDecimal) {
        if (bigDecimal != null) {
            return bigDecimal.toBigInteger();
        }
        else return BigInteger.ZERO;
    }

    public ListMapper<List<Employee>> employeesListMapper() {

        class EmployeeListMapperImplementation implements ListMapper<List<Employee>> {

            @Override
            public List<Employee> mapList(ResultSet resultSet) {
                List<Employee> employeeList = new ArrayList<>();

                try {
                    while (resultSet.next()) {
                        BigInteger id = bigIntegerOrZeroOf(resultSet.getBigDecimal("ID"));
                        String firstName = resultSet.getString("FIRSTNAME");
                        String lastName = resultSet.getString("LASTNAME");
                        String middleName = resultSet.getString("MIDDLENAME");
                        Position position = Position.valueOf(resultSet.getString("POSITION"));
                        LocalDate hired = resultSet.getDate("HIREDATE").toLocalDate();
                        BigDecimal salary = resultSet.getBigDecimal("SALARY");
                        BigInteger managerId = bigIntegerOrZeroOf(resultSet.getBigDecimal("MANAGER"));
                        BigInteger departmentId = bigIntegerOrZeroOf(resultSet.getBigDecimal("DEPARTMENT"));

                        employeeList.add(new Employee(
                                id,
                                new FullName(firstName, lastName, middleName),
                                position,
                                hired,
                                salary,
                                managerId,
                                departmentId
                        ));
                    }
                } catch (Exception exception) {
                    throw new UnsupportedOperationException();
                }

                return employeeList;
            }
        }

        return new EmployeeListMapperImplementation();
    }

    public ListMapper<List<Department>> departmentsListMapper() {

        class DepartmentListMapperImplementation implements  ListMapper<List<Department>> {

            @Override
            public List<Department> mapList(ResultSet resultSet) {
                List<Department> departmentList = new ArrayList<>();

                try {
                    while (resultSet.next()) {
                        BigInteger id = bigIntegerOrZeroOf(resultSet.getBigDecimal("ID"));
                        String name = resultSet.getString("NAME");
                        String location = resultSet.getString("LOCATION");

                        departmentList.add(new Department(id, name, location));
                    }
                }
                catch (Exception exception) {
                    throw new UnsupportedOperationException();
                }

                return departmentList;
            }
        }

        return new DepartmentListMapperImplementation();
    }
}
