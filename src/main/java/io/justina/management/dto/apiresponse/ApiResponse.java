package io.justina.management.dto.apiresponse;

import java.io.Serializable;

public record ApiResponse<T>(

        boolean estado,

        String message,

        T data,

        Iterable<T> dataIterable
) implements Serializable {

    public ApiResponse(boolean estado, String message, T data) {
        this(estado, message, data, null);
    }

    public ApiResponse(boolean estado, String message, Iterable<T> dataIterable) {
        this(estado, message, null, dataIterable);
    }
}
