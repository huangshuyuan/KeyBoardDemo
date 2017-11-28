package com.github.yoojia.keyboard;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

/**
 * 中国民用
 *
 * @author yoojia.chen@gmail.com
 * @version version 2015-04-24
 * @since 1.0
 */
public class VehiclePlateKeyboard extends AbstractKeyboard {

    private static final int NUMBER_LENGTH = 7;

    public static final String WJ_PREFIX = "WJ";

    private static final String PROVINCE_CHINESE = "@京津晋冀蒙辽吉黑沪苏浙皖闽赣鲁豫鄂湘粤桂琼渝川贵云藏陕甘青宁新武";
    public static final  String EXTRA_CHINESE    = "@港澳警学挂";

    private final KeyboardView mKeyboardView;
    private final TextView[] mNumbersTextView = new TextView[NUMBER_LENGTH];

    private View mCommitButton;
    private int mShowingKeyboard = 0;
    private TextView mSelectedTextView;

    private Keyboard mProvinceKeyboard_1;
    private Keyboard mProvinceKeyboard_0;
    private Keyboard mCityCodeKeyboard;
    private Keyboard mNumberKeyboard;
    private Keyboard mNumberExtraKeyboard;

    private String mDefaultPlateNumber;
    Context context;

    public VehiclePlateKeyboard(Context context, OnKeyActionListener keyActionListener) {
        super(context, keyActionListener);
        context = context;

        final View contentView = putContentView(R.layout.keyboard_vehicle_plate);

        mNumbersTextView[0] = (TextView) contentView.findViewById(R.id.keyboard_number_0);
        mNumbersTextView[1] = (TextView) contentView.findViewById(R.id.keyboard_number_1);
        mNumbersTextView[2] = (TextView) contentView.findViewById(R.id.keyboard_number_2);
        mNumbersTextView[3] = (TextView) contentView.findViewById(R.id.keyboard_number_3);
        mNumbersTextView[4] = (TextView) contentView.findViewById(R.id.keyboard_number_4);
        mNumbersTextView[5] = (TextView) contentView.findViewById(R.id.keyboard_number_5);
        mNumbersTextView[6] = (TextView) contentView.findViewById(R.id.keyboard_number_6);

        final View.OnClickListener listener = createNumberListener();
        for (TextView view : mNumbersTextView) {
            view.setSoundEffectsEnabled(false);
            view.setOnClickListener(listener);
        }

        mProvinceKeyboard_1 = new Keyboard(context, R.xml.keyboard_vehicle_province_1);
        mProvinceKeyboard_0 = new Keyboard(context, R.xml.keyboard_vehicle_province_0);
        mCityCodeKeyboard = new Keyboard(context, R.xml.keyboard_vehicle_code);
        mNumberKeyboard = new Keyboard(context, R.xml.keyboard_vehicle_number);
        mNumberExtraKeyboard = new Keyboard(context, R.xml.keyboard_vehicle_number_extra);

        mKeyboardView = (KeyboardView) contentView.findViewById(R.id.keyboard_view);
        mKeyboardView.setOnKeyboardActionListener(new OnKeyboardActionHandler() {
            @Override
            public void onKey(int charCode, int[] keyCodes) {
                // 在键盘XML文件中，40x为省份文字使用的编码，50x为特定尾号文字的编码
                if (400 < charCode && charCode < 500) { // 400 See keyboard xml
                    charCode = PROVINCE_CHINESE.charAt(charCode - 400);
                } else if (500 < charCode) {
                    charCode = EXTRA_CHINESE.charAt(charCode - 500);
                }
                mSelectedTextView.setText(Character.toString((char) charCode));
                nextNumber();
            }
        });
        mKeyboardView.setPreviewEnabled(false);// !!! Must be false

        mCommitButton = contentView.findViewById(R.id.keyboard_commit);
        mCommitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String number = getInput(mNumbersTextView);
                if (number.length() == mNumbersTextView.length) {
                    mOnKeyActionListener.onFinish(number);
                    dismiss();
                }
            }
        });
    }

    public void setDefaultPlateNumber(String number) {
        if (!TextUtils.isEmpty(number)) {
            if (number.startsWith(WJ_PREFIX)) {
                mDefaultPlateNumber = "武" + number.substring(number.length() > 2 ? 2 : 0);
            } else {
                mDefaultPlateNumber = number;
            }
        }
    }

    @Override
    public void show(View anchorView) {
        if (!TextUtils.isEmpty(mDefaultPlateNumber)) {
            final char[] numbers = mDefaultPlateNumber.toUpperCase().toCharArray();
            final int limited = Math.min(NUMBER_LENGTH, numbers.length);
            for (int i = 0; i < limited; i++) {
                mNumbersTextView[i].setText(Character.toString(numbers[i]));
            }
        }
        super.show(anchorView);
    }

    @Override
    protected void onShow() {
        mNumbersTextView[0].performClick();
    }

    private void nextNumber() {
        final String number = getInput(mNumbersTextView);
        mOnKeyActionListener.onProcess(number);
        final int viewId = mSelectedTextView.getId();
        if (viewId == R.id.keyboard_number_0) {
            mNumbersTextView[1].performClick();
        } else if (viewId == R.id.keyboard_number_1) {
            mNumbersTextView[2].performClick();
        } else if (viewId == R.id.keyboard_number_2) {
            mNumbersTextView[3].performClick();
        } else if (viewId == R.id.keyboard_number_3) {
            mNumbersTextView[4].performClick();
        } else if (viewId == R.id.keyboard_number_4) {
            mNumbersTextView[5].performClick();
        } else if (viewId == R.id.keyboard_number_5) {
            mNumbersTextView[6].performClick();
        } else if (viewId == R.id.keyboard_number_6) {
            mCommitButton.performClick();
        }
    }

    private View.OnClickListener createNumberListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectedTextView != null) {
                    mSelectedTextView.setActivated(false);
                }
                mSelectedTextView = (TextView) view;
                mSelectedTextView.setActivated(true);
                int id = view.getId();
                if (id == R.id.keyboard_number_0) {
                    if (mShowingKeyboard != R.xml.keyboard_vehicle_province_1) {
                        mShowingKeyboard = R.xml.keyboard_vehicle_province_1;
                        mKeyboardView.setKeyboard(mProvinceKeyboard_1);
                    }
                } else if (id == R.id.keyboard_number_1) {
                    final String number = getInput(mNumbersTextView);
                    if (number.startsWith(WJ_PREFIX)) {
                        mShowingKeyboard = R.xml.keyboard_vehicle_province_0;
                        mKeyboardView.setKeyboard(mProvinceKeyboard_0);
                    } else {
                        if (mShowingKeyboard != R.xml.keyboard_vehicle_code) {
                            mShowingKeyboard = R.xml.keyboard_vehicle_code;
                            mKeyboardView.setKeyboard(mCityCodeKeyboard);
                        }
                    }
                } else if (id == R.id.keyboard_number_6) {
                    if (mShowingKeyboard != R.xml.keyboard_vehicle_number_extra) {
                        mShowingKeyboard = R.xml.keyboard_vehicle_number_extra;
                        mKeyboardView.setKeyboard(mNumberExtraKeyboard);
                    }
                } else {
                    if (mShowingKeyboard != R.xml.keyboard_vehicle_number) {
                        mShowingKeyboard = R.xml.keyboard_vehicle_number;
                        mKeyboardView.setKeyboard(mNumberKeyboard);
                    }
                }
                mKeyboardView.invalidateAllKeys();
                mKeyboardView.invalidate();
            }
        };
    }

    @Override
    protected String getInput(TextView[] inputs) {
        final String number = super.getInput(inputs);
        return number.replace("武", WJ_PREFIX);
    }

    public static void show(Activity activity, OnKeyActionListener listener) {
        new VehiclePlateKeyboard(activity, listener).show(activity.getWindow().getDecorView().getRootView());
    }

    public static VehiclePlateKeyboard create(Context context, OnKeyActionListener listener) {
        return new VehiclePlateKeyboard(context, listener);
    }

}
