package com.decagon.rewardyourteacher.services;

import com.decagon.rewardyourteacher.dto.*;
import com.decagon.rewardyourteacher.entity.Notification;

public interface TeacherService {

    UserResponseDTO signUp(SignUpDto signUpDto) throws Exception;
    LoginResponse teacherLogin(LoginDTO loginDTO);
    UserResponseDTO updateTeacher(TeacherRequestDTO teacherRequestDTO, String email);
    Notification teacherAppreciatesStudent(String email, Long userId, MessageDTO messageDTO);
    LoginResponse teacherSocialLogin(SocialLoginRequest socialLoginRequest);
}
