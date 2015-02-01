package api;

/**
 * A generic interface that can be used as a framework for defining types that model
 * families of abstract groups (in the mathematical sense).  This is achieved by following
 * the paradigm of having the class implement the Group interface <em>with the type parameter being 
 * the class itself</em>, (so that the operation is internal).  For example the header of the class definition for GLn_PrimeField 
 * looks as follows: &#160 &#160
 * <p>
 * &#160 &#160 &#160 &#160 <code>public class GLn_PrimeField implements Group&ltGLn_PrimeField&gt {...}</code>
 * </p>
 * <p>
 * In this way, if g and h are GLn_PrimeField objects, then g.rightProductBy(h) is also a
 * GLn_PrimeField object.
 * </p>
 * <p>
 * Besides the methods associated with the group operation, the Group interface 
 * incudes the method isOperationalWith() in order to distinguish between 
 * different groups in the same family.  For example, with the implementing class
 * GLn_PrimeField, the call g.isOperationalWith(h) returns true if and only if
 * g and h are GLn_PrimeField objects of the same dimension, and over the 
 * same prime field.
 * </p>
 * 
 * 
 * Of course, to define a legitimate group according to this paradigm, the
 * implemented methods must satisfy the following requirements:
 * <p>
 * &#160 &#160 &#160 &#160 (0) isOperationalWith() is an equivalence relation:<br>
 * &#160 &#160 &#160 &#160 &#160 &#160 &#160&#160&#160(i)   a.isOperationalWith(a) is always true<br>
 * &#160 &#160 &#160 &#160 &#160 &#160 &#160&#160(ii)  a.isOperationalWith(b) == b.isOperationalWith(a)<br>
 * &#160 &#160 &#160 &#160 &#160 &#160 &#160(iii) If (a.isOperationalWith(b) && b.isOperationalWith(c)), 
 *                      then a.isOperationalWith(c) is true.<br>
 * &#160 &#160 &#160 (1A) a.getIdentity() and a.getInverse() are both operational with a.<br>
 * &#160 &#160 &#160 (1B) If a is operational with b, then a.leftProductBy(b) and a.rightProductBy(b)
 *           are both operational with a.  Otherwise they both return null.<br>
 * If a, b, c are operational with each other, then:<br>
 * &#160 &#160 &#160 (1C)                  b.leftProductBy(a) equals a.rightProductBy(b)<br>  
 * &#160 &#160 &#160 (1D)                     a.getIdentity() equals b.getIdentity()<br>
 * &#160 &#160 &#160 &#160 (2) Associativity: c.leftProductBy(b).leftProductBy(a) equals c.leftProductBy(b.leftProductBy(a))<br>
 * &#160 &#160 &#160 &#160 (3) Identity: a.leftProductBy(a.getIdentity()) equals a<br>
 * &#160 &#160 &#160 &#160 (4) Inverses: a.leftProductBy(a.getInverse()) equals a.getIdentity()
 * </p>
 * <p>
 * Remark: If these requirements are satisfied, then it is automatic that the
 *         following are also satisfied:<br>
 * &#160 &#160 &#160 &#160 (3') a.rightProductBy(a.getIdentity()) equals a<br>
 * &#160 &#160 &#160 &#160 (4') a.rightProductBy(a.getInverse()) equals a.getIdentity()
 * </p>
 * <p>
 * <u>Important</u>: Besides overriding these methods, it is critically important
 * that the equals and hashCode methods be overridden, so that distinct objects
 * representing the same group element return a value of true when compared to one
 * another with the equals method.
 * </p>
 * 
 * General group-theoretic constructs and algorithms, such as the construction of Cayley graphs for
 * arbitrary finite groups, can be implemented as <em>generic</em> methods (or within a generic class)
 * that have a type parameter G that extends the Group interface over the same parameter G.
 * For example the header for the static method getCayleyGraph of the CayleyGraphBuilder class 
 * is as follows:
 * <p>
 * &#160 &#160 &#160 &#160 <code>public static &ltG extends Group&ltG&gt&gt NeighborGraph&ltG&gt getCayleyGraph(Set&ltG&gt genSet, G root)</code>
 * </p>
 * <p>
 * Within the definition of this method appears "generic" code something along the lines of:
 * </p>
 * <p>
 * &#160 &#160 &#160 &#160 <code>G g=root;</code><br>
 * &#160 &#160 &#160 &#160...<br>
 * &#160 &#160 &#160 &#160 <code>for (G s : genSet)</code><br>
 * &#160 &#160 &#160 &#160 &#160 &#160 &#160 <code>G neighbor = g.rightProductBy(s);</code>
 * </p>
 * 
 * With this method, one can construct a Cayley graph for <u><em>any</em></u> particular Group implementation, such as GLn_PrimeField, by making a call like the following:
 * <p>
 * &#160 &#160 <code>NeighborGraph&ltGLn_PrimeField&gt cayleyGraph = CayleyGraphBuilder.getCayleyGraph&ltGLn_PrimeField&gt(genSet, root);</code>
 * </p>
 * (see the api package of the Graphs module for the NeighborGraph&ltS&gt api, and the package cayleygraphs of the CayleyGraphs module for the CayleyGraphBuilder class).
 * 
 * 
 * 
 * @author pdokos
 * 
 * @param <T> The type on which the operation is defined.  In the intended use 
 * case for this interface, the type parameter is the same as the implementing class (see below).
 */
public interface Group<T> {

    /**
     * Returns the left action of h on the element making the call.
     * 
     * @param h Any element of T.
     * 
     * @return The left action of h on the element making the call.  Returns 
     * null if the calling element is not operational with h.
     */
    public T leftProductBy(T h);
    
    /**
     * Returns the right action of h on the element making the call.
     * 
     * @param h Any element of T.
     * 
     * @return The right action of h on the element making the call.  Returns 
     * null if the calling element is not operational with h.
     */
    public T rightProductBy(T h);
    
    /**
     * Returns the inverse of the element making the call.
     * 
     * @return The inverse of the element making the call.
     */
    public T getInverse();
    
    /**
     * Returns the identity element of the group that contains the element making the call.
     * 
     * @return The identity element of the group that contains the element making the call.
     */
    public T getIdentity();
    
    /**
     * 
     * @param h Any element of T.
     * 
     * @return true if h is in the same group as the element making the call.
     */
    public boolean isOperationalWith(T h);

}