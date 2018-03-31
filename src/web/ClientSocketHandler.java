package web;

import com.alibaba.fastjson.JSONObject;
import pojo.Client;
import pojo.Remote;
import server.SocketContainer;
import util.JSONHelper;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint("/ws/client")
public class ClientSocketHandler {
    private Session session;
    private final String LOGIN_ACTION = "LOGIN";
    private final String GET_PROCESS_ACTION = "GETPROCES";
    private final String KILL_PROCESS_ACTION = "KILL";
    private final String Action_Tag = "ACTION";
    private final String Target_Tag = "TARGET";
    private final String Data_Tag = "DATA";

    private Client client;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        client = new Client(this);

    }

    @OnClose
    public void onClose() {

    }

    @OnMessage
    public void onMessage(String message) throws IOException {
        JSONObject jsonObject = JSONObject.parseObject(message);
        String action = jsonObject.getString("ACTION");
        switch (action) {
            case LOGIN_ACTION:
                JSONObject remoteinfo = jsonObject.getJSONObject("DATA");
                int rid = remoteinfo.getInteger("UID");
                String pwd = remoteinfo.getString("PWD");
                Remote remote = SocketContainer.getInstance().getRemoteByIdPwd(rid,pwd);
                if(remote !=null){
                    //correct login
                    remote.addClient(client);
                    SocketContainer.getInstance().addRemoteConnection(remote);
                    JSONObject return_message = JSONHelper.constructJson(action,rid,"SUCCESS");
                    client.addRemote(remote);
                    sendMessage(return_message.toJSONString());
                }else{
                    //invalid login
                    JSONObject return_message = JSONHelper.constructJson(action,"","FAIL");
                    sendMessage(return_message.toJSONString());
                }
                break;
            case GET_PROCESS_ACTION:
                int targetID = jsonObject.getInteger(Target_Tag);
                remote = client.getRemoteById(targetID);
                if (remote == null) {
                    break;
                }
                JSONObject send_Message = JSONHelper.constructJson(action,client.getClient_id(),"");
                remote.getRemoteSocketHandler().sendMessage(send_Message.toJSONString());
                break;
            case KILL_PROCESS_ACTION:
                targetID = jsonObject.getInteger(Target_Tag);
                remote = client.getRemoteById(targetID);
                if(remote == null){
                    break;
                }
                send_Message = JSONHelper.constructJson(action,client.getClient_id(), jsonObject.get(Data_Tag));
                remote.getRemoteSocketHandler().sendMessage(send_Message.toJSONString());
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