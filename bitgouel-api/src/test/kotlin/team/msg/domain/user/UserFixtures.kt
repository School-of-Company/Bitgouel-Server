package team.msg.domain.user

import team.msg.common.enums.ApproveStatus
import team.msg.domain.user.enums.Authority
import team.msg.domain.user.model.User
import java.util.UUID

fun createUser(
    userId: UUID = UUID.randomUUID(),
    email: String = "s22000@gsm.hs.kr",
    name: String = "박주홍",
    phoneNumber: String = "01012345678",
    password: String = "123456789a@",
    authority: Authority = Authority.ROLE_STUDENT,
    approveStatus: ApproveStatus = ApproveStatus.PENDING
): User =
    User(
        id = userId,
        email = email,
        name = name,
        phoneNumber = phoneNumber,
        password = password,
        authority = authority,
        approveStatus = approveStatus
    )