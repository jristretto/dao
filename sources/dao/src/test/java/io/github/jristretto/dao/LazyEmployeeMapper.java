/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.github.jristretto.dao;

import io.github.jristretto.annotations.Generated;
import io.github.jristretto.annotations.ID;
import java.time.LocalDate;
import java.util.function.Function;

/**
 *
 * @author Pieter van den Hombergh {@code <pieter.van.den.hombergh@gmail.com>}
 */
class LazyEmployeeMapper extends AbstractMapper<Employee, Integer> {

    public LazyEmployeeMapper() {
        super( Employee.class );
    }

    @Override
    public Function<Employee, Integer> keyExtractor() {
        return Employee::employeeid;
    }
}
