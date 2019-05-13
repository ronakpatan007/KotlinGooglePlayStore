package com.pushpal.googleplayclone.activities

import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.pushpal.googleplayclone.R
import com.pushpal.googleplayclone.adapters.AppReviewsAdapter
import com.pushpal.googleplayclone.adapters.AppScreenshotsAdapter
import com.pushpal.googleplayclone.models.AppReviewItemModel
import java.util.*

class AppActivity : AppCompatActivity() {

    private val handler = Handler()
    private var appScreenshotsRecyclerView: RecyclerView? = null
    private var appReviewsRecyclerView: RecyclerView? = null
    private var progressStatus = 0
    private var downloadSize: TextView? = null
    private var downloadPercent: TextView? = null
    private var downloadProgress: ProgressBar? = null
    private var downloadLayout: RelativeLayout? = null
    private var openAppLayout: LinearLayout? = null
    private var installButton: AppCompatButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)

        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar.title = ""
        setSupportActionBar(toolbar)

        Objects.requireNonNull<ActionBar>(supportActionBar).setDisplayHomeAsUpEnabled(true)

        val profileImageView = findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.profile_image)
        Glide.with(this)
                .load(R.drawable.profile_image)
                .into(profileImageView)

        appScreenshotsRecyclerView = findViewById(R.id.rv_app_screenshots)
        val horizontalLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        appScreenshotsRecyclerView!!.layoutManager = horizontalLayoutManager
        appScreenshotsRecyclerView!!.setHasFixedSize(true)
        loadScreenshotsDataAndSetAdapter()

        appReviewsRecyclerView = findViewById(R.id.rv_app_reviews)
        val verticalLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        appReviewsRecyclerView!!.layoutManager = verticalLayoutManager
        appReviewsRecyclerView!!.setHasFixedSize(true)
        loadAppReviewsDataAndSetAdapter()

        downloadLayout = findViewById(R.id.rl_download)
        openAppLayout = findViewById(R.id.ll_open_app)
        installButton = findViewById(R.id.btn_install)
        downloadSize = findViewById(R.id.tv_download_size)
        downloadPercent = findViewById(R.id.tv_download_percent)
        downloadProgress = findViewById(R.id.pb_app_download)
        val openAppButton = findViewById<AppCompatButton>(R.id.btn_open)

        installButton!!.setOnClickListener { DownloadApp().execute("") }

        openAppButton.setOnClickListener {
            val pm = this@AppActivity.packageManager
            val launchIntent = pm.getLaunchIntentForPackage("com.udacity.android")
            if (launchIntent != null) {
                this@AppActivity.startActivity(launchIntent)
            } else {
                Toast.makeText(this@AppActivity, "You forgot this is a clone! Udacity App is not installed in your device!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun loadScreenshotsDataAndSetAdapter() {
        val screenshotImages = ArrayList<String>()
        screenshotImages.add("screenshot_1")
        screenshotImages.add("screenshot_2")
        screenshotImages.add("screenshot_3")
        screenshotImages.add("screenshot_4")
        screenshotImages.add("screenshot_5")
        screenshotImages.add("screenshot_6")

        appScreenshotsRecyclerView!!.adapter = AppScreenshotsAdapter(screenshotImages, this)
    }

    private fun loadAppReviewsDataAndSetAdapter() {
        val appReviews = ArrayList<AppReviewItemModel>()
        appReviews.add(AppReviewItemModel("John Butler", "This clone is one of the best and has good " +
                "ux/ui but it lacks on content downloads for pls add resume/pause support for the active " +
                "download", 4, "02/04/18", R.drawable.john_butler))

        appReviews.add(AppReviewItemModel("David Gilmour", "Great app! Loved it as " + "much as I loved Pink Floyd.", 5, "09/04/18", R.drawable.david_gilmour))

        appReviews.add(AppReviewItemModel("Steven Wilson", "", 5, "02/04/18", R.drawable.steven_wilson))

        appReviewsRecyclerView!!.adapter = AppReviewsAdapter(appReviews, this)
    }

    private inner class DownloadApp : AsyncTask<String, Void, String>() {

        override fun onPreExecute() {
            installButton!!.visibility = View.GONE
            openAppLayout!!.visibility = View.GONE
            downloadLayout!!.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: String): String {

            while (progressStatus < 100) {
                progressStatus += 1

                try {
                    Thread.sleep(25)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                handler.post {
                    downloadProgress!!.progress = progressStatus
                    downloadPercent!!.text = "$progressStatus %"
                    downloadSize!!.text = String.format("%.2f", progressStatus * (28.81 / 100)) + "MB/28.81MB"
                }
            }

            return ""
        }

        override fun onPostExecute(result: String) {
            downloadLayout!!.visibility = View.GONE
            openAppLayout!!.visibility = View.VISIBLE
        }
    }
}
