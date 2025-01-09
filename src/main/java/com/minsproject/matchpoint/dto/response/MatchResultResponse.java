package com.minsproject.matchpoint.dto.response;

import com.minsproject.matchpoint.entity.MatchResult;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MatchResultResponse {
    private Long id;
    private Long matchId;
    private Long winnerId;
    private Long loserId;

    private MatchResultResponse(Long id, Long matchId, Long winnerId, Long loserId) {
        this.id = id;
        this.matchId = matchId;
        this.winnerId = winnerId;
        this.loserId = loserId;
    }

    public static MatchResultResponse fromEntity(MatchResult entity) {
        return new MatchResultResponse(
                entity.getId(),
                entity.getMatch().getId(),
                entity.getWinner().getId(),
                entity.getLoser() == null ? null : entity.getLoser().getId()
        );
    }
}