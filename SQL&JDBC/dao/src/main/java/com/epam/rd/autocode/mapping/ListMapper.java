package com.epam.rd.autocode.mapping;

import java.sql.ResultSet;

public interface ListMapper<T> {
    T mapList(ResultSet resultSet);
}
