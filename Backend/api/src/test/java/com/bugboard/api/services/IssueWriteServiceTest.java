package com.bugboard.api.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import com.bugboard.api.auth.AuthService;
import com.bugboard.api.events.IssueAssignedEvent;
import com.bugboard.api.mapper.IssueMapper;
import com.bugboard.api.models.Issue;
import com.bugboard.api.models.IssueStatus;
import com.bugboard.api.models.User;
import com.bugboard.api.repositories.IssueRepository;
import com.bugboard.api.repositories.UserRepository;
import org.springframework.web.client.ResourceAccessException;

@ExtendWith(MockitoExtension.class)
public class IssueWriteServiceTest {

    @Mock
    IssueRepository issueRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    AuthService authService;

    @Mock
    IssueMapper issueMapper;

    @Mock
    ApplicationEventPublisher eventPublisher;

    @InjectMocks
    IssueWriteService issueWriteService;

    
    @Test
    void testAssignIssue_validUser() {
        UUID issueId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Issue issue = new Issue();
        User user = new User();

        when(issueRepository.findByUuid(issueId)).thenReturn(Optional.of(issue));
        when(userRepository.findByUuid(userId)).thenReturn(Optional.of(user));

        issueWriteService.assignIssue(issueId, userId);

        // Verifiche (ORACLE)
        assertEquals(user, issue.getAssignedTo());
        assertNotNull(issue.getAssignedAt());
        assertEquals(IssueStatus.IN_PROGRESS, issue.getStatus());

        verify(eventPublisher).publishEvent(any(IssueAssignedEvent.class));
    }

    
    @Test
    void testAssignIssue_userNotFound() {
        UUID issueId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Issue issue = new Issue();

        when(issueRepository.findByUuid(issueId)).thenReturn(Optional.of(issue));
        when(userRepository.findByUuid(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceAccessException.class, () -> {
            issueWriteService.assignIssue(issueId, userId);
        });
    }
    

    @Test
    void testAssignIssue_issueNotFound() {

        UUID issueId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

       

        when(issueRepository.findByUuid(issueId)).thenReturn(Optional.empty());
        

        assertThrows(ResourceAccessException.class, () -> {
            issueWriteService.assignIssue(issueId, userId);
        });
    }

    
    

}
