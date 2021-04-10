package com.example.daytoday

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.daytoday.databinding.ActivityMainBinding
import com.example.daytoday.model.Pages
import com.example.daytoday.model.Wiki
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    lateinit var model: MainViewModel
    var runnable: Runnable? = null
    val handler = Handler()
    var searchQuery = ""
    lateinit var clickListener: Adapter.onClickListener
    lateinit var alertDialog: AlertDialog
    lateinit var networkChangeReceiver: NetworkChangeReceiver
    lateinit var activityMainBinding: ActivityMainBinding
    lateinit var searchView: SearchView
    private lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = activityMainBinding.root
        setContentView(view)
        setSupportActionBar(toolbar)

        adapter = Adapter()
        alertDialog = AlertDialog.Builder(this).create()
        setNetworkReceiver()
        initViewModel()
        clickListener()
    }

    fun showInternetAlertDialog(activity: Activity) {

        alertDialog.setTitle("Info")
        alertDialog.setCancelable(false)
        alertDialog.setMessage("Internet not available, Cross check your internet connectivity and try again")
        alertDialog.setButton("Ok") { dialog, which ->
            activity.startActivity(Intent(Settings.ACTION_DATA_ROAMING_SETTINGS))
            dialog.dismiss()
        }

        alertDialog.show()
    }

    private fun setNetworkReceiver() {

        networkChangeReceiver = object : NetworkChangeReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (!FunctionUtil.isInternetOn(this@MainActivity)) {
                    showInternetAlertDialog(this@MainActivity)
                    return
                } else {
                    alertDialog.dismiss()
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(
                networkChangeReceiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
        }
    }

    override fun onPause() {
        super.onPause()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            unregisterReceiver(networkChangeReceiver)
        }
    }

    private fun initViewModel() {
        model = getViewModel()
        model.listLiveData.observe(this,
            Observer<Result<Wiki>> {
                when (it) {
                    is Result.Loading -> {
                        activityMainBinding.recyclerView.visibility = View.GONE
                        activityMainBinding.shimmerLayout.startShimmer()
                        activityMainBinding.shimmerLayout.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        activityMainBinding.shimmerLayout.stopShimmer()
                        activityMainBinding.recyclerView.visibility = View.VISIBLE
                        activityMainBinding.shimmerLayout.visibility = View.GONE
                        if (it.data.query != null && it.data.query.pages.isNotEmpty()) {
                            activityMainBinding.noRecordFound.visibility = View.GONE
                            activityMainBinding.recyclerView.visibility = View.VISIBLE
                            setAdapter(it.data.query.pages)
                        } else {
                            activityMainBinding.noRecordFound.visibility = View.VISIBLE
                            activityMainBinding.recyclerView.visibility = View.GONE
                        }
                    }
                    is Result.Error -> {
                        activityMainBinding.recyclerView.visibility = View.GONE
                        activityMainBinding.shimmerLayout.visibility = View.GONE
                        activityMainBinding.shimmerLayout.stopShimmer()
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
        searchView = MenuItemCompat.getActionView(searchItem) as SearchView
        searchView.queryHint = "Search"
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

                    if (searchQuery.isNotEmpty()) {
                        callApi(searchQuery)
                    }
                }
                handler.postDelayed(runnable!!, 1000)
                return false
            }
        })

        searchView.setOnCloseListener {
            searchQuery = ""
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
        activityMainBinding.recyclerView.layoutManager = layoutManager
        adapter.setData(applicationContext, pages, clickListener)
        activityMainBinding.recyclerView.adapter = adapter
    }


    private fun callApi(query: String) {
        model.callParseDataCall(query)
    }

    fun openWebViewFragment(url: String) {
        val webViewActivity = WebViewDialog(url)
        webViewActivity.show(supportFragmentManager, "WebViewActivity")
    }

    override fun onBackPressed() {
        searchView.isIconified = true
        super.onBackPressed()
    }
}