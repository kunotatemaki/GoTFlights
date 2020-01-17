@file:Suppress("unused")

package com.rookia.gotflights.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import java.lang.ref.WeakReference
import java.text.Normalizer

fun String.normalizedString(): String {
    val normalized: String = Normalizer.normalize(this, Normalizer.Form.NFD)
    return normalized.replace("[^\\p{ASCII}]".toRegex(), "").trim { it <= ' ' }.toLowerCase()
}

fun <T> WeakReference<T>.safe(body : T.() -> Unit) {
    this.get()?.body()
}

class NonNullMediatorLiveData<T> : MediatorLiveData<T>()

fun <T> LiveData<T>.nonNull(): NonNullMediatorLiveData<T> {
    val mediator: NonNullMediatorLiveData<T> = NonNullMediatorLiveData()
    mediator.addSource(this) { it?.let { mediator.value = it } }
    return mediator
}
