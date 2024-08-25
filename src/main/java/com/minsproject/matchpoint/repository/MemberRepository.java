package com.minsproject.matchpoint.repository;

import com.minsproject.matchpoint.entity.Member;
import com.minsproject.matchpoint.entity.Sport;
import com.minsproject.matchpoint.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUserAndSport(User user, Sport sport);

    Optional<Member> findByNickname(String nickname);
}