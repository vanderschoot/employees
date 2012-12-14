package org.biz.employeesMR;

import org.apache.cordova.DroidGap;
import org.biz.employeesMR.R;

import android.os.Bundle;
import android.view.Menu;

public class Departments extends DroidGap {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_departments);
        super.loadUrl("file:///android_asset/www/index.html");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_departments, menu);
        return true;
    }
}
