package io.github.jristretto.dao;

import io.github.jristretto.mappers.AbstractMapper;
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

}
