package pl.sleepoker.interview.betradar;

import pl.sleepoker.interview.betradar.FootballGame.Score;
import pl.sleepoker.interview.betradar.FootballGame.Team;
import pl.sleepoker.interview.betradar.exception.ScoreBoardException;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
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
        gamesInProgressMap.remove(key);
    }

    public void updateScore(String homeTeamName, String awayTeamName, Score score) {
        checkTeamNames(homeTeamName, awayTeamName, "update");

        String key = getKey(homeTeamName, awayTeamName);
        var game = gamesInProgressMap.get(key);
        if (nonNull(game)) {
            game.setScore(score);
            return;
        }

        game = gameRepository.get(key)
                .orElseThrow(() -> new ScoreBoardException("Can't update game score. Game hasn't started yet"));
        game.setScore(score);
        gameRepository.save(game);
    }

    public void updateTeamScore(String homeTeamName, String awayTeamName, Team team) {
        checkTeamNames(homeTeamName, awayTeamName, "update");

        String key = getKey(homeTeamName, awayTeamName);
        var game = gamesInProgressMap.get(key);
        if (isNull(game)) {
            throw new ScoreBoardException("Can't update game score. Game is not in progress.");
        }

        game.getScore().score(team);
    }

    public List<FootballGame> getSummary() {
        List<FootballGame> allGames = gameRepository.findAll();
        Comparator<FootballGame> byTotalDesc = Comparator.comparing(FootballGame::getTotal).reversed();
        Comparator<FootballGame> byCreatedDateDesc = Comparator.comparing(FootballGame::getCreated).reversed();
        return allGames.stream()
                .sorted(byTotalDesc.thenComparing(byCreatedDateDesc))
                .collect(toList());
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

}
