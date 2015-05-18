package com.codi.testjackson;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private final String USERNAME = "xiao";
    private final String PASSWORD = "nie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void parserUserBean1(View view) {
        UserBean1 userBean1 = new UserBean1();
        userBean1.setName(USERNAME);
        userBean1.setPassword(PASSWORD);
        String result = JacksonUtil.serialize(userBean1);
        Log.d(TAG, result);
        Log.d(TAG, JacksonUtil.readValue(result, UserBean1.class).toString());
    }

    public void parserUserBean(View view) {
        UserBean userBean = new UserBean();
        userBean.setName(USERNAME);
        userBean.setPassword(PASSWORD);
        String result = JacksonUtil.serialize(userBean);
        Log.d(TAG, result);
        Log.d(TAG, JacksonUtil.readValue(result, UserBean.class).toString());
    }

    public void parserUser(View view) {
        User user = new User();
        user.name = USERNAME;
        user.password = PASSWORD;
        String result = JacksonUtil.serialize(user);
        Log.d(TAG, result);
        Log.d(TAG, JacksonUtil.readValue(result, User.class).toString());
    }
}
