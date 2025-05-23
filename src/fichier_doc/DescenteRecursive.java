package fichier_doc;

/** @author Ahmed Khoumsi */

import java.util.ArrayList;
import java.util.Stack;

/** Cette classe effectue l'analyse syntaxique
 */
public class DescenteRecursive {

  // Attributs
  private int currentInstructionIndex;
  private int currentTokenIndex;
  private String fileContent;
  private Stack<ElemAST> nodes;

/** Constructeur de fichier_doc.DescenteRecursive :
      - recoit en argument le nom du fichier contenant l'expression a analyser
      - pour l'initalisation d'attribut(s)
 */
public DescenteRecursive(String in) {
  currentInstructionIndex = 0;
  currentTokenIndex = 0;
  fileContent = in;
  nodes = new Stack<>();
}


private String[] PreprocessFileContent() {
  String[] instructionArray = fileContent.replace("\r", "").replace("\n", "").split("E");
  for (int i = 0; i < instructionArray.length; i++) {
    if (!instructionArray[i].contains("A")) {
      continue;
    }
    instructionArray[i] = instructionArray[i].split("A")[1]; // Removes left assignation part
  }
  return instructionArray;
  // Old code that keeps semi colons, but no need for this prob
  // return fileContent.replace("\r", "").replace("\n", "").split("(?<=;)");
}


private ElemAST AnalyzeGrammarE(String instruction) {
  ElemAST node = AnalyzeGrammarT(instruction);

  if (currentTokenIndex < instruction.length()) {
    if (instruction.charAt(currentTokenIndex) != 'P') { // Plus or minus
      return node;
    }
    currentTokenIndex++;
    //ElemAST leftExpression = node;
    nodes.push(node);
    ElemAST rightExpression = AnalyzeGrammarE(instruction);
    return new NoeudAST("P", nodes.pop(), rightExpression);
  }
  //return new NoeudAST("ASD", node, nodes.pop());
  return node;
}


private ElemAST AnalyzeGrammarT(String instruction) {
  ElemAST node = AnalyzeGrammarF(instruction);

  if (currentTokenIndex < instruction.length()) {
    if (instruction.charAt(currentTokenIndex) != 'D') { // D = Multiplier or divider
      return node;
    }
    currentTokenIndex++;
    ElemAST rightExpression = AnalyzeGrammarT(instruction);
    return new NoeudAST("D", node, rightExpression);
  }
  return node;
}


private ElemAST AnalyzeGrammarF(String instruction) {
  if (currentTokenIndex >= instruction.length()) {
    ErreurSynt("Erreur de syntaxe dans l'instruction. Une opérande ou une parenthèse est manquante.");
  }
  if (instruction.charAt(currentTokenIndex) == 'O') { // Opening parenthesis
    currentTokenIndex++;
    ElemAST expressionNode = AnalyzeGrammarE(instruction);
    if (currentTokenIndex >= instruction.length()) {
      ErreurSynt("Erreur de syntaxe dans l'instruction. Une parenthèse fermante est manquante.");
    }
    if (instruction.charAt(currentTokenIndex) == 'C') { // Closing parenthesis
      currentTokenIndex++;
      return expressionNode;
    }
  } else if (instruction.charAt(currentTokenIndex) == 'C') {
    ErreurSynt("Erreur de syntaxe dans l'instruction. Une parenthèse ouvrante est manquante.");
  } else if (instruction.charAt(currentTokenIndex) == 'N') { // Operande (Variable ou nombre)
    currentTokenIndex++;
    return new FeuilleAST("N");
  } else {
    ErreurSynt("Erreur de syntaxe dans l'instruction.");
  }
  return null;
}


/** AnalSynt() effectue l'analyse syntaxique et construit l'AST.
 *    Elle retourne une reference sur la racine de l'AST construit
 */
public ArrayList<ElemAST> AnalSynt() {
  String[] instructions = PreprocessFileContent();
  ArrayList<ElemAST> asts = new ArrayList<>();

  while (currentInstructionIndex < instructions.length) {
    asts.add(AnalyzeGrammarE(instructions[currentInstructionIndex]));
    currentInstructionIndex++;
  }
   return asts;
}


// Methode pour chaque symbole non-terminal de la grammaire retenue
// ... 
// ...



/** ErreurSynt() envoie un message d'erreur syntaxique
 */
public void ErreurSynt(String s) {
  throw new RuntimeException(s);
}



  //Methode principale a lancer pour tester l'analyseur syntaxique 
  public static void main(String[] args) {
    String toWriteLect = "";
    String toWriteEval = "";

    System.out.println("Debut d'analyse syntaxique");
    if (args.length == 0) {
      args = new String [2];
      args[0] = "ResultatLexical.txt";
      args[1] = "ResultatSyntaxique.txt";
    }

    Reader r = new Reader(args[0]);
    DescenteRecursive dr = new DescenteRecursive(r.toString());
    try {
      ElemAST RacineAST = dr.AnalSynt().getFirst();
      toWriteLect += "Lecture de l'AST trouve : " + RacineAST.LectAST() + "\n";
      System.out.println(toWriteLect);
      toWriteEval += "Evaluation de l'AST trouve : " + RacineAST.EvalAST() + "\n";
      System.out.println(toWriteEval);
      Writer w = new Writer(args[1],toWriteLect+toWriteEval); // Ecriture de toWrite 
                                                              // dans fichier args[1]
    } catch (Exception e) {
      System.out.println(e);
      e.printStackTrace();
      System.exit(51);
    }
    System.out.println("Analyse syntaxique terminee");
  }

}

