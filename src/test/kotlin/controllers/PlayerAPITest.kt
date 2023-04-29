package controllers

import models.Match
import models.Player
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertTrue
import persistence.JSONSerializer
import persistence.XMLSerializer
import java.io.File
import java.text.SimpleDateFormat
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

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
        peter = Player(0,"Peter Woods", "11-09-2002", 9, false)
        james = Player(1,"James Power", "13-02-2000", 8, false)
        joe = Player(2,"Joe Doe", "02-04-2003", 7, false)
        bob = Player(3,"Bob Builder", "22-10-1999", 6, false)
        mary = Player(4,"Mary Daly", "30-09-1998", 7, false)

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
        fun testListByWorst() {
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



        @Test
        fun `test for listing all players who played over 60 mins`() {
            // Create a list of players
            val match1 = Match(0, 45, false)
            val match2 = Match(0, 66, false)
            val match4 = Match(0, 45, false)
            val match3 = Match(0, 33, false)
            val matches = mutableSetOf(match1, match2)
            val matches2 = mutableSetOf(match3, match4)

            val newPlayer = Player(1, "Leo Messi", "10-08-1993", 10, false, matches)
            val newPlayer2 = Player(2, "Ronaldo", "10-08-1993", 10, false, matches2)

            val isAdded = populatedPlayers!!.add(newPlayer)
            val isAdded2 = populatedPlayers!!.add(newPlayer2)
            val actual = populatedPlayers!!.playersSixtyMins()
            val expected = listOf(populatedPlayers)


            assertTrue(isAdded)
            assertTrue(isAdded2)
            assertEquals("6: Leo Messi, DOB(10-08-1993), Rating(10), PRO(N) \n" +
                    "\t0: 45 mins (Lost)\n" +
                    "\t0: 66 mins (Lost)", actual)


        }

        @Test
        fun `test for listing all players who should go Pro`() {
            // Create a list of players
            val match1 = Match(0, 76, true)
            val match2 = Match(0, 66, false)
            val match4 = Match(0, 45, false)
            val match3 = Match(0, 76, true)
            val matches = mutableSetOf(match1, match2)
            val matches2 = mutableSetOf(match3, match4)

            val newPlayer = Player(1, "Leo Messi", "10-08-1993", 10, false, matches)
            val newPlayer2 = Player(2, "Ronaldo", "10-08-1993", 5, false, matches2)

            val isAdded = populatedPlayers!!.add(newPlayer)
            val isAdded2 = populatedPlayers!!.add(newPlayer2)
            val actual = populatedPlayers!!.suggestPro()
            val expected = listOf(populatedPlayers)


            assertTrue(isAdded)
            assertTrue(isAdded2)
            assertEquals("6: Leo Messi, DOB(10-08-1993), Rating(10), PRO(N) \n" +
                    "\t0: 76 mins (Won)\n" +
                    "\t0: 66 mins (Lost)", actual)


        }



    }

    @Nested
    inner class SearchPlayers{

        @Test
        fun SearchPlayerByDOB() {

            val searchDate1 = "01-01-1990"
            val searchResult1 = populatedPlayers?.searchPlayerByDOB(searchDate1)
            val searchDate2 = "01-01-2005"
            val searchResult2 = populatedPlayers?.searchPlayerByDOB(searchDate2)


            assertEquals("0: Peter Woods 11-09-2002 \n" +
                    "1: James Power 13-02-2000 \n" +
                    "2: Joe Doe 02-04-2003 \n" +
                    "3: Bob Builder 22-10-1999 \n" +
                    "4: Mary Daly 30-09-1998 \n", searchResult1)
            assertEquals("No matches found for: 01-01-2005", searchResult2)
        }


    }


    @Nested
    inner class DeletePlayers {

        @Test
        fun `deleting a Player that does not exist, returns false`() {
            assertFalse { (emptyPlayers!!.delete(0)) }
            assertFalse {(populatedPlayers!!.delete(-1))}
            assertFalse {(populatedPlayers!!.delete(5))}
        }

        @Test
        fun `deleting a Player that exists delete and returns deleted object`() {
            assertEquals(5, populatedPlayers!!.numberOfPlayers())
          populatedPlayers!!.delete(0)
            assertEquals(4, populatedPlayers!!.numberOfPlayers())
         populatedPlayers!!.delete(1)
            assertEquals(3, populatedPlayers!!.numberOfPlayers())
        }
    }


    @Nested
    inner class UpdatePlayers {
        @Test
        fun `updating a Player that does not exist returns false`(){
            assertFalse(populatedPlayers!!.update(6, Player(6,"Updating Player", "01-01-2000", 8, false)))
            assertFalse(populatedPlayers!!.update(-1, Player(-1,"Updating Player", "01-01-2000", 8, false)))
            assertFalse(emptyPlayers!!.update(0, Player(0,"Updating Player", "01-01-2000", 8, false)))
        }

        @Test
        fun `updating a Player that exists returns true and updates`() {
            //check Player 1 exists and check the contents
            assertEquals(peter, populatedPlayers!!.findPlayer(0))
            assertEquals("Peter Woods", populatedPlayers!!.findPlayer(0)!!.playerName)
            assertEquals("11-09-2002", populatedPlayers!!.findPlayer(0)!!.playerDOB)
            assertEquals(9, populatedPlayers!!.findPlayer(0)!!.playerRating)

            //update Player 1 with new information and ensure contents updated successfully
            assertTrue(populatedPlayers!!.update(0, Player(0,"Peter Wood", "11-09-2004", 8, false)))
            assertEquals("Peter Wood", populatedPlayers!!.findPlayer(0)!!.playerName)
            assertEquals("11-09-2004", populatedPlayers!!.findPlayer(0)!!.playerDOB)
            assertEquals(8, populatedPlayers!!.findPlayer(0)!!.playerRating)
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


