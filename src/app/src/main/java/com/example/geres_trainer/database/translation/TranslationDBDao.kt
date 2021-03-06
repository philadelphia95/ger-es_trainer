package com.example.geres_trainer.database.translation

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.geres_trainer.database.translation.Translation


@Dao
interface TranslationDBDao {

    @Insert
    fun insert(translation : Translation)




    @Update
    fun update(translation: Translation)

    @Query("DELETE FROM word_translation_table")
    fun clear()

    @Query("Select * FROM word_translation_table WHERE translationID = :key")
    fun getTranslationByKey(key: Long) : LiveData<Translation>

    @Query("SELECT * FROM word_translation_table WHERE word_ger = :string")
    fun getTranslatioByGer(string: String) : Translation

    @Query("SELECT * FROM word_translation_table ORDER BY translationID DESC LIMIT 1")
    fun getMostRecentWord(): Translation

    @Query("SELECT * FROM word_translation_table WHERE word_ger = :string")
    fun getTranslationsBySearch(string: String): LiveData<List<Translation>>

    @Query("SELECT * from word_translation_table")
    fun getAllTranslations() : LiveData<List<Translation>>

    @Query("SELECT * from word_translation_table")
    fun getAllTranslationsNotLive() : List<Translation>

    @Query("SELECT * FROM word_translation_table WHERE translationID IN (:keys)")
    fun getTranslationsByKeys(keys: LongArray): LiveData<List<Translation>>


}