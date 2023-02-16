package com.decagon.rewardyourteacher.services;

import com.decagon.rewardyourteacher.dto.*;
import com.decagon.rewardyourteacher.entity.User;

import java.util.List;

public interface StudentService {
    UserResponseDTO signUp(SignUpDto signUpDto);
    LoginResponse studentLogin(LoginDTO loginDTO);
    UserResponseDTO updateStudent(StudentRequestDTO requestDTO, String email);
    LoginResponse studentSocialLogin(SocialLoginRequest socialLoginRequest);
    User findUserByEmail(String email);
    void saveUser(User user);
    List<TeacherResponseDTO> findAllTeachers(Integer pageNumber, Integer pageSize, String sortProperty, String school);
    List<TeacherResponseDTO> searchTeachers(String name, String email, String gender, String school);
    TeacherResponseDTO searchTeacher(String id);


}

