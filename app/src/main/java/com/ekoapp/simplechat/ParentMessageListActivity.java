package com.ekoapp.simplechat;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import com.ekoapp.ekosdk.EkoMessage;
import com.ekoapp.ekosdk.EkoTags;
import com.ekoapp.simplechat.intent.IntentRequestCode;
import com.ekoapp.simplechat.intent.OpenCustomMessageSenderActivityIntent;
import com.ekoapp.simplechat.intent.OpenFileMessageSenderActivityIntent;
import com.ekoapp.simplechat.intent.OpenImageMessageSenderActivityIntent;
import com.ekoapp.simplechat.intent.ViewChildMessagesIntent;
import com.ekoapp.simplechat.intent.ViewParentMessagesIntent;

import butterknife.OnClick;
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
                new EkoTags(includingTags),
                new EkoTags(excludingTags), stackFromEnd.get());
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
        if(message.isDeleted()) {
            return;
        }

        ViewChildMessagesIntent intent = new ViewChildMessagesIntent(this,
                message.getChannelId(),
                message.getMessageId(),
                message.getData().has("text") ?
                        message.getData().get("text").getAsString() :
                        "image");

        startActivity(intent);
    }

    @Override
    Completable createTextMessage(String text) {
        return messageRepository.createMessage(getChannelId())
                .text(text)
                .build()
                .send();
    }

    @OnClick(R.id.message_image_button)
    void sendImageMessage() {
        startActivityForResult(new OpenImageMessageSenderActivityIntent(this, getChannelId()), IntentRequestCode.REQUEST_SEND_IMAGE_MESSAGE);
    }


    @OnClick(R.id.message_file_button)
    void sendFileMessage() {
        startActivityForResult(new OpenFileMessageSenderActivityIntent(this, getChannelId()), IntentRequestCode.REQUEST_SEND_FILE_MESSAGE);
    }

    @OnClick(R.id.message_custom_button)
    void sendCustomMessage() {
        startActivityForResult(new OpenCustomMessageSenderActivityIntent(this, getChannelId()), IntentRequestCode.REQUEST_SEND_CUSTOM_MESSAGE);
    }

}
