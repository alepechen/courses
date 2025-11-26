package com.example.course.producer;

import com.example.course.dto.CourseEnrollmentEventDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EnrollmentEventProducer {

    private static final Logger logger = LoggerFactory.getLogger(EnrollmentEventProducer.class);
    private final KafkaTemplate<String, CourseEnrollmentEventDto> kafkaTemplate;
    @Value("${spring.kafka.topic.course-enrollment}")
    private String topic;

    public void sendEnrollmentEvent(CourseEnrollmentEventDto event) {
        kafkaTemplate.send(topic, event.getEnrollmentId(), event)
                .addCallback(
                        result -> {
                            logger.info("Event sent successfully - partition: {}, offset: {}",
                                    result.getRecordMetadata().partition(),
                                    result.getRecordMetadata().offset());
                        },
                        ex -> {
                            logger.error("Failed to send event for enrollment: {}", event.getEnrollmentId(), ex);
                        }
                );
    }

}
