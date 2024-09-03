package com.minsproject.matchpoint.repository;

import com.minsproject.matchpoint.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResultRepository extends JpaRepository<Result, Long> {
    Optional<Result> findByMatchIdAndMemberId(Long matchId, Long memberId);
}