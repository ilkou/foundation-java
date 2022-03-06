package io.soffa.foundation;

import io.soffa.foundation.commons.HttpStatus;
import io.soffa.foundation.errors.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExceptionsTests {

    private static final String ERROR = "error";

    @Test
    public void testExceptions() {
        assertEquals(HttpStatus.SERVER_ERROR, ErrorUtil.resolveErrorCode(new ConfigurationException(ERROR)));
        assertEquals(HttpStatus.SERVER_ERROR, ErrorUtil.resolveErrorCode(new ConfigurationException(ERROR, (Throwable) null)));

        assertEquals(HttpStatus.CONFLICT, ErrorUtil.resolveErrorCode(new ConflictException(ERROR)));
        assertEquals(HttpStatus.CONFLICT, ErrorUtil.resolveErrorCode(new ConflictException(ERROR, (Throwable) null)));

        assertEquals(HttpStatus.SERVER_ERROR, ErrorUtil.resolveErrorCode(new DatabaseException(ERROR)));
        assertEquals(HttpStatus.SERVER_ERROR, ErrorUtil.resolveErrorCode(new DatabaseException(ERROR, (Throwable) null)));

        assertEquals(-1, ErrorUtil.resolveErrorCode(new FakeException(ERROR)));
        assertEquals(HttpStatus.FORBIDDEN, ErrorUtil.resolveErrorCode(new ForbiddenException(ERROR)));
        assertEquals(HttpStatus.BAD_REQUEST, ErrorUtil.resolveErrorCode(new FunctionalException(ERROR)));
        assertEquals(HttpStatus.UNAUTHORIZED, ErrorUtil.resolveErrorCode(new InvalidAuthException(ERROR)));
        assertEquals(HttpStatus.BAD_REQUEST, ErrorUtil.resolveErrorCode(new InvalidTenantException(ERROR)));
        assertEquals(HttpStatus.UNAUTHORIZED, ErrorUtil.resolveErrorCode(new InvalidTokenException(ERROR)));
        assertEquals(HttpStatus.NO_CONTENT, ErrorUtil.resolveErrorCode(new NoContentException(ERROR)));
        assertEquals(HttpStatus.NOT_IMLEMENTED, ErrorUtil.resolveErrorCode(new NotImplementedException(ERROR)));
        assertEquals(HttpStatus.EXPECTATION_FAILED, ErrorUtil.resolveErrorCode(new RequirementException(ERROR)));
        assertEquals(HttpStatus.NOT_FOUND, ErrorUtil.resolveErrorCode(new ResourceNotFoundException(ERROR)));
        assertEquals(HttpStatus.BAD_REQUEST, ErrorUtil.resolveErrorCode(new RetryException(ERROR)));
        assertEquals(HttpStatus.SERVER_ERROR, ErrorUtil.resolveErrorCode(new TechnicalException(ERROR)));
        assertEquals(HttpStatus.REQUEST_TIMEOUT, ErrorUtil.resolveErrorCode(new TimeoutException(ERROR)));
        assertEquals(HttpStatus.NOT_IMLEMENTED, ErrorUtil.resolveErrorCode(new TodoException(ERROR)));
        assertEquals(HttpStatus.UNAUTHORIZED, ErrorUtil.resolveErrorCode(new UnauthorizedException(ERROR)));
        assertEquals(HttpStatus.BAD_REQUEST, ErrorUtil.resolveErrorCode(new ValidationException(ERROR)));
    }


}
