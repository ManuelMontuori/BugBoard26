package com.bugboard.api.mapper;

import com.bugboard.api.dto.CreateIssueDTO;
import com.bugboard.api.dto.IssueDTO;
import com.bugboard.api.models.Issue;
import com.bugboard.api.models.IssuePriority;
import com.bugboard.api.models.IssueType;
import org.springframework.stereotype.Component;

@Component
public class IssueMapper {
    public IssueDTO mapToDTO(Issue issue) {
        return new IssueDTO(
                issue.getUuid().toString(),
                issue.getTitle(),
                issue.getDescription(),
                issue.getType() != null ? issue.getType().name() : null,
                issue.getPriority() != null ? issue.getPriority().name() : null,
                issue.getStatus() != null ? issue.getStatus().name() : null,
                issue.getReporter() != null ? issue.getReporter().getUuid().toString() : null,
                issue.getAssignedTo() != null ? issue.getAssignedTo().getUuid().toString() : null,
                issue.getImagePath() != null ? issue.getImagePath() : null,
                issue.getCreatedAt() != null ? issue.getCreatedAt(): null,
                issue.getResolvedAt() != null ? issue.getResolvedAt(): null,
                issue.getAssignedAt() != null ? issue.getAssignedAt():null
        );
    }

    public Issue mapToEntity(CreateIssueDTO dto, Issue issue) {
        issue.setTitle(dto.title());
        issue.setDescription(dto.description());
        issue.setType(dto.type() != null ? IssueType.valueOf(dto.type()) : null);
        issue.setPriority(dto.priority() != null ? IssuePriority.valueOf(dto.priority()) : null);
        issue.setImagePath(dto.imgPath() != null ? dto.imgPath() : null);
        return issue;
    }
}
