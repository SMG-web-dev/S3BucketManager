package com.BucketManager.S3Manager;

public class S3ServiceException extends RuntimeException {
    private String errorMessage;

    public S3ServiceException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

