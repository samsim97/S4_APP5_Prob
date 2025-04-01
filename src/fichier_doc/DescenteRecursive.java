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
  private String originalFileContent;
  private String[] instructions;
  private Stack<Character> openingParenthesisStack;
  private Boolean lastTokenIsLiteral = false;

/** Constructeur de fichier_doc.DescenteRecursive :
      - recoit en argument le nom du fichier contenant l'expression a analyser
      - pour l'initalisation d'attribut(s)
 */
public DescenteRecursive(String in, String originalExpression) {
  currentInstructionIndex = 0;
  currentTokenIndex = 0;
  fileContent = in;
  originalFileContent = originalExpression;
  openingParenthesisStack = new Stack<>();
  instructions = PreprocessFileContent();
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


private ElemAST AnalyzeGrammarE() {
  ElemAST node = AnalyzeGrammarT();
  String instruction = instructions[currentInstructionIndex];

  if (currentTokenIndex < instruction.length()) {
    if (instruction.charAt(currentTokenIndex) == 'P') { // Plus or minus
      lastTokenIsLiteral = false;
      String leftToken = String.valueOf(instruction.charAt(currentTokenIndex));
      currentTokenIndex++;
      ElemAST rightExpression = AnalyzeGrammarE();
      return new NoeudAST(leftToken, node, rightExpression);

    } else if (instruction.charAt(currentTokenIndex) == 'C' && openingParenthesisStack.empty()) {
      DisplaySyntaxError("Erreur de syntaxe dans l'instruction. Une parenthèse ouvrante est manquante.", currentTokenIndex);
    } else if (instruction.charAt(currentTokenIndex) == 'N' && lastTokenIsLiteral) {
      DisplaySyntaxError("Erreur de syntaxe dans l'instruction. Un litéral ne peut pas être suivi par un autre litéral.", currentTokenIndex);
    }
  }
  return node;
}


private ElemAST AnalyzeGrammarT() {
  ElemAST node = AnalyzeGrammarF();
  String instruction = instructions[currentInstructionIndex];

  if (currentTokenIndex < instruction.length()) {
    if (instruction.charAt(currentTokenIndex) == 'D') { // D = Multiplier or divider
      lastTokenIsLiteral = false;
      String leftToken = String.valueOf(instruction.charAt(currentTokenIndex));
      currentTokenIndex++;
      ElemAST rightExpression = AnalyzeGrammarE();
      return new NoeudAST(leftToken, node, rightExpression);

    } else if (instruction.charAt(currentTokenIndex) == 'C' && openingParenthesisStack.empty()) {
      DisplaySyntaxError("Erreur de syntaxe dans l'instruction. Une parenthèse ouvrante est manquante.", currentTokenIndex);
    }
  }
  return node;
}


private ElemAST AnalyzeGrammarF() {
  String instruction = instructions[currentInstructionIndex];
  if (currentTokenIndex >= instruction.length()) {
    DisplaySyntaxError("Erreur de syntaxe dans l'instruction. Une opérande ou une parenthèse est manquante.", currentTokenIndex);
  }
  if (instruction.charAt(currentTokenIndex) == 'O') { // Opening parenthesis
    openingParenthesisStack.push(instruction.charAt(currentTokenIndex));
    currentTokenIndex++;
    lastTokenIsLiteral = false;

    ElemAST expressionNode = AnalyzeGrammarE();
    if (currentTokenIndex >= instruction.length()) {
      DisplaySyntaxError("Erreur de syntaxe dans l'instruction. Une parenthèse fermante est manquante.", currentTokenIndex);
    }
    if (instruction.charAt(currentTokenIndex) == 'C') { // Closing parenthesis
        openingParenthesisStack.pop();
        currentTokenIndex++;
        lastTokenIsLiteral = false;

      if (currentTokenIndex < instruction.length() && instruction.charAt(currentTokenIndex) == 'N') {
        DisplaySyntaxError("Erreur de syntaxe dans l'instruction. Un opérateur est manquant.", currentTokenIndex);
      }

      return expressionNode;
    } else {
      DisplaySyntaxError("Erreur de syntaxe dans l'instruction. Une parenthèse fermante est manquante.", currentTokenIndex);
    }
  } else if (instruction.charAt(currentTokenIndex) == 'C') {
    if (currentTokenIndex - 1 >= 0 && (instruction.charAt(currentTokenIndex - 1) == 'P' || instruction.charAt(currentTokenIndex - 1) == 'D')) {
      DisplaySyntaxError("Erreur de syntaxe dans l'instruction. Une opérande est manquante.", currentTokenIndex);
    }
    DisplaySyntaxError("Erreur de syntaxe dans l'instruction. Une parenthèse ouvrante est manquante.", currentTokenIndex);
  } else if (instruction.charAt(currentTokenIndex) == 'N') { // Operande (Variable ou nombre)
    if (currentTokenIndex + 1 < instruction.length() - 1 && instruction.charAt(currentTokenIndex + 1) == 'O') {
      DisplaySyntaxError("Erreur de syntaxe dans l'instruction. Un opérateur est manquant.", currentTokenIndex);
    }
    currentTokenIndex++;
    lastTokenIsLiteral = true;

    return new FeuilleAST("N");
  } else {
    DisplaySyntaxError("Erreur de syntaxe dans l'instruction. Un opérateur innattandu a été détecté.", currentTokenIndex);
  }
  return null;
}


private void DisplaySyntaxError(String msg, int lastTokenIndex) {
  String errorMessage = msg + "\n";
  String[] instructionExpressions = originalFileContent.split("(?<=[+\\-*/()])|(?=[+\\-*/();])");
  StringBuilder detailedError = new StringBuilder();
  for (int i = 0; i < lastTokenIndex; i++) {
    detailedError.append(instructionExpressions[i]);
  }
  int errorIndicationStartIndex = detailedError.length();
  detailedError.append(instructionExpressions[lastTokenIndex]);
  int errorIndicationEndIndex = detailedError.length();
  for (int i = lastTokenIndex + 1; i < instructionExpressions.length; i++) {
    detailedError.append(instructionExpressions[i]);
  }
  detailedError.append("\n");
  for (int i = 0; i < originalFileContent.length(); i++) {
    if (i >= errorIndicationStartIndex && i < errorIndicationEndIndex) {
      detailedError.append("^");
    } else {
      detailedError.append(" ");
    }
  }
  ErreurSynt(errorMessage + "\n" + detailedError.toString());
}


/** AnalSynt() effectue l'analyse syntaxique et construit l'AST.
 *    Elle retourne une reference sur la racine de l'AST construit
 */
public ArrayList<ElemAST> AnalSynt() {
  String[] instructions = PreprocessFileContent();
  ArrayList<ElemAST> asts = new ArrayList<>();

  while (currentInstructionIndex < instructions.length) {
    asts.add(AnalyzeGrammarE());
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
    String lexicalExpression = "";
    String toWriteLect = "";
    String toWriteEval = "";

    System.out.println("Debut d'analyse syntaxique");
    if (args.length == 0) {
      args = new String [2];
      args[0] = "ExpArith.txt";
      args[1] = "ResultatSyntaxique.txt";
    }

    Reader r = new Reader(args[0]);

    AnalLex lexical = new AnalLex(r.toString());

    Terminal t = null;
    while(lexical.resteTerminal()) {
      t = lexical.prochainTerminal();
      lexicalExpression += t.name; // + "\n" ;  // toWrite contient le resultat
      if (t.type == TerminalTokenType.INSTRUCTION_END) {
        lexicalExpression += "\n";
      }
    }

    DescenteRecursive dr = new DescenteRecursive(lexicalExpression, r.toString());
    try {
      dr.AnalSynt();
      //ElemAST RacineAST = dr.AnalSynt().getFirst();
      //toWriteLect += "Lecture de l'AST trouve : " + RacineAST.LectAST() + "\n";
      //System.out.println(toWriteLect);
      //toWriteEval += "Evaluation de l'AST trouve : " + RacineAST.EvalAST() + "\n";
      //System.out.println(toWriteEval);
      //Writer w = new Writer(args[1],toWriteLect+toWriteEval); // Ecriture de toWrite
                                                              // dans fichier args[1]
    } catch (Exception e) {
      System.out.println(e);
      e.printStackTrace();
      System.exit(51);
    }
    System.out.println("Analyse syntaxique terminee");
  }

}

