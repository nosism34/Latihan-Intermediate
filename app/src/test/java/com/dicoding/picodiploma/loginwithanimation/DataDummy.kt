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
                    photoUrl = "url_photo",
                    createdAt = "20/06/2023",
                    name = "Rohman",
                    description = "Submission untuk android intermediate",
                    lon = 107.908699,
                    id = "ID_Dummy",
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