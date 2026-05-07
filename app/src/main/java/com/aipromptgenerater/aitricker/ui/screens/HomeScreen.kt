package com.aipromptgenerater.aitricker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aipromptgenerater.aitricker.ui.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToGenerate: (String) -> Unit,
    onNavigateToBilling: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val credits by viewModel.credits.collectAsState()

    LaunchedEffect(Unit) { viewModel.fetchCredits() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("App Builder AI", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold) },
                actions = {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.padding(end = 16.dp).clickable { onNavigateToBilling() }
                    ) {
                        Row(Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = "Credits", tint = Color(0xFFEAB308), modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("$credits Credits", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text("What do you want to build today?", fontSize = 24.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            Spacer(modifier = Modifier.height(32.dp))

            SelectionCard("Mobile App", "Generate comprehensive prompts for Android/iOS apps", Icons.Default.Build) {
                onNavigateToGenerate("App")
            }
            Spacer(modifier = Modifier.height(16.dp))
            SelectionCard("Website", "Generate structured prompts for Web Development", Icons.Default.Build) {
                onNavigateToGenerate("Website")
            }
        }
    }
}

@Composable
fun SelectionCard(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(modifier = Modifier.padding(24.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(56.dp).clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, fontSize = 20.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                Text(subtitle, fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}
