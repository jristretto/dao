package io.github.jristretto.dao;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
import io.github.jristretto.annotations.DAOFlavor;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

/**
 *
 * @author Pieter van den Hombergh {@code <pieter.van.den.hombergh@gmail.com>}
 */
public class DAOServiceFinderTest {

//@Disabled("think TDD")
    @Test @DisplayName( "Get the in memory DAO" )
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

    //@Disabled("think TDD")
    @Test @DisplayName( "get the default dao" )
    public void testGetDefaultService() {
        DAOFactory daf = new DAOServiceFinder().getDAOFactory();
        assertThat( daf )
                .isNotNull();
        System.out.println( "daf = " + daf.getClass()
                .getCanonicalName() );
        assertThat( daf.getClass()
                .getAnnotation( DAOFlavor.class )
                .value() )
                .isEqualTo( "inmemory" );
//        fail( "method GetDefaultService reached end. You know what to do." );
    }

    //@Disabled("think TDD")
    @Test @DisplayName( "non existing flavor  returns null" )
    public void testTryNonExistingFlavor() {
        DAOFactory daf = new DAOServiceFinder( "no-service" ).getDAOFactory();

        assertThat( daf )
                .isNull();;
//        fail( "method TryNonExistingFlavor reached end. You know what to do." );
    }

    //@Disabled("think TDD")
    @Test @DisplayName( "some story line" )
    public void testCacheUse() {
        DAOServiceFinder dsf = new DAOServiceFinder( "inmemory" );
        DAOFactory daoFactory1 = dsf.getDAOFactory();
        DAOFactory daoFactory2 = dsf.getDAOFactory();
        assertThat( daoFactory1 ).isSameAs( daoFactory2 );
        assertThat( dsf.toString() ).contains( "flavor", "inmemory" );
//        fail( "method CacheUse reached end. You know what to do." );
    }
}
