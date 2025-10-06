package org.example.kafkadlt.kafkadlt.model

import java.util.UUID

/**
 * Pizza domain model.
 */

data class Pizza(
    val id: UUID,
    val name: String,
    val size: Size
)

/**
 * Pizza sizes: Pequena (P), Média (M), Grande (G)
 */

enum class Size { P, M, G }
