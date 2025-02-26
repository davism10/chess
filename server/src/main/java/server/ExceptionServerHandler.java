package server;

import exception.ResponseException;
import spark.*;

public class ExceptionServerHandler {
    public void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.StatusCode());
        res.body(ex.toJson());
    }
}
