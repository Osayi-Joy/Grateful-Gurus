package com.osayijoy.rewardyourteacher.services;


import com.osayijoy.rewardyourteacher.dto.SchoolResponse;
import com.osayijoy.rewardyourteacher.entity.School;

import java.util.List;

public interface SchoolService {
    List<SchoolResponse> getAllSchools(String schoolName, int pageNumber, int pageSize, String sortProperty);

    int getSchoolCount();

    String saveSchool(List<School> schoolEntities);
}
