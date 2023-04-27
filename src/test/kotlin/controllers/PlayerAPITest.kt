package controllers

import models.Player
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import persistence.JSONSerializer
import persistence.XMLSerializer
import java.io.File
import kotlin.test.assertEquals

class PlayerAPITest {

    private var peter: Player? = null
    private var james: Player? = null
    private var joe: Player? = null
    private var bob: Player? = null
    private var mary: Player? = null
    private var populatedPlayers: PlayerAPI? = PlayerAPI(XMLSerializer(File("players.xml")))
    private var emptyPlayers: PlayerAPI? = PlayerAPI(XMLSerializer(File("players.xml")))

    @BeforeEach
    fun setup() {
        peter = Player(1,"Peter Woods", "11-09-2002", 9, false)
        james = Player(2,"James Power", "13-02-2000", 8, false)
        joe = Player(3,"Joe Doe", "02-04-2003", 7, false)
        bob = Player(4,"Bob Builder", "22-10-1999", 6, false)
        mary = Player(5,"Mary Daly", "30-09-1998", 7, false)

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
            val newPlayer = Player(1,"Leo Messi", "10-08-1993", 10, false)
            assertEquals(5, populatedPlayers!!.numberOfPlayers())
            assertTrue(populatedPlayers!!.add(newPlayer))
            assertEquals(6, populatedPlayers!!.numberOfPlayers())
            assertEquals(newPlayer, populatedPlayers!!.findPlayer(populatedPlayers!!.numberOfPlayers() - 1))
        }

        @Test
        fun `adding a Player to an empty list adds to ArrayList`() {
            val newPlayer = Player(1,"Leo Messi", "10-08-1993", 10, false)
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

        @Test
        fun testListByBest() {
            // Create a list of players
            val players = listOf(
               bob,
                james,
                joe,
               peter
            )

            // Sort the players by rating best to worst
            val expected = listOf(
                peter,
                james,
                joe,
                bob
            )



            // Call the listByBest function
            val actual = populatedPlayers!!.listByBest(players)

            // Compare the expected and actual results
            assertEquals(expected, actual)
        }

        @Test
        fun testListByWorse() {
            // Create a list of players
            val players = listOf(
                bob,
                james,
                joe,
                peter
            )

            // Sort the players by rating best to worst
            val expected = listOf(
                bob,joe,james,peter
            )

            // Call the listByWorst function
            val actual = populatedPlayers!!.listByWorst(players)

            // Compare the expected and actual results
            assertEquals(expected, actual)
        }
    }


    @Nested
    inner class PersistenceTests {

        @Test
        fun `saving and loading an empty collection in XML doesn't crash app`() {
            // Saving an empty players.XML file.
            val storingPlayers = PlayerAPI(XMLSerializer(File("players.xml")))
            storingPlayers.store()

            //Loading the empty players.xml file into a new object
            val loadedPlayers = PlayerAPI(XMLSerializer(File("players.xml")))
            loadedPlayers.load()

            //Comparing the source of the players (storingPlayers) with the XML loaded players (loadedPlayers)
            assertEquals(0, storingPlayers.numberOfPlayers())
            assertEquals(0, loadedPlayers.numberOfPlayers())
            assertEquals(storingPlayers.numberOfPlayers(), loadedPlayers.numberOfPlayers())
        }

        @Test
        fun `saving and loading an loaded collection in XML doesn't loose data`() {
            // Storing 3 players to the players.XML file.
            val storingPlayers = PlayerAPI(XMLSerializer(File("players.xml")))
            storingPlayers.add(peter!!)
            storingPlayers.add(james!!)
            storingPlayers.add(bob!!)
            storingPlayers.store()

            //Loading players.xml into a different collection
            val loadedPlayers = PlayerAPI(XMLSerializer(File("players.xml")))
            loadedPlayers.load()

            //Comparing the source of the players (storingPlayers) with the XML loaded players (loadedPlayers)
            assertEquals(3, storingPlayers.numberOfPlayers())
            assertEquals(3, loadedPlayers.numberOfPlayers())
            assertEquals(storingPlayers.numberOfPlayers(), loadedPlayers.numberOfPlayers())
            assertEquals(storingPlayers.findPlayer(0), loadedPlayers.findPlayer(0))
            assertEquals(storingPlayers.findPlayer(1), loadedPlayers.findPlayer(1))
            assertEquals(storingPlayers.findPlayer(2), loadedPlayers.findPlayer(2))
        }

/*
        @Nested
        inner class PersistenceTests {

            @Test
            fun `saving and loading an empty collection in JSON doesn't crash app`() {
                // Saving an empty players.JSON file.
                val storingPlayers = PlayerAPI(JSONSerializer(File("players.JSON")))
                storingPlayers.store()

                //Loading the empty players.JSON file into a new object
                val loadedPlayers = PlayerAPI(JSONSerializer(File("players.JSON")))
                loadedPlayers.load()

                //Comparing the source of the players (storingPlayers) with the JSON loaded players (loadedPlayers)
                assertEquals(0, storingPlayers.numberOfPlayers())
                assertEquals(0, loadedPlayers.numberOfPlayers())
                assertEquals(storingPlayers.numberOfPlayers(), loadedPlayers.numberOfPlayers())
            }

            @Test
            fun `saving and loading an loaded collection in JSON doesn't loose data`() {
                // Storing 3 players to the players.JSON file.
                val storingPlayers = PlayerAPI(JSONSerializer(File("players.JSON")))
                storingPlayers.add(peter!!)
                storingPlayers.add(james!!)
                storingPlayers.add(bob!!)
                storingPlayers.store()

                //Loading players.JSON into a different collection
                val loadedPlayers = PlayerAPI(JSONSerializer(File("players.JSON")))
                loadedPlayers.load()

                //Comparing the source of the players (storingPlayers) with the JSON loaded players (loadedPlayers)
                assertEquals(3, storingPlayers.numberOfPlayers())
                assertEquals(3, loadedPlayers.numberOfPlayers())
                assertEquals(storingPlayers.numberOfPlayers(), loadedPlayers.numberOfPlayers())
                assertEquals(storingPlayers.findPlayer(0), loadedPlayers.findPlayer(0))
                assertEquals(storingPlayers.findPlayer(1), loadedPlayers.findPlayer(1))
                assertEquals(storingPlayers.findPlayer(2), loadedPlayers.findPlayer(2))
            }
        }
*/
    }







}


