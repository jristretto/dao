/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package io.github.jristretto.dao;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Set;
import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.*;

/**
 *
 * @author Pieter van den Hombergh {@code <pieter.van.den.hombergh@gmail.com>}
 */
public class MapperTest {

    //@Disabled("think TDD")
    @Test @DisplayName( "generated components" )
    public void testGeneratedComponents() {
        Employee jean = new Employee( 0, "Klaassen", "Jean",
                "jan@example.com", 1, true, LocalDate.of( 1991,
                        2, 23 ),
                LocalDate.of( 1973, Month.MARCH, 4 ) );
        Mapper<Employee, Integer> mapper = new EmployeeMapper();

        var generatedFields = mapper.generatedComponents();
        assertThat( generatedFields )
                .hasSize( 2 );
//        fail( "method EmployeeMapper reached end. You know what to do." );
    }

}
