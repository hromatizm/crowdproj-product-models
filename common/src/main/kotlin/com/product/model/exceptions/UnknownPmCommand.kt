package ru.otus.otuskotlin.marketplace.common.exceptions

import com.product.model.inner.InnerPmCommand

class UnknownPmCommand(command: InnerPmCommand) : Throwable("Wrong command $command at mapping toTransport stage")
