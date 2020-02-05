package com.ue.uebook.ChatSdk;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.ue.uebook.ChatSdk.Adapter.MyAdapter;
import com.ue.uebook.HomeActivity.HomeScreen;
import com.ue.uebook.R;

public class ChatHistoryScreen extends AppCompatActivity implements ChatHistoryFrag.OnFragmentInteractionListener, GroupChatFrag.OnFragmentInteractionListener, View.OnClickListener {
    TabLayout tabLayout;
    ViewPager viewPager;
    private ImageButton backbtnChat;
    private FloatingActionButton newChatbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_history_screen);
        tabLayout = findViewById(R.id.tabLayout);
        backbtnChat=findViewById(R.id.backbtnChat);
        backbtnChat.setOnClickListener(this);
        newChatbtn= findViewById(R.id.newChatbtn);
        newChatbtn.setOnClickListener(this);
        viewPager = findViewById(R.id.viewPager);
        tabLayout.addTab(tabLayout.newTab().setText("Chat"));
        tabLayout.addTab(tabLayout.newTab().setText("Group Chat"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final MyAdapter adapter = new MyAdapter(this,getSupportFragmentManager(),
                tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClick(View v) {
        if (v== backbtnChat){
            Intent intent = new Intent(this , HomeScreen.class);
            startActivity(intent);
            finish();
        }
       else if (v==newChatbtn){
            Intent intent = new Intent(this,ContactListScreen.class);
              startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,HomeScreen.class);
        startActivity(intent);
        finish();
    }
}
