package com.ekoapp.simplechat.chatkit;

import android.support.annotation.NonNull;

import com.ekoapp.ekosdk.EkoMessage;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.Date;

public class ChatKitMessage implements IMessage {

    public static final IMessage PROXY = new IMessage() {
        @Override
        public String getId() {
            return "proxy_imessage";
        }

        @Override
        public String getText() {
            return "Proxy Text";
        }

        @Override
        public IUser getUser() {
            return null;
        }

        @Override
        public Date getCreatedAt() {
            return new Date();
        }
    };


    @NonNull
    private final EkoMessage message;


    private ChatKitMessage(@NonNull EkoMessage message) {
        this.message = message;
    }

    @Override
    public String getId() {
        return message.getMessageId();
    }

    @Override
    public String getText() {
        return String.valueOf(message.getData());
    }

    @Override
    public IUser getUser() {
        return null;
    }

    @Override
    public Date getCreatedAt() {
        return new Date(message.getCreatedAt().getMillis());
    }

    public static ChatKitMessage from(@NonNull EkoMessage message) {
        return new ChatKitMessage(message);
    }
}
