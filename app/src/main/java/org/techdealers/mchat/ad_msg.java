package org.techdealers.mchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ad_msg extends RecyclerView.Adapter<ad_msg.ViewHolder> {

    private Context context;
    private List<pojo_msg> list;

    public ad_msg(Context context, List<pojo_msg> list) {
        this.context = context;
        this.list = list;
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
            holder.msg_right.setText(item.getMessage());
            holder.right.setVisibility(View.VISIBLE);
            holder.left.setVisibility(View.GONE);
        } else {
            holder.msg_left.setText(item.getMessage());
            holder.left.setVisibility(View.VISIBLE);
            holder.right.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView msg_left, msg_right;
        private CardView left, right;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            msg_left = itemView.findViewById(R.id.message_txt_left);
            msg_right = itemView.findViewById(R.id.message_txt_right);
            left = itemView.findViewById(R.id.card_left);
            right = itemView.findViewById(R.id.card_right);

        }
    }
}
