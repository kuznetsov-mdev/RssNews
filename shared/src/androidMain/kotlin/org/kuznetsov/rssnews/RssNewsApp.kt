package org.kuznetsov.rssnews

import android.app.Application
import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.kuznetsov.rssnews.di.initKoin

class RssNewsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this

        initKoin {
            androidContext(appContext)
        }
    }

    companion object {
        lateinit var appContext: Context
            private set
    }
}
