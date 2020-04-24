package com.ekoapp.sample.chatfeature.channellist;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ekoapp.ekosdk.EkoClient;
import com.ekoapp.sample.BaseActivity;
import com.ekoapp.sample.chatfeature.R;
import com.ekoapp.sample.chatfeature.intent.ViewChannelMembershipsIntent;

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
