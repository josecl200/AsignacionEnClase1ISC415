

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

import static spark.Spark.*;

/**
 * Ejemplos de uso de filtros en Spark.
 */
public class Filtros {

    /**
     *
     */
    public void aplicarFiltros(){

        /**
         * Se ejecuta antes de un request. Podemos modificar las llamada.
         */
        before((request, response) -> {
            System.out.println("Filtro Before -> Realizando llamada a la ruta: "+request.pathInfo());
        });

        /**
         * Luego de la ejecución permute interceptar el response...
         */
        after((request, response) -> {
            System.out.println("Filtro After -> Incluyendo Header...");
            response.header("barcamp", ""+new SimpleDateFormat("yyyy").format(new Date()));
            response.header("otroHeader", "Cualquier Cosa");

            if(request.queryParams("pararFiltro")!=null){
                System.out.println("Parando la ejecución del filtro...");
                halt(501, "Parada del proceso"); //
            }

            //si paro el filtro, no se ejecuta, la creación del header.
            response.header("profesor", "Carlos Camacho");
        });

        /**
         * Es visualizado como un bloque siempre se va a ejecutar
         * puede ser visto como finally de un try catch.
         */
        afterAfter((request, response) -> {
            HttpServletResponse raw = response.raw();
            if(raw.getHeader("profesor") == null){
                response.header("profesor", "Carlos Camacho");
                response.header("ejecutado", "bloque afterAfter");
            }

        });

        /**
         * Se ejecuta antes de un request. Podemos modificar las llamada.
         *
         */
        before("/",(request,response) ->{
            Usuario usuario=request.session(true).attribute("usuario");
            if(usuario==null){
                response.redirect("/formulario");
            }
        });



    }
}
