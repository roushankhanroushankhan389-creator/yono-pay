package com.example

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.ScreenTab
import com.example.model.TransactionType
import com.example.ui.components.LogoutDialog
import com.example.ui.components.ShareInviteDialog
import com.example.ui.components.TopHeader
import com.example.ui.components.WithdrawDialog
import com.example.ui.components.YonoBottomNav
import com.example.ui.screens.AssetsScreen
import com.example.ui.screens.DepositScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.TeamScreen
import com.example.ui.screens.ToolsScreen
import com.example.ui.screens.TransactionsScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.YonoPayViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        YonoPayApp()
      }
    }
  }
}

@Composable
fun YonoPayApp(
  viewModel: YonoPayViewModel = viewModel()
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val snackbarHostState = remember { SnackbarHostState() }
  val context = LocalContext.current

  // Handle snackbar messages from ViewModel
  LaunchedEffect(uiState.snackbarMessage) {
    uiState.snackbarMessage?.let { msg ->
      snackbarHostState.showSnackbar(
        message = msg,
        duration = SnackbarDuration.Short
      )
      viewModel.clearSnackbar()
    }
  }

  // Handle BackHandler to return to HOME screen from sub-screens
  BackHandler(enabled = uiState.currentTab != ScreenTab.HOME) {
    viewModel.setTab(ScreenTab.HOME)
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      TopHeader(
        userProfile = uiState.userProfile,
        onResetSandbox = { viewModel.resetSandbox() },
        onOpenNotifications = {
          Toast.makeText(context, "All sandbox notifications up to date", Toast.LENGTH_SHORT).show()
        }
      )
    },
    bottomBar = {
      YonoBottomNav(
        currentTab = if (uiState.currentTab == ScreenTab.TRANSACTIONS) ScreenTab.HOME else uiState.currentTab,
        onTabSelected = { tab ->
          viewModel.setTab(tab)
        }
      )
    },
    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .background(Color(0xFFF8FAFC))
    ) {
      when (uiState.currentTab) {
        ScreenTab.HOME -> {
          HomeScreen(
            uiState = uiState,
            onNavigateTab = { tab -> viewModel.setTab(tab) },
            onOpenWithdraw = { viewModel.setShowWithdrawDialog(true) },
            onSelectTransaction = { tx -> viewModel.selectTransaction(tx) },
            onQuickSimulateDeposit = { amount ->
              viewModel.executeMockDeposit(
                amount = amount,
                account = uiState.selectedAccountForDeposit,
                packageName = "Calculator Quick Buy"
              )
            }
          )
        }

        ScreenTab.DEPOSIT -> {
          DepositScreen(
            uiState = uiState,
            onExecuteDeposit = { amount, account, pkgName ->
              viewModel.executeMockDeposit(amount, account, pkgName)
            }
          )
        }

        ScreenTab.TRANSACTIONS -> {
          TransactionsScreen(
            uiState = uiState,
            onSearchQueryChanged = { query -> viewModel.setSearchQuery(query) },
            onFilterTypeChanged = { type -> viewModel.setFilterType(type) },
            onFilterStatusChanged = { status -> viewModel.setFilterStatus(status) },
            onSelectTransaction = { tx -> viewModel.selectTransaction(tx) },
            onUpdateTransactionStatus = { id, status ->
              viewModel.updateTransactionStatus(id, status)
            }
          )
        }

        ScreenTab.TOOLS -> {
          ToolsScreen(
            uiState = uiState,
            onToggleAccountActive = { id -> viewModel.toggleAccountActive(id) },
            onAddAccount = { provider, name, upi ->
              viewModel.addDemoPaymentAccount(provider, name, upi)
            }
          )
        }

        ScreenTab.TEAM -> {
          TeamScreen(
            uiState = uiState,
            onCopyInviteCode = {
              copyToClipboard(context, "Yono Pay Code", uiState.teamStats.invitationCode)
              Toast.makeText(context, "Invitation code copied!", Toast.LENGTH_SHORT).show()
            },
            onShareInvite = { viewModel.setShowInviteDialog(true) }
          )
        }

        ScreenTab.ASSETS -> {
          AssetsScreen(
            uiState = uiState,
            onNavigateTab = { tab -> viewModel.setTab(tab) },
            onOpenWithdraw = { viewModel.setShowWithdrawDialog(true) },
            onViewDepositHistory = {
              viewModel.setFilterType(TransactionType.PAYIN)
              viewModel.setFilterStatus(null)
              viewModel.setTab(ScreenTab.TRANSACTIONS)
            },
            onViewWithdrawalHistory = {
              viewModel.setFilterType(TransactionType.PAYOUT)
              viewModel.setFilterStatus(null)
              viewModel.setTab(ScreenTab.TRANSACTIONS)
            },
            onViewAllTransactions = {
              viewModel.setFilterType(null)
              viewModel.setFilterStatus(null)
              viewModel.setTab(ScreenTab.TRANSACTIONS)
            },
            onResetSandbox = { viewModel.resetSandbox() },
            onOpenLogout = { viewModel.setShowLogoutDialog(true) }
          )
        }
      }
    }
  }

  // Withdraw Dialog
  if (uiState.showWithdrawDialog) {
    WithdrawDialog(
      availableEarnings = uiState.todayEarnings,
      accounts = uiState.paymentAccounts,
      isProcessing = uiState.isProcessing,
      onDismiss = { viewModel.setShowWithdrawDialog(false) },
      onConfirmWithdraw = { amount, acc ->
        viewModel.executeMockWithdrawal(amount, acc)
      }
    )
  }

  // Logout / Persona Dialog
  if (uiState.showLogoutDialog) {
    LogoutDialog(
      onDismiss = { viewModel.setShowLogoutDialog(false) },
      onSwitchUser = { persona ->
        viewModel.switchDemoUser(persona)
      }
    )
  }

  // Invite Dialog
  if (uiState.showInviteDialog) {
    ShareInviteDialog(
      inviteCode = uiState.teamStats.invitationCode,
      onDismiss = { viewModel.setShowInviteDialog(false) },
      onCopy = {
        copyToClipboard(context, "Yono Pay Code", uiState.teamStats.invitationCode)
        Toast.makeText(context, "Copied code ${uiState.teamStats.invitationCode}!", Toast.LENGTH_SHORT).show()
        viewModel.setShowInviteDialog(false)
      }
    )
  }
}

private fun copyToClipboard(context: Context, label: String, text: String) {
  val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
  val clip = ClipData.newPlainText(label, text)
  clipboard.setPrimaryClip(clip)
}
