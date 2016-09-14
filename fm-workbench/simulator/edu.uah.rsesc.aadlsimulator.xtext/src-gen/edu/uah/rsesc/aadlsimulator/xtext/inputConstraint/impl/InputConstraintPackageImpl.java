/**
 */
package edu.uah.rsesc.aadlsimulator.xtext.inputConstraint.impl;

import edu.uah.rsesc.aadlsimulator.xtext.inputConstraint.BinaryExpression;
import edu.uah.rsesc.aadlsimulator.xtext.inputConstraint.BooleanLiteral;
import edu.uah.rsesc.aadlsimulator.xtext.inputConstraint.ConstRefExpression;
import edu.uah.rsesc.aadlsimulator.xtext.inputConstraint.ElementRefExpression;
import edu.uah.rsesc.aadlsimulator.xtext.inputConstraint.Expression;
import edu.uah.rsesc.aadlsimulator.xtext.inputConstraint.InputConstraint;
import edu.uah.rsesc.aadlsimulator.xtext.inputConstraint.InputConstraintFactory;
import edu.uah.rsesc.aadlsimulator.xtext.inputConstraint.InputConstraintPackage;
import edu.uah.rsesc.aadlsimulator.xtext.inputConstraint.IntegerLiteral;
import edu.uah.rsesc.aadlsimulator.xtext.inputConstraint.IntervalExpression;
import edu.uah.rsesc.aadlsimulator.xtext.inputConstraint.NegativeExpression;
import edu.uah.rsesc.aadlsimulator.xtext.inputConstraint.Operator;
import edu.uah.rsesc.aadlsimulator.xtext.inputConstraint.PreExpression;
import edu.uah.rsesc.aadlsimulator.xtext.inputConstraint.RandomElementExpression;
import edu.uah.rsesc.aadlsimulator.xtext.inputConstraint.RandomExpression;
import edu.uah.rsesc.aadlsimulator.xtext.inputConstraint.RandomIntegerExpression;
import edu.uah.rsesc.aadlsimulator.xtext.inputConstraint.RandomRealExpression;
import edu.uah.rsesc.aadlsimulator.xtext.inputConstraint.RealLiteral;
import edu.uah.rsesc.aadlsimulator.xtext.inputConstraint.RefExpression;
import edu.uah.rsesc.aadlsimulator.xtext.inputConstraint.ScalarExpression;
import edu.uah.rsesc.aadlsimulator.xtext.inputConstraint.SetExpression;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class InputConstraintPackageImpl extends EPackageImpl implements InputConstraintPackage
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass inputConstraintEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass expressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass scalarExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass randomExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass refExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass intervalExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass setExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass integerLiteralEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass realLiteralEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass booleanLiteralEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass binaryExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass preExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass randomIntegerExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass randomRealExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass randomElementExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass elementRefExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass constRefExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass negativeExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum operatorEEnum = null;

  /**
   * Creates an instance of the model <b>Package</b>, registered with
   * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
   * package URI value.
   * <p>Note: the correct way to create the package is via the static
   * factory method {@link #init init()}, which also performs
   * initialization of the package, or returns the registered package,
   * if one already exists.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.emf.ecore.EPackage.Registry
   * @see edu.uah.rsesc.aadlsimulator.xtext.inputConstraint.InputConstraintPackage#eNS_URI
   * @see #init()
   * @generated
   */
  private InputConstraintPackageImpl()
  {
    super(eNS_URI, InputConstraintFactory.eINSTANCE);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static boolean isInited = false;

  /**
   * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
   * 
   * <p>This method is used to initialize {@link InputConstraintPackage#eINSTANCE} when that field is accessed.
   * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static InputConstraintPackage init()
  {
    if (isInited) return (InputConstraintPackage)EPackage.Registry.INSTANCE.getEPackage(InputConstraintPackage.eNS_URI);

    // Obtain or create and register package
    InputConstraintPackageImpl theInputConstraintPackage = (InputConstraintPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof InputConstraintPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new InputConstraintPackageImpl());

    isInited = true;

    // Create package meta-data objects
    theInputConstraintPackage.createPackageContents();

    // Initialize created meta-data
    theInputConstraintPackage.initializePackageContents();

    // Mark meta-data to indicate it can't be changed
    theInputConstraintPackage.freeze();

  
    // Update the registry and return the package
    EPackage.Registry.INSTANCE.put(InputConstraintPackage.eNS_URI, theInputConstraintPackage);
    return theInputConstraintPackage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getInputConstraint()
  {
    return inputConstraintEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getExpression()
  {
    return expressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getScalarExpression()
  {
    return scalarExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getRandomExpression()
  {
    return randomExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getRefExpression()
  {
    return refExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIntervalExpression()
  {
    return intervalExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIntervalExpression_LeftClosed()
  {
    return (EAttribute)intervalExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIntervalExpression_Left()
  {
    return (EReference)intervalExpressionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIntervalExpression_Right()
  {
    return (EReference)intervalExpressionEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIntervalExpression_RightClosed()
  {
    return (EAttribute)intervalExpressionEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getSetExpression()
  {
    return setExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSetExpression_Members()
  {
    return (EReference)setExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIntegerLiteral()
  {
    return integerLiteralEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIntegerLiteral_Value()
  {
    return (EAttribute)integerLiteralEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getRealLiteral()
  {
    return realLiteralEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRealLiteral_Value()
  {
    return (EAttribute)realLiteralEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getBooleanLiteral()
  {
    return booleanLiteralEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getBooleanLiteral_Value()
  {
    return (EAttribute)booleanLiteralEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getBinaryExpression()
  {
    return binaryExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getBinaryExpression_Left()
  {
    return (EReference)binaryExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getBinaryExpression_Op()
  {
    return (EAttribute)binaryExpressionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getBinaryExpression_Right()
  {
    return (EReference)binaryExpressionEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getPreExpression()
  {
    return preExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getPreExpression_Ref()
  {
    return (EReference)preExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getRandomIntegerExpression()
  {
    return randomIntegerExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getRandomIntegerExpression_Interval()
  {
    return (EReference)randomIntegerExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getRandomRealExpression()
  {
    return randomRealExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getRandomRealExpression_Interval()
  {
    return (EReference)randomRealExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getRandomElementExpression()
  {
    return randomElementExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getRandomElementExpression_Set()
  {
    return (EReference)randomElementExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getElementRefExpression()
  {
    return elementRefExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getElementRefExpression_Ids()
  {
    return (EAttribute)elementRefExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getConstRefExpression()
  {
    return constRefExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getConstRefExpression_PackageSegments()
  {
    return (EAttribute)constRefExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getConstRefExpression_ConstantName()
  {
    return (EAttribute)constRefExpressionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getNegativeExpression()
  {
    return negativeExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getNegativeExpression_Value()
  {
    return (EReference)negativeExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EEnum getOperator()
  {
    return operatorEEnum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public InputConstraintFactory getInputConstraintFactory()
  {
    return (InputConstraintFactory)getEFactoryInstance();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isCreated = false;

  /**
   * Creates the meta-model objects for the package.  This method is
   * guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void createPackageContents()
  {
    if (isCreated) return;
    isCreated = true;

    // Create classes and their features
    inputConstraintEClass = createEClass(INPUT_CONSTRAINT);

    expressionEClass = createEClass(EXPRESSION);

    scalarExpressionEClass = createEClass(SCALAR_EXPRESSION);

    randomExpressionEClass = createEClass(RANDOM_EXPRESSION);

    refExpressionEClass = createEClass(REF_EXPRESSION);

    intervalExpressionEClass = createEClass(INTERVAL_EXPRESSION);
    createEAttribute(intervalExpressionEClass, INTERVAL_EXPRESSION__LEFT_CLOSED);
    createEReference(intervalExpressionEClass, INTERVAL_EXPRESSION__LEFT);
    createEReference(intervalExpressionEClass, INTERVAL_EXPRESSION__RIGHT);
    createEAttribute(intervalExpressionEClass, INTERVAL_EXPRESSION__RIGHT_CLOSED);

    setExpressionEClass = createEClass(SET_EXPRESSION);
    createEReference(setExpressionEClass, SET_EXPRESSION__MEMBERS);

    integerLiteralEClass = createEClass(INTEGER_LITERAL);
    createEAttribute(integerLiteralEClass, INTEGER_LITERAL__VALUE);

    realLiteralEClass = createEClass(REAL_LITERAL);
    createEAttribute(realLiteralEClass, REAL_LITERAL__VALUE);

    booleanLiteralEClass = createEClass(BOOLEAN_LITERAL);
    createEAttribute(booleanLiteralEClass, BOOLEAN_LITERAL__VALUE);

    binaryExpressionEClass = createEClass(BINARY_EXPRESSION);
    createEReference(binaryExpressionEClass, BINARY_EXPRESSION__LEFT);
    createEAttribute(binaryExpressionEClass, BINARY_EXPRESSION__OP);
    createEReference(binaryExpressionEClass, BINARY_EXPRESSION__RIGHT);

    preExpressionEClass = createEClass(PRE_EXPRESSION);
    createEReference(preExpressionEClass, PRE_EXPRESSION__REF);

    randomIntegerExpressionEClass = createEClass(RANDOM_INTEGER_EXPRESSION);
    createEReference(randomIntegerExpressionEClass, RANDOM_INTEGER_EXPRESSION__INTERVAL);

    randomRealExpressionEClass = createEClass(RANDOM_REAL_EXPRESSION);
    createEReference(randomRealExpressionEClass, RANDOM_REAL_EXPRESSION__INTERVAL);

    randomElementExpressionEClass = createEClass(RANDOM_ELEMENT_EXPRESSION);
    createEReference(randomElementExpressionEClass, RANDOM_ELEMENT_EXPRESSION__SET);

    elementRefExpressionEClass = createEClass(ELEMENT_REF_EXPRESSION);
    createEAttribute(elementRefExpressionEClass, ELEMENT_REF_EXPRESSION__IDS);

    constRefExpressionEClass = createEClass(CONST_REF_EXPRESSION);
    createEAttribute(constRefExpressionEClass, CONST_REF_EXPRESSION__PACKAGE_SEGMENTS);
    createEAttribute(constRefExpressionEClass, CONST_REF_EXPRESSION__CONSTANT_NAME);

    negativeExpressionEClass = createEClass(NEGATIVE_EXPRESSION);
    createEReference(negativeExpressionEClass, NEGATIVE_EXPRESSION__VALUE);

    // Create enums
    operatorEEnum = createEEnum(OPERATOR);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isInitialized = false;

  /**
   * Complete the initialization of the package and its meta-model.  This
   * method is guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void initializePackageContents()
  {
    if (isInitialized) return;
    isInitialized = true;

    // Initialize package
    setName(eNAME);
    setNsPrefix(eNS_PREFIX);
    setNsURI(eNS_URI);

    // Create type parameters

    // Set bounds for type parameters

    // Add supertypes to classes
    expressionEClass.getESuperTypes().add(this.getInputConstraint());
    scalarExpressionEClass.getESuperTypes().add(this.getExpression());
    randomExpressionEClass.getESuperTypes().add(this.getScalarExpression());
    refExpressionEClass.getESuperTypes().add(this.getScalarExpression());
    intervalExpressionEClass.getESuperTypes().add(this.getExpression());
    setExpressionEClass.getESuperTypes().add(this.getExpression());
    integerLiteralEClass.getESuperTypes().add(this.getScalarExpression());
    realLiteralEClass.getESuperTypes().add(this.getScalarExpression());
    booleanLiteralEClass.getESuperTypes().add(this.getScalarExpression());
    binaryExpressionEClass.getESuperTypes().add(this.getScalarExpression());
    preExpressionEClass.getESuperTypes().add(this.getScalarExpression());
    randomIntegerExpressionEClass.getESuperTypes().add(this.getRandomExpression());
    randomRealExpressionEClass.getESuperTypes().add(this.getRandomExpression());
    randomElementExpressionEClass.getESuperTypes().add(this.getRandomExpression());
    elementRefExpressionEClass.getESuperTypes().add(this.getRefExpression());
    constRefExpressionEClass.getESuperTypes().add(this.getRefExpression());
    negativeExpressionEClass.getESuperTypes().add(this.getScalarExpression());

    // Initialize classes and features; add operations and parameters
    initEClass(inputConstraintEClass, InputConstraint.class, "InputConstraint", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(expressionEClass, Expression.class, "Expression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(scalarExpressionEClass, ScalarExpression.class, "ScalarExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(randomExpressionEClass, RandomExpression.class, "RandomExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(refExpressionEClass, RefExpression.class, "RefExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(intervalExpressionEClass, IntervalExpression.class, "IntervalExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getIntervalExpression_LeftClosed(), ecorePackage.getEBoolean(), "leftClosed", null, 0, 1, IntervalExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIntervalExpression_Left(), this.getScalarExpression(), null, "left", null, 0, 1, IntervalExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIntervalExpression_Right(), this.getScalarExpression(), null, "right", null, 0, 1, IntervalExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getIntervalExpression_RightClosed(), ecorePackage.getEBoolean(), "rightClosed", null, 0, 1, IntervalExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(setExpressionEClass, SetExpression.class, "SetExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getSetExpression_Members(), this.getScalarExpression(), null, "members", null, 0, -1, SetExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(integerLiteralEClass, IntegerLiteral.class, "IntegerLiteral", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getIntegerLiteral_Value(), ecorePackage.getEBigInteger(), "value", null, 0, 1, IntegerLiteral.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(realLiteralEClass, RealLiteral.class, "RealLiteral", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getRealLiteral_Value(), ecorePackage.getEBigDecimal(), "value", null, 0, 1, RealLiteral.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(booleanLiteralEClass, BooleanLiteral.class, "BooleanLiteral", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getBooleanLiteral_Value(), ecorePackage.getEBoolean(), "value", null, 0, 1, BooleanLiteral.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(binaryExpressionEClass, BinaryExpression.class, "BinaryExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getBinaryExpression_Left(), this.getScalarExpression(), null, "left", null, 0, 1, BinaryExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getBinaryExpression_Op(), this.getOperator(), "op", null, 0, 1, BinaryExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getBinaryExpression_Right(), this.getScalarExpression(), null, "right", null, 0, 1, BinaryExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(preExpressionEClass, PreExpression.class, "PreExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getPreExpression_Ref(), this.getRefExpression(), null, "ref", null, 0, 1, PreExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(randomIntegerExpressionEClass, RandomIntegerExpression.class, "RandomIntegerExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getRandomIntegerExpression_Interval(), this.getIntervalExpression(), null, "interval", null, 0, 1, RandomIntegerExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(randomRealExpressionEClass, RandomRealExpression.class, "RandomRealExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getRandomRealExpression_Interval(), this.getIntervalExpression(), null, "interval", null, 0, 1, RandomRealExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(randomElementExpressionEClass, RandomElementExpression.class, "RandomElementExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getRandomElementExpression_Set(), this.getSetExpression(), null, "set", null, 0, 1, RandomElementExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(elementRefExpressionEClass, ElementRefExpression.class, "ElementRefExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getElementRefExpression_Ids(), ecorePackage.getEString(), "ids", null, 0, -1, ElementRefExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(constRefExpressionEClass, ConstRefExpression.class, "ConstRefExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getConstRefExpression_PackageSegments(), ecorePackage.getEString(), "packageSegments", null, 0, -1, ConstRefExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getConstRefExpression_ConstantName(), ecorePackage.getEString(), "constantName", null, 0, 1, ConstRefExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(negativeExpressionEClass, NegativeExpression.class, "NegativeExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getNegativeExpression_Value(), this.getScalarExpression(), null, "value", null, 0, 1, NegativeExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    // Initialize enums and add enum literals
    initEEnum(operatorEEnum, Operator.class, "Operator");
    addEEnumLiteral(operatorEEnum, Operator.ADD_SUB_OPERATOR);
    addEEnumLiteral(operatorEEnum, Operator.MULTI_DIV_OPERATOR);
    addEEnumLiteral(operatorEEnum, Operator.ADDITION);
    addEEnumLiteral(operatorEEnum, Operator.SUBTRACTION);
    addEEnumLiteral(operatorEEnum, Operator.MULTIPLICATION);
    addEEnumLiteral(operatorEEnum, Operator.DIVISION);

    // Create resource
    createResource(eNS_URI);
  }

} //InputConstraintPackageImpl
