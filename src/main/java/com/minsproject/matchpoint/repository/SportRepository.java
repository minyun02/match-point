package com.minsproject.matchpoint.repository;

import com.minsproject.matchpoint.entity.Sport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SportRepository extends JpaRepository<Sport, Long> {
}
