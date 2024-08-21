package com.minsproject.matchpoint.repository;

import com.minsproject.matchpoint.entity.TeamMember;
import com.minsproject.matchpoint.repository.querydsl.TeamMemberCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TeamMemberRepository extends JpaRepository<TeamMember, Long>, TeamMemberCustomRepository {}