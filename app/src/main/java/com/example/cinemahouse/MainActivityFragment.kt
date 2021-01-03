package com.example.cinemahouse

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_main.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainActivityFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

private const val TAG = "MainActivityFragment"

class MainActivityFragment : Fragment(),
CursorRecyclerViewAdapter.OnTaskClickListener {

    interface OnMovieEdit {
        fun onMovieEdit(movie: Movie)
    }


        private val viewModel by lazy {
            ViewModelProviders.of(activity!!).get(CinemaViewModel::class.java)
        }
        private val mAdapter = CursorRecyclerViewAdapter(null, this)

        // TODO: Rename and change types of parameters
        private var param1: String? = null
        private var param2: String? = null

    override fun onAttach(context: Context) {
        Log.d(TAG, "onAttach: called")
        super.onAttach(context)

        if (context !is OnMovieEdit) {
            throw RuntimeException("${context.toString()} must implement OnMovieEdit")
        }


    }

        override fun onCreate(savedInstanceState: Bundle?) {
            Log.d(TAG, "onCreate: called")
            super.onCreate(savedInstanceState)
            viewModel.cursor.observe(
                this,
                Observer { cursor -> mAdapter.swapCursor(cursor)?.close() })
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            Log.d(TAG, "onViewCreated: called")
            super.onViewCreated(view, savedInstanceState)

            movie_list.layoutManager = LinearLayoutManager(context)
            movie_list.adapter = mAdapter
        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_main, container, false)
        }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG, "onActivityCreated: called")
        super.onActivityCreated(savedInstanceState)
    }

    override fun onEditClick(movie: Movie) {
        (activity as OnMovieEdit?)?.onMovieEdit(movie)
    }

    override fun onDeleteClick(movie: Movie) {
        viewModel.deleteMovie(movie.id)
    }

    override fun onTaskLongClick(movie: Movie) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

        companion object {
            /**
             * Use this factory method to create a new instance of
             * this fragment using the provided parameters.
             *
             * @param param1 Parameter 1.
             * @param param2 Parameter 2.
             * @return A new instance of fragment MainActivityFragment.
             */
            // TODO: Rename and change types and number of parameters
            @JvmStatic
            fun newInstance(param1: String, param2: String) =
                MainActivityFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
        }
}