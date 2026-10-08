package com.example

import com.example.data.DemoDataProvider
import com.example.model.TransactionStatus
import com.example.model.TransactionType
import com.example.viewmodel.YonoPayViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class YonoPayUnitTest {

  @Test
  fun testCommissionCalculationExact() {
    val amount = 1000.0
    val rate = 0.03 // 3%
    val commission = amount * rate
    val expectedReturn = amount + commission

    assertEquals(30.0, commission, 0.001)
    assertEquals(1030.0, expectedReturn, 0.001)
  }

  @Test
  fun testDepositPackagesRanges() {
    val packages = DemoDataProvider.depositPackages
    assertEquals(6, packages.size)

    val pkg1 = packages[0]
    assertEquals("₹100–₹199", pkg1.rangeLabel)
    assertEquals(100.0, pkg1.minAmount, 0.001)
    assertEquals(199.0, pkg1.maxAmount, 0.001)

    val pkg4 = packages[3]
    assertEquals("₹1,000–₹1,999", pkg4.rangeLabel)
    assertEquals(1000.0, pkg4.minAmount, 0.001)
    assertEquals(30.0, pkg4.calculateCommission(1000.0), 0.001)
    assertEquals(1030.0, pkg4.calculateExpectedEarning(1000.0), 0.001)
  }

  @Test
  fun testInitialDemoAccountsPresent() {
    val accounts = DemoDataProvider.initialDemoAccounts
    val providers = accounts.map { it.provider }

    assertTrue(providers.contains("Paytm"))
    assertTrue(providers.contains("PhonePe"))
    assertTrue(providers.contains("Google Pay Business"))
    assertTrue(providers.contains("BharatPe Business"))
    assertTrue(providers.contains("Freecharge"))
    assertTrue(providers.contains("MobiKwik"))
  }

  @Test
  fun testTeamStatsAndCommissionRate() {
    val stats = DemoDataProvider.initialTeamStats
    assertEquals(0.03, stats.commissionRate, 0.0001)
    assertEquals(148500.0, stats.teamDeposit, 0.01)
    assertEquals(4455.0, stats.teamCommission, 0.01)
    assertEquals(142, stats.successOrders)
    assertEquals(28, stats.activeUsers)
    assertEquals(8, stats.newUsers)
    assertEquals("YONO8899", stats.invitationCode)
  }
}
