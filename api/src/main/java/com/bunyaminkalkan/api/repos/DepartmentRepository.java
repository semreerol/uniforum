package com.bunyaminkalkan.api.repos;

import com.bunyaminkalkan.api.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    boolean existsByName(String facultyOfLaw);

    Department findByName(String computerEngineering);
}
