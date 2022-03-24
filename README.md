1) I suppose, that normally we would have the games already created in repository, before they go to the LiveBoard.
But since we need to simplify the task and there is nothing said about it in the requirements, I've decided to
use "homeTeamName-awayTeamName" instead of ID. If we'd operate with ID's methods and Map keys would look
a bit better.
2) Decided not to cover all the methods with tests for the cases where we pass invalid team names, the tests
are pretty similar.
3) AC-3. For the full score update, decided to update both running match and already completed match. Since
as far as I know, there is possibility we would need this (mistake during live, maybe some super rare decisions
during WorldCup, etc.). But for the separate score of away or home team, decided to go only for Live matches.
