package io.github.jristretto.mappers;

import io.github.jristretto.dao.ComponentPair;
import static io.github.jristretto.mappers.LazyEmployee.Gender.F;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import static java.util.stream.Collectors.toList;
import org.junit.jupiter.api.*;
//import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

/**
 *
 * @author Pieter van den Hombergh {@code <pieter.van.den.hombergh@gmail.com>}
 */
public class LazyMapperTest {

    // janneke has no empoyeeid yet, nor a hire data
    static LazyEmployee janneke = new LazyEmployee( null, "Puk", "Janneke",
            "jannek@dreamworks.com", 5, F, false, LocalDate.of( 1999,
                    12, 23 ), null );
    static LazyEmployee karen = new LazyEmployee( 3, "Heinz", "Karen",
            "karen@madhatter.info", 5, F, false, LocalDate.of( 1999,
                    12, 23 ),
            LocalDate.of( 2023, Month.MARCH, 4 ) );
    @DisplayName( "test dropped where null and generated" )
    @Test
    void tDropGeneratedComponents() {

        Mapper<LazyEmployee, Integer> mapper = AbstractMapper.mapperFor(
                LazyEmployee.class );
        List<ComponentPair> dropGeneratedComponents = mapper
                .dropGeneratedFields(
                        janneke );
        final List<String> remainingComponents = dropGeneratedComponents
                .stream()
                .map( rcp -> rcp.component()
                .getName() )
                .collect(
                        toList() );

        assertSoftly( softly -> {
            softly.assertThat( remainingComponents )
                    .as( "id should have been dropped" )
                    .hasSize( 7 );
            softly.assertThat( remainingComponents )
                    .containsExactly( "lastname",
                            "firstname", "email", "departmentid", "gender",
                            "available",
                            "dob" );
        } );
//        fail( "method DropGeneratedFields completed succesfully; you know what to do" );
    }

    @DisplayName( "test not dropped because not null and generated" )
    @Test
    void tDropGeneratedComponentsNonOnull() {

        Mapper<LazyEmployee, Integer> mapper = AbstractMapper.mapperFor(
                LazyEmployee.class );
        List<ComponentPair> dropGeneratedComponents = mapper
                .dropGeneratedFields(
                        karen );
        final List<String> remainingComponents = dropGeneratedComponents
                .stream()
                .map( rcp -> rcp.component()
                .getName() )
                .collect(
                        toList() );

        assertSoftly( softly -> {
            softly.assertThat( remainingComponents )
                    .as( "id should noy have been dropped" )
                    .hasSize( 9 );
            softly.assertThat( remainingComponents )
                    .containsExactly( "employeeid", "lastname",
                            "firstname", "email", "departmentid", "gender",
                            "available",
                            "dob", "hiredate" );
        } );
//        fail( "method DropGeneratedFields completed succesfully; you know what to do" );
    }
}
