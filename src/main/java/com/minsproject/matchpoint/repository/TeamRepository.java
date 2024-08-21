package com.minsproject.matchpoint.repository;

import com.minsproject.matchpoint.entity.Team;
import com.minsproject.matchpoint.repository.querydsl.TeamCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long>, TeamCustomRepository {
}
