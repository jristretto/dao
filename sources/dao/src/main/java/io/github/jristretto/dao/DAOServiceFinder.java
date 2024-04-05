/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.github.jristretto.dao;

import io.github.jristretto.annotations.DAOFlavor;
import java.util.Optional;
import java.util.ServiceLoader;

/**
 * Finds the DAO service.
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

    public DAOServiceFinder(String flavor) {
        this.flavor = flavor;
    }

    private DAOFactory dafCache;

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
        System.err.println( "Could not load AbstractAPPFactory" );
        return null;
    }

    @Override
    public String toString() {
        return "DAOServiceFinder{" + "flavor=" + flavor + ", dafCache=" + dafCache + '}';
    }

}
