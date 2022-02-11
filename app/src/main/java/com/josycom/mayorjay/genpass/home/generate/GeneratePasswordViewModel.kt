package com.josycom.mayorjay.genpass.home.generate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.josycom.mayorjay.genpass.data.getPasswordCharacters
import com.josycom.mayorjay.genpass.data.getPasswordDisplayTexts
import com.josycom.mayorjay.genpass.util.Constants
import org.apache.commons.lang3.StringUtils
import java.security.SecureRandom

class GeneratePasswordViewModel : ViewModel() {

     val passwordType = MutableLiveData<String>()
     val passwordLength = MutableLiveData<String>()
     val password = MutableLiveData<String>()

    fun getPasswordTypes(): MutableList<String> = getPasswordDisplayTexts()

    fun validateInputs(): String {
        if (StringUtils.isBlank(passwordType.value) || StringUtils.equalsIgnoreCase(passwordType.value, Constants.SELECT_PASSWORD_TYPE)) {
            return Constants.SELECT_PASSWORD_TYPE
        }

        if (passwordLength.value == null) {
            return "Password Length cannot be blank"
        }

        if (passwordLength.value?.toInt() ?: 0 < 15 || passwordLength.value?.toInt() ?: 0 > 64) {
            return "Please input a value between 15 and 64"
        }
        return StringUtils.EMPTY
    }

    fun generatePassword(passwordType: String, passwordLength: Int): String {
        val buffer = StringBuffer()
        val characters = getPasswordCharacters(passwordType)
        val charactersLength = characters.length
        val secureRandom = SecureRandom()
        for (i in 0 until passwordLength) {
            val index = secureRandom.nextInt(charactersLength).toDouble()
            buffer.append(characters[index.toInt()])
        }
        return buffer.toString()
    }
}