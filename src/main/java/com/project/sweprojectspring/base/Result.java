package com.project.sweprojectspring.base;

public class Result<T> {
    private final Exception error;
    private final T value;

    public Result(T value){
        this.value=value;
        this.error=null;
    }

    public Result(Exception exception){
        this.error=exception;
        this.value=null;
    }
    public Result(String message){
        this.error=new Exception(message);
        this.value=null;
    }

    public boolean isFailed(){
        return value==null;
    }

    public boolean isSuccessful(){
        return error==null;
    }
    public T ToValue(){
        return value;
    }

    public Exception ToError(){
        return error;
    }

    public static <Tf> Result<Tf> fail(Exception error){
        return new Result<Tf>(error);
    }

    public static <Tf> Result<Tf> fail(String message) {
        return new Result<Tf>(message);
    }
    public static <Ts> Result<Ts> success(Ts value){
        return new Result<Ts>(value);
    }
}
