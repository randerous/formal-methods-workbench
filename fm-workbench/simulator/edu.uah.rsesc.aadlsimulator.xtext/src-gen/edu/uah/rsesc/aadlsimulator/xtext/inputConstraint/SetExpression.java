/**
 */
package edu.uah.rsesc.aadlsimulator.xtext.inputConstraint;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Set Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link edu.uah.rsesc.aadlsimulator.xtext.inputConstraint.SetExpression#getMembers <em>Members</em>}</li>
 * </ul>
 *
 * @see edu.uah.rsesc.aadlsimulator.xtext.inputConstraint.InputConstraintPackage#getSetExpression()
 * @model
 * @generated
 */
public interface SetExpression extends Expression
{
  /**
   * Returns the value of the '<em><b>Members</b></em>' containment reference list.
   * The list contents are of type {@link edu.uah.rsesc.aadlsimulator.xtext.inputConstraint.ScalarExpression}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Members</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Members</em>' containment reference list.
   * @see edu.uah.rsesc.aadlsimulator.xtext.inputConstraint.InputConstraintPackage#getSetExpression_Members()
   * @model containment="true"
   * @generated
   */
  EList<ScalarExpression> getMembers();

} // SetExpression
