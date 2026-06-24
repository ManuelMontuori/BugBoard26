package org.frontend.util;

import org.frontend.services.*;

public class BackendServiceFactory {

    private static BackendServiceFactory instance;

    private final ApiClient apiClient;
    private final IssueApiService issueApiService;
    private final IssueService issueService;
    private final UserService userService;
    private final UserApiService userApiService;


    private BackendServiceFactory() {
        this.apiClient       = new ApiClient("http://localhost:8080");
        this.issueApiService = new IssueApiService(apiClient);
        this.userApiService = new UserApiService(apiClient);
        this.issueService    = new IssueService(issueApiService);
        this.userService = new UserService(userApiService);
    }

    public static BackendServiceFactory getInstance() {
        if (instance == null) instance = new BackendServiceFactory();
        return instance;
    }

    public IssueService getIssueService() { return issueService; }
    public UserService getUserService() { return  userService; }
    // Aggiungi altri getter man mano che aggiungi servizi
}