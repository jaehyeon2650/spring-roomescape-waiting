package roomescape.reservation.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationPeriod;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeRepository;

public class FakeThemeRepository implements ThemeRepository {

    private final List<Theme> themes;
    private final List<Reservation> reservations;

    private AtomicLong index = new AtomicLong(0);

    public FakeThemeRepository(List<Theme> themes, List<Reservation> reservations) {
        this.reservations = reservations;
        this.themes = themes;
    }

    @Override
    public Theme save(Theme theme) {
        long currentIndex = index.incrementAndGet();
        themes.add(Theme.createWithId(currentIndex, theme.getName(), theme.getDescription(), theme.getThumbnail()));
        return theme.assignId(currentIndex);
    }

    @Override
    public List<Theme> findPopularThemes(ReservationPeriod period, int popularCount) {
        List<Reservation> filterReservations = reservations.stream()
                .filter(reservation -> !(reservation.getDate().isBefore(period.findStartDate()) || reservation.getDate()
                        .isAfter(period.findEndDate())))
                .toList();

        Map<Theme, Long> themes = new HashMap<>();
        for (Reservation filterReservation : filterReservations) {
            Theme theme = Theme.createWithId(filterReservation.themeId(), filterReservation.themeName(),
                    filterReservation.themeDescription(), filterReservation.themeThumbnail());
            themes.put(theme, themes.getOrDefault(theme, 0L) + 1);
        }

        return themes.entrySet().stream()
                .sorted(Map.Entry.<Theme, Long>comparingByValue().reversed())
                .limit(popularCount)
                .map(Map.Entry::getKey)
                .toList();
    }

    @Override
    public Optional<Theme> findById(Long id) {
        return themes.stream()
                .filter(theme -> Objects.equals(theme.getId(), id))
                .findAny();
    }

    @Override
    public List<Theme> findAll() {
        return Collections.unmodifiableList(themes);
    }

    @Override
    public void deleteById(Long id) {
        Optional<Theme> findTheme = themes.stream()
                .filter(theme -> Objects.equals(theme.getId(), id))
                .findAny();

        if (findTheme.isEmpty()) {
            return;
        }

        Theme theme = findTheme.get();
        themes.remove(theme);
    }
}
