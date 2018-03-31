package web;

import com.alibaba.fastjson.JSONObject;
import com.sun.istack.internal.Nullable;
import pojo.Client;
import pojo.Remote;
import server.SocketContainer;


import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint(value = "/ws/remote")
public class RemoteSocketHandler {
    private final String LOGIN_ACTION = "LOGIN";
    private final String GET_PROCESS_ACTION = "GETPROCES";
    private final String KILL_PROCESS_ACTION = "KILL";
    private static String password = "1";
    private Session session;
    private Remote remote;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        remote = new Remote();
        remote.setPassword(password);
        remote.setSession(session);
        SocketContainer.getInstance().addRemoteConnection(remote);
    }

    @OnClose
    public void onClose() {
        System.out.println(remote.getId() + " Disconnected");
    }

    @OnMessage
    public void onMessage(String message, Session send_session) throws IOException {
        JSONObject jsonObject = (JSONObject) JSONObject.parse(message);
        String action = jsonObject.get("ACTION").toString();
        JSONObject return_json = new JSONObject();
        return_json.put("ACTION", action);
        switch (action) {
            case LOGIN_ACTION:
                JSONObject login_info = new JSONObject();
                login_info.put("UID", remote.getId());
                login_info.put("PWD", remote.getPassword());
                return_json.put("TARGET", "");
                return_json.put("DATA", login_info);
                sendMessage(return_json.toJSONString());
                break;
            case GET_PROCESS_ACTION:
                int targetID = jsonObject.getInteger("TARGET");
                Client client = remote.getClientByID(targetID);
                if(client == null){
                    break;
                }
                jsonObject.put("TARGET",remote.getId());
                client.getClientSocketHandler().sendMessage(jsonObject.toString());
                break;
            case KILL_PROCESS_ACTION:
                targetID = jsonObject.getInteger("TARGET");
                client = remote.getClientByID(targetID);
                if(client == null){
                    break;
                }
                jsonObject.put("TARGET",remote.getId());
                client.getClientSocketHandler().sendMessage(jsonObject.toString());
                break;
            default:
                break;
        }


    }

    @OnError
    public void onError(Session error_session, Throwable throwable) {

    }

    public void sendMessage(String message) {
        session.getAsyncRemote().sendText(message);
    }
}
