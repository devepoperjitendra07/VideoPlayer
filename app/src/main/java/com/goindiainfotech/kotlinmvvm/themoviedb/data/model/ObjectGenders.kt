package com.goindiainfotech.kotlinmvvm.themoviedb.data.model

import java.io.Serializable


data class ObjectGenders(val genres: MutableList<ObjectListGenders>) : Serializable

data class ObjectListGenders(val id: Int, val name: String) : Serializable