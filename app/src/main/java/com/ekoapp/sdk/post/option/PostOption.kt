package com.ekoapp.sdk.post.option


enum class PostOption(val value: String) {

    VIEW_POST("view post"),

    FLAG_POST("flag a post"),

    EDIT("edit"),

    DELETE("delete"),

    ADD_REACTION("add reaction"),

    REMOVE_REACTION("remove reaction"),

    REACTION_HISTORY("reaction history");

    companion object {
        fun enumOf(value: String): PostOption? = PostOption.values().find { it.value == value }
    }

}