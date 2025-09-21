package com.sn.snuser.dto;

public record UserEventDto(
        String operationType,
        UserDto user,
        long timestamp
) {}
