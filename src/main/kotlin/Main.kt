import utils.ScannerInput
import java.lang.System.exit
import java.util.*


fun main(args: Array<String>) {
    runMenu()
}

    val scanner = Scanner(System.`in`)




fun mainMenu() : Int {
    return ScannerInput.readNextInt(""" 
        >  ----------------------------------
        >  |        NOTE KEEPER APP         |
        >  ----------------------------------
        > | NOTE MENU                      |
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
        println("You chose Add Player")
    }

    fun listPlayers(){
        println("You chose List Players")
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



