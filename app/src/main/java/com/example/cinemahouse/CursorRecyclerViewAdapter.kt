package com.example.cinemahouse

import androidx.recyclerview.widget.RecyclerView


import android.database.Cursor
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.task_list_item.*



class MovieViewHolder(override val containerView: View) :
    RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bind(movie: Movie, listener: CursorRecyclerViewAdapter.OnTaskClickListener) {
        tli_name.text = movie.name
        tli_description.text = movie.genre
        tli_edit.visibility = View.VISIBLE
        tli_delete.visibility = View.VISIBLE

        tli_edit.setOnClickListener {
            listener.onEditClick(movie)
        }

        tli_delete.setOnClickListener {
            listener.onDeleteClick(movie)
        }

        containerView.setOnLongClickListener {
            listener.onTaskLongClick(movie)
            true
        }
    }
}


private const val TAG = "CursorRecyclerViewAdapt"

class CursorRecyclerViewAdapter(private var cursor: Cursor?, private val listener: OnTaskClickListener) :
    RecyclerView.Adapter<MovieViewHolder>() {

    interface OnTaskClickListener {
        fun onEditClick(movie: Movie)
        fun onDeleteClick(task: Movie)
        fun onTaskLongClick(task: Movie)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        Log.d(TAG, "onCreateViewHolder: new view requested")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_list_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val cursor = cursor     // avoid problems with smart cast

        if(cursor == null || cursor.count == 0) {



            Log.d(TAG, "onBindViewHolder: providing instructions")
            holder.tli_name.setText(R.string.instructions_heading)
            holder.tli_description.setText(R.string.instructions)
            holder.tli_edit.visibility = View.GONE
            holder.tli_delete.visibility = View.GONE
        } else {
            if(!cursor.moveToPosition(position)) {
                throw IllegalStateException("Couldn't move cursor to position $position")
            }

            // Create a movie object from the data in the cursor
            val movie = Movie(
                cursor.getString(cursor.getColumnIndex(MoviesContract.Columns.MOVIE_NAME)),
                cursor.getString(cursor.getColumnIndex(MoviesContract.Columns.MOVIE_GENRE)),
                cursor.getInt(cursor.getColumnIndex(MoviesContract.Columns.MOVIE_GENRE)))
            // Remember that the id isn't set in the constructor
            movie.id = cursor.getLong(cursor.getColumnIndex(MoviesContract.Columns.ID))

            holder.bind(movie, listener)
        }
    }

    override fun getItemCount(): Int {

        val cursor = cursor
        val count = if (cursor == null || cursor.count == 0) {
            1   // fib, because we populate a single ViewHolder with instructions
        } else {
            cursor.count
        }

        return count
    }

    /**
     * Swap in a new Cursor, returning the old Cursor.
     * The returned old Cursor is *not* closed.
     *
     * @param newCursor The new cursor to be used
     * @return Returns the previously set Cursor, or null if there wasn't
     * one.
     * If the given new Cursor is the same instance as the previously set
     * Cursor, null is also returned.
     */
    fun swapCursor(newCursor: Cursor?): Cursor? {
        if (newCursor === cursor) {
            return null
        }
        val numItems = itemCount
        val oldCursor = cursor
        cursor = newCursor
        if (newCursor != null) {
            // notify the observers about the new cursor
            notifyDataSetChanged()
        } else {
            // notify the observers about the lack of a data set
            notifyItemRangeRemoved(0, numItems)
        }
        return oldCursor
    }
}

