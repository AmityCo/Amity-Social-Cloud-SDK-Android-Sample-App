package com.ekoapp.simplechat.messagelist.option;

public enum MessageOption {

    FLAG_MESSAGE("flag a message"),

    FLAG_SENDER("flag a sender"),

    SET_TAG("set tag(s)"),

    EDIT("edit"),

    DELETE("delete"),

    OPEN_FILE("open file"),

    ADD_REACTION("add reaction"),

    REMOVE_REACTION("remove reaction");

    private final String value;

    MessageOption(final String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

}