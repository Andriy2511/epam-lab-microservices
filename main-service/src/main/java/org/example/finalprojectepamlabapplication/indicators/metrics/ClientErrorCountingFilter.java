package org.example.finalprojectepamlabapplication.indicators.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ClientErrorCountingFilter implements Filter {

    private final Counter clientErrorCounter;

    public ClientErrorCountingFilter(MeterRegistry meterRegistry) {
        this.clientErrorCounter = Counter.builder("custom.client.errors.total")
                .description("Total number of HTTP 4xx responses")
                .register(meterRegistry);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        chain.doFilter(request, response);

        if (response instanceof HttpServletResponse) {
            int status = ((HttpServletResponse) response).getStatus();
            if (status >= 400 && status < 500) {
                clientErrorCounter.increment();
            }
        }
    }
}
