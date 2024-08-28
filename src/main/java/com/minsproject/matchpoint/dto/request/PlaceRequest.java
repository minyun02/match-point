package com.minsproject.matchpoint.dto.request;

import com.minsproject.matchpoint.entity.Place;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PlaceRequest {

    private Long placeId;

    private String name;

    private String city;

    private String district;

    private String neighborhood;

    private String detailAddress;

    private Integer zipcode;

    public static Place toEntity(PlaceRequest dto) {
        if (dto.getPlaceId() == null) {
            return new Place(
                    dto.getCity(),
                    dto.getDistrict(),
                    dto.getNeighborhood(),
                    dto.getDetailAddress(),
                    dto.getZipcode()
            );
        }

        return new Place(
                dto.getPlaceId()
        );
    }
}
