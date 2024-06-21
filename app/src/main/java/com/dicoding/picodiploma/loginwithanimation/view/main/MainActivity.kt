package com.dicoding.picodiploma.loginwithanimation.view.main

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.Api.ApiConfig
import com.dicoding.picodiploma.loginwithanimation.data.FileUploadResponse
import com.dicoding.picodiploma.loginwithanimation.data.GetAllStoryResponse
import com.dicoding.picodiploma.loginwithanimation.database.StoryDatabase
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityMainBinding
import com.dicoding.picodiploma.loginwithanimation.view.Story.AddStoryActivity
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.maps.MapsActivity
import com.dicoding.picodiploma.loginwithanimation.view.welcome.WelcomeActivity
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var usersList: GetAllStoryResponse
    private lateinit var token: String
    private lateinit var rvadapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.normal_type -> {
                    val intent = Intent(this, MapsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        val db = Room.databaseBuilder(
            applicationContext,
            StoryDatabase::class.java, "story_database"
        ).build()

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
            else {
                token = user.token
                getData()
            }
        }

        setupView()
        setupAction()
    }
   /* private fun requestLogin() {
        lifecycleScope.launch {
            showLoading(true)

            try {

                val apiService = ApiConfig.getApiService(token)
                Log.d("token validation",token)
                val successResponse = apiService.getStories(1,20)
                try {
                    usersList = successResponse
                    val storyList = usersList.listStory

                    binding.tvMain.apply {
                        rvadapter = Adapter(storyList)
                        layoutManager = LinearLayoutManager(this@MainActivity)
                        adapter = rvadapter
                        showLoading(false)

                    }


                } catch (e: Exception) {
                    Log.e("JSON", "Error parsing JSON: ${e.message}")
                }

            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Gson().fromJson(errorBody, FileUploadResponse::class.java)
            }
        }
    }

    */
    private fun getData() {
        val adapter = Adapter()
        binding.tvMain.adapter = adapter.withLoadStateFooter(
            footer = StateLoadingAdapter{
                adapter.retry()

            }
        )
        val userToken = "Bearer $token"
        Log.d("token validation",userToken)
        viewModel.story(userToken).observe(this, {
            adapter.submitData(lifecycle, it)

        })

    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.logoutButton.setOnClickListener {
            viewModel.logout()
        }

        binding.btnUpload.setOnClickListener{
            val intent = Intent(this,AddStoryActivity::class.java)
            intent.putExtra("token", token)
            startActivity(intent,
                ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity as Activity).toBundle())
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}