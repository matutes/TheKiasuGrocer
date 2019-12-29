package com.raimikarim.thekiasushopper;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class CalculatorTest {

    @Test
    public void integerInputs() {
        Calculator calculator = new Calculator(
                "1","2","3", "4");
        boolean isDouble = calculator.isAllDouble();
        assertThat(isDouble, is(equalTo(true)));
    }

    @Test
    public void mixedInputs() {
        Calculator calculator = new Calculator(
                "1.2","2","3.3", "4");
        boolean isDouble = calculator.isAllDouble();
        assertThat(isDouble, is(equalTo(true)));
    }

    @Test
    public void emptyInputs() {
        Calculator calculator = new Calculator(
                "","2","", "4");
        boolean isDouble = calculator.isAllDouble();
        assertThat(isDouble, is(equalTo(false)));
    }

    @Test
    public void whitespaceOnlyInputs() {
        Calculator calculator = new Calculator(
                " ","2","3", "4");
        boolean isDouble = calculator.isAllDouble();
        assertThat(isDouble, is(equalTo(false)));
    }

    @Test
    public void whitespaceDoubleInputs() {
        Calculator calculator = new Calculator(
                "2.0 ","2","6", "4");
        boolean isDouble = calculator.isAllDouble();
        assertThat(isDouble, is(equalTo(true)));
    }

    @Test
    public void parseStrings() {
        Calculator calculator = new Calculator(
                "2","2","8", "4");
        if (calculator.isAllDouble()) {
            calculator.parseStrings();
        }
        calculator.calculateRates();
        String rateA = calculator.getRateARounded();
        assertThat(rateA, is(equalTo("1.0")));
    }

    @Test
    public void parseStringsWithWhitespace() {
        Calculator calculator = new Calculator(
                "2 ","2","8", "4");
        if (calculator.isAllDouble()) {
            calculator.parseStrings();
        }
        calculator.calculateRates();
        String rateA = calculator.getRateARounded();
        assertThat(rateA, is(equalTo("1.0")));
    }

    @Test
    public void divideByZero() {
        Calculator calculator = new Calculator(
                "2","0","8", "0");
        boolean allValid = calculator.isAllValid();
        assertThat(allValid, is(equalTo(false)));
    }
}