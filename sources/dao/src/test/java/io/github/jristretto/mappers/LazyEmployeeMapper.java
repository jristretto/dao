package io.github.jristretto.mappers;

import io.github.jristretto.mappers.AbstractMapper;
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
}
