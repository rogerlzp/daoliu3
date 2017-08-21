package com.wash.daoliu.utility;

import android.os.CountDownTimer;

/**
 * Created by rogerlzp on 15/12/22.
 */
public class TimeCounter  extends CountDownTimer {

    public TimeCounter(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onFinish() {// 计时完毕
        // TODO:

    }

    @Override
    public void onTick(long millisUntilFinished) {// 计时过程
        // TODO
    }
}
