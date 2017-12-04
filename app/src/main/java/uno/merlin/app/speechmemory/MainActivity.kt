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
package uno.merlin.app.speechmemory

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Html
import android.text.Html.FROM_HTML_MODE_COMPACT
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.longToast
import uno.merlin.diffstring.DiffString

class MainActivity : AppCompatActivity() {
    val SPEECH_REQUEST_CODE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            val results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spokenText = results[0].replace(".", " period").toLowerCase()
            val masterString = prepareMasterString(masterText.text.toString().toLowerCase())

            // heavy lifting for comparison is done in DiffString
            val diffedString = DiffString(masterString, spokenText)

            val message = if (diffedString.isEqual) R.string.match_success else R.string.match_failure
            longToast(message)

            val markedUpText = diffedString.diffedDisciple()
            voiceTextView.text = Html.fromHtml(markedUpText, FROM_HTML_MODE_COMPACT)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * Starts listening to the user
     */
    fun startRecording(view: View) {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        startActivityForResult(intent, SPEECH_REQUEST_CODE)
    }

    private fun prepareMasterString(masterString: String): String {
        val removalRegex = "[^a-zA-Z0-9 ]".toRegex()

        return masterString.replace(removalRegex, "")
    }
}
