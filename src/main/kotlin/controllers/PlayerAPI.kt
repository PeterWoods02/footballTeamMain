package controllers

import models.Player
import persistence.Serializer
import utils.Utilities.formatListString
import java.text.SimpleDateFormat

class PlayerAPI(serializerType: Serializer) {

    private var serializer: Serializer = serializerType

    private var players = ArrayList<Player>()

    fun add(player: Player): Boolean {
        val newPlayerId = if (players.isEmpty()) 1 else players.maxByOrNull { it.playerId }!!.playerId + 1
        player.playerId = newPlayerId
        return players.add(player)
    }

    fun delete(id: Int) = players.removeIf { player -> player.playerId == id }

    fun update(id: Int, player: Player?): Boolean {
        // find the player object by the index number
        val foundPlayer = findPlayer(id)

        // if the player exists, use the player details passed as parameters to update the found player in the ArrayList.
        if ((foundPlayer != null) && (player != null)) {
            foundPlayer.playerName = player.playerName
            foundPlayer.playerDOB = player.playerDOB
            foundPlayer.playerRating = player.playerRating
            return true
        }
        // if the player was not found, return false, indicating that the update was not successful
        return false
    }

    /* this is for list best to worst and visa versa
    fun getAllPlayers(): List<Player> {
        return players.toList()
    }
*/
    fun turnPro(id: Int): Boolean {
        val foundPlayer = findPlayer(id)
        if ((foundPlayer != null) && (!foundPlayer.isPlayerPro)

        ) {
            foundPlayer.isPlayerPro = true
            return true
        }
        return false
    }

    fun listAllPlayers(): String {
        return if (players.isEmpty()) {
            "No players stored"
        } else {
            var listOfPlayers = ""
            for (i in players.indices) {
                listOfPlayers += "$i: ${players[i]} \n"
            }
            listOfPlayers
        }
    }

    fun numberOfPlayers(): Int {
        return players.size
    }

    fun findPlayer(playerId: Int) = players.find { player -> player.playerId == playerId }

    fun searchPlayerByName(searchString: String) =
        formatListString(players.filter { player -> player.playerName.contains(searchString, ignoreCase = true) })

    fun searchPlayerByDOB(userInputDate: String): String {
        if (numberOfPlayers() == 0) return "No players stored"
        var listOfPlayers = ""
        val dateFormat = SimpleDateFormat("DD-MM-YYYY")
        val inputDate = dateFormat.parse(userInputDate)
        for (player in players) {
            val playerDOB = dateFormat.parse(player.playerDOB)
            if (playerDOB.after(inputDate)) {
                listOfPlayers += "${player.playerId}: ${player.playerName} ${player.playerDOB} \n"
            }
        }

        return if (listOfPlayers == "") "No matches found for: $userInputDate" else listOfPlayers
    }

    fun aboveRating(searchInt: Int) =
        formatListString(players.filter { player -> player.playerRating >= searchInt })

    // list only players that are amateur
    fun listAmateurPlayers(): String =
        if (numberOfAmateurPlayers() == 0) {
            "No Amateur Players !"
        } else {
            formatListString(players.filter { player -> !player.isPlayerPro })
        }

    // list pro players
    fun listProPlayers(): String =
        if (numberOfProPlayers() == 0) {
            "No Pro Players"
        } else {
            formatListString(players.filter { player -> player.isPlayerPro })
        }

    // returns number of players that are pro
    fun numberOfProPlayers(): Int = players.count { player: Player -> player.isPlayerPro }

    // returns number of players that are amateur
    fun numberOfAmateurPlayers(): Int = players.count { player: Player -> !player.isPlayerPro }

// methods to run for testing
    // List by highest to the lowest rating
    fun listByWorst(players: List<Player?>): List<Player?> {
        return players.sortedBy { it?.playerRating }
    }

    // list by lowest to the highest rating
    fun listByBest(players: List<Player?>): List<Player?> {
        return players.sortedByDescending { it?.playerRating }
    }

    // List by highest to the lowest rated players
    fun listByLeast() = players.sortBy { it.playerRating }.toString()

    // list by lowest to the highest prated players
    fun listByMost() = players.sortByDescending { it.playerRating }.toString()

    fun searchMatchesByMinutes(searchString: String): String {
        return if (numberOfPlayers() == 0) {
            "No players stored"
        } else {
            var listOfPlayers = ""
            for (player in players) {
                for (match in player.matches) {
                    if (match.minPlayed.toString() >= searchString) {
                        listOfPlayers += "${player.playerId}: ${player.playerName} \n\t${match}\n"
                    }
                }
            }
            if (listOfPlayers == "") {
                "No matches found for: $searchString"
            } else {
                listOfPlayers
            }
        }
    }

    //  listing matches

    fun listToPlayMatches(): String =
        if (numberOfPlayers() == 0) {
            "No players stored"
        } else {
            var listOfToMatches = ""
            for (player in players) {
                for (match in player.matches) {
                    if (!match.matchWon) {
                        listOfToMatches += player.playerName + ": " + match.minPlayed + " minutes played" + "\n"
                    }
                }
            }
            listOfToMatches
        }

    //  counting for matches

    fun numberOfMatchesWon(): Int {
        var numberOfMatchesToPlay = 0
        for (player in players) {
            for (match in player.matches) {
                if (!match.matchWon) {
                    numberOfMatchesToPlay++
                }
            }
        }
        return numberOfMatchesToPlay
    }

    fun playersSixtyMinutes(): String {
        var playersSixty = ""
        for (player in players) {
            for (match in player.matches) {
                if (match.minPlayed > 60) {
                    playersSixty += player.toString()
                }
            }
        }
        return playersSixty
    }

    fun suggestPro(): String {
        var suggestPro = ""
        for (player in players) {
            if (player.playerRating >= 7) {
                for (match in player.matches) {
                    if (match.minPlayed > 45 && match.matchWon) {
                        suggestPro += player.toString()
                    }
                }
            }
        }
        return suggestPro
    }

    @Suppress("UNCHECKED_CAST")
    @Throws(Exception::class)
    fun load() {
        players = serializer.read() as ArrayList<Player>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(players)
    }
}
