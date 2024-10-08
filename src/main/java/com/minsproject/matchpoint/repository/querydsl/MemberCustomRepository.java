package com.minsproject.matchpoint.repository.querydsl;

import com.minsproject.matchpoint.dto.request.MemberSearchRequest;
import com.minsproject.matchpoint.entity.MemberWithDistance;

import java.util.List;

public interface MemberCustomRepository {

    List<MemberWithDistance> findAvailableMembers(MemberSearchRequest request);
}
