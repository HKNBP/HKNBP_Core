/*
 * HKNBP_Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * HKNBP_Core is distributed in the hope that it will be useful,
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with HKNBP_Core.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.sourcekey.hknbp.hknbp_core

import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLElement
import kotlin.browser.document
import kotlin.browser.window

object NativeAppInstallButton {
    private val nativeAppInstallButton = document.getElementById("nativeAppInstallButton") as HTMLButtonElement
    private var installPromptEvent: dynamic = null

    val addToHomeScreen = fun(event: dynamic) {
        val a2hsBtn = document.querySelector(".ad2hs-prompt") as HTMLElement  // hide our user interface that shows our A2HS button
        a2hsBtn.style.display = "none"  // Show the prompt
        installPromptEvent.prompt()  // Wait for the user to respond to the prompt
        installPromptEvent.userChoice.then(fun(choiceResult: dynamic){
            if (choiceResult.outcome === "accepted") {
                console.log("User accepted the A2HS prompt")
            } else {
                console.log("User dismissed the A2HS prompt")
            }
            installPromptEvent = null
        })
    }

    fun showAddToHomeScreen() {
        val a2hsBtn = document.querySelector(".ad2hs-prompt") as HTMLElement
        a2hsBtn.style.display = "block"
        a2hsBtn.addEventListener("click", addToHomeScreen)
    }

    init{
        window.addEventListener("beforeinstallprompt", fun(event: dynamic){
            println("iii")
            // Prevent Chrome <= 67 from automatically showing the prompt
            event.preventDefault()
            // Stash the event so it can be triggered later.
            installPromptEvent = event
            // Update the install UI to notify the user app can be installed
            //document.querySelector('#install-button').disabled = false

            showAddToHomeScreen()
        })

        nativeAppInstallButton.onclick = fun(event){
            println("niii")
            installPromptEvent.prompt()
        }
    }
}