package com.ekoapp.sample.chatfeature.messagelist;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import com.ekoapp.ekosdk.EkoMessage;
import com.ekoapp.ekosdk.EkoTags;
import com.ekoapp.sample.chatfeature.R;
import com.ekoapp.sample.intent.IntentRequestCode;
import com.ekoapp.sample.chatfeature.intent.OpenCustomMessageSenderIntent;
import com.ekoapp.sample.chatfeature.intent.OpenFileMessageSenderIntent;
import com.ekoapp.sample.chatfeature.intent.OpenImageMessageSenderIntent;
import com.ekoapp.sample.chatfeature.intent.ViewChildMessagesIntent;
import com.ekoapp.sample.chatfeature.intent.ViewParentMessagesIntent;

import butterknife.OnClick;
import io.reactivex.Completable;

public class ParentMessageListActivity extends MessageListActivity {

    @Override
    protected String getChannelId() {
        return ViewParentMessagesIntent.getChannelId(getIntent());
    }

    @Override
    protected void setTitleName() {

    }

    @Override
    protected void setSubtitleName() {
        getChannelRepository().getChannel(getChannelId())
                .observe(this, channel -> toolbar.setSubtitle(String.format("unreadCount: %s messageCount:%s",
                        channel.getUnreadCount(),
                        channel.getMessageCount())));
    }

    @Override
    protected int getMenu() {
        return R.menu.menu_parent_message_list;
    }

    @Override
    protected LiveData<PagedList<EkoMessage>> getMessageCollection() {
        return getMessageRepository().getMessageCollectionByTags(getChannelId(),
                null,
                new EkoTags(getIncludingTags()),
                new EkoTags(getExcludingTags()), getStackFromEnd().get());
    }

    @Override
    protected boolean getDefaultStackFromEnd() {
        return true;
    }

    @Override
    protected boolean getDefaultRevertLayout() {
        return false;
    }

    @Override
    protected void startReading() {
        getChannelRepository().membership(getChannelId()).startReading();
    }

    @Override
    protected void stopReading() {
        getChannelRepository().membership(getChannelId()).stopReading();
    }

    @Override
    protected void onClick(EkoMessage message) {
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
    protected Completable createTextMessage(String text) {
        return getMessageRepository().createMessage(getChannelId())
                .text(text)
                .build()
                .send();
    }

    @OnClick(R.id.message_image_button)
    void sendImageMessage() {
        startActivityForResult(new OpenImageMessageSenderIntent(this, getChannelId()), IntentRequestCode.REQUEST_SEND_IMAGE_MESSAGE);
    }


    @OnClick(R.id.message_file_button)
    void sendFileMessage() {
        startActivityForResult(new OpenFileMessageSenderIntent(this, getChannelId()), IntentRequestCode.REQUEST_SEND_FILE_MESSAGE);
    }

    @OnClick(R.id.message_custom_button)
    void sendCustomMessage() {
        startActivityForResult(new OpenCustomMessageSenderIntent(this, getChannelId()), IntentRequestCode.REQUEST_SEND_CUSTOM_MESSAGE);
    }

}
