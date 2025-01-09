package com.minsproject.matchpoint.constant.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SportType {

    BADMINTON("배드민턴"),

    TENNIS("테니스"),

    TABLE_TENNIS("탁구"),

    SQUASH("스쿼시");

    private final String name;
}
