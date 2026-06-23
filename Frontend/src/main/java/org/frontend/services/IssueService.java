package org.frontend.services;

import org.frontend.models.Issue;
import org.frontend.models.dtos.IssueDTO;
import org.frontend.util.JsonUtil;
import java.util.List;

public class IssueService {

    private final IssueApiService api;

    public IssueService(IssueApiService api){

        this.api = api;

    }

    public List<Issue> findAll(){
        try {
            String json =
                    api.findAll();
            // 🚨 LOG DI DEBUG IMPERATIVO:
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
                                            IssueDTO.class
                                    )
                    );
            return dtoList.stream()
                    .map(Issue::new)
                    .toList();

        }catch(Exception e){
            e.printStackTrace();
            return List.of();
        }
    }

    public List<Issue> findAssignedToMe(String userUuid) {
        try {
            String json = api.findAssignedToMe(userUuid);

            // 🚨 LOG DI DEBUG IMPERATIVO:
            System.out.println("--- DEBUG RESPONSE FIND_ALL ---");
            System.out.println(json);
            System.out.println("-------------------------------");

            List<IssueDTO> dtoList = JsonUtil.mapper.readValue(
                    json,
                    JsonUtil.mapper.getTypeFactory()
                            .constructCollectionType(List.class, IssueDTO.class)
            );

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
            // 1. Serializzazione: Convertiamo il DTO in una stringa JSON
            String json = JsonUtil.mapper.writeValueAsString(issue);

            // 🚨 LOG DI DEBUG PAYLOAD INVIATO
            System.out.println("--- DEBUG POST PAYLOAD ---");
            System.out.println(json);
            System.out.println("--------------------------");

            // 2. Chiamata API
            String jsonResponse = api.create(json);

            // 🚨 LOG DI DEBUG RISPOSTA RICEVUTA
            System.out.println("--- DEBUG POST RESPONSE ---");
            System.out.println(jsonResponse);
            System.out.println("---------------------------");

            // 3. Deserializzazione: Convertiamo il JSON di risposta nel DTO
            IssueDTO savedDto = JsonUtil.mapper.readValue(jsonResponse, IssueDTO.class);

            System.out.println("SIAMO NEL CREATE DEL SERVICE");
            // 4. Mappatura sul modello di dominio e ritorno
            return new Issue(savedDto);

        } catch (Exception e) {
            e.printStackTrace();
            // Puoi decidere se lanciare una tua eccezione personalizzata o ritornare null
            return null;
        }
    }
}