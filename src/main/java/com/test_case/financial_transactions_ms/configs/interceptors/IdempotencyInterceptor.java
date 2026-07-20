package com.test_case.financial_transactions_ms.configs.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.time.Duration;

@Slf4j
@Component
public class IdempotencyInterceptor implements HandlerInterceptor {

    private static final int IDEMPOTENCY_DURATION_IN_SECONDS = 5;
    private static final String IDEMPOTENCY_KEY = "Idempotency-Key";
    private final RedisTemplate<Object, Object> redisTemplate;

    public IdempotencyInterceptor(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response, Object handler) throws IOException {

        if (!HttpMethod.POST.toString().equals(request.getMethod())) {
            return true;
        }

        String idempotencyKey = request.getHeader(IDEMPOTENCY_KEY);

        if (idempotencyKey == null) {
            throw new IllegalArgumentException("Missing Idempotency-Key" );
        }

        String redisKey = IDEMPOTENCY_KEY + ":" + idempotencyKey;

        try {
            Object previousResponse = redisTemplate.opsForValue().get(redisKey);

            if (previousResponse != null) {
                response.setStatus(HttpStatus.OK.value());
                response.getWriter().write(previousResponse.toString());

                return false;
            }

            Boolean lock =
                    redisTemplate.opsForValue()
                            .setIfAbsent(
                                    redisKey + ":lock",
                                    "processing",
                                    Duration.ofSeconds(IDEMPOTENCY_DURATION_IN_SECONDS));

            if (Boolean.FALSE.equals(lock)) {
                response.sendError(HttpStatus.CONFLICT.value(), "Request already being processed");
                return false;
            }

        } catch (Exception ex) {
            // try/catch to workaround redis unavailability - only for testing purposes.
            log.warn("Error occurred while processing idempotency", ex);
        }

        return true;
    }
}