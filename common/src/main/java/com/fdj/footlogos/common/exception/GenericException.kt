package com.fdj.footlogos.common.exception

abstract class GenericException(val code: String, override val message: String) : Exception(message)