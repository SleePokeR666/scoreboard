package pl.sleepoker.interview.betradar;

import java.util.*;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.join;

public class GameRepository {

    private final Map<String, FootballGame> gameSummary;

    private static final String KEY_DELIMITER = "-";

    public GameRepository() {
        gameSummary = new HashMap<>();
    }

    public void save(FootballGame game) {
        String key = getKey(game.getHomeTeamName(), game.getAwayTeamName());
        gameSummary.put(key, game);
    }

    public Optional<FootballGame> get(String key) {
        return ofNullable(gameSummary.get(key));
    }

    public List<FootballGame> findAll() {
        return new ArrayList<>(gameSummary.values());
    }

    private String getKey(String homeTeamName, String awayTeamName) {
        return join(homeTeamName, KEY_DELIMITER, awayTeamName);
    }
}
