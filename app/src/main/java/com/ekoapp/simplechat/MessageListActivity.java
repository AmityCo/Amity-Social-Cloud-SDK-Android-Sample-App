package com.ekoapp.simplechat;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ekoapp.ekosdk.EkoChannelRepository;
import com.ekoapp.ekosdk.EkoClient;
import com.ekoapp.ekosdk.EkoMessage;
import com.ekoapp.ekosdk.EkoMessageRepository;
import com.ekoapp.ekosdk.EkoUserRepository;
import com.ekoapp.ekosdk.exception.EkoError;
import com.ekoapp.simplechat.intent.ViewMessagesIntent;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class MessageListActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.message_list_recyclerview)
    RecyclerView messageListRecyclerView;

    @BindView(R.id.message_edittext)
    EditText messageEditText;

    @BindView(R.id.message_send_button)
    Button sendButton;

    private final EkoChannelRepository channelRepository = EkoClient.newChannelRepository();
    private final EkoMessageRepository messageRepository = EkoClient.newMessageRepository();
    private final EkoUserRepository userRepository = EkoClient.newUserRepository();
    private final MessageListAdapter adapter = new MessageListAdapter();

    private String channelId;

    private final CompositeDisposable disposable = new CompositeDisposable();


    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_message_list);

        channelId = ViewMessagesIntent.getChannelId(getIntent());

        toolbar.setTitle(channelId);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        setSupportActionBar(toolbar);

        if (channelId != null) {
            disposable.add(adapter.getOnLongClickFlowable()
                    .doOnNext(this::flag)
                    .subscribe());

            messageListRecyclerView.setAdapter(adapter);
            messageRepository.getMessageCollection(channelId)
                    .observe(this, adapter::submitList);

            ItemInsertedDataObserver.create(adapter);

            channelRepository.getChannel(channelId)
                    .observe(this, channel -> toolbar.setSubtitle(String.format("unreadCount: %s messageCount:%s", channel.getUnreadCount(), channel.getMessageCount())));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_message_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_leave_channel) {
            channelRepository.leaveChannel(channelId)
                    .doOnComplete(this::finish)
                    .subscribe();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void flag(EkoMessage message) {
        new MaterialDialog.Builder(this)
                .items("flag a message", "flag a sender")
                .itemsCallback((dialog, itemView, position, text) -> {
                    if (position == 0) {
                        flagMessage(message.getMessageId());
                    } else {
                        flagUser(message.getUserId());
                    }
                })
                .show();
    }

    private void flagMessage(String messageId) {
        new MaterialDialog.Builder(this)
                .content("flag/un-flag a message")
                .positiveText("flag")
                .onPositive((dialog, which) -> disposable.add(messageRepository.flag(messageId)
                        .flag()
                        .subscribe()))
                .negativeText("un-flag")
                .onNegative((dialog, which) -> disposable.add(messageRepository.flag(messageId)
                        .unflag()
                        .subscribe()))
                .show();
    }

    private void flagUser(String userId) {
        new MaterialDialog.Builder(this)
                .content("flag/un-flag a sender")
                .positiveText("flag")
                .onPositive((dialog, which) -> disposable.add(userRepository.flag(userId)
                        .flag()
                        .subscribe()))
                .negativeText("un-flag")
                .onNegative((dialog, which) -> disposable.add(userRepository.flag(userId)
                        .unflag()
                        .subscribe()))
                .show();
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
