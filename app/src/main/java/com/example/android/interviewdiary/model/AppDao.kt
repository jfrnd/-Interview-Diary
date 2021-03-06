package com.example.android.interviewdiary.model

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

    /**
     * Probably needed for the back up functionality.
     */
    @RawQuery
    fun checkpoint(supportSQLiteQuery: SupportSQLiteQuery?): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTracker(tracker: Tracker)
    @Update
    suspend fun updateTracker(tracker: Tracker)
    @Delete
    suspend fun deleteTracker(tracker: Tracker)
    @Query("DELETE FROM tracker_table WHERE tracker_id = :trackerId")
    suspend fun deleteTracker(trackerId: Int)

    @Query("DELETE FROM tracker_table")
    suspend fun clearAllTrackers()


    @Query("SELECT * FROM tracker_table WHERE tracker_id = :trackerId")
    suspend fun getTracker(trackerId: Int): Tracker?

    @Query("SELECT * FROM tracker_table")
    suspend fun getAllTrackers(): List<Tracker>

    @Query("SELECT * FROM tracker_table WHERE tracker_id = :trackerId")
    fun streamTracker(trackerId: Int): Flow<Tracker?>
    @Query("SELECT * FROM tracker_table")
    fun streamAllTrackers(): Flow<List<Tracker>>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecord(record: Record)
    @Update
    suspend fun updateRecord(record: Record)
    @Delete
    suspend fun deleteRecord(record: Record)
    @Query("DELETE FROM record_table WHERE record_id = :recordId")
    suspend fun deleteRecord(recordId: Int)

    @Query("DELETE FROM record_table WHERE tracker_id = :trackerId")
    suspend fun clearRecords(trackerId: Int)
    @Query("DELETE FROM record_table")
    suspend fun clearAllRecords()

    @Query("SELECT * FROM record_table WHERE tracker_id = :trackerId AND date = :date ")
    suspend fun getRecord(trackerId: Int, date: LocalDate): Record?
    @Query("SELECT * FROM record_table WHERE record_id = :recordID")
    suspend fun getRecord(recordID: Int): Record?
    @Query("SELECT * FROM record_table WHERE tracker_id = :trackerId ORDER BY date DESC")
    suspend fun getAllRecords(trackerId: Int): List<Record>
    @Query("SELECT * FROM record_table ORDER BY date DESC")
    suspend fun getAllRecords(): List<Record>

    @Query("SELECT * FROM record_table WHERE tracker_id = :trackerId AND date = :date ")
    fun streamRecord(trackerId: Int?, date: LocalDate): Flow<Record?>
    @Query("SELECT * FROM record_table WHERE tracker_id = :trackerId ORDER BY date DESC")
    fun streamAllRecords(trackerId: Int): Flow<List<Record>>
    /**
     * Streams a list of records containing only the latest record of each trackerID (in the database), based on the date which was passed as a parameter.
     */
    @Query("SELECT * FROM (SELECT * FROM record_table WHERE date <= :date ORDER BY date ASC) GROUP BY tracker_id")
    fun streamLatestRecords(date: LocalDate): Flow<List<Record>>

}