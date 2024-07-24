package io.justina.management;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Clase que gestiona el inicio de la aplicación y muestra la documentación de Swagger una vez que la aplicación está lista.
 * Imprime la URL de la documentación Swagger basada en las propiedades configuradas.
 */
@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    @Value("${server.port:8080}")
    private String serverPort;

    @Value("${springdoc.swagger-ui.path}")
    private String apiDocsPath;

    /**
     * Método invocado cuando la aplicación está lista para manejar peticiones.
     * Imprime la URL de la documentación Swagger en la consola.
     *
     * @param event Evento de inicio de la aplicación que contiene información sobre el contexto de la aplicación.
     */
    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        System.out.println("DOCUMENTACION SWAGGER: http://localhost:" + serverPort + apiDocsPath);
    }
}

