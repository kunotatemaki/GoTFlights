package com.rookia.gotflights.di.modules

import com.rookia.gotflights.framework.utils.security.EncryptionImpl
import com.rookia.gotflights.framework.utils.view.ViewUtilsImpl
import com.rookia.gotflights.data.persistence.PersistenceManager
import com.rookia.gotflights.framework.persistence.PersistenceManagerImpl
import com.rookia.gotflights.data.preferences.PreferencesManager
import com.rookia.gotflights.framework.preferences.PreferencesManagerImpl
import com.rookia.gotflights.data.repository.Repository
import com.rookia.gotflights.framework.repository.RepositoryImpl
import com.rookia.gotflights.data.resources.ResourcesManager
import com.rookia.gotflights.framework.resources.ResourcesManagerImpl
import com.rookia.gotflights.utils.ViewUtils
import com.rookia.gotflights.utils.security.Encryption
import dagger.Binds
import dagger.Module
import javax.inject.Singleton


@Module(includes = [(ViewModelModule::class)])
abstract class BindsModule {


    @Binds
    abstract fun providesResourcesManager(resourcesManagerImpl: ResourcesManagerImpl): ResourcesManager

    @Binds
    abstract fun providesPersistenceManager(persistenceManagerImpl: PersistenceManagerImpl): PersistenceManager

    @Binds
    abstract fun providesEncryption(encryptionImpl: EncryptionImpl): Encryption

    @Binds
    abstract fun providesPreferencesManager(preferencesManagerImpl: PreferencesManagerImpl): PreferencesManager

    @Binds
    abstract fun providesRepository(repositoryImpl: RepositoryImpl): Repository

    @Binds
    @Singleton
    abstract fun providesViewUtils(viewUtilsImpl: ViewUtilsImpl): ViewUtils


}