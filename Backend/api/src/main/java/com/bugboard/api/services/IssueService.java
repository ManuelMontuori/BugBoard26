package com.bugboard.api.services;

import com.bugboard.api.dto.CreateIssueDTO;
import com.bugboard.api.dto.IssueDTO;
import com.bugboard.api.models.*;
import com.bugboard.api.observer.Observer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface IssueService {

    List<Observer> observers=new ArrayList<>();

    IssueDTO createIssue(CreateIssueDTO dto);

    List<IssueDTO> getIssues(IssueStatus status, IssuePriority priority, IssueType type);

    IssueDTO getIssueByUuid(UUID id);

    List<IssueDTO> searchIssueByTitleOrDescription(String keyword);

    List<IssueDTO> findByAssignedToUuid(UUID assignedTo);

    void attach(Observer observer);

    void detach(Observer observer);

    void notifyObservers(Issue issue, User user);

    void assignIssue(UUID issueUuid, UUID userUuid);
}
