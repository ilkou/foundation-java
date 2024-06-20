package com.company.app;

import io.soffa.foundation.core.pubsub.PubSubMessenger;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(properties = {"app.pubsub.enabled=false"})
class NoPubSubMessengerTest {

    @Autowired(required = false)
    private PubSubMessenger messenger;

    @SneakyThrows
    @Test
    void testConfiguration() {
        Assertions.assertNull(messenger);
    }

}
