
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
            Set<String> parametros = request.queryParams();
            request.session(true);
            request.cookie("usuario",request.queryParams("usuario"))
            response.redirect("/");
            return "ok";
        });

        new Filtros().aplicarFiltros();


    }
    private static String renderContent(String htmlFile) {
        try {
            // If you are using maven then your files
            // will be in a folder called resources.
            // getResource() gets that folder
            // and any files you specify.
            URL url = Main.class.getResource(htmlFile);

            // Return a String which has all
            // the contents of the file.
            Path path = Paths.get(url.toURI());
            return new String(Files.readAllBytes(path), Charset.defaultCharset());
        } catch (IOException | URISyntaxException e) {
            // Add your own exception handlers here.
        }
        return null;
    }

    private static Object procesarParametros(Request request, Response response){
        System.out.println("Recibiendo mensaje por el metodo: "+request.requestMethod());
        Set<String> parametros = request.queryParams();
        String salida="";
        for(String param : parametros){
            salida+=String.format("Parametro[%s] = %s <br/>", param, request.queryParams(param));
        }
        return salida;
    }

}
