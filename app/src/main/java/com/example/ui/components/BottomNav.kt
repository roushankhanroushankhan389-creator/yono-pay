package com.example.ui.components

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ScreenTab
import com.example.ui.theme.YonoPrimaryPurple
import com.example.ui.theme.YonoSecondaryBlue

@Composable
fun YonoBottomNav(
  currentTab: ScreenTab,
  onTabSelected: (ScreenTab) -> Unit,
  modifier: Modifier = Modifier
) {
  NavigationBar(
    modifier = modifier.navigationBarsPadding(),
    containerColor = Color.White,
    tonalElevation = 8.dp
  ) {
    val items = listOf(
      NavigationItem(
        tab = ScreenTab.HOME,
        label = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        testTag = "nav_home"
      ),
      NavigationItem(
        tab = ScreenTab.DEPOSIT,
        label = "Deposit",
        selectedIcon = Icons.Filled.AccountBalanceWallet,
        unselectedIcon = Icons.Outlined.AccountBalanceWallet,
        testTag = "nav_deposit"
      ),
      NavigationItem(
        tab = ScreenTab.TOOLS,
        label = "Tools",
        selectedIcon = Icons.Filled.Build,
        unselectedIcon = Icons.Outlined.Build,
        testTag = "nav_tools"
      ),
      NavigationItem(
        tab = ScreenTab.TEAM,
        label = "Teams",
        selectedIcon = Icons.Filled.Groups,
        unselectedIcon = Icons.Outlined.Groups,
        testTag = "nav_teams"
      ),
      NavigationItem(
        tab = ScreenTab.ASSETS,
        label = "Assets",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
        testTag = "nav_assets"
      )
    )

    items.forEach { item ->
      val selected = currentTab == item.tab
      NavigationBarItem(
        selected = selected,
        onClick = { onTabSelected(item.tab) },
        icon = {
          Icon(
            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
            contentDescription = item.label,
            modifier = Modifier.size(22.dp)
          )
        },
        label = {
          Text(
            text = item.label,
            fontSize = 11.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
          )
        },
        colors = NavigationBarItemDefaults.colors(
          selectedIconColor = YonoPrimaryPurple,
          selectedTextColor = YonoPrimaryPurple,
          indicatorColor = YonoPrimaryPurple.copy(alpha = 0.15f),
          unselectedIconColor = Color(0xFF64748B),
          unselectedTextColor = Color(0xFF64748B)
        ),
        modifier = Modifier.testTag(item.testTag)
      )
    }
  }
}

private data class NavigationItem(
  val tab: ScreenTab,
  val label: String,
  val selectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
  val unselectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
  val testTag: String
)
