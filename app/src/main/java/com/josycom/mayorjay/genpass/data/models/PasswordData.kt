package com.josycom.mayorjay.genpass.data.models

import org.apache.commons.lang3.StringUtils

data class PasswordData(val key: String = StringUtils.EMPTY, val password: String, val timeGenerated: Long)
