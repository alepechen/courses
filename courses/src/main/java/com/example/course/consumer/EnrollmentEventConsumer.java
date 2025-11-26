package com.example.course.consumer;

import com.example.course.dto.CourseEnrollmentEventDto;
import com.example.course.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class EnrollmentEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(EnrollmentEventConsumer.class);

    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    public EnrollmentEventConsumer(EmailService emailService, ObjectMapper objectMapper) {
        this.emailService = emailService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = "${spring.kafka.topic.course-enrollment}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeEnrollmentEvent(
            @Payload String message,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {
        try {
            CourseEnrollmentEventDto event = objectMapper.readValue(message, CourseEnrollmentEventDto.class);
            emailService.sendEnrollmentConfirmationEmail(event);
            logger.info("Successfully processed enrollment event for user: {}", event.getUserEmail());
        } catch (Exception e) {
            logger.error("Error processing enrollment event: {}", message, e);
        }
    }
}
