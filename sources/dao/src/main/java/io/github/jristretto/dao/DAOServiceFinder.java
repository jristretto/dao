/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.github.jristretto.dao;

import io.github.jristretto.annotations.DAOFlavor;
import java.util.Optional;
import java.util.ServiceLoader;

/**
 * Finds the DAO service by fining hat DAOFactory implementation with a specif flavor.
 *
 * This class uses the ServiceLoader API.
 *
 * The implementer of a DAOFactory should ensure that the DAOFactory service is
 * announces in a file {@code
 * src/main/resources/META-INF/services/io.github.jristretto.dao.DAOFactory}
 * containing one line {@code name.of.the.DAOimplementiation}.
 *
 *
 * Subsequent calls will return the same instance of the found factory if any.
 *
 * @author Pieter van den Hombergh {@code <pieter.van.den.hombergh@gmail.com>}
 */
public class DAOServiceFinder {

    private final String flavor;

    /**
     * Create a finder for a specific DAOflavor. Specify the value string here.
     *
     * @param flavor value string of the requested flavor.
     */
    public DAOServiceFinder(String flavor) {
        this.flavor = flavor;
    }

    /**
     * Create a finder for a the flavor {@code "inmemory"}. Specify the value
     * string here.
     *
     */
    public DAOServiceFinder() {
        this( "inmemory" );
    }

    private DAOFactory dafCache;

    /**
     * Try to get a factory.
     *
     * @return null if not found, otherwise an implementation.
     */
    public DAOFactory getDAOFactory() {
        if ( null != dafCache ) {
            return dafCache;
        }
        Optional<DAOFactory> fac = ServiceLoader.load( DAOFactory.class )
                .stream()
                .map( ServiceLoader.Provider::get )
                .filter( c -> c.getClass()
                .getAnnotation( DAOFlavor.class )
                .value()
                .equals( flavor ) )
                .findFirst();
        if ( fac.isPresent() ) {
            dafCache = fac.get();
            return dafCache;
        }
        System.err.println( "Could not load DAOFactory" );
        return null;
    }

    @Override
    public String toString() {
        return "DAOServiceFinder{" + "flavor=" + flavor + ", dafCache=" + dafCache + '}';
    }

}
