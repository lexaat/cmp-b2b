package data.mapper

import data.model.dto.AccountDto
import b2b.database.AccountEntity
import domain.model.Account

fun AccountDto.toEntity(clientId: Long): AccountEntity =
    AccountEntity(
        aid = this.aid,
        client_id = clientId,
        account = this.account,
        name = this.name,
        branch = this.branch,
        state = this.state,
        sgn = this.sgn,
        valuta = this.valuta,
        dt = this.dt,
        ct = this.ct,
        o_date = this.o_date,
        l_date = this.l_date,
        s_in = this.s_in,
        s_out = this.s_out,
        canpay = this.canpay,
        notif_in = this.notif_in,
        notif_out = this.notif_out
)

fun AccountDto.toDomain(): Account = Account(
    aid = aid,
    account = account,
    name = name,
    branch = branch,
    state = state,
    sgn = sgn,
    valuta = valuta,
    dt = dt,
    ct = ct,
    oDate = o_date,
    lDate = l_date,
    sIn = s_in,
    sOut = s_out,
    canPay = canpay,
    notifIn = notif_in,
    notifOut = notif_out
)

fun AccountEntity.toDomain(): Account = Account(
    aid = aid,
    account = account,
    name = name,
    branch = branch,
    state = state,
    sgn = sgn,
    valuta = valuta,
    dt = dt,
    ct = ct,
    oDate = o_date,
    lDate = l_date,
    sIn = s_in,
    sOut = s_out,
    canPay = canpay,
    notifIn = notif_in,
    notifOut = notif_out
)

fun Account.toEntity(clientId: Long): AccountEntity = AccountEntity(
    aid = aid,
    client_id = clientId,
    account = account,
    name = name,
    branch = branch,
    state = state,
    sgn = sgn,
    valuta = valuta,
    dt = dt,
    ct = ct,
    o_date = oDate,
    l_date = lDate,
    s_in = sIn,
    s_out = sOut,
    canpay = canPay,
    notif_in = notifIn,
    notif_out = notifOut
)