package com.example.cinemahouse


import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.fragment_add__edit.*


private const val TAG = "AddEditFragment"

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_Movie = "movie"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [AddEditFragment.OnSaveClicked] interface
 * to handle interaction events.
 * Use the [AddEditFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class AddEditFragment : Fragment() {
    private var movie: Movie? = null
    private var listener: OnSaveClicked? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: starts")
        super.onCreate(savedInstanceState)
        movie = arguments?.getParcelable(ARG_Movie)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView: starts")

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add__edit, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated: called")
        if ( savedInstanceState == null) {
            val movie = movie
            if (movie !=null) {
                Log.d(TAG, "onViewCreated: Movie details found, editing movie ${movie.id}")
                addedit_name.setText(movie.name)
                addedit_genre.setText(movie.genre)
                addedit_rating.setText(Integer.toString(movie.rating))
            } else {
                // No Movie, so we must be addidng a new movie, and editing and existing one
                Log.d(TAG, "onViewCreated: No argument, adding new record")
            }
        }
    }

    private fun saveTask() {
        // Update the database if at least one field has changed.
        // - There's no need to hit the database unless this has happened.
        val rating = if (addedit_rating.text.isNotEmpty()) {
            Integer.parseInt(addedit_rating.text.toString())
        } else {
            0
        }

        val values = ContentValues()
        val movie = movie

        if (movie != null) {
            Log.d(TAG, "saveTask: updating existing movie")
            if (addedit_name.text.toString() != movie.name) {
                values.put(MoviesContract.Columns.MOVIE_NAME, addedit_name.text.toString())
            }
            if (addedit_genre.text.toString() != movie.genre) {
                values.put(MoviesContract.Columns.MOVIE_GENRE,
                    addedit_genre.text.toString())
            }
            if (rating != movie.rating) {
                values.put(MoviesContract.Columns.MOVIE_RATINGS, rating)
            }
            if (values.size() != 0) {
                Log.d(TAG, "saveTask: Updating movie")
                activity?.contentResolver?.update(MoviesContract.buildUriFromId(movie.id),
                    values, null, null)
            }
        } else {
            Log.d(TAG, "saveTask: adding new movie")
            if (addedit_name.text.isNotEmpty()) {
                values.put(MoviesContract.Columns.MOVIE_NAME, addedit_name.text.toString())
                if (addedit_genre.text.isNotEmpty()) {
                    values.put(MoviesContract.Columns.MOVIE_GENRE,
                        addedit_genre.text.toString())
                }
                values.put(MoviesContract.Columns.MOVIE_RATINGS, rating)  // defaults to zero if empty
                activity?.contentResolver?.insert(MoviesContract.CONTENT_URI, values)
            }
        }
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG, "onActivityCreated: starts")
        super.onActivityCreated(savedInstanceState)

        if (listener is AppCompatActivity) {
            val actionBar = (listener as AppCompatActivity)?.supportActionBar
            actionBar?.setDisplayHomeAsUpEnabled(true)
        }

        addedit_save.setOnClickListener {
            saveTask()
            listener?.onSaveClicked()
        }
    }

    override fun onAttach(context: Context) {
        Log.d(TAG, "onAttach: starts")
        super.onAttach(context)
        if (context is OnSaveClicked) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnSaveClicked")
        }
    }

    override fun onDetach() {
        Log.d(TAG, "onDetach: starts")
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnSaveClicked {
        fun onSaveClicked()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param movie The movie to be edited, or null to add a new movie.
         * @return A new instance of fragment AddEditFragment.
         */
        @JvmStatic
        fun newInstance(movie: Movie?) =
            AddEditFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_Movie, movie)
                }
            }
    }
}

//fun createFrag(task: Task) {
//    val args = Bundle()
//    args.putParcelable(ARG_TASK, task)
//    val fragment = AddEditFragment()
//    fragment.arguments = args
//}
//
//fun createFrag2(task: Task) {
//    val fragment = AddEditFragment().apply {
//        arguments = Bundle().apply {
//            putParcelable(ARG_TASK, task)
//        }
//    }
//}
//
//fun simpler(task: Task) {
//    val fragment = AddEditFragment.newInstance(task)
//}
