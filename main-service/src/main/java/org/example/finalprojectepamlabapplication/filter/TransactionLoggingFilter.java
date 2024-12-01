package org.example.finalprojectepamlabapplication.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Enumeration;
import java.util.UUID;

@Slf4j
@Component
public class TransactionLoggingFilter extends OncePerRequestFilter {

    private final String TRANSACTION_ID = "transactionId";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) {

        String transactionId = UUID.randomUUID().toString();
        MDC.put(TRANSACTION_ID, transactionId);

        try {
            log.info("Transaction id is {}", MDC.get(TRANSACTION_ID));
            getRequestDetails(request);

            filterChain.doFilter(request, response);

            getResponseDetails(response);
        } catch (Exception e) {
            getErrorMessage(e);
        } finally {
            MDC.remove(TRANSACTION_ID);
        }
    }

    private void getRequestDetails(HttpServletRequest request) {
        String method = request.getMethod();
        String endpoint = request.getRequestURI();
        String params = request.getQueryString();
        String attributes = getAttributes(request);

        log.info("Request: Method is {}, Endpoint is {}, Params: {}, Attributes: {}",
                method, endpoint, params, attributes);
    }

    private String getAttributes(HttpServletRequest request) {
        Enumeration<String> attributeNames = request.getAttributeNames();
        StringBuilder attributes = new StringBuilder();

        while (attributeNames != null && attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            Object attributeValue = request.getAttribute(attributeName);
            attributes.append(attributeName).append("=").append(attributeValue).append("; ");
        }
        return attributes.toString();
    }

    private void getResponseDetails(HttpServletResponse response) {
        int status = response.getStatus();
        log.info("Transaction {} has been finished. Response: Status={}", MDC.get(TRANSACTION_ID), status);
    }

    private void getErrorMessage(Exception e) {
        log.error("An error occurred: {}", e.getMessage(), e);
    }
}
