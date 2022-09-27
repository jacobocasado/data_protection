En la generacion de claves, hay que poner alguna cosilla mas

Lo mas importante es las claves
	openssl maneja las claves con un formato PEM
	ese formato se codifica en base64, similar a asciii
	---begin rsa key --- key -- end rsa key ---
	El formato mas estandar es PKCSH8,
keypairgenerator coge las cos cosas, kpublica y privada, objetos java **que debemos de mapearlo en algo (en un fichero)**

sabemos como estan leyendo, una vez con el objeto, debemos dumpearla een un fichero con ese formato.


para la 3º práctica
la clave de sesion y la firma ti enen tamaño fijo -> Para desencriptar es sencillo.

## Links usados para la p2
https://niels.nu/blog/2016/java-rsa

https://github.com/anujpatel/RSA-Algorithm/blob/master/RSAEncryptionDescription.java

https://cryptobook.nakov.com/digital-signatures/rsa-sign-verify-examples



# Para la práctica 3
1. No cambiar formato del comando. Debe ser ese.
2. Validar la entrada del usuario
	1. en la entrada del comando
	2. longitud de la clave a 16 bytes. 
	3. tocar la libreria de la p2 para que use el passphrase.
	4. no hay problema tampoco en usar la libreria de la p2 con el private.key y sobreescribir la private.key
3. Al final firmar. aunque no lo ponga en el apéndice.
4. Verificaciones:
	1. Ver si verifica la firma 
	2. Ver si pones mal la passphrase.
	3. Si no hay claves generadas23


**Para ver si esta bien**
Ver el tamaño de los ficheros. la clave de sesion y la firma son fijas.


**Metodología** de la praćtica:
encripto con simetrico, generada aleatoriamente la clave.
Como previamente tengo kpublica y kprivada, encripto la ksesion y la concateno al fichero y firmo con la kprivada (+passphrase); **ojo, hasheo y firmo.**
aseguro que la firma es correcta
desencripto la ksesion y con la ksesion desencripto el texto.

Ksesion 256 bytes.
sha 1 160 bytes