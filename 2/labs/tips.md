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

