package com.lenovo.innovate.prince.view;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.lenovo.innovate.R;


public class ColoredToast extends Toast {

    @IntDef(value = {
            LENGTH_SHORT,
            LENGTH_LONG
    })
    @interface Duration {}

    private ColoredToast(Context context) {
        super(context);
    }

    public static class Maker {

        private Context mContext;
        private ColoredToast mToast;
        private View mToastView;
        private TextView mTextMessage;

        public Maker(Context context) {
            mContext = context;
            mToast = new ColoredToast(context);
            mToastView = LayoutInflater.from(context).inflate(R.layout.toast_colored, null);
            mTextMessage = mToastView.findViewById(R.id.toast_message);
        }

        /**
         * Set text color and background color for toast by resource id
         */
        public Maker setColor(@ColorRes int textColor, @ColorRes int backgroundColor) {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(mContext.getColor(backgroundColor));
            drawable.setCornerRadius(mTextMessage.getLayoutParams().height / 2);
            mToastView.setBackground(drawable);
            mTextMessage.setTextColor(mContext.getColor(textColor));
            return this;
        }

        /**
         * Set position
         * @see android.view.Gravity
         */
        public Maker setGravity(int gravity, int xOffset, int yOffset) {
            mToast.setGravity(gravity, xOffset, yOffset);
            return this;
        }

        public ColoredToast makeToast(@StringRes int resId, @Duration int duration) {
            mTextMessage.setText(resId);
            mToast.setView(mToastView);
            mToast.setDuration(duration);
            return mToast;
        }

        public ColoredToast makeToast(@NonNull String text, @Duration int duration,Context context) {
            try {
                if (context != null) {
                    Intent intent = new Intent("android.settings.ACCESSIBILITY_SETTINGS");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            mToast.setView(mToastView);
            mToast.setDuration(duration);
            return mToast;
        }

        public void activity(Context context){


        }



    }
}
