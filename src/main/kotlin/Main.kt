import controllers.PlayerAPI
import models.Player
import models.Match
import mu.KotlinLogging
import persistence.XMLSerializer
import utils.ScannerInput.readNextChar
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.io.File
import java.lang.System.exit
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


private val logger = KotlinLogging.logger {}
private val playerAPI = PlayerAPI(XMLSerializer(File("players.xml")))

//private val PlayerAPI = PlayerAPI(JSONSerializer(File("players.json")))




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
         > |   11) Search by Date of Birth                     |
         > |   12) List above X rating                         |
         > |   13) List from best to worst                     |
         > -----------------------------------------------------  
         > | REPORT MENU FOR Matches                           |                                
         > |   14) Search for all matches                      |
         > |   15) List Lost Matches                           |
         > |   16) List players good game time >60             |
         > |   17) Suggest players to turn pro                 |
         > -----------------------------------------------------  
         > |   20) Save to External File                       |
         > |   21) Load from External File                     |
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
            8 -> deleteAMatch()
            9 -> markMatchStatus()
            10 -> searchPlayers()
            11 -> searchDOB()
            12 -> listAboveRating()
            13 -> listWorstBest()
            14 -> searchMatches()
            15 -> listLostMatches()
            16 -> recSixtyMins()
            17 -> suggestPros()
            20 -> save()
            21 -> load()
            0 -> exitApp()
            else -> println("Invalid menu choice: $option")
        }
    } while (true)
}


fun addPlayer(): String {
    //logger.info { "addPlayer() function invoked" }
    val playerName = readNextLine("Enter the players name: ")

    val dateFormat = SimpleDateFormat("DD-MM-YYYY")
    while (true) {
        val playerDOB = readNextLine("Enter players DOB (DD-MM-YYYY): ")
        try {
            dateFormat.parse(playerDOB)

            val playerRating = readNextInt("Enter player rating (1-10): ")
            val isAdded =
                playerAPI.add(Player(playerName = playerName, playerDOB = playerDOB, playerRating = playerRating))
            if (isAdded) {
                println("Added Successfully")
            } else {
                println("Add Failed")
            }
            return playerDOB
        } catch (e: ParseException) {
            println("Invalid date. Please enter your date of players birth in the format DD-MM-YYYY:")
        }
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


fun searchDOB() {
    val searchDOB = readNextLine("Enter a Date (DD-MM-YYYY): ")
    val dateFormat = SimpleDateFormat("DD-MM-YYYY")
    try {
        dateFormat.parse(searchDOB) // try to parse the user input as a date
        val searchResults = playerAPI.searchPlayerByDOB(searchDOB)
        if (searchResults.isEmpty()) {
            println("No players found")
        } else {
            println(searchResults)
        }
    } catch (e: ParseException) {
        println("Invalid date") //stackOverflow to catch incorrect date input when going from string to date input
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



fun listAboveRating(){
    val searchRating = readNextInt("Enter rating to search by: ")
    val searchResults = playerAPI.aboveRating(searchRating)
    if (searchResults.isEmpty()) {
        println("No players found")
    } else {
        println(searchResults)
    }
}

fun listWorstBest(){
    if (playerAPI.numberOfPlayers() > 0) {
        val option = readNextInt(
            """
                  > -------------------------------------------
                  > |   1) List by Highest - Lowest rating     |
                  > |   2) List by Lowest - Highest rating     |
                  > -------------------------------------------
         > ==>> """.trimMargin(">"))

        when (option) {
            1 -> playerAPI.listByMost()
            2 -> playerAPI.listByLeast()
            else -> println("Invalid option entered: $option")
        }
        println(playerAPI.listAllPlayers())
    } else {
        println("Option Invalid - No players stored")
    }
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


fun deleteAMatch() {
    val player: Player? = askUserToChooseAmateurPlayer()
    if (player != null) {
        val match: Match? = askUserToChooseMatch(player)
        if (match != null) {
            val isDeleted = player.delete(match.matchId)
            if (isDeleted) {
                println("Delete Successful!")
            } else {
                println("Delete NOT Successful")
            }
        }
    }
}

fun markMatchStatus() {
    val player: Player? = askUserToChooseAmateurPlayer()
    if (player != null) {
        val match: Match? = askUserToChooseMatch(player)
        if (match != null) {
            var changeStatus = 'X'
            if (match.matchWon) {
                changeStatus = readNextChar("The match has been won, Mark as Complete? (Y)")
                if ((changeStatus == 'Y') ||  (changeStatus == 'y'))
                    match.matchWon = false
            }
            else {
                changeStatus = readNextChar("The match has been lost, mark as complete?? (Y)")
                if ((changeStatus == 'Y') ||  (changeStatus == 'y'))
                    match.matchWon = true
            }
        }
    }
}
fun searchMatches() {
    val searchMins = readNextLine("Enter the minutes played to search by: ")
    val searchResults = playerAPI.searchMatchesByMins(searchMins)
    if (searchResults.isEmpty()) {
        println("No matches found")
    } else {
        println(searchResults)
    }
}





fun listLostMatches(){
    if (playerAPI.numberOfMatchesWon() > 0) {
        println("Total Lost matches: ${playerAPI.numberOfMatchesWon()}")
    }
    println(playerAPI.listToPlayMatches())
}

fun recSixtyMins(){
    if(playerAPI.numberOfPlayers()>0){

        println("Players with more than 60 mins played: \n ${playerAPI.playersSixtyMins()} ")
    }
    else
println("No players")
}


fun suggestPros(){
    if(playerAPI.numberOfPlayers()>0){

        println("Suggestions to turn pro: \n ${playerAPI.suggestPro()} ")
    }
    println("No players")
}





fun save() {
    try {
        playerAPI.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun load() {
    try {
        playerAPI.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}




fun exitApp(){
        println("Exiting...bye")
        exit(0)
    }

















