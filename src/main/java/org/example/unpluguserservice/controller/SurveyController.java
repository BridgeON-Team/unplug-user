package org.example.unpluguserservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
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
    @Operation(
            summary = "설문 문항 불러오기",
            description = "설문 문항 10개가 불러와지고 각 설문에 대해 사용자는 1~5 사이의 응답 가능",
            responses = {@ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            value = """
                                                [
                                                    {
                                                        "questionId": 1,
                                                        "question": "스마트폰을 사용하지 않으면 불안하거나 초조함을 느낍니다."
                                                    },
                                                    {
                                                        "questionId": 2,
                                                        "question": "스마트폰을 사용하다 보면 계획한 시간보다 오래 사용하게 됩니다."
                                                    },
                                                    {
                                                        "questionId": 3,
                                                        "question": "스마트폰 때문에 해야 할 일을 미루거나 놓친 적이 있습니다."
                                                    },
                                                    {
                                                        "questionId": 4,
                                                        "question": "식사나 대화 중에도 스마트폰을 확인합니다."
                                                    },
                                                    {
                                                        "questionId": 5,
                                                        "question": "스마트폰 사용으로 수면 시간이 줄어든 적이 있습니다."
                                                    },
                                                    {
                                                        "questionId": 6,
                                                        "question": "스마트폰 사용을 줄이려 시도했지만 잘 되지 않았습니다."
                                                    },
                                                    {
                                                        "questionId": 7,
                                                        "question": "스마트폰이 없으면 집중이 잘 안 됩니다."
                                                    },
                                                    {
                                                        "questionId": 8,
                                                        "question": "SNS 알림이 없으면 허전하거나 불안합니다."
                                                    },
                                                    {
                                                        "questionId": 9,
                                                        "question": "스마트폰 사용으로 가족이나 친구와 갈등이 생긴 적이 있습니다."
                                                    },
                                                    {
                                                        "questionId": 10,
                                                        "question": "스마트폰 사용을 줄여야 한다고 느낍니다."
                                                    }
                                                ]
                                    """)}
                    ))})
    public ResponseEntity<List<SurveyQuestionDto>> getSurveyQuestions(){
        return ResponseEntity.ok(surveyService.getSurveyQuestions());
    }

    @PostMapping("/submit")
    @Operation(summary = "설문 문항 제출", description = "각 설문에 대해 사용자는 1~5 사이의 응답 가능, 프론트에서는 각 응답을 숫자 리스트로 보냄, 응답은 설문 결과")
    public ResponseEntity<SurveyResponseDto> submitSurvey(@RequestBody SurveyRequestDto request){
        SurveyResponseDto response = surveyService.saveSurvey(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/result")
    @Operation(summary = "설문 결과 조회", description = "마이페이지에서 설문 결과 조회가 필요할 시 사용, 안 써도 됨")
    public ResponseEntity<SurveyResponseDto> getSurveyResult(@RequestHeader(value = "X-Auth-Username", required = false) String username){
        return ResponseEntity.ok(surveyService.getSurveyResult(username));
    }
}
