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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CallReceived
import androidx.compose.material.icons.filled.CallMade
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ScreenTab
import com.example.model.TransactionItem
import com.example.model.TransactionType
import com.example.ui.components.CommissionBadge
import com.example.ui.components.SandboxBanner
import com.example.ui.components.StatusBadge
import com.example.ui.components.TypeBadge
import com.example.ui.components.YonoCardGradient
import com.example.ui.components.YonoFintechGradient
import com.example.ui.components.formatRupees
import com.example.ui.theme.YonoDarkBlue
import com.example.ui.theme.YonoPrimaryPurple
import com.example.ui.theme.YonoSecondaryBlue
import com.example.ui.theme.YonoSuccessGreen
import com.example.viewmodel.YonoPayUiState

@Composable
fun HomeScreen(
  uiState: YonoPayUiState,
  onNavigateTab: (ScreenTab) -> Unit,
  onOpenWithdraw: () -> Unit,
  onSelectTransaction: (TransactionItem) -> Unit,
  onQuickSimulateDeposit: (Double) -> Unit,
  modifier: Modifier = Modifier
) {
  var calcAmountInput by remember { mutableStateOf("1000") }
  val calcAmount = calcAmountInput.toDoubleOrNull() ?: 0.0
  val calcCommission = calcAmount * uiState.commissionRate
  val calcTotalExpected = calcAmount + calcCommission

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF8FAFC))
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    item {
      Spacer(modifier = Modifier.height(8.dp))
      SandboxBanner(
        text = "YONO PAY SANDBOX • 3% Commission Active • Virtual Demo Balances"
      )
    }

    // Main Gradient Financial Overview Card
    item {
      MainOverviewCard(
        todayEarnings = uiState.todayEarnings,
        todayWithdraw = uiState.todayWithdraw,
        todayDeposit = uiState.todayDeposit,
        inTransaction = uiState.inTransaction,
        commissionRate = uiState.commissionRate,
        onDepositClick = { onNavigateTab(ScreenTab.DEPOSIT) },
        onWithdrawClick = onOpenWithdraw
      )
    }

    // Quota & Daily Rewards Card
    item {
      QuotaAndRewardsCard(
        currentQuota = uiState.userProfile.availableQuota,
        totalQuota = uiState.userProfile.totalQuota,
        dailyOrderRewards = uiState.dailyOrderRewards,
        dailyAmountRewards = uiState.dailyAmountRewards
      )
    }

    // Quick Action Hub
    item {
      QuickActionsRow(
        onDepositClick = { onNavigateTab(ScreenTab.DEPOSIT) },
        onWithdrawClick = onOpenWithdraw,
        onToolsClick = { onNavigateTab(ScreenTab.TOOLS) },
        onTeamClick = { onNavigateTab(ScreenTab.TEAM) },
        onTxClick = { onNavigateTab(ScreenTab.TRANSACTIONS) }
      )
    }

    // Interactive Commission Calculator / Simulator Widget
    item {
      CommissionSimulatorCard(
        amountInput = calcAmountInput,
        onAmountChanged = { calcAmountInput = it },
        calcAmount = calcAmount,
        commission = calcCommission,
        expectedReturn = calcTotalExpected,
        onTestDeposit = {
          if (calcAmount >= 100.0) {
            onQuickSimulateDeposit(calcAmount)
          }
        }
      )
    }

    // Recent Transactions Header
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.History,
            contentDescription = null,
            tint = YonoPrimaryPurple,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Recent Sandbox Orders",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = YonoDarkBlue
          )
        }
        Text(
          text = "View All (${uiState.transactions.size})",
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = YonoSecondaryBlue,
          modifier = Modifier
            .clickable { onNavigateTab(ScreenTab.TRANSACTIONS) }
            .testTag("home_view_all_transactions")
        )
      }
    }

    // Recent Transactions list preview
    val recentItems = uiState.transactions.take(4)
    items(recentItems) { item ->
      RecentTransactionRow(
        transaction = item,
        onClick = { onSelectTransaction(item) }
      )
    }

    item {
      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

@Composable
private fun MainOverviewCard(
  todayEarnings: Double,
  todayWithdraw: Double,
  todayDeposit: Double,
  inTransaction: Double,
  commissionRate: Double,
  onDepositClick: () -> Unit,
  onWithdrawClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("home_main_overview_card"),
    shape = RoundedCornerShape(20.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(YonoCardGradient)
        .padding(18.dp)
    ) {
      Column {
        // Top row: Today's Earning & Commission badge
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "Today's Earning",
              color = Color.White.copy(alpha = 0.85f),
              fontSize = 13.sp,
              fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = formatRupees(todayEarnings),
              color = Color(0xFF4ADE80), // Bright green
              fontSize = 28.sp,
              fontWeight = FontWeight.Black
            )
          }

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(20.dp))
              .background(Color.White.copy(alpha = 0.2f))
              .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
              .padding(horizontal = 10.dp, vertical = 6.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                contentDescription = null,
                tint = Color(0xFFFDE047),
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "${(commissionRate * 100).toInt()}% Commission",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Grid of 3 key metrics: Today's Withdraw, Today's Deposit, In Transaction
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          MetricColumn(
            label = "Today's Withdraw",
            value = formatRupees(todayWithdraw),
            icon = Icons.Default.CallMade,
            iconTint = Color(0xFFFCA5A5)
          )
          MetricColumn(
            label = "Today's Deposit",
            value = formatRupees(todayDeposit),
            icon = Icons.Default.CallReceived,
            iconTint = Color(0xFF93C5FD)
          )
          MetricColumn(
            label = "In Transaction",
            value = formatRupees(inTransaction),
            icon = Icons.Default.HourglassTop,
            iconTint = Color(0xFFFDE68A)
          )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Action Buttons Row inside Card
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Button(
            onClick = onDepositClick,
            modifier = Modifier
              .weight(1f)
              .height(44.dp)
              .testTag("home_deposit_button"),
            colors = ButtonDefaults.buttonColors(
              containerColor = Color.White,
              contentColor = YonoPrimaryPurple
            ),
            shape = RoundedCornerShape(12.dp)
          ) {
            Icon(
              imageVector = Icons.Default.AddCard,
              contentDescription = null,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Deposit",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
          }

          Button(
            onClick = onWithdrawClick,
            modifier = Modifier
              .weight(1f)
              .height(44.dp)
              .testTag("home_withdraw_button"),
            colors = ButtonDefaults.buttonColors(
              containerColor = Color.White.copy(alpha = 0.2f),
              contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Payments,
              contentDescription = null,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Withdraw",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
          }
        }
      }
    }
  }
}

@Composable
private fun MetricColumn(
  label: String,
  value: String,
  icon: ImageVector,
  iconTint: Color
) {
  Column {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = iconTint,
        modifier = Modifier.size(13.dp)
      )
      Spacer(modifier = Modifier.width(4.dp))
      Text(
        text = label,
        color = Color.White.copy(alpha = 0.75f),
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium
      )
    }
    Spacer(modifier = Modifier.height(3.dp))
    Text(
      text = value,
      color = Color.White,
      fontSize = 14.sp,
      fontWeight = FontWeight.Bold
    )
  }
}

@Composable
private fun QuotaAndRewardsCard(
  currentQuota: Double,
  totalQuota: Double,
  dailyOrderRewards: Double,
  dailyAmountRewards: Double
) {
  val progress = (currentQuota / totalQuota).coerceIn(0.0, 1.0).toFloat()

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("home_quota_card"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      // Quota header & progress
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Current Quota",
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          color = YonoDarkBlue
        )
        Text(
          text = "${formatRupees(currentQuota)} / ${formatRupees(totalQuota)}",
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold,
          color = YonoSecondaryBlue
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier
          .fillMaxWidth()
          .height(8.dp)
          .clip(RoundedCornerShape(4.dp)),
        color = YonoSecondaryBlue,
        trackColor = Color(0xFFE2E8F0)
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Daily rewards split
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(Color(0xFFF1F5F9), RoundedCornerShape(12.dp))
          .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(32.dp)
              .clip(CircleShape)
              .background(Color(0xFFFEF3C7)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.CardGiftcard,
              contentDescription = null,
              tint = Color(0xFFD97706),
              modifier = Modifier.size(18.dp)
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Column {
            Text(
              text = "Daily Order Rewards",
              fontSize = 11.sp,
              color = Color(0xFF64748B)
            )
            Text(
              text = formatRupees(dailyOrderRewards),
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = YonoDarkBlue
            )
          }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(32.dp)
              .clip(CircleShape)
              .background(Color(0xFFDCFCE7)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Stars,
              contentDescription = null,
              tint = Color(0xFF16A34A),
              modifier = Modifier.size(18.dp)
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Column {
            Text(
              text = "Daily Amount Rewards",
              fontSize = 11.sp,
              color = Color(0xFF64748B)
            )
            Text(
              text = formatRupees(dailyAmountRewards),
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = YonoDarkBlue
            )
          }
        }
      }
    }
  }
}

@Composable
private fun QuickActionsRow(
  onDepositClick: () -> Unit,
  onWithdrawClick: () -> Unit,
  onToolsClick: () -> Unit,
  onTeamClick: () -> Unit,
  onTxClick: () -> Unit
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    QuickActionButton(
      icon = Icons.Default.AddCard,
      label = "Deposit",
      color = Color(0xFF8B5CF6),
      onClick = onDepositClick,
      tag = "quick_deposit"
    )
    QuickActionButton(
      icon = Icons.Default.Payments,
      label = "Withdraw",
      color = Color(0xFF3B82F6),
      onClick = onWithdrawClick,
      tag = "quick_withdraw"
    )
    QuickActionButton(
      icon = Icons.Default.History,
      label = "Orders",
      color = Color(0xFF10B981),
      onClick = onTxClick,
      tag = "quick_history"
    )
    QuickActionButton(
      icon = Icons.Default.AccountBalanceWallet,
      label = "Gateways",
      color = Color(0xFFF59E0B),
      onClick = onToolsClick,
      tag = "quick_tools"
    )
    QuickActionButton(
      icon = Icons.Default.Groups,
      label = "Teams",
      color = Color(0xFFEC4899),
      onClick = onTeamClick,
      tag = "quick_team"
    )
  }
}

@Composable
private fun QuickActionButton(
  icon: ImageVector,
  label: String,
  color: Color,
  onClick: () -> Unit,
  tag: String
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .clickable(onClick = onClick)
      .testTag(tag)
  ) {
    Box(
      modifier = Modifier
        .size(48.dp)
        .clip(RoundedCornerShape(14.dp))
        .background(color.copy(alpha = 0.12f)),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = label,
        tint = color,
        modifier = Modifier.size(24.dp)
      )
    }
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = label,
      fontSize = 11.sp,
      fontWeight = FontWeight.Medium,
      color = YonoDarkBlue
    )
  }
}

@Composable
private fun CommissionSimulatorCard(
  amountInput: String,
  onAmountChanged: (String) -> Unit,
  calcAmount: Double,
  commission: Double,
  expectedReturn: Double,
  onTestDeposit: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("home_calculator_card"),
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
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Calculate,
            contentDescription = null,
            tint = YonoPrimaryPurple,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "3% Commission Calculator",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = YonoDarkBlue
          )
        }
        CommissionBadge(commissionText = "3% Fixed")
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = "Enter any sandbox amount to preview exact commission and earnings:",
        fontSize = 12.sp,
        color = Color(0xFF64748B)
      )

      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        OutlinedTextField(
          value = amountInput,
          onValueChange = { input ->
            if (input.all { it.isDigit() } && input.length <= 7) {
              onAmountChanged(input)
            }
          },
          prefix = { Text("₹", fontWeight = FontWeight.Bold) },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          singleLine = true,
          modifier = Modifier
            .weight(1f)
            .testTag("calc_amount_input"),
          shape = RoundedCornerShape(10.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = YonoPrimaryPurple,
            unfocusedBorderColor = Color(0xFFCBD5E1)
          )
        )

        Spacer(modifier = Modifier.width(8.dp))

        Button(
          onClick = onTestDeposit,
          enabled = calcAmount >= 100.0,
          colors = ButtonDefaults.buttonColors(containerColor = YonoPrimaryPurple),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.testTag("calc_test_deposit_button")
        ) {
          Text("Demo Buy")
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Instant Result breakdown box
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
          Text(
            text = "Order Amount",
            fontSize = 11.sp,
            color = Color(0xFF64748B)
          )
          Text(
            text = formatRupees(calcAmount),
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = YonoDarkBlue
          )
        }
        Text(text = "+", color = Color(0xFF94A3B8), fontWeight = FontWeight.Bold)
        Column {
          Text(
            text = "3% Commission",
            fontSize = 11.sp,
            color = Color(0xFF059669)
          )
          Text(
            text = formatRupees(commission),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF059669)
          )
        }
        Text(text = "=", color = Color(0xFF94A3B8), fontWeight = FontWeight.Bold)
        Column {
          Text(
            text = "Expected Earning",
            fontSize = 11.sp,
            color = YonoPrimaryPurple
          )
          Text(
            text = formatRupees(expectedReturn),
            fontSize = 14.sp,
            fontWeight = FontWeight.Black,
            color = YonoPrimaryPurple
          )
        }
      }
    }
  }
}

@Composable
private fun RecentTransactionRow(
  transaction: TransactionItem,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .testTag("recent_tx_${transaction.id}"),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(
              if (transaction.type == TransactionType.PAYIN) Color(0xFFDCFCE7) else Color(0xFFF3E8FF)
            ),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = if (transaction.type == TransactionType.PAYIN) Icons.Default.CallReceived else Icons.Default.CallMade,
            contentDescription = null,
            tint = if (transaction.type == TransactionType.PAYIN) Color(0xFF16A34A) else Color(0xFF7E22CE),
            modifier = Modifier.size(18.dp)
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = transaction.id,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace,
              color = YonoDarkBlue
            )
            Spacer(modifier = Modifier.width(6.dp))
            TypeBadge(type = transaction.type)
          }
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "${transaction.paymentRail} • ${transaction.formattedDate}",
            fontSize = 11.sp,
            color = Color(0xFF64748B)
          )
        }
      }

      Column(horizontalAlignment = Alignment.End) {
        Text(
          text = "${if (transaction.type == TransactionType.PAYIN) "+" else "-"}${formatRupees(transaction.amount)}",
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          color = if (transaction.type == TransactionType.PAYIN) Color(0xFF16A34A) else Color(0xFF7E22CE)
        )
        if (transaction.commission > 0) {
          Text(
            text = "+${formatRupees(transaction.commission)} comm.",
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF059669)
          )
        }
        Spacer(modifier = Modifier.height(2.dp))
        StatusBadge(status = transaction.status)
      }
    }
  }
}
