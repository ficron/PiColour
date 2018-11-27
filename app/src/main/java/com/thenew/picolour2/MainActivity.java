package com.thenew.picolour2;

import android.app.Activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;

import java.util.ArrayList;


import petrov.kristiyan.colorpicker.ColorPicker;

public class MainActivity extends Activity implements Constants, SeekBar.OnSeekBarChangeListener {

    private ColorPickerView cp;
    private LinearLayout llborder;

    private Button btnsave;
    private Button btnload;
    private Button btnconn;
    private Button btnsend;

    private String borderRGBColor;
    private int currentCollor = 0;
    private float[] hsv;

    private SeekBar sbSaturation;
    private SeekBar sbValue;
    private TextView textLogo;
    private TextView textColorName;
    private EditText hsvText;
    private GradientDrawable gd;

    private ArrayList<Integer> colorsHexList;
    private ColorUtils cu;
    private MyBluetoothService MBl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initalViewsandComponents();
        initSeakBars();
        initLogo();
        initColorLibrary();
        initColorPicker();
        initButtons();
        changeColorState(Color.CYAN);
    }

    private void initalViewsandComponents() {
        Log.d(TAG, " 1.initalViews");
        llborder = (LinearLayout) findViewById(R.id.llborder);
        cu = new ColorUtils();
        MBl = new MyBluetoothService(getBaseContext());
        gd = new GradientDrawable();
        gd.setColor(Color.TRANSPARENT);
        gd.setCornerRadius(40);
    }


    private void initButtons() {
        Log.d(TAG, " 6. initButtons");
        btnsave = (Button) findViewById(R.id.btnsave);
        btnload = (Button) findViewById(R.id.btnload);
        btnconn = (Button) findViewById(R.id.btnconn);
        btnsend = (Button) findViewById(R.id.btnsend);

        View.OnClickListener oclBtnListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == btnconn.getId()) {
                    Toast.makeText(getBaseContext(), "CONN ", Toast.LENGTH_SHORT).show();
                    MBl.tryToDoConnection();

                } else if (v.getId() == btnload.getId()) {
                    Toast.makeText(getBaseContext(), "LOAD " + borderRGBColor, Toast.LENGTH_SHORT).show();

                    ColorPicker colorPicker = new ColorPicker(MainActivity.this);
                    colorPicker.setTitle("Saved Colors");
                    colorPicker.setColors(cu.convertIntegers(colorsHexList));
                    colorPicker.dismissDialog(); // dismiss dialog slowly
                    colorPicker.setColumns(4); // set columns number
                    colorPicker.setRoundColorButton(true);
                    colorPicker.setTitlePadding(10, 0, 0, 0);
                    colorPicker.setColorButtonTickColor(Color.DKGRAY);
                    colorPicker.show();

                    colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                        @Override
                        public void onChooseColor(int position, int color) {
                            changeColorState(color);
                        }

                        @Override
                        public void onCancel() {
                            // put code
                        }
                    });
                } else if (v.getId() == btnsave.getId()) {
                    Toast.makeText(getBaseContext(), "SAVE", Toast.LENGTH_SHORT).show();
                    colorsHexList.add(currentCollor);
                } else if (v.getId() == btnsend.getId()) {
                    Toast.makeText(getBaseContext(), "SEND", Toast.LENGTH_SHORT).show();
                    MBl.sendData(borderRGBColor + "\n");
                }
            }
        };

        btnsave.setOnClickListener(oclBtnListener);
        btnload.setOnClickListener(oclBtnListener);
        btnconn.setOnClickListener(oclBtnListener);
        btnsend.setOnClickListener(oclBtnListener);

    }

    private void initColorPicker() {
        Log.d(TAG, " 5. initColorPicker");
        cp = (ColorPickerView) findViewById(R.id.color_picker_view);
        cp.setInitialColor(Color.YELLOW, false);
        OnColorChangedListener changeColorListener = new OnColorChangedListener() {
            @Override
            public void onColorChanged(int i) {
                changeColorState(i);
            }
        };
        cp.addOnColorChangedListener(changeColorListener);
    }

    private void initSeakBars() {
        Log.d(TAG, " 2.initSeakBars");
        sbSaturation = (SeekBar) findViewById(R.id.sbSaturation);
        sbSaturation.setOnSeekBarChangeListener(this);

        sbValue = (SeekBar) findViewById(R.id.sbValue);

        sbValue.setOnSeekBarChangeListener(this);
        sbValue.setRotation(180);
    }


    private void initLogo() {
        Log.d(TAG, " 3. initLogo");
        textLogo = (TextView) findViewById(R.id.logotext);
        Typeface typeFaceLight = Typeface.createFromAsset(getAssets(), "fonts/orbitron-light.otf");
        textLogo.setTypeface(typeFaceLight);

        textColorName = (TextView) findViewById(R.id.colorname);
        textColorName.setTypeface(typeFaceLight);

        hsvText = (EditText) findViewById(R.id.editedText);
        hsvText.setTypeface(typeFaceLight);
    }

    private void initColorLibrary() {
        Log.d(TAG, " 4. initColorLibrary");
        colorsHexList = new ArrayList<>();
        colorsHexList.add(Color.RED);
        colorsHexList.add(Color.GREEN);
        colorsHexList.add(Color.WHITE);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Log.d(TAG,"seekBar "+progress);
        float mProgress;
        if (seekBar.getId() == sbSaturation.getId()) {
            mProgress = (float) progress / 100;
            Log.d(TAG,"SATURATION "+progress);
            hsv[1] = mProgress;
        } else if (seekBar.getId() == sbValue.getId()) {
            int reverceProgress = Math.abs(progress);
            Log.d(TAG,"VALUE "+progress);
            //if (reverceProgress>70)reverceProgress=70;
            mProgress = (float) reverceProgress / 100;

            hsv[2] = mProgress;
        }
        changeColorState(Color.HSVToColor(hsv));
    }


    private void changeColorState(int color) {
        llborder.setBackgroundColor(color);
        cp.setColor(color, false);
        currentCollor = color;
        borderRGBColor = cu.convertToHex(color);
        Log.d(TAG, "borderRGBColor: " + borderRGBColor + " color: " + currentCollor + " HEX: " + Integer.toHexString(color));
        hsv = cu.converterColorToHSV(color);

        textLogo.setTextColor(color);
        textColorName.setTextColor(color);
        textColorName.setText(cu.getColorNameFromHex(color));

        hsvText.setText(cu.getTextForView(color));
        hsvText.setTextColor(color);

        sbSaturation.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        sbSaturation.getThumb().setColorFilter(color, PorterDuff.Mode.SRC_IN);

        sbValue.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

        sbValue.getThumb().setColorFilter(color, PorterDuff.Mode.SRC_IN);

        btnconn.setTextColor(color);
        btnsave.setTextColor(color);
        btnload.setTextColor(color);
        btnsend.setTextColor(color);

        gd.setStroke(5, color);
        btnconn.setBackground(gd);
        btnsave.setBackground(gd);
        btnload.setBackground(gd);
        btnsend.setBackground(gd);

    }

    @Override
    public void onResume() {
        super.onResume();
        MBl.tryToDoConnection();

    }

    @Override
    public void onPause() {
        super.onPause();
        MBl.checkState();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

}

