
package com.lianshang.mvvm.ui.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.lianshang.mvvm.R;


public class AutoTextView extends TextSwitcher implements ViewSwitcher.ViewFactory {
    private final int mTextColor;
    private float mTextSize;
    private Context mContext;
    //mInUp,mOutUp分离构成向下翻页的进出动画
    private Animation mInUp;
    private Animation mOutUp;

    //mInDown,mOutDown分离构成向下翻页的进出动画
    private Animation mInDown;
    private Animation mOutDown;

    public AutoTextView(Context context) {
        this(context, null);
    }

    public AutoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AutoTextView);
        mTextSize = a.getDimensionPixelSize(R.styleable.AutoTextView_textSize, 15);
        mTextColor = a.getColor(R.styleable.AutoTextView_textColor, 0xFF333333);
        a.recycle();
        mContext = context;
        init();
    }

    private void init() {
        setFactory(this);
//		mInUp = createAnim(-90, 0, true, true);
//		mOutUp = createAnim(0, 90, false, true);
//		mInDown = createAnim(90, 0, true, false);
//		mOutDown = createAnim(0, -90, false, false);
        mInUp = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_from_bottom);
        mOutUp = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_to_top);
        mInDown = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_from_top);
        mOutDown = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_to_bottom);
        //TextSwitcher重要用于文件切换，比如 从文字A 切换到 文字 B，
        //setInAnimation()后，A将执行inAnimation，
        //setOutAnimation()后，B将执行OutAnimation
        setInAnimation(mInUp);
        setOutAnimation(mOutUp);
    }

    @SuppressWarnings("unused")
    private Rotate3dAnimation createAnim(float start, float end, boolean turnIn, boolean turnUp) {
        final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end, turnIn, turnUp);
        rotation.setDuration(800);
        rotation.setFillAfter(false);
        rotation.setInterpolator(new AccelerateInterpolator());
        return rotation;
    }

    //这里返回的TextView，就是我们看到的View
    @Override
    public View makeView() {
        TextView t = new TextView(mContext);
        t.setEllipsize(TextUtils.TruncateAt.END);
        t.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        t.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        t.setTextColor(mTextColor);
        t.setSingleLine();
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        t.setLayoutParams(params);
        return t;
    }

    //定义动作，向下滚动翻页
    public void previous() {
        if (getInAnimation() != mInDown) {
            setInAnimation(mInDown);
        }
        if (getOutAnimation() != mOutDown) {
            setOutAnimation(mOutDown);
        }
    }

    //定义动作，向上滚动翻页
    @SuppressWarnings("unused")
    public void next() {
        if (getInAnimation() != mInUp) {
            setInAnimation(mInUp);
        }
        if (getOutAnimation() != mOutUp) {
            setOutAnimation(mOutUp);
        }
    }

    class Rotate3dAnimation extends Animation {
        private final float mFromDegrees;
        private final float mToDegrees;
        private final boolean mTurnIn;
        private final boolean mTurnUp;
        private float mCenterX;
        private float mCenterY;
        private Camera mCamera;

        public Rotate3dAnimation(float fromDegrees, float toDegrees, boolean turnIn, boolean turnUp) {
            mFromDegrees = fromDegrees;
            mToDegrees = toDegrees;
            mTurnIn = turnIn;
            mTurnUp = turnUp;
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            mCamera = new Camera();
            mCenterY = getHeight() / 2;
            mCenterX = getWidth() / 2;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            final float fromDegrees = mFromDegrees;
            float degrees = fromDegrees + ((mToDegrees - fromDegrees) * interpolatedTime);
            final float centerX = mCenterX;
            final float centerY = mCenterY;
            final Camera camera = mCamera;
            final int direction = mTurnUp ? 1 : -1;
            final Matrix matrix = t.getMatrix();
            camera.save();
            if (mTurnIn) {
                camera.translate(0.0f, direction * mCenterY * (interpolatedTime - 1.0f), 0.0f);
            } else {
                camera.translate(0.0f, direction * mCenterY * (interpolatedTime), 0.0f);
            }
            camera.rotateX(degrees);
            camera.getMatrix(matrix);
            camera.restore();
            matrix.preTranslate(-centerX, -centerY);
            matrix.postTranslate(centerX, centerY);
        }
    }
}