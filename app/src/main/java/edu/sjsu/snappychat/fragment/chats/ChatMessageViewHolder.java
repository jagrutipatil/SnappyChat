package edu.sjsu.snappychat.fragment.chats;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import edu.sjsu.snappychat.R;

/**
 * Created by i856547 on 12/6/16.
 */

    public class ChatMessageViewHolder extends RecyclerView.ViewHolder {

        TextView sender, msg;
        public ChatMessageViewHolder(View itemView) {
            super(itemView);
            sender = (TextView) itemView.findViewById(R.id.sender);
            msg = (TextView) itemView.findViewById(R.id.msg);
        }
    }

