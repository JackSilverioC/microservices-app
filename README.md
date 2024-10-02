# Arquitectura de Microservicios con Spring Boot

Este proyecto implementa una arquitectura de microservicios utilizando **Spring Boot** y otras tecnologías para garantizar una solución escalable, segura y distribuida.

En este proyecto se configuraron y utilizaron las siguientes tecnologías:

- **Servidor de Configuración**: para centralizar y gestionar las configuraciones de los microservicios.
- **Servidor de Descubrimiento (Eureka)**: para registrar y descubrir los microservicios de manera dinámica.
- **API Gateway**: para gestionar las solicitudes y redirigirlas a los microservicios correspondientes.
- **Comunicación Asíncrona con Kafka**: para enviar mensajes entre microservicios de forma eficiente y desacoplada.
- **Comunicación Síncrona con OpenFeign y RestTemplates**: para llamadas directas entre microservicios cuando se requiere una respuesta inmediata.
- **Trazabilidad Distribuida con Zipkin y Spring Actuator**: para monitorear y rastrear solicitudes a través de múltiples microservicios.
- **Seguridad con Keycloak**: para proteger las rutas de los microservicios con autenticación y autorización basada en OAuth2.
- **Infraestructura y Herramientas con Docker y Docker Compose**: para crear contenedores y orquestar los servicios en un entorno local o de producción.

Este proyecto sigue principios de diseño como **Domain-Driven Design (DDD)** y una arquitectura distribuida para garantizar modularidad, escalabilidad y mantenimiento.
