package com.goindiainfotech.kotlinmvvm.themoviedb.ui.activities.main


import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.goindiainfotech.kotlinmvvm.themoviedb.data.model.ObjectMovies
import com.goindiainfotech.kotlinmvvm.themoviedb.data.model.ObjectTrailers
import com.goindiainfotech.kotlinmvvm.themoviedb.data.repository.MainRepository
import com.goindiainfotech.kotlinmvvm.themoviedb.utils.*
import com.goindiainfotech.kotlinmvvm.themoviedb.utils.ConstStrings.Companion.POPULAR_MOVIE
import kotlinx.coroutines.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class MainViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val parentJob = SupervisorJob()
    private val coroutineContext: CoroutineContext get() = parentJob + Dispatchers.IO
    private var scope = CoroutineScope(coroutineContext)

    private val responseMT = MutableLiveData<Resource<ObjectTrailers>>()
    val apiresponsMT: LiveData<Resource<ObjectTrailers>>
        get() = responseMT

    private val _movies = MutableLiveData<Resource<ObjectMovies>>()
    val movies: LiveData<Resource<ObjectMovies>>
        get() = _movies




    fun fetch(){
        fetchPopularMovies()
    }

    private fun fetchPopularMovies() {
        //if (networkHelper.isNetworkConnected()) {
        Log.d("data","fetchPopularMovies")
        scope.launch {
            //if (networkHelper.isNetworkConnected()) {
            try {
                Log.d("data", "fetchPopularMoviesv " + getParams(1))
                 mainRepository.getMoviesData(POPULAR_MOVIE, getParams(1)).let {
                     if (it.isSuccessful) {
                         _movies.postValue(Resource.success(it.body()))
                         Log.d("data", "fetchPopularMoviesv it.isSuccessful" )

                     } else{
                        //_users.postValue(Resource.error(it.errorBody().toString(), null))
                         Log.d("data", "fetchPopularMoviesv it.errorBody()")
                         _movies.postValue(Resource.error("Unknown Exception", null))

                     }

                 }

            } catch (e: ApiException) {
                Log.d("ApiException %s", e.message)
                _movies.postValue(Resource.error(e.message.toString(), null))
            } catch (e: NoInternetException) {
                Log.d("NoInternetException %s", e.message)
                _movies.postValue(Resource.error("No internet connection", null))
            } catch (e: CancellationException) {
                Log.d("Canceceptionx %s", e.message)
                _movies.postValue(Resource.error(e.message.toString(), null))
            } catch (e: Exception) {
                Log.d("Exception %s", e.message)
                _movies.postValue(Resource.error(e.message.toString(), null))
            }
            /*}else{
                errorResponse =  "No internet connection"

            }*/


        }


        /* }else{
             errorResponse =  "No internet connection"

         }*/


    }

    fun fetchMTrailer(url :String){
         Log.d("data","fetchMTrailer")
        scope.launch {
            //if (networkHelper.isNetworkConnected()) {
            try {
                Log.d("data", "fetchPopularMoviesv " + getParams(1))
                mainRepository.getTailer(url, getParams(1)).let {
                    if (it.isSuccessful) {
                        if(it.code()==200){
                            responseMT.postValue(Resource.success(it.body()))
                            Log.d("data", "fetchPopularMoviesv it.isSuccessful" )

                        }else{
                            responseMT.postValue(Resource.error("Unknown Exception "+ it.code().toString(), null))
                            Log.d("data", "fetchPopularMoviesv it.isSuccessful "+ it.code().toString() )

                        }


                    } else{
                        //_users.postValue(Resource.error(it.errorBody().toString(), null))
                        Log.d("data", "fetchPopularMoviesv it.errorBody()")


                        val error = it.errorBody()?.string()
                        Timber.d("SafeApiRequest %s", it.errorBody()?.string())
                        val message = StringBuilder()
                        error?.let {
                            try {
                                message.append(JSONObject(it).getString("message"))
                                Timber.d("SafeApiRequest %s", JSONObject(it).getString("message"))
                            } catch (e: JSONException) {
                            }
                            message.append("\n")

                        }

                        responseMT.postValue(Resource.error("Unknown Exception "+message.toString(), null))

                    }

                }

            } catch (e: ApiException) {
                Log.d("ApiException %s", e.message)
                responseMT.postValue(Resource.error(e.message.toString(), null))
            } catch (e: NoInternetException) {
                Log.d("NoInternetException %s", e.message)
                responseMT.postValue(Resource.error("No internet connection", null))
            } catch (e: CancellationException) {
                Log.d("Canceceptionx %s", e.message)
                responseMT.postValue(Resource.error(e.message.toString(), null))
            } catch (e: Exception) {
                Log.d("Exception %s", e.message)
                responseMT.postValue(Resource.error(e.message.toString(), null))
            }
            /*}else{
                errorResponse =  "No internet connection"

            }*/


        }


    }


    fun getParams(page: Int): MutableMap<String, String> {
        val params = mutableMapOf<String, String>()
        params[ConstStrings.API_KEY] = ConstFun.getApiKeyTmdb()
        params[ConstStrings.LANGUAGE] = ConstFun.getLocale()
        params[ConstStrings.PAGE] = page.toString()
        params[ConstStrings.REGION] = ConstFun.getCountry()
        return params
    }



}