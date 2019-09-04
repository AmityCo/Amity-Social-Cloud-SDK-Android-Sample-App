package com.ekoapp.simplechat;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import com.ekoapp.ekosdk.EkoMessage;
import com.ekoapp.ekosdk.EkoTags;
import com.ekoapp.simplechat.intent.ViewChildMessagesIntent;

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
                getChannelId(),
                new EkoTags(includingTags.get()),
                new EkoTags(excludingTags.get()), isStackFromEnd());
    }

    @Override
    boolean isStackFromEnd() {
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
    Completable createMessage(String text) {
        return messageRepository.createMessage(getChannelId())
                .text(text)
                .parentId(getParentId())
                .send();
    }
}
