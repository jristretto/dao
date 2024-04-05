package io.github.jristretto.dao;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */


import io.github.jristretto.dao.DAOServiceFinder;
import io.github.jristretto.dao.DAOFactory;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

/**
 *
 * @author Pieter van den Hombergh {@code <pieter.van.den.hombergh@gmail.com>}
 */
public class DAOServiceFinderTest {

//@Disabled("think TDD")
    @Test @DisplayName( "Get the DAO" )
    public void testGetDAO() {
        DAOFactory daf = new DAOServiceFinder( "inmemory" ).getDAOFactory();
        assertThat( daf )
                .isNotNull();
        System.out.println( "daf = " + daf.getClass()
                .getCanonicalName() );
        assertThat( daf.getClass()
                .getName() )
                .contains( "memory" );

//      fail( "method GetDAO reached end. You know what to do." );
    }

}
