package com.ballotbuddy.config;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import static org.junit.jupiter.api.Assertions.*;

class PerformanceConfigTest {

    @Test
    void shallowEtagHeaderFilter_BeanCreation() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(PerformanceConfig.class)) {
            assertTrue(context.containsBean("shallowEtagHeaderFilter"));
            assertNotNull(context.getBean(ShallowEtagHeaderFilter.class));
        }
    }
}
