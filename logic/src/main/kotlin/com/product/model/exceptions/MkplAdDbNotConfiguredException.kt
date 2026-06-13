package com.product.model.exceptions

import com.product.model.inner.InnerPmWorkMode

class PmDbNotConfiguredException(val workMode: InnerPmWorkMode) : Exception(
    "Database is not configured properly for workmode $workMode"
)
