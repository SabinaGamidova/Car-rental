package connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public interface Transactionable {

    default void wrapIntoTransaction(VoidWorker worker) {
        Logger logger = LoggerFactory.getLogger(Transactionable.class);
        Connection connection = ConnectionManager.getConnection();
        try {
            disableAutoCommit(connection);
            worker.work();
        } catch (Exception exception) {
            logger.error("Transaction exception's occurred, it will be rolled back", exception);
            rollbackTransaction(connection);
            throw new RuntimeException(exception);
        } finally {
            enableAutoCommit(connection);
        }
    }

    default <T> T wrapIntoTransaction(Worker<T> worker) {
        Logger logger = LoggerFactory.getLogger(Transactionable.class);
        Connection connection = ConnectionManager.getConnection();
        try {
            disableAutoCommit(connection);
            return worker.work();
        } catch (Exception exception) {
            logger.error("Transaction exception's occurred, it will be rolled back", exception);
            rollbackTransaction(connection);
            throw new RuntimeException(exception);
        } finally {
            enableAutoCommit(connection);
        }
    }

    private void disableAutoCommit(Connection connection) {
        try {
            connection.setAutoCommit(Boolean.FALSE);
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void enableAutoCommit(Connection connection) {
        try {
            connection.setAutoCommit(Boolean.TRUE);
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void rollbackTransaction(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}