package com.bugboard.api.dto;

public record UserWorkloadOutDTO (
   String uuid,
   String firstName,
   String lastName,
   Long issuesCount
) {}
