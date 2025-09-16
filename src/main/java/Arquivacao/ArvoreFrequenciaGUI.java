/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Arquivacao;

/**
 *
 * @author Aluno
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.Normalizer;
import java.util.*;
import java.util.Arrays;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ArvoreFrequenciaGUI extends JFrame {

    private JTextField caminhoArquivoField;
    private JButton botaoSelecionarArquivo;
    private JButton botaoProcessar;
    private JButton botaoMostrarBST;
    private JButton botaoMostrarAVL;
    private JButton botaoVisualizarEstruturaBST;
    private JButton botaoVisualizarEstruturaAVL;

    private BSTree bst;
    private AVLTree avl;

    private StringBuilder bstOutput;
    private StringBuilder avlOutput;

    private Set<String> stopWords;

    private static final String STOPWORDS = "de a o que e do da em um para é com não uma os no se na por mais as dos como mas foi ao ele das tem à seu sua ou ser quando muito há nos já está eu também só pelo pela até isso ela entre era depois sem mesmo aos ter seus quem nas me esse eles estão você tinha foram essa num nem suas meu às minha têm numa pelos elas havia seja qual será nós tenho lhe deles essas esses pelas este fosse dele tu te vocês vos lhes meus minhas teu tua teus tuas nosso nossa nossos nossas dela delas esta estes estas aquele aquela aqueles aquelas isto aquilo estou está estamos estão estive esteve estivemos estiveram estava estávamos estavam estivera estivéramos esteja estejamos estejam estivesse estivéssemos estivessem estiver estivermos estiverem hei há havemos hão houve houvemos houveram houvera houvéramos haja hajamos hajam houvesse houvéssemos houvessem houver houvermos houverem houverei houverá houveremos houverão houveria houveríamos houveriam sou somos são era éramos eram fui foi fomos foram fora fôramos seja sejamos sejam fosse fôssemos fossem for formos forem serei será seremos serão seria seríamos seriam tenho tem temos tém tinha tínhamos tinham tive teve tivemos tiveram tivera tivéramos tenha tenhamos tenham tivesse tivéssemos tivessem tiver tivermos tiverem terei terá teremos terão teria teríamos teriam";

    public ArvoreFrequenciaGUI() {
        super("Contador de Frequência - Árvores BST e AVL");

        bst = new BSTree();
        avl = new AVLTree();
        stopWords = new HashSet<>(Arrays.asList(STOPWORDS.split(" ")));

        setLayout(new FlowLayout());

        caminhoArquivoField = new JTextField(30);
        caminhoArquivoField.setEditable(false);

        botaoSelecionarArquivo = new JButton("Selecionar Arquivo");
        botaoProcessar = new JButton("Processar Texto");
        botaoMostrarBST = new JButton("Mostrar Árvore BST");
        botaoMostrarAVL = new JButton("Mostrar Árvore AVL");
        botaoVisualizarEstruturaBST = new JButton("Visualizar Estrutura BST");
        botaoVisualizarEstruturaAVL = new JButton("Visualizar Estrutura AVL");

        // Inicialmente desabilitados os botões de exibir árvores e estruturas
        botaoMostrarBST.setEnabled(false);
        botaoMostrarAVL.setEnabled(false);
        botaoVisualizarEstruturaBST.setEnabled(false);
        botaoVisualizarEstruturaAVL.setEnabled(false);

        add(caminhoArquivoField);
        add(botaoSelecionarArquivo);
        add(botaoProcessar);
        add(botaoMostrarBST);
        add(botaoMostrarAVL);
        add(botaoVisualizarEstruturaBST);
        add(botaoVisualizarEstruturaAVL);

        setSize(650, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        botaoSelecionarArquivo.addActionListener(e -> selecionarArquivo());
        botaoProcessar.addActionListener(e -> processarArquivo());

        botaoMostrarBST.addActionListener(e -> mostrarArvore(bstOutput, "Árvore Binária de Pesquisa (BST)"));
        botaoMostrarAVL.addActionListener(e -> mostrarArvore(avlOutput, "Árvore AVL"));

        botaoVisualizarEstruturaBST.addActionListener(e -> mostrarEstruturaArvore(bst.getStructureString(), "Estrutura da Árvore BST"));
        botaoVisualizarEstruturaAVL.addActionListener(e -> mostrarEstruturaArvore(avl.getStructureString(), "Estrutura da Árvore AVL"));
    }

    private void selecionarArquivo() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Arquivos de texto", "txt");
        fileChooser.setFileFilter(filtro);

        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File arquivo = fileChooser.getSelectedFile();
            caminhoArquivoField.setText(arquivo.getAbsolutePath());
        }
    }

    private void processarArquivo() {
        String caminhoArquivo = caminhoArquivoField.getText();
        if (caminhoArquivo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um arquivo antes de processar!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        bst = new BSTree();
        avl = new AVLTree();
        bstOutput = new StringBuilder();
        avlOutput = new StringBuilder();

        StringBuilder textoCompleto = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                textoCompleto.append(linha).append(" ");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao ler o arquivo: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String texto = removerAcentos(textoCompleto.toString().toLowerCase());
        String[] palavras = texto.split("[^a-zA-Z]+");

        // ======= BST =======
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
        double tempoTotalBST = (fimBST - inicioBST) / 1_000_000.0;
        double tempoBuscaBST = tempoBuscaBSTNano / 1_000_000.0;

        // ======= AVL =======
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
        double tempoTotalAVL = (fimAVL - inicioAVL) / 1_000_000.0;
        double tempoBuscaAVL = tempoBuscaAVLNano / 1_000_000.0;

        // ======= Imprimir árvores =======
        bst.printInOrder(bstOutput);
        avl.printInOrder(avlOutput);

        // ======= Mostrar resultados =======
        StringBuilder relatorio = new StringBuilder();
        relatorio.append("===== Resultados =====\n\n");
        relatorio.append("BST - Arvore Binaria de Pesquisa:\n");
        relatorio.append(String.format("Tempo total (insercao + busca): %.3f ms\n", tempoTotalBST));
        relatorio.append(String.format("Tempo total apenas de busca: %.3f ms\n\n", tempoBuscaBST));

        relatorio.append("AVL - Arvore AVL:\n");
        relatorio.append(String.format("Tempo total (insercao + busca): %.3f ms\n", tempoTotalAVL));
        relatorio.append(String.format("Tempo total apenas de busca: %.3f ms\n\n", tempoBuscaAVL));

        JOptionPane.showMessageDialog(this, relatorio.toString(), "Resultados de Desempenho", JOptionPane.INFORMATION_MESSAGE);

        botaoMostrarBST.setEnabled(true);
        botaoMostrarAVL.setEnabled(true);
        botaoVisualizarEstruturaBST.setEnabled(true);
        botaoVisualizarEstruturaAVL.setEnabled(true);
    }

    private void mostrarArvore(StringBuilder arvoreStr, String titulo) {
        JFrame frame = new JFrame(titulo);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(true);                 // <<-- aqui
        JTextArea textArea = new JTextArea(arvoreStr.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        textArea.setLineWrap(false);
        textArea.setCaretPosition(0);

        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.setSize(900, 700);                  // tamanho inicial
        frame.setLocationRelativeTo(this);
        frame.setVisible(true);
    }

    private void mostrarEstruturaArvore(String estrutura, String titulo) {
        JFrame frame = new JFrame(titulo);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(true);  // <<-- agora redimensionável

        JTextArea textArea = new JTextArea(estrutura);
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        textArea.setLineWrap(false); // garante que a estrutura não quebre linhas
        textArea.setCaretPosition(0); // começa do topo

        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        frame.setSize(900, 700); // tamanho inicial da janela
        frame.setLocationRelativeTo(this); // centraliza em relação à janela principal
        frame.setVisible(true);
    }

    private String removerAcentos(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ArvoreFrequenciaGUI().setVisible(true));
    }
}

// BSTree modificada para gerar string da estrutura da árvore
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
        int cmp = palavra.compareTo(node.palavra);

        if (cmp < 0) {
            node.left = insertRec(node.left, palavra);
        } else if (cmp > 0) {
            node.right = insertRec(node.right, palavra);
        } else {
            node.frequencia++;
            stats.atribuicoes++;
        }

        return node;
    }

    // Imprime no console
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

    // Imprime no StringBuilder (para usar em GUI etc)
    public void printInOrder(StringBuilder sb) {
        printInOrder(root, sb);
    }

    private void printInOrder(Node node, StringBuilder sb) {
        if (node != null) {
            printInOrder(node.left, sb);
            sb.append(node.palavra).append(": ").append(node.frequencia).append("\n");
            printInOrder(node.right, sb);
        }
    }

    // Visualização da estrutura da árvore
    public String getStructureString() {
        StringBuilder sb = new StringBuilder();
        printStructure(root, sb, "", true);
        return sb.toString();
    }

    private void printStructure(Node node, StringBuilder sb, String prefix, boolean isTail) {
        if (node == null) {
            return;
        }

        sb.append(prefix)
                .append(isTail ? "└── " : "├── ")
                .append(node.palavra)
                .append(" (")
                .append(node.frequencia)
                .append(")")
                .append("\n");

        Node[] children = new Node[2];
        int count = 0;

        if (node.left != null) {
            children[count++] = node.left;
        }
        if (node.right != null) {
            children[count++] = node.right;
        }

        for (int i = 0; i < count; i++) {
            printStructure(children[i], sb, prefix + (isTail ? "    " : "│   "), i == count - 1);
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

// AVLTree modificada para gerar string da estrutura da árvore
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
        int cmp = palavra.compareTo(node.palavra);

        if (cmp < 0) {
            node.left = insertRec(node.left, palavra);
        } else if (cmp > 0) {
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

        if (balance > 1 && getBalanceamento(node.left) >= 0) {
            return rotacaoDireita(node);
        }

        if (balance > 1 && getBalanceamento(node.left) < 0) {
            node.left = rotacaoEsquerda(node.left);
            return rotacaoDireita(node);
        }

        if (balance < -1 && getBalanceamento(node.right) <= 0) {
            return rotacaoEsquerda(node);
        }

        if (balance < -1 && getBalanceamento(node.right) > 0) {
            node.right = rotacaoDireita(node.right);
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

    // Imprime no console
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

    // Imprime no StringBuilder (para GUI, etc)
    public void printInOrder(StringBuilder sb) {
        printInOrder(root, sb);
    }

    private void printInOrder(Node node, StringBuilder sb) {
        if (node != null) {
            printInOrder(node.left, sb);
            sb.append(node.palavra).append(": ").append(node.frequencia).append("\n");
            printInOrder(node.right, sb);
        }
    }

    // Visualização da estrutura da árvore
    public String getStructureString() {
        StringBuilder sb = new StringBuilder();
        printStructure(root, sb, "", true);
        return sb.toString();
    }

    private void printStructure(Node node, StringBuilder sb, String prefix, boolean isTail) {
        if (node == null) {
            return;
        }

        sb.append(prefix)
                .append(isTail ? "└── " : "├── ")
                .append(node.palavra)
                .append(" (")
                .append(node.frequencia)
                .append(")")
                .append("\n");

        Node[] children = new Node[2];
        int count = 0;

        if (node.left != null) {
            children[count++] = node.left;
        }
        if (node.right != null) {
            children[count++] = node.right;
        }

        for (int i = 0; i < count; i++) {
            printStructure(children[i], sb, prefix + (isTail ? "    " : "│   "), i == count - 1);
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

class Stats {

    public int comparacoes = 0;
    public int atribuicoes = 0;
}
