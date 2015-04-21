package com.codepath.shareyourtable.helpers;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by nsamant on 4/2/15.
 */
public class KeyboardHelper {
    public static void showSoftKeyboard(View view, Context context){
        if(view.requestFocus()){
            InputMethodManager imm =(InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view,InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static void hideSoftKeyboard(View view, Context context){
        InputMethodManager imm =(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
