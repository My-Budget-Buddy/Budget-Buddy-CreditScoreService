package com.skillstorm.taxdemo;

//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

//import com.skillstorm.taxdemo.services.TaxCalculatorService;

//import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TaxDemoApplicationTests {

    /*@Autowired
    private TaxCalculatorService taxCalculatorService;

    @Test
    void testSingleFiler1() {
        double taxableIncome = taxCalculatorService.calculateSingleFilerTax(50000);
        assertEquals(6307.59, taxableIncome, 0.01);
    }

    @Test
    void testMarriedFiler1() {
        double taxableIncome = taxCalculatorService.calculateMarriedFilerTax(75000);
        assertEquals(8560.00, taxableIncome, 0.01);
    }

    @Test
    void testSingleFiler2() {
        double taxableIncome = taxCalculatorService.calculateSingleFilerTax(749374);
        assertEquals(237600.98, taxableIncome, 0.01);
    }

    @Test
    void testMarriedFiler2() {
        double taxableIncome = taxCalculatorService.calculateMarriedFilerTax(47000);
        assertEquals(5200.00, taxableIncome, 0.01);
    }

    @Test
    void testSingleFiler3() {
        double taxableIncome = taxCalculatorService.calculateSingleFilerTax(89373);
        assertEquals(14969.66, taxableIncome, 0.01);
    }

    @Test
    void testMarriedFiler3() {
        double taxableIncome = taxCalculatorService.calculateMarriedFilerTax(653748);
        assertEquals(172601.30, taxableIncome, 0.01);
    }

    @Test
    void testSingleFiler4() {
        double taxableIncome = taxCalculatorService.calculateSingleFilerTax(64029);
        assertEquals(9393.98, taxableIncome, 0.01);
    }

    @Test
    void testMarriedFiler4() {
        double taxableIncome = taxCalculatorService.calculateMarriedFilerTax(849232);
        assertEquals(244130.44, taxableIncome, 0.01);
    }

    @Test
    void testSingleFiler5() {
        double taxableIncome = taxCalculatorService.calculateSingleFilerTax(74937);
        assertEquals(11793.74, taxableIncome, 0.01);
    }

    @Test
    void testMarriedFiler5() {
        double taxableIncome = taxCalculatorService.calculateMarriedFilerTax(8393);
        assertEquals(839.30, taxableIncome, 0.01);
    }

    // Add more test cases as needed*/
}
