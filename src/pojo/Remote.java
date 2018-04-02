package pojo;

import web.RemoteSocketHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Remote {
    private static int RID = 0;
    private final List<Client> clientList = Collections.synchronizedList(new ArrayList<Client>());
    private RemoteSocketHandler remoteSocketHandler;
    private int id;
    private String password;

    public Remote() {
        id = RID++;
    }

    public RemoteSocketHandler getRemoteSocketHandler() {
        return remoteSocketHandler;
    }

    public void setRemoteSocketHandler(RemoteSocketHandler remoteSocketHandler) {
        this.remoteSocketHandler = remoteSocketHandler;
    }

    public List<Client> getClientList() {
        return clientList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean verifyClientID(int cid) {
        synchronized (clientList) {
            for (Client c : clientList) {
                if (c.getClient_id() == cid) {
                    return true;
                }

            }
        }
        return false;
    }

    public void addClient(Client client) {
        synchronized (clientList) {
            clientList.add(client);
        }
    }

    public void removeClientByID(int cid) {
        synchronized (clientList) {
            for (int i = 0; i < clientList.size(); i++) {
                if (clientList.get(i).getClient_id() == cid) {
                    clientList.remove(i);
                    return;
                }
            }
        }
    }

    public Client getClientByID(int cid) {
        synchronized (clientList) {
            for (Client c : clientList) {
                if (c.getClient_id() == cid) {
                    return c;
                }
            }
        }
        return null;
    }
}
