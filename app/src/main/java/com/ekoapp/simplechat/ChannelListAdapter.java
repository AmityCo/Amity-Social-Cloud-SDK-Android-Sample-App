package com.ekoapp.simplechat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ekoapp.ekosdk.EkoChannel;
import com.ekoapp.ekosdk.EkoObjects;
import com.ekoapp.ekosdk.adapter.EkoChannelAdapter;
import com.ekoapp.simplechat.intent.ViewParentMessagesIntent;
import com.google.common.base.Joiner;

import butterknife.BindView;

import static com.ekoapp.simplechat.ChannelListAdapter.ChannelViewHolder;

public class ChannelListAdapter extends EkoChannelAdapter<ChannelViewHolder> {

    @NonNull
    @Override
    public ChannelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_channel, parent, false);
        return new ChannelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChannelViewHolder holder, int position) {
        EkoChannel channel = getItem(position);

        if (EkoObjects.isProxy(channel)) {
            holder.channelId = null;
            holder.channelIdTextView.setText("loading...");
        } else {
            String text = new StringBuilder()
                    .append("id: ")
                    .append(channel.getChannelId())
                    .append("\nname: ")
                    .append(channel.getDisplayName())
                    .append("\nmember count: ")
                    .append(channel.getMemberCount())
                    .append("\nunread count: ")
                    .append(channel.getUnreadCount())
                    .append("\nmessage count: ")
                    .append(channel.getMessageCount())
                    .append("\ntags: ")
                    .append(Joiner.on(", ").join(channel.getTags()))
                    .append("\ntype: ")
                    .append(channel.getChannelType())
                    .toString();

            holder.channelId = channel.getChannelId();
            holder.channelIdTextView.setText(text);
        }
    }


    static class ChannelViewHolder extends BaseViewHolder {

        String channelId;

        @BindView(R.id.channel_textview)
        TextView channelIdTextView;


        ChannelViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(view -> {
                Context context = view.getContext();
                ViewParentMessagesIntent intent = new ViewParentMessagesIntent(context, channelId);
                context.startActivity(intent);
            });
        }
    }
}
