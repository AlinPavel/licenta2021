import 'zone.js/dist/zone';
import '@angular/localize/init';
import 'zone.js';
import 'core-js/proposals/reflect-metadata';

// Fix needed for SockJS, see https://github.com/sockjs/sockjs-client/issues/439
(window as any).global = window;
