package com.decagon.rewardyourteacher.services.servicesImpl;

import com.decagon.rewardyourteacher.config.security.AuthenticateService;
import com.decagon.rewardyourteacher.config.security.JwtService;
import com.decagon.rewardyourteacher.dto.*;
import com.decagon.rewardyourteacher.entity.User;
import com.decagon.rewardyourteacher.enums.UserRole;
import com.decagon.rewardyourteacher.exceptions.CustomException;
import com.decagon.rewardyourteacher.exceptions.UserNotFoundException;
import com.decagon.rewardyourteacher.repository.UserRepository;
import com.decagon.rewardyourteacher.services.StudentService;
import com.decagon.rewardyourteacher.utils.EmailValidatorService;
import com.decagon.rewardyourteacher.utils.MapStructMapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import static com.decagon.rewardyourteacher.enums.UserRole.STUDENT;
import static com.decagon.rewardyourteacher.enums.UserRole.TEACHER;

@Service
@AllArgsConstructor
public class StudentImpl implements StudentService {

    private final UserRepository studentRepository;
    private final UserRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;
    private final MapStructMapper mapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuthenticateService authenticateService;
    private final UserImpl userImpl;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public UserResponseDTO signUp(SignUpDto signUpDto) {
        if ("".equals(signUpDto.getEmail().trim())) {
            throw new CustomException("Email can not be empty");
        }

        String email = signUpDto.getEmail().toLowerCase();
        if(!EmailValidatorService.isValid(email)){
            throw new CustomException("Enter a valid email address");
        }
        if (Objects.nonNull(studentRepository.findByEmail(email))) {
            throw new CustomException("Student already exist");
        }

        User student = User.builder()
                .firstName(signUpDto.getFirstName())
                .lastName(signUpDto.getLastName())
                .email(email)
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .phoneNumber(signUpDto.getPhoneNumber())
                .gender(signUpDto.getGender())
                .about(signUpDto.getAbout())
                .position(signUpDto.getPosition())
                .yearOfEmployment(signUpDto.getYearOfEmployment())
                .yearOfResignation(signUpDto.getYearOfResignation())
                .profilePicture(signUpDto.getProfilePicture())
                .createdAt(LocalDateTime.now())
                .role(STUDENT)
                .build();

        studentRepository.save(student);

        return new ModelMapper().map(student, UserResponseDTO.class);
    }

    @Override
    public LoginResponse studentLogin(LoginDTO loginDTO) {
        return authenticateService.getLoginResponse(loginDTO, authenticationManager, jwtService, STUDENT);
    }

    @Override
    public UserResponseDTO updateStudent(StudentRequestDTO userRequestDTO, String email) {
        return userImpl.updateStudentResponseEntity(userRequestDTO, studentRepository, email);
    }

    @Override
    public LoginResponse studentSocialLogin(SocialLoginRequest socialLoginRequest) {
        socialLoginRequest.setPassword("");
        User user = studentRepository.findByEmail(socialLoginRequest.getEmail());
        if (user == null) {
                user = User.builder()
                    .firstName(socialLoginRequest.getFirstName())
                    .lastName(socialLoginRequest.getLastName())
                    .password(passwordEncoder.encode(socialLoginRequest.getPassword()))
                    .email(socialLoginRequest.getEmail())
                    .role(UserRole.STUDENT)
                    .profilePicture(socialLoginRequest.getDisplayPicture())
                    .createdAt(LocalDateTime.now())
                    .build();
            studentRepository.save(user);
        }

        String token = "Bearer " + jwtService.generateToken
                (new org.springframework.security.core.userdetails.User(socialLoginRequest.getEmail(), socialLoginRequest.getFirstName(),
                        new ArrayList<>()));

        return LoginResponse.builder()
                .firstName(socialLoginRequest.getFirstName())
                .lastName(socialLoginRequest.getLastName())
                .email(socialLoginRequest.getEmail())
                .token(token)
                .role(UserRole.STUDENT)
                .profilePicture(socialLoginRequest.getDisplayPicture())
                .build();
    }

    @Override
    public User findUserByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    @Override
    public void saveUser(User user) {
        studentRepository.save(user);
    }

    @Override
    public List<TeacherResponseDTO> searchTeachers(String name, String email, String gender, String school) {
        if (name == null || "".equals(name.trim())) {
            return new ArrayList<>();
        }
        List<TeacherResponseDTO> searchResult = new ArrayList<>();
        String nameLCase = name.toLowerCase();
        String[] nameArr = nameLCase.split(" ");
        List<String> filteredName = Arrays.stream(nameArr)
                .filter(s -> !s.equals("")).toList();
        List<User> teachersList;

        if (filteredName.size() == 1) {
            teachersList = searchForTeachers(filteredName.get(0), "", email, gender, school);
        } else {
            teachersList = searchForTeachers(filteredName.get(0), filteredName.get(1), email, gender, school);
        }

        for (User user : teachersList) {
            TeacherResponseDTO teacherResponseDTO = TeacherResponseDTO.builder()
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .gender(user.getGender())
                    .role(user.getRole())
                    .profilePicture(user.getProfilePicture())
                    .school(user.getSchool())
                    .build();
            searchResult.add(teacherResponseDTO);
        }
        return searchResult;
    }

    @Override
    public TeacherResponseDTO searchTeacher(String id) {
        Pattern pattern = Pattern.compile("\\d+");
        User user;
        if (pattern.matcher(id).matches()) {
            user = teacherRepository.findById(Long.valueOf(id))
                    .orElse(null);
        } else {
            user = teacherRepository.findByEmail(id);
        }
        if (user == null || user.getRole() != TEACHER) {
            throw new UserNotFoundException();
        }
        return mapper.userToTeacherResponseDTO(user);
    }

    @Override
    public List<TeacherResponseDTO> findAllTeachers(Integer pageNumber, Integer pageSize, String sortProperty, String school) {
        List<User> listOfTeachers = findTeachers(pageNumber, pageSize, sortProperty, school);

        List<TeacherResponseDTO> responseDTOList = new ArrayList<>();
        for (User user : listOfTeachers) {
            responseDTOList.add(new ModelMapper().map(user, TeacherResponseDTO.class));
        }
        return responseDTOList;
    }

    private List<User> searchForTeachers(String firstName, String lastName, String email, String gender, String school) {
        StringBuilder builder = new StringBuilder()
                .append("select u from User u where (lower(u.firstName) like CONCAT('%',:firstName,'%') ")
                .append("and lower(u.lastName) like CONCAT('%',:lastName,'%') ")
                .append("or lower(u.lastName) like CONCAT('%',:firstName,'%') ")
                .append("and lower(u.firstName) like CONCAT('%',:lastName,'%')) ")
                .append("and u.role = 'TEACHER'");

        if (email != null && !"".equals(email.trim())) {
            builder.append("and lower(u.email) = lower(:email) ");
        }
        if (gender != null && !"".equals(gender.trim())) {
            builder.append("and u.gender = upper(:gender) ");
        }

        if (school != null && !"".equals(school.trim())) {
            builder.append("and lower(u.school.name) like lower(CONCAT('%',:school,'%'))");
        }

        String teachersBuilder = builder.toString();

        TypedQuery<User> query = entityManager.createQuery(teachersBuilder, User.class)
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName);

        if (email != null && !"".equals(email.trim())) {
            query.setParameter("email", email);
        }
        if (gender != null && !"".equals(gender.trim())) {
            query.setParameter("gender", gender);
        }

        if (school != null && !"".equals(school.trim())) {
            query.setParameter("school", school);
        }

        return query.getResultList();
    }

    private List<User> findTeachers(Integer pageNumber, Integer pageSize, String sortProperty, String school) {
        StringBuilder builder = new StringBuilder()
                .append("select u from User u where u.role = 'TEACHER' ");

        if (school != null && !"".equals(school.trim())) {
            builder.append("and (u.school.id = :schoolId or lower(u.school.name) = lower(:school)) ");
        }
        builder.append("order by u.")
                .append(sortProperty)
                .append(" ASC");

        String teachersBuilder = builder.toString();

        pageNumber = pageNumber > 0 ? pageNumber : 1;
        TypedQuery<User> query = entityManager.createQuery(teachersBuilder, User.class)
                .setFirstResult((pageNumber - 1) * pageSize)
                .setMaxResults(pageSize);

        if (school != null && !"".equals(school.trim())) {
            boolean isNumber = Pattern.compile("\\d+").matcher(school.trim()).matches();
            query.setParameter("schoolId", isNumber ? Long.parseLong(school) : 0);
            query.setParameter("school", school);
        }

        return query.getResultList();

    }
}
