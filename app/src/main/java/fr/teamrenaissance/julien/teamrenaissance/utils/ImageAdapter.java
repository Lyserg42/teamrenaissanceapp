package fr.teamrenaissance.julien.teamrenaissance.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import fr.teamrenaissance.julien.teamrenaissance.R;
import fr.teamrenaissance.julien.teamrenaissance.beans.Card;
import fr.teamrenaissance.julien.teamrenaissance.beans.Dialog;

public class ImageAdapter extends BaseAdapter{
    private Context context;
    private List<Card> urls = new ArrayList<>();

    public ImageAdapter(Context context,List<Card> urls) {
        this.context = context;
        this.urls = urls;
    }

    public void setUrls(List<Card> urls) {
        this.urls = urls;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public String getItem(int position) {
        return urls.get(position).getImg();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NumImageView imageView;
        if (convertView == null) {
            imageView = new NumImageView(context);
            imageView.setNum(urls.get(position).getQty());
            imageView.setLayoutParams(new GridView.LayoutParams(300, 300));//set ImageView width,heigh
            //imageView.setAdjustViewBounds(false);
            //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(50, 50, 50, 50);

        } else {
            imageView = (NumImageView) convertView;
        }

        Glide.with(context).load(urls.get(position).getImg()).into(imageView);
        return imageView;
    }
}




