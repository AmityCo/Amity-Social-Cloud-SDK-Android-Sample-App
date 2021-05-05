package com.amity.sample.ascsdk.messagelist.option;

public enum ReactionOption {

    LIKE("like"),

    DISLIKE("dislike"),

    HAPPY("happy"),

    SAD("sad"),

    CRYING("crying"),

    ANGRY("angry"),

    UNICORN("unicorn"),

    STRESS("stress"),

    KISS("kiss"),

    LOL("lol");

    private final String value;

    ReactionOption(final String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

}