package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.DemoDataProvider
import com.example.model.DemoPaymentAccount
import com.example.model.DepositPackage
import com.example.model.ScreenTab
import com.example.model.TeamMember
import com.example.model.TeamStats
import com.example.model.TransactionItem
import com.example.model.TransactionStatus
import com.example.model.TransactionType
import com.example.model.UserProfile
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class YonoPayUiState(
  val currentTab: ScreenTab = ScreenTab.HOME,
  val transactions: List<TransactionItem> = emptyList(),
  val depositPackages: List<DepositPackage> = emptyList(),
  val paymentAccounts: List<DemoPaymentAccount> = emptyList(),
  val teamMembers: List<TeamMember> = emptyList(),
  val teamStats: TeamStats = TeamStats(),
  val userProfile: UserProfile = UserProfile(),
  val todayEarnings: Double = 1845.0,
  val todayWithdraw: Double = 1200.0,
  val todayDeposit: Double = 9900.0,
  val inTransaction: Double = 3000.0,
  val dailyOrderRewards: Double = 120.0,
  val dailyAmountRewards: Double = 350.0,
  val commissionRate: Double = 0.03, // 3%
  val searchQuery: String = "",
  val filterType: TransactionType? = null,
  val filterStatus: TransactionStatus? = null,
  val selectedTransaction: TransactionItem? = null,
  val selectedAccountForDeposit: DemoPaymentAccount? = null,
  val isProcessing: Boolean = false,
  val snackbarMessage: String? = null,
  val showAddAccountDialog: Boolean = false,
  val showWithdrawDialog: Boolean = false,
  val showInviteDialog: Boolean = false,
  val showLogoutDialog: Boolean = false,
  val showSuccessAnimation: Boolean = false
) {
  val filteredTransactions: List<TransactionItem>
    get() = transactions.filter { item ->
      val matchesQuery = searchQuery.isBlank() ||
        item.id.contains(searchQuery, ignoreCase = true) ||
        item.paymentRail.contains(searchQuery, ignoreCase = true) ||
        item.remarks.contains(searchQuery, ignoreCase = true)
      val matchesType = filterType == null || item.type == filterType
      val matchesStatus = filterStatus == null || item.status == filterStatus
      matchesQuery && matchesType && matchesStatus
    }
}

class YonoPayViewModel : ViewModel() {

  private val _uiState = MutableStateFlow(
    YonoPayUiState(
      transactions = DemoDataProvider.getInitialTransactions(),
      depositPackages = DemoDataProvider.depositPackages,
      paymentAccounts = DemoDataProvider.initialDemoAccounts,
      teamMembers = DemoDataProvider.initialTeamMembers,
      teamStats = DemoDataProvider.initialTeamStats,
      userProfile = DemoDataProvider.initialUserProfile,
      selectedAccountForDeposit = DemoDataProvider.initialDemoAccounts.firstOrNull { it.isActive }
    )
  )
  val uiState: StateFlow<YonoPayUiState> = _uiState.asStateFlow()

  fun setTab(tab: ScreenTab) {
    _uiState.update { it.copy(currentTab = tab) }
  }

  fun setSearchQuery(query: String) {
    _uiState.update { it.copy(searchQuery = query) }
  }

  fun setFilterType(type: TransactionType?) {
    _uiState.update { it.copy(filterType = type) }
  }

  fun setFilterStatus(status: TransactionStatus?) {
    _uiState.update { it.copy(filterStatus = status) }
  }

  fun selectTransaction(transaction: TransactionItem?) {
    _uiState.update { it.copy(selectedTransaction = transaction) }
  }

  fun selectAccountForDeposit(account: DemoPaymentAccount) {
    _uiState.update { it.copy(selectedAccountForDeposit = account) }
  }

  fun clearSnackbar() {
    _uiState.update { it.copy(snackbarMessage = null) }
  }

  fun setShowAddAccountDialog(show: Boolean) {
    _uiState.update { it.copy(showAddAccountDialog = show) }
  }

  fun setShowWithdrawDialog(show: Boolean) {
    _uiState.update { it.copy(showWithdrawDialog = show) }
  }

  fun setShowInviteDialog(show: Boolean) {
    _uiState.update { it.copy(showInviteDialog = show) }
  }

  fun setShowLogoutDialog(show: Boolean) {
    _uiState.update { it.copy(showLogoutDialog = show) }
  }

  /**
   * Executes a simulated mock deposit order with 3% commission
   */
  fun executeMockDeposit(
    amount: Double,
    account: DemoPaymentAccount?,
    packageName: String = "Instant Demo Deposit"
  ) {
    val rate = _uiState.value.commissionRate
    val commission = amount * rate
    val now = System.currentTimeMillis()
    val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.ENGLISH)
    val randomSuffix = (1000..9999).random()
    val newId = "YP-${SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).format(Date())}-$randomSuffix"
    val railName = account?.provider ?: "PhonePe"

    viewModelScope.launch {
      _uiState.update { it.copy(isProcessing = true) }
      // Simulate rapid sandbox execution delay
      delay(600)

      val newTx = TransactionItem(
        id = newId,
        amount = amount,
        type = TransactionType.PAYIN,
        commission = commission,
        status = TransactionStatus.SUCCESS,
        timestamp = now,
        formattedDate = sdf.format(Date(now)),
        paymentRail = railName,
        remarks = "Sandbox Order: $packageName (+3% demo commission earned)"
      )

      _uiState.update { state ->
        val updatedTransactions = listOf(newTx) + state.transactions
        val newTodayEarnings = state.todayEarnings + commission
        val newTodayDeposit = state.todayDeposit + amount
        val newQuota = (state.userProfile.availableQuota - amount).coerceAtLeast(0.0)

        // update payment account volume
        val updatedAccounts = state.paymentAccounts.map { acc ->
          if (acc.id == account?.id) acc.copy(totalVolume = acc.totalVolume + amount) else acc
        }

        state.copy(
          transactions = updatedTransactions,
          todayEarnings = newTodayEarnings,
          todayDeposit = newTodayDeposit,
          paymentAccounts = updatedAccounts,
          userProfile = state.userProfile.copy(availableQuota = newQuota),
          isProcessing = false,
          showSuccessAnimation = true,
          snackbarMessage = "Demo Order Successful! ₹${amount.toInt()} credited. ₹${String.format(Locale.ENGLISH, "%.2f", commission)} commission earned (3%)."
        )
      }
    }
  }

  /**
   * Executes a simulated mock withdrawal
   */
  fun executeMockWithdrawal(amount: Double, account: DemoPaymentAccount) {
    val now = System.currentTimeMillis()
    val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.ENGLISH)
    val randomSuffix = (1000..9999).random()
    val newId = "YP-${SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).format(Date())}-$randomSuffix"

    viewModelScope.launch {
      _uiState.update { it.copy(isProcessing = true) }
      delay(600)

      val newTx = TransactionItem(
        id = newId,
        amount = amount,
        type = TransactionType.PAYOUT,
        commission = 0.0,
        status = TransactionStatus.SUCCESS,
        timestamp = now,
        formattedDate = sdf.format(Date(now)),
        paymentRail = account.provider,
        remarks = "Sandbox Payout to ${account.upiId}"
      )

      _uiState.update { state ->
        val updatedTransactions = listOf(newTx) + state.transactions
        val newTodayWithdraw = state.todayWithdraw + amount
        val newTodayEarnings = (state.todayEarnings - amount).coerceAtLeast(0.0)

        state.copy(
          transactions = updatedTransactions,
          todayWithdraw = newTodayWithdraw,
          todayEarnings = newTodayEarnings,
          isProcessing = false,
          showWithdrawDialog = false,
          snackbarMessage = "Simulated withdrawal of ₹${amount.toInt()} processed to ${account.provider} (${account.upiId})."
        )
      }
    }
  }

  /**
   * Update transaction status (Pending -> Success or Failed) to test sandbox edge cases
   */
  fun updateTransactionStatus(transactionId: String, newStatus: TransactionStatus) {
    _uiState.update { state ->
      val updated = state.transactions.map { item ->
        if (item.id == transactionId) item.copy(status = newStatus) else item
      }
      state.copy(
        transactions = updated,
        selectedTransaction = state.selectedTransaction?.let {
          if (it.id == transactionId) it.copy(status = newStatus) else it
        },
        snackbarMessage = "Transaction $transactionId status set to ${newStatus.name}."
      )
    }
  }

  /**
   * Add a new demo payment account
   */
  fun addDemoPaymentAccount(provider: String, accountName: String, upiId: String) {
    val newAccount = DemoPaymentAccount(
      id = "acc_${UUID.randomUUID().toString().take(6)}",
      provider = provider,
      accountName = if (accountName.isBlank()) "Merchant $provider (DEMO)" else "$accountName (DEMO)",
      upiId = if (upiId.isBlank()) "demo.${provider.lowercase().replace(" ", "")}@upi" else upiId,
      isActive = true,
      totalVolume = 0.0,
      badge = "DEMO CUSTOM"
    )

    _uiState.update { state ->
      state.copy(
        paymentAccounts = state.paymentAccounts + newAccount,
        showAddAccountDialog = false,
        snackbarMessage = "Added demo account: ${newAccount.provider} (${newAccount.upiId})"
      )
    }
  }

  /**
   * Toggle active status of a demo payment account
   */
  fun toggleAccountActive(accountId: String) {
    _uiState.update { state ->
      val updated = state.paymentAccounts.map { acc ->
        if (acc.id == accountId) acc.copy(isActive = !acc.isActive) else acc
      }
      val target = updated.find { it.id == accountId }
      state.copy(
        paymentAccounts = updated,
        snackbarMessage = "${target?.provider} demo account set to ${if (target?.isActive == true) "Active" else "Inactive"}."
      )
    }
  }

  /**
   * Reset sandbox back to fresh demo state
   */
  fun resetSandbox() {
    _uiState.value = YonoPayUiState(
      transactions = DemoDataProvider.getInitialTransactions(),
      depositPackages = DemoDataProvider.depositPackages,
      paymentAccounts = DemoDataProvider.initialDemoAccounts,
      teamMembers = DemoDataProvider.initialTeamMembers,
      teamStats = DemoDataProvider.initialTeamStats,
      userProfile = DemoDataProvider.initialUserProfile,
      selectedAccountForDeposit = DemoDataProvider.initialDemoAccounts.firstOrNull { it.isActive },
      snackbarMessage = "Demo sandbox reset to initial test state."
    )
  }

  /**
   * Switch demo user persona
   */
  fun switchDemoUser(persona: String) {
    val updatedProfile = when (persona) {
      "VIP Agent" -> UserProfile(
        userId = "YP_VIP_99014",
        name = "VIP Agency Manager",
        tierName = "VIP Tier 3 (Super Agent)",
        totalQuota = 250000.0,
        availableQuota = 195000.0,
        inTransaction = 12000.0,
        dailyOrderRewards = 450.0,
        dailyAmountRewards = 890.0,
        commissionRate = 0.03,
        invitationCode = "YONOVIP77"
      )
      "New Trader" -> UserProfile(
        userId = "YP_NEW_10204",
        name = "New Sandbox Trader",
        tierName = "Basic Tier 1",
        totalQuota = 25000.0,
        availableQuota = 24500.0,
        inTransaction = 0.0,
        dailyOrderRewards = 25.0,
        dailyAmountRewards = 50.0,
        commissionRate = 0.03,
        invitationCode = "YONONEW01"
      )
      else -> DemoDataProvider.initialUserProfile
    }

    _uiState.update {
      it.copy(
        userProfile = updatedProfile,
        showLogoutDialog = false,
        snackbarMessage = "Switched to demo persona: ${updatedProfile.name} (${updatedProfile.tierName})"
      )
    }
  }
}
