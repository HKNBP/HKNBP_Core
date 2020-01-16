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
import org.w3c.dom.HTMLElement
import kotlin.browser.document
import kotlin.browser.window

abstract class UserInterface(
        protected val mainFrameElement: HTMLElement,
        protected var firstFocusElement: HTMLElement? = null,
        protected var isFocusCountdownHide: Boolean = true,
        protected var isFocusOutHide: Boolean = false,
        protected val isHideFocusToUserControlPanelShower: Boolean = false,
        protected val isShowToHideUserControlPanel: Boolean = false,
        protected val conversionFocusHideTime: Int? = 15000
) {
    companion object{
        val allUserInterfaceList = ArrayList<UserInterface>()

        fun hideAllUserInterface(){
            for(userInterface in allUserInterfaceList){userInterface.hide()}
        }
    }

    private var lastTimeFocusElement: dynamic? = {
        val _firstFocusElement = firstFocusElement
        if(_firstFocusElement != null){
            try {
                jq(_firstFocusElement)
            }catch (e: dynamic){ null }
        }else{ null }
    }()

    open fun update(){}

    /**
     * 係顯示介面時首次Focus
     *
     * 每個介面當顯示出來時
     * 都會Focus去上次Focus嘅Element度
     * 此值   為辨別Focus類型
     * */
    private var isShowUserInterfaceFirstFocus: Boolean = false

    /**
     * 隱藏頻道訊息計時器
     * */
    private var hideTimer = 0
        set(value) {
            window.clearTimeout(field)
            field = value
        }

    open val isShow: Boolean
        get() = mainFrameElement.style.display == "block"

    private fun setHideTimer(showTime: Int?){
        hideTimer = if(showTime != null){
            window.setTimeout(fun(){ hide() }, showTime)
        }else{ 0 }
    }

    open fun show(showTime: Int?){
        mainFrameElement.style.display = "block"
        isShowUserInterfaceFirstFocus = true
        if(isShow){ lastTimeFocusElement?.focus() }
        setHideTimer(showTime)
    }

    open fun hide(){
        mainFrameElement.style.display = "none"

        if(isHideFocusToUserControlPanelShower){
            //focus到userControlPanelShower,為左之後撳centerButton可以顯示VirtualRemote
            (document.getElementById("userControlPanelShower") as HTMLElement).focus()
        }
    }

    fun showHideAlternately(showTime: Int?){
        if(isShow){ hide() }else{ show(showTime) }
    }

    init {
        val id = mainFrameElement.id
        jq(
                "#${id} button" + "," +
                "#${id} select" + "," +
                "#${id} option" + "," +
                "#${id} input"
        ).focus(fun(event){if(!js("\$(\"this\").is(\":focus\")")){
            //設 當onfocus 就onhover 同步
            jqThis().hover(fun(){})
            //記住依家Focus邊粒element為之後再Show呢個介面時Focus返對上個次嘅element
            lastTimeFocusElement = jqThis()
            //當focus就重新倒數介面顯示時間 同 唔係顯示介面時首次Focus
            if((!isShowUserInterfaceFirstFocus)&&isFocusCountdownHide){
                isShowUserInterfaceFirstFocus = false
                if(conversionFocusHideTime != null){setHideTimer(conversionFocusHideTime)}
            }
        }})
        jq(
                "#${id} button" + "," +
                "#${id} select" + "," +
                "#${id} option" + "," +
                "#${id} input"
        ).hover(fun(){
            //設 當onhover 就onfocus 同步
            jqThis().focus()
        })

        /**
        jq("#${id}").blur(fun(){
            if(isFocusOutHide){
                hide()
            }
        })*/

        //將呢個UserInterface加去一個List,為其他位置可以一次過搵到哂所有UserInterface
        allUserInterfaceList.add(this)
    }
}