package io.github.jristretto.inmemorydao;

import io.github.jristretto.dao.DAO;
import io.github.jristretto.dao.DAOFactory;
import io.github.jristretto.dao.TransactionToken;
import io.github.jristretto.annotations.DAOFlavor;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 *
 * @author Pieter van den Hombergh {@code <pieter.van.den.hombergh@gmail.com>}
 */
@DAOFlavor( "inmemory" )
public class MemoryDAOFactory implements DAOFactory {

    public MemoryDAOFactory() {
    }

    @Override
    public <E extends java.lang.Record & java.io.Serializable, K extends Number> DAO<E, K> createDao(
            Class<E> forClass) {
        InMemoryDAO<E, K> result = new InMemoryDAO<>( forClass );

        return result;
    }

    @Override
    public <E extends java.lang.Record & java.io.Serializable, K extends Number> DAO<E, K> createDao(
            Class<E> forClass, TransactionToken token) {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public static MemoryDAOFactory provider() {
        return new MemoryDAOFactory();
    }
}
