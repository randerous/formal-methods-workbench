/**
 */
package com.rockwellcollins.atc.agree.agree;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Assert Statement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.rockwellcollins.atc.agree.agree.AssertStatement#getStr <em>Str</em>}</li>
 *   <li>{@link com.rockwellcollins.atc.agree.agree.AssertStatement#getExpr <em>Expr</em>}</li>
 *   <li>{@link com.rockwellcollins.atc.agree.agree.AssertStatement#getPattern <em>Pattern</em>}</li>
 * </ul>
 *
 * @see com.rockwellcollins.atc.agree.agree.AgreePackage#getAssertStatement()
 * @model
 * @generated
 */
public interface AssertStatement extends SpecStatement {
	/**
	 * Returns the value of the '<em><b>Str</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Str</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Str</em>' attribute.
	 * @see #setStr(String)
	 * @see com.rockwellcollins.atc.agree.agree.AgreePackage#getAssertStatement_Str()
	 * @model
	 * @generated
	 */
	String getStr();

	/**
	 * Sets the value of the '{@link com.rockwellcollins.atc.agree.agree.AssertStatement#getStr <em>Str</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Str</em>' attribute.
	 * @see #getStr()
	 * @generated
	 */
	void setStr(String value);

	/**
	 * Returns the value of the '<em><b>Expr</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Expr</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Expr</em>' containment reference.
	 * @see #setExpr(Expr)
	 * @see com.rockwellcollins.atc.agree.agree.AgreePackage#getAssertStatement_Expr()
	 * @model containment="true"
	 * @generated
	 */
	Expr getExpr();

	/**
	 * Sets the value of the '{@link com.rockwellcollins.atc.agree.agree.AssertStatement#getExpr <em>Expr</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Expr</em>' containment reference.
	 * @see #getExpr()
	 * @generated
	 */
	void setExpr(Expr value);

	/**
	 * Returns the value of the '<em><b>Pattern</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Pattern</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Pattern</em>' containment reference.
	 * @see #setPattern(PatternStatement)
	 * @see com.rockwellcollins.atc.agree.agree.AgreePackage#getAssertStatement_Pattern()
	 * @model containment="true"
	 * @generated
	 */
	PatternStatement getPattern();

	/**
	 * Sets the value of the '{@link com.rockwellcollins.atc.agree.agree.AssertStatement#getPattern <em>Pattern</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pattern</em>' containment reference.
	 * @see #getPattern()
	 * @generated
	 */
	void setPattern(PatternStatement value);

} // AssertStatement