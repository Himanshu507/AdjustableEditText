package com.himanshu.adjustable_edittext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    AutoFitEditText mAutoFitEditText;
    ConstraintLayout mRootView;
    Button mbackground_change, mfont_change, next_btn;
    List<String> fonts;
    boolean first_time = false;
    private List<Integer> colorPickerColors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        initViews();

        initAutoFitEditText();
        create_color_array();
        change_Background();
    }

    private void initViews() {

        mAutoFitEditText = findViewById(R.id.text_here);
        mRootView = findViewById(R.id.root);
        mbackground_change = findViewById(R.id.background_change);
        mfont_change = findViewById(R.id.font_change);
        next_btn = findViewById(R.id.next_btn);

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bitmap bitmap = getBitmapFromView(mAutoFitEditText);
                saveImage(bitmap);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

            }
        });

        mbackground_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change_Background();
            }
        });

        mfont_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                font_Change();
            }
        });
    }

    private void font_Change() {
        Random r = new Random();
        int low = 0;
        int high = 4;
        int result = r.nextInt(high - low) + low;
        mAutoFitEditText.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), fonts.get(result)));
    }

    public void initAutoFitEditText() {
        mAutoFitEditText.setEnabled(true);
        mAutoFitEditText.setFocusableInTouchMode(true);
        mAutoFitEditText.setFocusable(true);
        mAutoFitEditText.setEnableSizeCache(false);
        //might cause crash on some devices
        mAutoFitEditText.setMovementMethod(null);
        // can be added after layout inflation;
        mAutoFitEditText.setMaxHeight(getScreenHeight());
        //don't forget to add min text size programmatically
        mAutoFitEditText.setMinTextSize(15f);

        AutoFitEditTextUtil.setNormalization(this, mRootView, mAutoFitEditText);
    }

    public void change_Background() {
        Random r = new Random();
        int low = 0;
        int high = 5;
        int result = r.nextInt(high - low) + low;
        mAutoFitEditText.setBackgroundColor(colorPickerColors.get(result));

    }

    public void create_color_array() {
        colorPickerColors = new ArrayList<>();
        colorPickerColors.add(ContextCompat.getColor(getApplicationContext(), R.color.colorGrayAlpha));
        colorPickerColors.add(ContextCompat.getColor(getApplicationContext(), R.color.colorBlue));
        colorPickerColors.add(ContextCompat.getColor(getApplicationContext(), R.color.colorSelectionBorder));
        colorPickerColors.add(ContextCompat.getColor(getApplicationContext(), R.color.colorWaveformBg));
        colorPickerColors.add(ContextCompat.getColor(getApplicationContext(), R.color.waveformSelected));
        colorPickerColors.add(ContextCompat.getColor(getApplicationContext(), R.color.colorTextGray));

        fonts = new ArrayList<>();
        fonts.add("pacifico.ttf");
        fonts.add("vacation.ttf");
        fonts.add("voice.ttf");
        fonts.add("montezregular.ttf");
        fonts.add("permanentmarker.ttf");
    }

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
        v.draw(c);
        return b;
    }

    public static Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    public static void saveImage(Bitmap bitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/.puc_text_image");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";
        File file = new File(myDir, fname);
        //  Log.i(TAG, "" + file);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}
