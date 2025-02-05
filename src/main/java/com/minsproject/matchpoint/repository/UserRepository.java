package com.minsproject.matchpoint.repository;

import com.minsproject.matchpoint.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndProvider(String email, String provider);

    Optional<User> findByProviderAndProviderId(String provider, String providerId);

}