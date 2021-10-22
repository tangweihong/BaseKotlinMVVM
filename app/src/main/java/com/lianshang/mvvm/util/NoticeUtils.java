package com.lianshang.mvvm.util;

import android.content.Context;
import android.view.View;


import com.lianshang.mvvm.R;
import com.lianshang.mvvm.ui.widget.AutoTextView;

import java.util.List;


public class NoticeUtils {
    INotice mNoticeLisenter;

    public NoticeUtils(Context context, INotice lisenter) {
        this.mNoticeLisenter = lisenter;
    }

    /**
     * 实体类 重写toString 方法 返回显示的字段
     *
     * @param data
     * @param vNotice
     */
    public void startNotice(final List<?> data, final AutoTextView vNotice) {
        if (vNotice == null) {
            return;
        }
        vNotice.setTag(R.id.notice_flag, true);
        if (data != null) {
            vNotice.setTag(R.id.notice_data, data);
            vNotice.setTag(R.id.notice_position, -1);
        } else {
            @SuppressWarnings("unchecked") List<?> data1 = (List<?>) vNotice.getTag(R.id.notice_data);
            if (data1 == null || data1.isEmpty()) {
                return;
            }
        }
        if (vNotice.getTag(R.id.notice_next) == null) {
            NextRunnable runnable = new NextRunnable(vNotice);
            vNotice.setTag(R.id.notice_next, runnable);
        } else {
            NextRunnable runnable = (NextRunnable) vNotice.getTag(R.id.notice_next);
            vNotice.removeCallbacks(runnable);
        }
        start(vNotice);
        vNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer position = (Integer) v.getTag(R.id.notice_position);
                @SuppressWarnings("unchecked") List<?> data = (List<?>) vNotice.getTag(R.id.notice_data);
                if (position == -1 || position > data.size() - 1) {
                    position = 0;
                }
//                INotice notice = data.get(position);
//                notice.onClick(position);
                mNoticeLisenter.onClick(position);
            }
        });
    }

    private void start(final AutoTextView vNotice) {
        //noinspection unchecked
        List<?> data = (List<?>) vNotice.getTag(R.id.notice_data);
        NextRunnable runnable = (NextRunnable) vNotice.getTag(R.id.notice_next);
        int position = (int) vNotice.getTag(R.id.notice_position);

        if (data != null && data.size() > 0) {
            if (data.size() > 1) {
                boolean isFirst = (boolean) vNotice.getTag(R.id.notice_flag);
                vNotice.setTag(R.id.notice_flag, false);
                if (isFirst) {
                    if (position > data.size() - 1 || position < 0) {
                        position = 0;
                    }
                    vNotice.setCurrentText(data.get(position).toString());
                } else {
                    position++;
                    if (position > data.size() - 1) {
                        position = 0;
                    }
                    vNotice.setText(data.get(position).toString());
                }
                vNotice.setTag(R.id.notice_position, position);
                vNotice.postDelayed(runnable, 4000);
            } else {
                vNotice.setCurrentText(data.get(0).toString());
                vNotice.setTag(data.get(0));
                vNotice.removeCallbacks(runnable);
            }
            ((View) vNotice.getParent()).setVisibility(View.VISIBLE);
        } else {
            ((View) vNotice.getParent()).setVisibility(View.GONE);
            if (runnable != null) {
                vNotice.removeCallbacks(runnable);
            }
        }
    }

    public void stopNotice(AutoTextView vNotice, boolean isClear) {
        if (vNotice == null) {
            return;
        }
        if (isClear) {
            ((View) vNotice.getParent()).setVisibility(View.GONE);
            vNotice.setTag(R.id.notice_data, null);
        }
        NextRunnable runnable = (NextRunnable) vNotice.getTag(R.id.notice_next);
        if (runnable != null) {
            vNotice.removeCallbacks(runnable);
        }
    }


    public interface INotice {
//        String getText();

        void onClick(int position);
    }

    private class NextRunnable implements Runnable {

        private final AutoTextView vNotice;

        NextRunnable(AutoTextView vNotice) {
            this.vNotice = vNotice;
        }

        @Override
        public void run() {
            start(vNotice);
        }
    }

}
