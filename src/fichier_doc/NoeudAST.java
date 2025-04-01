package fichier_doc; /** @author Ahmed Khoumsi */

/** Classe representant une feuille d'AST
 */
public class NoeudAST extends ElemAST {

  // Attributs
  public String Name;
  //public ElemAST Parent;
  public ElemAST FirstChild;
  public ElemAST SecondChild;

  /** Constructeur pour l'initialisation d'attributs
   */
  public NoeudAST(String name, ElemAST firstChild, ElemAST secondChild) { // avec arguments
    this.Name = name;
    //this.Parent = null;
    this.FirstChild = firstChild;
    this.SecondChild = secondChild;
  }

  public NoeudAST(String name, ElemAST parent, ElemAST firstChild, ElemAST secondChild) { // avec arguments
    this.Name = name;
    //this.Parent = parent;
    this.FirstChild = firstChild;
    this.SecondChild = secondChild;
  }

 
  /** Evaluation de noeud d'AST
   */
  public int EvalAST( ) {
     return 0;
  }


  /** Lecture de noeud d'AST
   */
  public String LectAST( ) {
     return Name;
  }

}


