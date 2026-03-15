package com.product.model

data class AppSettings(
    val appUrls: List<String> = emptyList(),
    override val corSettings: CorSettings = CorSettings(),
    override val processor: PmProcessor = PmProcessor(corSettings),
): IAppSettings