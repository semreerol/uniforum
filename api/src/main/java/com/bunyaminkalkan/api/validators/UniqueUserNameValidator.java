package com.bunyaminkalkan.api.validators;

import com.bunyaminkalkan.api.annotations.UniqueUserName;
import com.bunyaminkalkan.api.repos.UserRepository;

import lombok.RequiredArgsConstructor;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class UniqueUserNameValidator implements ConstraintValidator<UniqueUserName, String> {


    private final UserRepository userRepository;

    @Override
    public void initialize(UniqueUserName constraintAnnotation) {
    }

    @Override
    public boolean isValid(String userName, ConstraintValidatorContext context) {
        if (userName == null || userName.isEmpty()) {
            return true;
        }

        return !userRepository.existsByUserName(userName);
    }
}