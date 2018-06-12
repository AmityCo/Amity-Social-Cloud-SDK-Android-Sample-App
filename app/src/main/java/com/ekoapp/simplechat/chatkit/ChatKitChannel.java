package com.ekoapp.simplechat.chatkit;

import android.support.annotation.NonNull;

import com.ekoapp.ekosdk.EkoChannel;
import com.stfalcon.chatkit.commons.models.IDialog;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.Collections;
import java.util.List;

public class ChatKitChannel implements IDialog<IMessage> {

    @NonNull
    private final EkoChannel channel;


    private ChatKitChannel(@NonNull EkoChannel channel) {
        this.channel = channel;
    }

    @Override
    public String getId() {
        return channel.getChannelId();
    }

    @Override
    public String getDialogPhoto() {
        return null;
    }

    @Override
    public String getDialogName() {
        return channel.getChannelId();
    }

    @Override
    public List<? extends IUser> getUsers() {
        return Collections.emptyList();
    }

    @Override
    public IMessage getLastMessage() {
        return ChatKitMessage.PROXY;
    }

    @Override
    public void setLastMessage(IMessage message) {
    }

    @Override
    public int getUnreadCount() {
        return channel.getMessageCount();
    }

    public EkoChannel getChannel() {
        return channel;
    }


    public static ChatKitChannel from(@NonNull EkoChannel channel) {
        return new ChatKitChannel(channel);
    }
}
