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
<prog> ::= <statlist>EOF

## Utilizzo