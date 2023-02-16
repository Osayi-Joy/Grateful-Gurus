//package com.decagon.rewardyourteacher.controllers;
//
//import static org.mockito.Mockito.any;
//import static org.mockito.Mockito.when;
//
//import com.decagon.rewardyourteacher.services.TeacherService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//@ContextConfiguration(classes = {TeacherController.class})
//@ExtendWith(SpringExtension.class)
//class TeacherControllerTest {
//    @Autowired
//    private TeacherController teacherController;
//
//    @MockBean
//    private TeacherService teacherService;
//
//    /**
//     * Method under test: {@link TeacherController#findAllTeachers(Integer, Integer, String)}
//     */
//    @Test
//    void testFindAllTeachers() throws Exception {
////        when(teacherService.findAllTeachers((Integer) any(), (Integer) any(), (String) any()))
////                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
////        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
////                .get("/user/find-all-teachers/{pageNumber}/{pageSize}/{sortProperty}", 10, 3, "Sort Property");
////        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(teacherController)
////                .build()
////                .perform(requestBuilder);
////        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(100));
//    }
//}