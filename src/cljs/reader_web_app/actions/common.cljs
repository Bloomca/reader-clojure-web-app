(ns reader-web-app.actions.common
  (:require [reader-web-app.utils.api :as api]
            [reader-web-app.state :as state]))

(defn set-books-loading [is-fetching]
  (swap! state/app-state update-in [:books] assoc :is-fetching is-fetching))

(defn get-all-books-success [res]
  (set-books-loading false)
  ())

(defn get-all-books-error [err]
  (set-books-loading false)
  ())

(defn get-all-books-all []
  (set-books-loading true)
  (api/get "/books" {:handler get-all-books-success
                     :error-handler get-all-books-error}))

(defn get-all-books []
  (let [{books :books} @state/app-state
        should-fetch (= (count (:items books)) 0)]
    (if (= should-fetch true)
      (get-all-books-all))))

(defn setBookLoading [id is-fetching]
  (swap! state/app-state assoc-in [:books id] :is-fetching is-fetching))

(defn getBookSuccess [id res]
  (setBookLoading id false)
  ())

(defn getBookError [err]
  (setBookLoading id false)
  ())

(defn getBookCall [id]
  (setBookLoading id true)
  (api/get (str "/books/" id) {:handler (partial getBookSuccess id)
                               :error-handler (partial getBookError id)}))

(defn getBook [id]
  (let [{book :book} @state/app-state
        should-fetch (nil? (get book id))]
    (if (should-fetch)
      (getBookCall id))))

(defn getUserBooks []
  ())

(defn update-progress [in-progress]
  (swap! state/app-state assoc :in-progress in-progress))

(defn upload-book-success []
  (update-progress false)
  ())

(defn upload-book-error []
  (update-progress false)
  ())

(defn upload-book [book]
  (update-progress true)
  (api/post-json "/books" {:params {:book book}
                           :handler upload-book-success
                           :error-handler upload-book-error
                           :keywords? true}))

(defn updateSettings []
  ())

(get updateBookInfo []
  ())