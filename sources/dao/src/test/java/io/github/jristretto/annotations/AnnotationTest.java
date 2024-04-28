/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package io.github.jristretto.annotations;

import io.github.jristretto.dao.Employee;
import io.github.jristretto.mappers.*;
import java.lang.reflect.RecordComponent;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;
import testdata.TestData;

/**
 *
 * @author Pieter van den Hombergh {@code <pieter.van.den.hombergh@gmail.com>}
 */
public class AnnotationTest implements TestData {
//@Disabled("think TDD")

    RecordComponent[] recordComponents = Employee.class
            .getRecordComponents();

    @Test @DisplayName( "test ID present on comp[0] generated true" )
    public void testID() {
        ID annotation = recordComponents[ 0 ].getAnnotation( ID.class );
        assertThat( annotation )
                .isNotNull();
        assertThat( annotation.generated() )
                .isTrue();
//        fail( "method ID reached end. You know what to do." );
    }
    //@Disabled("think TDD")

    @Test @DisplayName( "Hire date is generated " )
    public void testHireDate() {
        Generated annotation = recordComponents[ 8 ].getAnnotation(
                Generated.class );
        assertThat( annotation )
                .isNotNull();

//        fail( "method HireDate reached end. You know what to do." );
    }

    //@Disabled("think TDD")
    @Test @DisplayName( "test generated components methods story line" )
    public void testGetGeneratedComponents() {
        Mapper<Employee, Integer> mapper = AbstractMapper.mapperFor(
                Employee.class );
        boolean generated = mapper.isGenerated( recordComponents[ 0 ] );
        assertThat( generated )
                .isTrue();
        generated = mapper.isGenerated( recordComponents[ 8 ] );
        assertThat( generated )
                .isTrue();
//        fail( "method JannekeShouldGetGeneratedFields reached end. You know what to do." );
    }

}
