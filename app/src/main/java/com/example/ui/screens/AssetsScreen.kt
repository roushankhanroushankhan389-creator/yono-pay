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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CallMade
import androidx.compose.material.icons.filled.CallReceived
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SwitchAccount
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ScreenTab
import com.example.model.TransactionType
import com.example.ui.components.SandboxBanner
import com.example.ui.components.YonoCardGradient
import com.example.ui.components.formatRupees
import com.example.ui.theme.YonoDarkBlue
import com.example.ui.theme.YonoPrimaryPurple
import com.example.ui.theme.YonoSecondaryBlue
import com.example.viewmodel.YonoPayUiState

@Composable
fun AssetsScreen(
  uiState: YonoPayUiState,
  onNavigateTab: (ScreenTab) -> Unit,
  onOpenWithdraw: () -> Unit,
  onViewDepositHistory: () -> Unit,
  onViewWithdrawalHistory: () -> Unit,
  onViewAllTransactions: () -> Unit,
  onResetSandbox: () -> Unit,
  onOpenLogout: () -> Unit,
  modifier: Modifier = Modifier
) {
  val profile = uiState.userProfile
  val quotaProgress = (profile.availableQuota / profile.totalQuota).coerceIn(0.0, 1.0).toFloat()

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
        text = "USER ASSETS & QUOTA • Sandbox Merchant Account Profile"
      )
    }

    // Demo User ID Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("assets_user_card"),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .background(YonoCardGradient)
            .padding(18.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
              )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = profile.name,
                  fontSize = 17.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFF10B981))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                  Text(
                    text = "VERIFIED",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                  )
                }
              }

              Spacer(modifier = Modifier.height(3.dp))

              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "Demo ID: ",
                  fontSize = 12.sp,
                  color = Color.White.copy(alpha = 0.75f)
                )
                Text(
                  text = profile.userId,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  fontFamily = FontFamily.Monospace,
                  color = Color(0xFFFDE047)
                )
              }

              Spacer(modifier = Modifier.height(2.dp))

              Text(
                text = "${profile.tierName} • 3% Commission Tier",
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.85f)
              )
            }
          }
        }
      }
    }

    // Quota Management Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("assets_quota_card"),
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
                text = "Trading Quota",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = YonoDarkBlue
              )
              Text(
                text = "Used quota replenishes on settlement",
                fontSize = 11.sp,
                color = Color(0xFF64748B)
              )
            }
            Text(
              text = "${(quotaProgress * 100).toInt()}% Available",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = YonoSecondaryBlue
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          LinearProgressIndicator(
            progress = { quotaProgress },
            modifier = Modifier
              .fillMaxWidth()
              .height(10.dp)
              .clip(RoundedCornerShape(5.dp)),
            color = YonoSecondaryBlue,
            trackColor = Color(0xFFE2E8F0)
          )

          Spacer(modifier = Modifier.height(12.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(
                text = "Available Quota",
                fontSize = 11.sp,
                color = Color(0xFF64748B)
              )
              Text(
                text = formatRupees(profile.availableQuota),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF16A34A)
              )
            }
            Column(horizontalAlignment = Alignment.End) {
              Text(
                text = "Total Quota Limit",
                fontSize = 11.sp,
                color = Color(0xFF64748B)
              )
              Text(
                text = formatRupees(profile.totalQuota),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = YonoDarkBlue
              )
            }
          }
        }
      }
    }

    // Today's Earning & Fast Withdraw Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("assets_earning_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "Today's Earning",
              fontSize = 12.sp,
              color = Color(0xFF64748B)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = formatRupees(uiState.todayEarnings),
              fontSize = 22.sp,
              fontWeight = FontWeight.Black,
              color = Color(0xFF10B981)
            )
            Text(
              text = "Includes 3% instant commission",
              fontSize = 11.sp,
              color = Color(0xFF059669)
            )
          }

          Button(
            onClick = onOpenWithdraw,
            colors = ButtonDefaults.buttonColors(containerColor = YonoPrimaryPurple),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.testTag("assets_withdraw_btn")
          ) {
            Icon(
              imageVector = Icons.Default.Payments,
              contentDescription = null,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Withdraw")
          }
        }
      }
    }

    // History Navigation Tiles
    item {
      Text(
        text = "Ledger & History",
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = YonoDarkBlue
      )
    }

    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
          HistoryNavRow(
            icon = Icons.Default.CallReceived,
            iconTint = Color(0xFF10B981),
            title = "Deposit History",
            subtitle = "View all PAYIN orders & 3% commissions",
            onClick = onViewDepositHistory,
            tag = "nav_deposit_history"
          )
          HistoryNavRow(
            icon = Icons.Default.CallMade,
            iconTint = Color(0xFF7E22CE),
            title = "Withdrawal History",
            subtitle = "View simulated payouts & settlements",
            onClick = onViewWithdrawalHistory,
            tag = "nav_withdrawal_history"
          )
          HistoryNavRow(
            icon = Icons.Default.History,
            iconTint = YonoSecondaryBlue,
            title = "Transaction History",
            subtitle = "Complete sandbox audit ledger",
            onClick = onViewAllTransactions,
            tag = "nav_all_history"
          )
        }
      }
    }

    // Sandbox Administration
    item {
      Text(
        text = "Sandbox Environment",
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = YonoDarkBlue
      )
    }

    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedButton(
            onClick = onResetSandbox,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("reset_sandbox_data_btn"),
            shape = RoundedCornerShape(10.dp)
          ) {
            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Reset Sandbox Test Data")
          }

          Button(
            onClick = onOpenLogout,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("logout_button"),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
            shape = RoundedCornerShape(10.dp)
          ) {
            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Logout / Switch Demo User")
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

@Composable
private fun HistoryNavRow(
  icon: ImageVector,
  iconTint: Color,
  title: String,
  subtitle: String,
  onClick: () -> Unit,
  tag: String
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .padding(horizontal = 8.dp, vertical = 12.dp)
      .testTag(tag),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Box(
        modifier = Modifier
          .size(36.dp)
          .clip(CircleShape)
          .background(iconTint.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = iconTint,
          modifier = Modifier.size(18.dp)
        )
      }
      Spacer(modifier = Modifier.width(12.dp))
      Column {
        Text(
          text = title,
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          color = YonoDarkBlue
        )
        Text(
          text = subtitle,
          fontSize = 11.sp,
          color = Color(0xFF64748B)
        )
      }
    }

    Icon(
      imageVector = Icons.AutoMirrored.Filled.ArrowForward,
      contentDescription = null,
      tint = Color(0xFF94A3B8),
      modifier = Modifier.size(18.dp)
    )
  }
}
