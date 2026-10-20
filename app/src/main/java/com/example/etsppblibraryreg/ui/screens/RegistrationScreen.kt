package com.example.etsppblibraryreg.ui.screens

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.etsppblibraryreg.data.local.MemberEntity
import com.example.etsppblibraryreg.viewmodel.LibraryViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    viewModel: LibraryViewModel,
    memberIdToEdit: Int? = null,
    onNavigateBack: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val members by viewModel.members.collectAsState()
    val memberToEdit = members.find { it.id == memberIdToEdit }
    val isEditMode = memberToEdit != null

    var namaLengkap by remember { mutableStateOf(memberToEdit?.namaLengkap ?: "") }
    var nimNip by remember { mutableStateOf(memberToEdit?.nimNip ?: "") }
    var programStudi by remember { mutableStateOf(memberToEdit?.programStudi ?: "") }
    var alamat by remember { mutableStateOf(memberToEdit?.alamat ?: "") }
    var nomorTelepon by remember { mutableStateOf(memberToEdit?.nomorTelepon ?: "") }
    var email by remember { mutableStateOf(memberToEdit?.email ?: "") }
    var jenisKeanggotaan by remember { mutableStateOf(memberToEdit?.jenisKeanggotaan ?: "Mahasiswa") }

    val membershipOptions = listOf("Mahasiswa", "Dosen", "Staf", "Umum")
    var expandedDropdown by remember { mutableStateOf(false) }

    // Validation States
    var generalError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var nimError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Edit Data Anggota" else "Formulir Pendaftaran") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (generalError != null) {
                Text(text = generalError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            OutlinedTextField(
                value = namaLengkap,
                onValueChange = { namaLengkap = it; generalError = null },
                label = { Text("Nama Lengkap *") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = nimNip,
                onValueChange = { nimNip = it; nimError = null },
                label = { Text("NIM / NIP / Nomor Induk *") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = nimError != null,
                supportingText = { nimError?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = expandedDropdown,
                onExpandedChange = { expandedDropdown = !expandedDropdown }
            ) {
                OutlinedTextField(
                    value = jenisKeanggotaan,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Jenis Keanggotaan") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedDropdown,
                    onDismissRequest = { expandedDropdown = false }
                ) {
                    membershipOptions.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type) },
                            onClick = {
                                jenisKeanggotaan = type
                                expandedDropdown = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = programStudi,
                onValueChange = { programStudi = it; generalError = null },
                label = { Text("Program Studi / Unit *") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it; emailError = null },
                label = { Text("Email *") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = emailError != null,
                supportingText = { emailError?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = nomorTelepon,
                onValueChange = { nomorTelepon = it; generalError = null },
                label = { Text("Nomor Telepon *") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = alamat,
                onValueChange = { alamat = it; generalError = null },
                label = { Text("Alamat *") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        generalError = null
                        emailError = null
                        nimError = null

                        if (namaLengkap.isBlank() || nimNip.isBlank() || programStudi.isBlank() || email.isBlank() || nomorTelepon.isBlank() || alamat.isBlank()) {
                            generalError = "Semua kolom wajib diisi"
                            return@launch
                        }

                        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            emailError = "Format email tidak valid"
                            return@launch
                        }

                        if (nimNip.length < 5 || !nimNip.all { it.isDigit() }) {
                            nimError = "Nomor induk minimal 5 digit angka"
                            return@launch
                        }

                        val isUnique = viewModel.checkNimUnique(nimNip, memberIdToEdit)
                        if (!isUnique) {
                            nimError = "NIM/NIP sudah terdaftar!"
                            return@launch
                        }

                        val member = MemberEntity(
                            id = memberToEdit?.id ?: 0,
                            namaLengkap = namaLengkap,
                            nimNip = nimNip,
                            programStudi = programStudi,
                            alamat = alamat,
                            nomorTelepon = nomorTelepon,
                            email = email,
                            jenisKeanggotaan = jenisKeanggotaan
                        )

                        if (isEditMode) {
                            viewModel.updateMember(member)
                        } else {
                            viewModel.addMember(member)
                        }
                        onNavigateBack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isEditMode) "Simpan Perubahan" else "Daftar")
            }
        }
    }
}