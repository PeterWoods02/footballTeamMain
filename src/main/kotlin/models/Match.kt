package models

data class Match(
    var matchId: Int = 0,
    var minPlayed: Int,
    var matchWon: Boolean = false
) {

    override fun toString() =
        if (matchWon) {
            "$matchId: $minPlayed minutes (Won)"
        } else {
            "$matchId: $minPlayed minutes (Lost)"
        }
}
