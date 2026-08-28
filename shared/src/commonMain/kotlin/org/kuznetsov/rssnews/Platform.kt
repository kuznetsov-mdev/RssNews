package org.kuznetsov.rssnews

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform