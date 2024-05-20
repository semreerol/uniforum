package com.bunyaminkalkan.api.config;

import com.bunyaminkalkan.api.entities.Department;
import com.bunyaminkalkan.api.entities.University;
import com.bunyaminkalkan.api.repos.DepartmentRepository;
import com.bunyaminkalkan.api.repos.UniversityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UniversityRepository universityRepository;
    private final DepartmentRepository departmentRepository;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            if (!departmentRepository.existsByName("Computer Engineering")) {
                Department ceDepartment = new Department();
                ceDepartment.setName("Computer Engineering");
                departmentRepository.save(ceDepartment);
            }

            if (!departmentRepository.existsByName("Faculty of Medicine")) {
                Department fmDepartment = new Department();
                fmDepartment.setName("Faculty of Medicine");
                departmentRepository.save(fmDepartment);
            }

            if (!departmentRepository.existsByName("Faculty of Law")) {
                Department flDepartment = new Department();
                flDepartment.setName("Faculty of Law");
                departmentRepository.save(flDepartment);
            }

            if (!universityRepository.existsByName("Trakya University")) {
                University trakyaUniversity = new University();
                trakyaUniversity.setName("Trakya University");
                trakyaUniversity.getDepartments().add(departmentRepository.findByName("Computer Engineering"));
                trakyaUniversity.getDepartments().add(departmentRepository.findByName("Faculty of Medicine"));
                universityRepository.save(trakyaUniversity);
            }

            if (!universityRepository.existsByName("Bogazici University")) {
                University bogaziciUniversity = new University();
                bogaziciUniversity.setName("Bogazici University");
                bogaziciUniversity.getDepartments().add(departmentRepository.findByName("Computer Engineering"));
                bogaziciUniversity.getDepartments().add(departmentRepository.findByName("Faculty of Law"));
                universityRepository.save(bogaziciUniversity);
            }
        };
    }
}
