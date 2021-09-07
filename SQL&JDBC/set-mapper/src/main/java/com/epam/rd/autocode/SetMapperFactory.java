package com.epam.rd.autocode;


import com.epam.rd.autocode.domain.Employee;
import com.epam.rd.autocode.domain.FullName;
import com.epam.rd.autocode.domain.Position;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.*;

public class SetMapperFactory {
    public SetMapper<Set<Employee>> employeesSetMapper() {

        class SetMapperImplementation implements SetMapper<Set<Employee>> {

            @Override
            public Set<Employee> mapSet(ResultSet resultSet) {
                Set<Employee> employeeSet = new HashSet<>();
                List<Employee> employeeList = new ArrayList<>();
                Map<BigInteger, BigInteger> idMap = new HashMap<>();

                try {
                    while (resultSet.next()) {
                        BigInteger id = resultSet.getBigDecimal("ID").toBigInteger();
                        String firstName = resultSet.getString("FIRSTNAME");
                        String lastName = resultSet.getString("LASTNAME");
                        String middleName = resultSet.getString("MIDDLENAME");
                        Position position = Position.valueOf(resultSet.getString("POSITION"));
                        LocalDate hired = resultSet.getDate("HIREDATE").toLocalDate();
                        BigDecimal salary = resultSet.getBigDecimal("SALARY");
                        BigDecimal managerId = resultSet.getBigDecimal("MANAGER");
                        if (managerId != null) {
                            idMap.put(id, managerId.toBigInteger());
                        }

                        employeeList.add(new Employee(
                                id,
                                new FullName(firstName, lastName, middleName),
                                position,
                                hired,
                                salary,
                                null
                        ));
                    }
                } catch (Exception exception) {
                    throw new UnsupportedOperationException();
                }

                while (true) {
                    for (Employee employee : employeeList) {
                        employeeSet.add(new Employee(
                                employee.getId(),
                                employee.getFullName(),
                                employee.getPosition(),
                                employee.getHired(),
                                employee.getSalary(),
                                employeeList.stream()
                                        .filter(manager -> idMap.containsKey(employee.getId()) &&
                                                idMap.get(employee.getId()).equals(manager.getId()))
                                        .findAny()
                                        .orElse(null)
                        ));
                    }
                    if (Arrays.equals(employeeList.toArray(), employeeSet.toArray())) break;
                    employeeList.clear();
                    employeeList.addAll(employeeSet);
                    employeeSet.clear();
                }

                return employeeSet;
            }
        }

        return new SetMapperImplementation();
    }
}
