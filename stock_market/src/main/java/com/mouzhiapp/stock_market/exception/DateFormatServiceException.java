package com.mouzhiapp.stock_market.exception;

public class DateFormatServiceException extends RuntimeException {

    private static final long serialVersionUID = -2996489172797289506L;

    public String getErrorDate() {
        return errorDate;
    }

    public DateFormatServiceException setErrorDate(String errorDate) {
        this.errorDate = errorDate;
        return this;
    }

    private String errorDate;

    public DateFormatServiceException() {
        super();
    }

    public DateFormatServiceException(String message) {
        super(message);
    }

    public DateFormatServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public DateFormatServiceException(String errDate, String message, Throwable cause) {
        super(message, cause);
        this.errorDate = errDate;
    }

    public DateFormatServiceException(Throwable cause) {
        super(cause);
    }

    protected DateFormatServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return super.fillInStackTrace();
    }
}
