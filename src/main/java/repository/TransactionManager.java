package repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;

/*@Slf4j
@RequiredArgsConstructor*/
public interface TransactionManager {
    //TODO 1)Наследование
    //TODO 2)Класс со статическим методом
    //TODO 3)Интерфейс с default методом


    //public static Connection connection;

    /*private static void disableAutoCommit() {
        try {
            connection.setAutoCommit(Boolean.FALSE);
        } catch (SQLException exception) {
            log.error("Can not disable autocommit", exception);
            throw new RuntimeException(exception);
        }
    }

    default void wrapIntoTransaction(Worker worker) {
        try {
            disableAutoCommit();
            worker.work();
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            rollbackTransaction();
            throw new RuntimeException(exception);
        } finally {
            enableAutoCommit();
        }
    }

    private static void enableAutoCommit() {
        try {
            connection.setAutoCommit(Boolean.TRUE);
        } catch (SQLException exception) {
            log.error("Can not enable autocommit", exception);
            throw new RuntimeException(exception);
        }
    }

    private static void rollbackTransaction() {
        try {
            connection.rollback();
        } catch (SQLException exception) {
            log.error("Can not rollback transaction", exception);
            throw new RuntimeException(exception);
        }
    }*/

    interface Worker {
        void work();
    }
}