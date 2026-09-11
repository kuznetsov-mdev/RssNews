package org.kuznetsov.rssnews

import android.app.Application
import android.content.Context

class RssNewsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this
    }

    companion object {
        lateinit var appContext: Context
            private set
    }
}