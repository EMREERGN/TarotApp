package com.emreergun.tarotapp.helper

import android.content.Context
import android.util.Log
import com.emreergun.tarotapp.model.TarotModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPrefHelper {
    companion object{

        private val PREF_KEY="Movie_App_Pref_Tag"
        private val TAROT_LIST="Tarot_List"


        fun setTarotList(context: Context, tarotList: ArrayList<TarotModel>) {
            val sharedPref = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
            val gson=Gson()
            val json=gson.toJson(tarotList)
            Log.i("JSON_MY",json.toString())
            with (sharedPref.edit()) {
                putString(TAROT_LIST, json)
                commit()
            }


        }

        fun getTarotList(context: Context): ArrayList<TarotModel> {
            val sharedPref = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
            val stringJson=sharedPref.getString(TAROT_LIST,null)

            var tarotList=ArrayList<TarotModel>()
            if (stringJson!=null){
                val gson=Gson()
                tarotList=gson.fromJson(sharedPref.getString(TAROT_LIST,null),object :
                    TypeToken<ArrayList<TarotModel>>(){}.type ) as ArrayList<TarotModel>
            }


            return tarotList
        }


    }



}