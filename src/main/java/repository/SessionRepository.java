package repository;

import exception.CarRentalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mapper.Mapper;
import models.session.Session;
import util.DateTimeUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


@Slf4j
@RequiredArgsConstructor
public class SessionRepository {
    private final Connection connection;

    public void open(String email) {
        String OPEN = "INSERT INTO session(client_id, start) VALUES ((SELECT id FROM \"user\" WHERE email=? AND status), ?);";
        try (PreparedStatement statement = connection.prepareStatement(OPEN)) {
            statement.setString(1, email);
            statement.setTimestamp(2, DateTimeUtil.getCurrentSqlTimestamp());
            if(statement.executeUpdate() != 1){
                throw new CarRentalException("Can not open session for user with email %s", email);
            }
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }


    public boolean isExistOpenSessions() {
        String IS_EXIST_OPEN_SESSIONS = "SELECT EXISTS (SELECT 1 FROM session WHERE finish IS NULL AND status);";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(IS_EXIST_OPEN_SESSIONS);

            boolean isExist = Boolean.FALSE;
            if (resultSet.next()) {
                isExist = resultSet.getBoolean(1);
            }

            resultSet.close();
            return isExist;
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }


    public Session getActive() {
        String GET_ACTIVE = "SELECT * FROM session WHERE finish IS NULL AND status";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(GET_ACTIVE);
            if (resultSet.next()) {
                Session session = (Session) Mapper.mapSingleFromResultSet(resultSet, Session.class);
                resultSet.close();
                return session;
            }
            resultSet.close();
            throw new CarRentalException("There is no active session");
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }

    public void close() {
        String CLOSE = "UPDATE session SET finish=?, status = FALSE WHERE finish IS NULL AND status;";
        try (PreparedStatement statement = connection.prepareStatement(CLOSE)) {
            statement.setTimestamp(1, DateTimeUtil.getCurrentSqlTimestamp());
            if(statement.executeUpdate() != 1){
                throw new CarRentalException("Can not close active session");
            }
        } catch (SQLException exception) {
            log.error("Can not process statement", exception);
            throw new CarRentalException(exception.getMessage());
        }
    }
}