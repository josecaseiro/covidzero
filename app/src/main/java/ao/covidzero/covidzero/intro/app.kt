package ao.covidzero.covidzero.intro

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex

class App : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        mSelf = this
    }

    companion object {
        private var mSelf: App? = null

        fun self(): App {
            return mSelf!!
        }
    }

}
