package com.minsproject.matchpoint.repository;

import com.minsproject.matchpoint.entity.Sports;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SportsRepository extends JpaRepository<Sports, Long> {
}
