/*
 * Copyright (c) 2026 Feryael Justice. Todos los derechos reservados.
 */

package com.feryaeljustice.compoundinterestmaster.di

import com.feryaeljustice.compoundinterestmaster.data.manager.AdMobManager
import com.feryaeljustice.compoundinterestmaster.domain.manager.AdManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AdModule {

    @Binds
    @Singleton
    abstract fun bindAdManager(
        adMobManager: AdMobManager
    ): AdManager
}
