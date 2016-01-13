package com.hrules.charter;

public interface CharterAnimListener {
  void onAnimStart();

  void onAnimFinish();

  void onAnimCancel();

  void onAnimProgress(float progress);
}
