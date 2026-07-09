package org.frontend.services;

import org.frontend.models.Issue;
import org.frontend.models.dtos.IssueDTO;
import org.frontend.util.JsonUtil;

import java.util.List;

public class IssueService {

    private final IssueApiService api;

    public IssueService(IssueApiService api) {

        this.api = api;

    }

    public List<Issue> findAll(String status, String priority, String type) {
        try {
            // Passiamo i parametri al metodo dell'API service
            String json = api.findAll(status, priority, type);

            List<IssueDTO> dtoList = JsonUtil.mapper.readValue(
                    json,
                    JsonUtil.mapper.getTypeFactory().constructCollectionType(List.class, IssueDTO.class));

            return dtoList.stream()
                    .map(Issue::new)
                    .toList();

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<Issue> findAssignedToMe(String userUuid) {
        try {
            String json = api.findAssignedToMe(userUuid);

            List<IssueDTO> dtoList = JsonUtil.mapper.readValue(
                    json,
                    JsonUtil.mapper.getTypeFactory()
                            .constructCollectionType(List.class, IssueDTO.class));

            return dtoList.stream()
                    .map(Issue::new)
                    .toList();

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<Issue> searchIssue(String keyword) {
        try {
            String json = api.searchIssue(keyword);

            List<IssueDTO> dtoList = JsonUtil.mapper.readValue(
                    json,
                    JsonUtil.mapper.getTypeFactory()
                            .constructCollectionType(List.class, IssueDTO.class));

            return dtoList.stream()
                    .map(Issue::new)
                    .toList();

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public Issue createIssue(Issue issue) throws Exception {

        String json = JsonUtil.mapper.writeValueAsString(issue);

        String jsonResponse = api.create(json);

        IssueDTO savedDto = JsonUtil.mapper.readValue(jsonResponse, IssueDTO.class);

        return new Issue(savedDto);
    }
}