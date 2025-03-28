package fichier_doc;

/** Cette classe identifie les terminaux reconnus et retournes par
 *  l'analyseur lexical
 */
public class Terminal {
// Constantes et attributs
//  ....

  public String name;
  public TerminalTokenType type;

/** Un ou deux constructeurs (ou plus, si vous voulez)
  *   pour l'initalisation d'attributs 
 */
public Terminal() {
    name = null;
    type = null;
}

  public Terminal(TerminalTokenType type) {   // arguments possibles
    setType(type);
  }

    public void setType(TerminalTokenType type) {
        switch (type) {
            case LITERAL -> this.name = "N";
            case PARENTHESIS_OPEN -> this.name = "O";
            case PARENTHESIS_CLOSE -> this.name = "C";
            case OPERATOR_PLUS_MINUS -> this.name = "P";
            case OPERATOR_MULTIPLIER_DIVIDER -> this.name = "D";
            case INSTRUCTION_END -> this.name = "E";
            case OPERATOR_ASSIGN -> this.name = "A";
            case null, default -> this.name = "";
        }
        this.type = type;
    }
}
