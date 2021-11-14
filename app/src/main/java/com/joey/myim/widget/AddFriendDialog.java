package com.joey.myim.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.joey.myim.R;

/**
 * 自定义弹窗——用于添加好友
 */

public class AddFriendDialog extends Dialog implements View.OnClickListener {
    private TextView mTvTitle, mTvCancel, mTvConfirm;
    private EditText mEtAdd;
    private String title, cancel, confirm;
    private OnCancelListener onCancelListener;
    private OnConfirmListener onConfirmListener;

    public AddFriendDialog(@NonNull Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog_add);
        //设置宽度
        WindowManager windowManager = getWindow().getWindowManager();
        Display defaultDisplay = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        Point point = new Point();
        defaultDisplay.getSize(point);
        attributes.width = (int)(point.x * 0.8);
        getWindow().setAttributes(attributes);

        mTvTitle = findViewById(R.id.tv_title);
        mEtAdd = findViewById(R.id.et_add);
        mTvCancel = findViewById(R.id.tv_cancel);
        mTvConfirm = findViewById(R.id.tv_confirm);

        if(!TextUtils.isEmpty(title)){
            mTvTitle.setText(title);
        }
        if(!TextUtils.isEmpty(cancel)){
            mTvCancel.setText(cancel);
        }
        if(!TextUtils.isEmpty(confirm)){
            mTvConfirm.setText(confirm);
        }
        mTvCancel.setOnClickListener(this);
        mTvConfirm.setOnClickListener(this);
    }

    public EditText getEtAdd() {
        return mEtAdd;
    }

    public void setEtAdd(EditText mEtAdd) {
        this.mEtAdd = mEtAdd;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_cancel:
                if(onCancelListener != null){
                    onCancelListener.onCancel(this);
                }
                break;
            case R.id.tv_confirm:
                if(onConfirmListener != null){
                    onConfirmListener.onConfirm(this);
                }
                break;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCancel() {
        return cancel;
    }

    public void setCancel(String cancel , OnCancelListener onCancelListener) {
        this.cancel = cancel;
        this.onCancelListener = onCancelListener;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm, OnConfirmListener onConfirmListener) {
        this.confirm = confirm;
        this.onConfirmListener = onConfirmListener;
    }


    public interface OnCancelListener{
        void onCancel(AddFriendDialog customDialog);
    }
    public interface OnConfirmListener{
        void onConfirm(AddFriendDialog customDialog);
    }
}
