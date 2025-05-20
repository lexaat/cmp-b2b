package features.home.data.mapper

import features.home.data.dto.AccountDto
import features.home.data.dto.ClientDto
import features.home.domain.model.Account
import features.home.domain.model.Client

fun ClientDto.toDomain(): Client {
    return Client(
        name = name,
        inn = inn,
        address = address,
        accounts = accounts.map { it.toDomain() }
    )
}


fun AccountDto.toDomain(): Account {
    return Account(
        account = account,
        balanceIn = s_in,
        balanceOut = s_out,
    )
}

