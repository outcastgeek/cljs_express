/**
 * Created by outcastgeek on 8/17/2015.
 */

var React = require("react"),
    ReactDOM = require("react-dom"),
    ReactDOMServer = require('react-dom/server'),
    injectTapEventPlugin = require('react-tap-event-plugin'),
    XMLHttpRequest = require('xhr2');

// React Tap Plugin Setup
injectTapEventPlugin();

// Fixes for Core Async on NodeJs due to goog.global.*
this.setTimeout = setTimeout;
this.clearTimeout = clearTimeout;
this.setInterval = setInterval;
this.clearInterval = clearInterval;

