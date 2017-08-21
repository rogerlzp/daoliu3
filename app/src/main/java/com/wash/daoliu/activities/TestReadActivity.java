package com.wash.daoliu.activities;

import android.content.res.AssetManager;
import android.os.Bundle;

/**
 * Created by rogerlzp on 16/2/29.
 */
public class TestReadActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AssetManager am = this.getApplicationContext().getAssets();

        try {
            am.open("banklist.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
