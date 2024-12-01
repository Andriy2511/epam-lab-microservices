package org.example.finalprojectepamlabapplication.indicators.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

class ExceptionCountingFilterTest {

    private SimpleMeterRegistry meterRegistry;
    private ExceptionCountingFilter exceptionCountingFilter;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        exceptionCountingFilter = new ExceptionCountingFilter(meterRegistry);
    }

    @Test
    void testFilterWithoutException() throws IOException, ServletException {
        ServletRequest request = mock(ServletRequest.class);
        ServletResponse response = mock(ServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        exceptionCountingFilter.doFilter(request, response, chain);

        Counter counter = meterRegistry.find("custom.exceptions.total").counter();
        Assertions.assertNotNull(counter);
        Assertions.assertEquals(0, counter.count());
    }

    @Test
    void testFilterWithException() throws IOException, ServletException {
        ServletRequest request = mock(ServletRequest.class);
        ServletResponse response = mock(ServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        doThrow(new RuntimeException()).when(chain).doFilter(request, response);

        try {
            exceptionCountingFilter.doFilter(request, response, chain);
        } catch (RuntimeException e) {}


        Counter counter = meterRegistry.find("custom.exceptions.total").counter();
        Assertions.assertNotNull(counter);
        Assertions.assertEquals(1, counter.count());
    }
}
