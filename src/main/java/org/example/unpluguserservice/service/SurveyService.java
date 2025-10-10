package org.example.unpluguserservice.service;

import lombok.RequiredArgsConstructor;
import org.example.unpluguserservice.dto.SurveyQuestionDto;
import org.example.unpluguserservice.dto.SurveyRequestDto;
import org.example.unpluguserservice.dto.SurveyResponseDto;
import org.example.unpluguserservice.entity.SurveyResult;
import org.example.unpluguserservice.repository.SurveyRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SurveyService {
    private final SurveyRepository surveyRepository;

    public List<SurveyQuestionDto> getSurveyQuestions() {
        return Arrays.asList(
                new SurveyQuestionDto(1, "스마트폰을 사용하지 않으면 불안하거나 초조함을 느낍니다."),
                new SurveyQuestionDto(2, "스마트폰을 사용하다 보면 계획한 시간보다 오래 사용하게 됩니다."),
                new SurveyQuestionDto(3, "스마트폰 때문에 해야 할 일을 미루거나 놓친 적이 있습니다."),
                new SurveyQuestionDto(4, "식사나 대화 중에도 스마트폰을 확인합니다."),
                new SurveyQuestionDto(5, "스마트폰 사용으로 수면 시간이 줄어든 적이 있습니다."),
                new SurveyQuestionDto(6, "스마트폰 사용을 줄이려 시도했지만 잘 되지 않았습니다."),
                new SurveyQuestionDto(7, "스마트폰이 없으면 집중이 잘 안 됩니다."),
                new SurveyQuestionDto(8, "SNS 알림이 없으면 허전하거나 불안합니다."),
                new SurveyQuestionDto(9, "스마트폰 사용으로 가족이나 친구와 갈등이 생긴 적이 있습니다."),
                new SurveyQuestionDto(10, "스마트폰 사용을 줄여야 한다고 느낍니다.")
        );
    }

    public SurveyResponseDto saveSurvey(SurveyRequestDto request){
        int total = request.getAnswers().stream().mapToInt(Integer::intValue).sum();
        String type;
        String description;

        if (total >= 41) {
            type = "A형 – 심각한 의존형";
            description = "스마트폰 사용이 일상에 큰 영향을 주는 상태입니다. 디지털 디톡스가 필요합니다.";
        } else if (total >= 31) {
            type = "B형 – 주의 필요형";
            description = "스마트폰 사용이 길어지고 자제력이 약해지는 경향이 있습니다.";
        } else if (total >= 21) {
            type = "C형 – 관리 필요형";
            description = "가벼운 의존 경향이 있지만 스스로 조절 가능합니다.";
        } else {
            type = "D형 – 건강한 사용형";
            description = "스마트폰 사용 습관이 안정적인 상태입니다.";
        }

        SurveyResult result = SurveyResult.builder()
                .username(request.getUsername())
                .totalScore(total)
                .type(type)
                .build();
        surveyRepository.save(result);

        return SurveyResponseDto.from(result, description);
    }

    public SurveyResponseDto getSurveyResult(String username){
        SurveyResult surveyResult = surveyRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않습니다."));

        String description;

        if (surveyResult.getTotalScore() >= 41) {
            description = "스마트폰 사용이 일상에 큰 영향을 주는 상태입니다. 디지털 디톡스가 필요합니다.";
        } else if (surveyResult.getTotalScore() >= 31) {
            description = "스마트폰 사용이 길어지고 자제력이 약해지는 경향이 있습니다.";
        } else if (surveyResult.getTotalScore() >= 21) {
            description = "가벼운 의존 경향이 있지만 스스로 조절 가능합니다.";
        } else {
            description = "스마트폰 사용 습관이 안정적인 상태입니다.";
        }
        return SurveyResponseDto.from(surveyResult, description);
    }
}
