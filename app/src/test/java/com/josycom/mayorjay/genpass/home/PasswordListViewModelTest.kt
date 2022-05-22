package com.josycom.mayorjay.genpass.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.josycom.mayorjay.genpass.home.view.PasswordListViewModel
import com.josycom.mayorjay.genpass.testdata.FakePreferenceManager
import junit.framework.TestCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Rule

class PasswordListViewModelTest : TestCase() {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var sut: PasswordListViewModel
    private var preferenceManager = FakePreferenceManager()
    private var testResult: String? = null

    override fun setUp() {
        sut = PasswordListViewModel(preferenceManager)
    }

    fun `test getPasswordPrefFlow pass_any_key_not_previously_saved null should be returned`() {
        getData("xyz")
        assertNull(testResult)
    }

    fun `test getPasswordPrefFlow pass_any_key_previously_saved value returned should noy be null`() {
        saveData("abc", "sdfghrdsfc")
        getData("abc")
        assertNotNull(testResult)
    }

    fun `test getPasswordPrefFlows pass_any_key_previously_saved a valid value should be returned`() {
        saveData("key", "password")
        getData("key")
        assertEquals("password", testResult)
    }

    private fun saveData(key: String, value: String) = runBlocking {
        preferenceManager.setStringPreference(key, value)
    }

    private fun getData(key: String) = runBlocking {
        testResult = sut.getPasswordPrefFlow(key).first()
    }

}