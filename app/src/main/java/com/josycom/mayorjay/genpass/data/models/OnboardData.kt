package com.josycom.mayorjay.genpass.data.models

import androidx.annotation.DrawableRes
import org.apache.commons.lang3.StringUtils

data class OnboardData(@DrawableRes val image: Int = 0,
                       val header: String = StringUtils.EMPTY,
                       val info: String = StringUtils.EMPTY
)