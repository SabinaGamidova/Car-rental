package services.session;

import exception.CarRentalException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import models.session.Session;
import org.apache.commons.lang3.StringUtils;
import repository.SessionRepository;

@Slf4j
@AllArgsConstructor
public class SessionService implements SessionInterface {
    private final SessionRepository sessionRepository;

    @Override
    public Session open(String email) {
        if (StringUtils.isBlank(email)) {
            throw new CarRentalException("Email must be not blank");
        }
        if (sessionRepository.isExistOpenSessions()) {
            log.error("There is already open session");
            this.close();
        }
        sessionRepository.open(email);
        return this.getActive();
    }

    @Override
    public Session getActive() {
        return sessionRepository.getActive();
    }

    @Override
    public void close() {
        if (sessionRepository.isExistOpenSessions()) {
            sessionRepository.close();
        }
    }

    @Override
    public boolean isUserAuthenticated() {
        return sessionRepository.isExistOpenSessions();
    }
}