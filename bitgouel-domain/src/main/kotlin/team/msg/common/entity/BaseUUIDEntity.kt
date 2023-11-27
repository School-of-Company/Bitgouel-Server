package team.msg.common.entity
import org.hibernate.annotations.GenericGenerator
import org.hibernate.proxy.HibernateProxy
import org.springframework.data.domain.Persistable
import team.msg.common.ulid.ULIDGenerator
import java.util.*
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class BaseUUIDEntity(

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)", nullable = false)
    @get:JvmName(name = "getIdentifier")
    open var id: UUID = UUID(0, 0)
) : BaseTimeEntity(), Persistable<UUID> {

    @Column(name = "ulid", updatable = false, unique = true)
    var ulid: String? = ULIDGenerator.generateULID()

    override fun isNew(): Boolean = (id == UUID(0,0)).also { new ->
        if(new) id = UUID.randomUUID()
    }

    override fun getId(): UUID? = id

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }

        if (other !is HibernateProxy && this::class != other::class) {
            return false
        }

        return id == if (other is HibernateProxy) {
            other.hibernateLazyInitializer.identifier
        } else {
            (other as BaseUUIDEntity).id
        }
    }

    override fun hashCode() = Objects.hashCode(id)

}