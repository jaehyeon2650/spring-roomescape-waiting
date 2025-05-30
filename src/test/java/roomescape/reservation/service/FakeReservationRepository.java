package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationRepository;

public class FakeReservationRepository implements ReservationRepository {

    private final List<Reservation> reservations;

    private AtomicLong index = new AtomicLong(0);

    public FakeReservationRepository(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    @Override
    public List<Reservation> findAll() {
        return Collections.unmodifiableList(reservations);
    }

    @Override
    public List<Reservation> findByMemberIdAndThemeIdAndDate(Long memberId, Long themeId, LocalDate dateFrom,
                                                             LocalDate dateTo) {
        return reservations.stream()
                .filter(reservation -> matchMemberId(reservation, memberId))
                .filter(reservation -> matchThemeId(reservation, themeId))
                .filter(reservation -> matchDateRange(reservation, dateFrom, dateTo))
                .toList();
    }

    @Override
    public boolean existsByTimeId(Long timeId) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.timeId().equals(timeId));
    }

    @Override
    public boolean hasSameReservation(Reservation reservation) {
        return reservations.stream()
                .anyMatch(nextReservation ->
                        nextReservation.reservationTime().equals(reservation.reservationTime())
                                && nextReservation.getDate().equals(reservation.getDate())
                                && nextReservation.themeId().equals(reservation.themeId()));
    }

    @Override
    public boolean existsByThemeId(Long themeId) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.themeId().equals(themeId));
    }

    @Override
    public List<Reservation> findByMemberId(Long memberId) {
        return reservations.stream()
                .filter(reservation -> reservation.memberId().equals(memberId))
                .toList();
    }


    @Override
    public Reservation save(Reservation reservation) {
        long currentIndex = index.incrementAndGet();

        reservations.add(reservation.assignId(currentIndex));
        return reservation.assignId(currentIndex);
    }

    @Override
    public void deleteById(Long id) {
        Optional<Reservation> findReservation = reservations.stream()
                .filter(reservation -> Objects.equals(reservation.getId(), id))
                .findAny();

        if (findReservation.isEmpty()) {
            return;
        }

        Reservation reservation = findReservation.get();
        reservations.remove(reservation);

    }

    @Override
    public List<Reservation> findByDateAndThemeId(LocalDate date, Long themeId) {
        return reservations.stream()
                .filter(reservation -> reservation.getDate().equals(date) && reservation.themeId().equals(themeId))
                .toList();
    }

    private boolean matchMemberId(Reservation reservation, Long memberId) {
        return memberId == null || reservation.memberId().equals(memberId);
    }

    private boolean matchThemeId(Reservation reservation, Long themeId) {
        return themeId == null || reservation.themeId().equals(themeId);
    }

    private boolean matchDateRange(Reservation reservation, LocalDate dateFrom, LocalDate dateTo) {
        if (dateFrom == null && dateTo == null) {
            return true;
        }
        LocalDate reservationDate = reservation.getDate();
        if (dateFrom != null && reservationDate.isBefore(dateFrom)) {
            return false;
        }
        if (dateTo != null && reservationDate.isAfter(dateTo)) {
            return false;
        }
        return true;
    }
}
