/***/
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
    "js/can-autoplay.min.js",
    "js/jquery.tabbable.js",
    "js/jquery-3.4.1.min.js",
    "out/production/HKNBP_Core/HKNBP_Core.js",
    "out/production/HKNBP_Core/HKNBP_Core.meta.js",
    "out/production/HKNBP_Core/HKNBP_Core/org/sourcekey/hknbp/hknbp_core/hknbp_core.kjsm",
    "out/production/HKNBP_Core/lib/kotlin.js",
    "out/production/HKNBP_Core/lib/kotlin.meta.js"
];

workbox.precaching.precacheAndRoute(cacheFiles, {
  // Ignore all URL parameters.
  ignoreURLParametersMatching: [/.*/]
});

/**
workbox.routing.registerRoute(
    new RegExp('.*\.*'),
    workbox.strategies.networkFirst({
      cacheName: 'cache',
      plugins: [
        new workbox.expiration.Plugin({
          // Only cache requests for a week
          maxAgeSeconds: 7 * 24 * 60 * 60,
          // Only cache 10 requests.
          maxEntries: 100,
        }),
      ]
    })
);
*/
//workbox.precaching.cleanupOutdatedCaches();

