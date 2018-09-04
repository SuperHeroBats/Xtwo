package com.xtwo.android.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

    public static Toast mToast;

    //长时间提醒
//    public static void showLongToast(Context context, String content) {
//        if (mToast == null) {
//            mToast = Toast.makeText(context, content, Toast.LENGTH_LONG);
//        }else {
//            mToast.setText(content);
//            mToast.setDuration(Toast.LENGTH_LONG);
//        }
//        mToast.show();
//    }

    //短时间提醒
    public static void showShortToast(String content) {
        if (mToast == null) {
            mToast = Toast.makeText(MyApplication.getContext(), content, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(content);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

}
