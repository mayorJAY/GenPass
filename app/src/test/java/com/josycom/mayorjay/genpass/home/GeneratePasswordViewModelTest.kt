package com.josycom.mayorjay.genpass.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.josycom.mayorjay.genpass.data.getPasswordCharacters
import com.josycom.mayorjay.genpass.home.generate.GeneratePasswordViewModel
import com.josycom.mayorjay.genpass.testdata.FakePreferenceManager
import com.josycom.mayorjay.genpass.util.Constants
import com.josycom.mayorjay.genpass.util.getFormattedDate
import junit.framework.TestCase
import org.apache.commons.lang3.StringUtils
import org.junit.Rule
import java.util.Date

class GeneratePasswordViewModelTest : TestCase() {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var sut: GeneratePasswordViewModel
    private var preferenceManager = FakePreferenceManager()

    override fun setUp() {
        sut = GeneratePasswordViewModel(preferenceManager)
    }

    fun `test getPasswordTypes should return a non empty list of PasswordTypes`() {
        val result = sut.getPasswordTypes()
        assertTrue(result.isNotEmpty())
        assertEquals(5, result.size)
    }

    fun `test validateInputs no_password_type_passed should return an error message`() {
        val result = sut.validateInputs(null, "5")
        assertEquals(Constants.SELECT_PASSWORD_TYPE, result)
    }

    fun `test validateInputs wrong_password_length_passed should return an error message`() {
        val result = sut.validateInputs("Numbers only", "5")
        assertEquals(Constants.INPUT_VALID_PASSWORD_LENGTH, result)
    }

    fun `test validateInputs correct_inputs_captured should return an empty String`() {
        val result = sut.validateInputs("Alphabets only", "35")
        assertTrue(StringUtils.isBlank(result))
    }

    fun `test generatePassword correct_inputs_passed should return a valid String`() {
        val result = sut.generatePassword("Alphabets only", 20)
        assertTrue(StringUtils.isNotBlank(result))
    }

    fun `test generatePassword correct_inputs_passed should return a valid String with accurate length`() {
        val result = sut.generatePassword("Alphanumeric and Symbols", 50)
        assertEquals(50, result.length)
    }

    fun `test getPasswordCharacters should return an appropriate Password Characters`() {
        val result = getPasswordCharacters("Alphanumeric Only")
        assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890", result)
    }

    fun `test getFormattedDate valid_time_passed should return a non null Date String`() {
        val result = Date().time.getFormattedDate()
        assertTrue(StringUtils.isNotBlank(result))
    }

    fun `test getFormattedDates valid_time_passed should return a valid Date String in the desired format`() {
        val result = System.currentTimeMillis().getFormattedDate()
        val dateRegex = "[A-Za-z]{3}\\s[0-9]{2}[-][A-Za-z]{3}[-][0-9]{4}\\s[0-9]{2}[:][0-9]{2}[:][0-9]{2}".toRegex()
        assertTrue(dateRegex.matches(result ?: ""))
    }
}