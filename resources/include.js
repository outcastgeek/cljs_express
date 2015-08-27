/**
 * Created by outcastgeek on 8/17/2015.
 */

var React = require("react"),
    injectTapEventPlugin = require('react-tap-event-plugin'),
    MaterialUI = require('material-ui'),
    XMLHttpRequest = require('xhr2');

// Fixes for Core Async on NodeJs due to goog.global.*
this.setTimeout = setTimeout;
this.clearTimeout = clearTimeout;
this.setInterval = setInterval;
this.clearInterval = clearInterval;

