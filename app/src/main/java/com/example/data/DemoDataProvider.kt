package com.example.data

import com.example.model.DemoPaymentAccount
import com.example.model.DepositPackage
import com.example.model.TeamMember
import com.example.model.TeamStats
import com.example.model.TransactionItem
import com.example.model.TransactionStatus
import com.example.model.TransactionType
import com.example.model.UserProfile
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DemoDataProvider {

  val depositPackages = listOf(
    DepositPackage(
      id = "pkg_100_199",
      rangeLabel = "₹100–₹199",
      minAmount = 100.0,
      maxAmount = 199.0,
      defaultAmount = 150.0,
      commissionRate = 0.03,
      tag = "Starter"
    ),
    DepositPackage(
      id = "pkg_200_499",
      rangeLabel = "₹200–₹499",
      minAmount = 200.0,
      maxAmount = 499.0,
      defaultAmount = 350.0,
      commissionRate = 0.03,
      tag = "Quick Trade"
    ),
    DepositPackage(
      id = "pkg_500_999",
      rangeLabel = "₹500–₹999",
      minAmount = 500.0,
      maxAmount = 999.0,
      defaultAmount = 750.0,
      commissionRate = 0.03,
      isPopular = true,
      tag = "Popular"
    ),
    DepositPackage(
      id = "pkg_1000_1999",
      rangeLabel = "₹1,000–₹1,999",
      minAmount = 1000.0,
      maxAmount = 1999.0,
      defaultAmount = 1000.0,
      commissionRate = 0.03,
      isPopular = true,
      tag = "High Earn"
    ),
    DepositPackage(
      id = "pkg_2000_4999",
      rangeLabel = "₹2,000–₹4,999",
      minAmount = 2000.0,
      maxAmount = 4999.0,
      defaultAmount = 3000.0,
      commissionRate = 0.03,
      tag = "Merchant Choice"
    ),
    DepositPackage(
      id = "pkg_5000_9999",
      rangeLabel = "₹5,000–₹9,999",
      minAmount = 5000.0,
      maxAmount = 9999.0,
      defaultAmount = 5000.0,
      commissionRate = 0.03,
      tag = "Max Commission"
    )
  )

  val initialDemoAccounts = listOf(
    DemoPaymentAccount(
      id = "acc_paytm",
      provider = "Paytm",
      accountName = "Yono Retail Store (DEMO)",
      upiId = "demo.yonopay@paytm",
      isActive = true,
      totalVolume = 54200.0,
      badge = "DEMO UPI"
    ),
    DemoPaymentAccount(
      id = "acc_phonepe",
      provider = "PhonePe",
      accountName = "Yono Digital Hub (DEMO)",
      upiId = "demo.yonopay@ybl",
      isActive = true,
      totalVolume = 68450.0,
      badge = "DEMO QR"
    ),
    DemoPaymentAccount(
      id = "acc_gpay",
      provider = "Google Pay Business",
      accountName = "Yono Commercial Sandbox",
      upiId = "yonopay.biz@okhdfcbank",
      isActive = true,
      totalVolume = 41200.0,
      badge = "DEMO BIZ"
    ),
    DemoPaymentAccount(
      id = "acc_bharatpe",
      provider = "BharatPe Business",
      accountName = "Yono All-In-One QR (DEMO)",
      upiId = "yonopay@bharatpe",
      isActive = true,
      totalVolume = 32800.0,
      badge = "DEMO POS"
    ),
    DemoPaymentAccount(
      id = "acc_freecharge",
      provider = "Freecharge",
      accountName = "Yono Recharge Sandbox",
      upiId = "yonopay@freecharge",
      isActive = false,
      totalVolume = 12500.0,
      badge = "DEMO WALLET"
    ),
    DemoPaymentAccount(
      id = "acc_mobikwik",
      provider = "MobiKwik",
      accountName = "Yono Zip Pay (DEMO)",
      upiId = "yonopay@ikwik",
      isActive = true,
      totalVolume = 18900.0,
      badge = "DEMO ZIP"
    )
  )

  fun getInitialTransactions(): List<TransactionItem> {
    val now = System.currentTimeMillis()
    val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.ENGLISH)
    return listOf(
      TransactionItem(
        id = "YP-20261008-8921",
        amount = 1000.0,
        type = TransactionType.PAYIN,
        commission = 30.0,
        status = TransactionStatus.SUCCESS,
        timestamp = now - 15 * 60 * 1000,
        formattedDate = sdf.format(Date(now - 15 * 60 * 1000)),
        paymentRail = "PhonePe",
        remarks = "Sandbox Order Range ₹1,000–₹1,999"
      ),
      TransactionItem(
        id = "YP-20261008-8412",
        amount = 500.0,
        type = TransactionType.PAYIN,
        commission = 15.0,
        status = TransactionStatus.SUCCESS,
        timestamp = now - 45 * 60 * 1000,
        formattedDate = sdf.format(Date(now - 45 * 60 * 1000)),
        paymentRail = "Paytm",
        remarks = "Sandbox Order Range ₹500–₹999"
      ),
      TransactionItem(
        id = "YP-20261008-7934",
        amount = 1200.0,
        type = TransactionType.PAYOUT,
        commission = 0.0,
        status = TransactionStatus.SUCCESS,
        timestamp = now - 120 * 60 * 1000,
        formattedDate = sdf.format(Date(now - 120 * 60 * 1000)),
        paymentRail = "Google Pay Business",
        remarks = "Simulated Settlement Payout"
      ),
      TransactionItem(
        id = "YP-20261008-6510",
        amount = 3000.0,
        type = TransactionType.PAYIN,
        commission = 90.0,
        status = TransactionStatus.PENDING,
        timestamp = now - 180 * 60 * 1000,
        formattedDate = sdf.format(Date(now - 180 * 60 * 1000)),
        paymentRail = "BharatPe Business",
        remarks = "Awaiting demo webhook confirmation"
      ),
      TransactionItem(
        id = "YP-20261008-5433",
        amount = 250.0,
        type = TransactionType.PAYIN,
        commission = 7.5,
        status = TransactionStatus.SUCCESS,
        timestamp = now - 240 * 60 * 1000,
        formattedDate = sdf.format(Date(now - 240 * 60 * 1000)),
        paymentRail = "PhonePe",
        remarks = "Sandbox Order Range ₹200–₹499"
      ),
      TransactionItem(
        id = "YP-20261008-4109",
        amount = 150.0,
        type = TransactionType.PAYIN,
        commission = 4.5,
        status = TransactionStatus.FAILED,
        timestamp = now - 360 * 60 * 1000,
        formattedDate = sdf.format(Date(now - 360 * 60 * 1000)),
        paymentRail = "Freecharge",
        remarks = "Demo gateway timeout test"
      ),
      TransactionItem(
        id = "YP-20261008-3320",
        amount = 5000.0,
        type = TransactionType.PAYIN,
        commission = 150.0,
        status = TransactionStatus.SUCCESS,
        timestamp = now - 520 * 60 * 1000,
        formattedDate = sdf.format(Date(now - 520 * 60 * 1000)),
        paymentRail = "Google Pay Business",
        remarks = "High Tier Batch Settlement"
      )
    )
  }

  val initialTeamMembers = listOf(
    TeamMember(
      id = "tm_1",
      name = "Rahul Sharma",
      userId = "YP_MBR_1042",
      tier = "Tier 1",
      joinedDate = "02 Oct, 2026",
      totalOrders = 38,
      depositVolume = 42500.0,
      commissionContributed = 1275.0
    ),
    TeamMember(
      id = "tm_2",
      name = "Priya Verma",
      userId = "YP_MBR_1098",
      tier = "Tier 1",
      joinedDate = "03 Oct, 2026",
      totalOrders = 29,
      depositVolume = 31200.0,
      commissionContributed = 936.0
    ),
    TeamMember(
      id = "tm_3",
      name = "Amit Patel",
      userId = "YP_MBR_2104",
      tier = "Tier 1",
      joinedDate = "04 Oct, 2026",
      totalOrders = 44,
      depositVolume = 48900.0,
      commissionContributed = 1467.0
    ),
    TeamMember(
      id = "tm_4",
      name = "Sneha Kulkarni",
      userId = "YP_MBR_3051",
      tier = "Tier 2",
      joinedDate = "05 Oct, 2026",
      totalOrders = 18,
      depositVolume = 15400.0,
      commissionContributed = 462.0
    ),
    TeamMember(
      id = "tm_5",
      name = "Vikram Singh",
      userId = "YP_MBR_4119",
      tier = "Tier 2",
      joinedDate = "06 Oct, 2026",
      totalOrders = 13,
      depositVolume = 10500.0,
      commissionContributed = 315.0
    )
  )

  val initialUserProfile = UserProfile()
  val initialTeamStats = TeamStats()
}
