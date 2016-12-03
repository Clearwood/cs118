#include <stdio.h>
#include <math.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>
 int main(int argc, char* argv[]){
     if (argc < 2 || argc > 2){
         printf("usage: ./vigenere <keyword> \n");
         return 1;
     }
     char* keyw = argv[1];
     int u = strlen(keyw);
     int key[u];
     // generates encryption array
     for (int i = 0; i<u; i++){
         if (isalpha(keyw[i])){
          //checks if the char is lowersized and generates the value by which the text has to be encrypted
             if (islower(keyw[i])){
                 key[i] = keyw[i]-97;
             }
             else {
                 key[i] = keyw[i]-65;
             }
         }
         else {
            printf("usage: ./vigenere <keyword> \n");
         return 1; 
         }
     }
     char cipher[1024];
     scanf("%s", cipher);
     int k = strlen(cipher);
     int crypt[k];
     int symbol = 0;
        for (int i = 0; i < k; i++){
            // passes all symbols directly into the array
         if ((90<cipher[i] && cipher[i]<97) || cipher[i]<65 || cipher[i]>122){
         crypt[i]= cipher[i];
         symbol++;
         }
         int change = key[(i-symbol)%u];
         //checks for uppercase letters and encrypts them
         if (isupper(cipher[i])){
          if ((90-cipher[i])<(change)){
           int tp3= change - ((90-cipher[i])% change);
           crypt[i]= (64+tp3);
          }
          else {
           crypt[i]= (cipher[i]+change);
          }
         }
          //checks for lowercase letters and encrypts them
         else if (islower(cipher[i])){
          if ((122-cipher[i])<(change)){
           int tp2= change - ((122-cipher[i])% change);
           crypt[i]= (96+tp2);
          }
          else {
           crypt[i]= (cipher[i]+change);
          }
         }
         
     }
     for (int i =0; i < k; i++){
        printf("%c", crypt[i]);
    }
    printf("\n");
     return 0;
}
