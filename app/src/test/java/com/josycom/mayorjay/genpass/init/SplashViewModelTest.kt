package com.josycom.mayorjay.genpass.init

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.josycom.mayorjay.genpass.init.splash.SplashViewModel
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
class SplashViewModelTest : TestCase() {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var sut: SplashViewModel
    private var preferenceManager = FakePreferenceManager()
    private var testResult: Boolean? = null
    private val dispatcher = TestCoroutineDispatcher()

    override fun setUp() {
        Dispatchers.setMain(dispatcher)
        sut = SplashViewModel(preferenceManager)
    }

    override fun tearDown() {
        super.tearDown()
        Dispatchers.resetMain()
    }

    fun `test getLaunchPref passing_any_key the value returned should be null`() {
        getData("abc")
        assertNull(testResult)
    }

    fun `test getLaunchPref passing_any_wrong_key the value returned should be null`() {
        saveData("ijk", false)
        getData("key")
        assertNull(testResult)
    }

    fun `test getLaunchPref passing_a_correct_key the value returned should be correct`() {
        saveData("xyz", true)
        getData("xyz")
        assertTrue(testResult ?: false)
    }

    fun `test deletePreferences the data store should be empty`() {
        saveData("xyz", true)
        saveData("tea", false)
        saveData("bcd", true)
        sut.deletePreferences()
        assertTrue(preferenceManager.isEmpty())
    }

    private fun saveData(key: String, value: Boolean) = runBlocking {
        preferenceManager.setBooleanPreference(key, value)
    }

    private fun getData(key: String) = runBlocking {
        testResult = sut.getLaunchPref(key).first()
    }
}