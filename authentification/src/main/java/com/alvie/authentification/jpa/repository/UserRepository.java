package com.alvie.authentification.jpa.repository;

import org.springframework.stereotype.Repository;
import com.alvie.authentification.jpa.data.AlvieUser;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface UserRepository extends JpaRepository<AlvieUser, Long> {
	public AlvieUser findByEmail(String email);
}
