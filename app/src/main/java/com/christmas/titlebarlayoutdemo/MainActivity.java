package com.christmas.titlebarlayoutdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.christmas.titlebarlayout.CustomTitle;
import com.christmas.titlebarlayout.TitleBarLayout;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
    TitleBarLayout.TitleBarTitleClickListener,
    TitleBarLayout.TitleBarIconClickListener {
  private final List<CustomTitle> horizontalTitles = Arrays.asList(
      new CustomTitle.Builder("标题一").
          titleNormalTextColor(android.R.color.holo_green_light).
          titleFocusTextColor(android.R.color.white).
          titleNormalBackground(R.drawable.shape_left_round_rectangle).
          titleFocusBackground(R.drawable.shape_left_round_rectangle_solid).
          build(),

      new CustomTitle.Builder("标题2").
          titleNormalTextColor(android.R.color.holo_red_dark).
          titleFocusTextColor(android.R.color.white).
          titleNormalBackground(R.drawable.shape_hollow_rectangle).
          titleFocusBackground(R.drawable.shape_hollow_rectangle_solid).
          build(),

      new CustomTitle.Builder("标题3").
          titleNormalTextColor(android.R.color.holo_purple).
          titleFocusTextColor(android.R.color.white).
          titleNormalBackground(R.drawable.shape_right_round_rectangle).
          titleFocusBackground(R.drawable.shape_right_round_rectangle_solid).
          build()
  );

  private TitleBarLayout tbl1;
  private TitleBarLayout tbl2;
  private TitleBarLayout tbl3;
  private TitleBarLayout tbl4;
  private TitleBarLayout tbl5;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    initViews();

    fillTitles();
  }

  private void initViews() {
    tbl1 = (TitleBarLayout) findViewById(R.id.tbl_1);
    tbl2 = (TitleBarLayout) findViewById(R.id.tbl_2);
    tbl3 = (TitleBarLayout) findViewById(R.id.tbl_3);
    tbl4 = (TitleBarLayout) findViewById(R.id.tbl_4);
    tbl5 = (TitleBarLayout) findViewById(R.id.tbl_5);
  }

  private void fillTitles() {
    // tbl1.
    tbl1.setTitleType(1);
    tbl1.setTitleOrientation(0);
    tbl1.setTitleClickable(true);
    tbl1.setTitles(horizontalTitles);
    tbl1.setTitleBarTitleOnClickListener(this);
    tbl1.changeTitleStatus(0);

    // tbl2.
    tbl2.setTitleType(TitleBarLayout.TitleType.MULTIPLE.ordinal());
    tbl2.setTitleOrientation(TitleBarLayout.TitleOrientation.VERTICAL.ordinal());
    tbl2.setTitleClickable(false);
    tbl2.setTitles(Arrays.asList(
            new CustomTitle.Builder("标题一").
                titleNormalTextColor(android.R.color.black).
                build(),

            new CustomTitle.Builder("标题二").
                titleTextSize(10f).
                titleNormalTextColor(android.R.color.holo_green_dark).
                build())
    );

    // tbl4.
    tbl4.setTitleBarIconOnClickListener(this);
  }

  @Override
  public void titleBarTitleClick(int position) {
    Toast.makeText(this, "点击标题: " + position, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void titleBarIconClick() {
    Toast.makeText(this, "点击图标", Toast.LENGTH_SHORT).show();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
