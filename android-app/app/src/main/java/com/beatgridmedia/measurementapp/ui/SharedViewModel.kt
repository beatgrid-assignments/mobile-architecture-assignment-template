package com.beatgridmedia.measurementapp.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.beatgridmedia.measurementkit.viewmodel.common.MeasurementKitViewModel

/**
 * Holds a shared (KMP) view model in an androidx [ViewModel] so it survives configuration changes and is cleared with
 * the owning screen.
 */
class SharedViewModelHolder<T : MeasurementKitViewModel>(val viewModel: T) : ViewModel() {
    override fun onCleared() {
        viewModel.onCleared()
    }
}

@Composable
inline fun <reified T : MeasurementKitViewModel> sharedViewModel(crossinline factory: () -> T): T {
    return viewModel(key = T::class.qualifiedName) { SharedViewModelHolder(factory()) }.viewModel
}
