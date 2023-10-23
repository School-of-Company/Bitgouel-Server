package team.msg.common.entity

import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.MappedSuperclass
import javax.persistence.PostLoad
import javax.persistence.PostPersist
import org.hibernate.annotations.GenericGenerator
import org.hibernate.proxy.HibernateProxy
import org.springframework.data.domain.Persistable
import team.msg.common.ulid.ULIDGenerator
import java.io.Serializable
import java.util.*

@MappedSuperclass
abstract class BaseUUIDEntity(
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)", nullable = false)
    open val id: UUID
) : BaseTimeEntity(), Persistable<UUID> {

    @Column(name = "ulid", updatable = false, unique = true)
    private var ulid: String? = ULIDGenerator.generateULID()

    override fun getId(): UUID = id

    @Transient
    private var _isNew = true

    override fun isNew(): Boolean = _isNew

    @PostPersist
    @PostLoad
    protected fun load() {
        _isNew = false
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }

        if (other !is HibernateProxy && this::class != other::class) {
            return false
        }

        return id == getIdentifier(other)
    }

    private fun getIdentifier(obj: Any): Serializable {
        return if (obj is HibernateProxy) {
            obj.hibernateLazyInitializer.identifier
        } else {
            (obj as BaseUUIDEntity).id
        }
    }

    override fun hashCode() = Objects.hashCode(id)

}