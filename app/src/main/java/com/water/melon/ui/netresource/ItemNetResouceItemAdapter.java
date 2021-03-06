package com.water.melon.ui.netresource;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sunfusheng.progress.GlideApp;
import com.water.melon.R;
import com.water.melon.base.ui.BaseRVListAdapter;
import com.water.melon.constant.XGConstant;
import com.water.melon.ui.netresource.NetResoutVideoInfo;
import com.water.melon.utils.ClickTooQucik;
import com.water.melon.utils.LogUtil;

import org.w3c.dom.Text;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;


public class ItemNetResouceItemAdapter extends BaseRVListAdapter<NetResoutVideoInfo> implements View.OnClickListener {
    private AdapterListen adapterListen;

    public ItemNetResouceItemAdapter(List<NetResoutVideoInfo> datas, AdapterListen adapterListen) {
        super(datas);
        this.adapterListen = adapterListen;
    }

    public interface AdapterListen {
        void videoItemClickListen(NetResoutVideoInfo item);
    }

    @Override
    public int getMyItemViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onMyCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_net_resource_item, parent, false);
        return new MyItemHolder(view);
    }

    @Override
    public void onMyBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        LogUtil.e("itemresoure","data = "+getItemCount());

        MyItemHolder myItemHolder = (MyItemHolder) holder;
        myItemHolder.setData(getDatas().get(position));
        myItemHolder.itemView.setTag(R.id.tag_id1, getDatas().get(position));
        myItemHolder.itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (ClickTooQucik.isFastClick() || null == adapterListen) {//防止用户快速点击
            return;
        }
        adapterListen.videoItemClickListen((NetResoutVideoInfo) view.getTag(R.id.tag_id1));
    }

    @Override
    public int onAddTopItemCount() {
        return 0;
    }

    public void addHeader(View header) {
        this.mHeaderView = header;
    }

    @Override
    public int onAddBottomItemCount() {
        return 0;
    }

    static class MyItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_net_resource_image)
        ImageView itemNetResourceImage;
        @BindView(R.id.item_net_resource_time)
        TextView itemNetResourceTime;
        @BindView(R.id.item_net_resource_name)
        TextView itemNetResourceName;
        @BindView(R.id.item_net_resource_sub)
        TextView item_net_resource_sub;

        public MyItemHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) itemNetResourceImage.getLayoutParams();
            int width = (int) ((XGConstant.Screen_Width) * 0.490);
            params.width = width;
////            params.height = (int) (width * 1.5);
            params.height = (int) (width * 0.9);


//            int width = (int) ((XGConstant.Screen_Width) * 0.3);
//            params.width = width;
//            params.height = (int) (width*1.6);
//            itemNetResourceImage.requestLayout();
        }

        private void setData(NetResoutVideoInfo data) {
            itemNetResourceName.setText(data.getTitle().trim());
//            if (null != data.getTorrents().getZh() && data.getTorrents().getZh().size() > 0) {
//                itemNetResourceTime.setText(data.getTorrents().getZh().get(data.getTorrents().getZh().size() - 1).getTitle());
//            }
            itemNetResourceTime.setText(GetTail(data.getSynopsis().trim()));
//            LogUtil.e("testjianjie", "data.getSynopsis().trim() = " + data.getSynopsis().trim());
            String syn = data.getSynopsis().trim();
//        syn = syn.replace(" ", "");
            syn = ItemNetResouceItemAdapter1.trim1(syn);
            itemNetResourceTime.setText(GetTail(data.getSynopsis().trim()));
            item_net_resource_sub.setText(syn);

            GlideApp.with(itemNetResourceImage.getContext())
                    .asBitmap()
                    .load(data.getPoster())
                    .placeholder(R.mipmap.bg_video_plact_vertical)
                    .override(200, 300)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(itemNetResourceImage);
        }

        String GetTail(String str) {
            int i;
            for (i = 0; i < str.length(); ++i) {
                if (str.charAt(i) > 0x20) {
                    break;
                }
            }

            return str.substring(i);
        }
    }
}
