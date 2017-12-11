/*
Copyright 2017 Nathanael Merlin

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package uno.merlin.diffstring

import java.util.logging.Logger
import java.util.logging.Logger.getLogger

/**
 * Created by nmerlin on 12/3/17.
 * An object for diff'ing two strings
 *
 * This class is constructed using two strings, a master string and
 * a disciple string to compare against the master.
 *
 * @property masterString the string to test against.
 * @property discipleString the string to compare against the master.
 * @constructor creates an object to compare master with disciple.
 */
class DiffString(val masterString: String, val discipleString: String) {
    val isEqual: Boolean = masterString == discipleString

    companion object {
        val className = DiffString::class.qualifiedName
        val logger: Logger? = getLogger(className)
    }

    init {
        logger?.info("Started logger for $className")
        val initMessage = """
            uno.merlin.diffstring.DiffString initialized with the following values
            masterString:   $masterString
            discipleString: $discipleString
            """.trimIndent()
        logger?.info(initMessage)
    }

    /**
     * Internal class for holding a DiffWord.  Used to decouple match status and formated output
     */
    data class DiffWord(val master: String, val disciple: String, val match: Boolean)

    /**
     * Marks up and returns the discipleString after comparison
     * @return the marked up discipleString
     */
    fun diffedDisciple(): String {
        // TODO: write functions for comparing
        // TODO: refactor this later, seems like things should be split better
        return markUpMasterDisciple()
    }

    private fun diffedMasterDisciple(): Array<DiffWord?> {
        val masterTokens = masterString.split(" ")
        val discipleTokens = discipleString.split(" ")

        logger?.info(masterTokens.joinToString("@"))
        logger?.info(discipleTokens.joinToString("@"))

        val shortestSize = if (masterTokens.size >= discipleTokens.size)
            discipleTokens.size
        else
            masterTokens.size

        // Log size information
        val sizeInfo = """
                masterTokensSize: ${masterTokens.size}
                discipleTokensSize: ${discipleTokens.size}
                shortestSize: $shortestSize
                """.trimIndent()
        logger?.info(sizeInfo)

        val diffWords = arrayOfNulls<DiffWord>(shortestSize)

        for (index in 0..(shortestSize - 1)) {
            val masterWord = masterTokens[index]
            val discipleWord = discipleTokens[index]
            val match = masterWord == discipleWord
            diffWords[index] = DiffWord(masterWord, discipleWord, match)
        }

        return diffWords
    }

    private fun markUpMasterDisciple(): String {
        // TODO: refactor with diffedDisciple, if possible
        val successTag = "<font color=#00ff00>"
        val failTag = "<font color=#ff0000>"
        val endTag = "</font>"

        var markedUpString = "$successTag$discipleString$endTag"
        if (!isEqual) {
            val diffedStrings = diffedMasterDisciple()

            val taggedDiscipleStrings: MutableList<String> = mutableListOf()
            for (diffWord in diffedStrings) {
                val discipleWord = diffWord?.disciple
                val match = diffWord?.match

                val addedDiscipleString = if (match == true) discipleWord
                else
                    "$failTag$discipleWord$endTag"

                taggedDiscipleStrings.add(addedDiscipleString.toString())

                logger?.info("compared ${diffWord?.master} to $discipleWord : $match")
            }

            val discipleTokens = discipleString.split(" ")
            if (discipleTokens.size > diffedStrings.size) {
                val overflowFromDisciple = discipleTokens.subList(diffedStrings.size, discipleTokens.size)

                taggedDiscipleStrings.add(failTag)
                taggedDiscipleStrings.addAll(overflowFromDisciple)
                taggedDiscipleStrings.add(endTag)

                markedUpString = taggedDiscipleStrings.joinToString(" ")
                logger?.info("String does not match and disciple is longer than master")
            } else {
                markedUpString = taggedDiscipleStrings.joinToString(" ")
                logger?.info("String does not match and disciple is shorter than master")
            }
        }

        logger?.info(markedUpString)
        return markedUpString
    }
}