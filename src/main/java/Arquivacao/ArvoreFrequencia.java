/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Arquivacao;

/**
 *
 * @author Aluno
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.Normalizer;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class ArvoreFrequencia {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite o caminho do arquivo: ");
        //pedindo o usuario para digitar qual arquivo ler o .txt
        String caminhoArquivo = scanner.nextLine();

        StringBuilder textoCompleto = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                textoCompleto.append(linha).append(" ");
            }
            System.out.println("Arquivo lido com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }

        String texto = textoCompleto.toString().toLowerCase();

        // Remove acentos e caracteres especiais
        texto = removerAcentos(texto);

        // Quebra o texto em palavras, removendo tudo que não for letra
        String[] palavras = texto.split("[^a-zA-Z]+");

        //criadores de arvore
        BSTree bst = new BSTree();
        AVLTree avl = new AVLTree();

        //lista enorme de Stopwords (basicamente palavras de complemento)
        String stopWordsStr = "de a o que e do da em um para é com não uma os no se na por mais as dos como mas foi ao ele das tem à seu sua ou ser quando muito há nos já está eu também só pelo pela até isso ela entre era depois sem mesmo aos ter seus quem nas me esse eles estão você tinha foram essa num nem suas meu às minha têm numa pelos elas havia seja qual será nós tenho lhe deles essas esses pelas este fosse dele tu te vocês vos lhes meus minhas teu tua teus tuas nosso nossa nossos nossas dela delas esta estes estas aquele aquela aqueles aquelas isto aquilo estou está estamos estão estive esteve estivemos estiveram estava estávamos estavam estivera estivéramos esteja estejamos estejam estivesse estivéssemos estivessem estiver estivermos estiverem hei há havemos hão houve houvemos houveram houvera houvéramos haja hajamos hajam houvesse houvéssemos houvessem houver houvermos houverem houverei houverá houveremos houverão houveria houveríamos houveriam sou somos são era éramos eram fui foi fomos foram fora fôramos seja sejamos sejam fosse fôssemos fossem for formos forem serei será seremos serão seria seríamos seriam tenho tem temos tém tinha tínhamos tinham tive teve tivemos tiveram tivera tivéramos tenha tenhamos tenham tivesse tivéssemos tivessem tiver tivermos tiverem terei terá teremos terão teria teríamos teriam";
        Set<String> stopWords = new HashSet<>(List.of(stopWordsStr.split(" ")));

        // Tempo de inserção na BST para compração
        long tempoBuscaBSTNano = 0;
        long inicioBST = System.nanoTime();
        for (String palavra : palavras) {
            if (!palavra.isEmpty() && !stopWords.contains(palavra)) {
                long inicioBusca = System.nanoTime();
                boolean existe = bst.search(palavra);
                long fimBusca = System.nanoTime();
                tempoBuscaBSTNano += (fimBusca - inicioBusca);

                if (!existe) {
                    bst.insert(palavra);
                }
            }
        }
        long fimBST = System.nanoTime();
        double tempoBST = (fimBST - inicioBST) / 1_000_000.0;
        double tempoBuscaBST = tempoBuscaBSTNano / 1_000_000.0;

        // Tempo de inserção na AVL para comparação
        long tempoBuscaAVLNano = 0;
        long inicioAVL = System.nanoTime();
        for (String palavra : palavras) {
            if (!palavra.isEmpty() && !stopWords.contains(palavra)) {
                long inicioBusca = System.nanoTime();
                boolean existe = avl.search(palavra);
                long fimBusca = System.nanoTime();
                tempoBuscaAVLNano += (fimBusca - inicioBusca);

                if (!existe) {
                    avl.insert(palavra);
                }
            }
        }
        long fimAVL = System.nanoTime();
        double tempoAVL = (fimAVL - inicioAVL) / 1_000_000.0;
        double tempoBuscaAVL = tempoBuscaAVLNano / 1_000_000.0;

        //Imprimindo no console
        System.out.println("\nFrequencia - Arvore Binaria de Pesquisa:");
        bst.printInOrder();
        System.out.println("Comparacoes: " + bst.stats.comparacoes);
        System.out.println("Atribuicoes: " + bst.stats.atribuicoes);
        System.out.printf("Tempo total (insercao + busca) na BST: %.3f ms\n", tempoBST);
        System.out.printf("Tempo total de busca na BST: %.3f ms\n", tempoBuscaBST);

        System.out.println("\nFrequencia - Arvore AVL:");
        avl.printInOrder();
        System.out.println("Comparacoes: " + avl.stats.comparacoes);
        System.out.println("Atribuicoes: " + avl.stats.atribuicoes);
        System.out.printf("Tempo total (insercao + busca) na AVL: %.3f ms\n", tempoAVL);
        System.out.printf("Tempo total de busca na AVL: %.3f ms\n", tempoBuscaAVL);

        //outra comparação para arquivos muito grandes ser mais facil de comparar
        System.out.println("_________________________________________________");
        System.out.println("Comparacao das da arvore BST e AVL");
        System.out.println("_________________________________________________");
        System.out.println("Arvore AVL:");
        System.out.printf("Tempo total (insercao + busca) na AVL: %.3f ms\n", tempoAVL);
        System.out.printf("Tempo total de busca na AVL: %.3f ms\n", tempoBuscaAVL);
        System.out.println("Comparacoes: " + avl.stats.comparacoes);
        System.out.println("Atribuicoes: " + avl.stats.atribuicoes);
        System.out.println("--------------");
        System.out.println("Arvore BST:");
        System.out.printf("Tempo total (insercao + busca) na BST: %.3f ms\n", tempoBST);
        System.out.printf("Tempo total de busca na BST: %.3f ms\n", tempoBuscaBST);
        System.out.println("Comparacoes: " + bst.stats.comparacoes);
        System.out.println("Atribuicoes: " + bst.stats.atribuicoes);
        System.out.println("_________________________________________________");

    }

    // Função para remover acentos e diacríticos
    public static String removerAcentos(String texto) {
        String textoNormalizado = Normalizer.normalize(texto, Normalizer.Form.NFD);
        return textoNormalizado.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}

// Classe para estatísticas
class Stats {

    public int comparacoes = 0;
    public int atribuicoes = 0;
}

// Árvore Binária de Pesquisa (sem balanceamento)
class BSTree {

    class Node {

        String palavra;
        int frequencia;
        Node left, right;

        Node(String palavra) {
            this.palavra = palavra;
            this.frequencia = 1;
        }
    }

    Node root;
    Stats stats = new Stats();

    public void insert(String palavra) {
        root = insertRec(root, palavra);
    }

    private Node insertRec(Node node, String palavra) {
        if (node == null) {
            stats.atribuicoes++;
            return new Node(palavra);
        }

        stats.comparacoes++;
        if (palavra.compareTo(node.palavra) < 0) {
            node.left = insertRec(node.left, palavra);
        } else if (palavra.compareTo(node.palavra) > 0) {
            node.right = insertRec(node.right, palavra);
        } else {
            node.frequencia++;
            stats.atribuicoes++;
        }

        return node;
    }

    public void printInOrder() {
        printInOrder(root);
    }

    private void printInOrder(Node node) {
        if (node != null) {
            printInOrder(node.left);
            System.out.println(node.palavra + ": " + node.frequencia);
            printInOrder(node.right);
        }
    }

    public boolean search(String palavra) {
        return searchRec(root, palavra);
    }

    private boolean searchRec(Node node, String palavra) {
        if (node == null) {
            return false;
        }

        stats.comparacoes++;
        int cmp = palavra.compareTo(node.palavra);
        if (cmp == 0) {
            return true;
        } else if (cmp < 0) {
            return searchRec(node.left, palavra);
        } else {
            return searchRec(node.right, palavra);
        }
    }
}

// Árvore AVL
class AVLTree {

    class Node {

        String palavra;
        int frequencia;
        int altura;
        Node left, right;

        Node(String palavra) {
            this.palavra = palavra;
            this.frequencia = 1;
            this.altura = 1;
        }
    }

    Node root;
    Stats stats = new Stats();

    public void insert(String palavra) {
        root = insertRec(root, palavra);
    }

    private Node insertRec(Node node, String palavra) {
        if (node == null) {
            stats.atribuicoes++;
            return new Node(palavra);
        }

        stats.comparacoes++;
        if (palavra.compareTo(node.palavra) < 0) {
            node.left = insertRec(node.left, palavra);
        } else if (palavra.compareTo(node.palavra) > 0) {
            node.right = insertRec(node.right, palavra);
        } else {
            node.frequencia++;
            stats.atribuicoes++;
            return node;
        }

        updateAltura(node);
        return balancear(node);
    }

    private void updateAltura(Node node) {
        node.altura = 1 + Math.max(getAltura(node.left), getAltura(node.right));
        stats.atribuicoes++;
    }

    private int getAltura(Node node) {
        return node == null ? 0 : node.altura;
    }

    private int getBalanceamento(Node node) {
        return node == null ? 0 : getAltura(node.left) - getAltura(node.right);
    }

    private Node balancear(Node node) {
        int balance = getBalanceamento(node);

        if (balance > 1 && getBalanceamento(node.left) < 0) {
            node.left = rotacaoEsquerda(node.left);
            return rotacaoDireita(node);
        }

        if (balance < -1 && getBalanceamento(node.right) > 0) {
            node.right = rotacaoDireita(node.right);
            return rotacaoEsquerda(node);
        }

        if (balance > 1) {
            return rotacaoDireita(node);
        }

        if (balance < -1) {
            return rotacaoEsquerda(node);
        }

        return node;
    }

    private Node rotacaoDireita(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        updateAltura(y);
        updateAltura(x);

        stats.atribuicoes += 3;
        return x;
    }

    private Node rotacaoEsquerda(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        updateAltura(x);
        updateAltura(y);

        stats.atribuicoes += 3;
        return y;
    }

    public void printInOrder() {
        printInOrder(root);
    }

    private void printInOrder(Node node) {
        if (node != null) {
            printInOrder(node.left);
            System.out.println(node.palavra + ": " + node.frequencia);
            printInOrder(node.right);
        }
    }

    public boolean search(String palavra) {
        return searchRec(root, palavra);
    }

    private boolean searchRec(Node node, String palavra) {
        if (node == null) {
            return false;
        }

        stats.comparacoes++;
        int cmp = palavra.compareTo(node.palavra);
        if (cmp == 0) {
            return true;
        } else if (cmp < 0) {
            return searchRec(node.left, palavra);
        } else {
            return searchRec(node.right, palavra);
        }
    }
}
