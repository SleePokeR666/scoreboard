package pl.sleepoker.interview.betradar;

import pl.sleepoker.interview.betradar.FootballGame.Score;
import pl.sleepoker.interview.betradar.exception.ScoreBoardException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isAnyBlank;
import static org.apache.commons.lang3.StringUtils.join;

public class FootballWorldCupScoreBoard {

    private final GameRepository gameRepository;
    private final Map<String, FootballGame> gamesInProgressMap;

    private static final String KEY_DELIMITER = "-";

    public FootballWorldCupScoreBoard(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
        this.gamesInProgressMap = new HashMap<>();
    }

    public void startGame(String homeTeamName, String awayTeamName) {
        checkTeamNames(homeTeamName, awayTeamName, "start");

        var inProgressGame = gamesInProgressMap.putIfAbsent(
                getKey(homeTeamName, awayTeamName),
                new FootballGame(homeTeamName, awayTeamName)
        );

        if (nonNull(inProgressGame)) {
            throw new ScoreBoardException("The game is already running.");
        }
    }

    public void finishGame(String homeTeamName, String awayTeamName) {
        checkTeamNames(homeTeamName, awayTeamName, "finish");

        String key = getKey(homeTeamName, awayTeamName);
        if (!gamesInProgressMap.containsKey(key)) {
            throw new ScoreBoardException("Can't finish the game. It hasn't started yet or it is already completed.");
        }

        var game = gamesInProgressMap.get(key);
        game.complete();

        gameRepository.save(game);
    }

    public void updateScore(String homeTeamName, String awayTeamName, Score score) {
        // TODO
    }

    public void updateTeamScore(String homeTeamName, String awayTeamName, Team team) {
        // TODO
    }

    public Optional<FootballGame> find(String homeTeamName, String awayTeamName) {
        String key = getKey(homeTeamName, awayTeamName);
        return ofNullable(gamesInProgressMap.get(key))
                .or(() -> gameRepository.get(key));
    }

    private String getKey(String homeTeamName, String awayTeamName) {
        return join(homeTeamName, KEY_DELIMITER, awayTeamName);
    }

    private void checkTeamNames(String homeTeamName, String awayTeamName, String operation) {
        if (isAnyBlank(homeTeamName, awayTeamName)) {
            String message = String.format("Can't %s the game. At least one team has invalid name.", operation);
            throw new ScoreBoardException(message);
        }
    }

    enum Team {
        HOME, AWAY
    }

}
