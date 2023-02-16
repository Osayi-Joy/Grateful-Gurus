package com.decagon.rewardyourteacher.controllers;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.decagon.rewardyourteacher.dto.SchoolResponse;
import com.decagon.rewardyourteacher.enums.SchoolType;
import com.decagon.rewardyourteacher.services.SchoolService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(value="abc")
class SchoolControllerTest {

    @MockBean
    private SchoolService schoolService;

    private SchoolResponse school;
    private SchoolResponse school2;
    private SchoolResponse school3;

    @Autowired
    private WebApplicationContext context;

    MockMvc mockMvc;

    @BeforeEach
    void setUp(){
        school = new SchoolResponse(1,"Kayron school", SchoolType.SECONDARY,"sangotedo lagos","lagos-island","Lagos");
        school2 = new SchoolResponse(2,"Child care school", SchoolType.PRIMARY,"sangotedo lagos","lagos-island","Lagos");
        school3 = new SchoolResponse(3,"Jand", SchoolType.SECONDARY,"sangotedo lagos","lagos-island","Lagos");
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void testGetAllSchools() throws Exception {
        List<SchoolResponse> schoolResponseList = schoolResponse();
        when(schoolService.getAllSchools(any(String.class), any(Integer.class), any(Integer.class), any(String.class))).thenReturn(schoolResponseList);
        mockMvc.perform(get("/api/schools")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.length()", is(3)))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    private List<SchoolResponse> schoolResponse () {
        List<SchoolResponse> schools = new ArrayList<>();
        schools.add(school);
        schools.add(school2);
        schools.add(school3);
        return schools;
    }
}

