package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SwitchAccount
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DemoPaymentAccount
import com.example.ui.theme.YonoDarkBlue
import com.example.ui.theme.YonoPrimaryPurple

@Composable
fun WithdrawDialog(
  availableEarnings: Double,
  accounts: List<DemoPaymentAccount>,
  isProcessing: Boolean,
  onDismiss: () -> Unit,
  onConfirmWithdraw: (Double, DemoPaymentAccount) -> Unit
) {
  var amountText by remember { mutableStateOf("500") }
  var selectedAccount by remember {
    mutableStateOf(accounts.firstOrNull { it.isActive } ?: accounts.firstOrNull())
  }
  val amount = amountText.toDoubleOrNull() ?: 0.0
  val isValid = amount > 0.0 && selectedAccount != null

  AlertDialog(
    onDismissRequest = { if (!isProcessing) onDismiss() },
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.Payments,
          contentDescription = null,
          tint = YonoPrimaryPurple,
          modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Simulated Withdrawal",
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = YonoDarkBlue
        )
      }
    },
    text = {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        SandboxBanner(text = "DEMO PAYOUT • Simulated credit to mock account")

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text("Available Earnings:", fontSize = 12.sp, color = Color(0xFF64748B))
          Text(
            formatRupees(availableEarnings),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF10B981)
          )
        }

        OutlinedTextField(
          value = amountText,
          onValueChange = { input ->
            if (input.all { it.isDigit() } && input.length <= 6) {
              amountText = input
            }
          },
          label = { Text("Withdrawal Amount") },
          prefix = { Text("₹", fontWeight = FontWeight.Bold) },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("withdraw_amount_input"),
          shape = RoundedCornerShape(10.dp)
        )

        Text(
          text = "Destination Demo Account:",
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = YonoDarkBlue
        )

        Column(
          modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(10.dp))
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
            .padding(6.dp)
        ) {
          accounts.take(4).forEach { acc ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { selectedAccount = acc }
                .padding(vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              RadioButton(
                selected = selectedAccount?.id == acc.id,
                onClick = { selectedAccount = acc }
              )
              Spacer(modifier = Modifier.width(4.dp))
              Column {
                Text(
                  text = acc.provider,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = YonoDarkBlue
                )
                Text(
                  text = acc.upiId,
                  fontSize = 11.sp,
                  color = Color(0xFF64748B),
                  fontFamily = FontFamily.Monospace
                )
              }
            }
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          selectedAccount?.let { acc ->
            onConfirmWithdraw(amount, acc)
          }
        },
        enabled = isValid && !isProcessing,
        colors = ButtonDefaults.buttonColors(containerColor = YonoPrimaryPurple),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.testTag("submit_withdraw_btn")
      ) {
        Text("Confirm Simulated Payout")
      }
    },
    dismissButton = {
      OutlinedButton(
        onClick = onDismiss,
        enabled = !isProcessing,
        shape = RoundedCornerShape(10.dp)
      ) {
        Text("Cancel")
      }
    }
  )
}

@Composable
fun LogoutDialog(
  onDismiss: () -> Unit,
  onSwitchUser: (String) -> Unit
) {
  val personas = listOf("Standard Merchant", "VIP Agent", "New Trader")

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.SwitchAccount,
          contentDescription = null,
          tint = YonoPrimaryPurple,
          modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Demo Persona Switcher",
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = YonoDarkBlue
        )
      }
    },
    text = {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Text(
          text = "Switch between preconfigured mock testing profiles or reset your session:",
          fontSize = 12.sp,
          color = Color(0xFF64748B)
        )

        Spacer(modifier = Modifier.height(4.dp))

        personas.forEach { persona ->
          Button(
            onClick = { onSwitchUser(persona) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F5F9)),
            shape = RoundedCornerShape(10.dp)
          ) {
            Text(
              text = persona,
              color = YonoDarkBlue,
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp
            )
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = onDismiss,
        colors = ButtonDefaults.buttonColors(containerColor = YonoPrimaryPurple),
        shape = RoundedCornerShape(10.dp)
      ) {
        Text("Close")
      }
    }
  )
}

@Composable
fun ShareInviteDialog(
  inviteCode: String,
  onDismiss: () -> Unit,
  onCopy: () -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.Share,
          contentDescription = null,
          tint = YonoPrimaryPurple,
          modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Invite Partners",
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = YonoDarkBlue
        )
      }
    },
    text = {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Text(
          text = "Earn 3% perpetual commission from all team deposit volume by sharing your code:",
          fontSize = 12.sp,
          color = Color(0xFF64748B)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF1F5F9), RoundedCornerShape(10.dp))
            .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(10.dp))
            .padding(12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = inviteCode,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Monospace,
            color = YonoPrimaryPurple
          )
          Button(
            onClick = onCopy,
            colors = ButtonDefaults.buttonColors(containerColor = YonoPrimaryPurple),
            shape = RoundedCornerShape(8.dp)
          ) {
            Text("Copy", fontSize = 12.sp)
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = onDismiss,
        shape = RoundedCornerShape(10.dp)
      ) {
        Text("Done")
      }
    }
  )
}
