package com.ekoapp.simplechat.channellist.option;

public enum ChannelTypeOption {

    STANDARD("standard");

    private final String value;

    ChannelTypeOption(final String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

}