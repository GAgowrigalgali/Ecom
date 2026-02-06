package org.example.ecom.repository;

import org.example.ecom.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //If not optional when there is no email found null will be returned
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}

