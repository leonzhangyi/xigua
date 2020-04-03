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
        ImageView itemNetResourceImage1 = helper.getView(R.id.item_net_resource_image_1);
        TextView itemNetResourceTime = helper.getView(R.id.item_net_resource_time);
        TextView itemNetResourceName = helper.getView(R.id.item_net_resource_name);
        TextView item_net_resource_sub = helper.getView(R.id.item_net_resource_sub);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) itemNetResourceImage.getLayoutParams();
        int width = (int) ((XGConstant.Screen_Width) * 0.495);
//        params.width = width;
        params.height = (int) (width * 0.9);
//
        itemNetResourceImage.requestLayout();
        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) itemNetResourceImage1.getLayoutParams();
        int width1 = (int) ((XGConstant.Screen_Width) * 0.495);
//        params1.width = width1;
        params1.height = (int) (width1 * 0.9);
        itemNetResourceImage1.requestLayout();

        itemNetResourceName.setText(item.getTitle().trim());
//            if (null != data.getTorrents().getZh() && data.getTorrents().getZh().size() > 0) {
//                itemNetResourceTime.setText(data.getTorrents().getZh().get(data.getTorrents().getZh().size() - 1).getTitle());
//            }
        String syn = item.getSynopsis().trim();
//        syn = syn.replace(" ", "");
        syn = trim1(syn);
        itemNetResourceTime.setText(GetTail(item.getSynopsis().trim()));
        item_net_resource_sub.setText(syn);
//            LogUtil.e("testjianjie", "data.getSynopsis().trim() = " + data.getSynopsis().trim());
        GlideApp.with(itemNetResourceImage.getContext())
                .asBitmap()
                .load(item.getPoster())
                .placeholder(R.mipmap.video_def)
                .override(200, 300)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(itemNetResourceImage);


        helper.itemView.setTag(R.id.tag_id1, item);
        helper.itemView.setOnClickListener(this);
    }

    public static String trim1(String s) {
        String result = "";
        if (null != s && !"".equals(s)) {
            result = s.replaceAll("^[　*| *|&nbsp;*|//s*]*", "").replaceAll("[　*| *|&nbsp;*|//s*]*$", "");
        }
        return result;
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
