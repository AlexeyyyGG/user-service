package com.example.userservice.notification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import com.example.userservice.common.dto.UserEvent;
import com.example.userservice.common.dto.UserEvent.EventType;
import com.example.userservice.notification.mail.MailService;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = "user-events",
        brokerProperties = {"listeners=PLAINTEXT://localhost:0", "port=0"})
class NotificationKafkaIntegrationTest {
    @Autowired
    private KafkaTemplate<String, UserEvent> kafkaTemplate;
    @MockitoBean
    private MailService mailService;

    @Test
    void shouldCallMailServiceWhenKafkaEventReceived() throws Exception {
        UserEvent event = new UserEvent("kafkauser@mail.ru", EventType.CREATE);
        kafkaTemplate.send("user-events", event.email(), event).get(10, TimeUnit.SECONDS);
        ArgumentCaptor<UserEvent> captor = ArgumentCaptor.forClass(UserEvent.class);
        verify(mailService, timeout(10000).times(1)).sendEmail(captor.capture());
        UserEvent receivedEvent = captor.getValue();
        assertEquals("kafkauser@mail.ru", receivedEvent.email());
        assertEquals(EventType.CREATE, receivedEvent.type());
    }
}