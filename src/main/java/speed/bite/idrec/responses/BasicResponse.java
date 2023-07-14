package speed.bite.idrec.responses;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class BasicResponse {
    public static <T> ResponseEntity<T> ok(T body) {
        return ResponseEntity.ok(body);
    }

    public static ResponseEntity<Map<String,String>> err(HttpStatusCode httpStatusCode, String msg) {
        return new ResponseEntity<Map<String,String>>(new HashMap<>(){{
            put("msg", msg);
        }}, httpStatusCode);
    }
}
