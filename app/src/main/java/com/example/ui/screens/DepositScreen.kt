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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DemoPaymentAccount
import com.example.model.DepositPackage
import com.example.ui.components.CommissionBadge
import com.example.ui.components.SandboxBanner
import com.example.ui.components.formatRupees
import com.example.ui.theme.YonoDarkBlue
import com.example.ui.theme.YonoPrimaryPurple
import com.example.ui.theme.YonoSecondaryBlue
import com.example.ui.theme.YonoSuccessGreen
import com.example.viewmodel.YonoPayUiState

@Composable
fun DepositScreen(
  uiState: YonoPayUiState,
  onExecuteDeposit: (Double, DemoPaymentAccount?, String) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedOrderPackage by remember { mutableStateOf<DepositPackage?>(null) }
  var pendingOrderAmount by remember { mutableDoubleStateOf(0.0) }
  var selectedAccount by remember {
    mutableStateOf(uiState.paymentAccounts.firstOrNull { it.isActive } ?: uiState.paymentAccounts.firstOrNull())
  }
  var showConfirmDialog by remember { mutableStateOf(false) }

  // Custom deposit amount field
  var customAmountText by remember { mutableStateOf("") }

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
        text = "DEMO DEPOSIT SANDBOX • Select order range below to simulate deposit"
      )
    }

    // Header Explanation banner
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
                text = "Deposit & Earn",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = YonoDarkBlue
              )
              Text(
                text = "Earn 3% instant commission on every completed order",
                fontSize = 12.sp,
                color = Color(0xFF64748B)
              )
            }
            CommissionBadge(commissionText = "3% Fixed")
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Example banner as specified in prompt
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(10.dp))
              .background(Color(0xFFEFF6FF))
              .border(1.dp, Color(0xFFBFDBFE), RoundedCornerShape(10.dp))
              .padding(10.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = YonoSecondaryBlue,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Commission Example: ₹1,000 deposit = ₹30 commission (Expected Earning: ₹1,030)",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1E40AF)
              )
            }
          }
        }
      }
    }

    // Custom Amount Quick Box
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Custom Sandbox Amount",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = YonoDarkBlue
          )
          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            OutlinedTextField(
              value = customAmountText,
              onValueChange = { input ->
                if (input.all { it.isDigit() } && input.length <= 6) {
                  customAmountText = input
                }
              },
              placeholder = { Text("Enter ₹ amount (e.g. 1500)") },
              prefix = { Text("₹", fontWeight = FontWeight.Bold) },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              singleLine = true,
              modifier = Modifier
                .weight(1f)
                .testTag("custom_deposit_input"),
              shape = RoundedCornerShape(10.dp),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = YonoPrimaryPurple,
                unfocusedBorderColor = Color(0xFFCBD5E1)
              )
            )

            Spacer(modifier = Modifier.width(8.dp))

            val parsedAmount = customAmountText.toDoubleOrNull() ?: 0.0
            Button(
              onClick = {
                if (parsedAmount >= 100.0) {
                  pendingOrderAmount = parsedAmount
                  selectedOrderPackage = null
                  showConfirmDialog = true
                }
              },
              enabled = parsedAmount >= 100.0,
              colors = ButtonDefaults.buttonColors(containerColor = YonoPrimaryPurple),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.testTag("custom_deposit_buy_btn")
            ) {
              Text("Demo Buy")
            }
          }

          if (customAmountText.isNotBlank()) {
            val amount = customAmountText.toDoubleOrNull() ?: 0.0
            val comm = amount * 0.03
            val expected = amount + comm
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "Commission: ${formatRupees(comm)} | Expected Earning: ${formatRupees(expected)}",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF059669)
            )
          }
        }
      }
    }

    // Header for the 6 prompt-specified order ranges
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Payment Order Ranges",
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = YonoDarkBlue
        )
        Text(
          text = "6 Tiers Available",
          fontSize = 12.sp,
          color = Color(0xFF64748B)
        )
      }
    }

    // List of the 6 prompt-defined order packages
    items(uiState.depositPackages) { pkg ->
      DepositPackageCard(
        pkg = pkg,
        onBuyClick = { amount ->
          pendingOrderAmount = amount
          selectedOrderPackage = pkg
          showConfirmDialog = true
        }
      )
    }

    item {
      Spacer(modifier = Modifier.height(24.dp))
    }
  }

  // Confirmation / Payment Rail Selection Dialog
  if (showConfirmDialog) {
    val commission = pendingOrderAmount * 0.03
    val expected = pendingOrderAmount + commission

    AlertDialog(
      onDismissRequest = {
        if (!uiState.isProcessing) showConfirmDialog = false
      },
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = null,
            tint = YonoPrimaryPurple,
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Confirm Demo Buy",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = YonoDarkBlue
          )
        }
      },
      text = {
        Column(modifier = Modifier.fillMaxWidth()) {
          SandboxBanner(text = "SIMULATED TRANSACTION • No real money charged")

          Spacer(modifier = Modifier.height(12.dp))

          // Order summary box
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .background(Color(0xFFF8FAFC), RoundedCornerShape(10.dp))
              .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
              .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Order Amount:", fontSize = 13.sp, color = Color(0xFF64748B))
              Text(
                formatRupees(pendingOrderAmount),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = YonoDarkBlue
              )
            }
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Commission (3%):", fontSize = 13.sp, color = Color(0xFF059669))
              Text(
                "+${formatRupees(commission)}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF059669)
              )
            }
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Expected Earning:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = YonoDarkBlue)
              Text(
                formatRupees(expected),
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                color = YonoPrimaryPurple
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          Text(
            text = "Route Through Demo Account:",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = YonoDarkBlue
          )

          Spacer(modifier = Modifier.height(6.dp))

          // Connected demo accounts radio list
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .background(Color.White, RoundedCornerShape(10.dp))
              .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
              .padding(6.dp)
          ) {
            uiState.paymentAccounts.take(4).forEach { acc ->
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

          if (uiState.isProcessing) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.Center,
              verticalAlignment = Alignment.CenterVertically
            ) {
              CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color = YonoPrimaryPurple
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Processing sandbox order...",
                fontSize = 12.sp,
                color = YonoPrimaryPurple
              )
            }
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            val pkgName = selectedOrderPackage?.rangeLabel ?: "Custom Amount"
            onExecuteDeposit(pendingOrderAmount, selectedAccount, pkgName)
            showConfirmDialog = false
          },
          enabled = !uiState.isProcessing,
          colors = ButtonDefaults.buttonColors(containerColor = YonoPrimaryPurple),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.testTag("confirm_sandbox_buy_button")
        ) {
          Text("Confirm Demo Buy")
        }
      },
      dismissButton = {
        OutlinedButton(
          onClick = { showConfirmDialog = false },
          enabled = !uiState.isProcessing,
          shape = RoundedCornerShape(10.dp)
        ) {
          Text("Cancel")
        }
      }
    )
  }
}

@Composable
private fun DepositPackageCard(
  pkg: DepositPackage,
  onBuyClick: (Double) -> Unit
) {
  var selectedAmount by remember { mutableDoubleStateOf(pkg.defaultAmount) }
  val commission = pkg.calculateCommission(selectedAmount)
  val expectedEarning = pkg.calculateExpectedEarning(selectedAmount)

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("pkg_card_${pkg.id}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      // Range Header Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(Color(0xFFEDE9FE)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.FlashOn,
              contentDescription = null,
              tint = YonoPrimaryPurple,
              modifier = Modifier.size(20.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = pkg.rangeLabel,
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold,
              color = YonoDarkBlue
            )
            Text(
              text = "Min ${formatRupees(pkg.minAmount)} • Max ${formatRupees(pkg.maxAmount)}",
              fontSize = 11.sp,
              color = Color(0xFF64748B)
            )
          }
        }

        if (pkg.isPopular) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(20.dp))
              .background(Color(0xFFFEF3C7))
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Text(
              text = "POPULAR",
              color = Color(0xFFB45309),
              fontSize = 10.sp,
              fontWeight = FontWeight.ExtraBold
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Preset quick amount chips within this range
      val presets = listOf(
        pkg.minAmount,
        ((pkg.minAmount + pkg.maxAmount) / 2.0).toInt().toDouble(),
        pkg.maxAmount
      ).distinct()

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        presets.forEach { amt ->
          val isSelected = selectedAmount == amt
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(8.dp))
              .background(if (isSelected) YonoPrimaryPurple else Color(0xFFF1F5F9))
              .border(
                1.dp,
                if (isSelected) YonoPrimaryPurple else Color(0xFFCBD5E1),
                RoundedCornerShape(8.dp)
              )
              .clickable { selectedAmount = amt }
              .padding(vertical = 6.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = formatRupees(amt),
              fontSize = 12.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              color = if (isSelected) Color.White else YonoDarkBlue
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // 3-part breakdown as required by prompt:
      // - Order amount
      // - 3% commission
      // - Expected earning
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(Color(0xFFF8FAFC), RoundedCornerShape(10.dp))
          .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
          .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text("Order Amount", fontSize = 11.sp, color = Color(0xFF64748B))
          Text(
            text = formatRupees(selectedAmount),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = YonoDarkBlue
          )
        }

        Column {
          Text("3% Commission", fontSize = 11.sp, color = Color(0xFF059669))
          Text(
            text = "+${formatRupees(commission)}",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF059669)
          )
        }

        Column {
          Text("Expected Earning", fontSize = 11.sp, color = YonoPrimaryPurple)
          Text(
            text = formatRupees(expectedEarning),
            fontSize = 14.sp,
            fontWeight = FontWeight.Black,
            color = YonoPrimaryPurple
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Demo Buy Button
      Button(
        onClick = { onBuyClick(selectedAmount) },
        modifier = Modifier
          .fillMaxWidth()
          .height(44.dp)
          .testTag("demo_buy_${pkg.id}"),
        colors = ButtonDefaults.buttonColors(containerColor = YonoPrimaryPurple),
        shape = RoundedCornerShape(12.dp)
      ) {
        Icon(
          imageVector = Icons.Default.ShoppingCart,
          contentDescription = null,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Demo Buy ${formatRupees(selectedAmount)}",
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold
        )
      }
    }
  }
}
