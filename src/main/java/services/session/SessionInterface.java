package services.session;

import models.session.Session;

public interface SessionInterface {
    Session open(String email);
    Session getActive();
    void close();
    boolean isUserAuthenticated();
}
