package com.ekoapp.simplechat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.ekoapp.ekosdk.EkoClient;
import com.ekoapp.simplechat.intent.ViewChannelMembershipsIntent;

import butterknife.BindView;

public class ChannelMembershipActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.channel_membership_list_recyclerview)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_membership_list);

        String channelId = ViewChannelMembershipsIntent.getChannelId(getIntent());

        toolbar.setTitle(channelId);
        toolbar.setSubtitle("member");
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        setSupportActionBar(toolbar);

        ChannelMembershipAdapter adapter = new ChannelMembershipAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        EkoClient.newChannelRepository()
                .membership(channelId)
                .getCollection()
                .observe(this, adapter::submitList);
    }
}
