package com.tgt.trans.common.testhelpers.examples.part1.comparing.collections

import java.math.BigDecimal

data class SpaceShip(val name: String,
                     val description: String,
                     val range: BigDecimal,
                     val capacity: BigDecimal,
                     val serialNumber: String)

val millenniumFalcon = SpaceShip("Millennium Falcon", "Han Solo's ship", BigDecimal.TEN, BigDecimal.TEN, "123456")

val ghost = SpaceShip("Ghost", "seen in Rogue One", BigDecimal.TEN, BigDecimal.TEN, "Unknown")

val blockadeRunner1 = SpaceShip("CR90 corvette", "Blockade Runner", BigDecimal.TEN, BigDecimal.TEN, "123456")

val blockadeRunner2 = SpaceShip("CR90 corvette", "Blockade Runner", BigDecimal.TEN, BigDecimal.TEN, "123356")

val blockadeRunner3 = SpaceShip("CR90 corvette", "Blockade Runner", BigDecimal.TEN, BigDecimal.TEN, "123457")

