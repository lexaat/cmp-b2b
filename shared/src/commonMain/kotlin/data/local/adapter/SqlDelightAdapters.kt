package data.local.adapter

import app.cash.sqldelight.ColumnAdapter
import b2b.database.ClientEntity
import b2b.database.AccountEntity

// 🔷 ClientEntity адаптер
val clientEntityAdapter = ClientEntity.Adapter(
    cards_countAdapter = IntAdapter,
    k2_countAdapter = IntAdapter
)

// 🔷 AccountEntity адаптер
val accountEntityAdapter = AccountEntity.Adapter(
    stateAdapter = IntAdapter,
    canpayAdapter = IntAdapter,
    notif_inAdapter = IntAdapter,
    notif_outAdapter = IntAdapter
)

// 🔧 Универсальный Int ↔ Long адаптер
object IntAdapter : ColumnAdapter<Int, Long> {
    override fun decode(databaseValue: Long): Int = databaseValue.toInt()
    override fun encode(value: Int): Long = value.toLong()
}

