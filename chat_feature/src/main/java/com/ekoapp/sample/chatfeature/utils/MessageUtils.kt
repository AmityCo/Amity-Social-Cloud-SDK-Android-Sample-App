package com.ekoapp.sample.chatfeature.utils

import com.ekoapp.ekosdk.EkoMessage
import com.ekoapp.sample.core.rx.into
import io.reactivex.disposables.CompositeDisposable

fun EkoMessage.toggleReaction(selected: String) {
    if (reactions.isNotEmpty()) {
        reactions.forEach {
            if (it.key == selected) {
                react().removeReaction(selected).subscribe() into CompositeDisposable()
            } else {
                react().addReaction(selected).subscribe() into CompositeDisposable()
            }
        }
    } else {
        react().addReaction(selected).subscribe() into CompositeDisposable()
    }
}