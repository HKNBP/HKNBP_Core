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
import org.w3c.dom.HTMLElement
import kotlin.browser.document
import kotlin.browser.window
import kotlin.js.Date

object ChannelDescription: UserInterface("channelDescription") {
    private val channelDescription            = document.getElementById("channelDescription") as HTMLDivElement
    private val currentChannelName              = document.getElementById("channelDescriptionCurrentChannelName") as HTMLDivElement
    private val currentChannelNumber            = document.getElementById("channelDescriptionCurrentChannelNumber") as HTMLDivElement
    private val currentDate                     = document.getElementById("channelDescriptionCurrentDate") as HTMLDivElement
    //private val currentChannelBitRate           = document.getElementById("channelDescriptionCurrentChannelBitRate") as org.w3c.dom.HTMLDivElement
    //private val currentChannelResolution        = document.getElementById("channelDescriptionCurrentChannelResolution") as org.w3c.dom.HTMLDivElement
    //private val currentChannelAspectRatio       = document.getElementById("channelDescriptionCurrentChannelAspectRatio") as HTMLDivElement
    private val currentProgrammeTitle           = document.getElementById("channelDescriptionCurrentProgrammeTitle") as HTMLDivElement
    private val currentProgrammeSubTitle        = document.getElementById("channelDescriptionCurrentProgrammeSubTitle") as HTMLDivElement
    private val currentProgrammeEpisode         = document.getElementById("channelDescriptionCurrentProgrammeEpisode") as HTMLDivElement
    private val currentProgrammeBroadcastTime   = document.getElementById("channelDescriptionCurrentProgrammeBroadcastTime") as HTMLDivElement
    private val currentProgrammeDesc            = document.getElementById("channelDescriptionCurrentProgrammeDesc") as HTMLDivElement
    private val currentProgrammeCategory        = document.getElementById("channelDescriptionCurrentProgrammeCategory") as HTMLDivElement

    private fun setCurrentChannelName(){
        currentChannelName.innerHTML = channels.node?.name?: ""
    }

    private fun setCurrentChannelNumber(){
        currentChannelNumber.innerHTML = (channels.node?.number?:"").toString().padStart(3, '0')
    }

    private var currentDateTimer = 0

    private fun setCurrentDate(){
        val script = fun(){currentDate.innerHTML = Date().toLocaleString()}
        script()
        currentDateTimer = window.setInterval(script, 1000)
    }

    private fun setCurrentProgrammeTitle(){
        currentProgrammeTitle.innerHTML = ""
        channels.node?.information?.getXMLTV(fun(xmltv){
            currentProgrammeTitle.innerHTML = xmltv.programmes?.getProgrammeByTime()?.titles?.getElementsByLanguage(userLanguageList)?.getOrNull(0)?.title?: ""
        })
    }

    private fun setCurrentProgrammeSubTitle(){
        currentProgrammeSubTitle.innerHTML = ""
        channels.node?.information?.getXMLTV(fun(xmltv){
            currentProgrammeSubTitle.innerHTML = xmltv.programmes?.getProgrammeByTime()?.subTitles?.getElementsByLanguage(userLanguageList)?.getOrNull(0)?.subTitle?: ""
        })
    }

    private fun setCurrentProgrammeEpisode(){
        currentProgrammeEpisode.innerHTML = ""
        channels.node?.information?.getXMLTV(fun(xmltv){
            Dialogue.getDialogues(fun(dialogues){
                var episodeInnerHTML = ""
                val season = xmltv.programmes?.getProgrammeByTime()?.episodeNum?.getSeason()
                if(season != null){
                    episodeInnerHTML += dialogues.node?.programmeSeason?.replace("\${season}", season.toString())?: ""
                }
                val episode = xmltv.programmes?.getProgrammeByTime()?.episodeNum?.getEpisode()
                if(episode != null){
                    episodeInnerHTML += dialogues.node?.programmeEpisode?.replace("\${episode}", episode.toString())?: ""
                }

                currentProgrammeEpisode.innerHTML = episodeInnerHTML
            })
        })
    }

    private fun setCurrentProgrammeBroadcastTime(){
        currentProgrammeBroadcastTime.innerHTML = ""
        channels.node?.information?.getXMLTV(fun(xmltv){
            val programmeTime = xmltv.programmes?.getProgrammeByTime()
            if(programmeTime != null){
                val fromTime = programmeTime.start.getHours().toString().padStart(2, '0') +
                        ":" + programmeTime.start.getMinutes().toString().padStart(2, '0')
                val toTime = programmeTime.stop.getHours().toString().padStart(2, '0') +
                        ":" + programmeTime.stop.getMinutes().toString().padStart(2, '0')
                currentProgrammeBroadcastTime.innerHTML = fromTime+"-"+toTime
            }else{
                currentProgrammeBroadcastTime.innerHTML = ""
            }
        })
    }

    private fun setCurrentProgrammeDesc(){
        currentProgrammeDesc.innerHTML = ""
        channels.node?.information?.getXMLTV(fun(xmltv){
            currentProgrammeDesc.innerHTML = xmltv.programmes?.getProgrammeByTime()?.descs?.getElementsByLanguage(userLanguageList)?.getOrNull(0)?.desc?: ""
        })
    }

    private fun setCurrentProgrammeCategory(){
        currentProgrammeCategory.innerHTML = ""
        channels.node?.information?.getXMLTV(fun(xmltv){
            currentProgrammeCategory.innerHTML = xmltv.programmes?.getProgrammeByTime()?.categorys?.getElementsByLanguage(userLanguageList)?.getOrNull(0)?.category?: ""
        })
    }

    override fun update(){
        setCurrentChannelName()
        setCurrentChannelNumber()
        setCurrentDate()
        setCurrentProgrammeTitle()
        setCurrentProgrammeSubTitle()
        setCurrentProgrammeEpisode()
        setCurrentProgrammeDesc()
        setCurrentProgrammeBroadcastTime()
        setCurrentProgrammeCategory()
    }
}