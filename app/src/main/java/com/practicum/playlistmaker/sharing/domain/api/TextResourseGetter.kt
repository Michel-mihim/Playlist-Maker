package com.practicum.playlistmaker.sharing.domain.api

import com.practicum.playlistmaker.sharing.domain.model.EmailData


interface TextResourseGetter {
    fun getTermsLink(): String
    fun getShareAppLink(): String
    fun getSupportEmailData(): EmailData
}