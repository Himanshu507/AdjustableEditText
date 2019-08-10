package com.himanshu.adjustable_edittext;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;


public class AutoFitEditTextUtil {


    public static void setNormalization(final AppCompatActivity a, View rootView, final AutoFitEditText aText) {

        // if the view is not instance of AutoFitEditText
        // i.e. if the user taps outside of the box
        if (!(rootView instanceof AutoFitEditText)) {

            rootView.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(a);
                    if (aText.get_minTextSize() != null && aText.getTextSize() < aText.get_minTextSize()) {
                        // you can define your minSize, in this case is 50f
                        // trim all the new lines and set the text as it was
                        // before
                        aText.setText(aText.getText().toString().replace("\n", ""));


                    }
                    return false;
                }
            });
        }

        // If a layout container, iterate over children and seed recursion.
        if (rootView instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) rootView).getChildCount(); i++) {
                View innerView = ((ViewGroup) rootView).getChildAt(i);
                setNormalization(a, innerView, aText);
            }
        }
    }

    public static void hideSoftKeyboard(AppCompatActivity a) {
        InputMethodManager inputMethodManager = (InputMethodManager) a
                .getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        if (a.getCurrentFocus() != null
                && a.getCurrentFocus().getWindowToken() != null)
            inputMethodManager.hideSoftInputFromWindow(a.getCurrentFocus().getWindowToken(), 0);
    }
}