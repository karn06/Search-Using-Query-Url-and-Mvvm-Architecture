package com.example.daytoday

import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.daytoday.model.Pages
import com.example.daytoday.model.Wiki
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar.*
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var model: MainViewModel
    var runnable: Runnable? = null
    val handler = Handler()
    var searchQuery = ""
    lateinit var clickListener: Adapter.onClickListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        initViewModel()
        clickListener()
    }

    fun initViewModel() {
        model = getViewModel()
        model.listLiveData.observe(this,
            Observer<Result<Wiki>> {
                when (it) {
                    is Result.Loading -> {
                        recyclerView.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
                        shimmerLayout.startShimmer()
                        shimmerLayout.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        shimmerLayout.stopShimmer()
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        shimmerLayout.visibility = View.GONE
                        if (it.data.query != null && it.data.query.pages.isNotEmpty()) {
                            noRecordFound.visibility = View.GONE
                            recyclerView.visibility = View.VISIBLE
                            setAdapter(it.data.query.pages)
                        } else {
                            noRecordFound.visibility = View.VISIBLE
                            recyclerView.visibility = View.GONE
                        }
                    }
                    is Result.Error -> {
                        recyclerView.visibility = View.GONE
                        shimmerLayout.visibility = View.GONE
                        shimmerLayout.stopShimmer();
                        progressBar.visibility = View.GONE
                        Toast.makeText(
                            applicationContext,
                            "Something Went Wrong!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
    }

    private fun clickListener() {
        clickListener = object : Adapter.onClickListener {
            override fun onClick(url: String) {
                openWebViewFragment(url)
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu.findItem(R.id.searchMenu)
        val searchView = MenuItemCompat.getActionView(searchItem) as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {


                runnable?.let { handler.removeCallbacks(it) }
                runnable = Runnable {
                    searchQuery = if (newText.trim().isNotEmpty()) {
                        newText.trim()
                    } else {
                        ""
                    }
                    callApi(searchQuery)
                }
                handler.postDelayed(runnable!!, 1000)
                return false
            }
        })

        searchView.setOnCloseListener {
            searchQuery = ""
            callApi(searchQuery)
            false
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun getViewModel(): MainViewModel {
        val factory = ViewModelFactory.getInstance(application)
        return ViewModelProviders.of(this, factory).get(MainViewModel::class.java)
    }

    private fun setAdapter(pages: ArrayList<Pages>) {
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        val adapter = Adapter()
        adapter.setData(applicationContext, pages, clickListener)
        recyclerView.adapter = adapter
    }

    private fun callApi(query: String) {
        model.callParseDataCall(query)
    }

    fun openWebViewFragment(url: String) {
        val webViewActivity = WebViewDialog(url)
        webViewActivity.show(supportFragmentManager, "WebViewActivity")
    }

    override fun onStart() {
        super.onStart()
        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
    }
}