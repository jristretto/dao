/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package testdata;

import io.github.jristretto.dao.Employee;
import static io.github.jristretto.dao.Employee.Gender.F;
import static io.github.jristretto.dao.Employee.Gender.M;
import static io.github.jristretto.dao.Employee.Gender.N;
import java.time.LocalDate;
import java.time.Month;

/**
 *
 * @author Pieter van den Hombergh {@code <pieter.van.den.hombergh@gmail.com>}
 */
public interface TestData {

    static Employee jean = new Employee( 1, "Klaassen", "Jean",
            "jan@example.com", 1, M, true, LocalDate.of( 1991,
                    2, 23 ),
            LocalDate.of( 1973, Month.MARCH, 4 ) );
    static Employee jean2 = new Employee( 1, "Klaassen", "Jean",
            "jan@example.com", 0, N, false, LocalDate.of( 1991,
                    2, 23 ),
            LocalDate.of( 1973, Month.MARCH, 4 ) );
    static Employee piet = new Employee( 2, "Puk", "Piet",
            "piet@somewhere.com", 1, M, false, LocalDate.of( 1999,
                    12, 23 ),
            LocalDate.of( 2023, Month.MARCH, 4 ) );
    // janneke has no empoyeeid yet, nor a hire data
    static Employee janneke = new Employee( null, "Puk", "Janneke",
            "jannek@dreamworks.com", 5, F, false, LocalDate.of( 1999,
                    12, 23 ), null );
    static Employee karen = new Employee( 3, "Heinz", "Karen",
            "karen@madhatter.info", 5, F, false, LocalDate.of( 1999,
                    12, 23 ),
            LocalDate.of( 2023, Month.MARCH, 4 ) );
}
