package com.dicoding.picodiploma.loginwithanimation

import com.dicoding.picodiploma.loginwithanimation.data.GetAllStoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.ListStoryItem

object DataDummy {

    fun successResponse(): GetAllStoryResponse {
        return GetAllStoryResponse(
            error = false,
            message = "success",
            listStory = arrayListOf(
                ListStoryItem(
                    photoUrl = "ini_adalah_photo_url_dummy",
                    createdAt = "20/06/2023",
                    name = "Rohim Kurniawan",
                    description = "Seubmission untuk android intermediate, tersu berusaha, terus semangat dan anda akan mendapatkan hasilnya!",
                    lon = 107.908699,
                    id = "ini_adalah_ID_dummy",
                    lat = -7.227906,
                )
            )
        )
    }
    fun errorResponse(): GetAllStoryResponse {
        val listStory = ArrayList<ListStoryItem>()
        val error = true
        val message = "Invalid content-type header: multipart missing boundary"
        return GetAllStoryResponse(listStory,error, message)
    }

}