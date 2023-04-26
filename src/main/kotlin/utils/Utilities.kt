package utils

import models.Match
import models.Player

object Utilities {

    @JvmStatic
    fun formatListString(playersToFormat: List<Player>): String =
        playersToFormat
            .joinToString(separator = "\n") {player -> "$player"  }

    @JvmStatic
    fun formatSetString(matchesToFormat: Set<Match>): String =
        matchesToFormat
            .joinToString(separator = "\n") {match -> "\t$match"  }

}