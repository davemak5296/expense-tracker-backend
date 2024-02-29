package com.codewithflow.exptracker.response;

public class GenericResponse <T, S> {
    private boolean success;
    private T data;
    private S errors;

    public GenericResponse(boolean success, T data, S errors) {
        this.success = success;
        this.data = data;
        this.errors = errors;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public S getErrors() {
        return errors;
    }

    public void setErrors(S errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "GenericResponse{" +
                "success=" + success +
                ", data=" + data +
                ", errors=" + errors +
                '}';
    }
}
