package gerbarika.web.application.dao.repository

import gerbarika.web.application.dao.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface UserRepository : JpaRepository<User, Long?> {

    fun findByName(name: String) : User

}


