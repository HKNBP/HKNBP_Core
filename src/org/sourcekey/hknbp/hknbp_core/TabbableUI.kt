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
import org.w3c.dom.PopStateEvent
import kotlin.browser.window

abstract class TabbableUI(
        mainFrameElement: HTMLElement,
        protected var firstFocusJqElement: JQuery? = null,              //當顯示用戶介面最先Focus嘅Element
        protected var transpositionFocusHideTime: Int? = 120000,        //Focus换位後用戶介面倒數隱藏時間,如null就唔會倒數隱藏
        protected val isFocusTriggerShowEventElement: Boolean = true,   //係米Focus返觸發顯示呢個用戶介面嘅Element
        protected var isFocusOutHide: Boolean = false                   //////////////////
): UserInterface(mainFrameElement = mainFrameElement) {

    companion object{
        /**
         * 正顯示介面嘅表
         * */
        private val showedIList = ArrayList<TabbableUI>()

        init {
            //設置PopState活動
            window.addEventListener("popstate", fun(event){
                //移除呢個介面響正顯示介面嘅表
                showedIList.removeAt(showedIList.lastIndex)
                //解除凍結上一個介面
                showedIList.getOrNull(showedIList.lastIndex)?.unfreeze()
            })
        }
    }

    /**
     * 記低呢個介面有否凍結
     *
     * 使凍結後介面免被操作
     * */
    private var isFreeze: Boolean = false

    /**
     * 凍結介面
     *
     * 因有其他新介面去顯示要隱藏呢個介面
     * 將之前嘅介面 隱藏 防止搖控讀取到其他介面Element
     * */
    fun freeze(){
        super.hide()
        //暫停倒數隱藏呢個介面
        setHideTimer(null)
        //記低呢個介面已凍結
        isFreeze = true
    }

    /**
     * 解除凍結介面
     * */
    fun unfreeze(){
        super.show(transpositionFocusHideTime)
        //記低呢個介面解除凍結
        isFreeze = false
    }

    /**
     * 係米正顯示但又未隱藏
     * 使正顯示介面當未隱藏就不會多次執行
     * */
    private var isShowOfNotHided: Boolean = false

    override fun show(showTime: Int?) {
        //如果未響表度就加到表度,如果響表度即是呢個介面係因有新介面顯示而被隱藏
        if(!isFreeze){
            if(!isShowOfNotHided){
                //凍結上一個介面
                showedIList.lastOrNull()?.freeze()
                //加呢個介面到正顯示介面嘅表到記低
                showedIList.add(this)
                //Push介面
                window.history.pushState("", "", "")
                //已顯示但未隱藏
                isShowOfNotHided = true
            }
            //顯示呢個介面
            super.show(showTime)
            //Focus去當用戶介面顯示時最先Focus嘅Element
            firstFocusJqElement?.focus()
        }
    }

    override fun hide() {
        if(!isFreeze){
            //隱藏呢個介面
            super.hide()
            //Pop介面
            window.history.back()
            //重新計顯示但未隱藏值
            isShowOfNotHided = false
        }
    }

    init {
        jq(mainFrameElement).on("hover", "button,select,option,input", fun(event){
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
        jq(mainFrameElement).on("focus", "button,select,option,input", fun(event){if(!js("\$(\"this\").is(\":focus\")")){
            //記住依家Focus邊粒element為之後再Show呢個介面時Focus返對上個次嘅element
            firstFocusJqElement = jqThis()
            //當focus就重新倒數介面顯示時間
            setHideTimer(transpositionFocusHideTime)
        }})
        jq(mainFrameElement).on("scroll", fun(event){
            //當focus就重新倒數介面顯示時間
            setHideTimer(transpositionFocusHideTime)
        })
        jq(mainFrameElement).on("mousemove", fun(event){
            //當focus就重新倒數介面顯示時間
            setHideTimer(transpositionFocusHideTime)
        })
        /**
        jq("#${id}").blur(fun(){
        if(isFocusOutHide){
        hide()
        }
        })*/
    }
}