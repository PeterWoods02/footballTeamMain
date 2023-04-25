package controllers

import models.Player
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PlayerAPITest {

    private var peter: Player? = null
    private var james: Player? = null
    private var joe: Player? = null
    private var bob: Player? = null
    private var mary: Player? = null
    private var populatedPlayers: PlayerAPI? = PlayerAPI()
    private var emptyPlayers: PlayerAPI? = PlayerAPI()

    @BeforeEach
    fun setup() {
        peter = Player("Peter Woods", "11-09-2002", 9, false)
        james = Player("James Power", "13-02-2000", 8, false)
        joe = Player("Joe Doe", "02-04-2003", 7, false)
        bob = Player("Bob Builder", "22-10-1999", 6, false)
        mary = Player("Mary Daly", "30-09-1998", 7, false)

        //adding 5 players to the players api
        populatedPlayers!!.add(peter!!)
        populatedPlayers!!.add(james!!)
        populatedPlayers!!.add(joe!!)
        populatedPlayers!!.add(bob!!)
        populatedPlayers!!.add(mary!!)
    }

    @AfterEach
    fun tearDown() {
        peter = null
        james = null
        joe = null
        bob = null
        mary = null
        populatedPlayers = null
        emptyPlayers = null
    }

    @Nested
    inner class AddPlayers {

        @Test
        fun `adding a Player to a populated list adds to ArrayList`() {
            val newPlayer = Player("Leo Messi", "10-08-1993", 10, false)
            assertEquals(5, populatedPlayers!!.numberOfPlayers())
            assertTrue(populatedPlayers!!.add(newPlayer))
            assertEquals(6, populatedPlayers!!.numberOfPlayers())
            assertEquals(newPlayer, populatedPlayers!!.findPlayer(populatedPlayers!!.numberOfPlayers() - 1))
        }

        @Test
        fun `adding a Player to an empty list adds to ArrayList`() {
            val newPlayer = Player("Leo Messi", "10-08-1993", 10, false)
            assertEquals(0, emptyPlayers!!.numberOfPlayers())
            assertTrue(emptyPlayers!!.add(newPlayer))
            assertEquals(1, emptyPlayers!!.numberOfPlayers())
            assertEquals(newPlayer, emptyPlayers!!.findPlayer(emptyPlayers!!.numberOfPlayers() - 1))
        }
    }


    @Nested
    inner class ListPlayers {

        @Test
        fun `listAllPlayers returns No Players Stored message when ArrayList is empty`() {
            assertEquals(0, emptyPlayers!!.numberOfPlayers())
            assertTrue(emptyPlayers!!.listAllPlayers().lowercase().contains("no players"))
        }

        @Test
        fun `listAllPlayers returns Players when ArrayList has players stored`() {
            assertEquals(5, populatedPlayers!!.numberOfPlayers())
            val playersString = populatedPlayers!!.listAllPlayers().lowercase()
            assertTrue(playersString.contains("peter"))
            assertTrue(playersString.contains("james"))
            assertTrue(playersString.contains("joe"))
            assertTrue(playersString.contains("bob"))
            assertTrue(playersString.contains("mary"))
        }
    }

}


