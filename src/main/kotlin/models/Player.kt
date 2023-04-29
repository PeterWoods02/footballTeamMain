package models
import utils.Utilities

data class Player(
    var playerId: Int = 0,
    var playerName: String,
    var playerDOB: String,
    var playerRating: Int,
    var isPlayerPro: Boolean = false,
    var matches: MutableSet<Match> = mutableSetOf()
) {

    private var lastMatchId = 0
    private fun getMatchId() = lastMatchId++

    fun addMatch(match: Match): Boolean {
        match.matchId = getMatchId()
        return matches.add(match)
    }

    fun numberOfMatches() = matches.size

    fun findOne(id: Int): Match? {
        return matches.find { match -> match.matchId == id }
    }

    fun delete(id: Int): Boolean {
        return matches.removeIf { match -> match.matchId == id }
    }

    fun update(id: Int, newMatch: Match): Boolean {
        val foundMatch = findOne(id)
        // if the object exists, use the details passed in the newMatch parameter to
        // update the found object in the Set
        if (foundMatch != null) {
            foundMatch.minPlayed = newMatch.minPlayed
            foundMatch.matchWon = newMatch.matchWon
            return true
        }
        // if the object was not found, return false, indicating that the update was not successful
        return false
    }

    fun listMatches() =
        if (matches.isEmpty()) {
            "\tNO MATCHES ADDED"
        } else {
            Utilities.formatSetString(matches)
        }

    override fun toString(): String {
        val pro = if (isPlayerPro) 'Y' else 'N'
        return "$playerId: $playerName, DOB($playerDOB), Rating($playerRating), PRO($pro) \n${listMatches()}"
    }
}
