/*
 * Copyright (c) 2026 Feryael Justice. Todos los derechos reservados.
 */

package com.feryaeljustice.compoundinterestmaster.di

import com.feryaeljustice.compoundinterestmaster.data.manager.FirebaseAnalyticsManager
import com.feryaeljustice.compoundinterestmaster.domain.manager.AnalyticsManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsModule {

    @Binds
    @Singleton
    abstract fun bindAnalyticsManager(
        firebaseAnalyticsManager: FirebaseAnalyticsManager
    ): AnalyticsManager
}
