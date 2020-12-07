package cn.work.suyuan.ui.adapter;

import android.net.Uri;
import android.os.Build;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.youth.banner.adapter.BannerAdapter;
import com.youth.banner.util.BannerUtils;

import java.util.List;

import cn.work.suyuan.R;

public class BannerImgAdapter extends BannerAdapter<Integer, BannerImgAdapter.BannerViewHolder> {


    public BannerImgAdapter(List<Integer> mDatas) {
        //设置数据，也可以调用banner提供的方法,或者自己在adapter中实现
        super(mDatas);
    }
    @Override
    public BannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        ImageView imageView = (ImageView) BannerUtils.getView(parent, R.layout.banner_img);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BannerUtils.setBannerRound(imageView,20);
        }
        return new BannerImgAdapter.BannerViewHolder(imageView);
    }

    @Override
    public void onBindView(BannerViewHolder holder, Integer data, int position, int size) {
        holder.imageView.setImageResource(data);
//        Glide.with(holder.itemView)
//                .load(data.getPic()).error(R.mipmap.wuneigui)
//                .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
//                .into(holder.imageView);
    }



    class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public BannerViewHolder(@NonNull ImageView view) {
            super(view);
            this.imageView = view;
        }
    }
}
