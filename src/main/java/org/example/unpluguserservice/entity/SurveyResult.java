package org.example.unpluguserservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "survey_result")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveyResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "survey_result_id", nullable = false)
    private Long SurveyResultId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "total_score", nullable = false)
    private int totalScore;

    @Column(name = "type", nullable = false)
    private String type;
}
