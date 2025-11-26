package com.example.course.service;

import com.example.course.dto.CourseEnrollmentEventDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEnrollmentConfirmationEmail(CourseEnrollmentEventDto event) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(event.getUserEmail());
            message.setSubject("Course Enrollment Confirmation - " + event.getCourseName());
            message.setText(buildEmailContent(event));

            mailSender.send(message);
            logger.info("Enrollment confirmation email sent to: {}", event.getUserEmail());
        } catch (Exception e) {
            logger.error("Failed to send email to: {}", event.getUserEmail(), e);
            throw new RuntimeException("Email sending failed", e);
        }
    }

    private String buildEmailContent(CourseEnrollmentEventDto event) {
        return String.format(
                "Dear %s,\n\n" +
                        "Congratulations! You have successfully enrolled in the course.\n\n" +
                        "Course Details:\n" +
                        "Course Name: %s\n" +
                        "Course ID: %s\n" +
                        "You can now access the course materials and start learning.\n\n" +
                        "Best regards,\n" +
                        "Course Management Team",
                event.getUserName(),
                event.getCourseName(),
                event.getCourseId()
        );
    }
}
