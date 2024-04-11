package io.github.jristretto.dao;

import io.github.jristretto.mappers.AbstractMapper;
import java.time.LocalDate;
import java.util.function.Function;

/**
 *
 * @author Pieter van den Hombergh {@code <pieter.van.den.hombergh@gmail.com>}
 */
class EmployeeMapper extends AbstractMapper<Employee, Integer> {

    private EmployeeMapper() {
        super( Employee.class );
    }

    static {
        AbstractMapper.register( new EmployeeMapper() );
    }

    @Override
    public Function<Employee, Integer> keyExtractor() {
        return Employee::employeeid;
    }

    @Override
    public Object[] asArray(Employee r) {
        Object[] result = new Object[]{
            r.employeeid(),
            r.lastname(),
            r.firstname(),
            r.email(),
            r.departmentid(),
            r.gender(),
            r.available(),
            r.dob(),
            r.hiredate()
        };

        return result;
    }

    @Override
    public Employee newEntity(Object[] components) {
        return new Employee(
                Integer.class.cast( components[ 0 ] ),
                String.class.cast( components[ 1 ] ),
                String.class.cast( components[ 2 ] ),
                String.class.cast( components[ 3 ] ),
                Integer.class.cast( components[ 4 ] ),
                Employee.Gender.class.cast( components[ 5 ] ),
                Boolean.class.cast( components[ 6 ] ),
                LocalDate.class.cast( components[ 7 ] ),
                LocalDate.class.cast( components[ 8 ] )
        );
    }

}
