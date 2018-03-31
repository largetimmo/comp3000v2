package pojo;

import web.ClientSocketHandler;
import web.RemoteSocketHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Client {
    private static int CID = 0;
    private int client_id;
    private ClientSocketHandler clientSocketHandler;
    private List<Remote> remoteList = Collections.synchronizedList(new ArrayList<Remote>());
    public Client(ClientSocketHandler clientSocketHandler){
        client_id = CID++;
        this.clientSocketHandler = clientSocketHandler;
    }

    public int getClient_id() {
        return client_id;
    }

    public ClientSocketHandler getClientSocketHandler() {
        return clientSocketHandler;
    }

    public void addRemote(Remote remote){
        synchronized (remoteList) {
            remoteList.add(remote);
        }
    }
    public Remote getRemoteById(int rid){
        synchronized (remoteList){
            for (Remote remote : remoteList){
                if(remote.getId() == rid){
                    return remote;
                }
            }
        }
        return null;
    }
}
