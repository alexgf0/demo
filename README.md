# Prueba técnica Docuten

El candidato debe crear una API REST simple que permita a los usuarios firmar digitalmente documentos y
verificar dichas firmas. Esto implica generar un par de claves (pública y privada), usar la clave privada para
firmar documentos y la clave pública para verificarla autenticidad de la firma.


## Endpoints

### Generación claves pública-privada

- Endpoint para generar un nuevo par de claves (pública y privada).
- Las claves deben ser almacenadas de manera segura y asociadas a un usuario específico.

### Firma de documentos
- Endpoint para firmar un documento. El documento puede ser enviado al endpoint como un string
en base64.
- El sistema debe usarla clave privada del usuario para firmar el documento.
- La API debe retornarla firma digital del documento.

### Verificación de la firma

- Endpoint para verificarla firma de un documento.
- El usuario enviará el documento original, la firma digital y el identificador del usuario cuya clave
pública debe usarse para la verificación.
- La API debe confirmar si la firma es válida o no.


## Entidades

### Usuario
Atributos:
- id
- name
- firstSurname
- secondSurname

Para `id` utilizaremos un `UUID` con generación no secuencial, dificultando la obtención de los datos si se desconoce el `id`.


### Claves
- Id de usuario
- Clave privada
- Clave pública





