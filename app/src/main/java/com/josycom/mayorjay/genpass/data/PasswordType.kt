package com.josycom.mayorjay.genpass.data

import com.josycom.mayorjay.genpass.util.Constants
import org.apache.commons.lang3.StringUtils

enum class PasswordType(val displayText: String, val characters: String) {

    ALPHABET("Alphabets Only", "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"),
    ALPHANUMERIC("Alphanumeric Only", "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"),
    NUMERIC("Numbers Only", "1234567890"),
    ALPHANUMERIC_SYMBOL("Alphanumeric and Symbols", "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!\"\\#$%&()*+,./:;<=>?@[]^_|{}~")
}

fun getPasswordDisplayTexts(): MutableList<String> {
    val list = mutableListOf(Constants.PASSWORD_TYPE)
    for (item in PasswordType.values()) {
        list.add(item.displayText)
    }
    return list
}

fun getPasswordCharacters(displayText: String): String {
    for (item in PasswordType.values()) {
        if (StringUtils.equalsIgnoreCase(item.displayText, displayText)) {
            return item.characters
        }
    }
    return StringUtils.EMPTY
}