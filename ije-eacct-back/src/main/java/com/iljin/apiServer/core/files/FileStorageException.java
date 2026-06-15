package com.iljin.apiServer.core.files;

public class FileStorageException extends RuntimeException{


    private static final long serialVersionUID = -1698363241184744410L;

    public FileStorageException(String msg) {
        super(msg);
    }

    public FileStorageException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
