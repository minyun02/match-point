package com.minsproject.matchpoint.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;

@Getter
@AllArgsConstructor
public class ProfileWithInfo<T> {

    private final T entity;
    private final Double distance;
    @Setter private Double similarity;

    public static <T> ProfileWithInfo<T> of(T entity, Double distance) {
        return new ProfileWithInfo<>(entity, distance, null);
    }

    public static <T> Comparator<ProfileWithInfo<T>> compareBySimilarity() {
        return (p1, p2) -> p2.getSimilarity().compareTo(p1.getSimilarity());
    }
}