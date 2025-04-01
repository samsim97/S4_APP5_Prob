package fichier_doc; /** @author Ahmed Khoumsi */

/** Classe representant une feuille d'AST
 */
public class FeuilleAST extends ElemAST {

  // Attribut(s)
    public String Name;
    //public NoeudAST Parent;

/**Constructeur pour l'initialisation d'attribut(s)
 */
public FeuilleAST(String name) {  // avec arguments
    this.Name = name;
    //this.Parent = null;
}

  public FeuilleAST(String name, NoeudAST parent) {  // avec arguments
    this.Name = name;
    //this.Parent = parent;
  }


  /** Evaluation de feuille d'AST
   */
  public int EvalAST( ) {
    return 0;
  }


 /** Lecture de chaine de caracteres correspondant a la feuille d'AST
  */
  public String LectAST( ) {
    return Name;
  }

}
