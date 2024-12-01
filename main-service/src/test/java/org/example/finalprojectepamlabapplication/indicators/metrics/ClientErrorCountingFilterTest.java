package org.example.finalprojectepamlabapplication.indicators.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

class ClientErrorCountingFilterTest {

    private SimpleMeterRegistry meterRegistry;
    private ClientErrorCountingFilter clientErrorCountingFilter;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        clientErrorCountingFilter = new ClientErrorCountingFilter(meterRegistry);
    }

    @Test
    void testFilterWhenStatusIsOK() throws IOException, ServletException {
        ServletRequest request = mock(ServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(response.getStatus()).thenReturn(200);

        clientErrorCountingFilter.doFilter(request, response, chain);

        Counter counter = meterRegistry.find("custom.client.errors.total").counter();
        Assertions.assertNotNull(counter);
        Assertions.assertEquals(0, counter.count());
    }

    @Test
    void testFilterWhenStatusIsBadRequest() throws IOException, ServletException {
        ServletRequest request = mock(ServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(response.getStatus()).thenReturn(400);

        clientErrorCountingFilter.doFilter(request, response, chain);

        Counter counter = meterRegistry.find("custom.client.errors.total").counter();
        Assertions.assertNotNull(counter);
        Assertions.assertEquals(1, counter.count());
    }
}
