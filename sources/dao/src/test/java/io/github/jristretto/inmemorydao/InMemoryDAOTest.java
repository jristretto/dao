/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package io.github.jristretto.inmemorydao;

import io.github.jristretto.dao.Employee;
import static io.github.jristretto.dao.Employee.Gender.*;
import io.github.jristretto.mappers.AbstractMapper;
import io.github.jristretto.mappers.Mapper;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import io.github.jristretto.inmemorydao.InMemoryDAO.EqualMask;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
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
        assertThat( result )
                .isEqualTo( expResult );
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

    /**
     * Test of equalMask method, of class InMemoryDAO.
     */
    @Test
    @DisplayName( "equal mask" )
    public void testEqualMask() {
        Object[] keyValues = { "available", true, "departmentid", 1 };
        EqualMask expResult = new EqualMask(
                new boolean[]{ false, false, false, false, true, false, true, false, false },
                new Object[]{ null, null, null, null, 1, null, true, null, null } );
        EqualMask result = dao.equalMask( keyValues );
        assertThat( result )
                .as( expResult.toString() + "\n!=" + result.toString() )
                .isEqualTo( expResult );
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
        boolean expResult = true;
        boolean result = dao.maskedEqual( j, equalMask );
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

}
