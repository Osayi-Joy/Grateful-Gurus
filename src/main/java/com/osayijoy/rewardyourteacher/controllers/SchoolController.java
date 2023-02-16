package com.decagon.rewardyourteacher.controllers;

import com.decagon.rewardyourteacher.dto.APIResponse;
import com.decagon.rewardyourteacher.dto.SchoolResponse;
import com.decagon.rewardyourteacher.services.SchoolService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schools")
public class SchoolController {

    private final SchoolService schoolService;

    public SchoolController(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<APIResponse<List<SchoolResponse>>> getAllSchools(
            @RequestParam(value = "name", defaultValue = "") String schoolName,
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "sortProperty", defaultValue = "name") String sortProperty) {
        try {
            List<SchoolResponse> responseList = schoolService.getAllSchools(schoolName, pageNumber, pageSize, sortProperty);
            return new ResponseEntity<>(
                    new APIResponse<>(true, "success", responseList), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(
                    new APIResponse<>(false, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<APIResponse<Integer>> getSchoolCount() {
        try {
            int schoolCount = schoolService.getSchoolCount();
            return new ResponseEntity<>(
                    new APIResponse<>(true, "success", schoolCount), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(
                    new APIResponse<>(false, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }
}
