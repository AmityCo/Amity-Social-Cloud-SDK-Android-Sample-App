package com.ekoapp.sample.entry

import android.content.Context
import com.ekoapp.sample.core.base.viewmodel.DisposableViewModel
import com.ekoapp.sample.core.preferences.PreferenceHelper.defaultPreference
import com.ekoapp.sample.core.preferences.PreferenceHelper.displayName
import com.ekoapp.sample.core.preferences.PreferenceHelper.registerDevice
import com.ekoapp.sample.core.ui.extensions.SingleLiveData
import com.ekoapp.sample.register.RegisterRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

sealed class EntryNavigation {
    object RegisterPage : EntryNavigation()
    object MainPage : EntryNavigation()
}

class EntryViewModel @Inject constructor(val context: Context, private val registerRepository: RegisterRepository) : DisposableViewModel() {
    private val defaultPrefs = defaultPreference(context)
    private val entryActionRelay = SingleLiveData<EntryNavigation>()

    fun observeEntryAction(): SingleLiveData<EntryNavigation> = entryActionRelay

    init {
        initEntryNavigation()
    }

    private fun initEntryNavigation() {
        if (defaultPrefs.registerDevice) {
            /** EkoClient SDK required call register again **/
            registerRepository.registerEkoClient(defaultPrefs.displayName.toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete {
                        entryActionRelay.postValue(EntryNavigation.MainPage)
                    }
                    .doOnError {
                        entryActionRelay.postValue(EntryNavigation.RegisterPage)
                    }
                    .subscribe()
        } else entryActionRelay.postValue(EntryNavigation.RegisterPage)
    }
}