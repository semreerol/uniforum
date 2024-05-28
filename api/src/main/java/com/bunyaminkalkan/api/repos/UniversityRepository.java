package com.bunyaminkalkan.api.repos;

import com.bunyaminkalkan.api.entities.University;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UniversityRepository extends JpaRepository<University, Long> {
    boolean existsByName(String trakyaUniversity);
}
