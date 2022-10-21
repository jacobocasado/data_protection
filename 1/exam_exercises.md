# Exercise 1 - CBC, CFB, OFB

Answer the following questions about the modes of operation CBC (block size 64 bits), CFB  and OFB (block size 32 bits) used with the DES algorithm



Block Size 64 bits
	There are normally 2 possible sizes, 64 for DES and 128 for AES

**Question** 1 :
**A random sequence of bits is sent and noise on the line causes one of the “0”s to become a  “1”. 
When decoding the text, how many bits in total will be altered?**

## With CBC
Answer:

We know 1 of the bits change. That means that, in a certain block, it will be changed.

Block -> 64 bits, with SAC Criterium, its 50% -> 32.
+1 bit changed on the next block -> 32 (half of the block) + 1 (xor on next block decypred because we use that block to decrypt) = 33.

**Answer: 33 bits.**

What happens with the symbols? -> Obviously they change.
But same symbols? **All the symbols change.**

## With CFB
CFB and OFB -> Block size is the half (32 bits)

Error propagates to 64 bit block -> 2 blocks
Each times, it "infects" half of the block size: 16 bits.
16 bits x 2 + 1 bit in the block = 33
If the bad bit is the last one, only 1 bit is bad.
If it is the penultimate, only (1+16) = 17 bits are bad.

## With OFB
No way to recover from a losing symbol! Important.
But if we have a problem in that bit we have the problem on that bit or that character.

Answer: 1 bit

**Question 2: 
A sequence of bits is sent consisting of 32 symbols of 16 bits each and noise on the line  
causes two bits that were “1” to become “0”: bit number 27 and bit number 470. When decoding  
the text, how many characters in total will be altered?

## With CBC

32 symbols, of 16 bits each.

S1-**S2**-S3-S4-S5-S6-S7-S8-....-**S30**-S31-S32

Block size 64 -> 4 symbols on each block (64 / 16)

We can see how many simbols are altered.

On CBC, all the symbols in the block are altered. We have 2 blocks altered fully, AND ANOTHER SYMBOL OF THE NEXT BLOCK IS ALTERED

## With CFB
Blocks with 2 characters each (32, size of block / 16, size of character)
The bad characters are S2 and S30
S1**S2**-**S3S4**-**S5S6**-....-S29**S30**-**S31S32**
1 character + 1 block (2 characters) +  1 block (2 characters) + ... + 1 block (2 characters) + 1 = 8 characters.

Question 3:
**A sequence of bits is sent consisting of 32 symbols of 16 bits each and noise on the line causes symbols 5, 6 and 30 not to arrive. When decoding the text, how many characters in total will be altered?**

## With CBC

Difference between original blocks and new blocks
1-2-3-4-5-6-7-8-9-10
1-2-3-4-**7**-**8**-**9**-10-11-12

CBC can repair a loss of information but if we lose an entire block.

## With CFB
S1S2 - S3S4 - S5S6 - S7S8 - S9S10
S1S2 - S3S4 - **S7S8** - S9S10 - S11S12

S5S6 fails by nature (it is lost)
S7S8 needs to receive S5S6 to decrypt -> It fails.
S9S10 fails too, because we lose 2 blocks.
same with S30, fails by nature and makes S31 and S32 to fail because we lose 2 blocks.


## With OFB
No way to recover from a losing symbol! Important.
But if we have a problem in that bit we have the problem on that bit or that character.

Answer: We lose everything from S5.
