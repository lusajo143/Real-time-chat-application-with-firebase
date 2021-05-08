package org.techdealers.mchat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText msg;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    private boolean changed = false;

    private RecyclerView recyclerView;
    private List<pojo_msg> list;
    private ad_msg adapter;
    private String choosen = null;
    private Uri uri = null;

    private RecyclerView recyclerView_image;
    private List<pojo_image> img_list;
    private ad_image img_adapter;

    private LinearLayout choose;
    private CardView image_card, video_card;

    private TextView typing_status;

    ValueEventListener listener = new ValueEventListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            list.clear();
            for (DataSnapshot shot : snapshot.getChildren()) {
                String phone = "";
                String message = "";
                String image = "";
                String status = "";
                String time = "";

                for (DataSnapshot head : shot.getChildren()) {
                    if (head.getKey().equals("phone")) {
                        phone = head.getValue().toString();
                    } else if (head.getKey().equals("message")) {
                        message = head.getValue().toString();
                    } else if (head.getKey().equals("image")) {
                        image = head.getValue().toString();
                    } else if (head.getKey().equals("status")) {
                        status = head.getValue().toString();
                    } else if (head.getKey().equals("time")) {
                        time = head.getValue().toString();
                    }
                }
                list.add(new pojo_msg(phone, message, image, shot.getKey(), status, time));
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
    };
    private LottieAnimationView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        msg = findViewById(R.id.msg);
        recyclerView = findViewById(R.id.messages);
        recyclerView_image = findViewById(R.id.recyclerImage);
        choose = findViewById(R.id.choose);
        image_card = findViewById(R.id.card_image);
        video_card = findViewById(R.id.card_video);
        loading = findViewById(R.id.loading_view);

        typing_status = findViewById(R.id.typing_status);


        recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();
        adapter = new ad_msg(this, list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView_image.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView_image.setLayoutManager(manager);
        img_list = new ArrayList<>();
        img_adapter = new ad_image(MainActivity.this, img_list, recyclerView_image);
        recyclerView_image.setAdapter(img_adapter);


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child("images");

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");
        myRef.addValueEventListener(listener);

        msg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                DatabaseReference ref = database.getReference().child("Status");
                DatabaseReference reference = ref.child(new dbHelper(MainActivity.this).getPhone());
                if (count == 0) {
                    reference.setValue("Not typing");
                } else {
                    reference.setValue("Typing");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        image_card.setOnClickListener(v -> {
            Intent gallery = new Intent(Intent.ACTION_PICK);
            gallery.setType("image/*");
            startActivityForResult(gallery, 100);
        });

        Typing_Status();

    }

    private void Typing_Status() {

        DatabaseReference reference = database.getReference("Status");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    if (snap.getValue().toString().equals("Typing")) {
                        typing_status.setText(snap.getKey() + " is typing");
                    } else {
                        typing_status.setText("...");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void send(View view) {
        // Write a message to the database
        final String[] text = {"null"};
        if (!msg.getText().toString().equals("")) {
            text[0] = msg.getText().toString();
        }
        if (img_list.size() == 0) {
            DatabaseReference ref = myRef.push();
            ref.child("phone").setValue(new dbHelper(MainActivity.this).getPhone());
            ref.child("message").setValue(text[0]);
            ref.child("status").setValue("Available");
            ref.child("image").setValue("null");
            ref.child("time").setValue(new Date().getTime() + "");
            msg.setText("");
        } else {
            loading.setVisibility(View.VISIBLE);
            recyclerView_image.setVisibility(View.GONE);

            for (int i = 0; i < img_list.size(); i++) {
                StorageReference file = storageReference.child(String.valueOf(new Date().getTime()));
                file.putFile(img_list.get(i).getUri())
                        .addOnSuccessListener(taskSnapshot -> {
                            loading.setVisibility(View.GONE);
                            file.getDownloadUrl().addOnSuccessListener(uri -> {
                                DatabaseReference ref = myRef.push();
                                ref.child("time").setValue(new Date().getTime() + "");
                                ref.child("phone").setValue(new dbHelper(MainActivity.this).getPhone());
                                ref.child("message").setValue(text[0]);
                                ref.child("status").setValue("Available");
                                text[0] = "null";
                                ref.child("image").setValue(uri.toString());
                            });
                        })
                        .addOnCanceledListener(() -> {
                            recyclerView_image.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.GONE);
                        });
            }

            msg.setText("");
            img_list.clear();
            img_adapter.notifyDataSetChanged();

        }
    }


    public void attach(View view ){

        if (choose.getVisibility() == View.GONE) {
            choose.setVisibility(View.VISIBLE);
        } else {
            choose.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            img_list.add(new pojo_image(data.getData()));
            img_adapter.notifyDataSetChanged();
            recyclerView_image.setVisibility(View.VISIBLE);
            choose.setVisibility(View.GONE);
        }

    }

    public void logout(View view) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage("Do you want to logout?")
                .setPositiveButton("Yes", ((dialog, which) -> {
                    new dbHelper(this)
                            .logout();
                    startActivity(new Intent(MainActivity.this,
                            Phone.class));
                    finish();
                }))
                .setNegativeButton("No", ((dialog, which) -> {

                }))
                .create();

        alertDialog.show();
    }

}