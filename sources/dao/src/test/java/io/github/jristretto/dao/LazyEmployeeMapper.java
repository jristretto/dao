/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.github.jristretto.dao;

import java.util.function.Function;

/**
 * Does no work other than required.
 *
 * @author Pieter van den Hombergh {@code <pieter.van.den.hombergh@gmail.com>}
 */
class LazyEmployeeMapper extends AbstractMapper<LazyEmployee, Integer> {

    static {
        AbstractMapper.register( new LazyEmployeeMapper() );
    }

    private LazyEmployeeMapper() {
        super( LazyEmployee.class );
    }

    @Override
    public Function<LazyEmployee, Integer> keyExtractor() {
        return LazyEmployee::employeeid;
    }
}
