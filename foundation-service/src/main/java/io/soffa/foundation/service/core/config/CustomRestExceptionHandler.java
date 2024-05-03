package io.soffa.foundation.service.core.config;

import io.soffa.foundation.commons.Logger;
import io.soffa.foundation.core.context.RequestContextHolder;
import io.soffa.foundation.errors.ErrorUtil;
import io.soffa.foundation.errors.FunctionalException;
import io.soffa.foundation.errors.TechnicalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = Logger.get(CustomRestExceptionHandler.class);
    private final Environment environment;

    @Autowired
    public CustomRestExceptionHandler(Environment environment) {
        super();
        this.environment = environment;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handleGlobalErrors(ex);
    }


    @ExceptionHandler({UndeclaredThrowableException.class, Throwable.class, Exception.class, TechnicalException.class, FunctionalException.class})
    public ResponseEntity<Object> handleGlobalErrors(Throwable ex) {
        boolean isProduction = environment.acceptsProfiles(Profiles.of("prod", "production"));
        Throwable error = ErrorUtil.unwrap(ex);
        HttpStatusCode status = deriveStatus(error);
        String message = ErrorUtil.loookupOriginalMessage(error);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("source", environment.getRequiredProperty("spring.application.name"));
        body.put("kind", error.getClass().getSimpleName());
        body.put("status", status.value());
        body.put("message", message);
        body.put("prod", isProduction);

        RequestContextHolder.get().ifPresent(context -> {
            body.put("traceId", context.getTraceId());
            body.put("spanId", context.getSpanId());
            Optional.ofNullable(context.getApplicationName()).ifPresent(s -> body.put("application", s));
            context.getUsername().ifPresent(s -> body.put("user", s));
            if (context.hasTenant()) {
                body.put("tenant", context.getTenantId());
            }
        });

        if (status.value() >= HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            LOG.error(error);
        }else {
            LOG.error(error.getMessage());
        }
        if (!isProduction && status != HttpStatus.UNAUTHORIZED && status != HttpStatus.FORBIDDEN) {
            body.put("trace", ErrorUtil.getStacktrace(error).split("\n"));
        }

        return ResponseEntity.status(status).body(body);
    }

    private HttpStatusCode deriveStatus(Throwable exception) {
        int code = ErrorUtil.resolveErrorCode(exception);
        if (code > -1) {
            return HttpStatus.valueOf(code);
        }

        if (exception instanceof AccessDeniedException) {
            if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
                return HttpStatus.FORBIDDEN;
            }
            return HttpStatus.UNAUTHORIZED;
        }
        if (exception instanceof MethodArgumentNotValidException) {
            return HttpStatus.BAD_REQUEST;
        }
        if (exception instanceof FunctionalException) {
            return HttpStatus.NOT_IMPLEMENTED;
        }
        if (exception instanceof ResponseStatusException) {
            return ((ResponseStatusException) exception).getStatusCode();
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }


}
