package com.ekoapp.simplechat;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import com.ekoapp.ekosdk.EkoMessage;
import com.ekoapp.ekosdk.EkoTags;
import com.ekoapp.simplechat.intent.ViewChildMessagesIntent;
import com.ekoapp.simplechat.intent.ViewParentMessagesIntent;

import io.reactivex.Completable;

public class ParentMessageListActivity extends MessageListActivity {

    @Override
    String getChannelId() {
        return ViewParentMessagesIntent.getChannelId(getIntent());
    }

    @Override
    void setTitleName() {

    }

    @Override
    void setSubtitleName() {
        channelRepository.getChannel(getChannelId())
                .observe(this, channel -> toolbar.setSubtitle(String.format("unreadCount: %s messageCount:%s",
                        channel.getUnreadCount(),
                        channel.getMessageCount())));
    }

    @Override
    int getMenu() {
        return R.menu.menu_parent_message_list;
    }

    @Override
    LiveData<PagedList<EkoMessage>> getMessageCollection() {
        return messageRepository.getMessageCollectionByTags(getChannelId(),
                null,
                new EkoTags(includingTags.get()),
                new EkoTags(excludingTags.get()), stackFromEnd.get());
    }

    @Override
    boolean getDefaultStackFromEnd() {
        return true;
    }

    @Override
    boolean getDefaultRevertLayout() {
        return false;
    }

    @Override
    void startReading() {
        channelRepository.membership(getChannelId()).startReading();
    }

    @Override
    void stopReading() {
        channelRepository.membership(getChannelId()).stopReading();
    }

    @Override
    void onClick(EkoMessage message) {
        ViewChildMessagesIntent intent = new ViewChildMessagesIntent(this,
                message.getChannelId(),
                message.getMessageId(),
                message.getData().has("text") ?
                        message.getData().get("text").getAsString() :
                        "image");

        startActivity(intent);
    }

    @Override
    Completable createMessage(String text) {
        return messageRepository.createMessage(getChannelId())
                .text(text)
                .send();
    }
}
