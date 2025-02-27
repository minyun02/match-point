package com.minsproject.matchpoint.repository;

import com.minsproject.matchpoint.sport_profile.domain.SportProfile;
import com.minsproject.matchpoint.repository.querydsl.SportProfileCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SportProfileRepository extends JpaRepository<SportProfile, Long>, SportProfileCustomRepository {
    Optional<SportProfile> findByNickname(String nickname);
}