(ns com.wsscode.log-test
  (:require
    [clojure.test :refer [deftest is are run-tests testing]]))

(deftest sanity-test
  (is (= 1 1)))
