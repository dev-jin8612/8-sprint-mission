package com.sprint.mission.discodeit.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class MDCLoggingInterceptor implements HandlerInterceptor {

  public static final String HEADER_REQUEST_ID = "Discodeit-Request-ID";

  // MDC Key (logback pattern에서 그대로 사용)
  public static final String MDC_REQUEST_ID = "requestId";
  public static final String MDC_METHOD = "method";
  public static final String MDC_URL = "url";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    String requestId = UUID.randomUUID().toString();

    // MDC 세팅
    MDC.put(MDC_REQUEST_ID, requestId);
    MDC.put(MDC_METHOD, request.getMethod());
    MDC.put(MDC_URL, request.getRequestURI());

    // 응답 헤더에 포함
    response.setHeader(HEADER_REQUEST_ID, requestId);

    return true;
  }

  @Override
  public void afterCompletion(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      Exception ex
  ) {
    // ThreadLocal 누수 방지 (중요)
    MDC.clear();
  }
}

