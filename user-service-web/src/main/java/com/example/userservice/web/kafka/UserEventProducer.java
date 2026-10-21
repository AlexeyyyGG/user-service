package com.example.userservice.web.kafka;

import com.example.userservice.common.dto.UserEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserEventProducer {
    private static final Logger log = LoggerFactory.getLogger(UserEventProducer.class);
    private static final String TOPIC = "user-events";
    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    public UserEventProducer(KafkaTemplate<String, UserEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(UserEvent event) {
        try {
            kafkaTemplate.send(TOPIC, event.email(), event)
                    .whenComplete((result, exception) -> {
                        if (exception == null) {
                            log.info("Событие {} успешно отправлено. Offset: {}", event.type(),
                                    result.getRecordMetadata().offset());
                        } else {
                            log.error("Ошибка при отправке события {}", event, exception);
                        }
                    });
        } catch (Exception exception) {
            log.error("Не удалось отправить событие {}", event, exception);
        }
    }
}