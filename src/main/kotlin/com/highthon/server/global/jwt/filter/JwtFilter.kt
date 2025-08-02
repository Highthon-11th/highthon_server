package com.highthon.server.global.jwt.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import com.highthon.server.global.exception.BasicException
import com.highthon.server.global.jwt.provider.AccessTokenProvider
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter


@Component
class JwtFilter(
    private val accessTokenProvider: AccessTokenProvider,
) : OncePerRequestFilter() {

    companion object {
        const val HEADER_KEY = "Authorization"
        const val PREFIX = "Bearer "
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val token = resolveToken(request)
        try {
            if (token != null && accessTokenProvider.validateTokenOrThrow(token)) {
                SecurityContextHolder.getContext().authentication = accessTokenProvider.getAuthentication(token)
            }
            filterChain.doFilter(request, response)
        } catch (e: BasicException) {
            response.sendError(e.status.value(), e.message)
        }
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val token = request.getHeader(HEADER_KEY)
        if (token != null && token.startsWith(PREFIX)) {
            return token.replace(PREFIX, "")
        }
        return null
    }
}
