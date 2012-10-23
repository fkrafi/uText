package com.therap.javafest.utext;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class GalleryNoteActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_note);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_gallery_note, menu);
        return true;
    }
}
