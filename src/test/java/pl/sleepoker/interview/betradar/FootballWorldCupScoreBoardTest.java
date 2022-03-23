package pl.sleepoker.interview.betradar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.sleepoker.interview.betradar.FootballGame.Score;
import pl.sleepoker.interview.betradar.exception.ScoreBoardException;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FootballWorldCupScoreBoardTest {

    private FootballWorldCupScoreBoard scoreBoard;

    @BeforeEach
    public void setup() {
        scoreBoard = new FootballWorldCupScoreBoard();
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

}
