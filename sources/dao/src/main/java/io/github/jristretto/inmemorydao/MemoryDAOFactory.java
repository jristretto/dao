package io.github.jristretto.inmemorydao;

import io.github.jristretto.dao.DAO;
import io.github.jristretto.dao.DAOFactory;
import io.github.jristretto.dao.TransactionToken;
import java.io.Serializable;
import io.github.jristretto.annotations.DAOFlavor;

/**
 *
 * @author Pieter van den Hombergh {@code <pieter.van.den.hombergh@gmail.com>}
 */
@DAOFlavor( "inmemory" )
public class MemoryDAOFactory implements DAOFactory {

    public MemoryDAOFactory() {
    }

    @Override
    public <E extends java.lang.Record & java.io.Serializable, K extends Serializable> DAO<E, K> createDao(
            Class<E> forClass) {
        return new InMemoryDAO<>( forClass );
    }

    @Override
    public <E extends java.lang.Record & java.io.Serializable, K extends Serializable> DAO<E, K> createDao(
            Class<E> forClass, TransactionToken token) {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    public static MemoryDAOFactory provider() {
        return new MemoryDAOFactory();
    }
}
