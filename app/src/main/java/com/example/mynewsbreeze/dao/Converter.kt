package com.example.mynewsbreeze.dao

import androidx.room.TypeConverter
import com.example.mynewsbreeze.model.Source


class Converter {

    @TypeConverter
    fun fromSource(source: Source): String? {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name+"random",name)
    }
}