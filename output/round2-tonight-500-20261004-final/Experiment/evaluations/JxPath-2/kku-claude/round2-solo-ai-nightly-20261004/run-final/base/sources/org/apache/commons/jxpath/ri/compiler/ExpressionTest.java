package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Iterator;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class ExpressionTest {

    private static class CountingExpression extends Expression {
        int computeCount = 0;
        boolean[] results;
        int resultIndex = 0;
        boolean throwOnCompute = false;

        CountingExpression(boolean... results) {
            this.results = results;
        }

        public boolean computeContextDependent() {
            computeCount++;
            if (throwOnCompute) {
                throw new RuntimeException("boom");
            }
            boolean r = results[resultIndex];
            if (resultIndex < results.length - 1) {
                resultIndex++;
            }
            return r;
        }

        public Object computeValue(EvalContext context) {
            return "value";
        }

        public Object compute(EvalContext context) {
            return "computed";
        }
    }

    @Test
    public void testIsContextDependentCachesResultTrue() {
        CountingExpression expr = new CountingExpression(true, false);
        boolean first = expr.isContextDependent();
        boolean second = expr.isContextDependent();
        assertTrue(first);
        assertTrue(second);
        assertEquals(1, expr.computeCount);
    }

    @Test
    public void testIsContextDependentCachesResultFalse() {
        CountingExpression expr = new CountingExpression(false, true);
        boolean first = expr.isContextDependent();
        boolean second = expr.isContextDependent();
        boolean third = expr.isContextDependent();
        assertFalse(first);
        assertFalse(second);
        assertFalse(third);
        assertEquals(1, expr.computeCount);
    }

    @Test
    public void testLazyEvaluationBeforeFirstCall() {
        CountingExpression expr = new CountingExpression(true);
        assertEquals(0, expr.computeCount);
        expr.isContextDependent();
        assertEquals(1, expr.computeCount);
    }

    @Test
    public void testIndependentInstancesCacheSeparately() {
        CountingExpression expr1 = new CountingExpression(true);
        CountingExpression expr2 = new CountingExpression(false);

        boolean r1 = expr1.isContextDependent();
        boolean r2 = expr2.isContextDependent();

        assertTrue(r1);
        assertFalse(r2);
        assertEquals(1, expr1.computeCount);
        assertEquals(1, expr2.computeCount);
    }

    @Test
    public void testComputeAndComputeValueDelegation() {
        CountingExpression expr = new CountingExpression(true);
        assertEquals("computed", expr.compute(null));
        assertEquals("value", expr.computeValue(null));
    }

    @Test
    public void testStaticConstantsHaveExpectedValues() throws Exception {
        Field zeroField = Expression.class.getDeclaredField("ZERO");
        zeroField.setAccessible(true);
        Double zero = (Double) zeroField.get(null);
        assertEquals(0.0, zero.doubleValue(), 0.0);
        assertTrue(Modifier.isStatic(zeroField.getModifiers()));

        Field oneField = Expression.class.getDeclaredField("ONE");
        oneField.setAccessible(true);
        Double one = (Double) oneField.get(null);
        assertEquals(1.0, one.doubleValue(), 0.0);

        Field nanField = Expression.class.getDeclaredField("NOT_A_NUMBER");
        nanField.setAccessible(true);
        Double nan = (Double) nanField.get(null);
        assertTrue(Double.isNaN(nan.doubleValue()));
    }

    @Test
    public void testValueIteratorUnwrapsPointerAndRemoveThrows() {
        java.util.List<Object> list = new java.util.ArrayList<Object>();
        list.add("plain");
        Iterator it = list.iterator();
        Expression.ValueIterator valueIterator = new Expression.ValueIterator(it);
        assertTrue(valueIterator.hasNext());
        assertEquals("plain", valueIterator.next());
        try {
            valueIterator.remove();
            org.junit.Assert.fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
}
