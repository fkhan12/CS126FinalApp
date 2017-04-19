package com.example.farhankhan.spacegame;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

//import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LeaderBoardActivity extends AppCompatActivity {

    private DatabaseReference mTitleReference;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mZillesCommentsReference;
    private DatabaseReference angraveReference;

//    private ArrayList<Users> mLeaders = new ArrayList<>();

    private RecyclerView mRecyclerView;
//    private LeaderBoardAdapter mLeaderAdapter;
    LinearLayoutManager mLayoutManager;

    private boolean loading = false;
    int pastVisisbleItems, visisbleItemCount, totalItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_leader_board);

        mDatabase = FirebaseDatabase.getInstance();
        mTitleReference = mDatabase.getReference("leaderboards");

        //https://firebase.google.com/docs/database/android/read-and-write
        /**
         *
         * private void writeNewUser(String userId, String name, String email) {
         User user = new User(name, email);

         mDatabase.child("users").child(userId).setValue(user);
         }
         */

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

//        mLeaderAdapter = new LeaderBoardAdapter(mLeaders);
//        mRecyclerView.setAdapter(mLeaderAdapter);

    }


}
