package com.decagon.rewardyourteacher.enums;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;

public enum UserRole {
    TEACHER,
    STUDENT;

    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){
        Set<SimpleGrantedAuthority> set = new HashSet<>();
        set.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        set.add(new SimpleGrantedAuthority("ROLE_USER"));
        return set;
    }
}
