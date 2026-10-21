package com.example.userservice.notification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class NotificationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private JavaMailSender mailSender;

    @Test
    void shouldSendEmailWhenRestApiCalled() throws Exception {
        String body = """
                {
                    "email": "restdeleteduser@mail.ru",
                    "type": "DELETE"
                }
                """;
        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
        SimpleMailMessage message = captureSentEmail();
        assertEquals("restdeleteduser@mail.ru", Objects.requireNonNull(message.getTo())[0]);
        assertEquals("Аккаунт удалён", message.getSubject());
        assertEquals("Здравствуйте! Ваш аккаунт был удалён.", message.getText());
    }

    @Test
    void shouldReturn400WhenEmailIsInvalid() throws Exception {
        String body = """
                {
                    "email": "invalid.email@",
                    "type": "CREATE"
                }
                """;
        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    private SimpleMailMessage captureSentEmail() {
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, timeout(5000).times(1)).send(captor.capture());
        return captor.getValue();
    }
}