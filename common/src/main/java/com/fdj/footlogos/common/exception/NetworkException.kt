package com.fdj.footlogos.common.exception

class NetworkException(
    ex: IGenericException
): GenericException(ex.code, ex.message){

    override val message: String
        get() = super.message
}