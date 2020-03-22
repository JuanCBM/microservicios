 # Valor del jdk del proyecto.
FROM openjdk:13 

# Es opcional, algunas apps de spring lo requieren porque tomcat guarda los logs por defecto aquí, como nuestra app muestra los logs por consola no haría falta.
VOLUME /tmp 
 
# Agregar un archivo a la imagen de origen y destino
ADD ./target/springboot-servicio-usuarios-0.0.1-SNAPSHOT.jar servicio-usuarios.jar

# Ejecutar o levantar la aplicación al arrancar el contenedor con nuestra app spring boot como punto de entrada

ENTRYPOINT ["java", "-jar", "/servicio-usuarios.jar"]