package org.frontend.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.frontend.services.*;

public class BackendServiceUtil {

    private static BackendServiceUtil instance;

    private final ApiClient apiClient;
    private final IssueApiService issueApiService;
    private final IssueService issueService;
    private final UserService userService;
    private final UserApiService userApiService;
    private final NotificationService notificationService;
    private final NotificationApiService notificationApiService;


    private BackendServiceUtil() {
        this.apiClient       = new ApiClient("http://52.211.223.99");
        this.issueApiService = new IssueApiService(apiClient);
        this.userApiService = new UserApiService(apiClient);
        this.issueService    = new IssueService(issueApiService);
        this.userService = new UserService(userApiService);
        this.notificationApiService = new NotificationApiService(apiClient);
        this.notificationService = new NotificationService(notificationApiService, new ObjectMapper());
    }

    public static BackendServiceUtil getInstance() {
        if (instance == null) instance = new BackendServiceUtil();
        return instance;
    }

    public IssueService getIssueService() { return issueService; }
    public UserService getUserService() { return  userService; }
    public NotificationService getNotificationService() { return notificationService; }
}