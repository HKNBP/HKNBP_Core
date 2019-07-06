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
        private val htmlElementID: String,
        private val onShow: ()->Unit = fun(){},
        private val onHide: ()->Unit = fun(){},
        private val firstFocusElementID: String? = null,
        private var isFocusCountdownHide: Boolean = true,
        private var isFocusOutHide: Boolean = false,
        private val conversionFocusHideTime: Int = 15000

) {
    val u0 = { document.getElementById("sss")?.innerHTML = "u0" }()
    private val htmlElement = document.getElementById(htmlElementID) as HTMLElement
    private var lastTimeFocusElement: dynamic = jQuery("#${firstFocusElementID}")
    val u1 = { document.getElementById("sss")?.innerHTML = "u1" }()
    open fun update(){}
    val u2 = { document.getElementById("sss")?.innerHTML = "u02" }()
    /**
     * 係顯示介面時首次Focus
     *
     * 每個介面當顯示出來時
     * 都會Focus去上次Focus嘅Element度
     * 此值   為辨別Focus類型
     * */
    private var isShowUserInterfaceFirstFocus: Boolean = false
    val u3 = { document.getElementById("sss")?.innerHTML = "u3" }()
    /**
     * 隱藏頻道訊息計時器
     * */
    private var hideTimer = 0
        set(value) {
            window.clearTimeout(field)
            field = value
        }
    val u4 = { document.getElementById("sss")?.innerHTML = "u4" }()
    open val isShow: Boolean
        get(){
            return htmlElement.style.display == "block"
        }
    val u5 = { document.getElementById("sss")?.innerHTML = "u5" }()
    open fun show(){
        htmlElement.style.display = "block"
        isShowUserInterfaceFirstFocus = true
        lastTimeFocusElement?.focus()
        onShow()
        update()
    }
    val u6 = { document.getElementById("sss")?.innerHTML = "u6" }()
    private fun setHideTimer(showTime: Int){
        hideTimer = window.setTimeout(fun(){ hide() }, showTime)
    }
    val u7 = { document.getElementById("sss")?.innerHTML = "u7" }()
    fun show(showTime: Int){
        show()
        setHideTimer(showTime)
    }
    val u8 = { document.getElementById("sss")?.innerHTML = "u8" }()
    open fun hide(){
        htmlElement.style.display = "none"
        onHide()
    }
    val u9 = { document.getElementById("sss")?.innerHTML = "u9" }()
    fun showHideAlternately(){
        if(isShow){ hide() }else{ show() }
    }
    val u10 = { document.getElementById("sss")?.innerHTML = "u10" }()
    fun showHideAlternately(showTime: Int){
        if(isShow){ hide() }else{ show(showTime) }
    }
    val u11 = { document.getElementById("sss")?.innerHTML = "u11" }()
    init {
        jQuery(
                "#${htmlElementID} button" + "," +
                "#${htmlElementID} select" + "," +
                "#${htmlElementID} option" + "," +
                "#${htmlElementID} input"
        )?.focus(fun(){if(!js("\$(\"this\").is(\":focus\")")){
            //設 當onfocus 就onhover 同步
            jQuery(js("this"))?.hover()
            //設定依家Focus邊粒element為之後再Show呢個介面時Focus返對上個次嘅element
            lastTimeFocusElement = jQuery(js("this"))
            //當focus就重新倒數介面顯示時間 同 唔係顯示介面時首次Focus
            if((!isShowUserInterfaceFirstFocus)&&isFocusCountdownHide){
                isShowUserInterfaceFirstFocus = false
                setHideTimer(conversionFocusHideTime)
            }
        }})
        jQuery(
                "#${htmlElementID} button" + "," +
                "#${htmlElementID} select" + "," +
                "#${htmlElementID} option" + "," +
                "#${htmlElementID} input"
        )?.hover(fun(){
            //設 當onhover 就onfocus 同步
            jQuery(js("this"))?.focus()
        })

        /**
        jQuery("#${htmlElementID}").blur(fun(){
            if(isFocusOutHide){
                hide()
            }
        })*/
    }
}