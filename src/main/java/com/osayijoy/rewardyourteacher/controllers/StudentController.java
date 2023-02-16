package com.decagon.rewardyourteacher.controllers;

import com.decagon.rewardyourteacher.dto.*;
import com.decagon.rewardyourteacher.services.StudentService;
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
import java.util.List;
import java.util.Map;

@CrossOrigin(value = "http://localhost:8080/")
@RequestMapping("/api/students")
@RestController
public class StudentController {

    private final StudentService studentService;
    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Operation(summary = "Register Student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student registered successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SignUpDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid user details",
                    content = @Content)})
    @PostMapping("/student-sign-up")
    public ResponseEntity<APIResponse<?>> signUp(@RequestBody @Valid SignUpDto signUpDto, BindingResult result){
        Map<String, String> errorsMap = ErrorsMap.getErrors(result);
        if(errorsMap != null){
            return new ResponseEntity<>(new APIResponse<>(false, "Details supplied is not valid", errorsMap), HttpStatus.BAD_REQUEST);
        }
        try {
            UserResponseDTO responseDTO = studentService.signUp(signUpDto);
            return new ResponseEntity<>(new APIResponse<>(true, "success", responseDTO), HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(new APIResponse<>(false, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/student-log-in")
    public ResponseEntity<APIResponse<LoginResponse>> login(@RequestBody LoginDTO loginDTO){
        try {
            LoginResponse response = studentService.studentLogin(loginDTO);
            return ResponseEntity.ok(new APIResponse<>(true, "success", response));
        } catch (Exception ex) {
            return new ResponseEntity<>(new APIResponse<>(false, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/oauth2/callback")
    public ResponseEntity<APIResponse<LoginResponse>> socialSignUp(@RequestBody SocialLoginRequest request){
        try {
            LoginResponse response = studentService.studentSocialLogin(request);
            return ResponseEntity.ok(new APIResponse<>(true, "success", response));
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(new APIResponse<>(false, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("update-student")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<APIResponse<UserResponseDTO>> updateStudent(@RequestBody StudentRequestDTO userRequestDTO,
            Principal principal
    ) {
        String email = principal.getName();
        try {
            UserResponseDTO responseDTO = studentService.updateStudent(userRequestDTO, email);
            return ResponseEntity.ok(new APIResponse<>(true, "success", responseDTO));
        } catch (Exception ex) {
            return new ResponseEntity<>(new APIResponse<>(false, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/search-teachers")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<APIResponse<List<TeacherResponseDTO>>>  searchTeachers(
            @RequestParam(required = false) final String name,
            @RequestParam(required = false) final String email,
            @RequestParam(required = false) final String gender,
            @RequestParam(required = false) final String school
    ) {
        try {
            List<TeacherResponseDTO> userList = studentService.searchTeachers(name, email, gender, school);
            return ResponseEntity.ok(new APIResponse<>(true, "success", userList));
        } catch (Exception ex) {
            return new ResponseEntity<>(new APIResponse<>(false, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/find-teachers/{id}")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<APIResponse<TeacherResponseDTO>>  searchTeacher(@PathVariable final String id) {
        try {
            TeacherResponseDTO teacherResponseDTO = studentService.searchTeacher(id);
            return ResponseEntity.ok(new APIResponse<>(true, "success", teacherResponseDTO));
        }  catch (Exception ex) {
            return new ResponseEntity<>(new APIResponse<>(false, ex.getMessage(), null), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/find-teachers")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<?> findAllTeachers(
            @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "sortProperty", defaultValue = "firstName") String sortProperty,
            @RequestParam(value = "school", defaultValue = "") String school
    ) {
        try {
            List<TeacherResponseDTO> teacherResponseDTO = studentService.findAllTeachers(pageNumber,pageSize,sortProperty, school);
            return ResponseEntity.ok(new APIResponse<>(true, "success", teacherResponseDTO));
        } catch (Exception ex) {

            return new ResponseEntity<>(new APIResponse<>(false, "Oops! Not available", null), HttpStatus.NOT_FOUND);
        }
    }
}
