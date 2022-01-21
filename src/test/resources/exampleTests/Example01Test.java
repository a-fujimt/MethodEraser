package exampleTests;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Example01Test {

    @Test
    public void testPlus() {
        int input = 3;
        int result = closeToZero(input);
        assertEquals(result, 2);
    }

    @Test
    public void testMinus() {
        int input = -5;
        int result = closeToZero(input);
        assertEquals(result, -4);
    }

    @Test
    public void testZero() {
        int input = 0;
        int result = closeToZero(input);
        assertEquals(result, 0);
    }

}