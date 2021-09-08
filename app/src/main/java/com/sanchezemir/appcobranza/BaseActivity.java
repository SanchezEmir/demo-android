package com.sanchezemir.appcobranza;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    //clase abstracta
    private ProgressDialog progres;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progres = new ProgressDialog(this);

    }

    public void showProgress(String msg){
        progres.setCancelable(false);
        progres.setMessage(msg);
        progres.show();
    }

    public void hideProgress(){
        if(progres.isShowing()){
            progres.dismiss();
        }
    }

    //dialog
    public void showOKDialog(String dialogMsg){
        AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
        builder.setMessage(dialogMsg)
                .setTitle(R.string.app_name);

        AlertDialog dialog = builder.create();

        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", ((dialogInterface, i) -> {
            dialog.dismiss();
            finish();
        }));
        dialog.show();
    }

}
