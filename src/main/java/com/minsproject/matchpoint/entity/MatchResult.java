package com.minsproject.matchpoint.entity;

import com.minsproject.matchpoint.sport_profile.domain.SportProfile;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class MatchResult extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private Long id;

    @OneToOne
    private Match match;

    @ManyToOne
    private SportProfile winner;

    @ManyToOne
    private SportProfile loser;

    @Builder
    private MatchResult(Match match, SportProfile winner, SportProfile loser) {
        this.match = match;
        this.winner = winner;
        this.loser = loser;
    }

    public static MatchResult createFromResult(Match match, SportProfile submitter, String result) {
        if ("win".equals(result)) {
            return MatchResult.builder()
                    .match(match)
                    .winner(submitter)
                    .build();
        } else {
            return MatchResult.builder()
                    .match(match)
                    .loser(submitter)
                    .build();
        }
    }

    public boolean canConfirmMatch() {
        return this.winner != null && this.loser != null;
    }
}