package com.example.wazzi;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SendWazzi extends AppCompatActivity implements View.OnClickListener {

    private Button btnSendTweet, btnShowTweets;
    private EditText edtTweet;
    private ListView listTweetView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweets_users);

        listTweetView = findViewById(R.id.listTweetView);

        edtTweet = findViewById(R.id.edtTweet);
        btnShowTweets = findViewById(R.id.btnShowTweets);
        btnSendTweet = findViewById(R.id.btnSendTweet);

        btnSendTweet.setOnClickListener(this);
        btnShowTweets.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnSendTweet:
                try {
                    ParseObject tweet = new ParseObject("MyTweets");
                    tweet.put("tweet", edtTweet.getText().toString());
                    tweet.put("user", ParseUser.getCurrentUser().getUsername());

                    final ProgressDialog dialog = new ProgressDialog(this);
                    dialog.setMessage("Loading...");
                    dialog.show();

                    tweet.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                FancyToast.makeText(SendWazzi.this, "Tweet sent successfully.", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                            } else {
                                FancyToast.makeText(SendWazzi.this, "Couldn't send tweet.", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            }
                            dialog.dismiss();
                        }
                    });

                } catch (Exception e) {
                    if (e == null) {
                        FancyToast.makeText(SendWazzi.this, "Object not saved.", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                }
            case R.id.btnShowTweets:
                final ArrayList<HashMap<String, String>> tweetList = new ArrayList<>();
                final SimpleAdapter adapter = new SimpleAdapter(SendWazzi.this, tweetList, android.R.layout.simple_list_item_2, new String[]{"tweetUserName", "tweetValue"}, new int[]{android.R.id.text1, android.R.id.text2});
                try {
                    ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("MyTweet");
                    parseQuery.whereContainedIn("user", ParseUser.getCurrentUser().getList("fanOf"));
                    parseQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (objects.size() > 0 && e == null) {
                                for (ParseObject tweetObject : objects) {
                                    HashMap<String, String> userTweet = new HashMap<>();
                                    userTweet.put("tweetUserName", tweetObject.getString("user"));
                                    userTweet.put("tweetValue", tweetObject.getString("tweet"));
                                    tweetList.add(userTweet);
                                }
                                listTweetView.setAdapter(adapter);
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }
}
