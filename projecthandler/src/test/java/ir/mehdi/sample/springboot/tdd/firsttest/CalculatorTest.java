package ir.mehdi.sample.springboot.tdd.firsttest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CalculatorTest {

    @Test
    void testDivideTwoPositiveNumbers() {
        //arrange
        Calculator calculator = new Calculator();

        //act
        double result = calculator.divide(6, 2);

        //assert
        assertEquals(3.0, result);
    }
    @Test
    void testDivideByZero() {
        Calculator calculator = new Calculator();
        assertThrows(ArithmeticException.class, () -> calculator.divide(6, 0));
    }
    @Test
    void testDivideTwoNegativeNumbers() {
        //arrange
        Calculator calculator = new Calculator();

        //act
        double result = calculator.divide(-6, -2);

        //assert
        assertEquals(3.0, result);
    }
}
