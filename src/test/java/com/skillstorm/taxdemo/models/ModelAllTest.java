package com.skillstorm.taxdemo.models;

import org.apache.commons.math.stat.descriptive.summary.Product;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;
import org.meanbean.test.BeanVerifier;

public class ModelAllTest {

    @Test
    public void ModelsAndDtoAllTest() {

        final Class<?>[] domainClasses = { CreditAccount.class, CreditScoreHistory.class, UserCreditData.class };
        BeanTester tester = new BeanTester();
        for (Class<?> domainClass : domainClasses) {
            // This method will test setter, getters, and constructor for listed classes
            tester.testBean(domainClass);
        }
    }

}
