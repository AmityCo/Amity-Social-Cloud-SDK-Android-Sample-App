package com.ekoapp.sample.chatfeature.messagelist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.ekoapp.ekosdk.EkoMessage;
import com.ekoapp.ekosdk.EkoObjects;
import com.ekoapp.ekosdk.EkoUser;
import com.ekoapp.ekosdk.adapter.EkoMessageAdapter;
import com.ekoapp.ekosdk.messaging.data.DataType;
import com.ekoapp.ekosdk.messaging.data.FileData;
import com.ekoapp.ekosdk.messaging.data.ImageData;
import com.ekoapp.ekosdk.messaging.data.TextData;
import com.ekoapp.sample.chatfeature.BaseViewHolder;
import com.ekoapp.sample.chatfeature.R;
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

public class MessageListAdapter extends EkoMessageAdapter<MessageListAdapter.MessageViewHolder> {

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
            renderLoadingItem(holder, visibility, position);
        } else if (m.isDeleted()) {
           renderDeletedMessage(holder, visibility);
        } else {
            ViewCollections.set(holder.optionalViews, visibility, View.VISIBLE);
            String type = m.getType();
            EkoUser sender = m.getUser();
            DateTime created = m.getCreatedAt();

            holder.messageIdTextview.setText(String.format("mid: %s %s:%s\nsegment: %s",
                    m.getMessageId(),
                    m.isFlaggedByMe() ? "\uD83C\uDFC1" : "\uD83C\uDFF3️",
                    m.getFlagCount(),
                    m.getChannelSegment()));

            holder.senderTextview.setText(String.format("uid: %s %s: %s\ndisplay name: %s",
                    sender != null ? sender.getUserId() : "",
                    sender != null && sender.isFlaggedByMe() ? "\uD83C\uDFC1" : "\uD83C\uDFF3️",
                    sender != null ? sender.getFlagCount() : 0,
                    sender != null ? sender.getDisplayName() : ""));

            holder.commentTextview.setText(String.format("comment count: %s", m.getChildrenNumber()));
            holder.tagsTextview.setText(String.format("tags️: %s", Joiner.on(", ").join(m.getTags())));

            if (DataType.TEXT == DataType.from(m.getType())) {
                holder.dataTextview.setText(String.format("text: %s", m.getData(TextData.class).getText()));
                Glide.with(holder.dataImageview.getContext()).clear(holder.dataImageview);

            } else if (DataType.IMAGE == DataType.from(m.getType())) {
                String url = m.getData(ImageData.class).getUrl();
                holder.dataTextview.setText(String.format("data type: %s,\nurl: %s,\ndata: ", type, url).concat(m.getData().toString()));
                Glide.with(holder.dataImageview.getContext()).load(url).into(holder.dataImageview);

            } else if (DataType.FILE == DataType.from(m.getType())) {
                String url = m.getData(FileData.class).getUrl();
                holder.dataTextview.setText(String.format("data type: %s,\nurl: %s,\ndata: ", type, url).concat(m.getData().toString()));
                Glide.with(holder.dataImageview.getContext()).clear(holder.dataImageview);
            } else {
                holder.dataTextview.setText(String.format("data type: %s,\ndata: ", type).concat(m.getData().toString()));
                Glide.with(holder.dataImageview.getContext()).clear(holder.dataImageview);
            }

            renderReaction(holder, m);

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

    private void renderLoadingItem(@NonNull MessageViewHolder holder, Setter<View, Integer> visibility, int position) {
        ViewCollections.set(holder.optionalViews, visibility, View.GONE);
        holder.messageIdTextview.setText(String.format("loading adapter position: %s", position));
        Glide.with(holder.dataImageview.getContext()).clear(holder.dataImageview);
    }

    private void renderDeletedMessage(@NonNull MessageViewHolder holder, Setter<View, Integer> visibility) {
        ViewCollections.set(holder.optionalViews, visibility, View.GONE);
        holder.messageIdTextview.setText(String.format("Deleted"));
        holder.dataTextview.setText("");
        Glide.with(holder.dataImageview.getContext()).clear(holder.dataImageview);
    }

    private void renderReaction(@NonNull MessageViewHolder holder, @NonNull EkoMessage m) {
        String reactions = "";
        for(String key: m.getReactions().keySet()) {
            int reactionCount = m.getReactions().getCount(key);
            reactions += String.format("\n%s : %s", key, reactionCount);
        }

        List<String> myReactions = m.getMyReactions();
        String myReactionsString = Joiner.on(" ").join(myReactions);

        holder.reactionTextview.setText(String.format("reaction count: %s %s \nmy reactions: %s",
                m.getReactionCount(),
                reactions,
                myReactionsString));
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
        @BindView(R.id.data_imageview)
        ImageView dataImageview;
        @BindView(R.id.reaction_textview)
        TextView reactionTextview;
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
