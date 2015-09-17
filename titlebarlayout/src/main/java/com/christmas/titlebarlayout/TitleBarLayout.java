package com.christmas.titlebarlayout;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 所有Activity的标题栏
 */
public class TitleBarLayout extends LinearLayout implements View.OnClickListener {
  private LinearLayout llTitleBarTitle;
  private LinearLayout llTitleBarLeftIcon;
  private ImageView ivTitleBarMenuTips;
  private ImageView ivTitleBarLeftMainIcon;

  private static final int DEFAULT_TITLE_PADDING_TOP = 20;

  private Context context;
  private List<CustomTitle> customTitleList = new ArrayList<CustomTitle>();
  private TitleBarIconClickListener titleBarClickListener;
  private TitleBarTitleClickListener titleBarTitleOnClickListener;

  private boolean titleClickable = false;
  private boolean leftIconVisible = true;
  private int titleType = 0;
  private int titleOrientation = 0;
  private int leftMainIconResId = 0;
  private int leftSubIconResId = 0;
  private float leftMainIconScaleX = 1.0f;
  private float leftMainIconScaleY = 1.0f;

  private int widthPerTitle;
  private int paddingLeft = 0;
  private int paddingTop = 0;

  public TitleBarLayout(Context context) {
    super(context);
  }

  public TitleBarLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs, 0);
  }

  public TitleBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs, defStyleAttr);
  }

  private void init(Context context, AttributeSet attrs, int defStyleAttr) {
    this.context = context;
    LayoutInflater.from(context).inflate(R.layout.layout_title_bar, this);

    llTitleBarTitle = (LinearLayout) findViewById(R.id.ll_title_bar_title);
    llTitleBarLeftIcon = (LinearLayout) findViewById(R.id.ll_title_bar_left_icon);
    ivTitleBarMenuTips = (ImageView) findViewById(R.id.iv_title_bar_left_sub_icon);
    ivTitleBarLeftMainIcon = (ImageView) findViewById(R.id.iv_title_bar_left_main_icon);
    llTitleBarLeftIcon.setOnClickListener(this);

    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBarLayout);

    // 左边图片资源相关.
    leftIconVisible = typedArray.getBoolean(R.styleable.TitleBarLayout_leftIconVisible, leftIconVisible);
    if (!leftIconVisible) {
      llTitleBarLeftIcon.setVisibility(View.GONE);

    } else {
      llTitleBarLeftIcon.setVisibility(View.VISIBLE);
      leftMainIconResId = typedArray.getResourceId(R.styleable.TitleBarLayout_leftMainIcon, leftMainIconResId);
      leftMainIconScaleX = typedArray.getFloat(R.styleable.TitleBarLayout_leftMainIconScaleX, leftMainIconScaleX);
      leftMainIconScaleY = typedArray.getFloat(R.styleable.TitleBarLayout_leftMainIconScaleY, leftMainIconScaleY);
      setLeftMainIcon();

      leftSubIconResId = typedArray.getResourceId(R.styleable.TitleBarLayout_leftSubIcon, leftSubIconResId);
      setLeftSubIcon();
    }

    // 标题相关.
    titleType = typedArray.getInt(R.styleable.TitleBarLayout_titleType, titleType);
    String titles = typedArray.getString(R.styleable.TitleBarLayout_customTitle);
    titleOrientation = typedArray.getInt(R.styleable.TitleBarLayout_titleOrientation, titleOrientation);
    setTitles(titles);

    typedArray.recycle();
  }

  private void calculateWidthAndPaddingPerTitle() {
    WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    Display display = windowManager.getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);
    int screenWidth = size.x;
    int leftIconWidth;
    if (llTitleBarLeftIcon.getWidth() == 0) {
      leftIconWidth = screenWidth / (customTitleList.size() + 2);
    } else {
      leftIconWidth = llTitleBarLeftIcon.getWidth() + llTitleBarLeftIcon.getPaddingLeft();
    }
    int titleWidthTotal = screenWidth - leftIconWidth * 2 - getPaddingLeft() * 2;
    widthPerTitle = titleWidthTotal / customTitleList.size();
    paddingLeft = widthPerTitle / 10;
    if (getHeight() == 0) {
      paddingTop = DEFAULT_TITLE_PADDING_TOP;
    } else {
      paddingTop = getHeight() / 10;
    }
  }

  private void handleTitles() {
    llTitleBarTitle.removeAllViews();

    if (titleType == TitleType.SINGLE.ordinal()) {
      View titleItemView = LayoutInflater.from(context).inflate(R.layout.layout_tab_title_item, this, false);
      TextView tvTitleItem = (TextView) titleItemView.findViewById(R.id.tv_tab_title);
      tvTitleItem.setTextColor(getResources().getColor(android.R.color.white));
      tvTitleItem.setTextSize(customTitleList.get(0).titleTextSize);
      tvTitleItem.setText(customTitleList.get(0).titleName);

      FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
          LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
      tvTitleItem.setLayoutParams(layoutParams);
      tvTitleItem.setPadding(16, 10, 16, 10);

      llTitleBarTitle.addView(titleItemView);

    } else {
      FrameLayout.LayoutParams layoutParams;
      if (titleOrientation == TitleOrientation.HORIZONTAL.ordinal()) {
        llTitleBarTitle.setOrientation(HORIZONTAL);

        calculateWidthAndPaddingPerTitle();
        layoutParams = new FrameLayout.LayoutParams(
            widthPerTitle, LayoutParams.WRAP_CONTENT);
      } else {
        llTitleBarTitle.setOrientation(VERTICAL);
        layoutParams = new FrameLayout.LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
      }
      addTitles(layoutParams, paddingLeft, paddingTop, paddingLeft, paddingTop);
    }
  }

  private void addTitles(FrameLayout.LayoutParams layoutParams, int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
    for (int i = 0, titleItemCount = customTitleList.size(); i < titleItemCount; i++) {
      View titleItemView = LayoutInflater.from(context).inflate(R.layout.layout_tab_title_item, this, false);
      TextView tvTitleItem = (TextView) titleItemView.findViewById(R.id.tv_tab_title);
      tvTitleItem.setTextColor(getResources().getColor(customTitleList.get(i).titleNormalTextColor));
      tvTitleItem.setTextSize(customTitleList.get(i).titleTextSize);
      tvTitleItem.setText(customTitleList.get(i).titleName);

      tvTitleItem.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
      tvTitleItem.setLayoutParams(layoutParams);

      titleItemView.setId(i);
      if (titleClickable) {
        titleItemView.setOnClickListener(this);
      }

      llTitleBarTitle.addView(titleItemView);
    }
  }

  public void setTitleType(int titleType) {
    this.titleType = titleType;
  }

  public void setTitleOrientation(int titleOrientation) {
    this.titleOrientation = titleOrientation;
  }

  public void setTitles(List<CustomTitle> customTitleList) {
    this.customTitleList = customTitleList;
    handleTitles();
  }

  public void setTitles(String titles) {
    if (titles != null) {
      List<String> titleList = new ArrayList<String>();
      titleList.addAll(Arrays.asList(titles.split(",")));

      // Arrays.asList() cannot use list.clear().
      // http://stackoverflow.com/questions/5755477/java-list-add-unsupportedoperationexception
      customTitleList = new ArrayList<CustomTitle>();
      for (String title : titleList) {
        customTitleList.add(new CustomTitle.Builder(title).build());
      }
      handleTitles();
    }
  }

  private void setLeftSubIcon() {
    if (leftSubIconResId != 0) {
      ivTitleBarMenuTips.setVisibility(View.VISIBLE);
      ivTitleBarMenuTips.setImageResource(leftSubIconResId);
    }
  }

  private void setLeftMainIcon() {
    ivTitleBarLeftMainIcon.setImageResource(leftMainIconResId);
    ivTitleBarLeftMainIcon.setScaleX(leftMainIconScaleX);
    ivTitleBarLeftMainIcon.setScaleY(leftMainIconScaleY);
  }

  public void setLeftMainIcon(int leftMainIconResId) {
    ivTitleBarLeftMainIcon.setImageResource(leftMainIconResId);
  }

  public void setLeftMainIcon(String leftMainIconPath) {
    ivTitleBarLeftMainIcon.setBackgroundResource(0);
//    ImageLoader.getInstance().displayImage(leftMainIconPath, rivTitleBarLeftMainIcon);
  }

  public void changeTitleStatus(int position) {
    CustomTitle customTitle;
    TextView textView;
    for (int i = 0, titleItemCount = customTitleList.size(); i < titleItemCount; i++) {
      customTitle = customTitleList.get(i);
      textView = (TextView) llTitleBarTitle.getChildAt(i);
      if (i != position) {
        textView.setTextColor(getResources().getColor(customTitle.titleNormalTextColor));
        textView.setBackgroundResource(customTitle.titleNormalBackground);

      } else {
        textView.setTextColor(getResources().getColor(customTitle.titleFocusTextColor));
        textView.setBackgroundResource(customTitle.titleFocusBackground);

      }
    }
  }

  /**
   * 标题是否可以点击，必须在setTitles之前设定.
   *
   * @param titleClickable
   */
  public void setTitleClickable(boolean titleClickable) {
    this.titleClickable = titleClickable;
  }

  public void performTitleClick(int position) {
    if (position > -1 && position < customTitleList.size()) {
      llTitleBarTitle.getChildAt(position).performClick();
    }
  }

  @Override
  public void onClick(View view) {
    int viewId = view.getId();
    if (viewId == R.id.ll_title_bar_left_icon) {
      if (this.titleBarClickListener != null) {
        titleBarClickListener.titleBarIconClick();

      } else {
        ((Activity) this.context).onBackPressed();

      }
    } else {
      changeTitleStatus(viewId);
      if (this.titleBarTitleOnClickListener != null) {
        this.titleBarTitleOnClickListener.titleBarTitleClick(viewId);
      }
    }
  }

  public void setTitleBarTitleOnClickListener(TitleBarTitleClickListener titleBarTitleOnClickListener) {
    this.titleBarTitleOnClickListener = titleBarTitleOnClickListener;
  }

  public void setTitleBarIconOnClickListener(TitleBarIconClickListener titleBarClickListener) {
    this.titleBarClickListener = titleBarClickListener;
  }

  public interface TitleBarIconClickListener {
    void titleBarIconClick();
  }

  public interface TitleBarTitleClickListener {
    void titleBarTitleClick(int position);
  }

  public enum TitleOrientation {
    HORIZONTAL,
    VERTICAL,
  }

  public enum TitleType {
    SINGLE,
    MULTIPLE,
  }
}
