package com.christmas.titlebarlayout;


/**
 * 自定义标题.
 */
public class CustomTitle {
  public static final float DEFAULT_TITLE_TEXT_SIZE = 15f;
  public static final int DEFAULT_NORMAL_TEXT_COLOR = android.R.color.white;

  public final String titleName;
  public final float titleTextSize;
  public final int titleFocusTextColor;
  public final int titleNormalTextColor;
  public final int titleFocusBackground;
  public final int titleNormalBackground;

  public static class Builder {
    private final String titleName;

    private float titleTextSize = DEFAULT_TITLE_TEXT_SIZE;
    private int titleNormalTextColor = DEFAULT_NORMAL_TEXT_COLOR;
    private int titleFocusTextColor = DEFAULT_NORMAL_TEXT_COLOR;
    private int titleFocusBackground = 0;
    private int titleNormalBackground = 0;

    public Builder(String titleName) {
      this.titleName = titleName;
    }

    public Builder titleTextSize(float titleTextSize) {
      this.titleTextSize = titleTextSize;
      return this;
    }

    public Builder titleFocusTextColor(int titleFocusTextColor) {
      this.titleFocusTextColor = titleFocusTextColor;
      return this;
    }

    public Builder titleNormalTextColor(int titleNormalTextColor) {
      this.titleNormalTextColor = titleNormalTextColor;
      return this;
    }

    public Builder titleFocusBackground(int titleFocusBackground) {
      this.titleFocusBackground = titleFocusBackground;
      return this;
    }

    public Builder titleNormalBackground(int titleNormalBackground) {
      this.titleNormalBackground = titleNormalBackground;
      return this;
    }

    public CustomTitle build() {
      return new CustomTitle(this);
    }
  }

  private CustomTitle(Builder builder) {
    this.titleName = builder.titleName;
    this.titleTextSize = builder.titleTextSize;
    this.titleFocusTextColor = builder.titleFocusTextColor;
    this.titleNormalTextColor = builder.titleNormalTextColor;
    this.titleFocusBackground = builder.titleFocusBackground;
    this.titleNormalBackground = builder.titleNormalBackground;
  }
}