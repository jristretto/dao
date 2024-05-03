/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package io.github.jristretto.inmemorydao;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 *
 * @author Pieter van den Hombergh {@code <pieter.van.den.hombergh@gmail.com>}
 */
interface SerialGenerator<S extends Number> extends Supplier<S>, Consumer<S> {
}
