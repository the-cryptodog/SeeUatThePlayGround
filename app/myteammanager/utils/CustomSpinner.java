package com.app.myteammanager.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import android.util.AttributeSet;
import android.widget.Spinner;

@SuppressLint("AppCompatCustomView")

public class CustomSpinner extends Spinner {
    public boolean isDropDownMenuShown=false;//标志下拉列表是否正在显示

    public CustomSpinner(Context context) {
        super(context);
    }

    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void
    setSelection(int position, boolean animate) {
        boolean sameSelected = position == getSelectedItemPosition();
        super.setSelection(position, animate);
        if (sameSelected) {
            // 如果选择项是Spinner当前已选择的项,则 OnItemSelectedListener并不会触发,因此这里手动触发回调
            getOnItemSelectedListener().onItemSelected(this, getSelectedView(), position, getSelectedItemId());
        }
    }

    @Override
    public boolean performClick() {
        this.isDropDownMenuShown = true;
        return super.performClick();
    }

    public boolean isDropDownMenuShown(){
        return isDropDownMenuShown;
    }

    public void setDropDownMenuShown(boolean isDropDownMenuShown){
        this.isDropDownMenuShown=isDropDownMenuShown;
    }

    @Override
    public void
    setSelection(int position) {
        boolean sameSelected = position == getSelectedItemPosition();
        super.setSelection(position);
        if (sameSelected) {
            getOnItemSelectedListener().onItemSelected(this, getSelectedView(), position, getSelectedItemId());
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

}
