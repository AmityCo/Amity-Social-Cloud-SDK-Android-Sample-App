package com.ekoapp.simplechat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ekoapp.ekosdk.EkoChannel;
import com.ekoapp.ekosdk.EkoObjects;
import com.ekoapp.ekosdk.adapter.EkoChannelAdapter;
import com.ekoapp.simplechat.intent.ViewMessagesIntent;

import butterknife.BindView;

import static com.ekoapp.simplechat.ChannelListAdapter.ChannelViewHolder;

public class ChannelListAdapter extends EkoChannelAdapter<ChannelViewHolder> {

    @NonNull
    @Override
    public ChannelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_channel, parent, false);
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
                    .toString();

            holder.channelId = channel.getChannelId();
            holder.channelIdTextView.setText(text);
        }
    }


    static class ChannelViewHolder extends BaseViewHolder {

        String channelId;

        @BindView(R.id.channel_id_textview)
        TextView channelIdTextView;


        ChannelViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(view -> {
                Context context = view.getContext();
                ViewMessagesIntent intent = new ViewMessagesIntent(context, channelId);
                context.startActivity(intent);
            });
        }
    }
}
