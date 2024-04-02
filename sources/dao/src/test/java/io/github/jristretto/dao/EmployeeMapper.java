/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.github.jristretto.dao;

import java.util.function.Function;

/**
 *
 * @author Pieter van den Hombergh {@code <pieter.van.den.hombergh@gmail.com>}
 */
class EmployeeMapper extends AbstractMapper<Employee, Integer> {

    private EmployeeMapper() {
        super( Employee.class );
    }

    static {
        AbstractMapper.register( new EmployeeMapper() );
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
