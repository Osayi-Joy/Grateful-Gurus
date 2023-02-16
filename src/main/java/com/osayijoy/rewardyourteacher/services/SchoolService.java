package com.decagon.rewardyourteacher.services;

import com.decagon.rewardyourteacher.dto.SchoolResponse;
import com.decagon.rewardyourteacher.entity.School;

import java.util.List;

public interface SchoolService {
    List<SchoolResponse> getAllSchools(String schoolName, int pageNumber, int pageSize, String sortProperty);

    int getSchoolCount();

    String saveSchool(List<School> schoolEntities);
}
