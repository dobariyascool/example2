package com.arraybit.global;

import java.util.ArrayList;

public class SpinnerItem {

    String Text;
    String Id;
    int Value;

    public SpinnerItem() {
    }

    public SpinnerItem(String text, int value) {
        this.Text = text;
        this.Value = value;
    }

    public static int GetSpinnerItemIndex(ArrayList<SpinnerItem> lstSpinnerItem, int id) {
        int i = 0;
        for (; i < lstSpinnerItem.size(); i++) {
            if (lstSpinnerItem.get(i).getValue() == id) {
                break;
            }
        }
        return i;
    }

    public String getText() {
        return this.Text;
    }

    public void setText(String text) {
        this.Text = text;
    }

    public int getValue() {
        return this.Value;
    }

    public void setValue(int value) {
        this.Value = value;
    }
}
