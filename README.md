# Ejecución del Proyecto
- Se requiere Java 17 o superior

Tras clonar el repositorio y entrar en el directorio raíz:
```bash
git clone git@github.com:alexgf0/demo.git
cd demo
```

podemos compilar el proyecto con el comando:
```bash
mvn clean install
```

ejecutar la aplicación con el comando:
```bash
mvn spring-boot:run
```

o ejecutar las *suites* de tests con el comando:
```bash
mvn test
```

# Definición de la API
A continuación veremos el uso de los diversos `endpoints` mediante peticiones `curl` para cada `endpoint`.

Adicionalmente en el directorio [misc](./misc), se incluye el fichero [DocutenDemo.postman_collection](./misc/DocutenDemo.postman_collection.json) para importar las peticiones más cómodamente a la herramienta `Postman`.
Sin embargo, también será necesario el cambio o propagación del `id` obtenido en la creación del usuario, a las diversas peticiones.


## Usuario
### Crear (Usuario)
el campo `secondSurname` es opcional.
```curl
curl --location 'http://localhost:8080/api/user/' \
--header 'Content-Type: application/json' \
--data '{
    "name": "Fernando",
    "firstSurname": "Alonso",
    "secondSurname": "Díaz"
}'
```

ejemplo de valor de retorno:
```json
{
  "id": "8fe77494-e539-4bc5-a843-3420a0e803c9",
  "name": "Fernando",
  "firstSurname": "Alonso",
  "secondSurname": "Díaz"
}
```
necesitaremos usar el **id de usuario** en las siguientes peticiones.

### Leer / Obtener (Usuario)
```curl
curl --location 'http://localhost:8080/api/user/8fe77494-e539-4bc5-a843-3420a0e803c9'
```

sustituir `8fe77494-e539-4bc5-a843-3420a0e803c9` por el respectivo id de usuario obtenido en la operación de creación.

### Actualizar (Usuario)
```curl
curl --location --request PUT 'http://localhost:8080/api/user/' \
--header 'Content-Type: application/json' \
--data '{
    "id": "8fe77494-e539-4bc5-a843-3420a0e803c9",
    "name": "Fernando",
    "firstSurname": "Alonso",
    "secondSurname": "Pérez"
}'
```
una vez deberemos rellenar el campo `id` acorde al valor obtenido.

### Borrar (Usuario)
Esta operación también borra las claves asignadas al usuario si estas existen.

```curl
curl --location --request DELETE 'http://localhost:8080/api/user/8fe77494-e539-4bc5-a843-3420a0e803c9'
```
el campo `id` debe corresponder a nuestro `id` de usuario.

## Claves
### Creación (Claves)
```curl
curl --location 'http://localhost:8080/api/keys/' \
--header 'Content-Type: application/json' \
--data '{
    "userId": "8fe77494-e539-4bc5-a843-3420a0e803c9"
}'
```
el valor del campo `userId` deberá corresponderse con el `id` de un usuario existente.

### Borrado (Claves)
```curl
curl --location --request DELETE 'http://localhost:8080/api/keys/8fe77494-e539-4bc5-a843-3420a0e803c9'
```
se deberá indicar el `id` de usuario para el cual queremos realizar el borrado de claves.

## Firma
### Firmar documento
```curl
curl --location 'http://localhost:8080/api/sign/create/' \
--header 'Content-Type: application/json' \
--data '{
    "userId": "8fe77494-e539-4bc5-a843-3420a0e803c9",
    "documentBase64": "VGVzdCBkb2N1bWVudCBjb250ZW50"
}'
```
el valor del campo `userId` deberá corresponderse con el `id` de un usuario existente que tenga claves generadas.

Si se realiza correctamente la petición a este `endpoint` recibiremos la firma en formato de String:
```text
F9GtCIMfmG89N6yR4316T3P4whfgBOQyYemTSWxx7fnoFNnlE58S9SCLzc0mPRhdeDz8rTJKVNk3l+KZLdgaO74IiRshtY+w3Pg6VhB3ddMLpUZJrH243hL4CgWy1GzzNaTeVpqEbt/4pHZJKQ59RJDvff0lbGhd0QKG9sPnrov4XTNf4CK5Wb3HmtsMhGw9Ob0BtXclJLW/qI9AeHxyUZ8SVAyAq+TQe6VfYl3oeCFGK/yNdH/VrbQUMrvhvuKb8DHbAtFxXqkTHQ7hGdsYiXI8/LjZPFZU3haef1n8Fv+h5oo9/0iNB7mSHfouF/4vAdMsqgt301bABZ5DOCGj4Q==
```

### Verificar firma
```curl
curl --location 'http://localhost:8080/api/sign/verify/' \
--header 'Content-Type: application/json' \
--data '{
    "userId": "8fe77494-e539-4bc5-a843-3420a0e803c9",
    "documentSignature": "F9GtCIMfmG89N6yR4316T3P4whfgBOQyYemTSWxx7fnoFNnlE58S9SCLzc0mPRhdeDz8rTJKVNk3l+KZLdgaO74IiRshtY+w3Pg6VhB3ddMLpUZJrH243hL4CgWy1GzzNaTeVpqEbt/4pHZJKQ59RJDvff0lbGhd0QKG9sPnrov4XTNf4CK5Wb3HmtsMhGw9Ob0BtXclJLW/qI9AeHxyUZ8SVAyAq+TQe6VfYl3oeCFGK/yNdH/VrbQUMrvhvuKb8DHbAtFxXqkTHQ7hGdsYiXI8/LjZPFZU3haef1n8Fv+h5oo9/0iNB7mSHfouF/4vAdMsqgt301bABZ5DOCGj4Q==",
    "documentBase64": "VGVzdCBkb2N1bWVudCBjb250ZW50"
}'
```
Para el correcto funcionamiento del endpoint, deberemos introducir el `userId` del usuario que firmó el documento, la firma de dicho documento en `documentSignature` y el documento original en `documentBase64`.



