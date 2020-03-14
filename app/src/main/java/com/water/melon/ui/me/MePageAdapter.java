package com.water.melon.ui.me;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.water.melon.R;
import com.water.melon.application.MyApplication;
import com.water.melon.ui.in.AdapterItemClick;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MePageAdapter extends RecyclerView.Adapter<MePageAdapter.ViewHolder> {

    private Context context;
    private List<PageBean> PageBeans;
    public AdapterItemClick AdapterItemClick;

    public MePageAdapter(List<PageBean> PageBeans, Context context) {
        this.context = context;
        this.PageBeans = PageBeans;
    }


    public void setAdapterItemClick(com.water.melon.ui.in.AdapterItemClick AdapterItemClick) {
        this.AdapterItemClick = AdapterItemClick;
    }

    @NonNull
    @Override
    public MePageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_me_page, parent, false);
        MePageAdapter.ViewHolder vh = new MePageAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MePageAdapter.ViewHolder holder, int position) {
        holder.layout_me_page_right_name.setText(PageBeans.get(position).getName());
        holder.layout_me_page_right_text.setText(PageBeans.get(position).getText());
        holder.layout_me_page_right_icon.setBackgroundResource(PageBeans.get(position).getBac());
    }

    @Override
    public int getItemCount() {
        return PageBeans.size();
    }




    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView layout_me_page_right_name;
        public TextView layout_me_page_right_text;
        public ImageView layout_me_page_right_icon;

        public ViewHolder(View view) {
            super(view);
            layout_me_page_right_name = view.findViewById(R.id.layout_me_page_right_name);
            layout_me_page_right_text = view.findViewById(R.id.layout_me_page_right_text);
            layout_me_page_right_icon = view.findViewById(R.id.layout_me_page_right_icon);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AdapterItemClick.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}

