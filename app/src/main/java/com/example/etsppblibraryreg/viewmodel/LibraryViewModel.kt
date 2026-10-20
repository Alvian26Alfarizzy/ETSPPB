package com.example.etsppblibraryreg.viewmodel

import androidx.lifecycle.ViewModel
import com.example.etsppblibraryreg.data.local.MemberEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LibraryViewModel : ViewModel() {

    private val _members = MutableStateFlow<List<MemberEntity>>(emptyList())
    val members: StateFlow<List<MemberEntity>> = _members.asStateFlow()

    init {
        // Pre-populate dummy data
        _members.value = listOf(
            MemberEntity(id = 1, namaLengkap = "Andi Pratama", nimNip = "20210101", programStudi = "Informatika", alamat = "Jl. Merdeka 1", nomorTelepon = "0811111", email = "andi@test.com", jenisKeanggotaan = "Mahasiswa"),
            MemberEntity(id = 2, namaLengkap = "Siti Aisyah", nimNip = "20210102", programStudi = "Sistem Informasi", alamat = "Jl. Merdeka 2", nomorTelepon = "0822222", email = "siti@test.com", jenisKeanggotaan = "Mahasiswa"),
            MemberEntity(id = 3, namaLengkap = "Budi Santoso", nimNip = "19851101", programStudi = "Informatika", alamat = "Jl. Veteran 3", nomorTelepon = "0833333", email = "budi@test.com", jenisKeanggotaan = "Dosen"),
            MemberEntity(id = 4, namaLengkap = "Rina Lestari", nimNip = "20210104", programStudi = "Informatika", alamat = "Jl. Melati 4", nomorTelepon = "0844444", email = "rina@test.com", jenisKeanggotaan = "Mahasiswa")
        )
    }

    suspend fun checkNimUnique(nim: String, currentId: Int?): Boolean {
        val existing = _members.value.find { it.nimNip == nim }
        return existing == null || existing.id == currentId
    }

    fun addMember(member: MemberEntity) {
        val newId = (_members.value.maxOfOrNull { it.id } ?: 0) + 1
        val newMember = member.copy(id = newId)
        _members.update { it + newMember }
    }

    fun updateMember(member: MemberEntity) {
        _members.update { list ->
            list.map { if (it.id == member.id) member else it }
        }
    }

    fun deleteMember(member: MemberEntity) {
        _members.update { list ->
            list.filter { it.id != member.id }
        }
    }
}