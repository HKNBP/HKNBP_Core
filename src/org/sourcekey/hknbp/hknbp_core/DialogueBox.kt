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

import jquery.jq
import org.w3c.dom.Element
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import kotlin.browser.document
import kotlin.random.Random

open class DialogueBox(
        var title: String = "",
        var dialogueHTML: String,
        var okButtonString: String = "OK",
        var okButtonScript: (()->Unit)? = null,
        var cancelButtonString: String = "Cancel",
        var cancelButtonScript: (()->Unit)? = null,
        val placeArea: HTMLDivElement = {
            //建立Window放置空間
            val placeArea = document.createElement("div") as HTMLDivElement
            placeArea.id                        = "dialogue${Random.nextInt(0, 99999999)}"
            placeArea.style.display             = "none"
            placeArea.style.position            = "absolute"
            placeArea.style.top                 = "0"
            placeArea.style.bottom              = "0"
            placeArea.style.left                = "0"
            placeArea.style.right               = "0"
            placeArea.style.zIndex              = "90"
            //將Window放置在dynamicUserInterfaceArea顯示
            val dynamicUserInterfaceArea = document.getElementById("dynamicUserInterfaceArea") as HTMLDivElement
            dynamicUserInterfaceArea.append(placeArea)

            placeArea
        }()
): UserInterface(
        placeArea,
        conversionFocusHideTime = null
) {

    val titleDiv = {
        val titleDiv = document.createElement("div") as HTMLDivElement
        titleDiv.style.textAlign                = "left"
        titleDiv.innerHTML                      = title
        titleDiv
    }()

    val dialogueDiv = {
        val dialogueDiv = document.createElement("div") as HTMLDivElement
        dialogueDiv.innerHTML                   = dialogueHTML
        dialogueDiv
    }()

    private val okButton = {
        val okButton = document.createElement("button") as HTMLButtonElement
        okButton.style.flex                     = "1"
        okButton.style.color                    = "#FFF"
        okButton.style.fontWeight               = "bold"
        okButton.style.fontSize                 = "4vh"
        okButton.innerHTML                      = okButtonString
        okButton.onclick = fun(event){
            okButtonScript?.invoke()
            hide()
        }
        okButton
    }()

    private val cancelButton = {
        val cancelButton = document.createElement("button") as HTMLButtonElement
        cancelButton.style.flex                 = "1"
        cancelButton.style.color                = "#FFF"
        cancelButton.style.fontWeight           = "bold"
        cancelButton.style.fontSize             = "4vh"
        cancelButton.innerHTML                  = cancelButtonString
        cancelButton.onclick = fun(event){
            cancelButtonScript?.invoke()
            hide()
        }
        cancelButton
    }()

    private fun createFlexArea(vararg elements: HTMLElement): HTMLDivElement{
        val flexArea = document.createElement("div") as HTMLDivElement
        flexArea.style.display                  = "flex"
        flexArea.style.width                    = "100%"
        for(element in elements){ flexArea.append(element) }
        return flexArea
    }

    val dialogueBox = {
        val dialogueBox = document.createElement("div") as HTMLDivElement
        dialogueBox.style.position              = "absolute"
        dialogueBox.style.backgroundColor       = "#303030"
        dialogueBox.style.cursor                = "auto"
        dialogueBox.onclick                     = fun(event){ event.stopPropagation() }//停止行父元素onclick
        dialogueBox.append(titleDiv, dialogueDiv, createFlexArea(okButton, cancelButton))
        dialogueBox
    }()

    val focusOutArea = {
        val focusOutArea = document.createElement("div") as HTMLDivElement
        focusOutArea.style.backgroundColor      = "rgba(0, 0, 0, 0.6)"
        focusOutArea.style.display              = "flex"
        focusOutArea.style.position             = "absolute"
        focusOutArea.style.top                  = "0"
        focusOutArea.style.bottom               = "0"
        focusOutArea.style.left                 = "0"
        focusOutArea.style.right                = "0"
        focusOutArea.style.zIndex               = "90"
        focusOutArea.style.alignItems           = "center"
        focusOutArea.style.justifyContent       = "center"
        //focusOutArea.onclick                    = fun(event){ hide() }
        focusOutArea.append(dialogueBox)
        focusOutArea
    }()

    override fun hide() {
        super.hide()
        val dynamicUserInterfaceArea = document.getElementById("dynamicUserInterfaceArea") as HTMLDivElement
        dynamicUserInterfaceArea.removeChild(placeArea)
    }

    init {
        placeArea.append(focusOutArea)
        show(null)
    }
}