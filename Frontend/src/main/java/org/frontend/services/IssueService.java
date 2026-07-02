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

    public List<Issue> findAll() {
        try {
            String json = api.findAll();

            System.out.println("--- DEBUG RESPONSE FIND_ALL ---");
            System.out.println(json);
            System.out.println("-------------------------------");

            List<IssueDTO> dtoList =

                    JsonUtil.mapper.readValue(
                            json,
                            JsonUtil.mapper
                                    .getTypeFactory()
                                    .constructCollectionType(
                                            List.class,
                                            IssueDTO.class));
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

            System.out.println("--- DEBUG RESPONSE FIND_ALL ---");
            System.out.println(json);
            System.out.println("-------------------------------");

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

    public Issue createIssue(Issue issue) {
        try {

            String json = JsonUtil.mapper.writeValueAsString(issue);

            System.out.println("--- DEBUG POST PAYLOAD ---");
            System.out.println(json);
            System.out.println("--------------------------");

            String jsonResponse = api.create(json);

            System.out.println("--- DEBUG POST RESPONSE ---");
            System.out.println(jsonResponse);
            System.out.println("---------------------------");

            IssueDTO savedDto = JsonUtil.mapper.readValue(jsonResponse, IssueDTO.class);

            System.out.println("SIAMO NEL CREATE DEL SERVICE");

            return new Issue(savedDto);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Errore durante la creazione della issue", e);
        }
    }

}