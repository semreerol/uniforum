package com.bunyaminkalkan.api.repos;

import com.bunyaminkalkan.api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
