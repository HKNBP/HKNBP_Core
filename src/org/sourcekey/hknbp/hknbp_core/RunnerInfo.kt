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

object RunnerInfo {
    val platform = js("platform")

    /**
     * 獲取系統家族
     * */
    fun getOsFamily(): String{
        val _getOS = js("""
        function (){
          var userAgent = window.navigator.userAgent,
              platform = window.navigator.platform,
              macosPlatforms = ['Macintosh', 'MacIntel', 'MacPPC', 'Mac68K'],
              windowsPlatforms = ['Win32', 'Win64', 'Windows', 'WinCE'],
              iosPlatforms = ['iPhone', 'iPad', 'iPod'],
              os = '';
          if (macosPlatforms.indexOf(platform) !== -1) {
            os = 'Mac OS';
          } else if (iosPlatforms.indexOf(platform) !== -1) {
            os = 'iOS';
          } else if (windowsPlatforms.indexOf(platform) !== -1) {
            os = 'Windows';
          } else if (/Android/.test(userAgent)) {
            os = 'Android';
          } else if (!os && /Linux/.test(platform)) {
            os = 'Linux';
          }

          return os;
        }
    """) as ()->String
        return _getOS()
    }

    /**
     * 獲取系統名
     *
     * 此funtion使用其他一個叫Platform.js嘅Lib
     * https://github.com/bestiejs/platform.js/
     * */
    fun getOsName(): String{
        return platform.os.toString()
    }

    /**
     * 獲取瀏覽器名
     *
     * 此funtion使用其他一個叫Platform.js嘅Lib
     * https://github.com/bestiejs/platform.js/
     * */
    fun getBrowserName(): String{
        return platform.name.toString() + " " + platform.version.toString()
    }
}