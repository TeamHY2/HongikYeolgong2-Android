package com.benenfeldt.remote.mapper

import com.benenfeldt.remote.dto.BaseResponse

private const val UNEXPECTED_ERROR_IN_MAPPER = "Unexpected Error in Mapper"

fun <T, R> Result<BaseResponse<T>>.toResult(mapper: (BaseResponse<T>) -> R): Result<R> {
    this.onSuccess {
        if (it.isSuccess()) {
            return Result.success(mapper(it))
        }
        return Result.failure(IllegalStateException(it.message))
    }.onFailure {
        return Result.failure(it)
    }

    return Result.failure(Exception(UNEXPECTED_ERROR_IN_MAPPER))
}

fun <T> Result<BaseResponse<T>>.toResult(): Result<Unit> {
    this.onSuccess {
        if (it.isSuccess()) {
            return Result.success(Unit)
        }
        return Result.failure(IllegalStateException(it.message))
    }.onFailure {
        return Result.failure(it)
    }

    return Result.failure(Exception(UNEXPECTED_ERROR_IN_MAPPER))
}
