package com.minsproject.matchpoint.repository;

import com.minsproject.matchpoint.entity.Match;
import com.minsproject.matchpoint.repository.querydsl.MatchCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long>, MatchCustomRepository {
}
