package org.techdealers.mchat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.List;

public class ad_msg extends RecyclerView.Adapter<ad_msg.ViewHolder> {

    private Context context;
    private List<pojo_msg> list;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    public ad_msg(Context context, List<pojo_msg> list) {
        this.context = context;
        this.list = list;
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");
    }

    @NonNull
    @Override
    public ad_msg.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.msg, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ad_msg.ViewHolder holder, int position) {
        pojo_msg item = list.get(position);

        if (new dbHelper(context).getPhone().equals(item.getPhone())) {
            holder.right.setVisibility(View.VISIBLE);
            holder.left.setVisibility(View.GONE);

            if (item.getStatus().equals("Deleted")) {
                holder.image_right.setVisibility(View.GONE);
                holder.msg_right.setText("This message is deleted");
                holder.msg_right.setVisibility(View.VISIBLE);
                holder.time_right.setVisibility(View.GONE);
            } else {

                if (item.getImage().equals("null")) {
                    holder.image_right.setVisibility(View.GONE);
                } else {
                    Glide.with(context)
                            .load(item.getImage())
                            .into(holder.image_right);
                    holder.image_right.setVisibility(View.VISIBLE);
                }

                // Setting time
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat format = new SimpleDateFormat("dd MMM HH:mm");
                holder.time_right.setText(format.format(Long.valueOf(item.getTime())));

                holder.right.setOnLongClickListener(v -> {
                    AlertDialog delete = new AlertDialog.Builder(context)
                            .setMessage("Delete this message?")
                            .setPositiveButton("Yes", (dialog, which) -> {
                                myRef.child(item.getKey()).child("status").setValue("Deleted");
                            })
                            .setNegativeButton("No", ((dialog, which) -> {

                            }))
                            .create();
                    delete.show();
                    return true;
                });

                if (item.getMessage().equals("null")) {
                    holder.msg_right.setVisibility(View.GONE);
                }
                holder.msg_right.setText(item.getMessage());
            }


        } else {
            holder.left.setVisibility(View.VISIBLE);
            holder.right.setVisibility(View.GONE);


            holder.username.setText(item.getPhone());

            if (item.getStatus().equals("Deleted")) {
                holder.image_left.setVisibility(View.GONE);
                holder.msg_left.setText("This message was deleted");
                holder.msg_left.setVisibility(View.VISIBLE);
                holder.time_left.setVisibility(View.GONE);
            } else {

                // Setting time
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat format = new SimpleDateFormat("dd MMM HH:mm");
                holder.time_left.setText(format.format(Long.valueOf(item.getTime())));

                holder.msg_left.setText(item.getMessage());
                if (item.getImage().equals("null")) {
                    holder.image_left.setVisibility(View.GONE);
                } else {
                    Glide.with(context)
                            .load(item.getImage())
                            .into(holder.image_left);
                    holder.image_left.setVisibility(View.VISIBLE);
                }
                if (item.getMessage().equals("null")) {
                    holder.msg_left.setVisibility(View.GONE);
                }

            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView msg_left, msg_right, username;
        private CardView left, right;
        private ImageView image_right, image_left;
        private TextView time_right, time_left;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            msg_left = itemView.findViewById(R.id.message_txt_left);
            msg_right = itemView.findViewById(R.id.message_txt_right);
            left = itemView.findViewById(R.id.card_left);
            right = itemView.findViewById(R.id.card_right);
            image_left = itemView.findViewById(R.id.image_left);
            image_right = itemView.findViewById(R.id.image_right);
            username = itemView.findViewById(R.id.username_msg);
            time_left = itemView.findViewById(R.id.msg_time_left);
            time_right = itemView.findViewById(R.id.msg_time_right);
        }
    }
}
