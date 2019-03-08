package com.zflabs.marveldatabase.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zflabs.marveldatabase.R;
import com.zflabs.marveldatabase.data.Character;
import com.zflabs.marveldatabase.data.Thumbnail;
import com.zflabs.marveldatabase.util.PictureUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CharacterAdapter extends Adapter<CharacterAdapter.MarvelAdapterViewHolder> {

    private List<Character> characterData;

    private final MarvelAdapterClickHandler mClickHandler;

    public interface MarvelAdapterClickHandler {
        void onCharacterClick(Character character);
    }

    public CharacterAdapter(MarvelAdapterClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class MarvelAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        @BindView(R.id.hero_thumbnail)
        public ImageView imageView;

        @BindView(R.id.hero_name_thumbnail)
        public TextView heroThumbnailText;

        public MarvelAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Character actualCharacter = characterData.get(adapterPosition);
            mClickHandler.onCharacterClick(actualCharacter);
        }
    }

    @Override
    public MarvelAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.character_card_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MarvelAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MarvelAdapterViewHolder marvelAdapterViewHolder, int position) {
        Context context = marvelAdapterViewHolder.imageView.getContext();
        Character character = characterData.get(position);
        Thumbnail thumbnail = character.getThumbnail();
        String path = PictureUtils.getPortraitUncanny(thumbnail);;
        Picasso.with(context).load(path).into(marvelAdapterViewHolder.imageView);
        String characterName = character.getName();
        marvelAdapterViewHolder.imageView.setContentDescription(context.getString(R.string.cd_image_of) + characterName);
        marvelAdapterViewHolder.heroThumbnailText.setText(characterName);
    }

    @Override
    public int getItemCount() {
        if (null == characterData) return 0;
        return characterData.size();
    }

    public void setCharacterData(List<Character> characterData) {
        this.characterData = characterData;
        notifyDataSetChanged();
    }

    public List<Character> getCharacterData(){
        return characterData;
    }
}