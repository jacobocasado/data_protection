# Certificates
## Certificates of identity
Identity data signed by anybody you trust.
Anybody of trust is someone called "Trusted Third Party" or CA, Certificate Authority.

It consists of:
	- Public key
	- Identifier of the user
	- Signed digitally by a CA.
Format: X.509

## X.509 Versions
There are several versions, but the main use was from the 2000 because the extensions are the core to add more information related to the way to deprecate of the certificate (Validity info)
V3 certificate -> Extensions

Hash of the certificate -> We sign the hash

To validate the certificate, you ask for the public key of the CA and validate the user.
We are going to use **the certificate of the CA to validate the certificate**


## X.509 Extensions
Can be public or private.
Can be defined as critical (must not be ommited) and non critical (may be ommited, no need to understand)

### Public extensions
Certificate revocation list -> Database of revoked certificates.
	You must do an extra consultation on this DB, to see if its revoked
	Protocol: OCSP (quite slow because of the check.)


## X.509 types
We have seen the certificates of names (or PKC)
But there is a second kind of certificates: attribute certificates (AC)
	Used to validate attributes asociated to the PKC
## CRC
Certificate Revocation List
## PKI
Exam:
![[Pasted image 20220927182252.png]]

RA: Registration authority. El que te da los datos de alta cuando te lo haces.
CA: Certification Authority: Who signs it. 
User does transaction on the RA and CA.

There is another possible entity: VA (VALIDATION AUTHORITY)
	afirma is a Validation Authority.
	It cannot issue certificates but you can validate certificates.
	It has a image of the repository of the CAs
	Not used to deliver, only validate.

## PMI
Privilege Management Infraestructure.

# Exam questions
Example 1: On v1 there are no extensions.
	No information related to CRL, etc.
Public key:
	With rsa encryption -> Modulus and Exponent.

Example 2: Version 3:
Extensions:
Root certificate of the CA. Because:
	Critical constraint CA: True.
	Serial number is 1. (first certificate of the CA)
	Who has signed this certificate? Autosigned. The root is autofirmed.
	How we know? Issuer = Subject.
	**Como saber que es raiz -> Issuer y subject son los mismos.**
	Si el numero de serie es 1 suele ser pero nadie te lo asegura.
	La CA Puede ser true pero puede ser un certificado dentro de la misma clase y no ser raíz, por lo que no hay que fi jarse únicamente en eso.

Example 3: version 3
10 years -> Something that wants to last.
Public key of 4096 bit
Signature algorithm sha256
On extensions:
	OCSP to check if the certificate is revoked
	OID: Check what type of instancee is trying to login. 
		User, employee, SSL component...
		It's optional -> You can't make it neccesary to all usersl.

Example 4: Version 3
CA: False -> It's for an user.
