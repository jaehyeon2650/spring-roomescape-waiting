package roomescape.theme.infrastructure.jpa;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.ReservationPeriod;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeRepository;

@Repository
public class ThemeJpaRepository implements ThemeRepository {

    private final JpaThemeRepository jpaThemeRepository;

    public ThemeJpaRepository(JpaThemeRepository jpaThemeRepository) {
        this.jpaThemeRepository = jpaThemeRepository;
    }

    @Override
    public Theme save(Theme theme) {
        return jpaThemeRepository.save(theme);
    }

    @Override
    public List<Theme> findPopularThemes(ReservationPeriod period, int popularCount) {
        return jpaThemeRepository.findPopularThemes(period, popularCount);
    }

    @Override
    public List<Theme> findAll() {
        return jpaThemeRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        jpaThemeRepository.deleteById(id);
    }

    @Override
    public Optional<Theme> findById(Long id) {
        return jpaThemeRepository.findById(id);
    }
}
