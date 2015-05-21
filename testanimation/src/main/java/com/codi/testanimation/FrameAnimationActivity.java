package com.codi.testanimation;

import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


public class FrameAnimationActivity extends ActionBarActivity {

    private ImageView mImageViewFilling, mImageViewEmptying, mImageViewSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_animation);

        mImageViewFilling = (ImageView) findViewById(R.id.imageview_animation_list_filling);
        mImageViewEmptying = (ImageView) findViewById(R.id.imageview_animation_list_emptying);
        mImageViewSelector = (ImageView) findViewById(R.id.imageview_animated_selector);

        mImageViewSelector.setActivated(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_frame_animation, menu);
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

    public void fillAnimation(View view) {
        ((AnimationDrawable) mImageViewFilling.getBackground()).stop();
        ((AnimationDrawable) mImageViewFilling.getBackground()).start();
    }

    public void emptyAnimation(View view) {
        ((AnimationDrawable) mImageViewEmptying.getBackground()).stop();
        ((AnimationDrawable) mImageViewEmptying.getBackground()).start();
    }

    public void selectorAnimation(View view) {
        mImageViewSelector.setActivated(!mImageViewSelector.isActivated());
    }
}
