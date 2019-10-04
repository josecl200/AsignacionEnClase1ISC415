
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.template.freemarker.FreeMarkerEngine;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {

        port(8080);

        staticFiles.location("/publico");

        get("/", (request, response) -> {
            System.out.println("Entrando al action de la /");
            return  "Bienvenido";
        });

        get("/formulario", (request, response) -> renderContent("publico/login.html"));

        post("/registro", (request, response) -> {

            response.cookie("usuario",request.queryParams("usuario"));
            System.out.println(request.queryParams("usuario"));
            request.session(true).attribute("usuario",request.queryParams("usuario"));
            request.session().attribute("contrasena",request.queryParams("contrasena"));
            response.redirect("/");
            return "ok";
        });

        new Filtros().aplicarFiltros();


    }
    private static String renderContent(String htmlFile) throws IOException, URISyntaxException {
            URL url = Main.class.getResource(htmlFile);
            Path path = Paths.get(url.toURI());
            return new String(Files.readAllBytes(path), Charset.defaultCharset());
    }
}
