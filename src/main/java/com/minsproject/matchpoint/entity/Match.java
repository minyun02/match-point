package com.minsproject.matchpoint.entity;

import com.minsproject.matchpoint.constant.status.MatchStatus;
import com.minsproject.matchpoint.constant.type.SportType;
import com.minsproject.matchpoint.exception.ErrorCode;
import com.minsproject.matchpoint.exception.MatchPointException;
import com.minsproject.matchpoint.sport_profile.domain.SportProfile;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    public static Match createQuickMatch(SportProfile inviter, SportProfile invitee) {
        return Match.builder()
                .inviter(inviter)
                .invitee(invitee)
                .sportType(inviter.getSportType())
                .matchDate(LocalDateTime.now())
                .status(MatchStatus.ACCEPTED)
                .build();
    }

    public static Match createRegularMatch(SportProfile inviter, SportProfile invitee, LocalDateTime matchDate) {
        return Match.builder()
                .inviter(inviter)
                .invitee(invitee)
                .sportType(inviter.getSportType())
                .matchDate(matchDate)
                .status(MatchStatus.PENDING)
                .build();
    }

    public void updateStatus(MatchStatus matchStatus) {
        this.status = matchStatus;
    }

    public void validateSubmitCondition() {
        if (this.status != MatchStatus.FINISHED) {
            throw new MatchPointException(ErrorCode.MATCH_STATUS_NOT_ALLOWED);
        }

        if (this.matchDate.isAfter(LocalDateTime.now())) {
            throw new MatchPointException(ErrorCode.INVALID_MATCH_DAY);
        }
    }

    public void validateSubmitter(Long submitterId) {
        if (!Objects.equals(this.inviter.getId(), submitterId) && !Objects.equals(this.invitee.getId(), submitterId)) {
            throw new MatchPointException(ErrorCode.MATCH_RESULT_UNAUTHORIZED);
        }
    }

    public void validateResultDuplication(String result) {
        if (this.result != null) {
            if ("win".equals(result) && this.result.getWinner() != null) {
                    throw new MatchPointException(ErrorCode.RESULT_ALREADY_SUBMITTED);
            }


            if ("lose".equals(result) && this.result.getLoser() != null) {
                    throw new MatchPointException(ErrorCode.RESULT_ALREADY_SUBMITTED);
            }
        }
    }

    public SportProfile returnSubmitterProfile(Long submitterId) {
        return Objects.equals(this.inviter.getId(), submitterId)  ? this.inviter : this.invitee;
    }

}
