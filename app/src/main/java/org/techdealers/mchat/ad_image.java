package org.techdealers.mchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ad_image extends RecyclerView.Adapter<ad_image.ViewHolder> {

    private Context context;
    private List<pojo_image> list;
    private RecyclerView recyclerView;

    public ad_image(Context context, List<pojo_image> list, RecyclerView recyclerView) {
        this.context = context;
        this.list = list;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ad_image.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ad_image.ViewHolder holder, int position) {

        pojo_image item = list.get(position);

        holder.image.setImageURI(item.getUri());

        holder.close.setOnClickListener(v -> {
            list.remove(position);
            notifyDataSetChanged();
            if (list.size() == 0) {
                recyclerView.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView close, image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            close = itemView.findViewById(R.id.image_close);
            image = itemView.findViewById(R.id.Image);

        }
    }
}
