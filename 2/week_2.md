## Diffie-Hellman Key exchange

Most popular session key exchange method.
Why? It is very easy to echange a key.
	But, you need to distribute information to **a middle man**.
	

1. Selection of a **prime number n (will be public)** and g a primitive root of n.
	But, although being public, you need a long key.
	As it is less secure than symettric encryption, you need like:
		1024, 2048, 4096 bits
		The same level of security of RSA
	We are covering a big space because of the exponent operation.

2. A selects a private part (S_A) of they keyset.
	1. And does the same
	2. You get both tickets, doing Ticket = g^S mod n
	3. (S is shared on a machine, the middleman).
	4. A and B exchange ticket.
	5. A and B calculate Key = Ticket(of the other one)^S_a mod n
	6. S key is the key of "El Gamal. Hard to crack."
		1. A and B don't need each other part
		
3. The key is finally shared, as both parts have the same result key.


## Shamir
Encription with the same key
The key depends on the same operations for A and B, so they both know.

# Digital Signatures
![[chapter5_slides_2022.pdf]]


## Integrity: 
a. Both message integrity (it's the correct message! )
b. data origin authentication (it's really bob who is sending the message)
c. Non-Repudiation (validate that YOU really sent the message, even if you said you didn't)
d. Non-Repudiation of the receiver: the same but with the destination.
e. Impersionation of the identity: How can we check if Alice is sending messages as if they were sent by Bob?

## Authentication of the sender
- Symmetric cryptography with shared secret
- Assimetric cryptography

## Message integrity
- Symmetric encoding with CRC
- MDCs (modification detector code)ç

## FIll NON REPUDIATION AND IMPERSIONATION


# Hash Function
Unidirectional -> Receives input (any length)
Fingerprint / marker / digest / hash value

It will have the same length independently of the size of the input.

## Two families of hash function
MDC -> Message alone
MAC -> Message + additional key

Why do we use hashes?
**advantage: velocity.**
Sometimes you even use hashing + dealing with collisions than using the direct nodes.

Short message that you need to manage
**BUT YOU NEED TO DEAL WITHOUT COLLISIONS!**

## CRC
Was designed to fight against noise.
We don't like CRC
**We want properties to avoid attacks!**

## Desired properties of HASH FUNCTIONS
1. Easy to compute
2. Unidirectional
	1. Pre image resistance aka. 
3. Compression -> Fixed length
4. Diffusion -> 1 bit flips, the digest changes almost half of the bits.


### Possible collision situations of a has
Unidirectional 
	every 2 share a hash, i think
	you have a message, you know **ANOTHER ONE HAS THE SAME HASH.**
Simple collision 
	We want 2 messages that gets in the same value
	**Dangerous: if we only use 0, and 1, and both have the same hash, how can we validate it?**
Strong collisions resistance


### Birthday paradox. Remember it

## Types of hash functions
MDC (manipulation detection codes) or MIC (message integrity codes) -> Do not use keys
	Unidirectials or ONE WAY HASH FUNCTIONS
	Collision resistant (strong) 
MAC: message authentication codes) → use keys
- The implicit key provides integrity, and if the key is a
shared secret, it also provides authentication.


## Generic model for iterated hash functions
It uses padding because not all the blocks will fit.

### MDCs of Rate 1 (1 encryption block)
Based on one iteration of a bllock cipher.
	Matyas-meyer-Oseas: Adding parts of the last block to fit.
	Davies

## MD4 -> Message Digest 4
128 bit hash function
2²⁰ operations necessary only.
4 encryption oerations for each of the blocks.


## Hash functions based on modular Arithmetic
2 disadvantages:
	- Proccessing speed. 
	- Estas trabajando en un espacio de trabajo mas pequeño -> No tienes garantias de seguridad.
	- Para el mismo tamaño de hash es mucho mas seguro uno simétrico que uno asimétrico.

## Message authentication Codes (MAC)
Similar of the practice 1
We use a key to the operation.
Ej. CBC-Mac algorithm.
You end with 1 block, you do not have to join.
Can it be decrypted? No, it adds a layer of security.







