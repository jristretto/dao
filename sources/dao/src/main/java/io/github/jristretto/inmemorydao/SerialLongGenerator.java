/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.github.jristretto.inmemorydao;

import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author Pieter van den Hombergh {@code <pieter.van.den.hombergh@gmail.com>}
 */
final class SerialLongGenerator implements SerialGenerator<Long> {

    private AtomicLong value = new AtomicLong();

    public SerialLongGenerator() {
    }

    long nextValue() {
        return value.incrementAndGet();
    }

    void presetTo(long v) {
        value.set( v );
    }

    @Override
    public Long get() {
        return nextValue();
    }

    @Override
    public void accept(Long t) {
        presetTo( Math.max( value.get(), t ) );
    }

}
