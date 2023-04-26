package models

data class Player(val playerId: Int = 0,
    val playerName: String,
                  val playerDOB: String,
                val playerRating: Int,
                val isPlayerPro :Boolean){
}
