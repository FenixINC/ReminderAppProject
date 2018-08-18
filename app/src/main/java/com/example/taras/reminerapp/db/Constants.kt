package com.example.taras.reminerapp.db

/**
 * Created by Taras Koloshmatin on 26.07.2018
 */
class Constants {
    companion object {
        const val BASE_URL = "http://192.168.0.115:8080"
        const val BASE_SPARK_URL = "http://192.168.0.115:8081"
        const val BASE_ = "http://192.168.0.100:8080"

        //--- TYPE SERVERS:
        const val SERVER_DEFAULT = "default"
        const val SERVER_SPARK = "spark"
        const val SERVER_JOOBY = "jooby"

        //--- TYPE_REMIND:
        const val TYPE_USER_REMIND = "type_user_remind"
        const val TYPE_NEWS = "type_news"
        const val TYPE_EVENT = "type_event"
        const val TYPE_VIDEO = "type_video"
    }
}