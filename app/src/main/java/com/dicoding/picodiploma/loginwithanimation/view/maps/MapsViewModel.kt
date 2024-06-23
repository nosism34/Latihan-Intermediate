package com.dicoding.picodiploma.loginwithanimation.view.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.GetAllStoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import kotlinx.coroutines.launch

class MapsViewModel(private val repository: UserRepository) : ViewModel() {
    private val _stories = MutableLiveData<GetAllStoryResponse>()
    val stories: LiveData<GetAllStoryResponse> = _stories
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getStoriesWithLocation(token: String) {
        viewModelScope.launch {
            try {
                val response = repository.getStoriesWithLocation(token)
                _stories.postValue(response)
            } catch (e: Exception) {
                Log.e("MapsViewModel", "Error fetching stories", e)
            }
        }
    }
}