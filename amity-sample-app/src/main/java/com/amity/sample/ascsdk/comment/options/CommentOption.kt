package com.amity.sample.ascsdk.comment.options


enum class CommentOption(val value: String) {

    EDIT("edit"),

    DELETE("delete"),

    FLAG("flag"),

    ADD_REACTION("add_react"),

    REMOVE_REACTION("remove_react"),

    ALL_REACTION("all_react"),;





    companion object {
        fun enumOf(value: String): CommentOption? = values().find { it.value == value }
    }

}