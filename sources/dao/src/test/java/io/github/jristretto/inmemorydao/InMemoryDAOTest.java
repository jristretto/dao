package io.github.jristretto.inmemorydao;

import io.github.jristretto.dao.Employee;
import static io.github.jristretto.dao.Employee.Gender.*;
import io.github.jristretto.mappers.AbstractMapper;
import io.github.jristretto.mappers.Mapper;
import java.util.List;
import java.util.Optional;
import io.github.jristretto.inmemorydao.InMemoryDAO.EqualMask;
import io.github.jristretto.mappers.LazyEmployee;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.Month;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import testdata.TestData;

/**
 *
 * @author Pieter van den Hombergh {@code <pieter.van.den.hombergh@gmail.com>}
 */
public class InMemoryDAOTest implements TestData {

    InMemoryDAO<Employee, Integer> dao;

    public InMemoryDAOTest() {
        dao = new InMemoryDAO<>( Employee.class );
        Optional<Employee> saved = dao.save( jean );
        assertThat( saved.get() )
                .isEqualTo( jean );
        saved = dao.save( piet );
        assertThat( saved.get() )
                .isEqualTo( piet );
    }

    //@Disabled("think TDD")
    @Test @DisplayName( "test applyGenerators not null" )
    public void testApplyGenerators() {
        var jean2 = dao.applyGenerators( jean );
        System.out.println( "jean2 = " + jean2 );
        assertThat( jean2 )
                .isEqualTo( jean );
//        fail( "method ApplyGenerators reached end. You know what to do." );
    }

    //@Disabled("think TDD")
    @Test @DisplayName( "test applyGenerators not null" )
    public void testApplyGenerators2() {
        var janneke2 = dao.applyGenerators( janneke );

        System.out.println( "janneke2 =" + janneke2 );
        assertThat( janneke2.employeeid() )
                .isNotNull();
//        fail( "method ApplyGenerators reached end. You know what to do." );
    }

    /**
     * Test of get method, of class InMemoryDAO.
     */
    @DisplayName( "get" )
    @Test
    public void testGet() {
        Integer id = piet.employeeid();
        var expResult = Optional.of( piet );
        var result = dao.get( id );
        assertThat( result )
                .isEqualTo( expResult );
    }

    /**
     * Test of getAll method, of class InMemoryDAO.
     */
    @Test
    @DisplayName( "getAll" )
    public void testGetAll() {
        dao.dropAll();
        var expResult = List.of( jean, piet );
        dao.saveAll( jean, piet );
        var result = dao.getAll();
        assertThat( result )
                .containsExactlyInAnyOrderElementsOf( expResult );
    }

    /**
     * Test of save method, of class InMemoryDAO.
     */
    @Test
    @DisplayName( "save" )
    public void testSave() {
        var e = janneke;
        Optional<Employee> expResult = Optional.of( janneke );
        Optional<Employee> result = dao.save( e );
        assertThat( result.get()
                .employeeid() )
                .isNotNull();
    }

    /**
     * Test of update method, of class InMemoryDAO.
     */
    @Test
    @DisplayName( "update" )
    public void testUpdate() {
        int beforeSize = dao.size();
        var e = jean2;
        var expResult = jean2;
        var result = dao.update( e );
        assertThat( result )
                .isEqualTo( expResult );
        assertThat( dao.size() )
                .isEqualTo( beforeSize );
    }

    /**
     * Test of deleteEntity method, of class InMemoryDAO.
     */
    @Test
    @DisplayName( "deleteEntity" )
    public void testDeleteEntity() {
        int beforeSize = dao.size();
        var e = jean;
        dao.deleteEntity( e );
        assertThat( dao.size() )
                .isEqualTo( beforeSize - 1 );
        Optional<Employee> get = dao.get( jean.employeeid() );
        assertThat( get )
                .isEmpty();
    }

    /**
     * Test of deleteById method, of class InMemoryDAO.
     */
    @Test
    @DisplayName( "deleteById" )
    public void testDeleteById() {
        int beforeSize = dao.size();
        var e = piet;
        Integer k = piet.employeeid();
        dao.deleteById( k );
        assertThat( dao.size() )
                .isEqualTo( beforeSize - 1 );
        Optional<Employee> get = dao.get( piet.employeeid() );
        assertThat( get )
                .isEmpty();

    }

    /**
     * Test of selectWhere method, of class InMemoryDAO.
     */
    @Test
    @DisplayName( "select where" )
    public void testSelectWhere() {
        dao.save( janneke );
        Object[] keyValues = new Object[]{ "departmentid", 1 };
        var result = dao.selectWhere( keyValues );
        var expResult = List.of( jean, piet );
        assertThat( result )
                .containsExactlyInAnyOrderElementsOf( expResult );
    }

    //@Disabled("think TDD")
    @Test @DisplayName( "mask and components must have same length" )
    public void testIllegaleEqualMask() {
        ThrowingCallable code = () -> {
            EqualMask expResult = new EqualMask(
                    new boolean[]{ false, false, false, false, true, false, true, false },// false },
                    new Object[]{ null, null, null, null, 1, null, true, null, null }
            );
        };
        assertThatThrownBy( code ).isExactlyInstanceOf(
                IllegalArgumentException.class );
//        fail( "method IllegaleEqualMask reached end. You know what to do." );
    }

    /**
     * Test of maskedEqual method, of class InMemoryDAO.
     */
    @Test
    @DisplayName( "Masked equal true" )
    public void testMaskedEqualTrue() {
        var j = janneke;
        EqualMask equalMask = dao.equalMask( "departmentid", 5, "gender",
                F );
        Mapper<Employee, Integer> mapper = dao.getMapper();
        boolean expResult = true;
        boolean result = equalMask.maskedEqual( mapper.asArray( j ) );
        assertThat( result )
                .isEqualTo( expResult );
    }

    /**
     * Test of maskedEqual method, of class InMemoryDAO.
     */
    @Test
    @DisplayName( "Masked equal false" )
    public void testMaskedEqualFalse() {
        var j = karen;
        EqualMask equalMask = dao.equalMask( "departmentid", 5, "firstname",
                "Janneke" );
        boolean expResult = false;
        boolean result = dao.maskedEqual( j, equalMask );
        assertThat( result )
                .isEqualTo( expResult );
    }

    /**
     * Test of getMapper method, of class InMemoryDAO.
     */
    @Test
    @DisplayName( "get mapper" )
    public void testGetMapper() {
        Mapper expResult = AbstractMapper.mapperFor( Employee.class );
        Mapper result = dao.getMapper();
        assertThat( result )
                .isEqualTo( expResult );
    }

    //@Disabled("think TDD")
    @Test @DisplayName( "some story line" )
    public void testJannekeShouldGetGeneratedComponents() {
        Employee jannekeG = dao.applyGenerators( janneke );

        assertThat( jannekeG.employeeid() )
                .isNotNull();
//        fail( "method JannekeShouldGetGeneratedComponents reached end. You know what to do." );
    }

    //@Disabled("think TDD")
    @Test @DisplayName( "persist dao" )
    public void testPersist() throws IOException {
        dao.dropAll();
        dao.saveAll( jean, piet, karen, janneke );
        for ( Employee employee : dao.getAll() ) {
            System.out.println( "employee = " + employee );
        }
        int size0 = dao.size();
        assertThat( size0 ).isEqualTo( 4 );

        dao.persistToDisk();
        String serName = dao.getStorageFileName();
        File f = new File( serName );
        assertThat( f ).exists();
        long fSize = Files.size( f.toPath() );
        assertThat( fSize ).isGreaterThan( 0 );

        dao.dropAll();
        dao.load( serName );

        int size1 = dao.size();

        assertThat( size1 ).isEqualTo( size0 );
//        fail( "method Persist reached end. You know what to do." );
    }

}
