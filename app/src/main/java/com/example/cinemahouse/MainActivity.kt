package com.example.cinemahouse

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

private const val TAG = "MainActivity"


class MainActivity : AppCompatActivity(), AddEditFragment.OnSaveClicked,
    MainActivityFragment.OnMovieEdit {

    // Whether or the activity is in 2-pane mode
    // i.e. running in landscape, or on a tablet.
    private var mTwoPane = false

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG,"onCreate: starts")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        mTwoPane = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        val fragment = supportFragmentManager.findFragmentById(R.id.movie_details_container)
        if(fragment != null) {
            // There was an existing fragment to edit a movie, make sure the panes are set correctly
            showEditPane()
        } else {
            movie_details_container.visibility = if(mTwoPane) View.VISIBLE else View.GONE
            mainFragment.view?.visibility = View.VISIBLE
        }
    }

    private fun showEditPane() {
        movie_details_container.visibility = View.VISIBLE
        // hide the left hand pane, if in single pane view
        mainFragment.view?.visibility = if(mTwoPane) View.VISIBLE else View.GONE

    }

    private fun removeEditPane(fragment: Fragment? = null) {
        Log.d(TAG, "removeEditPane called")
        if(fragment != null) {
            supportFragmentManager.beginTransaction()
                .remove(fragment)
                .commit()

        }

        // Set the visibility of the right hand pane
        movie_details_container.visibility = if(mTwoPane) View.INVISIBLE else View.GONE
        // and show the left hand pane
        mainFragment.view?.visibility = View.VISIBLE

        supportActionBar?.setDisplayHomeAsUpEnabled(false)

    }


    override fun onSaveClicked() {
        Log.d(TAG, "onSaveClicked: called")
        val fragment = supportFragmentManager.findFragmentById(R.id.movie_details_container)
        removeEditPane(fragment)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.menumain_addMovie -> movieEditRequest(null)
//            R.id.menumain_settings -> true
            android.R.id.home -> {
                Log.d(TAG,"onOptionsSelected: home button pressed")
                val fragment = supportFragmentManager.findFragmentById(R.id.movie_details_container)
                removeEditPane(fragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMovieEdit(movie: Movie) {
        movieEditRequest(movie)
    }


    private fun movieEditRequest(movie: Movie?) {
        Log.d(TAG, "movieEditRequest: starts")

        // Create a new fragment to edit the movie
        val newFragment = AddEditFragment.newInstance(movie)
        supportFragmentManager.beginTransaction()
            .replace(R.id.movie_details_container, newFragment)
            .commit()

        showEditPane()

        Log.d(TAG, "Exiting movieEditRequest")
    }



    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.movie_details_container)
        if (fragment == null || mTwoPane){
            super.onBackPressed()
        } else {
            removeEditPane(fragment)
        }

    }
}

