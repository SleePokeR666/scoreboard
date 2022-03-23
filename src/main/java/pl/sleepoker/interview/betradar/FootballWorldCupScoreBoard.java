package pl.sleepoker.interview.betradar;

import pl.sleepoker.interview.betradar.exception.ScoreBoardException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isAnyBlank;
import static org.apache.commons.lang3.StringUtils.join;

public class FootballWorldCupScoreBoard {

    private final Map<String, FootballGame> gamesInProgressMap;

    private static final String KEY_DELIMITER = "-";

    public FootballWorldCupScoreBoard() {
        this.gamesInProgressMap = new HashMap<>();
    }

    public void startGame(String homeTeamName, String awayTeamName) {
        if (isAnyBlank(homeTeamName, awayTeamName)) {
            throw new ScoreBoardException("The game can't be started. At least one team has invalid name.");
        }

        var inProgressGame = gamesInProgressMap.putIfAbsent(
                getKey(homeTeamName, awayTeamName),
                new FootballGame(homeTeamName, awayTeamName)
        );

        if (nonNull(inProgressGame)) {
            throw new ScoreBoardException("The game is already running.");
        }
    }

    public Optional<FootballGame> find(String homeTeamName, String awayTeamName) {
        return ofNullable(gamesInProgressMap.get(getKey(homeTeamName, awayTeamName)));
    }

    private String getKey(String homeTeamName, String awayTeamName) {
        return join(homeTeamName, KEY_DELIMITER, awayTeamName);
    }

}
