/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package io.github.jristretto.mappers;

import static io.github.jristretto.mappers.LazyEmployee.Gender.N;
import java.time.LocalDate;
import java.time.Month;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;

/**
 *
 * @author Pieter van den Hombergh {@code <pieter.van.den.hombergh@gmail.com>}
 */
public class MapperTest {

    LazyEmployee jean = new LazyEmployee( 0, "Klaassen", "Jean",
            "jan@example.com", 1, N, true, LocalDate.of( 1991,
                    2, 23 ),
            LocalDate.of( 1973, Month.MARCH, 4 ) );

    //@Disabled("think TDD")
    @Test @DisplayName( "some story line" )
    public void testIndexTest() {
        var m = AbstractMapper.mapperFor( LazyEmployee.class );
        Map<String, Integer> componentIndex = m.componentIndex();
        Map<String, Integer> expected = Map.of(
                "employeeid", 0,
                "lastname", 1,
                "firstname", 2,
                "email", 3,
                "departmentid", 4,
                "gender", 5,
                "available", 6,
                "dob", 7,
                "hiredate", 8
        );

        assertThat( componentIndex )
                .isEqualTo( expected );
//        fail( "method IndexTest reached end. You know what to do." );
    }

    //@Disabled("think TDD")
    @Test @DisplayName( "some story line" )
    public void testStream() {
        var m = AbstractMapper.mapperFor( LazyEmployee.class );
        Entry[] toArray = m.stream( jean )
                .map( cp -> Map.entry( cp.component()
                .getName(), cp.value() ) )
                .toArray( Entry[]::new );
        var mapped = Map.ofEntries( toArray );
        Map<String, Object> expected = Map.of(
                "employeeid", 0,
                "lastname", "Klaassen",
                "firstname", "Jean",
                "email", "jan@example.com",
                "departmentid", 1,
                "gender", N,
                "available", true,
                "dob", LocalDate.of( 1991,
                        2, 23 ),
                "hiredate", LocalDate.of( 1973, Month.MARCH, 4 )
        );

        assertThat( mapped )
                .isEqualTo( expected );
//        fail( "method Stream reached end. You know what to do." );
    }

}
