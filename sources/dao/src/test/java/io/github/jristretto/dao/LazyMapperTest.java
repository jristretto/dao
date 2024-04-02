/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package io.github.jristretto.dao;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import static java.util.stream.Collectors.toList;
import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

/**
 *
 * @author Pieter van den Hombergh {@code <pieter.van.den.hombergh@gmail.com>}
 */
public class LazyMapperTest {

    //@Disabled("Think TDD")
    @Test
    void tDropGeneratedComponents() {
        LazyEmployee jean = new LazyEmployee( 0, "Klaassen", "Jean",
                "jan@example.com", 1, true, LocalDate.of( 1991,
                        2, 23 ),
                LocalDate.of( 1973, Month.MARCH, 4 ) );
        Mapper<LazyEmployee, Integer> mapper = AbstractMapper.mapperFor(
                LazyEmployee.class );
        mapper.asArray( jean );
        List<ComponentPair> dropGeneratedComponents = mapper
                .dropGeneratedFields(
                        jean );
        final List<String> remainingComponents = dropGeneratedComponents
                .stream()
                .map( rcp -> rcp.component()
                .getName() )
                .collect(
                        toList() );

        assertSoftly( softly -> {
            softly.assertThat( remainingComponents )
                    .as( "id should have been dropped" )
                    .hasSize( 6 );
            softly.assertThat( remainingComponents )
                    .containsExactly( "lastname",
                            "firstname", "email", "departmentid", "available",
                            "dob" );
        } );
//        fail( "method DropGeneratedFields completed succesfully; you know what to do" );
    }
}
