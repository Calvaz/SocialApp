package com.example.wazzi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class WazziUsers extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_users);

        listView = findViewById(R.id.listView);
        arrayList = new ArrayList();
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_checked, arrayList);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(this);

        final SwipeRefreshLayout swipeRefreshLayout = new SwipeRefreshLayout(this);

        final ProgressDialog dialog = new ProgressDialog(this);

        Toast.makeText(this, "Welcome" + ParseUser.getCurrentUser().getUsername(), Toast.LENGTH_SHORT).show();


            final ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
            parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());

            dialog.setMessage("Loading..");
            dialog.show();
            parseQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> users, ParseException e) {

                    if (e == null) {
                        if (users.size() > 0) {

                            for (ParseUser user : users) {

                                arrayList.add(user.getUsername());

                            }
                            listView.setAdapter(arrayAdapter);

                                for (String socialUser : arrayList) {
                                    if (ParseUser.getCurrentUser().getList("fanOf") != null) {
                                        if (ParseUser.getCurrentUser().getList("fanOf").contains(socialUser)) {
                                            listView.setItemChecked(arrayList.indexOf(socialUser), true);
                                        }
                                    }
                                }
                            listView.setVisibility(View.VISIBLE);
                            dialog.dismiss();

                        }
                    }
                }
            });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
                    parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
                    parseQuery.whereNotContainedIn("username", arrayList);
                    parseQuery.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> users, ParseException e) {
                            if (users.size() > 0) {
                                if (e == null) {
                                    for (ParseUser user : users) {
                                        arrayList.add(user.getUsername());

                                    }
                                    arrayAdapter.notifyDataSetChanged();
                                    if (swipeRefreshLayout.isRefreshing()) {
                                        swipeRefreshLayout.setRefreshing(false);
                                    }
                                }

                            } else {
                                if (swipeRefreshLayout.isRefreshing()) {
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.logoutItem:
                ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        Intent intent = new Intent(WazziUsers.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            case R.id.tweetItem:
                    Intent intent1 = new Intent(WazziUsers.this, SendWazzi.class);
                    startActivity(intent1);
                    finish();
            case R.id.sendMessage:
                    Intent intent2 = new Intent(WazziUsers.this, WazziTexts.class);
                    startActivity(intent2);
                    finish();
                    break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(WazziUsers.this, WazziTexts.class);
        intent.putExtra("selectedUser", arrayList.get(position));
        startActivity(intent);
    }
}
