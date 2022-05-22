package com.josycom.mayorjay.genpass.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.josycom.mayorjay.genpass.testdata.FakePreferenceManager
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Rule

@ExperimentalCoroutinesApi
class HomeViewModelTest : TestCase() {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var sut: HomeViewModel
    private var preferenceManager = FakePreferenceManager()
    private var testResult: Int? = null
    private val dispatcher = TestCoroutineDispatcher()

    override fun setUp() {
        Dispatchers.setMain(dispatcher)
        sut = HomeViewModel(preferenceManager)
    }

    override fun tearDown() {
        super.tearDown()
        Dispatchers.resetMain()
    }

    fun `test getAppOpenCountPref pass_any_key_not_previously_saved null should be returned`() {
        getData("xyz")
        assertNull(testResult)
    }

    fun `test saveAppOpenCounts when retrieved the value should be the correct value saved`() {
        saveData("key", 20)
        getData("key")
        assertEquals(20, testResult)
    }

    fun `test getAppOpenCountPref when retrieved the value should be the correct value saved`() {
        saveData("abc", 5)
        getData("abc")
        assertEquals(5, testResult)
    }

    private fun saveData(key: String, value: Int) = runBlocking {
        sut.saveAppOpenCounts(key, value)
    }

    private fun getData(key: String) = runBlocking {
        testResult = sut.getAppOpenCountPref(key).first()
    }
}