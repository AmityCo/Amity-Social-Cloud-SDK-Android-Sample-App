package com.ekoapp.sample.socialfeature.reactions.data

import android.os.Parcelable
import com.ekoapp.sample.socialfeature.enums.ReactionTypes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserReactionData(val postId: String, val reactionTypes: ReactionTypes = ReactionTypes.LIKE) : Parcelable