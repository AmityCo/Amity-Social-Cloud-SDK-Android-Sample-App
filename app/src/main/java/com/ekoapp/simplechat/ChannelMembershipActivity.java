package com.ekoapp.simplechat;

import android.arch.lifecycle.LiveData;
import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.ekoapp.ekosdk.EkoChannelMembership;
import com.ekoapp.ekosdk.EkoChannelMembershipFilter;
import com.ekoapp.ekosdk.EkoClient;
import com.ekoapp.simplechat.intent.ViewChannelMembershipsIntent;

import butterknife.BindView;

public class ChannelMembershipActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.channel_membership_list_recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.filter_spinner)
    Spinner spinner;

    private String channelId;

    private ChannelMembershipAdapter adapter;

    private LiveData<PagedList<EkoChannelMembership>> channelMemberships;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_membership_list);

        channelId = ViewChannelMembershipsIntent.getChannelId(getIntent());

        toolbar.setTitle(channelId);
        toolbar.setSubtitle("member");
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        setSupportActionBar(toolbar);

        adapter = new ChannelMembershipAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        String[] filters = new String[]{
                EkoChannelMembershipFilter.ALL.getApiKey(),
                EkoChannelMembershipFilter.MEMBER.getApiKey(),
                EkoChannelMembershipFilter.BANNED.getApiKey(),
        };

        spinner.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                filters));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                observeChannelMembershipCollection(EkoChannelMembershipFilter.fromApiKey(filters[position]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void observeChannelMembershipCollection(EkoChannelMembershipFilter filter) {
        if (channelMemberships != null) {
            channelMemberships.removeObservers(this);
        }

        channelMemberships = EkoClient.newChannelRepository()
                .membership(channelId)
                .getCollection(filter);

        channelMemberships.observe(this, adapter::submitList);
    }
}
