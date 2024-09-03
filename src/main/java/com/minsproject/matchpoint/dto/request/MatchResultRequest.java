package com.minsproject.matchpoint.dto.request;

import com.minsproject.matchpoint.entity.Match;
import com.minsproject.matchpoint.entity.Member;
import com.minsproject.matchpoint.entity.Result;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class MatchResultRequest {

    @Schema(description = "매칭 결과값(W, L)")
    @NotBlank(message = "매칭 결과는 필수입니다.")
    private String resultType;

    @Schema(description = "매칭 결과값(W, L)")
    @NonNull()
    private Long memberId;

    public MatchResultRequest(String resultType, Long memberId) {
        this.resultType = resultType;
        this.memberId = memberId;
    }

    public static Result toEntity(Match match, String resultType, Member member) {
        return Result.builder()
                .match(match)
                .result(resultType)
                .member(member)
                .build();
    }
}
