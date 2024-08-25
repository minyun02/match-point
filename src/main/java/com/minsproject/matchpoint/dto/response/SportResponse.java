package com.minsproject.matchpoint.dto.response;

import com.minsproject.matchpoint.entity.Sport;

public class SportResponse {

    private Long id;

    private String name;

    private SportResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static SportResponse fromEntity(Sport entity) {
        return new SportResponse(
                entity.getId(),
                entity.getName()
        );
    }
}
