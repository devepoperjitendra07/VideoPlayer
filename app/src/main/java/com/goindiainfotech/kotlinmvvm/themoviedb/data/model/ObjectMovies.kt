package com.goindiainfotech.kotlinmvvm.themoviedb.data.model

import java.io.Serializable

data class ObjectMovies(
        var page: Int,
        var total_pages: Int,
        var results: MutableList<PojoResultsMovie>
) : Serializable

data class PojoResultsMovie(
        var vote_count: Int,
        var id: Int,
        var vote_average: Float,
        var title: String,
        var poster_path: String,
        var genre_ids: MutableList<Int>,
        var backdrop_path: String,
        var overview: String,
        var release_date: String
) : Serializable

data class PojoDetailsMovies(
        var backdrop_path: String,
        var genres: MutableList<ObjectListGenders>,
        var id: Int,
        var imdb_id: String,
        var overview: String,
        var release_date: String,
        var title: String,
        var vote_average: Float,
        var vote_count: Int,
        var status: String,
        var runtime: Int,
        var homepage: String,
        var budget: Long,
        var revenue: Long
) : Serializable

data class ObjectMoviePerson(val cast:MutableList<PojoResultsMovie>) : Serializable
