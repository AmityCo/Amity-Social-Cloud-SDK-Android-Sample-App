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
import com.google.gson.JsonObject;

import butterknife.OnClick;
import io.reactivex.Completable;

public class ChildMessageListActivity extends MessageListActivity {

    @Override
    String getChannelId() {
        return ViewChildMessagesIntent.getChannelId(getIntent());
    }

    String getParentId() {
        return ViewChildMessagesIntent.getParentId(getIntent());
    }

    String getData() {
        return ViewChildMessagesIntent.getData(getIntent());
    }

    @Override
    void setTitleName() {

    }

    @Override
    void setSubtitleName() {
        toolbar.setSubtitle(getData());
    }

    @Override
    int getMenu() {
        return R.menu.menu_child_message_list;
    }

    @Override
    LiveData<PagedList<EkoMessage>> getMessageCollection() {
        return messageRepository.getMessageCollectionByTags(getChannelId(),
                getParentId(),
                new EkoTags(includingTags.get()),
                new EkoTags(excludingTags.get()), stackFromEnd.get());
    }

    @Override
    boolean getDefaultStackFromEnd() {
        return false;
    }

    @Override
    boolean getDefaultRevertLayout() {
        return false;
    }

    @Override
    void startReading() {

    }

    @Override
    void stopReading() {

    }

    @Override
    void onClick(EkoMessage message) {

    }

    @Override
    Completable createTextMessage(String text) {
        return messageRepository.createMessage(getChannelId())
                .text(text)
                .parentId(getParentId())
                .build()
                .send();
    }

    @OnClick(R.id.message_image_button)
    void sendImageMessage() {
        startActivityForResult(new OpenImageMessageSenderActivityIntent(this, getChannelId(), getParentId()), IntentRequestCode.REQUEST_SEND_IMAGE_MESSAGE);
    }


    @OnClick(R.id.message_file_button)
    void sendFileMessage() {
        startActivityForResult(new OpenFileMessageSenderActivityIntent(this, getChannelId(), getParentId()), IntentRequestCode.REQUEST_SEND_FILE_MESSAGE);
    }

    @OnClick(R.id.message_custom_button)
    void sendCustomMessage() {
        startActivityForResult(new OpenCustomMessageSenderActivityIntent(this, getChannelId(), getParentId()), IntentRequestCode.REQUEST_SEND_CUSTOM_MESSAGE);
    }

}
