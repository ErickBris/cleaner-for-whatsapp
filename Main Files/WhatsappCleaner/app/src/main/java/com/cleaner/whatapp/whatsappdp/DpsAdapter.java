package com.cleaner.whatapp.whatsappdp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.cleaner.whatapp.R;
import java.util.ArrayList;

public class DpsAdapter extends DpMultiSelect<DpsAdapter.ViewHolder> {
    public static final String A4 = "8fc19dv";
    public Context mContext;
    private ArrayList<DpAlbum> mAlbumImages;
    private ViewHolder.ClickListener clickListener;
    public DpsAdapter(Context context, ArrayList<DpAlbum> arrayList, ViewHolder.ClickListener context2) {
        this.mAlbumImages = arrayList;
        this.mContext = context;
        this.clickListener = context2;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dp_listview_item, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView, clickListener);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        Glide.with(mContext)
                .load("file://" + mAlbumImages.get(position).getAlbumImages())
                .centerCrop()
                .placeholder(R.drawable.ic_loading)
                .crossFade()
                .into(viewHolder.imgAlbum);
        viewHolder.selectedOverlay.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);
    }
    @Override
    public int getItemCount() {
        return mAlbumImages.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private final RelativeLayout selectedOverlay;
        public ImageView imgAlbum;
        private ClickListener listener;
        TextView text;
        public ViewHolder(View itemLayoutView, ClickListener listener) {
            super(itemLayoutView);
            this.listener = listener;
            imgAlbum = (ImageView) itemLayoutView.findViewById(R.id.image);
            selectedOverlay = (RelativeLayout) itemView.findViewById(R.id.selected_overlay);
            text = (TextView) itemView.findViewById(R.id.text);
            itemLayoutView.setOnClickListener(this);
            itemLayoutView.setOnLongClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClicked(getAdapterPosition());
            }
        }
        @Override
        public boolean onLongClick(View view) {
            if (listener != null) {
                return listener.onItemLongClicked(getAdapterPosition());
            }
            return false;
        }
        public interface ClickListener {
            public void onItemClicked(int position);
            public boolean onItemLongClicked(int position);
            void onBackPressed();
        }
    }
    public ArrayList<DpAlbum> getAlbumImagesList() {
        return mAlbumImages;
    }
}