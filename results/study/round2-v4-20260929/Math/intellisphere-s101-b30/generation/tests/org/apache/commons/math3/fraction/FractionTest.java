package org.apache.commons.math3.fraction;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Test;

import static org.junit.Assert.*;

public class FractionTest {

    @Test
    public void testConstructorReducesAndNormalizesSign() {
        Fraction f = new Fraction(-2, -4);
        assertEquals(1, f.getNumerator());
        assertEquals(2, f.getDenominator());
        assertEquals("1 / 2", f.toString());
    }

    @Test
    public void testZeroNumeratorKeepsDenominatorPositiveAfterReduction() {
        Fraction f = new Fraction(0, -5);
        assertEquals(0, f.getNumerator());
        assertEquals(1, f.getDenominator());
        assertEquals("0", f.toString());
    }

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    
}
