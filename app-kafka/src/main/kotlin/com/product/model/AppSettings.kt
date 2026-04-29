package com.product.model

import com.product.model.processor.PmProcessor

data class AppSettings(
    val appUrls: List<String> = emptyList(),
    override val corSettings: CorSettings = CorSettings(),
    override val processor: IProcessor = PmProcessor(corSettings),
): IAppSettings