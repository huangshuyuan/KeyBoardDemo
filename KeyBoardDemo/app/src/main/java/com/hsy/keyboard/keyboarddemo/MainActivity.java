package com.hsy.keyboard.keyboarddemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.yoojia.keyboard.OnKeyActionListener;
import com.github.yoojia.keyboard.VehiclePlateKeyboard;
import com.hsy.keyboard.keyboardlibrary.KeyboardTouchListener;
import com.hsy.keyboard.keyboardlibrary.KeyboardUtil;

public class MainActivity extends AppCompatActivity {

    EditText edittext1;
    private View viewContainer;
    TextView carEditText;//车牌号键盘
    EditText myKey;//自定义键盘

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /************************************车牌号*******************************************/
        carEditText = (TextView) findViewById(R.id.carEditText);
        carEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                VehiclePlateKeyboard keyboard = new VehiclePlateKeyboard(MainActivity.this, new OnKeyActionListener() {
                    @Override
                    public void onFinish(String input) {
                        // 输入车牌完成/点击“完成”后回调
                        carEditText.setText(input);
                    }

                    @Override
                    public void onProcess(String input) {
                        // 输入过程中回调，返回已输入的车牌号
                        carEditText.setText(input);
                    }
                });
                keyboard.setDefaultPlateNumber("粤123456");
                keyboard.show(MainActivity.this.getWindow().getDecorView().getRootView());


            }
        });
        /****************************************自定义1***************************************/
        rootView = (RelativeLayout) findViewById(R.id.root_view);

        normalEd = (EditText) findViewById(R.id.normal_ed);
        specialEd = (EditText) findViewById(R.id.special_ed);
        specialEd2 = (EditText) findViewById(R.id.special_ed2);
        initMoveKeyBoard();


    }

    private RelativeLayout rootView;
    private EditText     normalEd;
    private EditText     specialEd, specialEd2;
    private KeyboardUtil keyboardUtil;

    private void initMoveKeyBoard() {
        keyboardUtil = new KeyboardUtil(this);
        keyboardUtil.setOtherEdittext(normalEd);
        // monitor the KeyBarod state
        keyboardUtil.setKeyBoardStateChangeListener(new KeyBoardStateListener());
        // monitor the finish or next Key
        keyboardUtil.setInputOverListener(new inputOverListener());
        keyboardUtil.setTitleHint("请输入");
        //        specialEd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
        //            @Override
        //            public void onFocusChange(View v, boolean hasFocus) {
        //                if (hasFocus) {
        //                    keyboardUtil.showKeyBoardLayout((EditText) v, KeyboardUtil.INPUTTYPE_ABC, -1);
        //                }
        //            }
        //        });
        specialEd.setOnTouchListener(new KeyboardTouchListener(keyboardUtil, KeyboardUtil.INPUTTYPE_ABC, -1, true));
        specialEd2.setOnTouchListener(new KeyboardTouchListener(keyboardUtil, KeyboardUtil.INPUTTYPE_ABC, -1, true));
        //        keyboardUtil.toUpperCase();
    }

    class KeyBoardStateListener implements KeyboardUtil.KeyBoardStateChangeListener {

        @Override
        public void KeyBoardStateChange(int state, EditText editText) {
            //            System.out.println("state" + state);
            //            System.out.println("editText" + editText.getText().toString());
        }
    }

    class inputOverListener implements KeyboardUtil.InputFinishListener {

        @Override
        public void inputHasOver(int onclickType, EditText editText) {
            //            System.out.println("onclickType" + onclickType);
            //            System.out.println("editText" + editText.getText().toString());
        }
    }

}
