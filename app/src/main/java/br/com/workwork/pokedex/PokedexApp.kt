package br.com.workwork.pokedex

import android.app.Application
import br.com.workwork.pokedex.di.appModule
import br.com.workwork.pokedex.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class PokedexApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@PokedexApp)
            modules(appModule, networkModule)
        }
    }
}