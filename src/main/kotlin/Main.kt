import controllers.PlayerAPI
import models.Player
import models.Match
import mu.KotlinLogging
import utils.ScannerInput
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.lang.System.exit
import java.util.*
import kotlin.system.exitProcess


private val logger = KotlinLogging.logger {}
private val playerAPI = PlayerAPI()



fun main(args: Array<String>) {
    runMenu()
}

    val scanner = Scanner(System.`in`)



fun mainMenu() = readNextInt(
    """ 
         > -----------------------------------------------------  
         > |                  PLAYER KEEPER APP                |
         > -----------------------------------------------------  
         > | Player MENU                                       |
         > |   1) Add a player                                 | 
         > |   2) List all players                             |
         > |   3) Update a player                              |
         > |   4) Delete a player                              |
         > |   5) Turn Player Pro                              |
         > -----------------------------------------------------  
         > | Match MENU                                        | 
         > |   6) Add Match to a Player                        |
         > |   7) Update Match content for a Player            |
         > |   8) Delete match from Player                     |
         > |   9) Mark Match as Won                            | 
         > -----------------------------------------------------  
         > | REPORT MENU FOR PLAYERS                           | 
         > |   10) Search for all player by Name               |
         > |   11) .....                                       |
         > |   12) .....                                       |
         > |   13) .....                                       |
         > |   14) .....                                       |
         > -----------------------------------------------------  
         > | REPORT MENU FOR Matches                           |                                
         > |   15) Search for all matches                      |
         > |   16) List TODO Matches                           |
         > |   17) .....                                       |
         > |   18) .....                                       |
         > |   19) .....                                       |
         > -----------------------------------------------------  
         > |   0) Exit                                         |
         > -----------------------------------------------------  
         > ==>> """.trimMargin(">")
)



fun runMenu() {
    do {
        when (val option = mainMenu()) {
            1  -> addPlayer()
            2  -> listPlayers()
            3  -> updatePlayer()
            4  -> deletePlayer()
            5 -> turnPro()
            6 -> addMatchToPlayer()
            7 -> updateMatchContentsInPlayer()
            //8 -> deleteAMatch()
            //9 -> markMatchStatus()
            10 -> searchPlayers()
            //15 -> searchMatches()
            //16 -> listToDoMatches()
            0 -> exitApp()
            else -> println("Invalid menu choice: $option")
        }
    } while (true)
}


fun addPlayer(){
    //logger.info { "addPlayer() function invoked" }
    val playerName = readNextLine("Enter the players name: ")
    val playerDOB = readNextLine("Enter players DOB (DD-MM-YYYY): ")
    val playerRating = readNextInt("Enter player rating (1-10): ")
    val isAdded = playerAPI.add(Player(playerName = playerName, playerDOB = playerDOB, playerRating = playerRating))

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}


fun listPlayers() {
    if (playerAPI.numberOfPlayers() > 0) {
        val option = readNextInt(
            """
                  > --------------------------------
                  > |   1) View ALL players        |
                  > |   2) View PRO players        |
                  > |   3) View Amateur players    |
                  > --------------------------------
         > ==>> """.trimMargin(">")
        )

        when (option) {
            1 -> listAllPlayers()
            2 -> listProPlayers()
            3 -> listAmateurPlayers()
            else -> println("Invalid option entered: $option")
        }
    } else {
        println("Option Invalid - No players stored")
    }
}

fun listAllPlayers() = println(playerAPI.listAllPlayers())
fun listProPlayers() = println(playerAPI.listProPlayers())
fun listAmateurPlayers() = println(playerAPI.listAmateurPlayers())



fun updatePlayer() {
    listPlayers()
    if (playerAPI.numberOfPlayers() > 0) {
        // only ask the user to choose the player if players exist
        val id = readNextInt("Enter the id of the player to update: ")
        if (playerAPI.findPlayer(id) != null) {
            val playerName = readNextLine("Enter the Players Name: ")
            val playerDOB = readNextLine("Enter Players DOB ( DD-MM-YYYY ): ")
            val playerRating = readNextInt("Enter a rating for Player ( 1-10 ): ")


            // pass the index of the player and details and a go to playerAPI for updating and check for success.
            if (playerAPI.update(id, Player(0, playerName, playerDOB, playerRating , false))){
                println("Update Successful")
            } else {
                println("Update Failed")
            }
        } else {
            println("There are no players for this index number")
        }
    }
}


fun deletePlayer() {
    listPlayers()
    if (playerAPI.numberOfPlayers() > 0) {
        // only ask the user to choose the player to delete if players exist
        val id = readNextInt("Enter the id of the player to delete: ")
        // pass the index of the player to playerAPI for deleting and check for success.
        val playerToDelete = playerAPI.delete(id)
        if (playerToDelete) {
            println("Delete Successful!")
        } else {
            println("Delete NOT Successful")
        }
    }
}

fun turnPro() {
    listAmateurPlayers()
    if (playerAPI.numberOfAmateurPlayers() > 0) {
        // only ask the user to choose the player to turn pro if AMateur players exist
        val id = readNextInt("Enter the id of the player to turn pro: ")
        // pass the index of the player to playerAPI  and check for success.
        if (playerAPI.turnPro(id)) {
            println("Promotion Successful")
        } else {
            println("Promotion Unsuccessful")
        }
    }
}


fun searchPlayers() {
    val searchName = readNextLine("Enter the name to search by: ")
    val searchResults = playerAPI.searchPlayerByName(searchName)
    if (searchResults.isEmpty()) {
        println("No players found")
    } else {
        println(searchResults)
    }
}


//HELPER FUNCTIONS

private fun askUserToChooseAmateurPlayer(): Player? {
    listAmateurPlayers()
    if (playerAPI.numberOfAmateurPlayers() > 0) {
        val player = playerAPI.findPlayer(readNextInt("\nEnter the id of the player: "))
        if (player != null) {
            if (player.isPlayerPro) {
                println("Player is Pro")
            } else {
                return player //chosen player is amateur
            }
        } else {
            println("Player id is not valid")
        }
    }
    return null //selected player is pro
}


private fun addMatchToPlayer() {
    val player: Player? = askUserToChooseAmateurPlayer()
    if (player != null) {
        if (player.addMatch(Match(minPlayed = readNextInt("\t Minutes Played: "))))
            println("Add Successful!")
        else println("Add NOT Successful")
    }
}


fun updateMatchContentsInPlayer() {
    val player :Player? = askUserToChooseAmateurPlayer()
    if (player != null) {
        val match :Match? = askUserToChooseMatch(player)
        if (match != null) {
            val updatedMins = readNextInt("Enter new minutes played: ")
            if (player.update(match.matchId, Match(minPlayed = updatedMins))) {
                println("Minutes played updated")
            } else {
                println("Minutes played NOT updated")
            }
        } else {
            println("Invalid Match Id")
        }
    }
}

private fun askUserToChooseMatch(player :Player): Match? {
    if (player.numberOfMatches() > 0) {
        print(player.listMatches())
        return player.findOne(readNextInt("\nEnter the id of the match: "))
    }
    else{
        println ("No matches for chosen player")
        return null
    }
}





fun exitApp(){
        println("Exiting...bye")
        exit(0)
    }

















