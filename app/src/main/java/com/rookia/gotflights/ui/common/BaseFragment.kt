package com.rookia.gotflights.ui.common

import com.rookia.gotflights.databinding.BindingComponent
import com.rookia.gotflights.data.resources.ResourcesManager
import com.rookia.gotflights.utils.ViewUtils
import dagger.android.support.DaggerFragment
import javax.inject.Inject


abstract class BaseFragment : DaggerFragment() {

    @Inject
    protected lateinit var viewModelFactory: ViewModelFactory

    @Inject
    protected lateinit var bindingComponent: BindingComponent

    @Inject
    protected lateinit var resourcesManager: ResourcesManager

    @Inject
    protected lateinit var viewUtils: ViewUtils

}