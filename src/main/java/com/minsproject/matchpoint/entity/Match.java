package com.minsproject.matchpoint.entity;

import com.minsproject.matchpoint.constant.status.MatchStatus;
import com.minsproject.matchpoint.constant.type.SportType;
import com.minsproject.matchpoint.sport_profile.domain.SportProfile;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ToString
@Entity(name = "matches")
public class Match extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "inviter_id")
    private SportProfile inviter;

    @ManyToOne
    @JoinColumn(name = "invitee_id")
    private SportProfile invitee;

    @Enumerated(EnumType.STRING)
    private SportType sportType;

    @Enumerated(EnumType.STRING)
    private MatchStatus status;

    private LocalDateTime matchDate;

    private LocalDateTime canceledAt;

    private LocalDateTime acceptedAt;

    private LocalDateTime rejectedAt;

    private LocalDateTime confirmedAt;

    @OneToOne(mappedBy = "match")
    private MatchResult result;

    @Builder
    private Match(SportProfile inviter, SportProfile invitee, SportType sportType, MatchStatus status, LocalDateTime matchDate, LocalDateTime acceptedAt) {
        this.inviter = inviter;
        this.invitee = invitee;
        this.sportType = sportType;
        this.status = status;
        this.matchDate = matchDate;
        this.acceptedAt = acceptedAt;
    }

    public static Match createQuickMatch(SportProfile inviter,
                                         SportProfile invitee,
                                         SportType sportType,
                                         MatchStatus status,
                                         LocalDateTime matchDate,
                                         LocalDateTime acceptedAt) {
        return new Match(
                inviter,
                invitee,
                sportType,
                status,
                matchDate,
                acceptedAt
        );
    }

    public void updateStatus(MatchStatus matchStatus) {
        this.status = matchStatus;
    }
}
