package io.github.jristretto.dao;

import io.github.jristretto.annotations.Generated;
import io.github.jristretto.annotations.ID;
import io.github.jristretto.annotations.TableName;
import java.io.Serializable;
import java.time.LocalDate;

/**
 *
 * @author Pieter van den Hombergh {@code <pieter.van.den.hombergh@gmail.com>}
 */
@TableName( "employees" )
public record Employee(
        @ID Integer employeeid,
        String lastname,
        String firstname,
        @ID( generated = false )
        String email,
        Integer departmentid,
        Gender gender,
        Boolean available,
        LocalDate dob,
        @Generated LocalDate hiredate) implements Serializable {

    public enum Gender {
        M, F, N
    }
}
