package com.alvie.authentification.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alvie.authentification.jpa.data.SecureToken;

@Repository
public interface SecureTokenRepository extends JpaRepository<SecureToken, Long> {
	SecureToken findByToken(final String token);
    Long removeByToken(String token);
}
