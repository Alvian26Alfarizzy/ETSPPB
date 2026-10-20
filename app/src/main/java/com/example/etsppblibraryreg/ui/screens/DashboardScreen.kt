package com.example.etsppblibraryreg.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.etsppblibraryreg.ui.theme.BlueCard
import com.example.etsppblibraryreg.ui.theme.GreenCard
import com.example.etsppblibraryreg.ui.theme.OrangeCard
import com.example.etsppblibraryreg.ui.theme.PurpleCard
import com.example.etsppblibraryreg.viewmodel.LibraryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: LibraryViewModel,
    isDarkTheme: Boolean,
    onThemeToggle: (Boolean) -> Unit,
    onNavigateToRegistration: () -> Unit,
    onNavigateToMemberList: () -> Unit
) {
    val members by viewModel.members.collectAsState()
    val totalAnggota = members.size
    val totalMahasiswa = members.count { it.jenisKeanggotaan == "Mahasiswa" }
    val totalDosen = members.count { it.jenisKeanggotaan == "Dosen" }
    val totalUmum = members.count { it.jenisKeanggotaan == "Umum" }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LibraryBooks, contentDescription = "Logo")
                        Spacer(Modifier.width(8.dp))
                        Text("LibraryReg")
                    }
                },
                actions = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(if (isDarkTheme) "Dark" else "Light", style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.width(4.dp))
                        Switch(
                            checked = isDarkTheme,
                            onCheckedChange = onThemeToggle,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Pendaftaran Anggota Perpustakaan",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Statistics Card
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItem("Total", totalAnggota)
                    StatItem("Mahasiswa", totalMahasiswa)
                    StatItem("Dosen", totalDosen)
                    StatItem("Umum", totalUmum)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Menu Cards Grid
            Row(modifier = Modifier.weight(1f)) {
                DashboardMenuCard(
                    title = "Pendaftaran Anggota",
                    icon = Icons.Default.PersonAdd,
                    color = BlueCard,
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToRegistration
                )
                Spacer(modifier = Modifier.width(16.dp))
                DashboardMenuCard(
                    title = "Daftar Anggota",
                    icon = Icons.Default.Group,
                    color = GreenCard,
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToMemberList
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.weight(1f)) {
                DashboardMenuCard(
                    title = "Pencarian Data",
                    icon = Icons.Default.Search,
                    color = OrangeCard,
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToMemberList
                )
                Spacer(modifier = Modifier.width(16.dp))
                DashboardMenuCard(
                    title = "Kelola Data",
                    icon = Icons.Default.Settings,
                    color = PurpleCard,
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToMemberList
                )
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value.toString(), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimaryContainer)
    }
}

@Composable
fun DashboardMenuCard(
    title: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }
    }
}