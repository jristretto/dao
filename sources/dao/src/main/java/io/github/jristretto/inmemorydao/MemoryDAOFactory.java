/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.github.jristretto.inmemorydao;

import io.github.jristretto.dao.DAO;
import io.github.jristretto.dao.DAOFactory;
import io.github.jristretto.dao.TransactionToken;
import java.io.Serializable;

/**
 *
 * @author Pieter van den Hombergh {@code <pieter.van.den.hombergh@gmail.com>}
 */
public class MemoryDAOFactory implements DAOFactory {

    @Override
    public <E extends java.lang.Record & java.io.Serializable, K extends Serializable> DAO<E, K> createDao(
            Class<E> forClass) {
        throw new UnsupportedOperationException( "Not supported yet." ); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public <E extends java.lang.Record & java.io.Serializable, K extends Serializable> DAO<E, K> createDao(
            Class<E> forClass, TransactionToken token) {
        throw new UnsupportedOperationException( "Not supported yet." ); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
