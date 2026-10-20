package com.example.etsppblibraryreg.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "members")
data class MemberEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val namaLengkap: String,
    val nimNip: String,
    val programStudi: String,
    val alamat: String,
    val nomorTelepon: String,
    val email: String,
    val jenisKeanggotaan: String
)