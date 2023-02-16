package com.osayijoy.rewardyourteacher.services;

import com.osayijoy.rewardyourteacher.dto.*;
import com.osayijoy.rewardyourteacher.entity.Notification;

public interface TeacherService {

    UserResponseDTO signUp(SignUpDto signUpDto) throws Exception;
    LoginResponse teacherLogin(LoginDTO loginDTO);
    UserResponseDTO updateTeacher(TeacherRequestDTO teacherRequestDTO, String email);
    Notification teacherAppreciatesStudent(String email, Long userId, MessageDTO messageDTO);
    LoginResponse teacherSocialLogin(SocialLoginRequest socialLoginRequest);
}
