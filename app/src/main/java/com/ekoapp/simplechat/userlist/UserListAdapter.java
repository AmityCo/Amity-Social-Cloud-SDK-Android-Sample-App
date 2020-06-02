package com.ekoapp.simplechat.userlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ekoapp.ekosdk.EkoObjects;
import com.ekoapp.ekosdk.EkoUser;
import com.ekoapp.ekosdk.adapter.EkoUserAdapter;
import com.ekoapp.simplechat.BaseViewHolder;
import com.ekoapp.simplechat.R;

import butterknife.BindView;

public class UserListAdapter extends EkoUserAdapter<UserListAdapter.UserViewHolder> {

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        EkoUser user = getItem(position);

        if (EkoObjects.isProxy(user)) {
            holder.userId = null;
            holder.userTextview.setText("loading...");
        } else {
            String text = new StringBuilder()
                    .append("id: ")
                    .append(user.getUserId())
                    .append("\ndisplayname: ")
                    .append(user.getDisplayName())
                    .append("\nflag count: ")
                    .append(user.getFlagCount())
                    .append("\nmetadata: ")
                    .append(user.getMetadata().toString())
                    .append("\ncreatedAt: ")
                    .append(user.getCreatedAt().toString())
                    .toString();

            holder.userId = user.getUserId();
            holder.userTextview.setText(text);
        }
    }


    static class UserViewHolder extends BaseViewHolder {

        String userId;

        @BindView(R.id.user_textview)
        TextView userTextview;


        UserViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(view -> {
                // do nothing
            });
        }
    }
}
