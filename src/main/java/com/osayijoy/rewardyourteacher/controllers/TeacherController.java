package com.decagon.rewardyourteacher.controllers;

import com.decagon.rewardyourteacher.dto.*;
import com.decagon.rewardyourteacher.entity.Notification;
import com.decagon.rewardyourteacher.exceptions.UserNotFoundException;
import com.decagon.rewardyourteacher.services.TeacherService;
import com.decagon.rewardyourteacher.utils.ErrorsMap;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;


@RequestMapping("/api/teachers")
@RestController
public class TeacherController {
    private final TeacherService teacherService;

    @Autowired
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @Operation(summary = "Register Teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teacher registered successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SignUpDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid user details",
                    content = @Content)})
    @PostMapping("/teacher-sign-up")
    public ResponseEntity<APIResponse<?>> signUp(@RequestBody @Valid SignUpDto signUpDto, BindingResult result) {
        Map<String, String> errorsMap = ErrorsMap.getErrors(result);
        if(errorsMap != null){
            return new ResponseEntity<>(new APIResponse<>(false, "Details supplied is not valid", errorsMap), HttpStatus.BAD_REQUEST);
        }
        try {
            UserResponseDTO responseDTO = teacherService.signUp(signUpDto);
            return new ResponseEntity<>(new APIResponse<>(true, "successful", responseDTO), HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(new APIResponse<>(false, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/teacher-log-in")
    public ResponseEntity<APIResponse<LoginResponse>> login(@RequestBody LoginDTO loginDTO){
        try {
            LoginResponse response = teacherService.teacherLogin(loginDTO);
            return ResponseEntity.ok(new APIResponse<>(true, "success", response));
        } catch (Exception ex) {
//            ex.printStackTrace();
            return new ResponseEntity<>(new APIResponse<>(false, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/oauth2/callback")
    public ResponseEntity<APIResponse<LoginResponse>> socialSignUp(@RequestBody SocialLoginRequest request){
        try {
            LoginResponse response = teacherService.teacherSocialLogin(request);
            return ResponseEntity.ok(new APIResponse<>(true, "success", response));
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(new APIResponse<>(false, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("update-teacher")
    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    public ResponseEntity<APIResponse<UserResponseDTO>> updateTeacher(@RequestBody TeacherRequestDTO teacherRequestDTO,
                                           Principal principal
    ){
        String email = principal.getName();
        try {
            UserResponseDTO responseDTO = teacherService.updateTeacher(teacherRequestDTO, email);
            return ResponseEntity.ok(new APIResponse<>(true, "success", responseDTO));
        } catch (Exception ex) {
            return new ResponseEntity<>(new APIResponse<>(false, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/appreciate-student/{studentId}")
    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    public ResponseEntity<APIResponse<Notification>>teacherAppreciateStudent(
            @PathVariable Long studentId, Principal principal, @RequestBody MessageDTO message
    ) {
        try {
            if (principal == null) {
                throw new UserNotFoundException("Oops! Not available");
            }
            String email = principal.getName();
            Notification notification  = teacherService.teacherAppreciatesStudent(email, studentId, message);
            APIResponse<Notification> response = new APIResponse<>(true, "success", notification);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new APIResponse<>(false, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }

    }
}
