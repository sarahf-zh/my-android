package com.example.carebridge;

import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;

import com.example.carebridge.fixtures.MessagesFixtures;
import com.example.carebridge.fixtures.FixtureMessage;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public abstract class BaseMessageListActivity extends AppCompatActivity
        implements MessagesListAdapter.SelectionListener,
        MessagesListAdapter.OnLoadMoreListener {

    private static final int TOTAL_MESSAGES_COUNT = 100;
    protected final String senderId = "0";
    protected ImageLoader imageLoader;
    protected MessagesListAdapter<FixtureMessage> messagesAdapter;

    private Menu menu;
    private int selectionCount;
    private Date lastLoadedDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url, Object payload) {
                Picasso.get().load(url).into(imageView);
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
     //   messagesAdapter.addToStart(MessagesFixtures.getTextMessage(), true);
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.chat_actions_menu, menu);
        onSelectionChanged(0);
        return true;
    }*/

 /*   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                messagesAdapter.deleteSelectedMessages();
                break;
            case R.id.action_copy:
                messagesAdapter.copySelectedMessagesText(this, getMessageStringFormatter(), true);
                AppUtils.showToast(this, R.string.copied_message, true);
                break;
        }
        return true;
    }*/

    @Override
    public void onBackPressed() {
        if (selectionCount == 0) {
            super.onBackPressed();
        } else {
            messagesAdapter.unselectAllItems();
        }
    }

    @Override
    public void onLoadMore(int page, int totalItemsCount) {
        Log.i("TAG", "onLoadMore: " + page + " " + totalItemsCount);
        if (totalItemsCount < TOTAL_MESSAGES_COUNT) {
          //  loadMessages();
        }
    }

    @Override
    public void onSelectionChanged(int count) {
        this.selectionCount = count;
      //  menu.findItem(R.id.action_delete).setVisible(count > 0);
     //   menu.findItem(R.id.action_copy).setVisible(count > 0);
    }

    protected void loadMessages() {
        new Handler().postDelayed(new Runnable() { //imitation of internet connection
            @Override
            public void run() {
                ArrayList<FixtureMessage> messages = MessagesFixtures.getMessages(lastLoadedDate);
                lastLoadedDate = messages.get(messages.size() - 1).getCreatedAt();
                messagesAdapter.addToEnd(messages, false);
            }
        }, 1000);
    }

    private MessagesListAdapter.Formatter<FixtureMessage> getMessageStringFormatter() {
        return new MessagesListAdapter.Formatter<FixtureMessage>() {
            @Override
            public String format(FixtureMessage message) {
                String createdAt = new SimpleDateFormat("MMM d, EEE 'at' h:mm a", Locale.getDefault())
                        .format(message.getCreatedAt());

                String text = message.getText();
                if (text == null) text = "[attachment]";

                return String.format(Locale.getDefault(), "%s: %s (%s)",
                        message.getUser().getName(), text, createdAt);
            }
        };
    }
}
