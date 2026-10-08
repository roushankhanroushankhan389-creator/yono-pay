package com.example.model

enum class TransactionType {
  PAYIN,
  PAYOUT
}

enum class TransactionStatus {
  PENDING,
  SUCCESS,
  FAILED
}

data class TransactionItem(
  val id: String,
  val amount: Double,
  val type: TransactionType,
  val commission: Double,
  val status: TransactionStatus,
  val timestamp: Long,
  val formattedDate: String,
  val paymentRail: String,
  val remarks: String = ""
) {
  val expectedEarning: Double
    get() = if (type == TransactionType.PAYIN) amount + commission else amount
}

data class DepositPackage(
  val id: String,
  val rangeLabel: String,
  val minAmount: Double,
  val maxAmount: Double,
  val defaultAmount: Double,
  val commissionRate: Double = 0.03,
  val isPopular: Boolean = false,
  val tag: String = ""
) {
  fun calculateCommission(amount: Double): Double = amount * commissionRate
  fun calculateExpectedEarning(amount: Double): Double = amount + calculateCommission(amount)
}

data class DemoPaymentAccount(
  val id: String,
  val provider: String,
  val accountName: String,
  val upiId: String,
  val isActive: Boolean = true,
  val totalVolume: Double = 0.0,
  val badge: String = "DEMO"
)

data class TeamMember(
  val id: String,
  val name: String,
  val userId: String,
  val tier: String,
  val joinedDate: String,
  val totalOrders: Int,
  val depositVolume: Double,
  val commissionContributed: Double
)

data class TeamStats(
  val teamDeposit: Double = 148500.0,
  val teamCommission: Double = 4455.0,
  val successOrders: Int = 142,
  val activeUsers: Int = 28,
  val newUsers: Int = 8,
  val invitationCode: String = "YONO8899",
  val commissionRate: Double = 0.03
)

data class UserProfile(
  val userId: String = "YP_MCH_84920",
  val name: String = "Demo Partner Merchant",
  val tierName: String = "VIP Tier 2",
  val totalQuota: Double = 100000.0,
  val availableQuota: Double = 68500.0,
  val inTransaction: Double = 4250.0,
  val dailyOrderRewards: Double = 120.0,
  val dailyAmountRewards: Double = 350.0,
  val commissionRate: Double = 0.03,
  val invitationCode: String = "YONO8899"
)

enum class ScreenTab {
  HOME,
  DEPOSIT,
  TRANSACTIONS,
  TOOLS,
  TEAM,
  ASSETS
}
