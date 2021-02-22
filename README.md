# LFT-2020
Progetto per il corso di Linguaggi Formali e Traduttori dell'anno accademico 2020-2021.

## Struttura
È diviso in più parti:
1. DFA: contiene l'implementazione dei 10 DFA assegnati
2. LEXER 1/2/3: varie iterazioni dell'analizzatore lessicale dove ogni versione successiva aggiunge nuove funzionalitá 
3. PARSER 1/2:  due iterazioni del parsificatore, prende un programma in input e lo scompone nei vari token
4. VALUTATORE: valuta espressioni aritmetiche
5. TRANSLATOR: traduttore vero e proprio, prende in input un programma inserito nel file A.lft (secritto seguendo la sintassi sotto riportata) e lo trasforma in un programma eseguibile dalla JVM appoggiandosi a jasmin

## Grammatica
```bash
prog ::= statlist EOF

statlist ::= stat statlistp

statilistp ::= ; stat statlistp | eps

stat ::= = ID expr

         print (exprlist)

         read (ID)
         
         cond whenlist esle stat
         
         while (bexpr) stat
         
         {statlist}

whenlist ::= whenitem whenlistp

whenlistp ::= whenitem whenlistp | eps

whenitem ::= when (bexpr) do stat

bexpr ::= RELOP expr expr

expr ::= + (exprlist) | - expr expr | * (exprlist) | / expr expr | NUM | ID

exprlist ::= expr exprlistp

exprlistp ::= expr exprlistp | eps
```

## Utilizzo
La parte di utilizzo tratta unicamente il caso della traduzione. 
1. Il programma da tradurre deve essere inserito all'interno del file A.lft. 
2. Dopo aver compilato il file Translator.java lanciare 
```bash
java Translator
```