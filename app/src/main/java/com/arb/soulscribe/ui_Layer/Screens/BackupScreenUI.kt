package com.arb.soulscribe.ui_Layer.Screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Home

import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.arb.soulscribe.ui_Layer.ViewModel.BackupViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupScreenUI(navController: NavController) {
    val viewModel: BackupViewModel = hiltViewModel()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var isLoading by remember { mutableStateOf(false) }
    var showSuccessMessage by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }
    var showErrorMessage by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Export backup launcher
    val createDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri: Uri? ->
        if (uri != null) {
            coroutineScope.launch {
                try {
                    isLoading = true
                    showSuccessMessage = false
                    showErrorMessage = false

                    val json = viewModel.generateBackupJson()
                    context.contentResolver.openOutputStream(uri)?.use { output ->
                        output.write(json.toByteArray())
                    }

                    isLoading = false
                    successMessage = "Backup exported securely to your device!"
                    showSuccessMessage = true
                    navController.navigateUp()

                } catch (e: Exception) {
                    isLoading = false
                    errorMessage = "Failed to export backup: ${e.localizedMessage}"
                    showErrorMessage = true
                }
            }
        } else {
            errorMessage = "Export cancelled"
            showErrorMessage = true
        }
    }

    // Import backup launcher
    val openDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            coroutineScope.launch {
                try {
                    isLoading = true
                    showSuccessMessage = false
                    showErrorMessage = false

                    val json = context.contentResolver.openInputStream(uri)
                        ?.bufferedReader()
                        ?.use { it.readText() }

                    if (json.isNullOrBlank()) {
                        throw Exception("File is empty or unreadable")
                    }

                    viewModel.importBackupJson(json)

                    isLoading = false
                    successMessage = "Backup merged successfully with existing data!"
                    showSuccessMessage = true
                    navController.navigateUp()

                } catch (e: Exception) {
                    isLoading = false
                    errorMessage = "Failed to restore backup: ${e.localizedMessage}"
                    showErrorMessage = true
                }
            }
        } else {
            errorMessage = "Import cancelled"
            showErrorMessage = true
        }
    }

    Scaffold(
        containerColor = Color(0xFF231B33),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF121025), Color(0xFF5E3B67))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .background(
                                    Color.White.copy(alpha = 0.1f),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 4.dp,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                        Text(
                            "Securing your data...",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            "Please wait while we process your backup",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 14.sp
                        )
                    }
                }

                showSuccessMessage -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .background(
                                    Color(0xFF4CAF50).copy(alpha = 0.2f),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = "Success",
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(64.dp)
                            )
                        }
                        Text(
                            successMessage,
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            "Returning to home...",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 16.sp
                        )
                    }
                }

                showErrorMessage -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .background(
                                    Color(0xFFF44336).copy(alpha = 0.2f),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Clear,
                                contentDescription = "Error",
                                tint = Color(0xFFF44336),
                                modifier = Modifier.size(64.dp)
                            )
                        }
                        Text(
                            "Oops! Something went wrong",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            errorMessage,
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                        Button(
                            onClick = {
                                showErrorMessage = false
                                errorMessage = ""
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF007AFF)
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .height(48.dp)
                        ) {
                            Text(
                                "Try Again",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Header with security icon
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(120.dp)
                                    .background(
                                        Color.White.copy(alpha = 0.1f),
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Lock,
                                    contentDescription = "Security Shield",
                                    tint = Color(0xFF00D4FF),
                                    modifier = Modifier.size(64.dp)
                                )
                            }

                            Text(
                                "Local Backup Security",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                "Your data stays on your device. Export securely to your local storage or merge backups without data loss.",
                                fontSize = 16.sp,
                                color = Color.White.copy(alpha = 0.8f),
                                textAlign = TextAlign.Center,
                                lineHeight = 22.sp
                            )
                        }

                        // Security features
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            SecurityFeature(
                                icon = Icons.Default.Home,
                                title = "Local",
                                description = "Device Only"
                            )
                            SecurityFeature(
                                icon = Icons.Default.Person,
                                title = "Private",
                                description = "No Cloud"
                            )
                            SecurityFeature(
                                icon = Icons.Default.Lock,
                                title = "Secure",
                                description = "Encrypted"
                            )
                        }

                        // Action buttons - Made smaller
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Export Button - Reduced height and padding
                            ElevatedButton(
                                onClick = {
                                    val timestamp = System.currentTimeMillis()
                                    createDocumentLauncher.launch("soulscribe_backup_$timestamp.json")
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp), // Reduced from 72dp
                                shape = RoundedCornerShape(16.dp), // Reduced from 20dp
                                colors = ButtonDefaults.elevatedButtonColors(
                                    containerColor = Color(0xFF007AFF),
                                    contentColor = Color.White
                                ),
                                elevation = ButtonDefaults.elevatedButtonElevation(
                                    defaultElevation = 8.dp, // Reduced from 12dp
                                    pressedElevation = 4.dp
                                )
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp) // Reduced spacing
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp) // Reduced from 40dp
                                            .background(
                                                Color.White.copy(alpha = 0.2f),
                                                CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.KeyboardArrowUp,
                                            contentDescription = "Export",
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Column {
                                        Text(
                                            "Export Backup",
                                            fontSize = 16.sp, // Reduced from 18sp
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            "Save to device storage",
                                            fontSize = 12.sp, // Reduced from 13sp
                                            color = Color.White.copy(alpha = 0.8f)
                                        )
                                    }
                                }
                            }

                            // Restore Button - Reduced height and padding
                            ElevatedButton(
                                onClick = {
                                    openDocumentLauncher.launch(arrayOf("application/json", "text/plain", "*/*"))
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp), // Reduced from 72dp
                                shape = RoundedCornerShape(16.dp), // Reduced from 20dp
                                colors = ButtonDefaults.elevatedButtonColors(
                                    containerColor = Color(0xFF34C759),
                                    contentColor = Color.White
                                ),
                                elevation = ButtonDefaults.elevatedButtonElevation(
                                    defaultElevation = 8.dp, // Reduced from 12dp
                                    pressedElevation = 4.dp
                                )
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp) // Reduced spacing
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp) // Reduced from 40dp
                                            .background(
                                                Color.White.copy(alpha = 0.2f),
                                                CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.KeyboardArrowDown, // Changed to Download
                                            contentDescription = "Restore",
                                            modifier = Modifier.size(20.dp) // Reduced from 24dp
                                        )
                                    }
                                    Column {
                                        Text(
                                            "Restore And Merge",
                                            fontSize = 16.sp, // Reduced from 18sp
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            "Safe merge, no data loss",
                                            fontSize = 12.sp, // Reduced from 13sp
                                            color = Color.White.copy(alpha = 0.8f)
                                        )
                                    }
                                }
                            }
                        }

                        // Info card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White.copy(alpha = 0.1f)
                            ),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp), // Reduced from 20dp
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    Icons.Default.Info, // Changed from Lock for better context
                                    contentDescription = "Info",
                                    tint = Color(0xFF00D4FF),
                                    modifier = Modifier.size(20.dp) // Reduced from 24dp
                                )
                                Text(
                                    "Smart merge technology preserves all your existing entries while adding backup data. Your journal stays complete and secure.",
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontSize = 13.sp, // Reduced from 14sp
                                    lineHeight = 18.sp // Reduced from 20sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SecurityFeature(
    icon: ImageVector,
    title: String,
    description: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(
                    Color.White.copy(alpha = 0.1f),
                    CircleShape
                )
                .border(
                    1.dp,
                    Color.White.copy(alpha = 0.3f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = title,
                tint = Color(0xFF00D4FF),
                modifier = Modifier.size(28.dp)
            )
        }
        Text(
            title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            description,
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.7f)
        )
    }
}