package com.example.course.service;

import com.example.course.dao.OutboxEventRepository;
import com.example.course.dao.entity.OutboxEvent;
import com.example.course.dto.CourseEnrollmentEventDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OutboxService {

    private static final Logger logger = LoggerFactory.getLogger(OutboxService.class);
    private static final String EVENT_TYPE_ENROLLMENT = "CourseEnrollment";

    private final OutboxEventRepository outboxRepository;
    private final ObjectMapper objectMapper;


    @Transactional(propagation = Propagation.MANDATORY)
    public OutboxEvent saveEnrollmentEvent(CourseEnrollmentEventDto event) {
        try {
            String eventId = event.getEnrollmentId() != null
                    ? event.getEnrollmentId()
                    : UUID.randomUUID().toString();

            String payload = objectMapper.writeValueAsString(event);
            OutboxEvent outboxEvent = OutboxEvent.builder()
                    .eventId(eventId)
                    .eventType(EVENT_TYPE_ENROLLMENT)
                    .payload(payload)
                    .published(false)
                    .createdAt(LocalDateTime.now())
                    .build();

            OutboxEvent saved = outboxRepository.save(outboxEvent);
            logger.info("Saved enrollment event to outbox: eventId={}, id={}",
                    eventId, saved.getId());

            return saved;

        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize event to JSON", e);
            throw new RuntimeException("Failed to save event to outbox", e);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markAsPublished(Long outboxEventId) {
        outboxRepository.findById(outboxEventId).ifPresent(event -> {
            event.markAsPublished();
            outboxRepository.save(event);
            logger.debug("Marked outbox event as published: id={}", outboxEventId);
        });
    }


}
