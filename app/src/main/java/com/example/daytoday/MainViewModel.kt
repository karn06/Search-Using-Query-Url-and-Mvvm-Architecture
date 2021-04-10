package com.example.daytoday

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.daytoday.model.Wiki


class MainViewModel(application: Application, val repository: Repository) :
    AndroidViewModel(application) {

    private val listMutableLiveData = MutableLiveData<Result<Wiki>>()
    val listLiveData: LiveData<Result<Wiki>>
        get() = listMutableLiveData


    fun callParseDataCall(query: String) {
        repository.getList(getApplication(), query, object : ResponseHandler<Result<Wiki>> {
            override fun response(response: Result<Wiki>) {
                listMutableLiveData.value = response
            }
        })
    }

}