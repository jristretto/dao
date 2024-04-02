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
class EmployeeMapper extends AbstractMapper<Employee, Integer> {

    public EmployeeMapper() {
        super( Employee.class );
    }

    @Override
    public Function<Employee, Integer> keyExtractor() {
        return Employee::employeeid;
    }

    @Override
    public Object[] asArray(Employee r) {
        Object[] result = new Object[]{
            r.employeeid(),
            r.lastname(),
            r.firstname(),
            r.email(),
            r.departmentid(),
            r.available(),
            r.dob(),
            r.hiredate()
        };

        return result;
    }


}
