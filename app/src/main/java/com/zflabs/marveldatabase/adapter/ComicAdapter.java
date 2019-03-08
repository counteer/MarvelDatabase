package com.zflabs.marveldatabase.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zflabs.marveldatabase.R;
import com.zflabs.marveldatabase.data.Comic;
import com.zflabs.marveldatabase.data.Thumbnail;
import com.zflabs.marveldatabase.util.PictureUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ComicAdapter extends Adapter<ComicAdapter.ComciAdapterViewHolder> {

    private List<Comic> comicData;

    public class ComciAdapterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.comic_thumbnail)
        public ImageView imageView;

        @BindView(R.id.comic_name_thumbnail)
        public TextView comicThumbnailText;

        public ComciAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }

    @Override
    public ComciAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.comic_card_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ComciAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ComciAdapterViewHolder comciAdapterViewHolder, int position) {
        Context context = comciAdapterViewHolder.imageView.getContext();
        Thumbnail th = comicData.get(position).getThumbnail();
        String path = PictureUtils.getPortraitUncanny(th);
        Picasso.with(context).load(path).into(comciAdapterViewHolder.imageView);
        comciAdapterViewHolder.comicThumbnailText.setText(comicData.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        if (null == comicData) return 0;
        return comicData.size();
    }

    public void setComicData(List<Comic> comicData) {
        this.comicData = comicData;
        notifyDataSetChanged();
    }
}