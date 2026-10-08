package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.TransactionStatus
import com.example.model.TransactionType
import com.example.ui.theme.YonoDarkBlue
import com.example.ui.theme.YonoErrorRed
import com.example.ui.theme.YonoPrimaryPurple
import com.example.ui.theme.YonoSecondaryBlue
import com.example.ui.theme.YonoSuccessGreen
import com.example.ui.theme.YonoWarningAmber
import java.text.NumberFormat
import java.util.Locale

fun formatRupees(amount: Double): String {
  val formatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
  val formatted = formatter.format(amount)
  // Format as ₹1,000.00 or ₹1,000
  return if (amount % 1.0 == 0.0) {
    formatted.replace(".00", "")
  } else {
    formatted
  }
}

val YonoFintechGradient = Brush.linearGradient(
  colors = listOf(
    Color(0xFF4C1D95), // Deep Violet
    Color(0xFF2563EB), // Vibrant Blue
    Color(0xFF0284C7)  // Sky Accent
  )
)

val YonoHeaderGradient = Brush.verticalGradient(
  colors = listOf(
    Color(0xFF3B0764),
    Color(0xFF1E3A8A)
  )
)

val YonoCardGradient = Brush.linearGradient(
  colors = listOf(
    Color(0xFF581C87),
    Color(0xFF1E40AF)
  )
)

@Composable
fun SandboxBanner(
  modifier: Modifier = Modifier,
  text: String = "DEMO SANDBOX • Mock transactions only • No real money"
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(8.dp))
      .background(Color(0xFFFEF3C7))
      .border(1.dp, Color(0xFFFDE68A), RoundedCornerShape(8.dp))
      .padding(horizontal = 12.dp, vertical = 6.dp)
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Icon(
        imageVector = Icons.Default.Shield,
        contentDescription = "Sandbox Shield",
        tint = Color(0xFFB45309),
        modifier = Modifier.size(16.dp)
      )
      Spacer(modifier = Modifier.width(6.dp))
      Text(
        text = text,
        color = Color(0xFF92400E),
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        maxLines = 1
      )
    }
  }
}

@Composable
fun StatusBadge(status: TransactionStatus, modifier: Modifier = Modifier) {
  val (bgColor, textColor, label) = when (status) {
    TransactionStatus.SUCCESS -> Triple(Color(0xFFDCFCE7), Color(0xFF15803D), "Success")
    TransactionStatus.PENDING -> Triple(Color(0xFFFEF3C7), Color(0xFFB45309), "Pending")
    TransactionStatus.FAILED -> Triple(Color(0xFFFEE2E2), Color(0xFFB91C1C), "Failed")
  }

  Box(
    modifier = modifier
      .clip(RoundedCornerShape(12.dp))
      .background(bgColor)
      .padding(horizontal = 10.dp, vertical = 4.dp),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = label,
      color = textColor,
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold
    )
  }
}

@Composable
fun TypeBadge(type: TransactionType, modifier: Modifier = Modifier) {
  val (bgColor, textColor, label) = when (type) {
    TransactionType.PAYIN -> Triple(Color(0xFFE0F2FE), Color(0xFF0369A1), "PAYIN")
    TransactionType.PAYOUT -> Triple(Color(0xFFF3E8FF), Color(0xFF6B21A8), "PAYOUT")
  }

  Box(
    modifier = modifier
      .clip(RoundedCornerShape(8.dp))
      .background(bgColor)
      .padding(horizontal = 8.dp, vertical = 3.dp),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = label,
      color = textColor,
      fontSize = 10.sp,
      fontWeight = FontWeight.ExtraBold,
      fontFamily = FontFamily.Monospace
    )
  }
}

@Composable
fun CommissionBadge(
  commissionText: String = "3% Commission",
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(12.dp))
      .background(Color(0xFF10B981).copy(alpha = 0.15f))
      .border(1.dp, Color(0xFF10B981).copy(alpha = 0.4f), RoundedCornerShape(12.dp))
      .padding(horizontal = 8.dp, vertical = 3.dp)
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Box(
        modifier = Modifier
          .size(6.dp)
          .clip(CircleShape)
          .background(Color(0xFF10B981))
      )
      Spacer(modifier = Modifier.width(4.dp))
      Text(
        text = commissionText,
        color = Color(0xFF047857),
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold
      )
    }
  }
}
