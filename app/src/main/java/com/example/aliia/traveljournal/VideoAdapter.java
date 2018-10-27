package com.example.aliia.traveljournal;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private Context mContext;
    private List<Upload> mUploads;

    private OnItemClickListener mListener;
    public VideoAdapter(Context context,List<Upload> uploads)
    {
        mContext=context;
        mUploads= uploads;
    }
    @Override
    public VideoAdapter.VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.video_item, parent,false);
        return new VideoAdapter.VideoViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {

        Upload uploadCurrent=mUploads.get(position);
        holder.textViewName.setText(uploadCurrent.getName());
        Log.d("Adapter img",uploadCurrent.getImageUrl());

        Uri u1= Uri.parse(uploadCurrent.getImageUrl());

        holder.videoView.setVideoURI(u1);
        holder.videoView.requestFocus();
        holder.videoView.start();
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener,MenuItem.OnMenuItemClickListener{

        public TextView textViewName;
        public VideoView videoView;


        public VideoViewHolder(View itemView)
        {
            super(itemView);
            textViewName=itemView.findViewById(R.id.text_view_name4);
            videoView=itemView.findViewById(R.id.video_view_upload);
            itemView.setOnClickListener(this);

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener!=null)
            {
                int position=getAdapterPosition();
                if(position!=RecyclerView.NO_POSITION)
                {
                    mListener.OnItemClick(position);
                }
            }

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");


            MenuItem delete =menu.add(Menu.NONE,2,2,"Delete");

            delete.setOnMenuItemClickListener(this);

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(mListener!=null)
            {
                int position=getAdapterPosition();
                if(position!=RecyclerView.NO_POSITION)
                {
                    switch (item.getItemId())
                    {

                        case 2:
                            mListener.onDeleteClick(position);
                            return true;

                    }

                }
            }
            return false;
        }
    }
    public interface OnItemClickListener
    {
        void OnItemClick(int position);



        void onDeleteClick(int poition);


    }
    public void setOnItemCickListene(OnItemClickListener listener)
    {
        mListener=listener;


    }
}
