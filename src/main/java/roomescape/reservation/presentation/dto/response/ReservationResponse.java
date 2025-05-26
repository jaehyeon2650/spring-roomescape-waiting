package roomescape.reservation.presentation.dto.response;

import java.time.LocalDate;
import roomescape.member.presentation.dto.response.ReservationMemberResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationTime.presentation.dto.response.ReservationTimeResponse;
import roomescape.theme.presentation.dto.response.ThemeResponse;

public record ReservationResponse(Long id, String status, ReservationMemberResponse member, LocalDate date,
                                  ReservationTimeResponse time,
                                  ThemeResponse theme) {
    public static ReservationResponse from(final Reservation reservation) {
        String status = "예약";
        if(reservation.isWaitingStatus()){
            status = "대기";
        }
        return new ReservationResponse(
                reservation.getId(),
                status,
                new ReservationMemberResponse(reservation.name()),
                reservation.getDate(),
                new ReservationTimeResponse(
                        reservation.timeId(),
                        reservation.reservationTime()
                ),
                new ThemeResponse(reservation.themeId(),
                        reservation.themeName(),
                        reservation.themeDescription(),
                        reservation.themeThumbnail())
        );
    }
}
