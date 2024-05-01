/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package io.github.jristretto.mappers;

import io.github.jristretto.dao.ComponentPair;
import io.github.jristretto.dao.Employee;
import static io.github.jristretto.mappers.LazyEmployee.Gender.N;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
//import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.mockito.*;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import testdata.TestData;

/**
 *
 * @author Pieter van den Hombergh {@code <pieter.van.den.hombergh@gmail.com>}
 */
@ExtendWith( MockitoExtension.class )
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
    @SuppressWarnings( "unchecked" )
    public void testStream() {
        var m = AbstractMapper.mapperFor( LazyEmployee.class );
        Entry<String, Object>[] toArray = m.stream( jean )
                .map( cp -> Map.entry( cp.component()
                .getName(), cp.value() ) )
                .toArray( Entry[]::new );
        Map<String, Object> mapped = Map.ofEntries( toArray );
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

    //@Disabled("think TDD")
    @Test @DisplayName( "code coverage in mapper" )
    public void testDropGeneratedFields() {
        var mapper = AbstractMapper.mapperFor( Employee.class );

        List<ComponentPair> dropGeneratedFields = mapper.dropGeneratedFields(
                TestData.ronaldo );
        assertThat( dropGeneratedFields ).hasSize( 7 );
//        fail( "method DropGeneratedFields reached end. You know what to do." );
    }

    //@Disabled("think TDD")
    @Test @DisplayName( "cover exeception Test" )
    public void testLoadMapper() throws Throwable {
        Appendable out = mock( Appendable.class );
        ArgumentCaptor<LogRecord> captor = ArgumentCaptor.forClass(
                LogRecord.class );
        Logger logger
                = Logger.getLogger( Mapper.class.getName() );
        Handler handler = mock( Handler.class );
        logger.addHandler( handler );

        record Puk(int i) implements Serializable {

        }
        // run the code

        ThrowingCallable code = () -> {
            AbstractMapper.loadMapperClass( Puk.class );
        };

        assertThatThrownBy( code ).isExactlyInstanceOf( RuntimeException.class )
                .hasCauseExactlyInstanceOf( ClassNotFoundException.class );
        verify( handler, atLeast( 1 ) ).publish( captor.capture() );

        assertThat( captor.getValue().getMessage() ).contains(
                "could", "not",
                "find", "mapper" );
//        fail( "method LoadMapper reached end. You know what to do." );
    }
}
