package com.raju.codechallange.network.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Country(
    val capital: String,
    val code: String,
    val currency: Currency,
    val flag: String,
    val language: Language,
    val name: String,
    val region: String
) : Parcelable

@Parcelize
data class Currency(
    val code: String,
    val name: String,
    val symbol: String?
) : Parcelable

@Parcelize
data class Language(
    val code: String?,
    val name: String
) : Parcelable
