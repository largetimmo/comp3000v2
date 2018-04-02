package web;

import com.alibaba.fastjson.JSONObject;
import pojo.Client;
import pojo.Remote;
import server.SocketContainer;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/ws/remote")
public class RemoteSocketHandler {
    private static String password = "1";
    private final String LOGIN_ACTION = "LOGIN";
    private final String GET_PROCESS_ACTION = "GETPROCES";
    private final String KILL_PROCESS_ACTION = "KILL";
    private Session session;
    private Remote remote;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        remote = new Remote();
        remote.setPassword(password);
        remote.setRemoteSocketHandler(this);
        SocketContainer.getInstance().addRemoteConnection(remote);
        System.out.println("NEW REMOTE CONNECTION:" + remote.getId());
        session.setMaxTextMessageBufferSize(3276800);
        session.setMaxBinaryMessageBufferSize(3276800);
        session.setMaxIdleTimeout(0);

    }

    @OnClose
    public void onClose(CloseReason reason) {
        System.out.println("Remote:" + remote.getId() + " Disconnected");
        System.out.println(reason.getCloseCode());
        System.out.println(reason.getReasonPhrase());
    }

    @OnMessage
    public void onMessage(String message, Session send_session) {
        System.out.println(send_session + ":" + message);
        try {
            System.out.println(send_session.getId());
            JSONObject jsonObject = JSONObject.parseObject(message);
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
                    if (client == null) {
                        break;
                    }
                    jsonObject.put("TARGET", remote.getId());
                    client.getClientSocketHandler().sendMessage(jsonObject.toString());
                    break;
                case KILL_PROCESS_ACTION:
                    targetID = jsonObject.getInteger("TARGET");
                    client = remote.getClientByID(targetID);
                    if (client == null) {
                        break;
                    }
                    jsonObject.put("TARGET", remote.getId());
                    client.getClientSocketHandler().sendMessage(jsonObject.toString());
                    break;
                default:
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @OnError
    public void onError(Session error_session, Throwable throwable) {

    }

    public void sendMessage(String message) {
        session.getAsyncRemote().sendText(message);
    }
}
