package com.minsproject.matchpoint.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
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
    public MatchResult(Match match, SportProfile winner, SportProfile loser) {
        this.match = match;
        this.winner = winner;
        this.loser = loser;
    }
}