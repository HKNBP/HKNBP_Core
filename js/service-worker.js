const version = "0.9.11";
const cacheName = `airhorner-${version}`;
self.addEventListener('install', e => {
  e.waitUntil(
    caches.open(cacheName).then(cache => {
      return cache.addAll([
        `/`,
        `/index.html`,
        `/styles/main.css`,
        `/scripts/main.min.js`,
        `/scripts/comlink.global.js`,
        `/scripts/messagechanneladapter.global.js`,
        `/scripts/pwacompat.min.js`,
        `/sounds/airhorn.mp3`
      ])
          .then(() => self.skipWaiting());
    })
  );
});

self.addEventListener('activate', event => {
  event.waitUntil(self.clients.claim());
});

self.addEventListener('fetch', event => {
  event.respondWith(
    caches.open(cacheName)
      .then(cache => cache.match(event.request, {ignoreSearch: true}))
      .then(response => {
      return response || fetch(event.request);
    })
  );
});



/**
var cacheName = 'hello-world-page';
var filesToCache = [
  '/',
  '/index.html'
];
self.addEventListener('install', function(e) {
  console.log('[ServiceWorker] Install');
  e.waitUntil(
    caches.open(cacheName).then(function(cache) {
      console.log('[ServiceWorker] Caching app shell');
      return cache.addAll(filesToCache);
    })
  );
});
self.addEventListener('activate',  event => {
  event.waitUntil(self.clients.claim());
});
self.addEventListener('fetch', event => {
  event.respondWith(
    caches.match(event.request, {ignoreSearch:true}).then(response => {
      return response || fetch(event.request);
    })
  );
});
*/

/**
// In your service worker
importScripts('https://cdnjs.cloudflare.com/ajax/libs/cache.adderall/1.0.0/cache.adderall.js');


// 监听 service worker 的 install 事件
this.addEventListener("install", function (event) {
    // 如果监听到了 service worker 已经安装成功的话，就会调用 event.waitUntil 回调函数
    event.waitUntil(
        // 安装成功后操作 CacheStorage 缓存，使用之前需要先通过 caches.open() 打开对应缓存空间。
        caches.open("cache-v2").then(function (cache) {
            // 通过 cache 缓存对象的 addAll 方法添加 precache 缓存
            return adderall.addAll(cache, [
                '/',
                '/index.html',
                '/x.txt',
                '/manifest.json'
            ]);
        })
    );
});
*/