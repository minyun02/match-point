package com.minsproject.matchpoint.repository;

import com.minsproject.matchpoint.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {
}
