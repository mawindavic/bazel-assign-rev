package com.example.bazel;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;

public class MainActivity extends AppCompatActivity {
  private MutableLiveData<Integer> image = new MutableLiveData<Integer>();
  private MutableLiveData<String> text = new MutableLiveData<String>();
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
     image.observe(this, mImage -> {
           ImageView imageView = findViewById(R.id.image_view);
           if(mImage != null) imageView.setImageDrawable(ContextCompat.getDrawable(this, mImage));
        });

        text.observe(this, mText -> {
            TextView textView = findViewById(R.id.text_view);
            if(mText != null) textView.setText(mText);
        });

        load();
  }

    void load() {
        image.setValue(R.drawable.bazel);
        text.setValue("Bazel");
    }

}