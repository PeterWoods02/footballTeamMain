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

}



