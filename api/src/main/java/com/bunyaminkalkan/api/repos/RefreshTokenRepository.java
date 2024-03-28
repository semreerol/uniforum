package com.bunyaminkalkan.api.repos;

import com.bunyaminkalkan.api.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    RefreshToken findByUserId(Long id);
}
