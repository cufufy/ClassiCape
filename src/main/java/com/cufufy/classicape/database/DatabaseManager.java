package com.cufufy.classicape.database;

import com.cufufy.classicape.ClassiCape;
import lombok.Getter;

public class DatabaseManager {

    @Getter
    private Database database;

    public DatabaseManager(ClassiCape instance, DatabaseType type) {
        switch (type) {
            case FILE:
                database = new FileDatabase(instance);
                break;
            case MYSQL:
                database = new MysqlDatabase(instance);
                break;

        }
    }

    public void initialize() {
        database.initialize();
    }

    public void close() {
        database.close();
    }

}
