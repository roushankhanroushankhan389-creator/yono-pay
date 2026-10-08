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
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import com.example.model.TeamMember
import com.example.ui.components.CommissionBadge
import com.example.ui.components.SandboxBanner
import com.example.ui.components.YonoCardGradient
import com.example.ui.components.formatRupees
import com.example.ui.theme.YonoDarkBlue
import com.example.ui.theme.YonoPrimaryPurple
import com.example.ui.theme.YonoSecondaryBlue
import com.example.ui.theme.YonoSuccessGreen
import com.example.viewmodel.YonoPayUiState

@Composable
fun TeamScreen(
  uiState: YonoPayUiState,
  onCopyInviteCode: () -> Unit,
  onShareInvite: () -> Unit,
  modifier: Modifier = Modifier
) {
  val stats = uiState.teamStats

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
        text = "TEAM REVENUE HUB • 3% Commission Network • Simulated Downline"
      )
    }

    // Gradient Team Revenue Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("team_hero_card"),
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
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = "Team Deposit",
                  color = Color.White.copy(alpha = 0.85f),
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = formatRupees(stats.teamDeposit),
                  color = Color.White,
                  fontSize = 26.sp,
                  fontWeight = FontWeight.Black
                )
              }

              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(20.dp))
                  .background(Color(0xFF10B981))
                  .padding(horizontal = 10.dp, vertical = 6.dp)
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.TrendingUp,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = "${(stats.commissionRate * 100).toInt()}% Commission Rate",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Total Team Commission Earned
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                .padding(12.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Total Commission Earned:",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
              )
              Text(
                text = formatRupees(stats.teamCommission),
                color = Color(0xFF4ADE80),
                fontSize = 18.sp,
                fontWeight = FontWeight.Black
              )
            }
          }
        }
      }
    }

    // 4 Stats Grid: Success Orders, Active Users, New Users, Commission Rate
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        TeamStatTile(
          title = "Success Orders",
          value = "${stats.successOrders}",
          icon = Icons.Default.Stars,
          color = Color(0xFF10B981),
          modifier = Modifier.weight(1f)
        )
        TeamStatTile(
          title = "Active Users",
          value = "${stats.activeUsers}",
          icon = Icons.Default.Groups,
          color = Color(0xFF3B82F6),
          modifier = Modifier.weight(1f)
        )
      }
    }

    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        TeamStatTile(
          title = "New Users",
          value = "${stats.newUsers}",
          icon = Icons.Default.GroupAdd,
          color = Color(0xFF8B5CF6),
          modifier = Modifier.weight(1f)
        )
        TeamStatTile(
          title = "Commission Rate",
          value = "${(stats.commissionRate * 100).toInt()}%",
          icon = Icons.Default.TrendingUp,
          color = Color(0xFFF59E0B),
          modifier = Modifier.weight(1f)
        )
      }
    }

    // Invitation Code Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("team_invite_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Invitation Code",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = YonoDarkBlue
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "Share with your network to earn 3% downline commission",
            fontSize = 11.sp,
            color = Color(0xFF64748B)
          )

          Spacer(modifier = Modifier.height(12.dp))

          // Code Display Box
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .background(Color(0xFFF1F5F9))
              .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(12.dp))
              .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "YOUR CODE",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF64748B)
              )
              Text(
                text = stats.invitationCode,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = YonoPrimaryPurple,
                letterSpacing = 1.sp
              )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              Button(
                onClick = onCopyInviteCode,
                colors = ButtonDefaults.buttonColors(containerColor = YonoPrimaryPurple),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("copy_invite_code_btn")
              ) {
                Icon(
                  imageVector = Icons.Default.ContentCopy,
                  contentDescription = null,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Copy", fontSize = 12.sp)
              }

              OutlinedButton(
                onClick = onShareInvite,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("share_invite_btn")
              ) {
                Icon(
                  imageVector = Icons.Default.Share,
                  contentDescription = null,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Share", fontSize = 12.sp)
              }
            }
          }
        }
      }
    }

    // Team Members List Header
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Team Members (${uiState.teamMembers.size})",
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          color = YonoDarkBlue
        )
        Text(
          text = "Tier 1 & Tier 2",
          fontSize = 12.sp,
          color = Color(0xFF64748B)
        )
      }
    }

    // List of team members
    items(uiState.teamMembers) { member ->
      TeamMemberCard(member = member)
    }

    item {
      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

@Composable
private fun TeamStatTile(
  title: String,
  value: String,
  icon: ImageVector,
  color: Color,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier,
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(
          text = title,
          fontSize = 12.sp,
          fontWeight = FontWeight.Medium,
          color = Color(0xFF64748B)
        )
        Box(
          modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.15f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(16.dp)
          )
        }
      }
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = value,
        fontSize = 20.sp,
        fontWeight = FontWeight.Black,
        color = YonoDarkBlue
      )
    }
  }
}

@Composable
private fun TeamMemberCard(member: TeamMember) {
  Card(
    modifier = Modifier.fillMaxWidth(),
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
            .size(36.dp)
            .clip(CircleShape)
            .background(Color(0xFFEDE9FE)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            tint = YonoPrimaryPurple,
            modifier = Modifier.size(20.dp)
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = member.name,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = YonoDarkBlue
            )
            Spacer(modifier = Modifier.width(6.dp))
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(if (member.tier == "Tier 1") Color(0xFFDCFCE7) else Color(0xFFE0F2FE))
                .padding(horizontal = 5.dp, vertical = 2.dp)
            ) {
              Text(
                text = member.tier,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = if (member.tier == "Tier 1") Color(0xFF15803D) else Color(0xFF0369A1)
              )
            }
          }
          Text(
            text = "${member.userId} • Joined ${member.joinedDate}",
            fontSize = 11.sp,
            color = Color(0xFF64748B)
          )
        }
      }

      Column(horizontalAlignment = Alignment.End) {
        Text(
          text = formatRupees(member.depositVolume),
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = YonoDarkBlue
        )
        Text(
          text = "+${formatRupees(member.commissionContributed)} comm.",
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF059669)
        )
      }
    }
  }
}
