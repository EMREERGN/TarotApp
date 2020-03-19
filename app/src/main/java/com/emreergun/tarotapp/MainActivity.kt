package com.emreergun.tarotapp

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emreergun.tarotapp.adapter.TarotAdapter
import com.emreergun.tarotapp.helper.SharedPrefHelper
import com.emreergun.tarotapp.model.TarotModel
import kotlinx.android.synthetic.main.activity_main.*
import org.jsoup.Jsoup



class MainActivity : AppCompatActivity() {

    lateinit var firstRecyclerView:RecyclerView
    var tarotList=ArrayList<TarotModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firstRecyclerView=findViewById(R.id.firstRecyclerView)

        //TarotGetInfosAsyncTask(this).execute()

        tarotList=SharedPrefHelper.getTarotList(this)
        tarotList.shuffle()
        val tarotAdapter=TarotAdapter(tarotList)

        firstRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager=LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
            adapter=tarotAdapter
        }
        firstRecyclerView.apply {
            adapter?.notifyDataSetChanged()
        }



        var counter=0
        randomBtn.setOnClickListener {
            counter++
            stop=false
            val random=(0 until tarotList.size).random()
            val imageUrl=tarotList[random].imageUrl
            firstRecyclerView.smoothScrollToPosition(random)
            firstRecyclerView.addOnScrollListener(object :RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    when(newState){
                        RecyclerView.SCROLL_STATE_IDLE->{
                            if (!stop){
                                putImagesWithTarot(counter,imageUrl)
                                if (counter==3)
                                    counter=0
                                stop=true
                            }

                        }
                    }
                }
            })
        }

    }
    var stop=false
    private fun putImagesWithTarot(que:Int,imageUrl:String) {
        when(que){
            1->{
                setDefaultImages()
                Glide
                    .with(this)
                    .load(imageUrl)
                    .apply(RequestOptions().placeholder(R.drawable.tarotcover))
                    .into(image1)
            }
            2->{
                Glide
                    .with(this)
                    .load(imageUrl)
                    .apply(RequestOptions().placeholder(R.drawable.tarotcover))
                    .into(image2)
            }
            3->{

                Glide
                    .with(this)
                    .load(imageUrl)
                    .apply(RequestOptions().placeholder(R.drawable.tarotcover))
                    .into(image3)

            }
        }
    }

    private fun setDefaultImages() {

        Glide
            .with(this)
            .load(R.drawable.tarotcover)
            .apply(RequestOptions().placeholder(R.drawable.tarotcover))
            .into(image1)

        Glide
            .with(this)
            .load(R.drawable.tarotcover)
            .apply(RequestOptions().placeholder(R.drawable.tarotcover))
            .into(image2)

        Glide
            .with(this)
            .load(R.drawable.tarotcover)
            .apply(RequestOptions().placeholder(R.drawable.tarotcover))
            .into(image3)
    }
}


class TarotGetInfosAsyncTask(val context: Context) : AsyncTask<Void, Void, Void>() {

    val list = ArrayList<TarotModel>()

    companion object {
        const val TAG = "TarotGetInfosAsyncTask"
    }

    override fun onPreExecute() {
        super.onPreExecute()
        Log.i(TAG, "Query Task start----------------------")
    }

    override fun doInBackground(vararg params: Void?): Void? {

        val url = "https://lightseerstarot.com/light-seers-tarot-meanings-10-of-wands/"
        val doc = Jsoup.connect(url)
            .userAgent("Mozilla/5.0")
            .get()
        Log.i(TAG, "Title :" + doc.title())


        val elemets = doc.getElementsByClass("elementor-text-editor elementor-clearfix")


        elemets.forEach {
            it.select("p").forEach {
                val name = it
                    .select("a")
                    .text()

                val link = it
                    .select("a")
                    .attr("href")
                    .toString()
                if (name.isNotEmpty())
                {
                    Log.i(TAG, "name :$name")

                    val newDoc = Jsoup.connect(link)
                        .userAgent("Mozilla/5.0")
                        .timeout(10*1000)
                        .get()

                    val newElement=newDoc.getElementsByClass("attachment-full size-full")

                    var imageUrl=""
                    newElement
                        .forEach {
                            if (it.attr("width")=="413"){
                                try {
                                    imageUrl= it.attr("src")
                                }catch (ex:Exception){
                                    imageUrl= it.attr("srcset")
                                }
                                Log.i(TAG, "image Link :$imageUrl")
                            }
                        }
                    val tarotModel=TarotModel(name,imageUrl)
                    list.add(tarotModel)
                }
            }


        }


        return null
    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)
        Log.i(TAG, "Query Task stop----------------------")
        SharedPrefHelper.setTarotList(context,list)
    }




}
