package domain.admin.repository

import domain.admin.model.Admin
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface AdminRepository : CrudRepository<Admin, UUID>