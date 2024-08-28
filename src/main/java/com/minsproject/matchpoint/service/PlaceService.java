package com.minsproject.matchpoint.service;

import com.minsproject.matchpoint.entity.Place;
import com.minsproject.matchpoint.exception.ErrorCode;
import com.minsproject.matchpoint.exception.MatchPointException;
import com.minsproject.matchpoint.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;

    public Place getPlaceById(Long placeId) {
        return placeRepository.findById(placeId).orElseThrow(() -> new MatchPointException(ErrorCode.PLACE_NOT_FOUND));
    }
}
