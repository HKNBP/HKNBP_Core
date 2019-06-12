importScripts('https://storage.googleapis.com/workbox-cdn/releases/4.3.1/workbox-sw.js');

//importScripts("https://storage.googleapis.com/workbox-cdn/releases/3.6.3/workbox-sw.js");

if (workbox) {
  console.log(`Yay! Workbox is loaded 🎉`);
} else {
  console.log(`Boo! Workbox didn't load 😬`);
}

// 使用precache功能，在offline下也可以執行
// 要存進cache storage裡的檔案清單
var cacheFiles = [
  'index.html'
];

workbox.precaching.precacheAndRoute(
    cacheFiles,
    {
        ignoreUrlParametersMatching:[/tvchannel/]
    }
);

workbox.routing.registerRoute(
    new RegExp('.*\.*'),
    workbox.strategies.networkFirst({
      cacheName: 'cache',
      plugins: [
        new workbox.expiration.Plugin({
          // Only cache requests for a week
          maxAgeSeconds: 1,
          // Only cache 10 requests.
          maxEntries: 100,
        }),
      ]
    })
);

//workbox.precaching.cleanupOutdatedCaches();