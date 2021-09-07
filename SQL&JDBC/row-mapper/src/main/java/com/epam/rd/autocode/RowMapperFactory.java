package com.epam.rd.autocode;

import com.epam.rd.autocode.domain.Employee;
import com.epam.rd.autocode.domain.FullName;
import com.epam.rd.autocode.domain.Position;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.time.LocalDate;

public class RowMapperFactory {

    public RowMapper<Employee> employeeRowMapper() {

        class RowMapperImplementation implements RowMapper<Employee> {

            @Override
            public Employee mapRow(ResultSet resultSet) {
                try {
                    BigInteger id = resultSet.getBigDecimal("ID").toBigInteger();
                    String firstName = resultSet.getString("FIRSTNAME");
                    String lastName = resultSet.getString("LASTNAME");
                    String middleName = resultSet.getString("MIDDLENAME");
                    Position position = Position.valueOf(resultSet.getString("POSITION"));
                    LocalDate hired = resultSet.getDate("HIREDATE").toLocalDate();
                    BigDecimal salary = resultSet.getBigDecimal("SALARY");

                    return new Employee(
                            id,
                            new FullName(firstName, lastName, middleName),
                            position,
                            hired,
                            salary);
                }
                catch (Exception exception) {
                    throw new UnsupportedOperationException();
                }
            }
        }

        return new RowMapperImplementation();
    }
}
