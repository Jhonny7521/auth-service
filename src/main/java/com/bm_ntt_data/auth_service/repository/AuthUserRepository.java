package com.bm_ntt_data.auth_service.repository;

import com.bm_ntt_data.auth_service.entity.AuthUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, Integer> {
    Optional<AuthUser> findByUserName(String username);
}
