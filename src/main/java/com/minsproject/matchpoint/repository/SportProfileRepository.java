package com.minsproject.matchpoint.repository;

import com.minsproject.matchpoint.entity.SportProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SportProfileRepository extends JpaRepository<SportProfile, Long> {
    Optional<SportProfile> findByNickname(String nickname);

}
