package pl.sleepoker.interview.betradar;

import java.util.Objects;

public class FootballGame {

    private final String homeTeamName;
    private final String awayTeamName;
    private final Score score;
    private GameStatus gameStatus;

    public FootballGame(String homeTeamName, String awayTeamName) {
        this.homeTeamName = homeTeamName;
        this.awayTeamName = awayTeamName;
        this.score = new Score();
        this.gameStatus = GameStatus.IN_PROGRESS;
    }

    public void complete() {
        this.gameStatus = GameStatus.FINISHED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FootballGame that = (FootballGame) o;
        return homeTeamName.equals(that.homeTeamName) && awayTeamName.equals(that.awayTeamName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(homeTeamName, awayTeamName);
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public String getAwayTeamName() {
        return awayTeamName;
    }

    public Score getScore() {
        return score;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public static class Score {

        private final int homeTeam;
        private final int awayTeam;

        public Score() {
            this.homeTeam = 0;
            this.awayTeam = 0;
        }

        public Score(int homeTeam, int awayTeam) {
            this.homeTeam = homeTeam;
            this.awayTeam = awayTeam;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Score score = (Score) o;
            return homeTeam == score.homeTeam && awayTeam == score.awayTeam;
        }

        @Override
        public int hashCode() {
            return Objects.hash(homeTeam, awayTeam);
        }
    }
}
