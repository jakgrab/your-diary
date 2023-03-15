package com.example.yourdiary.data.repository

import com.example.yourdiary.model.Diary
import com.example.yourdiary.util.RequestState
import io.realm.kotlin.types.ObjectId
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

typealias Diaries = RequestState<Map<LocalDate, List<Diary>>>
interface MongoRepository {
    fun configureTheRealm()

    fun getAllDiaries(): Flow<Diaries>

    fun getSelectedDiary(diaryId: ObjectId): RequestState<Diary>
}