package com.satis.example.viewmodel;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.satis.example.R;
import com.satis.viewmodel.annotation.Observe;
import com.satis.viewmodel.core.BaseActivity;
import com.satis.viewmodel.core.SatisViewModel;

public class TestActivity extends BaseActivity<ViewModel> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        SatisViewModel.INSTANCE.observe(this);
    }
    @Observe(tag = "sssss")
    public void onTest(String s){
        Log.e("sunshaobei","test"+s);
    }
}