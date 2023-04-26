package models

data class Match (
    var matchId: Int = 0,
    var minPlayed : Int,
    var matchWon:Boolean = false){

    override fun toString() =
        if (matchWon)
            "$matchId: $minPlayed (Complete)"
        else
            "$matchId: $minPlayed (TODO)"

}


