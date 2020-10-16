package com.goindiainfotech.kotlinmvvm.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.goindiainfotech.kotlinmvvm.R
import com.goindiainfotech.kotlinmvvm.network.response.MainResponse
import com.goindiainfotech.kotlinmvvm.viewmodel.PageViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_details.*


class DetailsFragment : BaseFragment() {
    private var pageViewModel: PageViewModel? = null
    private var _hasLoadedOnce = false // your boolean field


    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        if (this.isVisible()) {
            if (menuVisible && !_hasLoadedOnce) {
                Toast.makeText(requireActivity(), "setMenuVisibility", Toast.LENGTH_LONG).show()
                _hasLoadedOnce = true
            }
            if (menuVisible) {
                if (_hasLoadedOnce) {
                    Toast.makeText(requireActivity(), "_hasLoadedOnce", Toast.LENGTH_LONG).show()
                }

            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(requireActivity()).get(PageViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObservers(view)


    }

    private fun subscribeObservers(view: View) {

        pageViewModel!!.mainRes.observe(requireActivity(),
            Observer<MainResponse?> { mainResponse ->
                if (mainResponse != null) {
                    Log.d("data", " fragment2 found..")
                    var gson = Gson()
                    var data = gson.toJson(mainResponse)
                    Log.d("data", "fragment2 found.."+data.toString())
                    if(mainResponse.statusCode==200){
                        val img_url = mainResponse.result!!.decriptionImage
                        loadImage(view.findViewById<AppCompatImageView>(R.id.des_image),img_url)
                        view.findViewById<TextView>(R.id.des_details).setText( mainResponse.result!!.descriptionBody)


                    }else{
                        Toast.makeText(activity,mainResponse.aPICODERESULT!!, Toast.LENGTH_LONG).show()

                    }

                }else{
                    Toast.makeText(activity,"fragment2 Blank Data Received", Toast.LENGTH_LONG).show()
                }
            })
        pageViewModel!!.isRequestTimedOut().observe(requireActivity(),
            Observer<Boolean?> { t ->
                if (t!! && !pageViewModel!!.didRetrieveData()) {
                    Log.d("data", " fragment2 onChanged: timed out..")
                    //displayError
                }
            })
    }


}
