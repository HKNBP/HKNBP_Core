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

import jquery.JQuery
import jquery.jq
import org.w3c.dom.HTMLElement

abstract class TabbableUI(
        mainFrameElement: HTMLElement,
        protected var firstFocusJqElement: JQuery? = null,              //當顯示用戶界面最先Focus嘅Element
        protected var transpositionFocusHideTime: Int? = 15000,         //Focus换位後用戶界面倒數隱藏時間,如null就唔會倒數隱藏
        protected val isFocusTriggerShowEventElement: Boolean = true,   //係米Focus返觸發顯示呢個用戶界面嘅Element
        protected var isFocusOutHide: Boolean = false                   //////////////////
): UserInterface(mainFrameElement = mainFrameElement) {

    companion object{
        private val tabbableUIList = ArrayList<TabbableUI>()
    }

    private var triggerShowEventJqElement: JQuery? = null

    override fun show(showTime: Int?) {
        //當呢個用戶界面由隱藏轉顯示時記低觸發顯示呢個用戶界面嘅Element
        if(!isShow){triggerShowEventJqElement = jq(":focus")}
        //顯示
        super.show(showTime)
        //Focus去當用戶界面顯示時最先Focus嘅Element
        firstFocusJqElement?.focus()
    }

    override fun hide() {
        //隱藏
        super.hide()
        //Focus返之前記低左觸發顯示呢個用戶界面嘅Element
        if(isFocusTriggerShowEventElement){triggerShowEventJqElement?.focus()}
    }

    init {
        //設置TabbableElement效果
        val mainFrameJqElementTabbableElement = jq(mainFrameElement).find("button,select,option,input")
        mainFrameJqElementTabbableElement.focus(fun(event){if(!js("\$(\"this\").is(\":focus\")")){
            //記住依家Focus邊粒element為之後再Show呢個介面時Focus返對上個次嘅element
            firstFocusJqElement = jqThis()
            //當focus就重新倒數介面顯示時間
            setHideTimer(transpositionFocusHideTime)
        }})
        mainFrameJqElementTabbableElement.hover(fun(){
            var isEquals = false
            for(i in 0 until jqThis().length){
                for(j in 0 until (firstFocusJqElement?.length?:0)){
                    if(jqThis().get(i) == firstFocusJqElement?.get(j)){isEquals = true}
                }
            }
            if(!isEquals){
                //設 當onhover 就onfocus 同步
                jqThis().focus()
            }
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