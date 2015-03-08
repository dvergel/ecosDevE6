/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ecos.ecosdeve6.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Dev
 */
public class CalcularSimpsonRuleTest extends TestCase{

    /**
     *
     */
    public CalcularSimpsonRuleTest() {
    }

    /**
     * Test of testCalcularValoresDefecto method, of class CalcularSimpsonRule.
     * @throws Exception 
     */
    public void testCalcularValoresDefecto() throws Exception {
        BigDecimal expResult;
        CalcularSimpsonRule ejercicio = new CalcularSimpsonRule(new BigDecimal(0.00001).setScale(5, RoundingMode.HALF_UP), new BigDecimal(1).setScale(0, RoundingMode.HALF_UP), new BigDecimal(10).setScale(0, RoundingMode.HALF_UP), new BigDecimal(6).setScale(0, RoundingMode.HALF_UP), new BigDecimal(0.20).setScale(2, RoundingMode.HALF_UP));
        ejercicio.proyectarX();
        expResult =new BigDecimal(0.55338).setScale(5, RoundingMode.HALF_UP);
        assertEquals(expResult, ejercicio.getX().setScale(5, RoundingMode.HALF_UP));
        ejercicio = new CalcularSimpsonRule(new BigDecimal(0.00001).setScale(5, RoundingMode.HALF_UP), new BigDecimal(1).setScale(0, RoundingMode.HALF_UP), new BigDecimal(10).setScale(0, RoundingMode.HALF_UP), new BigDecimal(15).setScale(0, RoundingMode.HALF_UP), new BigDecimal(0.45).setScale(2, RoundingMode.HALF_UP));
        ejercicio.proyectarX();
        expResult =new BigDecimal(1.75305).setScale(5, RoundingMode.HALF_UP);
        assertEquals(expResult, ejercicio.getX().setScale(5, RoundingMode.HALF_UP));
        ejercicio = new CalcularSimpsonRule(new BigDecimal(0.00001).setScale(5, RoundingMode.HALF_UP), new BigDecimal(1).setScale(0, RoundingMode.HALF_UP), new BigDecimal(10).setScale(0, RoundingMode.HALF_UP), new BigDecimal(4).setScale(0, RoundingMode.HALF_UP), new BigDecimal(0.495).setScale(3, RoundingMode.HALF_UP));
        ejercicio.proyectarX();
        expResult =new BigDecimal(4.60352).setScale(5, RoundingMode.HALF_UP);
        assertEquals(expResult, ejercicio.getX().setScale(5, RoundingMode.HALF_UP)); 
    }
}
