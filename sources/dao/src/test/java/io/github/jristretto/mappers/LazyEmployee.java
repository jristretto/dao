package io.github.jristretto.mappers;

import io.github.jristretto.annotations.Generated;
import io.github.jristretto.annotations.ID;
import java.io.Serializable;
import java.time.LocalDate;

/**
 *
 * @author Pieter van den Hombergh {@code <pieter.van.den.hombergh@gmail.com>}
 */
public record LazyEmployee(@ID Integer employeeid, String lastname, String firstname,
        String email, Integer departmentid, Boolean available, LocalDate dob,
        @Generated LocalDate hiredate) implements Serializable {

}
