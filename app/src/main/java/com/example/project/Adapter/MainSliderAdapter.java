package com.example.project.Adapter;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class MainSliderAdapter extends SliderAdapter {

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder viewHolder) {
        switch (position) {
            case 0:
                viewHolder.bindImageSlide("https://cdn.pixabay.com/photo/2016/03/09/15/30/dessert-1246687_1280.jpg");
                break;
            case 1:
                viewHolder.bindImageSlide("https://cdn.pixabay.com/photo/2018/04/17/11/03/cocktail-3327242_1280.jpg");
                break;
            case 2:
                viewHolder.bindImageSlide("https://assets.materialup.com/uploads/76d63bbc-54a1-450a-a462-d90056be881b/preview.png");
                break;
        }
    }
}