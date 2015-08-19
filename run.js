try {
    require("source-map-support").install();
} catch(err) {
}
require("./out/goog/bootstrap/nodejs.js");
require("./out/cljs_express.js");
goog.require("cljs_express.core");
goog.require("cljs.nodejscli");
