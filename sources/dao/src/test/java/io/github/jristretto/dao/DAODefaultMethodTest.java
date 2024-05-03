package io.github.jristretto.dao;

import io.github.jristretto.mappers.AbstractMapper;
import io.github.jristretto.mappers.Mapper;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;

import testdata.TestData;
import static testdata.TestData.jean;

/**
 * Tets the default methods in the DAO.
 *
 * @author Pieter van den Hombergh {@code <pieter.van.den.hombergh@gmail.com>}
 */
public class DAODefaultMethodTest implements TestData {

    DAO<Employee, Integer> ddao = new DAOMock();

    class DAOMock implements DAO<Employee, Integer> {

        Mapper<Employee, Integer> mapper = AbstractMapper.mapperFor(
                Employee.class );
        int deleteByIdCalled = 0;

        public Mapper<Employee, Integer> getMapper() {
            return mapper;
        }

        @Override
        public Optional<Employee> get(Integer id) {
            return Optional.of( piet );
        }

        @Override
        public List<Employee> getAll() {
            return List.of( jean, piet, karen );
        }

        @Override
        public Optional<Employee> save(Employee e) {
            return Optional.of( e );
        }

        @Override
        public void deleteById(Integer k) {
            deleteByIdCalled++;
        }

        @Override
        public List<Employee> selectWhere(Object... keyValues) {
            return List.of( jean );
        }

    };

    //@Disabled("think TDD")
    @Test @DisplayName( "test delete where " )
    public void testDeletewhere() {
        var l = ddao.deleteWhere( "empoyeeid", 1 );
        assertThat( l ).hasSize( 1 );
//        fail( "method Deletewhere reached end. You know what to do." );
    }

    //@Disabled("think TDD")
    @Test @DisplayName( "test poersistent field names" )
    public void testPersistentFieldNames() {
        assertThat( ddao.persistentFieldNames() ).containsExactlyInAnyOrder(
                "employeeid",
                "lastname",
                "firstname",
                "email",
                "departmentid",
                "gender",
                "available",
                "dob",
                "hiredate" );
//        fail( "method PersistentFieldNames reached end. You know what to do." );
    }

    //@Disabled("think TDD")
    @Test @DisplayName( "test extract id" )
    public void testExtractId() {
        Integer extractId = ddao.extractId( karen );
        assertThat( extractId ).isEqualTo( 3 );

//        fail( "method ExtractId reached end. You know what to do." );
    }

    //@Disabled("think TDD")
    @Test @DisplayName( "test Delete all returns proper values" )
    public void testDeleteAll() {

        ThrowingCallable code = () -> {
            ddao.deleteAll( jean, piet );
        };

        assertThatCode( code ).doesNotThrowAnyException();
        assertThat( ( (DAOMock) ddao ).deleteByIdCalled ).isGreaterThan(
                1 );
//        fail( "method DeleteAll reached end. You know what to do." );
    }

    //@Disabled("think TDD")
    @Test @DisplayName( "test that dummy has size 0 forever" )
    public void testSizeZero() {
        assertThat( ddao.size() ).isEqualTo( 0 );
//        fail( "method SizeZero reached end. You know what to do." );
    }

    //@Disabled("think TDD")
    @Test @DisplayName( "any query does not crash" )
    public void testAnyQueryReturnsEmptyList() {

        assertThat( ddao.anyQuery( "select e where true" ) ).hasSize( 0 );
//        fail( "method AnyQueryReturnsEmptyList reached end. You know what to do." );
    }

    //@Disabled("think TDD")
    @Test @DisplayName( "no exceptions witn dropall" )
    public void testDraopAllDoesNotThrowExceptions() {
        ThrowingCallable code = () -> {
            ddao.dropAll();
        };

        assertThatCode( code ).doesNotThrowAnyException();
//        fail( "method DraopAllDoesNotThrowExceptions reached end. You know what to do." );
    }

    //@Disabled("think TDD")
    @Test @DisplayName( "some story line" )
    public void testUpdate() {

        Employee update = ddao.update( jean );

        assertThat( update ).isEqualTo( jean );
//        fail( "method Update reached end. You know what to do." );
    }

}
