package gerbarika.web.application.service.impl

import gerbarika.web.application.dao.repository.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service


@Service
class UserServiceImpl(private val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByName(username)
        return object : UserDetails {
            override fun getAuthorities(): Collection<GrantedAuthority?> {
                return setOf(SimpleGrantedAuthority("ROLE_UNIQUE"))
            }

            override fun getPassword(): String {
                return user.password
            }

            override fun getUsername(): String {
                return user.name
            }

            override fun isAccountNonExpired(): Boolean {
                return true
            }

            override fun isAccountNonLocked(): Boolean {
                return true
            }

            override fun isCredentialsNonExpired(): Boolean {
                return true
            }

            override fun isEnabled(): Boolean {
                return true
            }
        }
    }
}
