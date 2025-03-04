package com.minsproject.matchpoint.dto.request;

import com.minsproject.matchpoint.constant.type.SportType;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchListRequest {
    private Long userId;

    private SportType sportType;

    private String sort;

    private Long lastId;

    @Min(value = 1, message = "페이지 크기는 최소 1 이상이어야 합니다.")
    private Integer pageSize = 20;
}