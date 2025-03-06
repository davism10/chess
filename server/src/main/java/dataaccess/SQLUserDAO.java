package dataaccess;

import model.UserData;

public class SQLUserDAO implements UserDAO{
    public SQLUserDAO() throws DataAccessException {
//        configureDatabase();
    }

    public void clear(String username){

    }

    public void clearAll(){

    }

    public UserData getUser(String username){

    }

    public void createUser(UserData userData){

    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  userData (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`),
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

//    private void configureDatabase() throws DataAccessException {
//        try (var conn = DatabaseManager.getConnection()) {
//            try (var preparedStatement = conn.prepareStatement("SELECT 1+1")) {
//                var rs = preparedStatement.executeQuery();
//                rs.next();
//                System.out.println(rs.getInt(1));
//            }
//        }
//    }

}
