package models

data class Player(
    var playerId: Int = 0,
    var playerName: String,
    var playerDOB: String,
    var playerRating: Int,
    var isPlayerPro :Boolean = false){
}
