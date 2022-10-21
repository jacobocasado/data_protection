
![[chapter1_slides_2022.pdf]]
Stallings is the reccomended book.

# Chapter 2 - What is security

Security is a **tradeoff** matter.
CERT and CSIRT -> Organizations that against threats

To physical resources:  
	- Theft, destruction, accidents, damage, usage errors  
To resource utilization:  
	- Reduction of availability, economic loss (disk space, CPU  
time, data volume transmitted on a leased line etc.)  
To stored information:  
	- Loss of integrity, confidentiality, availability  
To transmitted data:  
	- Loss of integrity, confidentiality, availability, authenticity, non-  
repudiation  
	**Point of interest -> Transmitting data is a clear point of attack (data is travelling)**
To the image and reputation:  
	- Inability to prevent or detect attacks or identify offenders,  
impact on third party assets, own responsibility

### Attacks
Most dangerous attacks -> Social engineering -> Weakest point of any system.
Phising -> Is profitable because at the end people fall in for them.

0day vulnerabilities -> Vulnerabilities that are not patched / fixed and they are "new". It happens because the vulnerability is not known, so nobody else knows. It's stealth and dangerous.


### Security costs
Investments (machines) + expenses (people)
Reputation
Complexity of use -> Tradeoff -> Something secure AND easy to use.
	If not, the performance can get harmed
Service restrictions
	It needs to be secure AND have the same use.
Performance reductions

#### Key: find the financial equilibrium between the security costs and the cost for incident.
Security costs will never be 0 (it decreases and then goes up again) and the cost for incident goes to 0 at 100% security level.

#### Total security does not exist.

## Security audit and security policies
Audits: You have certain persons that check if the security is being applied on your organization.
Policies: Rules inside the organization.

# Security services
## CIA:
- Availability
- Confidentiality
- Integrity -> Only authorized can access
## NR
- Non Repudiation

## Security mechanisms
Data encriptions
Message digests
Digital signatures
Autenthication exhange
Key echange

We are going to implement this mechanisms via **criptography**.

# Cryptosystem model - # 1 point in final exam
Important to hace a secure channel to facilitate the main communication in channel
Or a trusted third party with the key information of the communcation channel.


Larger key means slower operation -> **Tradeoff**
You need a good key space AND a fast computing time.

Simmetric key -> same for decription and encription
Assimetric -> Not the same to encrypt and decrypt

## Assumptions we have to make
Worst case:
	- Real time interception & inspection
	- Message alteration / creation / repetition
	- Bruteforce against obsolete algorithms or with good information of the key
	- Sources of attacks are known users
Wireless networks make perfectly valid some of the things before.

## Hop by hop encryption or end to end
It depends 
Hop by hop -> More information to encrypt & a single node can break the communication

# Chapter 3 - Symmetric cryptography
## Main slides:
Modern crypto
Types of ciphers
Confussion/Diffusion-SAC
Padding
Des -> Block/key
**Modes of operation**
Triple des
AES -> Block/Key
Needman - Schroder


## Security types:
Unconditional(theoretical)
Computational(practical)
	It can be possibly breakable as it is not secure from attacks with unlimited resources and time
Probable
	Not demonstrated that the sytem is insecure
Conditional

You need a key long enough to avoid brute-forcing attacks but not too long to make the system that use it slow.

## Modern crypto
Secret key:
	Simmetric encryption base
	Methods of authenticating messages
	Mechanisms of authentication and key echange
Public ...

## Types of cyphers
Block Ciphers:
	We have a long pipe and we BREAK the file into chunks and encode them separately.
	Basically, building into blocks.
	We will have to see afterwards how we join it
Stream Ciphers:
	We are encoding continuous strings of bits in such a way that each operation depends on the previous encoding, **not separately as before.**

**One time pad: Theoretical: ** XOR with the key as same length as the message.
Not crackeable (system is completely secure) but we have another problem.

## Confusion and diffusion
Imagine the operation with 1 block.
**Confusion**: the ciphertext depends (as complex as possible) on the key and the plaintext.
	That is archieved by substitution
**Difussion**: Without changing the letters but reordering them.
	Archieved by trasposition
	
What we do on modern times is apply a series of iterations / rounds of confussion and diffusion.
**SAC**: Variation of 1 bit in the plaintext OR the key results in a variation of the 50% of the CIPHERTEXT result of the rounds / iterations -> **STRAIGHT AVALANCHE CRITERION**
**We want our algorithms to have this property.**

## Random substitution
More powerful than transposition
	With transposition you have a less wider space.
It should be enough

### DES properties
- Block ciphers  with block size of 64 bits, key size of 56 bits (2^56) and the avalanche effect is 50%

The algorithm works in iterations.
	Initial permutation
	Iteration 1
		Expansion/permutation + XOR + Substitution/choice + permutation and final XOR
	You do it 16 times.
	
### Padding
Needed to make the sum of the size of the blocks equal to the size of the text.
We don't like padding because it adds redundancy.
But it is necessary.
Padding oracle -> Attack that uses the validation of the padding to attack.
There are many ways to do the paddings

With filling 0-1 paddings as tail, we have the problem that we may not know when does the message ends.

On PKCS#5 the padding is:
We need X characters to put at the end. 
We fill the rest with the character X.
To decrypt you get the last symbol and you delete a number related to the last symbol.
**What happens if the plaintext does not need padding? 
How we detect that the last number is not the nº of characters to delete?**
You can add another block full of 8's
But you need to do this always -> More redundancy.
You pay performance at the cost of security.
The encrypted message will always be bigger due to the padding.

### Modes of operation
We have to do something to the key to be different between each block (to be safer).
How to change that?
We are going to work with CBC (Cipher Block Chaining)
ECB doesn't do anything

### CBC
CBC encrypts the message + something that comes with the previous encrypted block and this is added to the XOR.
The key is the same -> But we apply the key 
C0 is the inicialization vector 

What happens if C1 has a problem?
You are saving for the upfollowing decryption a bad origin so the following block is going to be bad again. Only one additional block.
CBC

### CTR (counter mode)
Allows parallel processing

### CFB AND OFB
Useful in channels with a lot of noise.

## Double DES
Failed because of the Meet in the middle Attack
Its a problem based on nearly the same complexity (x2 in space)

## Triple DES
Much more secure but was substituted by AES.

## AES
Multiple size of keys: 128, 192, 256 bits keys.
The key has its own scheduel

# Class 2 - Thursday, week 1

## Key distribution
- Physical delivery: Secure but not useful
- Using an old key: If you capture onne of the keys the system is broken
- Specific key -> Protocol needed to generate the key
- Trusted third party -> Complicates the mechanism

## Direct Exchange 
K_s -> Session Key -> To identify the session
K_ab -> Encription key -> Key for communication 
T_A -> Timer -> Just in case the attacker gets the Key
B -> For replay attacks -> To identify the sender, to avoid MITM attacks


In first case, this communicasion can be easily intercepted

Second case: More useful to avoid replay attacks
The problem is how we manage how we share the encription key
Another problem for key distribution: 
$$
\frac{N\cdot(N-1)}{2}
$$
This is the number of keys we need to ensure a safe communication (one for each pair of nodes communicated)

## Needham  - Schroeder
![[Pasted image 20220915180548.png]]

One distribution center (KDC) -> Manages the keys so it acts like organizer.
	Problem of bottleneck on KDC

1. A talks to KDC -> I want to talk with B with a Session number N_a
2. The KDC has a secure key for each of the nodes that depend (in this case, one for A and B). The KDC sends the key to A with only a key that A knows, and a ticket. A doesnt know what is encrypted with Ekb.
3. A sends the encrypted thing to B
4. fill
5.

Reccomended upgrades:
	Timer for session key, to assure that after a certain time the Session key is not valid.

## Conclusions
Assymetric encription is NOT More secure than symmetric.
	Symmetric needs much less size of key to be as same as secure as symmetric
	But assymetric encryption is significantly slower than symmetric (1000 times hardware, 100 software).

Symmetric -> For **data**
Assymetric -> For **key**

# Asymmetric Algorithms
Used for authentication and encryption. 
Assymetric key pairs -> Public and private key.
Colloquialy, we authenticate with private, we encrypt with public.

