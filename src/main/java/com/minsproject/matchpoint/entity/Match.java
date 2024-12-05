package com.minsproject.matchpoint.entity;

import com.minsproject.matchpoint.constant.status.MatchStatus;
import com.minsproject.matchpoint.constant.type.SportType;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
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

    private Long winnerProfileId;
}
