package com.amity.sample.ascsdk.post.option


enum class PostOption(val value: String) {

    VIEW_POST("view post"),

    FLAG_POST("flag a post"),

    EDIT("edit"),

    DELETE("delete"),

    ADD_REACTION("add reaction"),

    REMOVE_REACTION("remove reaction"),

    REACTION_HISTORY("reaction history"),

    APPROVE_POST("approve post"),

    DECLINE_POST("decline post");


    companion object {
        fun enumOf(value: String): PostOption? = PostOption.values().find { it.value == value }
    }

}