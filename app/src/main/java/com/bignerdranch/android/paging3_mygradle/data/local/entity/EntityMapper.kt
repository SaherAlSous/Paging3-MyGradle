package com.bignerdranch.android.paging3_mygradle.data.local.entity

interface EntityMapper<Model, Entity> {
    abstract fun mapToEntity(model: List<Model>): List<Entity>
}