package com.example.course.scheduler;

import com.example.course.dao.OutboxEventRepository;
import com.example.course.dao.entity.OutboxEvent;
import com.example.course.dto.CourseEnrollmentEventDto;
import com.example.course.producer.EnrollmentEventProducer;
import com.example.course.service.OutboxService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(OutboxEventPublisher.class);
    private final OutboxEventRepository outboxRepository;
    private final EnrollmentEventProducer eventProducer;
    private final OutboxService outboxService;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 3000)
    public void publishOutboxEvents() {
        try {
            List<OutboxEvent> unpublishedEvents = outboxRepository.findByPublishedFalseOrderByCreatedAtAsc();

            if (unpublishedEvents.isEmpty()) {
                return;
            }

            logger.info("Found {} unpublished events in outbox", unpublishedEvents.size());
            int batchSize = 10;
            int processed = 0;
            for (OutboxEvent outboxEvent : unpublishedEvents) {
                if (processed >= batchSize) {
                    logger.info("Reached batch size limit ({}), will process remaining in next cycle", batchSize);
                    break;
                }

                try {
                    publishEvent(outboxEvent);
                    processed++;
                } catch (Exception e) {
                    logger.error("Failed to publish outbox event: id={}, eventId={}", outboxEvent.getId(), outboxEvent.getEventId(), e);
                }
            }

        } catch (Exception e) {
            logger.error("Error in outbox publisher", e);
        }
    }

    private void publishEvent(OutboxEvent outboxEvent) throws Exception {
        CourseEnrollmentEventDto event = objectMapper.readValue(outboxEvent.getPayload(), CourseEnrollmentEventDto.class);

        eventProducer.sendEnrollmentEvent(event);

        outboxService.markAsPublished(outboxEvent.getId());

        logger.debug("Successfully published outbox event: id={}, eventId={}", outboxEvent.getId(), outboxEvent.getEventId());
    }
}
