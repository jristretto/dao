package genericdao.dao;

/**
 * Wraps any exception occurring in the DAO.
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class DAOException extends RuntimeException {

    /**
     * Message and underlying cause.
     *
     * @param message to transfer
     * @param cause leading to this exception
     */
    public DAOException( String message, Throwable cause ) {
        super( message, cause );
    }

    /**
     * When there is no underlying cause.
     *
     * @param message to transfer
     */
    public DAOException( String message ) {
        super( message );
    }

}
