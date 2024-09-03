package com.minsproject.matchpoint.entity;

import com.minsproject.matchpoint.constant.status.MatchStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity(name = "matches")
public class Match extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inviter_id")
    private Member inviter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invitee_id")
    private Member invitee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sport_id")
    private Sport sport;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;

    private LocalDateTime matchDay;

    private String message;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MatchStatus status;

    public Match(Member inviter,
                 Member invitee,
                 Sport sport,
                 Place place,
                 LocalDateTime matchDay,
                 String message,
                 MatchStatus status) {
        this.inviter = inviter;
        this.invitee = invitee;
        this.sport = sport;
        this.place = place;
        this.matchDay = matchDay;
        this.message = message;
        this.status = status;
    }

    public void updateStatus(MatchStatus status) {
        this.status = status;
    }

    public boolean isFinished() {
        return this.status == MatchStatus.FINISHED && this.matchDay.isBefore(LocalDateTime.now());
    }
}
