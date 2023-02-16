package com.decagon.rewardyourteacher.services.servicesImpl;

import com.decagon.rewardyourteacher.dto.SchoolResponse;
import com.decagon.rewardyourteacher.entity.School;
import com.decagon.rewardyourteacher.repository.SchoolRepository;
import com.decagon.rewardyourteacher.services.SchoolService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SchoolServiceImpl implements SchoolService {
    private final SchoolRepository  schoolRepository;

    @PersistenceContext
    EntityManager entityManager;

    public SchoolServiceImpl(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }

    @Override
    public List<SchoolResponse> getAllSchools(String schoolName, int pageNumber, int pageSize, String sortProperty) {
        pageNumber = pageNumber <= 0 ? 1 : pageNumber;

        List<School> schools = findSchools(schoolName, pageNumber, pageSize, sortProperty);
        List<SchoolResponse> schoolResponseList = new ArrayList<>();

        for (School school : schools) {
            schoolResponseList.add(new ModelMapper().map(school, SchoolResponse.class));
        }

        return schoolResponseList;
    }

    private List<School> findSchools(String schoolName, int pageNumber, int pageSize, String sortProperty) {

        schoolName = Arrays.stream(schoolName.split(" "))
                        .filter(str -> !"".equals(str))
                        .collect(Collectors.joining(" "));

        String builder = "select s from School s where lower(s.name) like lower(CONCAT('%',:name,'%')) " +
                "order by s." +
                sortProperty +
                " ASC";

        pageNumber = pageNumber > 0 ? pageNumber : 1;
        TypedQuery<School> query = entityManager.createQuery(builder, School.class)
                .setFirstResult((pageNumber - 1) * pageSize)
                .setMaxResults(pageSize);

        query.setParameter("name", schoolName.trim());

        return query.getResultList();
    }

    @Override
    public int getSchoolCount() {
        return schoolRepository.findAll().size();
    }

    @Override
    public String saveSchool(List<School> schoolEntities) {
        schoolRepository.saveAll(schoolEntities);
        return "success";
    }
}
