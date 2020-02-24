package com.water.melon.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.water.melon.R;


public class RemindDialog extends Dialog {

    public RemindDialog(Context context, int theme) {
        super(context, theme);
    }

    public RemindDialog(Context context) {
        super(context);
    }

    public static class Builder {
        private Context context;
        private String title;
        private String message;
        private String postiveText;
        private String negtiveText;
        private String middleText;
        private View convertView;
        private View view;
        private LinearLayout view_ll;
        private OnClickListener postiveButton, negtiveButton, middleButton;
        private OnDialogItemClickListener itemListView;
        private int width;
        private int heigh;

        private LinearLayout dialog_ll, dialog_parent;
        private TextView dialog_title;
        private Button dialog_positive, dialog_negetive, dialog_middle;
        private RelativeLayout messageRl;
        private View splitViewFirst, splitViewSecond;
        private TextView dialog_messageTv;

        private final String TAG = "RemindDialog";

        public Builder(Context context) {
            this.context = context;
            width = context.getResources().getDisplayMetrics().widthPixels;
            heigh = context.getResources().getDisplayMetrics().heightPixels;
        }

        public interface OnDialogItemClickListener {
            public void onDialogItemClick(DialogInterface dialog, View view, int position);
        }


        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        public Builder setPositive(String postiveText, OnClickListener postiveButton) {
            this.postiveText = postiveText;
            this.postiveButton = postiveButton;
            return this;
        }

        ;

        public Builder setPositive(int postiveText, OnClickListener postiveButton) {
            this.postiveText = (String) context.getText(postiveText);
            this.postiveButton = postiveButton;
            return this;
        }

        ;

        public Builder setMiddletive(int middleText, OnClickListener middleButton) {
            this.middleText = (String) context.getText(middleText);
            this.middleButton = middleButton;
            return this;
        }

        public Builder setMiddletive(String middleText, OnClickListener middleButton) {
            this.middleText = middleText;
            this.middleButton = middleButton;
            return this;
        }


        public Builder setNegtive(String negtiveText, OnClickListener negtiveButton) {
            this.negtiveText = negtiveText;
            this.negtiveButton = negtiveButton;
            return this;
        }

        public Builder setNegtive(int negtiveText, OnClickListener negtiveButton) {
            this.negtiveText = (String) context.getText(negtiveText);
            this.negtiveButton = negtiveButton;
            return this;
        }


        public Builder setContentView(View convertView) {
            this.convertView = convertView;
            return this;
        }

        public Builder setContentView(int convertView) {
            this.convertView = View.inflate(context, convertView, null);
            return this;
        }

        public Builder setView(View view) {
            this.view = view;
            return this;
        }

        public RemindDialog createDialog() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.reminddialog_main, null);
            final RemindDialog dialog = new RemindDialog(context, R.style.remindDialog);
            dialog.setCanceledOnTouchOutside(true);//设置可点击外部取消dialog
            initView(layout);

            //设置dialog属性
            Window window = dialog.getWindow();
            window.setGravity(Gravity.CENTER);
            android.view.WindowManager.LayoutParams params = window.getAttributes();
            //params.x = width*7/10;
            //params.width = width*5/10;
            params.gravity = Gravity.CENTER;
            window.setAttributes(params);

            if (message == null) {
                view_ll.setVisibility(View.VISIBLE);
                messageRl.setVisibility(View.GONE);
                RelativeLayout.LayoutParams ll_params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                view_ll.setLayoutParams(ll_params);
                view_ll.addView(view);
                LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                view.setLayoutParams(pa);
            } else {
                dialog_messageTv.setText(message);
                view_ll.setVisibility(View.GONE);

            }

            if (title != null) {
                dialog_title.setText(title);
            }
            if (postiveText == null && negtiveText != null) {
                dialog_positive.setVisibility(View.GONE);
                dialog_negetive.setText(negtiveText);
                dialog_negetive.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        negtiveButton.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);

                    }
                });
            } else if (postiveText != null && negtiveText == null) {
                dialog_negetive.setVisibility(View.GONE);
                dialog_positive.setText(postiveText);
                dialog_positive.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        postiveButton.onClick(dialog, DialogInterface.BUTTON_POSITIVE);

                    }
                });
            } else if (postiveText != null && negtiveText != null) {

                dialog_negetive.setText(negtiveText);
                dialog_positive.setText(postiveText);

                dialog_negetive.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        negtiveButton.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);

                    }

                });

                dialog_positive.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        postiveButton.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                    }
                });
            }
            dialog.setContentView(layout);

            return dialog;
        }

        private void initView(View layout) {
            dialog_title = (TextView) layout.findViewById(R.id.reminddialog_title);
            messageRl = (RelativeLayout) layout.findViewById(R.id.reminddialog_message_rl);
            dialog_messageTv = (TextView) layout.findViewById(R.id.reminddialog_message_tv);
            view_ll = (LinearLayout) layout.findViewById(R.id.reminddialog_view_ll);
            dialog_positive = (Button) layout.findViewById(R.id.dialog_btn_sure);
            dialog_negetive = (Button) layout.findViewById(R.id.dialog_btn_cancel);
            dialog_parent = (LinearLayout) layout.findViewById(R.id.reminddialog_main_parent);
            //设置最外层布局属性
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width * 8 / 10, LinearLayout.LayoutParams.WRAP_CONTENT);
            dialog_parent.setLayoutParams(params);
        }
    }

}
