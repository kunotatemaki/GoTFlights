package testclasses

import android.content.Context
import com.rookia.gotflights.GoTFlightsApplication
import com.rookia.gotflights.data.network.NetworkServiceFactory
import com.rookia.gotflights.data.persistence.PersistenceManager
import com.rookia.gotflights.data.preferences.PreferencesManager
import com.rookia.gotflights.data.repository.Repository
import com.rookia.gotflights.data.resources.ResourcesManager
import com.rookia.gotflights.domain.model.FlightsCache
import com.rookia.gotflights.framework.network.NetworkServiceFactoryImpl
import com.rookia.gotflights.framework.persistence.PersistenceManagerImpl
import com.rookia.gotflights.framework.persistence.databases.AppDatabase
import com.rookia.gotflights.framework.preferences.PreferencesManagerImpl
import com.rookia.gotflights.framework.repository.RepositoryImpl
import com.rookia.gotflights.framework.resources.ResourcesManagerImpl
import com.rookia.gotflights.framework.utils.security.EncryptionImpl
import com.rookia.gotflights.framework.utils.view.ViewUtilsImpl
import com.rookia.gotflights.ui.main.FlightsViewModel
import com.rookia.gotflights.usecases.GetFlightsUseCase
import com.rookia.gotflights.utils.RateLimiter
import com.rookia.gotflights.utils.ViewUtils
import com.rookia.gotflights.utils.security.Encryption
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Suppress("unused")
@Module
open class AppModuleForTest {

    @Singleton
    @Provides
    open fun providesContext(application: GoTFlightsApplication): Context =
        application.applicationContext

    @Provides
    open fun providesMainViewModel(
        getFlightsUseCase: GetFlightsUseCase
    ): FlightsViewModel =
        FlightsViewModel(
            getFlightsUseCase
        )

    @Singleton
    @Provides
    open fun provideResourcesManager(resourcesManagerImpl: ResourcesManagerImpl): ResourcesManager =
        resourcesManagerImpl

    @Singleton
    @Provides
    open fun provideNetworkServiceFactory(networkServiceFactoryImp: NetworkServiceFactoryImpl): NetworkServiceFactory =
        networkServiceFactoryImp

    @Singleton
    @Provides
    open fun provideEncryption(encryptionImpl: EncryptionImpl): Encryption = encryptionImpl

    @Singleton
    @Provides
    open fun providePreferencesManager(preferencesManagerImpl: PreferencesManagerImpl): PreferencesManager =
        preferencesManagerImpl

    @Singleton
    @Provides
    open fun providePersistenceManager(persistenceManagerImpl: PersistenceManagerImpl): PersistenceManager =
        persistenceManagerImpl

    @Singleton
    @Provides
    open fun provideRateLimiter(): RateLimiter = RateLimiter()

    @Singleton
    @Provides
    open fun provideRepository(repositoryImpl: RepositoryImpl): Repository = repositoryImpl

    @Singleton
    @Provides
    open fun provideDb(
        context: Context
    ): AppDatabase = AppDatabase.getInstance(context)

    @Provides
    open fun providesViewUtils(viewUtilsImpl: ViewUtilsImpl): ViewUtils = viewUtilsImpl

    @Provides
    @Singleton
    open fun providesGetProductsUseCase(repository: Repository): GetFlightsUseCase =
        GetFlightsUseCase(repository)

    @Provides
    @Singleton
    open fun providesFlightsCache() = FlightsCache()
}