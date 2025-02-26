package server;

import com.google.gson.Gson;
import exception.ResponseException;
import service.ClearService;
import spark.Request;
import spark.Response;

import java.util.Map;

public class ClearServerHandler {
    final private ClearService clearService;

    public ClearServerHandler(ClearService clearService) {
        this.clearService = clearService;
    }

    public Object clearHandler(Request req, Response res) throws ResponseException {
        try {
            clearService.clear();
            return new Gson().toJson(null);
        }
        catch (ResponseException e){
            int error = e.StatusCode();
            res.status(error);
            throw e;
        }
    }
}
