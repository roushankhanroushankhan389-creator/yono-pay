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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallMade
import androidx.compose.material.icons.filled.CallReceived
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.TransactionItem
import com.example.model.TransactionStatus
import com.example.model.TransactionType
import com.example.ui.components.SandboxBanner
import com.example.ui.components.StatusBadge
import com.example.ui.components.TypeBadge
import com.example.ui.components.formatRupees
import com.example.ui.theme.YonoDarkBlue
import com.example.ui.theme.YonoPrimaryPurple
import com.example.ui.theme.YonoSecondaryBlue
import com.example.viewmodel.YonoPayUiState

@Composable
fun TransactionsScreen(
  uiState: YonoPayUiState,
  onSearchQueryChanged: (String) -> Unit,
  onFilterTypeChanged: (TransactionType?) -> Unit,
  onFilterStatusChanged: (TransactionStatus?) -> Unit,
  onSelectTransaction: (TransactionItem?) -> Unit,
  onUpdateTransactionStatus: (String, TransactionStatus) -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF8FAFC))
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Spacer(modifier = Modifier.height(8.dp))
      SandboxBanner(
        text = "TRANSACTION LEDGER • Simulated PAYIN & PAYOUT orders"
      )
    }

    // Search bar
    item {
      OutlinedTextField(
        value = uiState.searchQuery,
        onValueChange = onSearchQueryChanged,
        placeholder = { Text("Search by ID (e.g. YP-20261008...)") },
        leadingIcon = {
          Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = Color(0xFF64748B)
          )
        },
        trailingIcon = {
          if (uiState.searchQuery.isNotEmpty()) {
            IconButton(onClick = { onSearchQueryChanged("") }) {
              Icon(Icons.Default.Close, contentDescription = "Clear search")
            }
          }
        },
        singleLine = true,
        modifier = Modifier
          .fillMaxWidth()
          .testTag("tx_search_input"),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = Color.White,
          unfocusedContainerColor = Color.White,
          focusedBorderColor = YonoPrimaryPurple,
          unfocusedBorderColor = Color(0xFFE2E8F0)
        )
      )
    }

    // Filter Chips Row
    item {
      FilterChipsRow(
        filterType = uiState.filterType,
        filterStatus = uiState.filterStatus,
        onFilterTypeChanged = onFilterTypeChanged,
        onFilterStatusChanged = onFilterStatusChanged
      )
    }

    // Count summary
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Showing ${uiState.filteredTransactions.size} of ${uiState.transactions.size} records",
          fontSize = 12.sp,
          color = Color(0xFF64748B),
          fontWeight = FontWeight.Medium
        )
      }
    }

    if (uiState.filteredTransactions.isEmpty()) {
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(
              imageVector = Icons.Default.FilterList,
              contentDescription = null,
              tint = Color(0xFF94A3B8),
              modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = "No matching sandbox transactions",
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              color = YonoDarkBlue
            )
            Text(
              text = "Try clearing filters or search query",
              fontSize = 12.sp,
              color = Color(0xFF64748B)
            )
          }
        }
      }
    } else {
      items(uiState.filteredTransactions) { item ->
        TransactionCard(
          item = item,
          onClick = { onSelectTransaction(item) }
        )
      }
    }

    item {
      Spacer(modifier = Modifier.height(24.dp))
    }
  }

  // Detail / Receipt Modal Dialog
  uiState.selectedTransaction?.let { tx ->
    TransactionReceiptDialog(
      transaction = tx,
      onDismiss = { onSelectTransaction(null) },
      onSetStatus = { newStatus ->
        onUpdateTransactionStatus(tx.id, newStatus)
      }
    )
  }
}

@Composable
private fun FilterChipsRow(
  filterType: TransactionType?,
  filterStatus: TransactionStatus?,
  onFilterTypeChanged: (TransactionType?) -> Unit,
  onFilterStatusChanged: (TransactionStatus?) -> Unit
) {
  LazyRow(
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    // All Chip
    item {
      val isAll = filterType == null && filterStatus == null
      FilterChipPill(
        label = "All",
        isSelected = isAll,
        onClick = {
          onFilterTypeChanged(null)
          onFilterStatusChanged(null)
        }
      )
    }

    // PAYIN Chip
    item {
      FilterChipPill(
        label = "PAYIN",
        isSelected = filterType == TransactionType.PAYIN,
        onClick = {
          onFilterTypeChanged(if (filterType == TransactionType.PAYIN) null else TransactionType.PAYIN)
        }
      )
    }

    // PAYOUT Chip
    item {
      FilterChipPill(
        label = "PAYOUT",
        isSelected = filterType == TransactionType.PAYOUT,
        onClick = {
          onFilterTypeChanged(if (filterType == TransactionType.PAYOUT) null else TransactionType.PAYOUT)
        }
      )
    }

    // Success Chip
    item {
      FilterChipPill(
        label = "Success",
        isSelected = filterStatus == TransactionStatus.SUCCESS,
        onClick = {
          onFilterStatusChanged(if (filterStatus == TransactionStatus.SUCCESS) null else TransactionStatus.SUCCESS)
        }
      )
    }

    // Pending Chip
    item {
      FilterChipPill(
        label = "Pending",
        isSelected = filterStatus == TransactionStatus.PENDING,
        onClick = {
          onFilterStatusChanged(if (filterStatus == TransactionStatus.PENDING) null else TransactionStatus.PENDING)
        }
      )
    }

    // Failed Chip
    item {
      FilterChipPill(
        label = "Failed",
        isSelected = filterStatus == TransactionStatus.FAILED,
        onClick = {
          onFilterStatusChanged(if (filterStatus == TransactionStatus.FAILED) null else TransactionStatus.FAILED)
        }
      )
    }
  }
}

@Composable
private fun FilterChipPill(
  label: String,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .clip(RoundedCornerShape(20.dp))
      .background(if (isSelected) YonoPrimaryPurple else Color.White)
      .border(
        1.dp,
        if (isSelected) YonoPrimaryPurple else Color(0xFFCBD5E1),
        RoundedCornerShape(20.dp)
      )
      .clickable(onClick = onClick)
      .padding(horizontal = 14.dp, vertical = 7.dp),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = label,
      fontSize = 12.sp,
      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
      color = if (isSelected) Color.White else YonoDarkBlue
    )
  }
}

@Composable
private fun TransactionCard(
  item: TransactionItem,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .testTag("tx_card_${item.id}"),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      // Top row: ID, Type, Status
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          TypeBadge(type = item.type)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = item.id,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            color = YonoDarkBlue
          )
        }
        StatusBadge(status = item.status)
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Middle: Amount & Commission
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "${item.paymentRail} • ${item.formattedDate}",
            fontSize = 11.sp,
            color = Color(0xFF64748B)
          )
          if (item.remarks.isNotBlank()) {
            Text(
              text = item.remarks,
              fontSize = 11.sp,
              color = Color(0xFF94A3B8),
              maxLines = 1
            )
          }
        }

        Column(horizontalAlignment = Alignment.End) {
          Text(
            text = "${if (item.type == TransactionType.PAYIN) "+" else "-"}${formatRupees(item.amount)}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Black,
            color = if (item.type == TransactionType.PAYIN) Color(0xFF16A34A) else Color(0xFF7E22CE)
          )
          if (item.commission > 0) {
            Text(
              text = "+${formatRupees(item.commission)} comm. (3%)",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF059669)
            )
          }
        }
      }
    }
  }
}

@Composable
private fun TransactionReceiptDialog(
  transaction: TransactionItem,
  onDismiss: () -> Unit,
  onSetStatus: (TransactionStatus) -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.ReceiptLong,
          contentDescription = null,
          tint = YonoPrimaryPurple,
          modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Sandbox Receipt",
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
        SandboxBanner(text = "SIMULATED RECORD • Sandbox Audit Trail")

        Spacer(modifier = Modifier.height(4.dp))

        ReceiptLine(label = "Transaction ID", value = transaction.id, isMonospace = true)
        ReceiptLine(label = "Type", value = transaction.type.name)
        ReceiptLine(label = "Status", value = transaction.status.name)
        ReceiptLine(label = "Gateway Rail", value = transaction.paymentRail)
        ReceiptLine(label = "Date & Time", value = transaction.formattedDate)
        ReceiptLine(label = "Order Amount", value = formatRupees(transaction.amount))

        if (transaction.commission > 0) {
          ReceiptLine(
            label = "Commission (3%)",
            value = "+${formatRupees(transaction.commission)}",
            valueColor = Color(0xFF059669)
          )
          ReceiptLine(
            label = "Expected Total",
            value = formatRupees(transaction.expectedEarning),
            valueColor = YonoPrimaryPurple,
            isBold = true
          )
        }

        if (transaction.remarks.isNotBlank()) {
          ReceiptLine(label = "Remarks", value = transaction.remarks)
        }

        // If Pending, allow interactive simulation of status transition
        if (transaction.status == TransactionStatus.PENDING) {
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Sandbox Status Override:",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = YonoDarkBlue
          )
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Button(
              onClick = { onSetStatus(TransactionStatus.SUCCESS) },
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.weight(1f)
            ) {
              Text("Mark Success", fontSize = 11.sp)
            }
            Button(
              onClick = { onSetStatus(TransactionStatus.FAILED) },
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.weight(1f)
            ) {
              Text("Mark Failed", fontSize = 11.sp)
            }
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
private fun ReceiptLine(
  label: String,
  value: String,
  isMonospace: Boolean = false,
  valueColor: Color = YonoDarkBlue,
  isBold: Boolean = false
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(
      text = label,
      fontSize = 12.sp,
      color = Color(0xFF64748B)
    )
    Text(
      text = value,
      fontSize = 12.sp,
      fontWeight = if (isBold) FontWeight.Bold else FontWeight.SemiBold,
      fontFamily = if (isMonospace) FontFamily.Monospace else FontFamily.Default,
      color = valueColor
    )
  }
}
