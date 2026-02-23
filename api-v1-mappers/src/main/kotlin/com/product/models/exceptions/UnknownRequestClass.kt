package com.product.models.exceptions

class UnknownRequestClass(clazz: Class<*>) : RuntimeException("Class $clazz cannot be mapped to MkplContext")