package com.water.melon.ui.netresource;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sunfusheng.progress.GlideApp;
import com.water.melon.R;
import com.water.melon.constant.XGConstant;
import com.water.melon.net.bean.CreateCodeBean;
import com.water.melon.utils.ClickTooQucik;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemNetResouceItemAdapter1 extends BaseQuickAdapter<NetResoutVideoInfo, BaseViewHolder> implements View.OnClickListener {

    public ItemNetResouceItemAdapter1() {
        super(R.layout.item_net_resource_item, null);
    }

    public void setAdapterListen(ItemNetResouceItemAdapter.AdapterListen adapterListen) {
        this.adapterListen = adapterListen;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, NetResoutVideoInfo item) {
        ImageView itemNetResourceImage = helper.getView(R.id.item_net_resource_image);
        TextView itemNetResourceTime = helper.getView(R.id.item_net_resource_time);
        TextView itemNetResourceName = helper.getView(R.id.item_net_resource_name);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) itemNetResourceImage.getLayoutParams();
        int width = (int) ((XGConstant.Screen_Width) * 0.495);
        params.width = width;
//            params.height = (int) (width * 1.5);
        params.height = (int) (width);
        itemNetResourceImage.requestLayout();

        itemNetResourceName.setText(item.getTitle().trim());
//            if (null != data.getTorrents().getZh() && data.getTorrents().getZh().size() > 0) {
//                itemNetResourceTime.setText(data.getTorrents().getZh().get(data.getTorrents().getZh().size() - 1).getTitle());
//            }
        itemNetResourceTime.setText(GetTail(item.getSynopsis().trim()));
//            LogUtil.e("testjianjie", "data.getSynopsis().trim() = " + data.getSynopsis().trim());
        GlideApp.with(itemNetResourceImage.getContext())
                .asBitmap()
                .load(item.getPoster())
                .placeholder(R.mipmap.bg_video_plact_vertical)
                .override(200, 300)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(itemNetResourceImage);


        helper.itemView.setTag(R.id.tag_id1, item);
        helper.itemView.setOnClickListener(this);
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

    private ItemNetResouceItemAdapter.AdapterListen adapterListen;


    public interface AdapterListen {
        void videoItemClickListen(NetResoutVideoInfo item);
    }

    @Override
    public void onClick(View view) {
        if (ClickTooQucik.isFastClick() || null == adapterListen) {//防止用户快速点击
            return;
        }
        adapterListen.videoItemClickListen((NetResoutVideoInfo) view.getTag(R.id.tag_id1));
    }


}
