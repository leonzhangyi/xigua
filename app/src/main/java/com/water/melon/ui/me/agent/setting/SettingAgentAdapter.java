package com.water.melon.ui.me.agent.setting;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.water.melon.R;
import com.water.melon.net.bean.MyAgentBean;

import androidx.annotation.NonNull;

public class SettingAgentAdapter extends BaseQuickAdapter<MyAgentBean.Vips, BaseViewHolder> {
    public interface SaveEditListener {
        void SaveEdit(MyAgentBean.Vips item, String string);
    }

    public SettingAgentAdapter() {
        super(R.layout.layout_my_agent_setting_item, null);
    }

    private SaveEditListener listener;

    public void setListener(SaveEditListener listener) {
        this.listener = listener;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MyAgentBean.Vips item) {
        TextView layout_setting_agent_item_name = helper.getView(R.id.layout_setting_agent_item_name);
        EditText setting_agent_wx = helper.getView(R.id.setting_agent_wx);

        layout_setting_agent_item_name.setText(item.getTitle());

        setting_agent_wx.addTextChangedListener(new TextSwitcher(setting_agent_wx));
        setting_agent_wx.setTag(item);
//        listener.SaveEdit(item,item.getPrice());
        setting_agent_wx.setText(item.getPrice());

    }

    //自定义EditText的监听类
    class TextSwitcher implements TextWatcher {

        private EditText mHolder;

        public TextSwitcher(EditText mHolder) {
            this.mHolder = mHolder;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            //用户输入完毕后，处理输入数据，回调给主界面处理
//            SaveEditListener listener = (SaveEditListener) mContext;
            if (s != null) {
                listener.SaveEdit((MyAgentBean.Vips) mHolder.getTag(), s.toString());
            }

        }
    }

}
