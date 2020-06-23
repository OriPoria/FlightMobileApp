package com.example.flightmobileapp.overview

import android.content.Context
import androidx.room.*
import androidx.room.Room.databaseBuilder

//class that has only properties, that represents a table in sql lite
@Entity(tableName = "connectors")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    var url: String,
    var startTimeMilli: Long = System.currentTimeMillis()
)

@Dao
interface UsersDataDao {
    @Insert
    suspend fun insert(user: User)

    @Query("Select * from connectors ORDER BY startTimeMilli DESC LIMIT 5")
    suspend fun getTable() : List<User>


    @Query("SELECT * FROM connectors WHERE url LIKE :url")
    suspend fun findByUrl(url: String) : User

    @Query("DELETE FROM connectors WHERE url LIKE :url")
    suspend fun deleteByUrl(url: String)
}

@Database(entities = [User::class], version = 2, exportSchema = false)
abstract class UsersDataBase : RoomDatabase() {
    abstract val userDatabaseDao : UsersDataDao

    //Companion object == singleton object of the UsersDataBase
    companion object {
        @Volatile
        private var INSTANCE: UsersDataBase? = null
        fun getInstance(context: Context): UsersDataBase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(context, UsersDataBase::class.java, "Connectors")
                        .fallbackToDestructiveMigration().build()
                }
                INSTANCE = instance
                return instance
            }
        }
    }
}

