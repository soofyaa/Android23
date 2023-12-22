package com.itis.android23.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.itis.android23.data.User

@Dao
interface UserDao {

    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    suspend fun getUserByEmailAndPassword(email: String, password: String): User?

    @Query("UPDATE users SET phone = :newPhone WHERE id = :userId")
    suspend fun updatePhoneNumber(userId: Long, newPhone: String)

    @Query("SELECT COUNT(*) FROM users WHERE phone = :phone")
    suspend fun getCountPhone(phone: String): Int

    @Query("SELECT * FROM users WHERE phone = :phone")
    suspend fun getUserByPhone(phone: String): User?

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUserById(userId: Long)

    @Query("UPDATE users SET password = :newPassword WHERE id = :userId")
    suspend fun updatePassword(userId: Long, newPassword: String)

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Long): User?

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?

}


