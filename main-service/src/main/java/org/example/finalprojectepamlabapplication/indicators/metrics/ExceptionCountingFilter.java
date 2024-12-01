package org.example.finalprojectepamlabapplication.indicators.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import jakarta.servlet.*;
import java.io.IOException;

@Component
public class ExceptionCountingFilter implements Filter {

    private final Counter exceptionCounter;

    public ExceptionCountingFilter(MeterRegistry meterRegistry) {
        this.exceptionCounter = Counter.builder("custom.exceptions.total")
                .description("Total number of exceptions")
                .register(meterRegistry);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (Exception ex) {
            exceptionCounter.increment();
            throw ex;
        }
    }
}
