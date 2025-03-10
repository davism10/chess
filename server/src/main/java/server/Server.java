package server;

import dataaccess.*;
import exception.ResponseException;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        try {
            GameDAO gameMemory = new MemoryGameDAO();
            UserDAO userMemory = new SQLUserDAO();
            AuthDAO authMemory = new MemoryAuthDAO();
        }
        catch (DataAccessException e){

        }

        ClearService clearService = new ClearService(gameMemory, authMemory, userMemory);
        UserService userService = new UserService(userMemory, authMemory);
        GameService gameService = new GameService(gameMemory, authMemory);

        ClearServerHandler clearServerHandler = new ClearServerHandler(clearService);
        UserServerHandler userServerHandler = new UserServerHandler(userService);
        GameServerHandler gameServerHandler = new GameServerHandler(gameService);
        ExceptionServerHandler exceptionServerHandler = new ExceptionServerHandler();

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", clearServerHandler::clearHandler);
        Spark.post("/user", userServerHandler::registerHandler);
        Spark.post("/session", userServerHandler::loginHandler);
        Spark.delete("/session", userServerHandler::logoutHandler);
        Spark.get("/game", gameServerHandler::listGamesHandler);
        Spark.post("/game", gameServerHandler::createGameHandler);
        Spark.put("/game", gameServerHandler::joinGameHandler);
        Spark.exception(ResponseException.class, exceptionServerHandler::exceptionHandler);
        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
