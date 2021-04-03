package com.example.project2.Interface;

public interface EditImage_Fragment_Listener
{
    void onBrightnessChanged(int brightness);
    void onSaturationChanged(float saturation);
    void onConstraintChanged(int constraint);

    void onEditStarted();
    void onEditCompleted();
}
