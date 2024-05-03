/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.github.jristretto.inmemorydao;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Pieter van den Hombergh {@code <pieter.van.den.hombergh@gmail.com>}
 */
final class SerialIntegerGenerator implements SerialGenerator<Integer> {

    private AtomicInteger value = new AtomicInteger();

    public SerialIntegerGenerator() {
    }

    int nextValue() {
        return value.incrementAndGet();
    }

    void presetTo(int v) {
        value.set( v );
    }

    @Override
    public Integer get() {
        return nextValue();
    }

    @Override
    public void accept(Integer t) {
        presetTo( Math.max( value.get(), t.intValue() ) );
    }

}
