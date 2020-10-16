package com.goindiainfotech.kotlinmvvm.fragments


import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.goindiainfotech.kotlinmvvm.R
import com.goindiainfotech.kotlinmvvm.adapter.ImageSlider
import com.goindiainfotech.kotlinmvvm.adapter.OfferAdapter
import com.goindiainfotech.kotlinmvvm.network.response.MainResponse
import com.goindiainfotech.kotlinmvvm.pojo.OfferPojo
import com.goindiainfotech.kotlinmvvm.viewmodel.PageViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_first.*
import java.util.*
import kotlin.collections.ArrayList


class FirstFragment : Fragment() {
    private var pageViewModel: PageViewModel? = null
    private var imageSlideradapter:ImageSlider?=null
    private var sliderlist:ArrayList<String>? = null
    private var dotscount = 0
    private lateinit var dots: Array<ImageView?>

    private var offerAdapter:OfferAdapter? = null
    private var offerPojoArray:ArrayList<OfferPojo>? = null
    private var lat:String = ""
    private var long:String = ""

    private var _hasLoadedOnce = false // your boolean field


    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        if (this.isVisible()) {
            if (menuVisible && !_hasLoadedOnce) {
                Toast.makeText(requireActivity(), "ff setMenuVisibility", Toast.LENGTH_LONG).show()
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
        sliderlist = ArrayList()
        offerPojoArray = ArrayList()
        imageSlideradapter = ImageSlider(requireActivity(), sliderlist!!)
        offerAdapter = OfferAdapter(requireActivity(),offerPojoArray!!)
        pageViewModel =  ViewModelProvider(requireActivity()).get(PageViewModel::class.java)


    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_first, container, false)

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSlider()
        subscribeObservers(view)

        view.findViewById<LinearLayoutCompat>(R.id.open_location).setOnClickListener {

                val location: Uri = Uri.parse("geo:$lat,$long?z=14") // z param is zoom level

                val mapIntent = Intent(Intent.ACTION_VIEW, location)
                try {
                    startActivity(Intent.createChooser(mapIntent, "Location"))
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(
                        requireActivity(),
                        "No App Available",
                        Toast.LENGTH_SHORT
                    ).show()

                }


        }


    }

    private fun initSlider(){
        viewPager!!.adapter = imageSlideradapter

        val linearLayoutManager = LinearLayoutManager(requireActivity())
        recycler_view.layoutManager = linearLayoutManager
        recycler_view.setHasFixedSize(true)
        recycler_view.isNestedScrollingEnabled = true

        recycler_view.adapter = offerAdapter

    }

    private fun SetOfferData(mainResponse: MainResponse){
        if(mainResponse.result!!.cupons!!.isNotEmpty()){
            for(x in mainResponse.result!!.cupons!!.indices){
                var offerPojo = OfferPojo(
                    mainResponse.result!!.cupons!![x].title!!,
                    mainResponse.result!!.cupons!![x].description!!,
                    mainResponse.result!!.cupons!![x].price!!
                    )
                offerPojoArray!!.add(offerPojo)

            }
            offerAdapter!!.notifyDataSetChanged()

        }

    }

    private fun subscribeObservers(view: View) {
        pageViewModel!!.getLiveData()!!.observe(requireActivity(), object : Observer<MainResponse?> {
           override fun onChanged(@Nullable mainResponse: MainResponse?) {
                if (mainResponse != null) {
                    if(mainResponse.statusCode==200){
                        sliderlist!!.clear()
                        offerPojoArray!!.clear()

                        Log.d("data", " fragment found..")
                        var gson = Gson()
                        var data = gson.toJson(mainResponse)
                        Log.d("data", "fragment found.."+data.toString())
                        pageViewModel!!.setMainData(mainResponse)

                        SetOfferData(mainResponse)
                        lat = mainResponse.result!!.latitudes!!
                        long = mainResponse.result!!.longitude!!

                        if(mainResponse.result!!.banner!!.isNotEmpty()){
                            dotscount = mainResponse.result!!.banner!!.size
                            dots = arrayOfNulls(dotscount)

                            for(x in mainResponse.result!!.banner!!.indices){
                                dots[x] = ImageView(activity)
                                dots[x]!!.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.nonactive_dot))
                                dots[x]!!.setPadding(10, 0, 10, 0)
                                val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                                params.setMargins(18, 0, 18, 0)
                                sliderDotspanel!!.addView(dots[x], params)


                                sliderlist!!.add(mainResponse!!.result!!.banner!![x])

                            }


                            dots[0]!!.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.active_dot))
                            viewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
                                override fun onPageSelected(position: Int) {
                                    for (i in 0 until dotscount) {
                                        dots[i]!!.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.nonactive_dot))
                                    }
                                    dots[position]!!.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.active_dot))
                                }

                                override fun onPageScrollStateChanged(state: Int) {}
                            })

                            imageSlideradapter!!.notifyDataSetChanged()

                            /*if(sliderlist!!.size>0){
                                val timer = Timer()
                                timer.scheduleAtFixedRate(MyTimerTask(), 2000, 4000)

                            }*/

                        }



                    }else{
                        Toast.makeText(activity,mainResponse.aPICODERESULT!!, Toast.LENGTH_LONG).show()

                    }

                }else{
                    Toast.makeText(activity,"fragment Blank Data Received",Toast.LENGTH_LONG).show()
                }
            }
        })
        pageViewModel!!.isRequestTimedOut().observe(requireActivity(), object : Observer<Boolean?> {

            override fun onChanged(t: Boolean?) {
                if (t!! && !pageViewModel!!.didRetrieveData()) {
                    Log.d("data", " fragment onChanged: timed out..")
                    //displayError
                }

            }
        })
    }

    inner class MyTimerTask : TimerTask() {
        override fun run() {

            try {
                activity!!.runOnUiThread {
                    if (viewPager!!.currentItem == 0) {
                        viewPager!!.currentItem = 1
                    } else {
                        viewPager!!.currentItem = 0
                    }
                }
            }catch (e:Exception){}


        }
    }

}
