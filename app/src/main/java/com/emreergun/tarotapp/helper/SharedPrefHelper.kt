package com.emreergun.tarotapp.helper

import android.content.Context
import android.util.Log
import com.emreergun.tarotapp.R
import com.emreergun.tarotapp.model.TarotModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.*
import java.io.InputStreamReader as InputStreamReader1


class SharedPrefHelper {

    companion object{

        private val PREF_KEY="Movie_App_Pref_Tag"
        private val TAROT_LIST="Tarot_List"


        fun setTarotList(context: Context, tarotList: ArrayList<TarotModel>) {
            val sharedPref = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
            val gson=Gson()
            val json=gson.toJson(tarotList)
            Log.i("JSON_MY",json.toString())
            writeToFile(json,context)
            with (sharedPref.edit()) {
                putString(TAROT_LIST, json)
                commit()
            }


        }
        private fun writeToFile(data: String, context: Context) {
            try {
                val outputStreamWriter = OutputStreamWriter(context.openFileOutput("data2.json", Context.MODE_PRIVATE))
                outputStreamWriter.write(data)
                outputStreamWriter.close()
            } catch (e: IOException) {
                Log.e("Exception", "File write failed: $e")
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


        fun getRawJsonData(context: Context): ArrayList<TarotModel> {
            val inputStream: InputStream = context.resources.openRawResource(R.raw.data)
            val writer: Writer = StringWriter()
            val buffer = CharArray(1024)
            inputStream.use { inputStream ->
                val reader: Reader = BufferedReader(InputStreamReader1(inputStream, "UTF-8"))
                var n: Int
                while (reader.read(buffer).also { n = it } != -1) {
                    writer.write(buffer, 0, n)
                }
            }

            val jsonString: String = writer.toString()

            val gson=Gson()
            val tarotList=gson.fromJson(jsonString,object :
                TypeToken<ArrayList<TarotModel>>(){}.type ) as ArrayList<TarotModel>

            return tarotList
        }


    }



}