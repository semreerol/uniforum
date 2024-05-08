package com.bunyaminkalkan.api.controllers;

import com.bunyaminkalkan.api.entities.Department;
import com.bunyaminkalkan.api.responses.UniversityResponse;
import com.bunyaminkalkan.api.services.UniversityDepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class UniversityDepartmentController {

    private final UniversityDepartmentService universityDepartmentService;

    @GetMapping("/universities")
    public List<UniversityResponse> getUniversities(){
        return universityDepartmentService.getUniversities();
    }

    @GetMapping("/departments")
    public List<Department> getDepartments(){
        return universityDepartmentService.getDepartments();
    }
}
