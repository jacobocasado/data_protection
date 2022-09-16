To encrypt test0.txt to test0.enc
openssl enc -aes-128-cbc -in test0.txt -out test0.enc -K 31323334353637383930313233343536 -iv 31323334353637383930313233343536
To decrypt test0.enc to test0.dec
openssl enc -d -aes-128-cbc -in test0.enc -out test0.dec -K 31323334353637383930313233343536 -iv 31323334353637383930313233343536

Obviously, test0.txt and test0.dec will be the same!

1 -> Generate the plaintext with padding.
    Calculate the padding.
2 -> Length and byte value. If needed, add a new block.

3 -> CBC algorithm with XOR operation.

Reccomended: System.arraycopy