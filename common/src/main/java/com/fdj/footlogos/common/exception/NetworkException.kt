package com.fdj.footlogos.common.exception

import com.fdj.footlogos.common.exception.GenericException
import com.fdj.footlogos.common.exception.IGenericException

class NetworkException(
    ex: IGenericException
): GenericException(ex.code, ex.message){

    override val message: String
        get() = super.message
}