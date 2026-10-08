package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DemoPaymentAccount
import com.example.ui.components.SandboxBanner
import com.example.ui.components.formatRupees
import com.example.ui.theme.YonoDarkBlue
import com.example.ui.theme.YonoPrimaryPurple
import com.example.ui.theme.YonoSecondaryBlue
import com.example.viewmodel.YonoPayUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolsScreen(
  uiState: YonoPayUiState,
  onToggleAccountActive: (String) -> Unit,
  onAddAccount: (String, String, String) -> Unit,
  modifier: Modifier = Modifier
) {
  var showAddDialog by remember { mutableStateOf(false) }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF8FAFC))
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Spacer(modifier = Modifier.height(8.dp))
      SandboxBanner(
        text = "DEMO PAYMENT GATEWAYS • Simulated accounts only • No bank connections"
      )
    }

    // Header Card with Add Button
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "Demo Payment Accounts",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = YonoDarkBlue
              )
              Text(
                text = "Configure mock UPI & business payment rails",
                fontSize = 12.sp,
                color = Color(0xFF64748B)
              )
            }

            Button(
              onClick = { showAddDialog = true },
              colors = ButtonDefaults.buttonColors(containerColor = YonoPrimaryPurple),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.testTag("add_demo_account_button")
            ) {
              Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text("Add Demo")
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Explanatory note
          Text(
            text = "These mock accounts simulate real Indian payment providers (Paytm, PhonePe, GPay Business, BharatPe Business, Freecharge, MobiKwik) for testing instant settlement workflows without real financial risks.",
            fontSize = 11.sp,
            color = Color(0xFF64748B)
          )
        }
      }
    }

    // Active Accounts Count
    item {
      val activeCount = uiState.paymentAccounts.count { it.isActive }
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Registered Gateways (${uiState.paymentAccounts.size})",
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          color = YonoDarkBlue
        )
        Text(
          text = "$activeCount Active",
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF16A34A)
        )
      }
    }

    // List of demo accounts
    items(uiState.paymentAccounts) { account ->
      DemoAccountCard(
        account = account,
        onToggleActive = { onToggleAccountActive(account.id) }
      )
    }

    item {
      Spacer(modifier = Modifier.height(24.dp))
    }
  }

  if (showAddDialog) {
    AddDemoAccountDialog(
      onDismiss = { showAddDialog = false },
      onConfirm = { provider, name, upi ->
        onAddAccount(provider, name, upi)
        showAddDialog = false
      }
    )
  }
}

@Composable
private fun DemoAccountCard(
  account: DemoPaymentAccount,
  onToggleActive: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("account_card_${account.id}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      // Top row: Provider, Demo Badge & Switch
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(38.dp)
              .clip(RoundedCornerShape(10.dp))
              .background(getProviderColor(account.provider).copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.QrCode,
              contentDescription = null,
              tint = getProviderColor(account.provider),
              modifier = Modifier.size(20.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = account.provider,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = YonoDarkBlue
              )
              Spacer(modifier = Modifier.width(6.dp))
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(4.dp))
                  .background(Color(0xFFFEF3C7))
                  .padding(horizontal = 5.dp, vertical = 2.dp)
              ) {
                Text(
                  text = "DEMO",
                  color = Color(0xFFB45309),
                  fontSize = 9.sp,
                  fontWeight = FontWeight.Black
                )
              }
            }
            Text(
              text = account.accountName,
              fontSize = 11.sp,
              color = Color(0xFF64748B)
            )
          }
        }

        Switch(
          checked = account.isActive,
          onCheckedChange = { onToggleActive() },
          colors = SwitchDefaults.colors(
            checkedThumbColor = Color.White,
            checkedTrackColor = YonoPrimaryPurple
          ),
          modifier = Modifier.testTag("toggle_account_${account.id}")
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      // VPA / UPI ID Box
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(Color(0xFFF1F5F9), RoundedCornerShape(8.dp))
          .padding(horizontal = 10.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Demo VPA / UPI ID",
            fontSize = 10.sp,
            color = Color(0xFF64748B)
          )
          Text(
            text = account.upiId,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.Monospace,
            color = YonoDarkBlue
          )
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Sensors,
              contentDescription = null,
              tint = if (account.isActive) Color(0xFF16A34A) else Color(0xFF94A3B8),
              modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = if (account.isActive) "Online" else "Offline",
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = if (account.isActive) Color(0xFF16A34A) else Color(0xFF94A3B8)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Processed volume
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = "Simulated Volume Processed",
          fontSize = 11.sp,
          color = Color(0xFF64748B)
        )
        Text(
          text = formatRupees(account.totalVolume),
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = YonoPrimaryPurple
        )
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddDemoAccountDialog(
  onDismiss: () -> Unit,
  onConfirm: (String, String, String) -> Unit
) {
  val providers = listOf(
    "Paytm",
    "PhonePe",
    "Google Pay Business",
    "BharatPe Business",
    "Freecharge",
    "MobiKwik"
  )
  var selectedProvider by remember { mutableStateOf(providers.first()) }
  var isDropdownExpanded by remember { mutableStateOf(false) }
  var accountName by remember { mutableStateOf("") }
  var upiId by remember { mutableStateOf("") }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = "Add Demo Payment Account",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = YonoDarkBlue
      )
    },
    text = {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        SandboxBanner(text = "SANDBOX ONLY • Real credentials strictly prohibited")

        Text(
          text = "Select Provider:",
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold,
          color = YonoDarkBlue
        )

        ExposedDropdownMenuBox(
          expanded = isDropdownExpanded,
          onExpandedChange = { isDropdownExpanded = !isDropdownExpanded }
        ) {
          OutlinedTextField(
            value = selectedProvider,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded) },
            modifier = Modifier
              .fillMaxWidth()
              .menuAnchor(),
            shape = RoundedCornerShape(10.dp)
          )

          ExposedDropdownMenu(
            expanded = isDropdownExpanded,
            onDismissRequest = { isDropdownExpanded = false }
          ) {
            providers.forEach { p ->
              DropdownMenuItem(
                text = { Text(p) },
                onClick = {
                  selectedProvider = p
                  isDropdownExpanded = false
                  if (upiId.isBlank()) {
                    val slug = p.lowercase().replace(" ", "")
                    upiId = "demo.merchant@$slug"
                  }
                }
              )
            }
          }
        }

        OutlinedTextField(
          value = accountName,
          onValueChange = { accountName = it },
          label = { Text("Business / Outlet Name") },
          placeholder = { Text("e.g. Metro Retail Hub") },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp)
        )

        OutlinedTextField(
          value = upiId,
          onValueChange = { upiId = it },
          label = { Text("Demo UPI / VPA") },
          placeholder = { Text("e.g. demo.merchant@upi") },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp)
        )
      }
    },
    confirmButton = {
      Button(
        onClick = {
          onConfirm(
            selectedProvider,
            if (accountName.isBlank()) "$selectedProvider Outlet" else accountName,
            if (upiId.isBlank()) "demo.${selectedProvider.lowercase().replace(" ", "")}@upi" else upiId
          )
        },
        colors = ButtonDefaults.buttonColors(containerColor = YonoPrimaryPurple),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.testTag("submit_add_account_btn")
      ) {
        Text("Save Demo Account")
      }
    },
    dismissButton = {
      OutlinedButton(
        onClick = onDismiss,
        shape = RoundedCornerShape(10.dp)
      ) {
        Text("Cancel")
      }
    }
  )
}

private fun getProviderColor(provider: String): Color {
  return when {
    provider.contains("Paytm", ignoreCase = true) -> Color(0xFF00B9F1)
    provider.contains("PhonePe", ignoreCase = true) -> Color(0xFF5F259F)
    provider.contains("Google", ignoreCase = true) -> Color(0xFF1A73E8)
    provider.contains("BharatPe", ignoreCase = true) -> Color(0xFF13A89E)
    provider.contains("Freecharge", ignoreCase = true) -> Color(0xFFF37254)
    provider.contains("MobiKwik", ignoreCase = true) -> Color(0xFF0070E0)
    else -> Color(0xFF6B7280)
  }
}
