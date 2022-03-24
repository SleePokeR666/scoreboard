package pl.sleepoker.interview.betradar;

import java.time.Instant;
import java.util.Objects;

import static java.time.Instant.now;

public class FootballGame {

    private final String homeTeamName;
    private final String awayTeamName;
    private Score score;
    private GameStatus gameStatus;
    private Instant created;

    public FootballGame(String homeTeamName, String awayTeamName) {
        this.homeTeamName = homeTeamName;
        this.awayTeamName = awayTeamName;
        this.score = new Score();
        this.gameStatus = GameStatus.IN_PROGRESS;
        this.created = now();
    }

    public FootballGame(String homeTeamName, String awayTeamName, Score score, GameStatus gameStatus, Instant created) {
        this.homeTeamName = homeTeamName;
        this.awayTeamName = awayTeamName;
        this.score = score;
        this.gameStatus = gameStatus;
        this.created = created;
    }

    public void complete() {
        this.gameStatus = GameStatus.FINISHED;
    }

    public int getTotal() {
        return getScore().getHomeTeam() + getScore().getAwayTeam();
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

    public Instant getCreated() {
        return created;
    }

    public void setScore(Score score) {
        this.score = score;
    }


    public static class Score {

        private int homeTeam;
        private int awayTeam;

        public Score() {
            this.homeTeam = 0;
            this.awayTeam = 0;
        }

        public Score(int homeTeam, int awayTeam) {
            this.homeTeam = homeTeam;
            this.awayTeam = awayTeam;
        }

        public void score(Team team) {
            switch (team) {
                case HOME -> homeTeam++;
                case AWAY -> awayTeam++;
            }
        }

        public int getHomeTeam() {
            return homeTeam;
        }

        public int getAwayTeam() {
            return awayTeam;
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

    enum Team {
        HOME, AWAY
    }
}
