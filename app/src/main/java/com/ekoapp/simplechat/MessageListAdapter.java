package com.ekoapp.simplechat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ekoapp.ekosdk.EkoMessage;
import com.ekoapp.ekosdk.EkoObjects;
import com.ekoapp.ekosdk.EkoUser;
import com.ekoapp.ekosdk.adapter.EkoMessageAdapter;
import com.ekoapp.ekosdk.messaging.data.TextData;
import com.google.common.base.Joiner;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.Setter;
import butterknife.ViewCollections;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.subjects.PublishSubject;

import static com.ekoapp.simplechat.MessageListAdapter.MessageViewHolder;

public class MessageListAdapter extends EkoMessageAdapter<MessageViewHolder> {

    private final PublishSubject<EkoMessage> onLongClickSubject = PublishSubject.create();
    private final PublishSubject<EkoMessage> onClickSubject = PublishSubject.create();


    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        EkoMessage m = getItem(position);

        Setter<View, Integer> visibility = (view, value, index) -> view.setVisibility(value);

        if (EkoObjects.isProxy(m)) {
            ViewCollections.set(holder.optionalViews, visibility, View.GONE);
            holder.messageIdTextview.setText(String.format("loading adapter position: %s", position));
        } else {
            ViewCollections.set(holder.optionalViews, visibility, View.VISIBLE);
            String type = m.getType();
            EkoUser sender = m.getUser();
            DateTime created = m.getCreatedAt();

            holder.messageIdTextview.setText(String.format("%s: %s %s: %s",
                    m.getChannelSegment(),
                    m.getMessageId(),
                    m.isFlaggedByMe() ? "\uD83C\uDFC1" : "\uD83C\uDFF3️",
                    m.getFlagCount()));

            holder.senderTextview.setText(String.format("%s: %s %s: %s",
                    sender != null ? sender.getDisplayName() : "",
                    sender != null ? sender.getUserId() : "",
                    sender != null && sender.isFlaggedByMe() ? "\uD83C\uDFC1" : "\uD83C\uDFF3️",
                    sender != null ? sender.getFlagCount() : 0));

            holder.commentTextview.setText(String.format("comment count: %s", m.getChildrenNumber()));
            holder.tagsTextview.setText(String.format("tags️: %s", Joiner.on(", ").join(m.getTags())));

            if ("text".equalsIgnoreCase(type)) {
                holder.dataTextview.setText(String.format("text: %s", m.getData(TextData.class).getText()));
            } else {
                holder.dataTextview.setText(String.format("data type: %s", type));
            }

            holder.syncStateTextview.setText(m.getSyncState().name());
            holder.timeTextview.setText(created.toString(DateTimeFormat.longDateTime()));
        }

        holder.itemView.setOnLongClickListener(v -> {
            onLongClickSubject.onNext(m);
            return true;
        });

        holder.itemView.setOnClickListener(v -> {
            onClickSubject.onNext(m);
        });
    }

    Flowable<EkoMessage> getOnLongClickFlowable() {
        return onLongClickSubject.toFlowable(BackpressureStrategy.BUFFER);
    }

    Flowable<EkoMessage> getOnClickFlowable() {
        return onClickSubject.toFlowable(BackpressureStrategy.BUFFER);
    }


    static class MessageViewHolder extends BaseViewHolder {

        @BindViews({
                R.id.sender_textview,
                R.id.data_textview,
                R.id.sync_state_textview,
                R.id.time_textview
        })
        List<View> optionalViews;

        @BindView(R.id.message_textview)
        TextView messageIdTextview;
        @BindView(R.id.sender_textview)
        TextView senderTextview;
        @BindView(R.id.data_textview)
        TextView dataTextview;
        @BindView(R.id.comment_count_textview)
        TextView commentTextview;
        @BindView(R.id.tags_textview)
        TextView tagsTextview;
        @BindView(R.id.sync_state_textview)
        TextView syncStateTextview;
        @BindView(R.id.time_textview)
        TextView timeTextview;


        MessageViewHolder(View itemView) {
            super(itemView);
        }
    }
}
