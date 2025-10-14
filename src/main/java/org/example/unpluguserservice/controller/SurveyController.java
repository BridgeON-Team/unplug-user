package org.example.unpluguserservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.unpluguserservice.common.ApiResponse;
import org.example.unpluguserservice.dto.SurveyQuestionDto;
import org.example.unpluguserservice.dto.SurveyRequestDto;
import org.example.unpluguserservice.dto.SurveyResponseDto;
import org.example.unpluguserservice.service.SurveyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/survey")
public class SurveyController {
    private final SurveyService surveyService;

    @GetMapping("/questions")
    public ResponseEntity<List<SurveyQuestionDto>> getSurveyQuestions(){
        return ResponseEntity.ok(surveyService.getSurveyQuestions());
    }

    @PostMapping("/submit")
    public ResponseEntity<SurveyResponseDto> submitSurvey(@RequestBody SurveyRequestDto request){
        SurveyResponseDto response = surveyService.saveSurvey(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/result")
    public ResponseEntity<SurveyResponseDto> getSurveyResult(@RequestHeader(value = "X-Auth-Username", required = false) String username){
        return ResponseEntity.ok(surveyService.getSurveyResult(username));
    }
}
