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

import org.w3c.dom.Element
import org.w3c.dom.get
import org.w3c.dom.url.URL
import org.w3c.xhr.XMLHttpRequest
import kotlin.browser.localStorage
import kotlin.browser.window
import kotlin.js.Date
import kotlin.js.JSON
import kotlin.js.Json
import kotlin.js.json
import kotlin.random.Random
import kotlinx.serialization.*
import kotlinx.serialization.json.*



object OfficialChannel {

    /**
     * 分析已讀取返來嘅電視頻道表資料
     * */
    private fun parseChannels(
            onParsedChannelsListener: (channels: ArrayLinkList<Channel>) -> Unit,
            onFailedParseChannelsListener: ()->Unit,
            vararg src: String
    ){
        LoadFile.load(fun(xmlHttp){
            onParsedChannelsListener(getChannels(xmlHttp))
        }, fun(){
            onFailedParseChannelsListener()
        }, src)
    }

    private fun getChannels(xmlHttp: XMLHttpRequest): ArrayLinkList<Channel>{
        val channels = ArrayLinkList<Channel>()

        var i = 0
        while(i < (xmlHttp.responseXML?.getElementsByTagName("channel")?.length ?: 0)) {
            val number      = getNumber(xmlHttp.responseXML?.getElementsByTagName("channel")?.get(i))
            val name        = getName(xmlHttp.responseXML?.getElementsByTagName("channel")?.get(i))
            val sources     = getSources(xmlHttp.responseXML?.getElementsByTagName("channel")?.get(i))
            val information = getInformation(xmlHttp.responseXML?.getElementsByTagName("channel")?.get(i))

            channels.add(Channel(number, name, sources, information))
            i++
        }
        if(i == 0){
            channels.add(Channel(0, "", ArrayLinkList(Channel.Source()), Channel.Information()))
        }

        return channels
    }

    private fun getNumber(element: Element?): Int {
        return element?.getElementsByTagName("number")?.get(0)?.innerHTML?.toIntOrNull()?: 0
    }

    private fun getName(element: Element?): String {
        return element?.getElementsByTagName("name")?.get(0)?.innerHTML?: ""
    }

    private fun getSources(element: Element?): ArrayLinkList<Channel.Source> {
        val sources = ArrayLinkList<Channel.Source>()

        var i = 0
        while(i < element?.getElementsByTagName("source")?.length ?: 0) {
            val description     = getDescription(element?.getElementsByTagName("source")?.get(i))
            val iFramePlayerSrc = getIFramePlayerSrc(element?.getElementsByTagName("source")?.get(i))
            val link            = getLink(element?.getElementsByTagName("source")?.get(i))

            sources.add(Channel.Source(description, iFramePlayerSrc, link))
            i++
        }
        if(i == 0){
            sources.add(Channel.Source("", "", ""))
        }
        return sources
    }

    private fun getInformation(element: Element?): Channel.Information {
        return Channel.Information(
                element?.getElementsByTagName("information")?.get(0)?.getAttribute("epgid") ?: "",
                element?.getElementsByTagName("information")?.get(0)?.getAttribute("src") ?: ""
        )
    }

    private fun getDescription(element: Element?): String {
        return element?.getElementsByTagName("dscription")?.get(0)?.innerHTML?: ""
    }

    private fun getIFramePlayerSrc(element: Element?): String {
        return element?.getElementsByTagName("iframeplayersrc")?.get(0)?.innerHTML?: ""
    }

    private fun getLink(element: Element?): String {
        return element?.getElementsByTagName("link")?.get(0)?.innerHTML?: ""
    }

    /**************************************************************************************************************/
    private val channelsInfoExpireTime: Int = 7 * 24 * 60 * 60 * 1000 //7日

    private var channelsInfoSaveDate: Date = {
        val channelsInfoSaveDateJsonString = localStorage.getItem("channelsInfoSaveDate")
        if(channelsInfoSaveDateJsonString != null){
            JSON.parse<Date>(channelsInfoSaveDateJsonString)
        }else{
            Date()
        }
    }()
    set(value) {
        localStorage.setItem("channelsInfoSaveDate", JSON.stringify(value))
        field = value
    }

    private var channelsCache: ArrayLinkList<Channel>? = {
        var officialChannels: ArrayLinkList<Channel>? = null/**
        val officialChannelsJsonString = localStorage.getItem("OfficialChannels")
        if(officialChannelsJsonString != null){
            val officialChannelsArray = JSON.parse<Array<Channel>>(officialChannelsJsonString)
            officialChannels = ArrayLinkList<Channel>(officialChannelsArray)
        }*/
        officialChannels
    }()
    set(value) {
        localStorage.setItem("OfficialChannels", JSON.stringify(value))
        field = value
    }

    /**
     * 讀取電視頻道表資料
     */
    fun getOfficialChannels(onLoadedChannelsListener: (channels: ArrayLinkList<Channel>)->Unit){
        /**
        try {
            println(channelsCache?.toString())
            println(JSON.stringify(channelsCache))
            println(channelsCache?.toJSON())

            val json = Json(JsonConfiguration.Stable)
            val _channelsCache = channelsCache
            if(_channelsCache != null){
                val sss = json.stringify(_channelsCache.toList())
                println(sss)
                val obj = json.parse<ArrayLinkList<Channel>>(sss)
                println(obj.size)
                println(obj.node)
                println(obj.node?.name)
            }

            println("======")
            println(channelsCache?.size)
            println(channelsCache?.node)
            println(channelsCache?.node?.name)
            println(JSON.stringify(channelsCache))

            println(channelsInfoSaveDate.getTime()+channelsInfoExpireTime)
            println(Date().getTime())
            println(channelsInfoSaveDate.getTime()+channelsInfoExpireTime < Date().getTime())
            println(channelsCache == null)
        }catch (e:dynamic){ println("XXX")}
        */
        if(channelsCache == null /**|| channelsInfoSaveDate.getTime()+channelsInfoExpireTime < Date().getTime()*/){
            parseChannels(fun(channels){
                channels.sortBy{ channel -> channel.number }
                channelsCache = channels
                onLoadedChannelsListener(channelsCache?:ArrayLinkList<Channel>())
            }, fun(){}, /**"${rootURL}data/official_channels.xml",*/ "data/official_channels.xml")
        }else{
            onLoadedChannelsListener(channels?:ArrayLinkList<Channel>())
        }
    }

    init{

    }
}