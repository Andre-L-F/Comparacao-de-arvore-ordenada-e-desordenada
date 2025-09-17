# Comparacao-de-arvore-ordenada-e-desordenada
Criei um programa para comparar a busca em uma árvore ordenada (AVL) e em uma árvore desordenada, com o objetivo de facilitar a compreensão da diferença de tempo de busca entre as duas estruturas.

Obs:
Existem duas versões do programa: uma com interface gráfica e outra sem. A versão sem interface é mais indicada para entender o funcionamento do programa, enquanto a versão com interface proporciona melhor experiência de uso.

Observações próprias:

Percebi que a diferença no tempo de pesquisa entre as duas árvores não é tão significativa para testes unitários. Por exemplo, ao inserir o livro inteiro de Harry Potter em uma árvore desbalanceada, a busca levou 14,53 ms, enquanto na árvore balanceada levou 10,50 ms — uma diferença de apenas 4 ms. Considerando que se trata de um livro extenso, essa diferença é pequena. No entanto, o processamento O(log n) da árvore balanceada ainda é muito mais eficiente do que o de um vetor simples, que possui tempo de busca O(n).

Em um computador mais potente, o processamento foi obviamente mais rápido, e a diferença de tempo se tornou ainda menor. Mesmo assim, em computadores menos potentes, especialmente em bancos de dados acessados constantemente por múltiplos usuários, essa pequena otimização pode ajudar a reduzir a sobrecarga do sistema, diminuindo operações de processamento muitas vezes desnecessárias.
