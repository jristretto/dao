/**
 * Simple yet powerful generic dao.
 *
 * <h2>Typical usage</h2>
 *
 * <pre class='brush:java'>
 *  {@literal
 * class Client{
 *    // resource
 *    DAOFactory dfax;
 *    // constructor injection
 *    Client(DaoFactory dfac) {
 *          this.dfac=dfac;
 *    }
 *
 *    List<Customer> getAll() {
 *          DAO<Customer> cDao = dfac.createDao( Customer.class );
 *          return cDao.getAll();
 *    }
 *
 *    List<Customer> getCustomersByCity( String aCountry, String aCity ) {
 *          DAO<Customer> cDao = dfac.createDao( Customer.class );
 *          return cDao.getByColumnValues( "city", aCity, "country", aCountry );
 *    }
 * }
 * }
 * </pre>
 *
 */

package io.github.jristretto.dao;
