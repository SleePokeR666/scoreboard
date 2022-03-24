package pl.sleepoker.interview.betradar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.sleepoker.interview.betradar.FootballGame.Score;
import pl.sleepoker.interview.betradar.FootballWorldCupScoreBoard.Team;
import pl.sleepoker.interview.betradar.exception.ScoreBoardException;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FootballWorldCupScoreBoardTest {

    private FootballWorldCupScoreBoard scoreBoard;

    @BeforeEach
    public void setup() {
        scoreBoard = new FootballWorldCupScoreBoard(new GameRepository());
    }

    @Test
    public void whenGameStartsThenItHasInitialScore() {
        // Given
        String givenHomeTeamName = "home";
        String givenAwayTeamName = "away";
        // When
        scoreBoard.startGame(givenHomeTeamName, givenAwayTeamName);
        // Then
        FootballGame actualGame = scoreBoard.find(givenHomeTeamName, givenAwayTeamName).orElseThrow();
        assertEquals(new Score(0, 0), actualGame.getScore());
    }

    @Test
    public void whenGameIsAlreadyRunningAndWeStartItAgainThenThrow() {
        // Given
        String givenHomeTeamName = "home";
        String givenAwayTeamName = "away";
        scoreBoard.startGame(givenHomeTeamName, givenAwayTeamName);
        // When
        Executable underTest = () -> scoreBoard.startGame(givenHomeTeamName, givenAwayTeamName);
        // Then
        assertThrows(ScoreBoardException.class, underTest);
    }

    @ParameterizedTest
    @MethodSource("invalidFootballTeamNames")
    public void whenTeamNameIsNotValidAndWeStartGameThenThrow(String givenHomeTeamName, String givenAwayTeamName) {
        // When
        Executable underTest = () -> scoreBoard.startGame(givenHomeTeamName, givenAwayTeamName);
        // Then
        assertThrows(ScoreBoardException.class, underTest);
    }

    @Test
    public void whenFinishGameThenItHasFinishedStatus() {
        // Given
        String givenHomeTeamName = "home";
        String givenAwayTeamName = "away";
        scoreBoard.startGame(givenHomeTeamName, givenAwayTeamName);
        // When
        scoreBoard.finishGame(givenHomeTeamName, givenAwayTeamName);
        // Then
        var actualGame = scoreBoard.find(givenHomeTeamName, givenAwayTeamName).orElseThrow();
        assertEquals(GameStatus.FINISHED, actualGame.getGameStatus());
    }

    @ParameterizedTest
    @MethodSource("invalidFootballTeamNames")
    public void whenTeamNameIsNotValidAndWeFinishGameThenThrow(String givenHomeTeamName, String givenAwayTeamName) {
        // When
        Executable underTest = () -> scoreBoard.finishGame(givenHomeTeamName, givenAwayTeamName);
        // Then
        assertThrows(ScoreBoardException.class, underTest);
    }

    @Test
    public void whenFinishGameAndItIsNotInProgressThenThrow() {
        // Given
        String givenHomeTeamName = "home";
        String givenAwayTeamName = "away";
        // When
        Executable underTest = () -> scoreBoard.finishGame(givenHomeTeamName, givenAwayTeamName);
        // Then
        assertThrows(ScoreBoardException.class, underTest);
    }

    @Test
    public void whenUpdateScoreAndGameIsInProgressThenPreviousScoreShouldBeUpdated() {
        // Given
        String givenHomeTeamName = "home";
        String givenAwayTeamName = "away";
        scoreBoard.startGame(givenHomeTeamName, givenAwayTeamName);

        Score givenScore = new Score(3, 4);
        // When
        scoreBoard.updateScore(givenHomeTeamName, givenAwayTeamName, givenScore);
        // Then
        var actualGame = scoreBoard.find(givenHomeTeamName, givenAwayTeamName).orElseThrow();
        assertEquals(new Score(3, 4), actualGame.getScore());
    }

    @Test
    public void whenUpdateScoreAndGameIsFinishedThenPreviousScoreShouldBeUpdated() {
        // Given
        String givenHomeTeamName = "home";
        String givenAwayTeamName = "away";
        scoreBoard.startGame(givenHomeTeamName, givenAwayTeamName);
        scoreBoard.finishGame(givenHomeTeamName, givenAwayTeamName);

        Score givenScore = new Score(3, 4);
        // When
        scoreBoard.updateScore(givenHomeTeamName, givenAwayTeamName, givenScore);
        // Then
        var actualGame = scoreBoard.find(givenHomeTeamName, givenAwayTeamName).orElseThrow();
        assertEquals(new Score(3, 4), actualGame.getScore());
    }

    @Test
    public void whenUpdateScoreAndGameIsNotFoundThenThrow() {
        // Given
        String givenHomeTeamName = "home";
        String givenAwayTeamName = "away";
        Score givenScore = new Score(3, 4);
        // When
        Executable underTest = () -> scoreBoard.updateScore(givenHomeTeamName, givenAwayTeamName, givenScore);
        // Then
        assertThrows(ScoreBoardException.class, underTest);
    }

    @ParameterizedTest
    @MethodSource("updateTeamScoreTestData")
    public void whenUpdateTeamScoreThenItShouldBeIncrementedByOne(Team givenTeamScore, Score expectedScore) {
        // Given
        String givenHomeTeamName = "home";
        String givenAwayTeamName = "away";
        scoreBoard.startGame(givenHomeTeamName, givenAwayTeamName);
        // When
        scoreBoard.updateTeamScore(givenHomeTeamName, givenAwayTeamName, givenTeamScore);
        // Then
        var actualGame = scoreBoard.find(givenHomeTeamName, givenAwayTeamName).orElseThrow();
        assertEquals(expectedScore, actualGame.getScore());
    }

    @ParameterizedTest
    @MethodSource("updateTeamScoreTestData")
    public void whenUpdateTeamScoreAndGameIsNotRunningThenThrows(Team givenTeamScore, Score expectedScore) {
        // Given
        String givenHomeTeamName = "home";
        String givenAwayTeamName = "away";
        scoreBoard.startGame(givenHomeTeamName, givenAwayTeamName);
        scoreBoard.finishGame(givenHomeTeamName, givenAwayTeamName);
        // When
        Executable underTest = () -> scoreBoard.updateTeamScore(givenHomeTeamName, givenAwayTeamName, givenTeamScore);
        // Then
        assertThrows(ScoreBoardException.class, underTest);
    }

    static Stream<Arguments> invalidFootballTeamNames() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of("", ""),
                Arguments.of(null, ""),
                Arguments.of("", null),
                Arguments.of(null, "away"),
                Arguments.of("home", null),
                Arguments.of("", "away"),
                Arguments.of("home", "")
        );
    }

    static Stream<Arguments> updateTeamScoreTestData() {
        return Stream.of(
                Arguments.of(Team.HOME, new Score(1, 0)),
                Arguments.of(Team.AWAY, new Score(0, 1))
        );
    }

}
