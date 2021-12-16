package io.soffa.foundation;

import io.soffa.foundation.config.TestPubSubListener;
import io.soffa.foundation.events.Event;
import io.soffa.foundation.exceptions.TechnicalException;
import io.soffa.foundation.pubsub.PubSubClient;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest(properties = {
    "app.amqp.enabled=true",
    "app.amqp.clients.t1=amqp://guest:guest@localhost:5672",
})
public class RabbitMQTest {

    @Autowired
    private PubSubClient pubSubClient;

    @SneakyThrows
    @Test
    public void testRabbitMQ() {
        Assertions.assertNotNull(pubSubClient);
        pubSubClient.sendInternal(new Event("HELLO"));
        pubSubClient.sendInternal(new Event("HELLO1"));
        Thread.sleep(1000 * 2);
        assertEquals(1, TestPubSubListener.TICK.intValue());

        Assertions.assertThrowsExactly(TechnicalException.class, () -> {
            pubSubClient.send("t1", "exchange1", "routing1", new Event("HELLO2"));
        });

    }

}
