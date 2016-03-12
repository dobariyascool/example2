package com.arraybit.global;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferenceManage {

    public void CreatePreference(String preferenceName,String name,String value,Context context)
    {
        SharedPreferences preference=context.getApplicationContext().getSharedPreferences(preferenceName,0);
        SharedPreferences.Editor editor=preference.edit();
        editor.putString(name,value);
        editor.commit();
    }

    public void RemovePreference(String CreatedPreferenceName,String removeName,Context context)
    {
        SharedPreferences preference=context.getApplicationContext().getSharedPreferences(CreatedPreferenceName,0);
        SharedPreferences.Editor editor=preference.edit();
        editor.remove(removeName);
        editor.commit();
    }

    public void ClearPreference(String CreatedPreferenceName,Context context)
    {
        SharedPreferences preference=context.getApplicationContext().getSharedPreferences(CreatedPreferenceName,0);
        SharedPreferences.Editor editor=preference.edit();
        editor.clear();
        editor.commit();
    }
    public String GetPreference(String CreatedPreferenceName,String name,Context context)
    {
        SharedPreferences preference=context.getApplicationContext().getSharedPreferences(CreatedPreferenceName,0);
        String editor=preference.getString(name, null);
        return editor;
    }

}
