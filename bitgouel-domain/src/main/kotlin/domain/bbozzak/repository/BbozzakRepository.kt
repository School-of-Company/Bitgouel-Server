package domain.bbozzak.repository

import domain.bbozzak.model.Bbozzak
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface BbozzakRepository : CrudRepository<Bbozzak, UUID>