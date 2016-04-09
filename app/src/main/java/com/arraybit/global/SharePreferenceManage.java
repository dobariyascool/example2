package com.arraybit.global;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"UnnecessaryLocalVariable", "ConstantConditions"})
public class SharePreferenceManage {

    public void CreatePreference(String preferenceName,String name,String value,Context context)
    {
        SharedPreferences preference=context.getApplicationContext().getSharedPreferences(preferenceName,0);
        SharedPreferences.Editor editor=preference.edit();
        editor.putString(name,value);
        editor.apply();
    }

    public void RemovePreference(String CreatedPreferenceName,String removeName,Context context)
    {
        SharedPreferences preference=context.getApplicationContext().getSharedPreferences(CreatedPreferenceName,0);
        SharedPreferences.Editor editor=preference.edit();
        editor.remove(removeName);
        editor.apply();
    }

    public void ClearPreference(String CreatedPreferenceName,Context context)
    {
        SharedPreferences preference=context.getApplicationContext().getSharedPreferences(CreatedPreferenceName,0);
        SharedPreferences.Editor editor=preference.edit();
        editor.clear();
        editor.apply();
    }
    public String GetPreference(String CreatedPreferenceName,String name,Context context)
    {
        SharedPreferences preference=context.getApplicationContext().getSharedPreferences(CreatedPreferenceName,0);
        String editor=preference.getString(name, null);
        return editor;
    }

    public void CreateStringListPreference(String preferenceName,String name,ArrayList<String> alString,Context context){
        SharedPreferences preference = context.getApplicationContext().getSharedPreferences(preferenceName,0);
        Set<String> set = new HashSet<String>();
        set.addAll(alString);
        SharedPreferences.Editor editor=preference.edit();
        editor.putStringSet(name, set);
        editor.apply();
    }

    public ArrayList<String> GetStringListPreference(String CreatedPreferenceName,String name,Context context)
    {
        SharedPreferences preference=context.getApplicationContext().getSharedPreferences(CreatedPreferenceName, 0);
        if(preference.getStringSet(name,null)!=null) {
            ArrayList<String> alString = new ArrayList<String>(preference.getStringSet(name, null));
            return alString;
        }
        return null;
    }
}
