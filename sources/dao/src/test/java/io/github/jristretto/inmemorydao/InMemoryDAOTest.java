/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package io.github.jristretto.inmemorydao;

import io.github.jristretto.inmemorydao.InMemoryDAO;
import io.github.jristretto.dao.Employee;
import io.github.jristretto.mappers.AbstractMapper;
import io.github.jristretto.mappers.Mapper;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import io.github.jristretto.inmemorydao.InMemoryDAO.EqualMask;
import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

/**
 *
 * @author Pieter van den Hombergh {@code <pieter.van.den.hombergh@gmail.com>}
 */
public class InMemoryDAOTest {

    InMemoryDAO<Employee, Integer> dao;

    Employee jean = new Employee( 1, "Klaassen", "Jean",
            "jan@example.com", 1, true, LocalDate.of( 1991,
                    2, 23 ),
            LocalDate.of( 1973, Month.MARCH, 4 ) );
    Employee jean2 = new Employee( 1, "Klaassen", "Jean",
            "jan@example.com", 0, false, LocalDate.of( 1991,
                    2, 23 ),
            LocalDate.of( 1973, Month.MARCH, 4 ) );
    Employee piet = new Employee( 2, "Puk", "Piet",
            "piet@somewhere.com", 1, false, LocalDate.of( 1999,
                    12, 23 ),
            LocalDate.of( 2023, Month.MARCH, 4 ) );
    Employee janneke = new Employee( 3, "Puk", "Janneke",
            "piet@somewhere.com", 5, false, LocalDate.of( 1999,
                    12, 23 ),
            LocalDate.of( 2023, Month.MARCH, 4 ) );
    Employee karen = new Employee( 3, "Heinz", "Karen",
            "piet@somewhere.com", 5, false, LocalDate.of( 1999,
                    12, 23 ),
            LocalDate.of( 2023, Month.MARCH, 4 ) );

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
    @Test
    public void testGet() {
        System.out.println( "get" );
        Integer id = piet.employeeid();
        Optional expResult = Optional.of( piet );
        Optional result = dao.get( id );
        assertThat( result )
                .isEqualTo( expResult );
    }

    /**
     * Test of getAll method, of class InMemoryDAO.
     */
    @Test
    public void testGetAll() {
        System.out.println( "getAll" );
        var expResult = List.of( jean, piet );
        var result = dao.getAll();
        assertThat( result )
                .containsExactlyInAnyOrderElementsOf( expResult );
    }

    /**
     * Test of save method, of class InMemoryDAO.
     */
    @Test
    public void testSave() {
        System.out.println( "save" );
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
    public void testUpdate() {
        System.out.println( "update" );
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
    public void testDeleteEntity() {
        System.out.println( "deleteEntity" );
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
    public void testDeleteById() {
        System.out.println( "deleteById" );
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
    public void testSelectWhere() {
        System.out.println( "selectWhere" );
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
    public void testEqualMask() {
        System.out.println( "equalMask" );
        Object[] keyValues = { "available", true, "departmentid", 1 };
        EqualMask expResult = new EqualMask(
                new boolean[]{ false, false, false, false, true, true, false, false },
                new Object[]{ null, null, null, null, 1, true, null, null } );
        EqualMask result = dao.equalMask( keyValues );
        assertThat( result )
                .as( expResult.toString() + "\n!=" + result.toString() )
                .isEqualTo( expResult );
    }

    /**
     * Test of maskedEqual method, of class InMemoryDAO.
     */
    @Test
    public void testMaskedEqualTrue() {
        System.out.println( "maskedEqual" );
        var j = janneke;
        EqualMask equalMask = dao.equalMask( "departmentid", 5, "firstname",
                "Janneke" );
        boolean expResult = true;
        boolean result = dao.maskedEqual( j, equalMask );
        assertThat( result )
                .isEqualTo( expResult );
    }

    /**
     * Test of maskedEqual method, of class InMemoryDAO.
     */
    @Test
    public void testMaskedEqualFalse() {
        System.out.println( "maskedEqualFalse" );
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
    public void testGetMapper() {
        System.out.println( "getMapper" );
        Mapper expResult = AbstractMapper.mapperFor( Employee.class );
        Mapper result = dao.getMapper();
        assertThat( result )
                .isEqualTo( expResult );
    }

}
