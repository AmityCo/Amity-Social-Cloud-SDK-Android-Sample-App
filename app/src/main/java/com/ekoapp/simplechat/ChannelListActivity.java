package com.ekoapp.simplechat;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.LiveDataReactiveStreams;
import android.arch.paging.PagedList;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ekoapp.ekosdk.EkoChannel;
import com.ekoapp.ekosdk.EkoChannelFilter;
import com.ekoapp.ekosdk.EkoChannelRepository;
import com.ekoapp.ekosdk.EkoClient;
import com.ekoapp.ekosdk.EkoTags;
import com.ekoapp.ekosdk.sdk.BuildConfig;
import com.ekoapp.simplechat.chatkit.ChatKitChannelListActivity;
import com.f2prateek.rx.preferences2.Preference;
import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Sets;
import com.google.firebase.iid.FirebaseInstanceId;

import org.bson.types.ObjectId;

import java.util.Set;

import butterknife.BindView;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ChannelListActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.total_unread_textview)
    TextView totalUnreadTextView;

    @BindView(R.id.filter_spinner)
    Spinner spinner;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.channel_list_recyclerview)
    RecyclerView channelListRecyclerView;

    private LiveData<PagedList<EkoChannel>> channels;

    private final EkoChannelRepository channelRepository = EkoClient.newChannelRepository();

    private ChannelListAdapter adapter;

    private EkoChannelFilter filter;

    private Preference<Set<String>> includingTags = SimplePreferences.getIncludingTags();
    private Preference<Set<String>> excludingTags = SimplePreferences.getExcludingTags();


    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_channel_list);
        final String appName = getString(R.string.app_name);
        toolbar.setTitle(String.format("%s %s: %s", appName, "Eko SDK", BuildConfig.VERSION_NAME));
        toolbar.setSubtitle(String.format("%s", BuildConfig.EKO_HTTP_URL));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        setSupportActionBar(toolbar);

        String userId = MoreObjects.firstNonNull(EkoClient.getUserId(), SimpleConfig.DEFAULT_USER_ID);
        String displayName = MoreObjects.firstNonNull(EkoClient.getDisplayName(), "Android " + ObjectId.get());

        register(userId, displayName)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(this::setupChannelList)
                .subscribe();

        fab.setOnClickListener(view ->
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show());

        Resources resources = getResources();
        LiveDataReactiveStreams.fromPublisher(channelRepository.getTotalUnreadCount())
                .observe(this, totalUnreadCount -> {
                    String totalUnreadCountString = resources.getString(R.string.total_unread_d, totalUnreadCount);
                    totalUnreadTextView.setText(totalUnreadCountString);
                });
    }

    private void setupChannelList() {
        adapter = new ChannelListAdapter();
        channelListRecyclerView.setAdapter(adapter);

        String[] modes = new String[]{
                EkoChannelFilter.ALL.getApiKey(),
                EkoChannelFilter.MEMBER.getApiKey(),
                EkoChannelFilter.NOT_MEMBER.getApiKey(),
        };

        spinner.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                modes));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filter = EkoChannelFilter.fromApiKey(modes[position]);
                observeChannelCollection();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_channel_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_change_user_id) {
            showDialog(R.string.change_user_id, "", EkoClient.getUserId(), false, (dialog, input) -> {
                String userId = String.valueOf(input);
                register(userId, userId)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnComplete(this::observeChannelCollection)
                        .subscribe();
            });
            return true;
        } else if (id == R.id.action_change_display_name) {
            showDialog(R.string.change_display_name, "", EkoClient.getDisplayName(), false, (dialog, input) -> {
                String displayName = String.valueOf(input);
                EkoClient.setDisplayName(displayName)
                        .subscribe();
            });
            return true;
        } else if (id == R.id.action_join_channel) {
            new MaterialDialog.Builder(this)
                    .items("standard", "broadcast", "conversation")
                    .itemsCallback((dialog, itemView, position, text) -> showDialog(R.string.join_channel, "", "", false, (d, input) -> {
                        String channelId = String.valueOf(input);
                        channelRepository.getOrCreateById(channelId, EkoChannel.Type.fromJson(text.toString()));
                    })).show();
            return true;
        } else if (id == R.id.action_change_api_key) {
            Preference<String> apiKeyStore = SimplePreferences.getApiKey();
            showDialog(R.string.change_api_key, "", apiKeyStore.get(), false, (dialog, input) -> {
                final String newApiKey = String.valueOf(input);
                apiKeyStore.set(newApiKey);
                EkoClient.setup(newApiKey);
            });
        } else if (id == R.id.action_with_tags) {
            showDialog(R.string.with_tag, "bnk48,football,concert", Joiner.on(",").join(includingTags.get()), true, (dialog, input) -> {
                Set<String> set = Sets.newConcurrentHashSet();
                for (String tag : String.valueOf(input).split(",")) {
                    if (tag.length() > 0) {
                        set.add(tag);
                    }
                }
                includingTags.set(set);
                observeChannelCollection();
            });
        } else if (id == R.id.action_without_tags) {
            showDialog(R.string.with_tag, "bnk48,football,concert", Joiner.on(",").join(excludingTags.get()), true, (dialog, input) -> {
                Set<String> set = Sets.newConcurrentHashSet();
                for (String tag : String.valueOf(input).split(",")) {
                    if (tag.length() > 0) {
                        set.add(tag);
                    }
                }
                excludingTags.set(set);
                observeChannelCollection();
            });
        } else if (id == R.id.action_enable_push) {
            FirebaseInstanceId.getInstance()
                    .getInstanceId()
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        EkoClient.registerDeviceForPushNotification(task.getResult().getToken())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnComplete(() -> Toast.makeText(this, String.format("enable push for %s", EkoClient.getUserId()), Toast.LENGTH_SHORT).show())
                                .subscribeOn(Schedulers.io())
                                .subscribe();
                    });
        } else if (id == R.id.action_disable_push) {
            EkoClient.unregisterDeviceForPushNotification(EkoClient.getUserId())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete(() -> Toast.makeText(this, String.format("disable push for %s", EkoClient.getUserId()), Toast.LENGTH_SHORT).show())
                    .subscribeOn(Schedulers.io())
                    .subscribe();
        } else if (id == R.id.action_chatkit) {
            Intent chatKit = new Intent(this, ChatKitChannelListActivity.class);
            startActivity(chatKit);
        }
        return super.onOptionsItemSelected(item);
    }

    private void observeChannelCollection() {
        if (channels != null) {
            channels.removeObservers(ChannelListActivity.this);
        }
        channels = channelRepository.getChannelCollectionByTags(filter, new EkoTags(includingTags.get()), new EkoTags(excludingTags.get()));
        channels.observe(ChannelListActivity.this, adapter::submitList);
    }

    private void showDialog(@StringRes int title, CharSequence hint, CharSequence prefill, boolean allowEmptyInput, MaterialDialog.InputCallback callback) {
        new MaterialDialog.Builder(this)
                .title(title)
                .inputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS)
                .input(hint, prefill, allowEmptyInput, callback)
                .show();
    }

    private Completable register(String userId, String displayName) {
        return EkoClient.registerDevice(userId, displayName);
    }
}
