package pl.sleepoker.interview.betradar;

public class FootballGame {

    private final String homeTeamName;
    private final String awayTeamName;
    private final Score score;

    public FootballGame(String homeTeamName, String awayTeamName) {
        this.homeTeamName = homeTeamName;
        this.awayTeamName = awayTeamName;
        this.score = new Score();
    }

    public Score getScore() {
        return score;
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
    }
}
