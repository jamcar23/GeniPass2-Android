# GeniPass-Android
GeniPass is a deterministic password manager that uses child key derivation cryptography to securely derive your passwords at runtime. What does this mean? It means GeniPass uses math to "load" (calculate) your passwords rather than store them in a database or on some server. Generated passwords consist of 6 words chosen randomly from a word list and have greater than 100 bits of entropy.

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [Usage](#usage)
- [Key Generation](#key-generation)
  - [Conventions](#conventions)
  - [Extended Keys](#extended-keys)
  - [Master Key Generation (MKG)](#master-key-generation-mkg)
  - [Child Key Derivation (CKD)](#child-key-derivation-ckd)
- [Passwords](#passwords)
  - [Random Generation](#random-generation)
  - [Key Derivation](#key-derivation)
- [Key Tree](#key-tree)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

## Usage 

GeniPass uses an username, master password, service, and tag. The service is name of the service for the password you want (i.e, Github) and the optional tag allows you to generate more than one password per service. Tip, for best practices don't make your username for GeniPass or any tag any common username you use (if people know you by that and it's easy to guess you probably shouldn't use it.) 

## Key Generation
### Conventions 

In the rest of this text I assume knowledge and understanding of one-way hash functions and key derivation functions. Knowledge in elliptic curve cryptography is helpful as I use the base point from ECDSA curve [secp256k1](http://www.secg.org/sec2-v2.pdf) to calculate the public keys. 

Concatenation (denoted ||) is the operation of appending one byte sequence to another. Xor (denoted ^) is the operation that outputs a byte sequence who's bits are true when both inputs differ (one is true the other is false.)

As standard convention functions, I assume:

* point(i): returns the x coordinate resulting from EC point multiplication of the secp256k1 base point with integer i
* parse(i): interprets a 32 byte sequence as a 256 bit number, big endian
* ser(i): encodes integer i into 32 byte sequence, big endian 
* ripemd(s): returns the RIPEMD-160 hash of s 
* sha3(c): returns the hash of c iterated through SHA3-256, 256 times hashing c twice per iteration for a total of 512 times. Before hashing c the first time one byte, containing the number of iterations (0, 1... 255), is append to c.
* hmac(_k, d): returns the HMAC-SHA2-512  hash where _k is the key and d is the data

The RIPEMD-160 function is specified [here](https://homes.esat.kuleuven.be/~bosselae/ripemd160.html).

The SHA-3 function is specified in [FIPS 202](http://nvlpubs.nist.gov/nistpubs/FIPS/NIST.FIPS.202.pdf)

The HMAC-SHA2-512 function is specified in [RFC 4868](https://tools.ietf.org/html/rfc4868).

### Extended Keys

In what follows, I will define a function that derives two generations of child keys from a parent key in order to derive a passphrase from the 2nd generation child key. Rather than derive the child directly from the key, the key is extended with 256 bits of entropy, called the chain code. The private and public keys do not share the same chain code. 

Extended private keys are denoted (k, c) where k is the normal private key and c is the private chain code. Extended public keys are denoted (K, C) where K = point(k) and C = sha3(c). K is in the compressed format, meaning it's only the x coordinate.

### Master Key Generation (MKG)

Below are the steps to generate the master private and public keys based on the username (denoted u) and master password (denoted mp). Genipass will not accept master passwords having less than 100 bits of entropy. 

* SCrypt returns a 64 byte sequence, denoted as b, using the following: 
	* P: ripemd(u) || ripemd(mp)
	* S: ripemd(u) ^ ripemd(mp) 
	* N: 2<sup>16</sup>
	* r: 8
	* p: 2
* Split b into two 32 byte sequences, b<sub>L</sub> and b<sub>R</sub>
* The returned master public key K is ser(point(parse(b<sub>L</sub>)))
* The returned master public chain code C is sha3(b<sub>R</sub>)
* (At this point the master private is discarded as it is no longer needed.) 

### Child Key Derivation (CKD)

Given a parent extended public key (denoted (K<sub>p</sub>, C<sub>p</sub>) where <sub>p</sub> means parent) and a string (denoted s where s is either the service or tag depending on whether its the second or third generation key respectively), it is possible to calculate a child public key using the steps below: 

* Let b = hmac(C<sub>p</sub>, s)
* Split b into two 32 byte sequences, b<sub>L</sub> and b<sub>R</sub>
* The returned child public key K is ser(point(parse(b<sub>L</sub>)))
* The returned child public chain code C is sha3(b<sub>R</sub>)
* (At this point the child private is discarded as it is no longer needed.)

## Passwords
GeniPass's passwords are created by looking up words on a specific line in a word list. The master password and all generated passwords must have greater than 100 bits of entropy. There are two ways to create a password as described below: 
### Random Generation 

GeniPass can generate a strong master password from random bytes. Steps are creating a random master password are as follows:

* Get 16 random bytes from /dev/urandom (using SecureRandom)
* Split bytes into 16 bit numbers 
* Look up each number in a word list 
* Add each word to password 
* Return password

 
### Key Derivation 

Given a second generation extended public key (K, C) it is possible to derive a password from the normal public key (K). The steps for password derivation are as follows:

* Remove first byte constant from K
* Split K into 16 bit numbers 
* Look up each number in a word list 
* Add each word to password 
* Return password

## Key Tree
![Key Tree](GeniPassKeyTree.png)

Above is the visual representation of key generation and password derivation. MKG() is defined in [Master Key Generation](#master-key-generation), CKD() is defined in [Child Key Derivation](#child-key-derivation), and PD() is defined in [Key Derivation](#key-derivation). 

For shorten notation I write CKD(CKD(m, s<sub>n</sub>), t<sub>n</sub>) as m/s<sub>n</sub>/t<sub>n</sub> All keys passed in CKD() are the public chain code. For both s<sub>n</sub> and t<sub>n</sub> n is any string entered by the user. 

Each leaf node in the tree corresponds to an actual public key (x coordinate on the curve), while the internal nodes correspond to the location of the branch needed to derive the leaf. The chain codes of leaf nodes are ignored as only their public key is needed to derive the password. 


