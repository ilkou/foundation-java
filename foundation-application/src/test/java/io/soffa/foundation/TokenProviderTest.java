package io.soffa.foundation;

import com.google.common.collect.ImmutableMap;
import io.soffa.foundation.core.models.Authentication;
import io.soffa.foundation.core.security.DefaultTokenProvider;
import io.soffa.foundation.core.security.TokenProvider;
import io.soffa.foundation.core.security.model.TokensConfig;
import io.soffa.foundation.models.TokenType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TokenProviderTest {

    @Test
    public void testTokenProvider() {
        TokensConfig config = new TokensConfig("soffa", "XoEH&!TWrQ&T");
        TokenProvider tokenProvider = new DefaultTokenProvider(config);
        String token = tokenProvider.create(TokenType.JWT, "agent", ImmutableMap.of("liveMode", true)).getValue();
        Authentication auth = tokenProvider.decode(token);
        assertNotNull(auth);
        assertTrue(auth.isLiveMode());
    }

}
