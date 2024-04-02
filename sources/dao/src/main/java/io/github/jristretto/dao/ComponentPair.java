package io.github.jristretto.dao;

import java.lang.reflect.RecordComponent;

/**
 * Simple key object pair to help mappings. Candidate record from Java
 * {@code >= 16}.
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public record ComponentPair(RecordComponent component, Object value) {

}
