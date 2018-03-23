package pojo;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Remote {
    private Session session;
    private String id;
    private String password;
    private List<String> client_ids = Collections.synchronizedList(new ArrayList<String>());
    public Remote(){}

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getClient_ids() {
        return client_ids;
    }

    public void setClient_ids(List<String> client_ids) {
        this.client_ids = client_ids;
    }
}
