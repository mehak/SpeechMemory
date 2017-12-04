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
    var isEqual: Boolean = masterString == discipleString

    companion object {
        val className = DiffString::class.qualifiedName
        val logger: Logger? = getLogger(className)
    }

    init {
        logger?.info("Started logger for $className")
        val initMessage = """
            uno.merlin.diffstring.DiffString initialized with the following values
            masterString: $masterString
            discipleString: $discipleString
            """.trimIndent()
        logger?.info(initMessage)
    }

    /**
     * Marks up and returns the discipleString after comparison
     * @return the marked up discipleString
     */
    fun diffedDisciple(): String {
        // TODO: write functions for comparing
        return discipleString
    }
}