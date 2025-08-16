(ns example.dev)

(defn main []
  (js/console.log "Loaded"))

(defn ^:dev/after-load reload []
  (js/console.log "Reloaded"))

(comment
  js/location.href

  :rcf)