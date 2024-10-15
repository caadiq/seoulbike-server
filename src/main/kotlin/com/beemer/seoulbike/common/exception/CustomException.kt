package com.beemer.seoulbike.common.exception

class CustomException(val errorCode: ErrorCode) : RuntimeException()