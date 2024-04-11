package io.github.jristretto.mappers;

import io.github.jristretto.dao.Employee;
import io.github.jristretto.mappers.AbstractMapper;
import java.time.LocalDate;
import java.util.function.Function;

/**
 * Does no work other than required.
 *
 * @author Pieter van den Hombergh {@code <pieter.van.den.hombergh@gmail.com>}
 */
class LazyEmployeeMapper extends AbstractMapper<LazyEmployee, Integer> {

    static {
        AbstractMapper.register( new LazyEmployeeMapper() );
    }

    private LazyEmployeeMapper() {
        super( LazyEmployee.class );
    }

    @Override
    public Function<LazyEmployee, Integer> keyExtractor() {
        return LazyEmployee::employeeid;

    }

    public LazyEmployee newEntity(Object[] components) {
        return new LazyEmployee(
                Integer.class.cast( components[ 0 ] ),
                String.class.cast( components[ 1 ] ),
                String.class.cast( components[ 2 ] ),
                String.class.cast( components[ 3 ] ),
                Integer.class.cast( components[ 4 ] ),
                LazyEmployee.Gender.class.cast( components[ 5 ] ),
                Boolean.class.cast( components[ 6 ] ),
                LocalDate.class.cast( components[ 7 ] ),
                LocalDate.class.cast( components[ 8 ] )
        );
    }

    @Override
    public Object[] asArray(LazyEmployee r) {

        return new Object[]{ r.employeeid(), r.lastname(), r.firstname(), r
                             .email(), r.departmentid(), r.gender(), r
                             .available(), r.dob(), r.hiredate() };
    }

}
