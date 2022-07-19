package com.example.storbook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context mContext;
    private List<ImageUrls> mImageUrl;

    public ImageAdapter(Context context, List<ImageUrls> url){
        mContext = context;
        mImageUrl = url;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        ImageUrls imgCurrent = mImageUrl.get(position);
        Picasso.with(mContext)
                .load(imgCurrent.getUrl()).fit().centerCrop().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mImageUrl.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view_upload);
        }
    }
}
