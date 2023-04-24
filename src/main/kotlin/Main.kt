import controllers.PlayerAPI
import models.Player
import mu.KotlinLogging
import utils.ScannerInput
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.lang.System.exit
import java.util.*
private val logger = KotlinLogging.logger {}
private val playerAPI = PlayerAPI()


fun main(args: Array<String>) {
    runMenu()
}

    val scanner = Scanner(System.`in`)




fun mainMenu() : Int {
    return ScannerInput.readNextInt(""" 
        >  ----------------------------------
        >  |        Player Organiser APP    |
        >  ----------------------------------
        >  | Player MENU                    |
        >  |   1) Add a player              |
        >  |   2) List all players          |
        >  |   3) Update a player           |
        >  |   4) Delete a player           |
        >  ----------------------------------
        >  |   0) Exit                      |
        >  ----------------------------------
        >  ==>> """)

}



fun runMenu() {
        do {
            val option = mainMenu()
            when (option) {
                1  -> addPlayer()
                2  -> listPlayers()
                3  -> updatePlayer()
                4  -> deletePlayer()
                0  -> exitApp()
                else -> println("Invalid option entered: ${option}")

            }
        } while (true)
    }


fun addPlayer(){
    //logger.info { "addPlayer() function invoked" }
    val playerName = readNextLine("Enter the players name: ")
    val playerDOB = readNextLine("Enter players DOB (DD-MM-YYYY): ")
    val playerRating = readNextInt("Enter player rating (1-10): ")
    val isAdded = playerAPI.add(Player(playerName, playerDOB, playerRating, false))

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}


fun listPlayers(){
    //logger.info { "listPlayers() function invoked" }
    println(playerAPI.listAllPlayers())
}


    fun updatePlayer(){
        println("You chose Update Player")
    }

    fun deletePlayer(){
        println("You chose Delete Player")
    }

    fun exitApp(){
        println("Exiting...bye")
        exit(0)
    }



