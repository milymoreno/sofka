# Aplicaciòn de prueba para fly pass
para ejecutarla de manera más ràpida, cree un archivo Docker Compose
para que el proyecto se ejecute en un contenedor Docker y la BD en otro Contenedor
Como requisito deben tener instalado Docker (el demonio) y doker compose

Crear un una estructura donde la aplicación que está en el repositorio se copie a una carpeta de nombre sofka
y a ese mismo nivel, colocar la de Mysql que está en el repositorio
Con esta estructura creada ejecutar el archivo docker-compose.yml

app_flyypass/

├── sofka/(Donde debe estar clonado el repositorio)

├── mysql/

├── docker-compose.yml

El repositorio es el siguiente:
https://github.com/milymoreno/pruebasura.git