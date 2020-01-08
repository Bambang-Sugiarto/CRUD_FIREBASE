package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {


    EditText edt_title, edt_content, edt_ipk, edt_smt;
    Button btn_post, btn_update, btn_delete;
    RecyclerView recyclerView;

    FirebaseRecyclerOptions<Post> options;
    FirebaseRecyclerAdapter<Post, MyRecyclerView> adapter;

    Post selectedPost;
    String selectedKey;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        edt_title = findViewById(R.id.ETitle);
        edt_content = findViewById(R.id.EContent);
        edt_ipk = findViewById(R.id.ETipk);
        edt_smt = findViewById(R.id.ESemster);
        btn_post = findViewById(R.id.btnPost);
        btn_update = findViewById(R.id.btnupdate);
        btn_delete = findViewById(R.id.btndelete);
        recyclerView = findViewById(R.id.recycleview);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("EDMT_FIREBASE");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postComment();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference
                        .child(selectedKey)
                        .setValue(new Post(edt_title.getText().toString(), edt_content.getText().toString(), edt_ipk.getText().toString(), edt_smt.getText().toString()))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "Update Succesfuly", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference
                        .child(selectedKey)
                        .removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "Delete Succesfuly", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        displaycomment();


    }


    @Override
    protected void onStop() {

        if (adapter != null) {
            adapter.stopListening();
        }
        super.onStop();
    }

    private void postComment() {

        String title = edt_title.getText().toString();
        String content = edt_content.getText().toString();
        String ipk = edt_ipk.getText().toString();
        String semester = edt_smt.getText().toString();

        Post post = new Post(title, content, ipk, semester);

        databaseReference.push().setValue(post);
        adapter.notifyDataSetChanged();


    }

    private void displaycomment() {

        options = new FirebaseRecyclerOptions.Builder<Post>().setQuery(databaseReference, Post.class).build();
        adapter = new FirebaseRecyclerAdapter<Post, MyRecyclerView>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyRecyclerView holder, int position, @NonNull final Post model) {

                holder.txttitle.setText(model.getTitle());
                holder.txtcontent.setText(model.getContent());
                holder.teksIpk.setText(model.getIpk());
                holder.teksSmt.setText(model.getSmt());

                holder.setItemClicked(new ItemClicked() {
                    @Override
                    public void onClick(View view, int position) {

                        selectedPost = model;
                        selectedKey = getSnapshots().getSnapshot(position).getKey();
                        Log.d("KEY ITEM", "" +selectedKey);

                        edt_content.setText(model.getContent());
                        edt_title.setText(model.getTitle());
                        edt_ipk.setText(model.getIpk());
                        edt_smt.setText(model.getSmt());

                    }
                });


            }

            @NonNull
            @Override
            public MyRecyclerView onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

                View itemView = LayoutInflater.from(getBaseContext()).inflate(R.layout.post_item, viewGroup, false);
                return new MyRecyclerView(itemView);


            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

}
