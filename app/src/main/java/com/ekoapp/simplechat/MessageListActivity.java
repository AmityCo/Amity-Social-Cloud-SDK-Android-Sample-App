package com.ekoapp.simplechat;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ekoapp.ekosdk.EkoChannelRepository;
import com.ekoapp.ekosdk.EkoClient;
import com.ekoapp.ekosdk.EkoMessageRepository;
import com.ekoapp.ekosdk.exception.EkoError;
import com.ekoapp.simplechat.intent.ViewMessagesIntent;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class MessageListActivity extends BaseActivity {

    @BindView(R.id.message_list_recyclerview)
    RecyclerView messageListRecyclerView;

    @BindView(R.id.message_edittext)
    EditText messageEditText;

    @BindView(R.id.message_send_button)
    Button sendButton;

    private final EkoChannelRepository channelRepository = EkoClient.newChannelRepository();
    private final EkoMessageRepository messageRepository = EkoClient.newMessageRepository();
    private final MessageListAdapter adapter = new MessageListAdapter();

    private String channelId;

    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_message_list);

        channelId = ViewMessagesIntent.getChannelId(getIntent());
        setTitle(channelId);

        if (channelId != null) {
            messageListRecyclerView.setAdapter(adapter);
            messageRepository.getMessageCollection(channelId)
                    .observe(this, adapter::submitList);

            ItemInsertedDataObserver.create(adapter);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (channelId != null) {
            channelRepository.membership(channelId)
                    .startReading();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (channelId != null) {
            channelRepository.membership(channelId)
                    .stopReading();
        }
    }

    @OnTextChanged(R.id.message_edittext)
    void onMessageTextChanged(CharSequence input) {
        String text = String.valueOf(input).trim();
        sendButton.setEnabled(!TextUtils.isEmpty(text));
    }

    @OnClick(R.id.message_send_button)
    void onSendClick() {
        final String channelId = ViewMessagesIntent.getChannelId(getIntent());
        if (channelId != null) {
            String text = String.valueOf(messageEditText.getText()).trim();
            messageEditText.setText(null);

            messageRepository.createMessage(channelId)
                    .text(text)
                    .send()
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(t -> {
                        EkoError ekoError = EkoError.from(t);
                        if (ekoError.is(EkoError.USER_IS_BANNED)) {
                            Activity activity = MessageListActivity.this;
                            messageEditText.post(Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT)::show);
                            messageEditText.postDelayed(this::finish, 500);
                        }
                    })
                    .doOnComplete(this::scrollToBottom)
                    .subscribe();
        }
    }

    void scrollToBottomIfAtBottom() {
        scrollToBottom();
    }

    void scrollToBottom() {
        messageListRecyclerView.postDelayed(() -> {
            int lastPosition = adapter.getItemCount() - 1;
            messageListRecyclerView.scrollToPosition(lastPosition);
        }, 10);
    }


    private static class ItemInsertedDataObserver extends RecyclerView.AdapterDataObserver {

        final RecyclerView.Adapter adapter;


        private ItemInsertedDataObserver(@NonNull RecyclerView.Adapter adapter) {
            this.adapter = adapter;
            adapter.registerAdapterDataObserver(this);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            Timber.e("ui: onItemRangeInserted: positionStart: %s itemCount: %s lastInsertPos: %s",
                    positionStart, itemCount, positionStart + itemCount);
            Timber.e("ui: -->: adapter.getItemCount(): %s",
                    adapter.getItemCount());
        }


        public static ItemInsertedDataObserver create(@NonNull RecyclerView.Adapter adapter) {
            return new ItemInsertedDataObserver(adapter);
        }
    }
}
