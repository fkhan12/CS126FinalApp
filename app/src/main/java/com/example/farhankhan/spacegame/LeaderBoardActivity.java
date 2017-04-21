package com.example.farhankhan.spacegame;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

//import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LeaderBoardActivity extends AppCompatActivity {

    private DatabaseReference leaderboardRef;
    private FirebaseDatabase mDatabase;

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_leader_board);

        mDatabase = FirebaseDatabase.getInstance();
        leaderboardRef = mDatabase.getReference("leaderboards");

        //the below comes from the firebase documentation: https://firebase.google.com/docs/reference/android/com/google/firebase/database/Query.html#orderByValue()
        //and a stackoverflow post: http://stackoverflow.com/questions/39441689/android-firebase-order-by-value
        //and another: http://stackoverflow.com/questions/36235919/how-to-use-a-firebaserecycleradapter-with-a-dynamic-reference-in-android
        final Query leaderboardQuery = leaderboardRef.orderByValue();

        /**
         * Typically a separate (or inner) class, the recycler view adapter for FireBase works as a single constant
         */
        final FirebaseRecyclerAdapter<Long, MyViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Long, MyViewHolder> (Long.class, R.layout.list_item, MyViewHolder.class, leaderboardQuery){

                    int pos = 1;

                    @Override
                    protected void populateViewHolder(final MyViewHolder viewHolder, Long model, int position) {
                        viewHolder.positionView.setText(pos + ".");
                        pos++;

                        String key = this.getRef(position).getKey();
                        viewHolder.usernameView.setText(key);
                        leaderboardRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                int scoreValue = dataSnapshot.getValue(Integer.class);
                                viewHolder.scoreView.setText(scoreValue + "");
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                };


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);

    }

/**
 * The ViewHolder for the RecyclerView. Uses the score_list_item.xml.
 */
public static class MyViewHolder extends RecyclerView.ViewHolder{

    public TextView positionView;
    public TextView usernameView;
    public TextView scoreView;

    public MyViewHolder(View itemView){
        super(itemView);
        positionView = (TextView) itemView.findViewById(R.id.rankingView);
        usernameView = (TextView) itemView.findViewById(R.id.userNameView);
        scoreView = (TextView) itemView.findViewById(R.id.scoreView);
    }

}
}
