/**
 * Created by outcastgeek on 8/17/2015.
 */

var React = require("react"),
    ReactDOM = require("react-dom"),
    ReactDOMServer = require('react-dom/server'),
    injectTapEventPlugin = require('react-tap-event-plugin'),
    XMLHttpRequest = require('xhr2'),
    Parse = require('parse/node');

// React Tap Plugin Setup
injectTapEventPlugin();

// Fixes for Core Async on NodeJs due to goog.global.*
this.setTimeout = setTimeout;
this.clearTimeout = clearTimeout;
this.setInterval = setInterval;
this.clearInterval = clearInterval;

// Initialize Parse
(function() {
    var applicationId = process.env.AID,
	javaScriptKey = process.env.JSKEY,
	masterKey = process.env.MKEY;
    Parse.initialize(applicationId, javaScriptKey, masterKey);
})();


