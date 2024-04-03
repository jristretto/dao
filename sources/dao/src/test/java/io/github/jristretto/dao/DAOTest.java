package io.github.jristretto.dao;

import io.github.jristretto.inmemorydao.InMemoryDAO;
import java.util.List;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import org.junit.jupiter.api.Test;
import testdata.TestData;
import static testdata.TestData.janneke;
import static testdata.TestData.jean;
//import static usertypes.Email.email;
//import static usertypes.Email.email;

/**
 * Coverage.
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
//@Disabled
public class DAOTest implements TestData {

    TransactionToken ignoredToken = new TransactionToken() {
    };

    DAO<Employee, Integer> dao = new InMemoryDAO<>( Employee.class );

    /**
     * This test serves to cover the default implementations in DAO.
     *
     * @throws Exception no expected
     */
    @Test
    public void testAllCovered() throws Exception {

        assertThat( dao.startTransaction() )
                .isNull();
        dao.setTransactionToken( ignoredToken );
        assertThat( dao.getTransactionToken() )
                .isNull();
        assertThat( dao.size() )
                .isGreaterThanOrEqualTo( 0 );
        assertThat( dao.lastId() )
                .isGreaterThanOrEqualTo( 0 );
        try {
            dao.close();
        } catch ( Exception e ) {
            fail( "should not throw exception, threw " + e );
        }
        //fail( "testAllCovered not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void testNotImplemented() {
        assertThatThrownBy( () -> {
            List notexepected = dao.getByColumnValues( "key", "value" );
        } )
                .isExactlyInstanceOf( UnsupportedOperationException.class );
        // fail( "test method testNotImplemented reached its end, you can remove this line when you aggree." );
    }

    //@Disabled("Think TDD")
    @Test
    void tDropGeneratedComponents() {
//        Employee jean = new Employee( 0, "Klaassen", "Jean",
//                "jan@example.com", 1, M, true, LocalDate.of( 1991,
//                        2, 23 ),
//                LocalDate.of( 1973, Month.MARCH, 4 ) );
        System.out.println( "janneke = " + janneke );
        List<ComponentPair> dropGeneratedComponents = dao.dropGeneratedFields(
                janneke );
        System.out.println(
                "dropGeneratedComponents = " + dropGeneratedComponents );
        final List<String> remainingComponents = dropGeneratedComponents
                .stream()
                .map( rcp -> rcp.component()
                .getName() )
                .collect(
                        toList() );
        assertSoftly( softly -> {
            softly.assertThat( remainingComponents )
                    .as( "id should have been dropped" )
                    .hasSize( 7 );
            softly.assertThat( remainingComponents )
                    .containsExactly( "lastname",
                            "firstname", "email", "departmentid",
                            "gender",
                            "available",
                            "dob" );
        } );
//        fail( "method DropGeneratedFields completed succesfully; you know what to do" );
    }
}
