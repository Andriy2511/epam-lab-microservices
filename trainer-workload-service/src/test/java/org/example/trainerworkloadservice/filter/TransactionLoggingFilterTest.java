package org.example.trainerworkloadservice.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

import java.util.Enumeration;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionLoggingFilterTest {

    private TransactionLoggingFilter transactionLoggingFilter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        transactionLoggingFilter = new TransactionLoggingFilter();
    }

    @Test
    void testFilterLogsTransactionDetails() throws Exception {
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/test");
        when(request.getQueryString()).thenReturn("param1=value1");
        when(request.getAttributeNames()).thenReturn(new Enumeration<>() {
            private final String[] elements = {};
            private int index = 0;

            @Override
            public boolean hasMoreElements() {
                return index < elements.length;
            }

            @Override
            public String nextElement() {
                return elements[index++];
            }
        });

        LogCaptor logCaptor = LogCaptor.forClass(TransactionLoggingFilter.class);

        transactionLoggingFilter.doFilterInternal(request, response, filterChain);

        assertTrue(logCaptor.getInfoLogs().stream()
                .anyMatch(log -> log.contains("Transaction id is")));
        assertTrue(logCaptor.getInfoLogs().stream()
                .anyMatch(log -> log.contains("Request: Method is GET, Endpoint is /api/test, Params: param1=value1")));

        assertNull(MDC.get("transactionId"));

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testFilterHandlesException() throws Exception {
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/api/error");

        doThrow(new RuntimeException("Test exception")).when(filterChain).doFilter(request, response);

        LogCaptor logCaptor = LogCaptor.forClass(TransactionLoggingFilter.class);

        transactionLoggingFilter.doFilterInternal(request, response, filterChain);

        assertTrue(logCaptor.getErrorLogs().stream()
                .anyMatch(log -> log.contains("An error occurred: Test exception")));
        assertNull(MDC.get("transactionId"));
    }
}
