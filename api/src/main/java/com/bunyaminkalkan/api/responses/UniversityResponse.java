package com.bunyaminkalkan.api.responses;

import com.bunyaminkalkan.api.entities.Department;
import com.bunyaminkalkan.api.entities.University;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class UniversityResponse {
    private Long id;
    private String name;
    private List<String> departments;

    public UniversityResponse(University entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.departments = entity.getDepartments().stream().map(Department::getName).collect(Collectors.toList());
    }
}
