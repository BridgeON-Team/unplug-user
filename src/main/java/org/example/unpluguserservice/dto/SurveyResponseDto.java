package org.example.unpluguserservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.unpluguserservice.entity.SurveyResult;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SurveyResponseDto {
    private String username;
    private int totalScore;
    private String type;
    private String description;

    public static SurveyResponseDto from(SurveyResult result, String description){
        return SurveyResponseDto.builder()
                .username(result.getUsername())
                .totalScore(result.getTotalScore())
                .type(result.getType())
                .description(description)
                .build();
    }
}
