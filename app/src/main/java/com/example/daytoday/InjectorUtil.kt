package com.example.daytoday

object InjectorUtil {
    fun provideReceptionRepository(): Repository {
        val dataSource = DefaultDataSource
        return Repository(dataSource)
    }
}
