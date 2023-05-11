package org.victorpiles.escacs.engine.util;

import org.junit.jupiter.api.Test;
import org.victorpiles.escacs.engine.util.notation.NotationConverter;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NotationConverterTest {

    @Test
    void notationToIndex() {
        assertEquals(0, NotationConverter.notationToIndex("a8"));
        assertEquals(1, NotationConverter.notationToIndex("b8"));
        assertEquals(63, NotationConverter.notationToIndex("h1"));
    }

    @Test
    void indexToNotation() {
        assertEquals("a8", NotationConverter.indexToNotation(0));
        assertEquals("b8", NotationConverter.indexToNotation(1));
        assertEquals("h1", NotationConverter.indexToNotation(63));
    }
}