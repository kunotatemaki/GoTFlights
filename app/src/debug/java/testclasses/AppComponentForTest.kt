package testclasses

import com.rookia.gotflights.GoTFlightsApplication
import com.rookia.gotflights.di.modules.ActivityBuilder
import com.rookia.gotflights.di.modules.FragmentsProvider
import com.rookia.gotflights.di.modules.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [(AndroidSupportInjectionModule::class), (ActivityBuilder::class), (ViewModelModule::class),
        (AppModuleForTest::class), (FragmentsProvider::class)]
)
interface AppComponentForTest : AndroidInjector<GoTFlightsApplication> {

    override fun inject(currencyConverterApp: GoTFlightsApplication)

    @Component.Builder
    interface Builder {
        fun appModuleForTest(appModuleForTest: AppModuleForTest): Builder

        @BindsInstance
        fun application(application: GoTFlightsApplication): Builder

        fun build(): AppComponentForTest
    }

}