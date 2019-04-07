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
import org.w3c.dom.events.Event
import kotlin.browser.document
import kotlin.browser.window
import kotlin.js.Date

val rootURL: String     = "https://hknbp.org/"
val coreVersion: String = "0.9"
var appVersion: String  = "0.9-Web"

val jQuery: dynamic = js("\$")
var userLanguageList: ArrayList<String?> = SettingWindow.getLanguageSetting()
lateinit var tvChannels: ArrayLinkList<TVChannel>
lateinit var player: Player

// m,n為正整數的分子和分母
fun reductionTo(m: Int, n: Int): IntArray{
    val arr = IntArray(2)
    var h = m
    var k = n
    var a = h
    var b = k
    if(a >= b){ a = h; b = k }else{ a = k; b = h}
    if (h != 1 && k != 1 ) {
        for ( i in b downTo 2 ) {
            if (h % i == 0 && k % i == 0 ) {
                h = (h / i)
                k = (k / i)
            }
        }
    }
    arr[0] = h
    arr[1] = k
    return arr
}

/**
 * 更新URL參數
 * @param param 參數名
 * @param paramVal 參數值
 * */
fun updateURLParameter(param: String, paramVal: String){
    val url: String = window.location.href
    var TheAnchor: String? = null
    var newAdditionalURL = ""
    var tempArray = url.split("?")
    var baseURL = tempArray.getOrNull(0)
    var additionalURL: String? = tempArray.getOrNull(1)
    var temp = ""

    if (additionalURL != null) {
        val tmpAnchor = additionalURL.split("#")
        val TheParams = tmpAnchor.getOrNull(0)
        TheAnchor = tmpAnchor.getOrNull(1)
        if (TheAnchor != null) {additionalURL = TheParams}

        tempArray = additionalURL!!.split("&")

        for (i in 0 until tempArray.size){
            if (tempArray.getOrNull(i)?.split('=')?.getOrNull(0) != param) {
                newAdditionalURL += temp + tempArray.getOrNull(i)
                temp = "&"
            }
        }
    } else {
        val tmpAnchor = baseURL!!.split("#")
        val TheParams = tmpAnchor.getOrNull(0)
        TheAnchor = tmpAnchor.getOrNull(1)

        if (TheParams != null) {baseURL = TheParams}
    }

    var _paramVal = paramVal
    if (TheAnchor != null) {_paramVal += "#" + TheAnchor}

    val rows_txt = temp + "" + param + "=" + _paramVal
    window.history.replaceState("", "", baseURL + "?" + newAdditionalURL + rows_txt)
}

/**
 * 去特定頻道
 * @param channelNumber 要轉去頻道冧把
 */
@JsName("designatedChannel")
fun designatedChannel(channelNumber: Int): Boolean {
    val channelNumberNodeID = TVChannel.toChannelNumberNodeID(tvChannels, channelNumber)
    if (channelNumberNodeID != null) {
        tvChannels.designated(channelNumberNodeID)
        return true
    } else {
        Dialogue.getDialogues(fun(dialogues) {
            PromptBox.promptMessage(dialogues.node?.canNotFind ?: "")
        })
        return false
    }
}

/**
 * 刷新頻道
 */
fun updateChannel() {
    player = Player(tvChannels.node ?: TVChannel())
    player.addOnPlayerEventListener(object : Player.OnPlayerEventListener {
        private var currentPlayer: Player? = null
        private var isPlaying: Boolean = false
        override fun on(onPlayerEvent: Player.OnPlayerEvent) {
            when (onPlayerEvent) {
                Player.OnPlayerEvent.playing -> {
                    currentPlayer = player
                    isPlaying = true
                    VirtualRemote.update()
                }
                Player.OnPlayerEvent.notPlaying -> {
                    //15秒後刷新頻道
                    isPlaying = false
                    //檢查呢15秒內Player有冇再繼續正常播放,若冇就刷新Player
                    window.setTimeout(fun() {
                        if ((!isPlaying) && (player == currentPlayer)) {
                            updateChannel()
                        }
                    }, 15000)
                }
                Player.OnPlayerEvent.videoTrackChanged,
                Player.OnPlayerEvent.audioTrackChanged,
                Player.OnPlayerEvent.subtitleTrackChanged -> {
                    VirtualRemote.update()
                }
                Player.OnPlayerEvent.volumeChanged -> {
                    AudioDescription.show(3000)
                }
                Player.OnPlayerEvent.mutedChanged -> {
                    InDisplayMutedButton.update()
                }
            }
        }
    })
    VirtualRemote.update()
}

/**
private var autoTransformEngineTimerSecond = 12000

private var isLoopAutoTransformEngineTimer = true

/**
 * 開或熄自動換引擎功能
 * 用來當某一方引擎嘅Source出現問題
 * 轉換至其他Source
 * 重複輛扭換引擎
 * ///////////////////寫法有待修改(至Listeners)////////////////////
*/
fun setAutoTransformEngine(onOff: Boolean) {
if (onOff) {
isLoopAutoTransformEngineTimer = true
autoTransformEngineTimer()
} else {
isLoopAutoTransformEngineTimer = false
}
}

/**
 * 自動換引擎嘅倒時器
 * 用來定時檢查引擎嘅Source有冇出現問題
*/
private fun autoTransformEngineTimer(){
if(isLoopAutoTransformEngineTimer){
//用JavaScript嘅Timer來做Timer功能
val timer: dynamic = js("""
function timer(second){
setTimeout(
function(){
//去Call Kotlin嘅function
HKNBP.UIFeatures.prototype.autoTransformEngineRun(); //倒數後行要行嘅Code
HKNBP.UIFeatures.prototype.autoTransformEngineTimer(); //設置Timer倒數再行多次
}, second
);
}
""")
timer(autoTransformEngineTimerSecond)
}
}

/**
 * 畀autoTransformEngineTimer()當倒時完之後
 * 檢查引擎嘅Source有冇出現問題
*/
private fun autoTransformEngineRun() {
if (!p]layer.isPlaying()) {
tvChannels.node.getSources().next()
updateChannel()
}
}
 */


/**
 * ****************************** *
 * 成個HKNBP_Core嘅Kotlin部分嘅開始 *
 * ****************************** *
 * */
fun main(args: Array<String>) {
    try {
        UserControlPanel
        ConsentPanel
        (document.querySelector("[tabindex=\"100000002\"]") as HTMLElement).focus()
    } catch (e: dynamic) { println("介面初始化哀左: $e") }

    TVChannel.getTVChannels(fun(tvChannels_) {
        tvChannels = tvChannels_
        tvChannels.addOnNodeEventListener(object : ArrayLinkList.OnNodeEventListener<TVChannel> {
            override fun OnNodeIDChanged(
                    preChangeNodeID: Int?, postChangeNodeID: Int?,
                    preChangeNode: TVChannel?, postChangeNode: TVChannel?
            ) {
                updateChannel()
                ChannelInformation.show(3000)
            }
        })
        updateChannel()
        ChannelInformation.show(3000)
    })
}