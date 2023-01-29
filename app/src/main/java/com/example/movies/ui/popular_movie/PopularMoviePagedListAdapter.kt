package com.example.movies.ui.popular_movie

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movies.data.api.POSTER_BASE_URL
import com.example.movies.data.repository.NetworkState
import com.example.movies.data.vo.Movie
import com.example.movies.databinding.MovieListItemBinding
import com.example.movies.databinding.NetworkStateItemBinding
import com.example.movies.ui.single_movie_details.SingleMovie

class PopularMoviePagedListAdapter(public val context: Context) : PagedListAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffCallback()) {

    val MOVIE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        if (viewType == MOVIE_VIEW_TYPE) {
            val listItemBinding = MovieListItemBinding.inflate(inflater, parent, false)
            return MovieItemViewHolder(listItemBinding)
        } else {
            val networkStateItemBinding = NetworkStateItemBinding.inflate(inflater, parent, false)
            return NetworkStateItemViewHolder(networkStateItemBinding)
        }

    }

    private fun hasExtraRow(): Boolean{
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }


    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else {
            MOVIE_VIEW_TYPE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == MOVIE_VIEW_TYPE) {
            (holder as MovieItemViewHolder).bind(getItem(position),context)
        }
        else {
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }    }

    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>(){

        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }


        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    class MovieItemViewHolder (val binding: MovieListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie?, context: Context) {
            binding.cvMovieTitle.text = movie?.title
            binding.cvMovieReleaseDate.text = movie?.releaseDate

            val moviePosterURL = POSTER_BASE_URL + movie?.posterPath
            Glide.with(itemView.context)
                .load(moviePosterURL)
                .into(binding.cvIvMoviePoster)

            itemView.setOnClickListener {
                val intent = Intent(context, SingleMovie::class.java)
                intent.putExtra("id", movie?.id)
                context.startActivity(intent)
            }

        }
    }

    class NetworkStateItemViewHolder (val binding: NetworkStateItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(networkState: NetworkState?) {
            if (networkState != null && networkState == NetworkState.LOADING) {
                binding.progressBarItem.visibility = View.VISIBLE;
            }
            else  {
                binding.progressBarItem.visibility = View.GONE;
            }

            if (networkState != null && networkState == NetworkState.ERROR) {
                binding.errorMsgItem.visibility = View.VISIBLE;
                binding.errorMsgItem.text = networkState.msg;
            }
            else if (networkState != null && networkState == NetworkState.ENDOFLIST) {
                binding.errorMsgItem.visibility = View.VISIBLE;
                binding.errorMsgItem.text = networkState.msg;
            }
            else {
                binding.errorMsgItem.visibility = View.GONE;
            }
        }
    }

    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {                             //hadExtraRow is true and hasExtraRow false
                notifyItemRemoved(super.getItemCount())    //remove the progressbar at the end
            } else {                                       //hasExtraRow is true and hadExtraRow false
                notifyItemInserted(super.getItemCount())   //add the progressbar at the end
            }
        } else if (hasExtraRow && previousState != newNetworkState) { //hasExtraRow is true and hadExtraRow true and (NetworkState.ERROR or NetworkState.ENDOFLIST)
            notifyItemChanged(itemCount - 1)       //add the network message at the end
        }
    }
}











