package com.justice.noteapp

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Note(var noteData: String,
                var id: String):Parcelable {
    var category:Int = 0
constructor() : this("dafault constructor","dafault constructor")

}