package team.msg.common.entity

import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.MappedSuperclass
import org.hibernate.annotations.GenericGenerator
import java.util.*

@MappedSuperclass
abstract class BaseUUIDEntity(
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)", nullable = false)
    open val id: UUID
) : BaseTimeEntity()