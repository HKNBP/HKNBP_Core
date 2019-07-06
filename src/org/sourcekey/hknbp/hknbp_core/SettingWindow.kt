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

import org.w3c.dom.*
import kotlin.browser.document
import kotlin.browser.localStorage
import kotlin.browser.window

object SettingWindow: UserInterface(
        "settingWindow",
        firstFocusElementID = "settingWindowHideButton",
        isFocusOutHide = true
) {
    val v11v0 = {
        document.getElementById("sss")?.innerHTML = "v11v0"
        ""
    }()
    private val settingWindow                   = document.getElementById("settingWindow") as HTMLDivElement
    private val hideButton                      = document.getElementById("settingWindowHideButton") as HTMLButtonElement
    private val languageSetHonJyutElegantSet    = document.getElementById("settingWindowLanguageSetHonJyutElegantSet") as HTMLButtonElement
    private val languageSetHonJyutColloquialSet = document.getElementById("settingWindowLanguageSetHonJyutColloquialSet") as HTMLButtonElement
    private val languageSetEnglishSet           = document.getElementById("settingWindowLanguageSetEnglishSet") as HTMLButtonElement
    private val languageSetPresetSet            = document.getElementById("settingWindowLanguageSetPresetSet") as HTMLButtonElement
    private val languageSetLanguageISOCodeInput = document.getElementById("settingWindowLanguageSetLanguageISOCodeInput") as HTMLInputElement
    private val languageSelectSequenceList      = document.getElementById("settingWindowLanguageSelectSequenceList") as HTMLSelectElement
    private val languageAddLanguage             = document.getElementById("settingWindowLanguageAddLanguage") as HTMLButtonElement
    private val languageRemoveLanguage          = document.getElementById("settingWindowLanguageRemoveLanguage") as HTMLButtonElement
    private val languageMoveUpLanguage          = document.getElementById("settingWindowLanguageMoveUpLanguage") as HTMLButtonElement
    private val languageMoveDownLanguage        = document.getElementById("settingWindowLanguageMoveDownLanguage") as HTMLButtonElement
    val v11v1 = {
        document.getElementById("sss")?.innerHTML = "v11v1"
        ""
    }()
    private val currentUserLanguage = js("navigator.language || navigator.userLanguage;") as String
    val v11v2 = {
        document.getElementById("sss")?.innerHTML = "v11v2"
        ""
    }()
    fun getLanguageSetting(): ArrayList<String?>{
        val userLanguageList = ArrayList<String?>()
        for(index in 0 until languageSelectSequenceList.length){
            userLanguageList.add((languageSelectSequenceList.options.get(index) as HTMLOptionElement).text)
        }
        return userLanguageList
    }
    val v11v3 = {
        document.getElementById("sss")?.innerHTML = "v11v3"
        ""
    }()
    private fun createLanguageOption(isoCode: String): HTMLOptionElement{
        val option = document.createElement("option") as HTMLOptionElement
        option.text = isoCode
        option.value = isoCode
        return option
    }
    val v11v4 = {
        document.getElementById("sss")?.innerHTML = "v11v4"
        ""
    }()
    fun initLangugeSetting(){
        languageAddLanguage.onclick = fun(event){
            if(languageSetLanguageISOCodeInput.value != ""){
                val option = document.createElement("option") as HTMLOptionElement
                option.text = languageSetLanguageISOCodeInput.value
                option.value = languageSetLanguageISOCodeInput.value
                languageSelectSequenceList.add(option)

                //儲存低返最近設定<語言選取次序>
                localStorage.setItem("RecentlyLanguageSelectSequence", languageSelectSequenceList.innerHTML)
            }
        }
        languageRemoveLanguage.onclick = fun(event){
            languageSelectSequenceList.remove(languageSelectSequenceList.selectedIndex)

            //儲存低返最近設定<語言選取次序>
            localStorage.setItem("RecentlyLanguageSelectSequence", languageSelectSequenceList.innerHTML)
        }
        languageMoveUpLanguage.onclick = fun(event){
            if(0 < languageSelectSequenceList.selectedIndex){
                val optionAIndex = languageSelectSequenceList.selectedIndex
                val optionBIndex = languageSelectSequenceList.selectedIndex - 1
                val options: ArrayList<HTMLOptionElement> = ArrayList()
                for(index in 0 until languageSelectSequenceList.length){
                    options.add(languageSelectSequenceList.options.get(index) as HTMLOptionElement)
                }
                val optionA = options.get(optionAIndex)
                val optionB = options.get(optionBIndex)
                options.set(optionAIndex, optionB)
                options.set(optionBIndex, optionA)
                languageSelectSequenceList.innerHTML = ""
                for(index in 0 until options.size){
                    val option = options.getOrNull(index)
                    if(option != null){
                        languageSelectSequenceList.append(option)
                    }
                }

                //儲存低返最近設定<語言選取次序>
                localStorage.setItem("RecentlyLanguageSelectSequence", languageSelectSequenceList.innerHTML)
            }
        }
        languageMoveDownLanguage.onclick = fun(event){
            if(languageSelectSequenceList.selectedIndex < languageSelectSequenceList.length){
                val optionAIndex = languageSelectSequenceList.selectedIndex
                val optionBIndex = languageSelectSequenceList.selectedIndex + 1
                val options: ArrayList<HTMLOptionElement> = ArrayList()
                for(index in 0 until languageSelectSequenceList.length){
                    options.add(languageSelectSequenceList.options.get(index) as HTMLOptionElement)
                }
                val optionA = options.get(optionAIndex)
                val optionB = options.get(optionBIndex)
                options.set(optionAIndex, optionB)
                options.set(optionBIndex, optionA)
                languageSelectSequenceList.innerHTML = ""
                for(index in 0 until options.size){
                    val option = options.getOrNull(index)
                    if(option != null){
                        languageSelectSequenceList.append(option)
                    }
                }

                //儲存低返最近設定<語言選取次序>
                localStorage.setItem("RecentlyLanguageSelectSequence", languageSelectSequenceList.innerHTML)
            }
        }

        languageSetHonJyutElegantSet.onclick = fun(event){
            languageSelectSequenceList.innerHTML = ""
            languageSelectSequenceList.append(
                    createLanguageOption("hon-JyutElegant"),
                    createLanguageOption("zh-HK"),
                    createLanguageOption("zh-Hant-HK"),
                    createLanguageOption("zh-TW"),
                    createLanguageOption("zh-Hant-TW"),
                    createLanguageOption("zh-Hant"),
                    createLanguageOption("hon-JyutColloquial"),
                    createLanguageOption("zh-Yue"),
                    createLanguageOption(currentUserLanguage),
                    createLanguageOption("en")
            )
        }
        languageSetHonJyutColloquialSet.onclick = fun(event){
            languageSelectSequenceList.innerHTML = ""
            languageSelectSequenceList.append(
                    createLanguageOption("hon-JyutColloquial"),
                    createLanguageOption("zh-Yue"),
                    createLanguageOption("hon-JyutElegant"),
                    createLanguageOption("zh-HK"),
                    createLanguageOption("zh-Hant-HK"),
                    createLanguageOption("zh-TW"),
                    createLanguageOption("zh-Hant-TW"),
                    createLanguageOption("zh-Hant"),
                    createLanguageOption(currentUserLanguage),
                    createLanguageOption("en")
            )
        }
        languageSetEnglishSet.onclick = fun(event){
            languageSelectSequenceList.innerHTML = ""
            languageSelectSequenceList.append(
                    createLanguageOption("en"),
                    createLanguageOption(currentUserLanguage)
            )
        }
        languageSetPresetSet.onclick = fun(event){
            languageSelectSequenceList.innerHTML = ""
            languageSelectSequenceList.append(
                    createLanguageOption(currentUserLanguage),
                    createLanguageOption("en")
            )
        }

        languageSelectSequenceList.innerHTML =
                localStorage.getItem("RecentlyLanguageSelectSequence")?:
                        "<option value=\"${currentUserLanguage}\">${currentUserLanguage}</option>"
    }
    val v11v5 = {
        document.getElementById("sss")?.innerHTML = "v11v5"
        ""
    }()
    init {
        document.getElementById("sss")?.innerHTML = "v11.0"
        settingWindow.style.cursor = "auto"
        document.getElementById("sss")?.innerHTML = "v11.1"
        hideButton.onclick = fun(event){ hide() }
        document.getElementById("sss")?.innerHTML = "v11.2"
        initLangugeSetting()
        document.getElementById("sss")?.innerHTML = "v11.3"
    }
}