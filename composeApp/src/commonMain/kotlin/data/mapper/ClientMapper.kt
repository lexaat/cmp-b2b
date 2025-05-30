package data.mapper

import b2b.database.ClientEntity
import data.model.dto.ClientDto
import domain.model.Account
import domain.model.Client

fun ClientDto.toEntity(): ClientEntity =
    ClientEntity(
        id = this.id,
        name = this.name,
        code = this.code,
        inn = this.inn,
        address = this.address,
        phone = this.phone,
        branch = this.branch,
        doc_num = this.doc_num,
        buh_name = this.buh_name,
        dir_name = this.dir_name,
        oper_day = this.oper_day,
        payment_scheme = this.payment_scheme,
        cards_count = this.cards_count,
        cert_sn = this.cert_sn,
        pinfl = this.pinfl,
        k2_count = this.k2_count
    )

fun ClientDto.toDomain(): Client = Client(
    id = id,
    name = name,
    code = code,
    inn = inn,
    address = address,
    phone = phone,
    branch = branch,
    docNum = doc_num,
    buhName = buh_name,
    dirName = dir_name,
    operDay = oper_day,
    paymentScheme = payment_scheme,
    cardsCount = cards_count,
    certSn = cert_sn,
    pinfl = pinfl,
    k2Count = k2_count,
    accounts = accounts.map { it.toDomain() }
)

fun ClientEntity.toDomain(accounts: List<Account>): Client = Client(
    id = id,
    name = name,
    code = code,
    inn = inn,
    address = address,
    phone = phone,
    branch = branch,
    docNum = doc_num,
    buhName = buh_name,
    dirName = dir_name,
    operDay = oper_day,
    paymentScheme = payment_scheme,
    cardsCount = cards_count,
    certSn = cert_sn,
    pinfl = pinfl,
    k2Count = k2_count,
    accounts = accounts
)

fun Client.toEntity(): ClientEntity = ClientEntity(
    id = id,
    name = name,
    code = code,
    inn = inn,
    address = address,
    phone = phone,
    branch = branch,
    doc_num = docNum,
    buh_name = buhName,
    dir_name = dirName,
    oper_day = operDay,
    payment_scheme = paymentScheme,
    cards_count = cardsCount,
    cert_sn = certSn,
    pinfl = pinfl,
    k2_count = k2Count
)