package com.bunyaminkalkan.api.services;

import com.bunyaminkalkan.api.entities.Department;
import com.bunyaminkalkan.api.exceptions.NotFoundException;
import com.bunyaminkalkan.api.repos.DepartmentRepository;
import com.bunyaminkalkan.api.repos.UniversityRepository;
import com.bunyaminkalkan.api.responses.UniversityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UniversityDepartmentService {

    private final UniversityRepository universityRepository;
    private final DepartmentRepository departmentRepository;

    @Transactional
    public List<UniversityResponse> getUniversities() {
        return universityRepository.findAll().stream().map(UniversityResponse::new).collect(Collectors.toList());
    }

    public List<Department> getDepartments() {
        return departmentRepository.findAll();
    }

    public UniversityResponse getUniversityById(Long uniId) {
        return universityRepository.findById(uniId).map(UniversityResponse::new).orElseThrow(() -> new NotFoundException("University not found"));
    }
}
