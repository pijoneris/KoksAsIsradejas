package com.isradejas.mbproductions.koksairadjas

import android.app.Activity
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_engineer_profile.podcast_recycler
import kotlinx.android.synthetic.main.activity_engineer_profile.img_back_arrow
import kotlinx.android.synthetic.main.activity_engineer_profile.profile_image
import kotlinx.android.synthetic.main.activity_engineer_profile.profile_name
import kotlinx.android.synthetic.main.activity_engineer_profile.profile_description
import kotlinx.android.synthetic.main.activity_engineer_profile.progress_bar
import kotlinx.android.synthetic.main.activity_engineer_profile.history_toolbar
import kotlinx.android.synthetic.main.activity_engineer_profile.profile_layout
import org.json.JSONArray
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.view.Display
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Constraints
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView


class EngineerProfile : AppCompatActivity() {

    companion object {
     lateinit var progressTextview:TextView
    }

    val podcasts: ArrayList<Podcast> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_engineer_profile)



        progressTextview = findViewById<TextView>(R.id.tv_progress)
        val engineer = intent.extras.get("Engineer") as Engineer

        setProfile(engineer)
        setPodcasts(engineer.FullName)
        onClicks()
        setFonts()


        podcast_recycler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        podcast_recycler.adapter = PodcastRecyclerView(podcasts, this,progress_bar)

    }

    fun onClicks(){
        img_back_arrow.setOnClickListener {
            if(PodcastRecyclerView.player.isPlaying && PodcastRecyclerView.player!=null){
                PodcastRecyclerView.player.reset()
                PodcastRecyclerView.handler.removeCallbacks(PodcastRecyclerView.runnable)
            }

            onBackPressed()
        }

    }


    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        if(PodcastRecyclerView.player.isPlaying && PodcastRecyclerView.player!=null){
            PodcastRecyclerView.player.reset()
            PodcastRecyclerView.handler.removeCallbacks(PodcastRecyclerView.runnable)
        }
        super.onBackPressed()
    }

    fun setProfile(engineer : Engineer){
        profile_image.setImageDrawable(resources.getDrawable(engineer.PhotoResource))
        profile_name.setText(engineer.FullName)
        profile_description.setText(engineer.About)
    }

    fun setPodcasts(fullName : String){
        var jsonFile = Utils.loadJSONFromAsset("podcasts.json",this)
        var jsonArray = JSONArray(jsonFile)
        for(x in 0 until jsonArray.length()){
            var obj = jsonArray.getJSONObject(x)
            var name = obj.getString("name")
            if(name.equals(fullName)){
                var podArray = obj.getJSONArray("topics");
                for(y in 0 until podArray.length()){
                    var podcast = podArray.getJSONObject(y)
                    var name = podcast.getString("question")
                    var raw = podcast.getString("raw")
                    podcasts.add(Podcast(false, Utils.getResourceID(raw,"raw",this),name))
                }
            }
        }

    }

    fun setFonts(){
        val typeface = Typeface.createFromAsset(applicationContext.assets, "fonts/larseit.otf")
        profile_name.setTypeface(typeface)
        profile_description.setTypeface(typeface)
    }

}
