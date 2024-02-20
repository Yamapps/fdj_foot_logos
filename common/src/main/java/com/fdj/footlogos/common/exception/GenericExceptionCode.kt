package com.fdj.footlogos.common.exception


enum class GenericExceptionCode(override val code: String, override val message: String):
    IGenericException {

    /*
     * GENERIC
     */
    DATA_NETWORK_EXCEPTION("00-001", "An error occurred while retrieving the data items"),
    CONNECTION_TIMEOUT_EXCEPTION("00-002", "A network problem occurred. Please try again later."),
    NO_NETWORK_EXCEPTION("00-003", "No network available. Please try again later.");

}