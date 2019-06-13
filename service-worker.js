importScripts('https://storage.googleapis.com/workbox-cdn/releases/4.3.1/workbox-sw.js');

if (workbox) {
  console.log("Yay! Workbox is loaded 🎉");
} else {
  console.log("Boo! Workbox didn't load 😬");
}

// 使用precache功能，在offline下也可以執行
// 要存進cache storage裡的檔案清單
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
    "css/fontello-ie7.css",
    "css/fontello-ie7-codes.css",
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
    "img/programmeNullIcon.png",
    "js/jquery.tabbable.js",
    "out/production/HKNBP_Core/HKNBP_Core.js",

    "https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js",
    "https://video-dev.github.io/can-autoplay/build/can-autoplay.min.js",
    "https://cdnjs.cloudflare.com/ajax/libs/platform/1.3.5/platform.min.js",
    "https://cdn.jsdelivr.net/npm/kotlin@1.3.31/kotlin.js"
];

workbox.precaching.precacheAndRoute(cacheFiles, {
  // Ignore all URL parameters.
  ignoreURLParametersMatching: [/.*/]
});

//workbox.precaching.cleanupOutdatedCaches();

