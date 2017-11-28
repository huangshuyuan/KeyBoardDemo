package com.github.yoojia.keyboard;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.View;
import android.widget.TextView;

/**
 * 6位的弹出密码键盘
 * @author YOOJIA.CHEN (yoojia.chen@gmail.com)
 */
public class PasswordKeyboard extends AbstractKeyboard{

    private final KeyboardView mKeyboardView;
    private final Keyboard mNumberKeyboard;

    private final TextView[] mNumbersTextView = new TextView[6];

    private TextView mSelectedTextView;

    public PasswordKeyboard(Context context, OnKeyActionListener commitListener) {
        super(context, commitListener);
        final View contentView = putContentView(R.layout.keyboard_password);

        mNumbersTextView[0] = (TextView) contentView.findViewById(R.id.keyboard_number_0);
        mNumbersTextView[1] = (TextView) contentView.findViewById(R.id.keyboard_number_1);
        mNumbersTextView[2] = (TextView) contentView.findViewById(R.id.keyboard_number_2);
        mNumbersTextView[3] = (TextView) contentView.findViewById(R.id.keyboard_number_3);
        mNumbersTextView[4] = (TextView) contentView.findViewById(R.id.keyboard_number_4);
        mNumbersTextView[5] = (TextView) contentView.findViewById(R.id.keyboard_number_5);

        final View.OnClickListener listener = createNumberListener();
        for (TextView view : mNumbersTextView) {
            // 关闭点击声效
            view.setSoundEffectsEnabled(false);
            view.setOnClickListener(listener);
        }

        mNumberKeyboard = new Keyboard(context, R.xml.keyboard_numbers);
        mKeyboardView = (KeyboardView) contentView.findViewById(R.id.keyboard_view);
        mKeyboardView.setOnKeyboardActionListener(new OnKeyboardActionHandler() {
            @Override
            public void onKey(int charCode, int[] keyCodes) {
                mSelectedTextView.setText(Character.toString((char) charCode));
                nextNumber();
            }
        });
        mKeyboardView.setPreviewEnabled(false);// !!! Must be false
        mKeyboardView.setKeyboard(mNumberKeyboard);
    }

    @Override
    protected void onShow() {
        mNumbersTextView[0].performClick();
    }

    private void nextNumber(){
        final String number = getInput(mNumbersTextView);
        final int viewId = mSelectedTextView.getId();
        if (viewId == R.id.keyboard_number_0) {
            mOnKeyActionListener.onProcess(number);
            mNumbersTextView[1].performClick();
        } else if (viewId == R.id.keyboard_number_1) {
            mOnKeyActionListener.onProcess(number);
            mNumbersTextView[2].performClick();
        } else if (viewId == R.id.keyboard_number_2) {
            mOnKeyActionListener.onProcess(number);
            mNumbersTextView[3].performClick();
        } else if (viewId == R.id.keyboard_number_3) {
            mOnKeyActionListener.onProcess(number);
            mNumbersTextView[4].performClick();
        } else if (viewId == R.id.keyboard_number_4) {
            mOnKeyActionListener.onProcess(number);
            mNumbersTextView[5].performClick();
        } else if (viewId == R.id.keyboard_number_5) {
            // 输入最后一位密码，自动提交
            if (number.length() == mNumbersTextView.length){
                mOnKeyActionListener.onFinish(number);
                dismiss();
            }
        }
    }

    private View.OnClickListener createNumberListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectedTextView != null){
                    mSelectedTextView.setActivated(false);
                }
                mSelectedTextView = (TextView) view;
                mSelectedTextView.setActivated(true);
            }
        };
    }

    public static void show(Activity context, OnKeyActionListener listener) {
        View v= context.getWindow().getDecorView().getRootView();
        new PasswordKeyboard(context, listener).show(v);
    }

    public static PasswordKeyboard create(Context context, OnKeyActionListener listener) {
        return new PasswordKeyboard(context, listener);
    }
}
