package DAO;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;

public class DBConnectionPool {

    private static HikariDataSource dataSource;

    public static HikariDataSource getDataSource() {
        if (dataSource == null) {
            initializeDataSource();
        }
        return dataSource;
    }

    private static void initializeDataSource() {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        String host = dotenv.get("DB_HOST");
        String port = dotenv.get("DB_PORT");
        String name = dotenv.get("DB_NAME");
        String user = dotenv.get("DB_USER");
        String password = dotenv.get("DB_PASSWORD");


        if (host == null || port == null || name == null || user == null || password == null) {
            throw new RuntimeException("Missing database configuration in .env file");
        }

        String url = String.format(
                "jdbc:mysql://%s:%s/%s?useSSL=false&serverTimezone=UTC",
                host, port, name
        );

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        dataSource = new HikariDataSource(config);
    }

    private DBConnectionPool() {
    }
}
