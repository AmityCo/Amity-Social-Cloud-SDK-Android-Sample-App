package com.ekoapp.simplechat.chatkit;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ekoapp.ekosdk.EkoChannelFilter;
import com.ekoapp.ekosdk.EkoChannelRepository;
import com.ekoapp.ekosdk.EkoClient;
import com.ekoapp.simplechat.BaseActivity;
import com.ekoapp.simplechat.R;
import com.ekoapp.simplechat.SimpleConfig;
import com.google.common.collect.Lists;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import java.util.List;

import butterknife.BindView;

public class ChatKitChannelListActivity extends BaseActivity {

    @BindView(R.id.chatkit_dialogslist)
    DialogsList dialogsList;

    final EkoChannelRepository channelRepository = EkoClient.newChannelRepository();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatkit_channel_list);
        EkoClient.registerDevice(SimpleConfig.DEFAULT_USER_ID, "ChatKit");

        DialogsListAdapter<ChatKitChannel> adapter = new DialogsListAdapter<>(null);
        channelRepository.getChannelCollection(EkoChannelFilter.MEMBER)
                .observe(this, channels -> {
                    if (channels != null) {
                        List<ChatKitChannel> ckc = Lists.transform(channels, ChatKitChannel::from);
                        adapter.setItems(ckc);
                    }
                });
        dialogsList.setAdapter(adapter);
    }
}
