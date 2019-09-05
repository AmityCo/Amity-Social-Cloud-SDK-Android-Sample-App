package com.ekoapp.simplechat;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ekoapp.ekosdk.EkoChannel;
import com.ekoapp.ekosdk.EkoChannelRepository;
import com.ekoapp.ekosdk.EkoClient;
import com.ekoapp.ekosdk.EkoLiveData;
import com.ekoapp.ekosdk.EkoMessage;
import com.ekoapp.ekosdk.EkoMessageRepository;
import com.ekoapp.ekosdk.EkoTags;
import com.ekoapp.ekosdk.EkoUser;
import com.ekoapp.ekosdk.EkoUserRepository;
import com.ekoapp.ekosdk.exception.EkoError;
import com.ekoapp.simplechat.intent.ViewChannelMembershipsIntent;
import com.f2prateek.rx.preferences2.Preference;
import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public abstract class MessageListActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.message_list_recyclerview)
    RecyclerView messageListRecyclerView;

    @BindView(R.id.message_edittext)
    EditText messageEditText;

    @BindView(R.id.message_send_button)
    Button sendButton;

    private LiveData<PagedList<EkoMessage>> messages;

    private MessageListAdapter adapter;

    final EkoChannelRepository channelRepository = EkoClient.newChannelRepository();
    final EkoMessageRepository messageRepository = EkoClient.newMessageRepository();
    final EkoUserRepository userRepository = EkoClient.newUserRepository();

    final Preference<Set<String>> includingTags = SimplePreferences.getIncludingTags();
    final Preference<Set<String>> excludingTags = SimplePreferences.getExcludingTags();

    final Preference<Boolean> stackFromEnd = SimplePreferences.getStackFromEnd(getClass().getName(), getDefaultStackFromEnd());
    final Preference<Boolean> revertLayout = SimplePreferences.getRevertLayout(getClass().getName(), getDefaultRevertLayout());

    private CompositeDisposable disposable = new CompositeDisposable();

    abstract String getChannelId();

    abstract int getMenu();

    abstract LiveData<PagedList<EkoMessage>> getMessageCollection();

    abstract boolean getDefaultStackFromEnd();

    abstract boolean getDefaultRevertLayout();

    abstract void setTitleName();

    abstract void setSubtitleName();

    abstract void startReading();

    abstract void stopReading();

    abstract void onClick(EkoMessage message);

    abstract Completable createMessage(String text);

    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_message_list);

        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        setSupportActionBar(toolbar);

        setTitleName();
        setSubtitleName();

        observeMessageCollection();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startReading();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopReading();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(getMenu(), menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_channel_membership) {
            startActivity(new ViewChannelMembershipsIntent(this, getChannelId()));
            return true;
        } else if (item.getItemId() == R.id.action_leave_channel) {
            channelRepository.leaveChannel(getChannelId())
                    .doOnComplete(this::finish)
                    .subscribe();
            return true;
        } else if (item.getItemId() == R.id.action_with_tags) {
            showDialog(R.string.with_tag, "bnk48,football,concert", Joiner.on(",").join(includingTags.get()), true, (dialog, input) -> {
                Set<String> set = Sets.newConcurrentHashSet();
                for (String tag : String.valueOf(input).split(",")) {
                    if (tag.length() > 0) {
                        set.add(tag);
                    }
                }
                includingTags.set(set);
                observeMessageCollection();
            });
            return true;
        } else if (item.getItemId() == R.id.action_without_tags) {
            showDialog(R.string.with_tag, "bnk48,football,concert", Joiner.on(",").join(excludingTags.get()), true, (dialog, input) -> {
                Set<String> set = Sets.newConcurrentHashSet();
                for (String tag : String.valueOf(input).split(",")) {
                    if (tag.length() > 0) {
                        set.add(tag);
                    }
                }
                excludingTags.set(set);
                observeMessageCollection();
            });
            return true;
        } else if (item.getItemId() == R.id.action_set_tags) {
            EkoLiveData<EkoChannel> liveData = channelRepository.getChannel(getChannelId());
            liveData.observeForever(new Observer<EkoChannel>() {
                @Override
                public void onChanged(EkoChannel channel) {
                    liveData.removeObserver(this);
                    showDialog(R.string.set_tags, "bnk48,football,concert", Joiner.on(",").join(channel.getTags()), true, (dialog, input) -> {
                        Set<String> set = Sets.newConcurrentHashSet();
                        for (String tag : String.valueOf(input).split(",")) {
                            if (tag.length() > 0) {
                                set.add(tag);
                            }
                        }
                        channelRepository.setTags(channel.getChannelId(), new EkoTags(set))
                                .subscribeOn(Schedulers.io())
                                .subscribe();
                    });
                }
            });
            return true;
        } else if (item.getItemId() == R.id.action_notification_for_current_channel) {
            channelRepository.notification(getChannelId())
                    .isAllowed()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess(allowed -> new MaterialDialog.Builder(this)
                            .checkBoxPrompt("allow notification for current channel", allowed, null)
                            .positiveText("save change")
                            .onPositive((dialog, which) -> channelRepository.notification(getChannelId())
                                    .setAllowed(dialog.isPromptCheckBoxChecked())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe())
                            .negativeText("discard")
                            .show())
                    .subscribe();
            return true;
        } else if (item.getItemId() == R.id.action_stack_from_end) {
            new MaterialDialog.Builder(this)
                    .checkBoxPrompt(getString(R.string.stack_from_end), stackFromEnd.get(), null)
                    .positiveText("save change")
                    .onPositive((dialog, which) -> {
                        stackFromEnd.set(dialog.isPromptCheckBoxChecked());
                        observeMessageCollection();
                    })
                    .negativeText("discard")
                    .show();
            return true;
        } else if (item.getItemId() == R.id.action_revert_layout) {
            new MaterialDialog.Builder(this)
                    .checkBoxPrompt(getString(R.string.revert_layout), revertLayout.get(), null)
                    .positiveText("save change")
                    .onPositive((dialog, which) -> {
                        revertLayout.set(dialog.isPromptCheckBoxChecked());
                        observeMessageCollection();
                    })
                    .negativeText("discard")
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onLongClick(EkoMessage message) {
        new MaterialDialog.Builder(this)
                .items("flag a message", "flag a sender", "set tag(s)")
                .itemsCallback((dialog, itemView, position, text) -> {
                    if (position == 0) {
                        flagMessage(message);
                    } else if (position == 1) {
                        flagUser(message.getUser());
                    } else {
                        setTags(message);
                    }
                })
                .show();
    }

    private void flagMessage(EkoMessage message) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
        if (message.isFlaggedByMe()) {
            builder.content("un-flag a message")
                    .positiveText("un-flag")
                    .onPositive((dialog, which) -> disposable.add(messageRepository.report(message.getMessageId())
                            .unflag()
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnComplete(() -> Toast.makeText(this, "successfully un-flagged a message", Toast.LENGTH_SHORT).show())
                            .subscribe()));
        } else {
            builder.content("flag a message")
                    .positiveText("flag")
                    .onPositive((dialog, which) -> disposable.add(messageRepository.report(message.getMessageId())
                            .flag()
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnComplete(() -> Toast.makeText(this, "successfully flagged the a message", Toast.LENGTH_SHORT).show())
                            .subscribe()));
        }
        builder.show();
    }

    private void flagUser(EkoUser user) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
        if (user.isFlaggedByMe()) {
            builder.content("un-flag a sender")
                    .positiveText("un-flag")
                    .onPositive((dialog, which) -> disposable.add(userRepository.report(user.getUserId())
                            .unflag()
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnComplete(() -> Toast.makeText(this, "successfully un-flagged a sender", Toast.LENGTH_SHORT).show())
                            .subscribe()));
        } else {
            builder.content("flag a sender")
                    .positiveText("flag")
                    .onPositive((dialog, which) -> disposable.add(userRepository.report(user.getUserId())
                            .flag()
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnComplete(() -> Toast.makeText(this, "successfully flagged a sender", Toast.LENGTH_SHORT).show())
                            .subscribe()));
        }
        builder.show();
    }

    private void setTags(EkoMessage message) {
        showDialog(R.string.set_tags, "bnk48,football,concert", Joiner.on(",").join(message.getTags()), true, (dialog, input) -> {
            Set<String> set = Sets.newConcurrentHashSet();
            for (String tag : String.valueOf(input).split(",")) {
                if (tag.length() > 0) {
                    set.add(tag);
                }
            }
            messageRepository.setTags(message.getMessageId(), new EkoTags(set))
                    .subscribeOn(Schedulers.io())
                    .subscribe();
        });
    }

    private void observeMessageCollection() {
        if (messages != null) {
            messages.removeObservers(this);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(stackFromEnd.get());
        layoutManager.setReverseLayout(revertLayout.get());
        messageListRecyclerView.setLayoutManager(layoutManager);

        adapter = new MessageListAdapter();
        messageListRecyclerView.setAdapter(adapter);

        disposable.clear();

        disposable.add(adapter.getOnLongClickFlowable()
                .doOnNext(this::onLongClick)
                .subscribe());

        disposable.add(adapter.getOnClickFlowable()
                .doOnNext(this::onClick)
                .subscribe());

        messages = getMessageCollection();
        messages.observe(this, adapter::submitList);
    }

    private void showDialog(@StringRes int title, CharSequence hint, CharSequence prefill, boolean allowEmptyInput, MaterialDialog.InputCallback callback) {
        new MaterialDialog.Builder(this)
                .title(title)
                .inputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS)
                .input(hint, prefill, allowEmptyInput, callback)
                .show();
    }

    @OnTextChanged(R.id.message_edittext)
    void onMessageTextChanged(CharSequence input) {
        String text = String.valueOf(input).trim();
        sendButton.setEnabled(!TextUtils.isEmpty(text));
    }

    @OnClick(R.id.message_send_button)
    void onSendClick() {
        String text = String.valueOf(messageEditText.getText()).trim();
        messageEditText.setText(null);
        createMessage(text)
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

    void scrollToBottom() {
        messageListRecyclerView.postDelayed(() -> {
            int lastPosition = adapter.getItemCount() - 1;
            messageListRecyclerView.scrollToPosition(lastPosition);
        }, 10);
    }
}
