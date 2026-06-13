package com.product.model

import com.datastax.oss.driver.api.core.cql.AsyncResultSet
import com.datastax.oss.driver.api.mapper.MapperContext
import com.datastax.oss.driver.api.mapper.entity.EntityHelper
import com.datastax.oss.driver.api.querybuilder.QueryBuilder
import com.product.model.inner.InnerPmUserId
import com.product.model.repo.DbPmFilterRequest
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import java.util.function.BiConsumer

class AdCassandraSearchProvider(
    private val context: MapperContext,
    private val entityHelper: EntityHelper<PmCassandraDTO>
) {
    fun search(filter: DbPmFilterRequest): CompletionStage<Collection<PmCassandraDTO>> {
        var select = entityHelper.selectStart().allowFiltering()

        if (filter.nameFilter.isNotBlank()) {
            // Внимание! При использовании LIKE необходимо использовать SASI индексы.
            // При использовании SASI индекса типа StandardAnalyzer происходит токенизация текста по пробелам.
            // Оператор LIKE в этом случае должен быть НЕ LIKE '%<токен>%' а LIKE '<токен>%'
            select = select
                .whereColumn(PmCassandraDTO.COLUMN_NAME)
                .like(QueryBuilder.literal("${filter.nameFilter}%"))
        }
        if (filter.ownerId != InnerPmUserId.NONE) {
            select = select
                .whereColumn(PmCassandraDTO.COLUMN_OWNER_ID)
                .isEqualTo(QueryBuilder.literal(filter.ownerId.asString(), context.session.context.codecRegistry))
        }
        if (filter.descriptionFilter.isNotBlank()) {
            select = select
                .whereColumn(PmCassandraDTO.COLUMN_DESCRIPTION)
                .like(QueryBuilder.literal("${filter.descriptionFilter}%"))
        }

        val asyncFetcher = AsyncFetcher()

        context.session
            .executeAsync(select.build())
            .whenComplete(asyncFetcher)

        return asyncFetcher.stage
    }

    inner class AsyncFetcher : BiConsumer<AsyncResultSet?, Throwable?> {
        private val buffer = mutableListOf<PmCassandraDTO>()
        private val future = CompletableFuture<Collection<PmCassandraDTO>>()
        val stage: CompletionStage<Collection<PmCassandraDTO>> = future

        override fun accept(resultSet: AsyncResultSet?, t: Throwable?) {
            when {
                t != null -> future.completeExceptionally(t)
                resultSet == null -> future.completeExceptionally(IllegalStateException("ResultSet should not be null"))
                else -> {
                    buffer.addAll(resultSet.currentPage().map { entityHelper.get(it, false) })
                    if (resultSet.hasMorePages())
                        resultSet.fetchNextPage().whenComplete(this)
                    else
                        future.complete(buffer)
                }
            }
        }
    }
}