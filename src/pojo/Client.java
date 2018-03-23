package pojo;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Client {
    private String token;
    private Session session;
    private List<String> remote_ids = Collections.synchronizedList(new ArrayList<String>());

}
