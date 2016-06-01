package com.limi.andorid.vocabularyassistant.acti;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.limi.andorid.vocabularyassistant.R;
import com.limi.andorid.vocabularyassistant.data.UserWord;
import com.limi.andorid.vocabularyassistant.data.Word;

import java.util.ArrayList;

/**
 * Created by limi on 16/5/4.
 */
public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.ViewHolder> {

    private ArrayList<Word> wordArrayList = new ArrayList<>();
    private Context mContext;


    public SummaryAdapter(Context context, ArrayList<Word> wordArrayList) {
        this.wordArrayList = wordArrayList;
        this.mContext = context;
    }

    @Override
    public SummaryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final SummaryAdapter.ViewHolder holder, int i) {
        Word w = wordArrayList.get(i);
        holder.wordTextView.setText(w.getWord());
        holder.meaningTextView.setText(w.getTrans());
        final UserWord userWord = UserWord.userWordHashMap.get(w.getId());
        if (userWord.isFavourite()) {
            holder.favButton.setBackgroundDrawable(mContext.getResources().getDrawable(R.mipmap.star4));
        } else {
            holder.favButton.setBackgroundDrawable(mContext.getResources().getDrawable(R.mipmap.star1));
        }
        holder.favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userWord.isFavourite()) {
                    userWord.setIsFavourite(false);
                    holder.favButton.setBackgroundDrawable(mContext.getResources().getDrawable(R.mipmap.star1));
                } else {
                    userWord.setIsFavourite(true);
                    holder.favButton.setBackgroundDrawable(mContext.getResources().getDrawable(R.mipmap.star4));
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return wordArrayList == null ? 0 : wordArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView wordTextView;
        public TextView meaningTextView;
        public ImageButton favButton;

        public ViewHolder(View itemView) {
            super(itemView);
            wordTextView = (TextView) itemView.findViewById(R.id.word);
            meaningTextView = (TextView) itemView.findViewById(R.id.meaning);
            favButton = (ImageButton) itemView.findViewById(R.id.wordFav);

        }
    }
}
