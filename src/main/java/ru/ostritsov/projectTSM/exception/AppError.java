package ru.ostritsov.projectTSM.exception;

public class AppError extends RuntimeException {
    public AppError(String message) {
        super(message);
    }
}
