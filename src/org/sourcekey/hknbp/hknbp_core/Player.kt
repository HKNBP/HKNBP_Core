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

import kotlin.browser.document
import kotlin.browser.localStorage
import kotlin.browser.window
import kotlin.js.Date
import kotlin.js.Math
import kotlin.random.Random

class Player(private val channel: Channel) {
    companion object{
        /**
         * 係米檢查自動播放需要靜音
         * */
        private var isCheckVideoAutoPlayNeedToMute = true

        /**
         * 檢查需唔需要可直接點擊IframePlayer模式嘅Timer
         *
         * 此值為清除之前Player嘅Timer
         * 因當觀眾轉台後
         * 上一台Player嘅Timer仍在計時
         * */
        private var checkNeedCanTouchIframePlayerModeTimer = 0
            set(value) {
                window.clearTimeout(checkNeedCanTouchIframePlayerModeTimer)
                field = value
            }

        /**
         * 檢查係米低訊號而去顯示ChannelDescription嘅Timer
         *
         * 此值為清除之前Player嘅Timer
         * 因當觀眾轉台後
         * 上一台Player嘅Timer仍在計時
         * */
        private var checkIsLowSignalShowChannelDescriptionTimer = 0
            set(value) {
                window.clearTimeout(checkIsLowSignalShowChannelDescriptionTimer)
                field = value
            }
    }

    private val iframePlayer: dynamic = document.getElementById("iframePlayer")
    private val watchingCounter: WatchingCounter = WatchingCounter(channel)

    enum class OnPlayerEvent{
        playing,
        notPlaying
    }

    interface OnPlayerEventListener{
        fun on(onPlayerEvent: OnPlayerEvent)
    }

    private var onPlayerEvents: ArrayList<OnPlayerEventListener> = ArrayList()

    fun addOnPlayerEventListener(onPlayerEventListener: OnPlayerEventListener) {
        onPlayerEvents.add(onPlayerEventListener)
    }


    /**
     * 片源表
     *
     * 由於唔能夠響主線程攞個表返來
     * 所以此值會設定響iframePlayer Load好個頻道之後
     * 先初始化此值
     * */
    var videoTracks: ArrayLinkList<TrackDescription> = {
        addOnPlayerEventListener(object : OnPlayerEventListener {
            var isInit: Boolean = false
            override fun on(onPlayerEvent: OnPlayerEvent) {
                when (onPlayerEvent) {
                    OnPlayerEvent.playing -> {
                        if(!isInit){
                            //設定VideoTracks值
                            callIframePlayerFunction("onGetIframePlayerVideoTracks", "", fun(tracks){
                                callIframePlayerFunction("onGetIframePlayerVideoTrack", "", fun(track){
                                    videoTracks = TrackDescription.fromIframePlayerReturnTrackDescriptionsToKotilnUseableTrackDescriptions(
                                            tracks, track
                                    )
                                    videoTracks.addOnNodeEventListener(object : ArrayLinkList.OnNodeEventListener<TrackDescription> {
                                        override fun OnNodeIDChanged(
                                                preChangeNodeID: Int?, postChangeNodeID: Int?,
                                                preChangeNode: TrackDescription?, postChangeNode: TrackDescription?
                                        ) {
                                            callIframePlayerFunction("onSetIframePlayerVideoTrack", postChangeNode)
                                            localStorage.setItem("RecentlyChannel${channel.number}VideoTrackID", postChangeNodeID.toString())
                                            VirtualRemote.updateVideoInformation()
                                        }
                                    })
                                    videoTracks.designated(
                                            localStorage.getItem("RecentlyChannel${channel.number}VideoTrackID")?.toIntOrNull()?:0
                                    )
                                })
                            })
                            isInit = true
                        }
                    }
                }
            }
        })
        ArrayLinkList(TrackDescription(-5, "-------"))
    }()
        private set

    /**
     * 聲道表
     *
     * 由於唔能夠響主線程攞個表返來
     * 所以此值會設定響iframePlayer Load好個頻道之後
     * 先初始化此值
     * */
    var audioTracks: ArrayLinkList<TrackDescription> = {
        addOnPlayerEventListener(object : OnPlayerEventListener {
            var isInit: Boolean = false
            override fun on(onPlayerEvent: OnPlayerEvent) {
                when (onPlayerEvent) {
                    OnPlayerEvent.playing -> {
                        if(!isInit){

                            isInit = true
                        }
                        //設定AudioTracks值
                        callIframePlayerFunction("onGetIframePlayerAudioTracks", "", fun(tracks){
                            callIframePlayerFunction("onGetIframePlayerAudioTrack", "", fun(track){
                                audioTracks = TrackDescription.fromIframePlayerReturnTrackDescriptionsToKotilnUseableTrackDescriptions(
                                        tracks, track
                                )
                                audioTracks.addOnNodeEventListener(object : ArrayLinkList.OnNodeEventListener<TrackDescription> {
                                    override fun OnNodeIDChanged(
                                            preChangeNodeID: Int?, postChangeNodeID: Int?,
                                            preChangeNode: TrackDescription?, postChangeNode: TrackDescription?
                                    ) {
                                        callIframePlayerFunction("onSetIframePlayerAudioTrack", postChangeNode)
                                        localStorage.setItem("RecentlyChannel${channel.number}AudioTrackID", postChangeNodeID.toString())
                                        VirtualRemote.updateAudioInformation()
                                    }
                                })
                                audioTracks.designated(
                                        localStorage.getItem("RecentlyChannel${channel.number}AudioTrackID")?.toIntOrNull()?:0
                                )
                            })
                        })
                    }
                }
            }
        })
        ArrayLinkList(TrackDescription(-5, "-------"))
    }()
        private set

    /**
     * 字幕表
     *
     * 由於唔能夠響主線程攞個表返來
     * 所以此值會設定響iframePlayer Load好個頻道之後
     * 先初始化此值
     * */
    var subtitleTracks: ArrayLinkList<TrackDescription> = {
        addOnPlayerEventListener(object : OnPlayerEventListener {
            var isInit: Boolean = false
            override fun on(onPlayerEvent: OnPlayerEvent) {
                when (onPlayerEvent) {
                    OnPlayerEvent.playing -> {
                        if(!isInit){
                            //設定SubtitleTracks值
                            callIframePlayerFunction("onGetIframePlayerSubtitleTracks", "", fun(tracks){
                                callIframePlayerFunction("onGetIframePlayerSubtitleTrack", "", fun(track){
                                    subtitleTracks = TrackDescription.fromIframePlayerReturnTrackDescriptionsToKotilnUseableTrackDescriptions(
                                            tracks, track
                                    )
                                    subtitleTracks.addOnNodeEventListener(object : ArrayLinkList.OnNodeEventListener<TrackDescription> {
                                        override fun OnNodeIDChanged(
                                                preChangeNodeID: Int?, postChangeNodeID: Int?,
                                                preChangeNode: TrackDescription?, postChangeNode: TrackDescription?
                                        ) {
                                            callIframePlayerFunction("onSetIframePlayerSubtitleTrack", postChangeNode)
                                            localStorage.setItem("RecentlyChannel${channel.number}SubtitleTrackID", postChangeNodeID.toString())
                                            VirtualRemote.updateSubtitleInformation()
                                        }
                                    })
                                    subtitleTracks.designated(
                                            localStorage.getItem("RecentlyChannel${channel.number}SubtitleTrackID")?.toIntOrNull()?:0
                                    )
                                })
                            })
                            isInit = true
                        }

                    }
                }
            }
        })
        ArrayLinkList(TrackDescription(-5, "-------"))
    }()
        private set


    private val volumeInit = {
        addOnPlayerEventListener(object : OnPlayerEventListener {
            var isInit: Boolean = false
            override fun on(onPlayerEvent: OnPlayerEvent) {
                when (onPlayerEvent) {
                    OnPlayerEvent.playing -> {
                        if(!isInit){
                            //讀取最近設定音量再去設定IframePlayer音量
                            callIframePlayerFunction("onSetIframePlayerVolume",
                                    localStorage.getItem("RecentlyVolume")?.toDoubleOrNull()?:100.0
                            )
                            isInit = true
                        }
                    }
                }
            }
        })
    }()

    /**
     * 設定iframePlayer嘅音量資訊
     *
     * 注:音量值用Double原因係因為有啲IframePlayer嘅音量值有小數
     * 小數中有取捨使到有機會調教唔到音量值
     * */
    fun setVolume(volume: Double) {
        var _volume = volume
        if(100 < _volume){_volume = 100.0}
        if(_volume < 0){_volume = 0.0}
        callIframePlayerFunction("onSetIframePlayerVolume", _volume)
        localStorage.setItem("RecentlyVolume", _volume.toString())//儲存低返最近設定音量
        VolumeDescription.show(3000)
    }

    /**
     * 獲取iframePlayer嘅音量資訊
     *
     * 注:音量值用Double原因係因為有啲IframePlayer嘅音量值有小數
     * 小數中有取捨使到有機會調教唔到音量值
     * */
    fun getVolume(onReturn: (volume: Double)->Unit) {
        callIframePlayerFunction("onGetIframePlayerVolume", "", fun(returnValue){
            onReturn(returnValue?.toString()?.toDoubleOrNull()?:100.0)
        })
    }


    private val mutedInit = {
        addOnPlayerEventListener(object : OnPlayerEventListener {
            var isInit: Boolean = false
            override fun on(onPlayerEvent: OnPlayerEvent) {
                when (onPlayerEvent) {
                    OnPlayerEvent.playing -> {
                        if(!isInit){
                            //因依家大部分 <瀏覽器> 唔畀自動播放, 如果要自動播放一定要將Player設為 <靜音>
                            if(isCheckVideoAutoPlayNeedToMute){
                                CanAutoplay.checkVideoAutoPlayNeedToMute(fun(){ setMuted(false) }, fun(){ setMuted(true) })
                            }else{
                                setMuted(false)
                            }
                            isInit = true
                        }
                    }
                }
            }
        })
    }()

    /**
     * 設定iframePlayer嘅靜音資訊
     * */
    fun setMuted(muted: Boolean) {
        callIframePlayerFunction("onSetIframePlayerMuted", muted)
        MutedDescription.update()
    }

    /**
     * 獲取iframePlayer嘅靜音資訊
     * */
    fun getMuted(onReturn: (muted: Boolean)->Unit) {
        callIframePlayerFunction("onGetIframePlayerMuted", "", fun(returnValue){
            onReturn(returnValue?.toString()?.toBoolean()?:true)
        })
    }


    /**
     *  播放
     *
     *  此Function防止Player冇自動播放時手動播放
     */
    fun play(){
        callIframePlayerFunction("onSetIframePlayerPlay", "")
    }

    /**
     * 當iframePlayer開始播放頻道時
     * 會執行此function
     * 即iframePlayer正確地播放緊
     * 有關資料可讀取
     * */
    private val onPlaying = fun(){ for(event in onPlayerEvents){ event.on(OnPlayerEvent.playing) } }

    /**
     * 當iframePlayer冇進行播放頻道時
     * 會執行此function
     * */
    private val onNotPlaying = fun(){ for(event in onPlayerEvents){ event.on(OnPlayerEvent.notPlaying) } }

    /******************************************************************************************************************/
    /**
    /**
     * 設定為最高畫質片源
    */
    private fun setHighestVideoQuality() {
    //designatedVideoTrack(getVideoTracks().size - 1)
    //updateVideoTrack()
    }

    /**
     * 設定為最低畫質片源
    */
    private fun setLowestVideoQuality() {
    //designatedVideoTrack(1)//因第0片源為冇畫面影片,所以第1片源先至係最低畫質
    //updateVideoTrack()
    }

    /**
     * 設定為自動選擇畫質片源
    */
    private fun setAutoSelectVideoQuality() {
    //designatedVideoTrack(-1)//-1為自動選擇畫質片源
    //updateVideoTrack()
    }
     */

    /**
     * 去依家嘅片源嘅下一個片源
     */
    fun nextVideoTrack() {
        player.videoTracks.next()
    }

    /**
     * 去依家嘅片源嘅上一個片源
     */
    fun previousVideoTrack() {
        player.videoTracks.previous()
    }

    /**
     * 去特定片源
     * @param videoTrackID 要轉去片源ID
     */
    @JsName("designatedVideoTrack") fun designatedVideoTrack(videoTrackID: Int): Boolean {
        val videoTracksNodeID = TrackDescription.toTracksNodeID(player.videoTracks, videoTrackID)

        if (videoTracksNodeID != null) {
            player.videoTracks.designated(videoTracksNodeID)
            return true
        } else {
            Dialogue.getDialogues(fun(dialogues) {
                PromptBox.promptMessage(dialogues.node?.canNotFind ?: "")
            })
            return false
        }
    }


    /**
     * 去依家嘅聲道嘅下一個聲道
     */
    fun nextAudioTrack() {
        player.audioTracks.next()
    }

    /**
     * 去依家嘅聲道嘅上一個聲道
     */
    fun previousAudioTrack() {
        player.audioTracks.previous()
    }

    /**
     * 去特定聲道
     * @param audioTrackID 要轉去聲道ID
     */
    @JsName("designatedAudioTrack") fun designatedAudioTrack(audioTrackID: Int): Boolean {
        val audioTracksNodeID = TrackDescription.toTracksNodeID(player.audioTracks, audioTrackID)

        if (audioTracksNodeID != null) {
            player.audioTracks.designated(audioTracksNodeID)
            return true
        } else {
            Dialogue.getDialogues(fun(dialogues) {
                PromptBox.promptMessage(dialogues.node?.canNotFind ?: "")
            })
            return false
        }
    }


    /**
     * 去依家嘅字幕嘅下一個字幕
     */
    fun nextSubtitleTrack() {
        player.subtitleTracks.next()
    }

    /**
     * 去依家嘅字幕嘅上一個字幕
     */
    fun previousSubtitleTrack() {
        player.subtitleTracks.previous()
    }

    /**
     * 去特定字幕
     * @param subtitleTrackID 要轉去字幕ID
     */
    @JsName("designatedSubtitleTrack") fun designatedSubtitleTrack(subtitleTrackID: Int): Boolean {
        val subtitleTracksNodeID = TrackDescription.toTracksNodeID(player.subtitleTracks, subtitleTrackID)

        if (subtitleTracksNodeID != null) {
            player.subtitleTracks.designated(subtitleTracksNodeID)
            return true
        } else {
            Dialogue.getDialogues(fun(dialogues) {
                PromptBox.promptMessage(dialogues.node?.canNotFind ?: "")
            })
            return false
        }
    }

    /******************************************************************************************************************/
    /**
     * 提升音量
     *
     * 由於其他平台需要其他位置設置提升音量
     * 因此此值可被修改成學合其他平台嘅程序
     * @returns Double 現在音量
     * */
    var volumeUp = fun(){
        getVolume(fun(volume){
            setVolume(volume + 1.0)
        })
    }

    /**
     * 降底音量
     *
     * 由於其他平台需要其他位置設置降底音量
     * 因此此值可被修改成學合其他平台嘅程序
     * @returns Double 現在音量
     * */
    var volumeDown = fun(){
        getVolume(fun(volume){
            setVolume(volume - 1.0)
        })
    }

    /**
     * 設換靜音
     *
     * Call一次靜音,再Call取消靜音
     * 由於其他平台需要其他位置設置設換靜音
     * 因此此值可被修改成學合其他平台嘅程序
     * */
    var volumeMute = fun(){
        getMuted(fun(volume){
            setMuted(!volume)
        })
    }

    /******************************************************************************************************************/
    /**
     *
     * */
    enum class ProgrammableColor {
        red,
        green,
        yellow,
        blue
    }

    /**
     * programmable鍵 嘅功能
     * @param color 咩野顏色嘅programmable鍵
     */
    @JsName("programmable") fun programmable(color: ProgrammableColor) {
        var colorString = ""
        when(color){
            ProgrammableColor.red       -> {colorString = "red"}
            ProgrammableColor.green     -> {colorString = "green"}
            ProgrammableColor.yellow    -> {colorString = "yellow"}
            ProgrammableColor.blue      -> {colorString = "blue"}
        }
        callIframePlayerFunction("onClickProgrammableButton", colorString)
    }

    /******************************************************************************************************************/
    private val callIframePlayerFunctionList = ArrayList<dynamic>()

    private fun setListenIframePlayer(){
        window.addEventListener("message", fun(event: dynamic){
            try{
                val callMessage = JSON.parse<dynamic>(event.data.toString())
                if (callMessage.name == null){
                    return
                }else if(callMessage.name == "HKNBPCore"){
                    // 之前callIframePlayerFunction嘅Return
                    for(obj in callIframePlayerFunctionList){
                        if(obj.id == callMessage.id){
                            obj.onReturn(callMessage.returnValue)
                            callIframePlayerFunctionList.remove(obj)
                        }
                    }
                }else if(callMessage.name == "IframePlaye"){
                    // 畀IframePlayer方便Call
                    val onPlaying = onPlaying
                    val onNotPlaying = onNotPlaying

                    /**
                    var onReturn = fun(returnValue: dynamic){
                    val obj = callMessage
                    obj.returnValue = returnValue
                    window.parent.postMessage(JSON.stringify(obj), "*")
                    }*/
                    eval(callMessage.functionName + "()")
                }
            }catch(e: dynamic){
                println("callIframePlayerFunction衰左: ${e}" + "\n" +
                        "JSON字串(message)內容: ${event.data.toString()}" + "\n" +
                        "Event內容: ${JSON.stringify(event)}"
                )
            }
        }, false)
    }

    private fun callIframePlayerFunction(
            functionName: String, value: dynamic = "", onReturn: (returnValue: dynamic)->Unit = fun(returnValue){}
    ){
        val caller = js("{}")
        caller.functionName = functionName
        caller.value = value
        caller.name = "HKNBPCore"
        caller.id = Date().getTime().toString() + Random.nextInt(0, 99999999)
        caller.onReturn = onReturn
        callIframePlayerFunctionList.add(caller)
        window.setTimeout(fun(){
            callIframePlayerFunctionList.remove(caller) //如果太耐冇return就響List自動清除免堆垃圾
        }, 60000)
        try {
            iframePlayer.contentWindow.postMessage(JSON.stringify(caller), "*")
        } catch (e: dynamic){ println("iframePlayer有啲Function搵唔到或發生問題: $e") }
    }

    init {
        addOnPlayerEventListener(object : OnPlayerEventListener {
            private var isPlaying: Boolean = false
            private var numberOfPlaying: Int = 0
            private var isLowSignalShowChannelDescription = false
            override fun on(onPlayerEvent: OnPlayerEvent) {
                when (onPlayerEvent) {
                    OnPlayerEvent.playing -> {
                        isPlaying = true
                        numberOfPlaying++
                        if(isLowSignalShowChannelDescription){
                            isLowSignalShowChannelDescription = false
                            ChannelDescription.hide()
                        }
                        ChannelDescription.show(5000)
                        VirtualRemote.update()
                        UserControlPanel.cannotTouchIframePlayerMode()
                    }
                    OnPlayerEvent.notPlaying -> {
                        isPlaying = false
                        if(0 < numberOfPlaying){
                            checkIsLowSignalShowChannelDescriptionTimer = window.setTimeout(fun(){
                                if(!isPlaying){
                                    isLowSignalShowChannelDescription = true
                                    ChannelDescription.show()
                                    PromptBox.promptMessage("訊號接收不良")
                                }
                            }, 10000)
                        }
                    }
                }
            }
            init {
                ChannelDescription.show()
                ChannelDescription.update()
                //如果冇自動播放就換到手動播放模式
                checkNeedCanTouchIframePlayerModeTimer = window.setTimeout(fun() {
                    if (!isPlaying && numberOfPlaying == 0) {
                        UserControlPanel.canTouchIframePlayerMode()
                        PromptBox.promptMessage("已切換到手動播放模式")
                    }
                }, 10000)
            }
        })
        iframePlayer?.src = channel.sources.node?.iFramePlayerSrc?: "iframePlayer/videojs_hls.html"
        iframePlayer?.onload = fun(){
            setListenIframePlayer()
            callIframePlayerFunction(
                    "onIframePlayerInit",
                    channel.sources.node?.link?:
                    "https://d2zihajmogu5jn.cloudfront.net/bipbop-advanced/bipbop_16x9_variant.m3u8"
            )
        }

    }
}