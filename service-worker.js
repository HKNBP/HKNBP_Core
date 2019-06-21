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

importScripts('https://storage.googleapis.com/workbox-cdn/releases/4.3.1/workbox-sw.js');

// 檢查Workbox
if (workbox) {
  console.log("Yay! Workbox is loaded 🎉");
} else {
  console.log("Boo! Workbox didn't load 😬");
}

// 使用precache功能，在offline下也可以執行
var cacheFiles = [
    ".",
    "index.html",
    "consent.html",
    "watching-counter.html",
    "manifest.json",
    "css/animation.css",
    "css/fontello.css",
    "css/fontello-codes.css",
    "css/fontello-embedded.css",
    "data/dialogue.json",
    "data/tv_channels.xml",
    "font/fontello.eot",
    "font/fontello.svg",
    "font/fontello.ttf",
    "font/fontello.woff",
    "font/fontello.woff2",
    "iframePlayer/videojs_dash.html",
    "iframePlayer/videojs_hls.html",
    "iframePlayer/youtube_api.html",
    "img/logo.png",
    "img/programmeNullIcon.png",
    "js/jquery.tabbable.js",
    "out/production/HKNBP_Core/HKNBP_Core.js",

    "https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js",
    "https://video-dev.github.io/can-autoplay/build/can-autoplay.min.js",
    "https://cdnjs.cloudflare.com/ajax/libs/platform/1.3.5/platform.min.js",
    "https://cdn.jsdelivr.net/npm/kotlin@1.3.31/kotlin.js"
];

/**
// 設立Cache
// 當接到版本名就開始設立Cache
new BroadcastChannel('sw-messages').addEventListener('message', event => {
    if(event.data.coreVersion){
    console.log(event.data.coreVersion)

    }
});*/

// Cache名 設置
workbox.core.setCacheNameDetails({
    prefix: "HKNBP",
    suffix: "0.9.40", // 控制cache版本
    precache: "precache",
    runtime: "runtime-cache"
});

// 使用precache功能，在offline下也可以執行
workbox.precaching.precacheAndRoute(cacheFiles, {
    // Ignore all URL parameters.
    ignoreURLParametersMatching: [/.*/]
});

// 刷新Cache
self.addEventListener('activate', function(event) {
    event.waitUntil(
        // 揀選舊版本cache去刪除
        caches.keys().then(cacheNames => {
            console.log("正儲存嘅版本Cache有: " + cacheNames);
            return Promise.all(
                cacheNames.filter(cacheName => {
                    // return true為刪除冇使用緊嘅cache
                    return cacheName !== workbox.core.cacheNames.precache;
                }).map(cacheName => {
                    console.log("刪除" + cacheName + "版本Cache");
                    // 被filter判定為要刪除嘅cache去刪除
                    return caches.delete(cacheName);
                })
            );
        }).then(() => {
            return self.clients.claim();
        })
    );
});
