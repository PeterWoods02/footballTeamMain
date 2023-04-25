package controllers

import models.Player

class PlayerAPI {
    private var players = ArrayList<Player>()


    fun add(player: Player): Boolean {
        return players.add(player)
    }

    fun listAllPlayers(): String {
        return if (players.isEmpty()) {
            "No players stored"
        } else {
            var listOfPlayers = ""
            for (i in players.indices) {
                listOfPlayers += "${i}: ${players[i]} \n"
            }
            listOfPlayers
        }
    }


    fun numberOfPlayers(): Int {
        return players.size
    }

    fun findPlayer(index: Int): Player? {
        return if (isValidListIndex(index, players)) {
            players[index]
        } else null
    }

    //utility method to determine if an index is valid in a list.
    fun isValidListIndex(index: Int, list: List<Any>): Boolean {
        return (index >= 0 && index < list.size)
    }

    //list only players that are amateur
    fun listAmateurPlayers(): String =
        if  (numberOfAmateurPlayers() == 0)  "No Amateur Players !"
        else formatListString(players.filter { player -> !player.isPlayerPro})
    //list pro players
    fun listProPlayers(): String =
        if (numberOfProPlayers() == 0) "No Pro Players"
        else formatListString(players.filter { player -> player.isPlayerPro})



    //returns number of players that are pro
    fun numberOfProPlayers(): Int = players.count { player: Player -> player.isPlayerPro }

    //returns number of players that are amateur
    fun numberOfAmateurPlayers(): Int = players.count { player: Player -> !player.isPlayerPro }


    //List by highest to the lowest rating
    fun listByWorst(players: List<Player?>): List<Player?> {return players.sortedBy { it?.playerRating }
    }
    //list by lowest to the highest rating
    fun listByBest(players: List<Player?>): List<Player?> { return players.sortedByDescending { it?.playerRating }
    }

    //returns number of players based on rating
    fun numberOfPlayersByRating(rating: Int): Int = players.count { player: Player -> player.playerRating == rating}





    //removes duplication within PlayerAPI
    private fun formatListString(playersToFormat : List<Player>) : String =
        playersToFormat
            .joinToString (separator = "\n") { player ->
                players.indexOf(player).toString() + ": " + player.toString() }


}



