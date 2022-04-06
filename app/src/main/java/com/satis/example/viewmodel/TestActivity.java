package com.satis.example.viewmodel;

import android.os.Bundle;
import android.util.Log;

import com.satis.core.component.FrameworkActivity;
import com.satis.core.component.mvvm.MVVMActivity;
import com.satis.example.R;
import com.satis.example.databinding.ActivityTestBinding;
import com.satis.viewmodel.annotation.Observe;
import com.satis.viewmodel.core.SatisViewModel;

public class TestActivity extends MVVMActivity<ActivityTestBinding,ViewModel> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SatisViewModel.INSTANCE.observe(this);
    }
    @Observe
    public void onText(String s){
        Log.e("sunshaobei","test"+s);
    }
}