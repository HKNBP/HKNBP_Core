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

import org.w3c.dom.HTMLDivElement
import kotlin.browser.document
import kotlin.browser.window

object SubtitleDescription: UserInterface("subtitleDescription") {
    private val subtitleDescription: HTMLDivElement = document.getElementById("subtitleDescription") as HTMLDivElement
    private val text: HTMLDivElement = document.getElementById("subtitleDescriptionText") as HTMLDivElement

    override fun show() {
        super.show()
        SubtitleDescription.text.innerHTML = player?.subtitleTracks?.node?.name?:""
    }

    init {
        println("Init SubtitleDescription")
    }
}