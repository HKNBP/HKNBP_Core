//importScripts('https://storage.googleapis.com/workbox-cdn/releases/4.3.1/workbox-sw.js');

importScripts("https://storage.googleapis.com/workbox-cdn/releases/3.6.3/workbox-sw.js");

if (workbox) {
  console.log(`Yay! Workbox is loaded 🎉`);
} else {
  console.log(`Boo! Workbox didn't load 😬`);
}

// 使用precache功能，在offline下也可以執行
// 要存進cache storage裡的檔案清單
const precacheController = new workbox.precaching.PrecacheController();

precacheController.addToCacheList([
  "index.html"
]);

workbox.routing.registerRoute(
  new RegExp('.*\.*'),
  workbox.strategies.networkFirst()
);
/**
workbox.routing.registerRoute(
  // Cache CSS files
  new RegExp('.*\.css'),
  // Use cache but update in the background ASAP
  workbox.strategies.staleWhileRevalidate({
    // Use a custom cache name
    cacheName: 'css-cache',
  })
);

workbox.routing.registerRoute(
  // Cache image files
  /.*\.(?:png|jpg|jpeg|svg|gif)/,
  // Use the cache if it's available
  workbox.strategies.cacheFirst({
    // Use a custom cache name
    cacheName: 'image-cache',
    plugins: [
      new workbox.expiration.Plugin({
        // Cache only 20 images
        maxEntries: 20,
        // Cache for a maximum of a week
        maxAgeSeconds: 7 * 24 * 60 * 60,
      })
    ],
  })
);
*/
/**
self.addEventListener('install', (event) => {
  event.waitUntil(precacheController.install());
});
self.addEventListener('activate', (event) => {
  event.waitUntil(precacheController.cleanup());
});
self.addEventListener('fetch', (event) => {
  const cacheKey = precacheController.getCacheKeyForURL(event.request.url);
  event.respondWith(caches.match(cacheKey).then(...));
});
*/

workbox.precaching.cleanupOutdatedCaches()