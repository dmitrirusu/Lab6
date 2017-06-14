import io.jsonwebtoken.*;
import spark.Filter;
import spark.Request;
import spark.Response;

import static spark.Spark.*;

public class Main {

    private static String jwtSecret = "secret";


    public static void main(String[] args) {
        port(12448);
        JWTAuthFilter filter = new JWTAuthFilter();
        webSocket("/request", WebsocketHandler.class);
        before("/request", filter);

        get("/hello", (req, res) -> "Hello World");
        get("/token", (request, response) -> Jwts.builder()
                .setSubject("auth_token")
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact());
    }

    public static class JWTAuthFilter implements Filter {

        @Override
        public void handle(Request request, Response response) throws Exception {

            String jwt = request.headers("Authorization");
            if (jwt == null || jwt.isEmpty()) {
                halt(401, "Unauthorized");
            }

            try {
                Jws<Claims> claims = Jwts.parser()
                        .requireSubject("auth_token")
                        .setSigningKey(jwtSecret.getBytes("UTF-8"))
                        .parseClaimsJws(jwt);
                if (claims.getBody().getSubject() == null) {
                    halt(401, "Unauthorized");
                }
            } catch (Exception e) {
                halt(401, "Unauthorized");
            }
        }
    }

}
