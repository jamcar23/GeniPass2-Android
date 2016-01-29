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
- [Security](#security)
- [Test Vectors](#test-vectors)
  - [Vector 1](#vector-1)
  - [Vector 2](#vector-2)
  - [Vector 3](#vector-3)
- [License](#license)

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


## Security 
In addition to the expectations from the EC public-key cryptography itself:

* Given a public key K, an attacker cannot find the corresponding private key more efficiently than by solving the EC discrete logarithm problem (assumed to require 2<sup>128</sup> group operations).

the intended security properties of this cryptosystem are: 

* Given an extended public key (K<sub>s</sub>, C<sub>s</sub>) and the string s, an  attacker cannot find the extend parent key (K<sub>p</sub>, C<sub>p</sub>) more efficiently than a 2<sup>256</sup> brute force of HMAC-SHA2-512.
* Given any number of (string, extended public key) tuples (s<sub>t</sub>, K<sub>s<sub>t</sub></sub>, C<sub>s<sub>t</sub></sub>) with distinct s<sub>t</sub>'s, determining whether they are derived from a common parent extended public key (i.e, whether there exist a (K<sub>p</sub>, C<sub>p</sub>) such that for each t in (0...N-1) CKD((K<sub>p</sub>, C<sub>p</sub>), s<sub>t</sub>) = (K<sub>s<sub>t</sub></sub>, C<sub>s<sub>t</sub></sub>) cannot be done more efficiently than a 2<sup>256</sup> brute force of HMAC-SHA2-512.
* Given a public chain code C an attacker cannot find the corresponding private chain code more efficiently than a 2<sup>128</sup> brute force of SHA-3 256. 

## Test Vectors 
All values are in hex (except generated passwords) and all vectors use the following username (empty string):

* u: 9C1185A5C5E9FC54612808977EE8F548B2258D31

### Vector 1

* p: 6C0FB0277B5B72885C888DF8587D1C8843F3180F
* Master:
	* Extended Private:	D65E4E48FC8758CCC9191E08B9159CB41895E89197EDFA0FFBCF4BADE1AE7D1D63F8FB8C1DF7DFB7DAC608542B1E77BAD632C9E16F1F2AEA0897F37191034F3A
	* Extended Public:
02A986776AB659CD52C5AC2C0B6DD5671D1A5241C1591DD4701A176E7AC23660643B3B93EDF675053C01EA4C8662F0524184E6E5845C32EFF0BC1319FB9EDF1881FF
* First Child:
	* Extended Private:
E2D6776024F2F4F099C4A7F608FB6B2F8DA8C383CF0EB44EB0F05FD48F86276EAD59D29F775A7508F425A5E34C159F501ADDCD996ED1B6F02DE57B9B512F366A
	* Extended Public:
0386EE6C2ABD38D2086A64B601CF1C6F4C13D6F10B94BE635433D311F958A3EFA4196F09B621643F3ED6BC0E65C437B7DD6D6CE3249FBA92D3DA30B783E8BB78ACFF	
* Second Child:
	* Extended Private:
232049E7AACF83078EE5D8BCA01D1BD2B34D453B821323DB4579A47844EC44D2B72FD22E325A131D58D7CBDFDE26FEBF71C8DAA221213ED2178F69C4C6683997
	* Extended Public:
0381CC2AADF60DC08FC4AB422F4AC3CBDD52A99BCB315ED4F5345B6C7105BB593F98A0CF5CF3D43F0FBC578189780A01D9B4BC286EB953159CB39B2C1362EDF1A5FF
* Generated Password: cyma nauseated fettuccine schlep arbitrariness evert 

### Vector 2

* p: 8C7C5D0EDDADAB1041373DF22F6885A4102AAE58 
* Master:
	* Extended Private:
2D7032871C1456C6329E30F771407B6AEF1B6A8F29A9A51498CC1629273F0A55684ADDE35E9AB06E732D7B4A4FFB5231D17E1879A73FF0F7AB99C532599C19F1	
	* Extended Public:
0227BD7ACE9ECDF4BB2EA9F3EF5ABCAA253C5CE12969C05F0ECE08FEDC542822D96DC0088EA4833D7C389CB25786DF128C922E75BABF7534035C86CCE0664A8835FF
* First Child:
	* Extended Private:
06B4B21C95730330421223CCEDA9CBF41F5F5E8DF1595628174DC11CFFA248C96FD45A24A89D6CAF1A282BE2D3019C6BF08D357E49BB12F717F1C911CE34E9B0
	* Extended Public:
037FABA1C1DE0E2D9FE4B2F76115D0CE67284DB730BE84CFC1E3A8B8B8CBF48735968A69A1447D32BF4E4F7A23FC750798F2615C40389AE404BF2500C688EBC0FEFF	
* Second Child:
	* Extended Private:
075E943488A5005F82057E99E846E0362906BB782796F9D26E2A9A6E732281258FAFB0C5E5AAF5E438E498B5084E88866A6636A5D43DC177AE105B4824119110
	* Extended Public:
02F968D0971387B497E558BC63847DE34DBE2094A8510422823A216A60662705B02D553482A687CE5548C4DE91D5582E11C8D367AC93B35E0EAC0E0A73B843B9ADFF
* Generated Password: parulidae epona jaculate lamaist equalize quis 

### Vector 3

* p: E285540F7E99E40F86C593306D4F46E71C938035
* Master:
	* Extended Private:	
375EEFA837FE778C2E2C15493FA1CA21F518B261618C9DB7C5A37D257EB2C1DA5F2B9E7BD1AE34589633C413BCAD842F346862215407926ED0B559CA23EF384F
	* Extended Public:
02A0581A79DACEA240E1718BE22219CA07948B5A0F161A7EB9A67335F32F34AA261DC807814B4E8E4D80940770915B577186E5AC394621B94DECBBE1A5202D2B7AFF
* First Child:
	* Extended Private:
3C42657D540AD38F3DBFBF1A23970A86C0A75A770F2495F678111F0AF6ADA81FF96182636E54DEF29CAED278FC2AD380056D0EA366B9DBD443475ADCDE9486AF
	* Extended Public:	
03B684CD004D33C70F4F9F48361D35730F9C308D52AE6322FCAE3A9931D117E743B0A7445F59494DEE4C1C8EDA79426AB72F7A3AFFE2C992A6108B028F174831B0FF
* Second Child:
	* Extended Private:
00EC884D72B1A127F640DD1ED5B2E9795468ED65A681B1E7E12F74F49DA18691D4FCC099BA7708A3ADD6966B8DE43C3DBFB400FD739FE2BDAF242DED5103433B
	* Extended Public:
026DA903697D680439F48EFA21B75114D7D401AF4077DCA3C9DEC6AD5B3D5DC1DB7E40B2C80B193577FE32D7AA52B7BAD4AE9B8792EAC574225880C6A77B6B056FFF
* Generated Password: sicence dapper rectitude beakless ievidelicet codling 	

## License 

Copyright (c) 2015, 2016 James Carroll

The full license is available in the [license](LICENSE) file.

GeniPass is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

GeniPass is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with GeniPass.  If not, see <http://www.gnu.org/licenses/>.