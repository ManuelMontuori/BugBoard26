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
}