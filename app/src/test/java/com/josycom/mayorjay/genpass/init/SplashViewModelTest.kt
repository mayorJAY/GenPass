package com.josycom.mayorjay.genpass.init

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.josycom.mayorjay.genpass.init.splash.SplashViewModel
import com.josycom.mayorjay.genpass.testdata.FakePreferenceManager
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.Rule

class SplashViewModelTest : TestCase() {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var sut: SplashViewModel
    private var preferenceManager = FakePreferenceManager()

    override fun setUp() {
        sut = SplashViewModel(preferenceManager)
    }

    fun `test getLaunchPref passing_any_key isFirstLaunch LiveData is reassigned`() {
        sut.getLaunchPref("abc")
        assertNotNull(sut.isFirstLaunch)
    }

    fun `test getLaunchPref passing_any_wrong_key the value in isFirstLaunch LiveData should be null`() {
        saveData("ijk", false)
        sut.getLaunchPref("abc")
        assertNull(sut.isFirstLaunch?.value)
    }

    fun `test getLaunchPref passing_a_correct_key the value in isFirstLaunch LiveData should not be null`() {
        saveData("xyz", true)
        sut.getLaunchPref("xyz")
        assertTrue(sut.isFirstLaunch?.value ?: false)
    }

    private fun saveData(key: String, value: Boolean) = runBlocking {
        preferenceManager.setBooleanPreference(key, value)
    }
}