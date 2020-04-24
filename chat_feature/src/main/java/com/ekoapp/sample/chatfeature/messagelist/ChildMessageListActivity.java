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

import butterknife.OnClick;
import io.reactivex.Completable;

public class ChildMessageListActivity extends MessageListActivity {

    @Override
    protected String getChannelId() {
        return ViewChildMessagesIntent.getChannelId(getIntent());
    }

    String getParentId() {
        return ViewChildMessagesIntent.getParentId(getIntent());
    }

    String getData() {
        return ViewChildMessagesIntent.getData(getIntent());
    }

    @Override
    protected void setTitleName() {

    }

    @Override
    protected void setSubtitleName() {
        toolbar.setSubtitle(getData());
    }

    @Override
    protected int getMenu() {
        return R.menu.menu_child_message_list;
    }

    @Override
    protected LiveData<PagedList<EkoMessage>> getMessageCollection() {

        return getMessageRepository().getMessageCollectionByTags(getChannelId(),
                getParentId(),
                new EkoTags(getIncludingTags()),
                new EkoTags(getExcludingTags()), getStackFromEnd().get());
    }

    @Override
    protected boolean getDefaultStackFromEnd() {
        return false;
    }

    @Override
    protected boolean getDefaultRevertLayout() {
        return false;
    }

    @Override
    protected void startReading() {

    }

    @Override
    protected void stopReading() {

    }

    @Override
    protected void onClick(EkoMessage message) {

    }

    @Override
    protected Completable createTextMessage(String text) {
        return getMessageRepository().createMessage(getChannelId())
                .text(text)
                .parentId(getParentId())
                .build()
                .send();
    }

    @OnClick(R.id.message_image_button)
    void sendImageMessage() {
        startActivityForResult(new OpenImageMessageSenderIntent(this, getChannelId(), getParentId()), IntentRequestCode.REQUEST_SEND_IMAGE_MESSAGE);
    }

    @OnClick(R.id.message_file_button)
    void sendFileMessage() {
        startActivityForResult(new OpenFileMessageSenderIntent(this, getChannelId(), getParentId()), IntentRequestCode.REQUEST_SEND_FILE_MESSAGE);
    }

    @OnClick(R.id.message_custom_button)
    void sendCustomMessage() {
        startActivityForResult(new OpenCustomMessageSenderIntent(this, getChannelId(), getParentId()), IntentRequestCode.REQUEST_SEND_CUSTOM_MESSAGE);
    }

}
