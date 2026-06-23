package org.frontend.util;

import org.frontend.services.ApiClient;
import org.frontend.services.IssueApiService;
import org.frontend.services.IssueService;

public class BackendServiceFactory {

    private static BackendServiceFactory instance;

    private final ApiClient apiClient;
    private final IssueApiService issueApiService;
    private final IssueService issueService;

    private BackendServiceFactory() {
        this.apiClient       = new ApiClient("http://localhost:8080");
        this.issueApiService = new IssueApiService(apiClient);
        this.issueService    = new IssueService(issueApiService);
    }

    public static BackendServiceFactory getInstance() {
        if (instance == null) instance = new BackendServiceFactory();
        return instance;
    }

    public IssueService getIssueService() { return issueService; }
    // Aggiungi altri getter man mano che aggiungi servizi
}