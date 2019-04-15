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

import org.w3c.dom.HTMLElement
import kotlin.browser.document
import kotlin.browser.window

abstract class UserInterface(
        val htmlElementID: String,
        private val onShow: ()->Unit = fun(){},
        private val onHide: ()->Unit = fun(){},
        private val firstFocusElementID: String? = null
) {
    private val htmlElement = document.getElementById(htmlElementID) as HTMLElement
    private var lastTimeFocusElement: dynamic = jQuery("#${firstFocusElementID}")

    /**
     * 隱藏頻道訊息計時器
     * */
    private var hideTimer = 0
        set(value) {
            window.clearTimeout(field)
            field = value
        }

    open val isShow: Boolean
        get(){
            return htmlElement.style.display == "block"
        }

    open fun show(){
        htmlElement.style.display = "block"
        lastTimeFocusElement?.focus()
        onShow()
    }

    fun show(showTime: Int){
        hideTimer = window.setTimeout(fun(){ hide() }, showTime)
        show()
    }

    open fun hide(){
        htmlElement.style.display = "none"
        onHide()
    }

    fun showHideAlternately(){
        if(isShow){ hide() }else{ show() }
    }

    init {
        jQuery(
                "#${htmlElementID} button" + "," +
                "#${htmlElementID} select" + "," +
                "#${htmlElementID} option" + "," +
                "#${htmlElementID} input"
        ).focus(fun(){
            //設 當onfocus 就onhover 同步
            jQuery(js("this"))?.hover()
            //設定依家Focus邊粒element為之後再Show呢個介面時Focus返對上個次嘅element
            lastTimeFocusElement = jQuery(js("this"))
        })
    }
}