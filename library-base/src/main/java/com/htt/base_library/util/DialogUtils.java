package com.htt.base_library.util;


import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.test.library_base.R;


@SuppressWarnings("unused")
public class DialogUtils {

    private static XDialog mDialog;


    public static XDialog showLoadingDialog(Context context, String msg, boolean cancelable) {
        dismiss();
        mDialog = new XDialog(context, R.style.Dialog_Transparent);
        mDialog.setCancelable(cancelable);
        mDialog.setCanceledOnTouchOutside(cancelable);
        mDialog.setMessage(msg);
        try {
            mDialog.show();
        } catch (Exception ignored) {
        }
        return mDialog;
    }

    public static void dismiss() {
        try {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
        } catch (Exception ignored) {
        }
        mDialog = null;
    }

    public interface OnUpdateListener {
        void onShow();

        void onDismiss();
    }

    public static class XDialog extends Dialog {
        private TextView vMessage;

        public XDialog(Context context) {
            this(context, R.style.Dialog);
        }

        public XDialog(Context context, int theme) {
            super(context, theme);
            setContentView(R.layout.dialog_custom_progress);
        }

        public XDialog setMessage(CharSequence msg) {
            return this.setMessage(msg, Gravity.CENTER);
        }

        public XDialog setMessage(CharSequence msg, int gravity) {
            if (vMessage == null) {
                vMessage = (TextView) findViewById(R.id.dialog_message);
            }
            if (vMessage != null) {
                if (TextUtils.isEmpty(msg)) {
                    vMessage.setVisibility(View.GONE);
                } else {
                    vMessage.setText(msg);
                    vMessage.setGravity(gravity);
                    vMessage.setVisibility(View.VISIBLE);
                }
            }
            return this;
        }

        @Override
        public void onBackPressed() {
            super.onBackPressed();
            dismiss();
        }
    }

}
