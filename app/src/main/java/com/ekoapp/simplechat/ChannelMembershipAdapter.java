package com.ekoapp.simplechat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ekoapp.ekosdk.EkoChannelMembership;
import com.ekoapp.ekosdk.EkoObjects;
import com.ekoapp.ekosdk.adapter.EkoChannelMembershipAdapter;
import com.ekoapp.simplechat.ChannelMembershipAdapter.EkoUserViewHolder;

import butterknife.BindView;

public class ChannelMembershipAdapter extends EkoChannelMembershipAdapter<EkoUserViewHolder> {

    @Override
    public EkoUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_channel_membership, parent, false);
        return new EkoUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EkoUserViewHolder holder, int position) {
        EkoChannelMembership channelMembership = getItem(position);
        if (EkoObjects.isProxy(channelMembership)) {
            holder.textView.setText("loading...");
        } else {
            holder.textView.setText(new StringBuilder()
                    .append("id: ")
                    .append(channelMembership.getUserId())
                    .append("\ndisplay name: ")
                    .append(channelMembership.getUser() != null ? channelMembership.getUser().getDisplayName() : "null")
                    .append("\nmembership: ")
                    .append(channelMembership.getMembership())
                    .toString());
        }
    }


    static class EkoUserViewHolder extends BaseViewHolder {

        @BindView(R.id.channel_membership_textview)
        TextView textView;

        EkoUserViewHolder(View itemView) {
            super(itemView);
        }
    }
}
