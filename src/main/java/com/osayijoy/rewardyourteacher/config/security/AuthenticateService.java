package com.decagon.rewardyourteacher.config.security;

import com.decagon.rewardyourteacher.dto.LoginDTO;
import com.decagon.rewardyourteacher.dto.LoginResponse;
import com.decagon.rewardyourteacher.entity.User;
import com.decagon.rewardyourteacher.enums.UserRole;
import com.decagon.rewardyourteacher.exceptions.CustomException;
import com.decagon.rewardyourteacher.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collection;

import static com.decagon.rewardyourteacher.enums.UserRole.STUDENT;
import static com.decagon.rewardyourteacher.enums.UserRole.TEACHER;

@Service
public class AuthenticateService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public AuthenticateService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if(user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        Collection<? extends GrantedAuthority> authorities = user.getRole().equals(STUDENT)?
                STUDENT.getGrantedAuthorities() : TEACHER.getGrantedAuthorities();
        return new SecurityUser(email, user.getPassword(), authorities);
    }

    public LoginResponse getLoginResponse(LoginDTO loginDTO, AuthenticationManager authenticationManager, JwtService jwtService,
                                          UserRole userRole
    ) {
        String email = loginDTO.getEmail();
        if (email == null || "".equals(email.trim())) {
            throw new CustomException("Email field cannot be empty");
        }
        email = email.toLowerCase();

        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, loginDTO.getPassword()));
        if (auth.isAuthenticated()) {
            User user = userRepository.findByEmail(email);

            if (!userRole.equals(user.getRole())) {
                throw new CustomException("Incorrect username or password");
            }

            String result = "Bearer " + jwtService.generateToken(new SecurityUser(email, loginDTO.getPassword(), auth.getAuthorities()));

            return LoginResponse.builder()
                    .userId(user.getId())
                    .token(result)
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .gender(user.getGender())
                    .about(user.getAbout())
                    .profilePicture(user.getProfilePicture())
                    .role(user.getRole())
                    .position(user.getPosition())
                    .yearOfEmployment(user.getYearOfEmployment())
                    .yearOfResignation(user.getYearOfResignation())
                    .school(user.getSchool())
                    .build();
        } else {
            throw new CustomException("Authentication failed");
        }
    }

}
