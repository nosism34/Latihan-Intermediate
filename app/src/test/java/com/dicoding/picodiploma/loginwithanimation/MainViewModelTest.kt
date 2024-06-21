package com.dicoding.picodiploma.loginwithanimation

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.dicoding.picodiploma.loginwithanimation.data.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.view.main.Adapter
import com.dicoding.picodiploma.loginwithanimation.view.main.MainViewModel
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.MockedStatic
import org.mockito.Mockito
import org.mockito.Mockito.mockStatic
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private var logMock: MockedStatic<Log>? = null

    @Before
    fun setup() {
        logMock = mockStatic(Log::class.java)
        logMock!!.`when`<Any> {
            Log.isLoggable(
                anyString(),
                anyInt()
            )
        }.thenReturn(true)
    }
    @Mock
    private lateinit var repository: UserRepository

    private lateinit var mainViewModel: MainViewModel
    private val successResponse = DataDummy.successResponse()
    private val errorResponse = DataDummy.errorResponse()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        mainViewModel = MainViewModel(repository)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()


    // Test dengan 2 skenario
    //1. Ketika berhasil memuat data cerita.
    //    - Memastikan data tidak null.
    //    - Memastikan jumlah data sesuai dengan yang diharapkan.
    //    - Memastikan data pertama yang dikembalikan sesuai.
    //2. Ketika tidak ada data cerita.
    //    - Memastikan jumlah data yang dikembalikan nol.

    @Test
    fun `scenario 1 get story success`() = runTest {
        val expectedResponse = MutableLiveData<PagingData<ListStoryItem>>()
        expectedResponse.value = PagingDataSource.snapshot(successResponse.listStory)
        Mockito.`when`(repository.getStory()).thenReturn(expectedResponse)

        val actualStory = mainViewModel.getStories().getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = Adapter.DIFF_CALLBACK,
            updateCallback = ListCallback(),
            mainDispatcher = mainDispatcherRule.testDispatcher,
            workerDispatcher = mainDispatcherRule.testDispatcher,
        )
        differ.submitData(actualStory)
        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(successResponse.listStory, differ.snapshot())
        Assert.assertEquals(successResponse.listStory.size, differ.snapshot().size)
        Assert.assertEquals(successResponse.listStory[0], differ.snapshot()[0])
    }

    @Test
    fun `scenario 2 get story empty response`() = runTest {
        val expectedResponse = MutableLiveData<PagingData<ListStoryItem>>()
        expectedResponse.value = PagingDataSource.snapshot(errorResponse.listStory)
        Mockito.`when`(repository.getStory()).thenReturn(expectedResponse)

        val actualStory = mainViewModel.getStories().getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = Adapter.DIFF_CALLBACK,
            updateCallback = ListCallback(),
            mainDispatcher = mainDispatcherRule.testDispatcher,
            workerDispatcher = mainDispatcherRule.testDispatcher,
        )
        differ.submitData(actualStory)
        Assert.assertEquals(errorResponse.listStory.size, differ.snapshot().size)
    }
    @After
    fun tearDown() {
        logMock!!.close()
    }
}