Generate a deterministic JUnit 4 Java suite using only the supplied fixed source, build context, shared target declarations and fixture policy. Return complete Java code fences with explicit package declarations, public test classes and imports. Use at most 30 @Test methods. Use meaningful assertions derived from fixed API behavior; avoid non-null-only or empty tests, randomness, time dependence and external services. Do not modify or shadow production code. No assertion repairs or feedback loop. Suites exceeding 30 methods are rejected entirely.

Project: Math; fixed revision: 1f.
Modified target classes:
org.apache.commons.math3.fraction.BigFraction
org.apache.commons.math3.fraction.Fraction

Fixture policy: common production types in fixed/buggy; simplest supported constructor selected by the shared probe. Use only the listed shared signatures and common fixture types.

Shared target declarations:
```json
[
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "",
    "method": "getReducedFraction",
    "parameter_types": "int,int"
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "<init>",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "abs",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "add",
    "parameter_types": "int"
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "add",
    "parameter_types": "java.math.BigInteger"
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "add",
    "parameter_types": "long"
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "add",
    "parameter_types": "org.apache.commons.math3.fraction.BigFraction"
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "bigDecimalValue",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "bigDecimalValue",
    "parameter_types": "int"
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "bigDecimalValue",
    "parameter_types": "int,int"
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "compareTo",
    "parameter_types": "org.apache.commons.math3.fraction.BigFraction"
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "divide",
    "parameter_types": "int"
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "divide",
    "parameter_types": "java.math.BigInteger"
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "divide",
    "parameter_types": "long"
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "divide",
    "parameter_types": "org.apache.commons.math3.fraction.BigFraction"
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "doubleValue",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "equals",
    "parameter_types": "java.lang.Object"
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "floatValue",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "getDenominator",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "getDenominatorAsInt",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "getDenominatorAsLong",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "getField",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "getNumerator",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "getNumeratorAsInt",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "getNumeratorAsLong",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "hashCode",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "intValue",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "longValue",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "multiply",
    "parameter_types": "int"
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "multiply",
    "parameter_types": "java.math.BigInteger"
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "multiply",
    "parameter_types": "long"
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "multiply",
    "parameter_types": "org.apache.commons.math3.fraction.BigFraction"
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "negate",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "percentageValue",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "pow",
    "parameter_types": "double"
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "pow",
    "parameter_types": "int"
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "pow",
    "parameter_types": "java.math.BigInteger"
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "pow",
    "parameter_types": "long"
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "reciprocal",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "reduce",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "subtract",
    "parameter_types": "int"
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "subtract",
    "parameter_types": "java.math.BigInteger"
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "subtract",
    "parameter_types": "long"
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "subtract",
    "parameter_types": "org.apache.commons.math3.fraction.BigFraction"
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double",
    "method": "toString",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double,double,int",
    "method": "<init>",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double,double,int,int",
    "method": "<init>",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "double,int",
    "method": "<init>",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "int",
    "method": "<init>",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "int,int",
    "method": "<init>",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "java.math.BigInteger",
    "method": "<init>",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "java.math.BigInteger,java.math.BigInteger",
    "method": "<init>",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "long",
    "method": "<init>",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.BigFraction",
    "constructor_types": "long,long",
    "method": "<init>",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.Fraction",
    "constructor_types": "",
    "method": "getReducedFraction",
    "parameter_types": "int,int"
  },
  {
    "class": "org.apache.commons.math3.fraction.Fraction",
    "constructor_types": "double",
    "method": "<init>",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.Fraction",
    "constructor_types": "double",
    "method": "abs",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.Fraction",
    "constructor_types": "double",
    "method": "add",
    "parameter_types": "int"
  },
  {
    "class": "org.apache.commons.math3.fraction.Fraction",
    "constructor_types": "double",
    "method": "add",
    "parameter_types": "org.apache.commons.math3.fraction.Fraction"
  },
  {
    "class": "org.apache.commons.math3.fraction.Fraction",
    "constructor_types": "double",
    "method": "addSub",
    "parameter_types": "org.apache.commons.math3.fraction.Fraction,boolean"
  },
  {
    "class": "org.apache.commons.math3.fraction.Fraction",
    "constructor_types": "double",
    "method": "compareTo",
    "parameter_types": "org.apache.commons.math3.fraction.Fraction"
  },
  {
    "class": "org.apache.commons.math3.fraction.Fraction",
    "constructor_types": "double",
    "method": "divide",
    "parameter_types": "int"
  },
  {
    "class": "org.apache.commons.math3.fraction.Fraction",
    "constructor_types": "double",
    "method": "divide",
    "parameter_types": "org.apache.commons.math3.fraction.Fraction"
  },
  {
    "class": "org.apache.commons.math3.fraction.Fraction",
    "constructor_types": "double",
    "method": "doubleValue",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.Fraction",
    "constructor_types": "double",
    "method": "equals",
    "parameter_types": "java.lang.Object"
  },
  {
    "class": "org.apache.commons.math3.fraction.Fraction",
    "constructor_types": "double",
    "method": "floatValue",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.Fraction",
    "constructor_types": "double",
    "method": "getDenominator",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.Fraction",
    "constructor_types": "double",
    "method": "getField",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.Fraction",
    "constructor_types": "double",
    "method": "getNumerator",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.Fraction",
    "constructor_types": "double",
    "method": "hashCode",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.Fraction",
    "constructor_types": "double",
    "method": "intValue",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.Fraction",
    "constructor_types": "double",
    "method": "longValue",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.Fraction",
    "constructor_types": "double",
    "method": "multiply",
    "parameter_types": "int"
  },
  {
    "class": "org.apache.commons.math3.fraction.Fraction",
    "constructor_types": "double",
    "method": "multiply",
    "parameter_types": "org.apache.commons.math3.fraction.Fraction"
  },
  {
    "class": "org.apache.commons.math3.fraction.Fraction",
    "constructor_types": "double",
    "method": "negate",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.Fraction",
    "constructor_types": "double",
    "method": "percentageValue",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.Fraction",
    "constructor_types": "double",
    "method": "reciprocal",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.Fraction",
    "constructor_types": "double",
    "method": "subtract",
    "parameter_types": "int"
  },
  {
    "class": "org.apache.commons.math3.fraction.Fraction",
    "constructor_types": "double",
    "method": "subtract",
    "parameter_types": "org.apache.commons.math3.fraction.Fraction"
  },
  {
    "class": "org.apache.commons.math3.fraction.Fraction",
    "constructor_types": "double",
    "method": "toString",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.Fraction",
    "constructor_types": "double,double,int",
    "method": "<init>",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.Fraction",
    "constructor_types": "double,double,int,int",
    "method": "<init>",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.Fraction",
    "constructor_types": "double,int",
    "method": "<init>",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.Fraction",
    "constructor_types": "int",
    "method": "<init>",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.math3.fraction.Fraction",
    "constructor_types": "int,int",
    "method": "<init>",
    "parameter_types": ""
  }
]
```

Common compiled production fixture classes (eligibility only, not oracle approval):
```json
[
  "org.apache.commons.math3.Field",
  "org.apache.commons.math3.FieldElement",
  "org.apache.commons.math3.RealFieldElement",
  "org.apache.commons.math3.analysis.BivariateFunction",
  "org.apache.commons.math3.analysis.DifferentiableMultivariateFunction",
  "org.apache.commons.math3.analysis.DifferentiableMultivariateVectorFunction",
  "org.apache.commons.math3.analysis.DifferentiableUnivariateFunction",
  "org.apache.commons.math3.analysis.DifferentiableUnivariateMatrixFunction",
  "org.apache.commons.math3.analysis.DifferentiableUnivariateVectorFunction",
  "org.apache.commons.math3.analysis.FunctionUtils",
  "org.apache.commons.math3.analysis.MultivariateFunction",
  "org.apache.commons.math3.analysis.MultivariateMatrixFunction",
  "org.apache.commons.math3.analysis.MultivariateVectorFunction",
  "org.apache.commons.math3.analysis.ParametricUnivariateFunction",
  "org.apache.commons.math3.analysis.TrivariateFunction",
  "org.apache.commons.math3.analysis.UnivariateFunction",
  "org.apache.commons.math3.analysis.UnivariateMatrixFunction",
  "org.apache.commons.math3.analysis.UnivariateVectorFunction",
  "org.apache.commons.math3.analysis.differentiation.DSCompiler",
  "org.apache.commons.math3.analysis.differentiation.DerivativeStructure",
  "org.apache.commons.math3.analysis.differentiation.FiniteDifferencesDifferentiator",
  "org.apache.commons.math3.analysis.differentiation.GradientFunction",
  "org.apache.commons.math3.analysis.differentiation.JacobianFunction",
  "org.apache.commons.math3.analysis.differentiation.MultivariateDifferentiableFunction",
  "org.apache.commons.math3.analysis.differentiation.MultivariateDifferentiableVectorFunction",
  "org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction",
  "org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableMatrixFunction",
  "org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableVectorFunction",
  "org.apache.commons.math3.analysis.differentiation.UnivariateFunctionDifferentiator",
  "org.apache.commons.math3.analysis.differentiation.UnivariateMatrixFunctionDifferentiator",
  "org.apache.commons.math3.analysis.differentiation.UnivariateVectorFunctionDifferentiator",
  "org.apache.commons.math3.analysis.function.Abs",
  "org.apache.commons.math3.analysis.function.Acos",
  "org.apache.commons.math3.analysis.function.Acosh",
  "org.apache.commons.math3.analysis.function.Add",
  "org.apache.commons.math3.analysis.function.Asin",
  "org.apache.commons.math3.analysis.function.Asinh",
  "org.apache.commons.math3.analysis.function.Atan",
  "org.apache.commons.math3.analysis.function.Atan2",
  "org.apache.commons.math3.analysis.function.Atanh",
  "org.apache.commons.math3.analysis.function.Cbrt",
  "org.apache.commons.math3.analysis.function.Ceil",
  "org.apache.commons.math3.analysis.function.Constant",
  "org.apache.commons.math3.analysis.function.Cos",
  "org.apache.commons.math3.analysis.function.Cosh",
  "org.apache.commons.math3.analysis.function.Divide",
  "org.apache.commons.math3.analysis.function.Exp",
  "org.apache.commons.math3.analysis.function.Expm1",
  "org.apache.commons.math3.analysis.function.Floor",
  "org.apache.commons.math3.analysis.function.Gaussian",
  "org.apache.commons.math3.analysis.function.HarmonicOscillator",
  "org.apache.commons.math3.analysis.function.Identity",
  "org.apache.commons.math3.analysis.function.Inverse",
  "org.apache.commons.math3.analysis.function.Log",
  "org.apache.commons.math3.analysis.function.Log10",
  "org.apache.commons.math3.analysis.function.Log1p",
  "org.apache.commons.math3.analysis.function.Logistic",
  "org.apache.commons.math3.analysis.function.Logit",
  "org.apache.commons.math3.analysis.function.Max",
  "org.apache.commons.math3.analysis.function.Min",
  "org.apache.commons.math3.analysis.function.Minus",
  "org.apache.commons.math3.analysis.function.Multiply",
  "org.apache.commons.math3.analysis.function.Pow",
  "org.apache.commons.math3.analysis.function.Power",
  "org.apache.commons.math3.analysis.function.Rint",
  "org.apache.commons.math3.analysis.function.Sigmoid",
  "org.apache.commons.math3.analysis.function.Signum",
  "org.apache.commons.math3.analysis.function.Sin",
  "org.apache.commons.math3.analysis.function.Sinc",
  "org.apache.commons.math3.analysis.function.Sinh",
  "org.apache.commons.math3.analysis.function.Sqrt",
  "org.apache.commons.math3.analysis.function.StepFunction",
  "org.apache.commons.math3.analysis.function.Subtract",
  "org.apache.commons.math3.analysis.function.Tan",
  "org.apache.commons.math3.analysis.function.Tanh",
  "org.apache.commons.math3.analysis.function.Ulp",
  "org.apache.commons.math3.analysis.integration.BaseAbstractUnivariateIntegrator",
  "org.apache.commons.math3.analysis.integration.IterativeLegendreGaussIntegrator",
  "org.apache.commons.math3.analysis.integration.LegendreGaussIntegrator",
  "org.apache.commons.math3.analysis.integration.MidPointIntegrator",
  "org.apache.commons.math3.analysis.integration.RombergIntegrator",
  "org.apache.commons.math3.analysis.integration.SimpsonIntegrator",
  "org.apache.commons.math3.analysis.integration.TrapezoidIntegrator",
  "org.apache.commons.math3.analysis.integration.UnivariateIntegrator",
  "org.apache.commons.math3.analysis.integration.gauss.BaseRuleFactory",
  "org.apache.commons.math3.analysis.integration.gauss.GaussIntegrator",
  "org.apache.commons.math3.analysis.integration.gauss.GaussIntegratorFactory",
  "org.apache.commons.math3.analysis.integration.gauss.HermiteRuleFactory",
  "org.apache.commons.math3.analysis.integration.gauss.LegendreHighPrecisionRuleFactory",
  "org.apache.commons.math3.analysis.integration.gauss.LegendreRuleFactory",
  "org.apache.commons.math3.analysis.integration.gauss.SymmetricGaussIntegrator",
  "org.apache.commons.math3.analysis.interpolation.BicubicSplineFunction",
  "org.apache.commons.math3.analysis.interpolation.BicubicSplineInterpolatingFunction",
  "org.apache.commons.math3.analysis.interpolation.BicubicSplineInterpolator",
  "org.apache.commons.math3.analysis.interpolation.BivariateGridInterpolator",
  "org.apache.commons.math3.analysis.interpolation.DividedDifferenceInterpolator",
  "org.apache.commons.math3.analysis.interpolation.FieldHermiteInterpolator",
  "org.apache.commons.math3.analysis.interpolation.HermiteInterpolator",
  "org.apache.commons.math3.analysis.interpolation.LinearInterpolator",
  "org.apache.commons.math3.analysis.interpolation.LoessInterpolator",
  "org.apache.commons.math3.analysis.interpolation.MicrosphereInterpolatingFunction",
  "org.apache.commons.math3.analysis.interpolation.MicrosphereInterpolator",
  "org.apache.commons.math3.analysis.interpolation.MultivariateInterpolator",
  "org.apache.commons.math3.analysis.interpolation.NevilleInterpolator",
  "org.apache.commons.math3.analysis.interpolation.SmoothingPolynomialBicubicSplineInterpolator",
  "org.apache.commons.math3.analysis.interpolation.SplineInterpolator",
  "org.apache.commons.math3.analysis.interpolation.TricubicSplineFunction",
  "org.apache.commons.math3.analysis.interpolation.TricubicSplineInterpolatingFunction",
  "org.apache.commons.math3.analysis.interpolation.TricubicSplineInterpolator",
  "org.apache.commons.math3.analysis.interpolation.TrivariateGridInterpolator",
  "org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator",
  "org.apache.commons.math3.analysis.interpolation.UnivariatePeriodicInterpolator",
  "org.apache.commons.math3.analysis.polynomials.PolynomialFunction",
  "org.apache.commons.math3.analysis.polynomials.PolynomialFunctionLagrangeForm",
  "org.apache.commons.math3.analysis.polynomials.PolynomialFunctionNewtonForm",
  "org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction",
  "org.apache.commons.math3.analysis.polynomials.PolynomialsUtils",
  "org.apache.commons.math3.analysis.solvers.AbstractDifferentiableUnivariateSolver",
  "org.apache.commons.math3.analysis.solvers.AbstractPolynomialSolver",
  "org.apache.commons.math3.analysis.solvers.AbstractUnivariateDifferentiableSolver",
  "org.apache.commons.math3.analysis.solvers.AbstractUnivariateSolver",
  "org.apache.commons.math3.analysis.solvers.AllowedSolution",
  "org.apache.commons.math3.analysis.solvers.BaseAbstractUnivariateSolver",
  "org.apache.commons.math3.analysis.solvers.BaseSecantSolver",
  "org.apache.commons.math3.analysis.solvers.BaseUnivariateSolver",
  "org.apache.commons.math3.analysis.solvers.BisectionSolver",
  "org.apache.commons.math3.analysis.solvers.BracketedUnivariateSolver",
  "org.apache.commons.math3.analysis.solvers.BracketingNthOrderBrentSolver",
  "org.apache.commons.math3.analysis.solvers.BrentSolver",
  "org.apache.commons.math3.analysis.solvers.DifferentiableUnivariateSolver",
  "org.apache.commons.math3.analysis.solvers.IllinoisSolver",
  "org.apache.commons.math3.analysis.solvers.LaguerreSolver",
  "org.apache.commons.math3.analysis.solvers.MullerSolver",
  "org.apache.commons.math3.analysis.solvers.MullerSolver2",
  "org.apache.commons.math3.analysis.solvers.NewtonRaphsonSolver",
  "org.apache.commons.math3.analysis.solvers.NewtonSolver",
  "org.apache.commons.math3.analysis.solvers.PegasusSolver",
  "org.apache.commons.math3.analysis.solvers.PolynomialSolver",
  "org.apache.commons.math3.analysis.solvers.RegulaFalsiSolver",
  "org.apache.commons.math3.analysis.solvers.RiddersSolver",
  "org.apache.commons.math3.analysis.solvers.SecantSolver",
  "org.apache.commons.math3.analysis.solvers.UnivariateDifferentiableSolver",
  "org.apache.commons.math3.analysis.solvers.UnivariateSolver",
  "org.apache.commons.math3.analysis.solvers.UnivariateSolverUtils",
  "org.apache.commons.math3.complex.Complex",
  "org.apache.commons.math3.complex.ComplexField",
  "org.apache.commons.math3.complex.ComplexFormat",
  "org.apache.commons.math3.complex.ComplexUtils",
  "org.apache.commons.math3.complex.Quaternion",
  "org.apache.commons.math3.complex.RootsOfUnity",
  "org.apache.commons.math3.dfp.BracketingNthOrderBrentSolverDFP",
  "org.apache.commons.math3.dfp.Dfp",
  "org.apache.commons.math3.dfp.DfpDec",
  "org.apache.commons.math3.dfp.DfpField",
  "org.apache.commons.math3.dfp.DfpMath",
  "org.apache.commons.math3.dfp.UnivariateDfpFunction",
  "org.apache.commons.math3.distribution.AbstractIntegerDistribution",
  "org.apache.commons.math3.distribution.AbstractMultivariateRealDistribution",
  "org.apache.commons.math3.distribution.AbstractRealDistribution",
  "org.apache.commons.math3.distribution.BetaDistribution",
  "org.apache.commons.math3.distribution.BinomialDistribution",
  "org.apache.commons.math3.distribution.CauchyDistribution",
  "org.apache.commons.math3.distribution.ChiSquaredDistribution",
  "org.apache.commons.math3.distribution.EnumeratedDistribution",
  "org.apache.commons.math3.distribution.EnumeratedIntegerDistribution",
  "org.apache.commons.math3.distribution.EnumeratedRealDistribution",
  "org.apache.commons.math3.distribution.ExponentialDistribution",
  "org.apache.commons.math3.distribution.FDistribution",
  "org.apache.commons.math3.distribution.GammaDistribution",
  "org.apache.commons.math3.distribution.GeometricDistribution",
  "org.apache.commons.math3.distribution.HypergeometricDistribution",
  "org.apache.commons.math3.distribution.IntegerDistribution",
  "org.apache.commons.math3.distribution.KolmogorovSmirnovDistribution",
  "org.apache.commons.math3.distribution.LevyDistribution",
  "org.apache.commons.math3.distribution.LogNormalDistribution",
  "org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution",
  "org.apache.commons.math3.distribution.MixtureMultivariateRealDistribution",
  "org.apache.commons.math3.distribution.MultivariateNormalDistribution",
  "org.apache.commons.math3.distribution.MultivariateRealDistribution",
  "org.apache.commons.math3.distribution.NormalDistribution",
  "org.apache.commons.math3.distribution.ParetoDistribution",
  "org.apache.commons.math3.distribution.PascalDistribution",
  "org.apache.commons.math3.distribution.PoissonDistribution",
  "org.apache.commons.math3.distribution.RealDistribution",
  "org.apache.commons.math3.distribution.SaddlePointExpansion",
  "org.apache.commons.math3.distribution.TDistribution",
  "org.apache.commons.math3.distribution.TriangularDistribution",
  "org.apache.commons.math3.distribution.UniformIntegerDistribution",
  "org.apache.commons.math3.distribution.UniformRealDistribution",
  "org.apache.commons.math3.distribution.WeibullDistribution",
  "org.apache.commons.math3.distribution.ZipfDistribution",
  "org.apache.commons.math3.distribution.fitting.MultivariateNormalMixtureExpectationMaximization",
  "org.apache.commons.math3.exception.ConvergenceException",
  "org.apache.commons.math3.exception.DimensionMismatchException",
  "org.apache.commons.math3.exception.MathArithmeticException",
  "org.apache.commons.math3.exception.MathIllegalArgumentException",
  "org.apache.commons.math3.exception.MathIllegalNumberException",
  "org.apache.commons.math3.exception.MathIllegalStateException",
  "org.apache.commons.math3.exception.MathInternalError",
  "org.apache.commons.math3.exception.MathParseException",
  "org.apache.commons.math3.exception.MathRuntimeException",
  "org.apache.commons.math3.exception.MathUnsupportedOperationException",
  "org.apache.commons.math3.exception.MaxCountExceededException",
  "org.apache.commons.math3.exception.MultiDimensionMismatchException",
  "org.apache.commons.math3.exception.NoBracketingException",
  "org.apache.commons.math3.exception.NoDataException",
  "org.apache.commons.math3.exception.NonMonotonicSequenceException",
  "org.apache.commons.math3.exception.NotANumberException",
  "org.apache.commons.math3.exception.NotFiniteNumberException",
  "org.apache.commons.math3.exception.NotPositiveException",
  "org.apache.commons.math3.exception.NotStrictlyPositiveException",
  "org.apache.commons.math3.exception.NullArgumentException",
  "org.apache.commons.math3.exception.NumberIsTooLargeException",
  "org.apache.commons.math3.exception.NumberIsTooSmallException",
  "org.apache.commons.math3.exception.OutOfRangeException",
  "org.apache.commons.math3.exception.TooManyEvaluationsException",
  "org.apache.commons.math3.exception.TooManyIterationsException",
  "org.apache.commons.math3.exception.ZeroException",
  "org.apache.commons.math3.exception.util.ArgUtils",
  "org.apache.commons.math3.exception.util.DummyLocalizable",
  "org.apache.commons.math3.exception.util.ExceptionContext",
  "org.apache.commons.math3.exception.util.ExceptionContextProvider",
  "org.apache.commons.math3.exception.util.Localizable",
  "org.apache.commons.math3.exception.util.LocalizedFormats",
  "org.apache.commons.math3.filter.DefaultMeasurementModel",
  "org.apache.commons.math3.filter.DefaultProcessModel",
  "org.apache.commons.math3.filter.KalmanFilter",
  "org.apache.commons.math3.filter.MeasurementModel",
  "org.apache.commons.math3.filter.ProcessModel",
  "org.apache.commons.math3.fitting.AbstractCurveFitter",
  "org.apache.commons.math3.fitting.CurveFitter",
  "org.apache.commons.math3.fitting.GaussianCurveFitter",
  "org.apache.commons.math3.fitting.GaussianFitter",
  "org.apache.commons.math3.fitting.HarmonicFitter",
  "org.apache.commons.math3.fitting.PolynomialFitter",
  "org.apache.commons.math3.fitting.WeightedObservedPoint",
  "org.apache.commons.math3.fitting.WeightedObservedPoints",
  "org.apache.commons.math3.fitting.leastsquares.AbstractLeastSquaresOptimizer",
  "org.apache.commons.math3.fitting.leastsquares.GaussNewtonOptimizer",
  "org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer",
  "org.apache.commons.math3.fitting.leastsquares.WithConvergenceChecker",
  "org.apache.commons.math3.fitting.leastsquares.WithMaxEvaluations",
  "org.apache.commons.math3.fitting.leastsquares.WithMaxIterations",
  "org.apache.commons.math3.fitting.leastsquares.WithModelAndJacobian",
  "org.apache.commons.math3.fitting.leastsquares.WithStartPoint",
  "org.apache.commons.math3.fitting.leastsquares.WithTarget",
  "org.apache.commons.math3.fitting.leastsquares.WithWeight",
  "org.apache.commons.math3.fraction.AbstractFormat",
  "org.apache.commons.math3.fraction.BigFraction",
  "org.apache.commons.math3.fraction.BigFractionField",
  "org.apache.commons.math3.fraction.BigFractionFormat",
  "org.apache.commons.math3.fraction.Fraction",
  "org.apache.commons.math3.fraction.FractionConversionException",
  "org.apache.commons.math3.fraction.FractionField",
  "org.apache.commons.math3.fraction.FractionFormat",
  "org.apache.commons.math3.fraction.ProperBigFractionFormat",
  "org.apache.commons.math3.fraction.ProperFractionFormat",
  "org.apache.commons.math3.genetics.AbstractListChromosome",
  "org.apache.commons.math3.genetics.BinaryChromosome",
  "org.apache.commons.math3.genetics.BinaryMutation",
  "org.apache.commons.math3.genetics.Chromosome",
  "org.apache.commons.math3.genetics.ChromosomePair",
  "org.apache.commons.math3.genetics.CrossoverPolicy",
  "org.apache.commons.math3.genetics.CycleCrossover",
  "org.apache.commons.math3.genetics.ElitisticListPopulation",
  "org.apache.commons.math3.genetics.Fitness",
  "org.apache.commons.math3.genetics.FixedElapsedTime",
  "org.apache.commons.math3.genetics.FixedGenerationCount",
  "org.apache.commons.math3.genetics.GeneticAlgorithm",
  "org.apache.commons.math3.genetics.InvalidRepresentationException",
  "org.apache.commons.math3.genetics.ListPopulation",
  "org.apache.commons.math3.genetics.MutationPolicy",
  "org.apache.commons.math3.genetics.NPointCrossover",
  "org.apache.commons.math3.genetics.OnePointCrossover",
  "org.apache.commons.math3.genetics.OrderedCrossover",
  "org.apache.commons.math3.genetics.PermutationChromosome",
  "org.apache.commons.math3.genetics.Population",
  "org.apache.commons.math3.genetics.RandomKey",
  "org.apache.commons.math3.genetics.RandomKeyMutation",
  "org.apache.commons.math3.genetics.SelectionPolicy",
  "org.apache.commons.math3.genetics.StoppingCondition",
  "org.apache.commons.math3.genetics.TournamentSelection",
  "org.apache.commons.math3.genetics.UniformCrossover",
  "org.apache.commons.math3.geometry.Space",
  "org.apache.commons.math3.geometry.Vector",
  "org.apache.commons.math3.geometry.VectorFormat",
  "org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D",
  "org.apache.commons.math3.geometry.euclidean.oned.Interval",
  "org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet",
  "org.apache.commons.math3.geometry.euclidean.oned.OrientedPoint",
  "org.apache.commons.math3.geometry.euclidean.oned.SubOrientedPoint",
  "org.apache.commons.math3.geometry.euclidean.oned.Vector1D",
  "org.apache.commons.math3.geometry.euclidean.oned.Vector1DFormat",
  "org.apache.commons.math3.geometry.euclidean.threed.CardanEulerSingularityException",
  "org.apache.commons.math3.geometry.euclidean.threed.Euclidean3D",
  "org.apache.commons.math3.geometry.euclidean.threed.FieldRotation",
  "org.apache.commons.math3.geometry.euclidean.threed.FieldVector3D",
  "org.apache.commons.math3.geometry.euclidean.threed.Line",
  "org.apache.commons.math3.geometry.euclidean.threed.NotARotationMatrixException",
  "org.apache.commons.math3.geometry.euclidean.threed.OutlineExtractor",
  "org.apache.commons.math3.geometry.euclidean.threed.Plane",
  "org.apache.commons.math3.geometry.euclidean.threed.PolyhedronsSet",
  "org.apache.commons.math3.geometry.euclidean.threed.Rotation",
  "org.apache.commons.math3.geometry.euclidean.threed.RotationOrder",
  "org.apache.commons.math3.geometry.euclidean.threed.Segment",
  "org.apache.commons.math3.geometry.euclidean.threed.SphericalCoordinates",
  "org.apache.commons.math3.geometry.euclidean.threed.SubLine",
  "org.apache.commons.math3.geometry.euclidean.threed.SubPlane",
  "org.apache.commons.math3.geometry.euclidean.threed.Vector3D",
  "org.apache.commons.math3.geometry.euclidean.threed.Vector3DFormat",
  "org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D",
  "org.apache.commons.math3.geometry.euclidean.twod.Line",
  "org.apache.commons.math3.geometry.euclidean.twod.NestedLoops",
  "org.apache.commons.math3.geometry.euclidean.twod.PolygonsSet",
  "org.apache.commons.math3.geometry.euclidean.twod.Segment",
  "org.apache.commons.math3.geometry.euclidean.twod.SubLine",
  "org.apache.commons.math3.geometry.euclidean.twod.Vector2D",
  "org.apache.commons.math3.geometry.euclidean.twod.Vector2DFormat",
  "org.apache.commons.math3.geometry.partitioning.AbstractRegion",
  "org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane",
  "org.apache.commons.math3.geometry.partitioning.BSPTree",
  "org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor",
  "org.apache.commons.math3.geometry.partitioning.BoundaryAttribute",
  "org.apache.commons.math3.geometry.partitioning.BoundarySizeVisitor",
  "org.apache.commons.math3.geometry.partitioning.Embedding",
  "org.apache.commons.math3.geometry.partitioning.Hyperplane",
  "org.apache.commons.math3.geometry.partitioning.Region",
  "org.apache.commons.math3.geometry.partitioning.RegionFactory",
  "org.apache.commons.math3.geometry.partitioning.Side",
  "org.apache.commons.math3.geometry.partitioning.SubHyperplane",
  "org.apache.commons.math3.geometry.partitioning.Transform",
  "org.apache.commons.math3.geometry.partitioning.utilities.AVLTree",
  "org.apache.commons.math3.geometry.partitioning.utilities.OrderedTuple",
  "org.apache.commons.math3.linear.AbstractFieldMatrix",
  "org.apache.commons.math3.linear.AbstractRealMatrix",
  "org.apache.commons.math3.linear.AnyMatrix",
  "org.apache.commons.math3.linear.Array2DRowFieldMatrix",
  "org.apache.commons.math3.linear.Array2DRowRealMatrix",
  "org.apache.commons.math3.linear.ArrayFieldVector",
  "org.apache.commons.math3.linear.ArrayRealVector",
  "org.apache.commons.math3.linear.BiDiagonalTransformer",
  "org.apache.commons.math3.linear.BlockFieldMatrix",
  "org.apache.commons.math3.linear.BlockRealMatrix",
  "org.apache.commons.math3.linear.CholeskyDecomposition",
  "org.apache.commons.math3.linear.ConjugateGradient",
  "org.apache.commons.math3.linear.DecompositionSolver",
  "org.apache.commons.math3.linear.DefaultFieldMatrixChangingVisitor",
  "org.apache.commons.math3.linear.DefaultFieldMatrixPreservingVisitor",
  "org.apache.commons.math3.linear.DefaultIterativeLinearSolverEvent",
  "org.apache.commons.math3.linear.DefaultRealMatrixChangingVisitor",
  "org.apache.commons.math3.linear.DefaultRealMatrixPreservingVisitor",
  "org.apache.commons.math3.linear.DiagonalMatrix",
  "org.apache.commons.math3.linear.EigenDecomposition",
  "org.apache.commons.math3.linear.FieldDecompositionSolver",
  "org.apache.commons.math3.linear.FieldLUDecomposition",
  "org.apache.commons.math3.linear.FieldMatrix",
  "org.apache.commons.math3.linear.FieldMatrixChangingVisitor",
  "org.apache.commons.math3.linear.FieldMatrixPreservingVisitor",
  "org.apache.commons.math3.linear.FieldVector",
  "org.apache.commons.math3.linear.HessenbergTransformer",
  "org.apache.commons.math3.linear.IllConditionedOperatorException",
  "org.apache.commons.math3.linear.IterativeLinearSolver",
  "org.apache.commons.math3.linear.IterativeLinearSolverEvent",
  "org.apache.commons.math3.linear.JacobiPreconditioner",
  "org.apache.commons.math3.linear.LUDecomposition",
  "org.apache.commons.math3.linear.MatrixDimensionMismatchException",
  "org.apache.commons.math3.linear.MatrixUtils",
  "org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException",
  "org.apache.commons.math3.linear.NonPositiveDefiniteOperatorException",
  "org.apache.commons.math3.linear.NonSelfAdjointOperatorException",
  "org.apache.commons.math3.linear.NonSquareMatrixException",
  "org.apache.commons.math3.linear.NonSquareOperatorException",
  "org.apache.commons.math3.linear.NonSymmetricMatrixException",
  "org.apache.commons.math3.linear.OpenMapRealMatrix",
  "org.apache.commons.math3.linear.OpenMapRealVector",
  "org.apache.commons.math3.linear.PreconditionedIterativeLinearSolver",
  "org.apache.commons.math3.linear.QRDecomposition",
  "org.apache.commons.math3.linear.RRQRDecomposition",
  "org.apache.commons.math3.linear.RealLinearOperator",
  "org.apache.commons.math3.linear.RealMatrix",
  "org.apache.commons.math3.linear.RealMatrixChangingVisitor",
  "org.apache.commons.math3.linear.RealMatrixFormat",
  "org.apache.commons.math3.linear.RealMatrixPreservingVisitor",
  "org.apache.commons.math3.linear.RealVector",
  "org.apache.commons.math3.linear.RealVectorChangingVisitor",
  "org.apache.commons.math3.linear.RealVectorFormat",
  "org.apache.commons.math3.linear.RealVectorPreservingVisitor",
  "org.apache.commons.math3.linear.RectangularCholeskyDecomposition",
  "org.apache.commons.math3.linear.SchurTransformer",
  "org.apache.commons.math3.linear.SingularMatrixException",
  "org.apache.commons.math3.linear.SingularOperatorException",
  "org.apache.commons.math3.linear.SingularValueDecomposition",
  "org.apache.commons.math3.linear.SparseFieldMatrix",
  "org.apache.commons.math3.linear.SparseFieldVector",
  "org.apache.commons.math3.linear.SparseRealMatrix",
  "org.apache.commons.math3.linear.SparseRealVector",
  "org.apache.commons.math3.linear.SymmLQ",
  "org.apache.commons.math3.linear.TriDiagonalTransformer",
  "org.apache.commons.math3.ml.clustering.CentroidCluster",
  "org.apache.commons.math3.ml.clustering.Cluster",
  "org.apache.commons.math3.ml.clustering.Clusterable",
  "org.apache.commons.math3.ml.clustering.Clusterer",
  "org.apache.commons.math3.ml.clustering.DBSCANClusterer",
  "org.apache.commons.math3.ml.clustering.DoublePoint",
  "org.apache.commons.math3.ml.clustering.FuzzyKMeansClusterer",
  "org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer",
  "org.apache.commons.math3.ml.clustering.MultiKMeansPlusPlusClusterer",
  "org.apache.commons.math3.ml.distance.CanberraDistance",
  "org.apache.commons.math3.ml.distance.ChebyshevDistance",
  "org.apache.commons.math3.ml.distance.DistanceMeasure",
  "org.apache.commons.math3.ml.distance.EarthMoversDistance",
  "org.apache.commons.math3.ml.distance.EuclideanDistance",
  "org.apache.commons.math3.ml.distance.ManhattanDistance",
  "org.apache.commons.math3.ode.AbstractIntegrator",
  "org.apache.commons.math3.ode.AbstractParameterizable",
  "org.apache.commons.math3.ode.ContinuousOutputModel",
  "org.apache.commons.math3.ode.EquationsMapper",
  "org.apache.commons.math3.ode.ExpandableStatefulODE",
  "org.apache.commons.math3.ode.FirstOrderConverter",
  "org.apache.commons.math3.ode.FirstOrderDifferentialEquations",
  "org.apache.commons.math3.ode.FirstOrderIntegrator",
  "org.apache.commons.math3.ode.JacobianMatrices",
  "org.apache.commons.math3.ode.MainStateJacobianProvider",
  "org.apache.commons.math3.ode.MultistepIntegrator",
  "org.apache.commons.math3.ode.ODEIntegrator",
  "org.apache.commons.math3.ode.ParameterConfiguration",
  "org.apache.commons.math3.ode.ParameterJacobianProvider",
  "org.apache.commons.math3.ode.ParameterJacobianWrapper",
  "org.apache.commons.math3.ode.Parameterizable",
  "org.apache.commons.math3.ode.ParameterizedODE",
  "org.apache.commons.math3.ode.ParameterizedWrapper",
  "org.apache.commons.math3.ode.SecondOrderDifferentialEquations",
  "org.apache.commons.math3.ode.SecondOrderIntegrator",
  "org.apache.commons.math3.ode.SecondaryEquations",
  "org.apache.commons.math3.ode.UnknownParameterException",
  "org.apache.commons.math3.ode.events.EventFilter",
  "org.apache.commons.math3.ode.events.EventHandler",
  "org.apache.commons.math3.ode.events.EventState",
  "org.apache.commons.math3.ode.events.FilterType",
  "org.apache.commons.math3.ode.events.Transformer",
  "org.apache.commons.math3.ode.nonstiff.AdamsBashforthIntegrator",
  "org.apache.commons.math3.ode.nonstiff.AdamsIntegrator",
  "org.apache.commons.math3.ode.nonstiff.AdamsMoultonIntegrator",
  "org.apache.commons.math3.ode.nonstiff.AdamsNordsieckTransformer",
  "org.apache.commons.math3.ode.nonstiff.AdaptiveStepsizeIntegrator",
  "org.apache.commons.math3.ode.nonstiff.ClassicalRungeKuttaIntegrator",
  "org.apache.commons.math3.ode.nonstiff.ClassicalRungeKuttaStepInterpolator",
  "org.apache.commons.math3.ode.nonstiff.DormandPrince54Integrator",
  "org.apache.commons.math3.ode.nonstiff.DormandPrince54StepInterpolator",
  "org.apache.commons.math3.ode.nonstiff.DormandPrince853Integrator",
  "org.apache.commons.math3.ode.nonstiff.DormandPrince853StepInterpolator",
  "org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator",
  "org.apache.commons.math3.ode.nonstiff.EulerIntegrator",
  "org.apache.commons.math3.ode.nonstiff.EulerStepInterpolator",
  "org.apache.commons.math3.ode.nonstiff.GillIntegrator",
  "org.apache.commons.math3.ode.nonstiff.GillStepInterpolator",
  "org.apache.commons.math3.ode.nonstiff.GraggBulirschStoerIntegrator",
  "org.apache.commons.math3.ode.nonstiff.GraggBulirschStoerStepInterpolator",
  "org.apache.commons.math3.ode.nonstiff.HighamHall54Integrator",
  "org.apache.commons.math3.ode.nonstiff.HighamHall54StepInterpolator",
  "org.apache.commons.math3.ode.nonstiff.MidpointIntegrator",
  "org.apache.commons.math3.ode.nonstiff.MidpointStepInterpolator",
  "org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator",
  "org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator",
  "org.apache.commons.math3.ode.nonstiff.ThreeEighthesIntegrator",
  "org.apache.commons.math3.ode.nonstiff.ThreeEighthesStepInterpolator",
  "org.apache.commons.math3.ode.sampling.AbstractStepInterpolator",
  "org.apache.commons.math3.ode.sampling.DummyStepHandler",
  "org.apache.commons.math3.ode.sampling.FixedStepHandler",
  "org.apache.commons.math3.ode.sampling.NordsieckStepInterpolator",
  "org.apache.commons.math3.ode.sampling.StepHandler",
  "org.apache.commons.math3.ode.sampling.StepInterpolator",
  "org.apache.commons.math3.ode.sampling.StepNormalizer",
  "org.apache.commons.math3.ode.sampling.StepNormalizerBounds",
  "org.apache.commons.math3.ode.sampling.StepNormalizerMode",
  "org.apache.commons.math3.optim.AbstractConvergenceChecker",
  "org.apache.commons.math3.optim.AbstractOptimizer",
  "org.apache.commons.math3.optim.BaseMultiStartMultivariateOptimizer",
  "org.apache.commons.math3.optim.BaseMultivariateOptimizer",
  "org.apache.commons.math3.optim.BaseOptimizer",
  "org.apache.commons.math3.optim.ConvergenceChecker",
  "org.apache.commons.math3.optim.InitialGuess",
  "org.apache.commons.math3.optim.MaxEval",
  "org.apache.commons.math3.optim.MaxIter",
  "org.apache.commons.math3.optim.OptimizationData",
  "org.apache.commons.math3.optim.PointValuePair",
  "org.apache.commons.math3.optim.PointVectorValuePair",
  "org.apache.commons.math3.optim.SimpleBounds",
  "org.apache.commons.math3.optim.SimplePointChecker",
  "org.apache.commons.math3.optim.SimpleValueChecker",
  "org.apache.commons.math3.optim.SimpleVectorValueChecker",
  "org.apache.commons.math3.optim.linear.LinearConstraint",
  "org.apache.commons.math3.optim.linear.LinearConstraintSet",
  "org.apache.commons.math3.optim.linear.LinearObjectiveFunction",
  "org.apache.commons.math3.optim.linear.LinearOptimizer",
  "org.apache.commons.math3.optim.linear.NoFeasibleSolutionException",
  "org.apache.commons.math3.optim.linear.NonNegativeConstraint",
  "org.apache.commons.math3.optim.linear.Relationship",
  "org.apache.commons.math3.optim.linear.SimplexSolver",
  "org.apache.commons.math3.optim.linear.SimplexTableau",
  "org.apache.commons.math3.optim.linear.UnboundedSolutionException",
  "org.apache.commons.math3.optim.nonlinear.scalar.GoalType",
  "org.apache.commons.math3.optim.nonlinear.scalar.GradientMultivariateOptimizer",
  "org.apache.commons.math3.optim.nonlinear.scalar.LeastSquaresConverter",
  "org.apache.commons.math3.optim.nonlinear.scalar.MultiStartMultivariateOptimizer",
  "org.apache.commons.math3.optim.nonlinear.scalar.MultivariateFunctionMappingAdapter",
  "org.apache.commons.math3.optim.nonlinear.scalar.MultivariateFunctionPenaltyAdapter",
  "org.apache.commons.math3.optim.nonlinear.scalar.MultivariateOptimizer",
  "org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction",
  "org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunctionGradient",
  "org.apache.commons.math3.optim.nonlinear.scalar.gradient.NonLinearConjugateGradientOptimizer",
  "org.apache.commons.math3.optim.nonlinear.scalar.gradient.Preconditioner",
  "org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex",
  "org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BOBYQAOptimizer",
  "org.apache.commons.math3.optim.nonlinear.scalar.noderiv.CMAESOptimizer",
  "org.apache.commons.math3.optim.nonlinear.scalar.noderiv.MultiDirectionalSimplex",
  "org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NelderMeadSimplex",
  "org.apache.commons.math3.optim.nonlinear.scalar.noderiv.PowellOptimizer",
  "org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexOptimizer",
  "org.apache.commons.math3.optim.nonlinear.vector.JacobianMultivariateVectorOptimizer",
  "org.apache.commons.math3.optim.nonlinear.vector.ModelFunction",
  "org.apache.commons.math3.optim.nonlinear.vector.ModelFunctionJacobian",
  "org.apache.commons.math3.optim.nonlinear.vector.MultiStartMultivariateVectorOptimizer",
  "org.apache.commons.math3.optim.nonlinear.vector.MultivariateVectorOptimizer",
  "org.apache.commons.math3.optim.nonlinear.vector.Target",
  "org.apache.commons.math3.optim.nonlinear.vector.Weight",
  "org.apache.commons.math3.optim.nonlinear.vector.jacobian.AbstractLeastSquaresOptimizer",
  "org.apache.commons.math3.optim.nonlinear.vector.jacobian.GaussNewtonOptimizer",
  "org.apache.commons.math3.optim.nonlinear.vector.jacobian.LevenbergMarquardtOptimizer",
  "org.apache.commons.math3.optim.univariate.BracketFinder",
  "org.apache.commons.math3.optim.univariate.BrentOptimizer",
  "org.apache.commons.math3.optim.univariate.MultiStartUnivariateOptimizer",
  "org.apache.commons.math3.optim.univariate.SearchInterval",
  "org.apache.commons.math3.optim.univariate.SimpleUnivariateValueChecker",
  "org.apache.commons.math3.optim.univariate.UnivariateObjectiveFunction",
  "org.apache.commons.math3.optim.univariate.UnivariateOptimizer",
  "org.apache.commons.math3.optim.univariate.UnivariatePointValuePair",
  "org.apache.commons.math3.optimization.AbstractConvergenceChecker",
  "org.apache.commons.math3.optimization.BaseMultivariateMultiStartOptimizer",
  "org.apache.commons.math3.optimization.BaseMultivariateOptimizer",
  "org.apache.commons.math3.optimization.BaseMultivariateSimpleBoundsOptimizer",
  "org.apache.commons.math3.optimization.BaseMultivariateVectorMultiStartOptimizer",
  "org.apache.commons.math3.optimization.BaseMultivariateVectorOptimizer",
  "org.apache.commons.math3.optimization.BaseOptimizer",
  "org.apache.commons.math3.optimization.ConvergenceChecker",
  "org.apache.commons.math3.optimization.DifferentiableMultivariateMultiStartOptimizer",
  "org.apache.commons.math3.optimization.DifferentiableMultivariateOptimizer",
  "org.apache.commons.math3.optimization.DifferentiableMultivariateVectorMultiStartOptimizer",
  "org.apache.commons.math3.optimization.DifferentiableMultivariateVectorOptimizer",
  "org.apache.commons.math3.optimization.GoalType",
  "org.apache.commons.math3.optimization.InitialGuess",
  "org.apache.commons.math3.optimization.LeastSquaresConverter",
  "org.apache.commons.math3.optimization.MultivariateDifferentiableMultiStartOptimizer",
  "org.apache.commons.math3.optimization.MultivariateDifferentiableOptimizer",
  "org.apache.commons.math3.optimization.MultivariateDifferentiableVectorMultiStartOptimizer",
  "org.apache.commons.math3.optimization.MultivariateDifferentiableVectorOptimizer",
  "org.apache.commons.math3.optimization.MultivariateMultiStartOptimizer",
  "org.apache.commons.math3.optimization.MultivariateOptimizer",
  "org.apache.commons.math3.optimization.OptimizationData",
  "org.apache.commons.math3.optimization.PointValuePair",
  "org.apache.commons.math3.optimization.PointVectorValuePair",
  "org.apache.commons.math3.optimization.SimpleBounds",
  "org.apache.commons.math3.optimization.SimplePointChecker",
  "org.apache.commons.math3.optimization.SimpleValueChecker",
  "org.apache.commons.math3.optimization.SimpleVectorValueChecker",
  "org.apache.commons.math3.optimization.Target",
  "org.apache.commons.math3.optimization.Weight",
  "org.apache.commons.math3.optimization.direct.AbstractSimplex",
  "org.apache.commons.math3.optimization.direct.BOBYQAOptimizer",
  "org.apache.commons.math3.optimization.direct.BaseAbstractMultivariateOptimizer",
  "org.apache.commons.math3.optimization.direct.BaseAbstractMultivariateSimpleBoundsOptimizer",
  "org.apache.commons.math3.optimization.direct.BaseAbstractMultivariateVectorOptimizer",
  "org.apache.commons.math3.optimization.direct.CMAESOptimizer",
  "org.apache.commons.math3.optimization.direct.MultiDirectionalSimplex",
  "org.apache.commons.math3.optimization.direct.MultivariateFunctionMappingAdapter",
  "org.apache.commons.math3.optimization.direct.MultivariateFunctionPenaltyAdapter",
  "org.apache.commons.math3.optimization.direct.NelderMeadSimplex",
  "org.apache.commons.math3.optimization.direct.PowellOptimizer",
  "org.apache.commons.math3.optimization.direct.SimplexOptimizer",
  "org.apache.commons.math3.optimization.fitting.CurveFitter",
  "org.apache.commons.math3.optimization.fitting.GaussianFitter",
  "org.apache.commons.math3.optimization.fitting.HarmonicFitter",
  "org.apache.commons.math3.optimization.fitting.PolynomialFitter",
  "org.apache.commons.math3.optimization.fitting.WeightedObservedPoint",
  "org.apache.commons.math3.optimization.general.AbstractDifferentiableOptimizer",
  "org.apache.commons.math3.optimization.general.AbstractLeastSquaresOptimizer",
  "org.apache.commons.math3.optimization.general.AbstractScalarDifferentiableOptimizer",
  "org.apache.commons.math3.optimization.general.ConjugateGradientFormula",
  "org.apache.commons.math3.optimization.general.GaussNewtonOptimizer",
  "org.apache.commons.math3.optimization.general.LevenbergMarquardtOptimizer",
  "org.apache.commons.math3.optimization.general.NonLinearConjugateGradientOptimizer",
  "org.apache.commons.math3.optimization.general.Preconditioner",
  "org.apache.commons.math3.optimization.linear.AbstractLinearOptimizer",
  "org.apache.commons.math3.optimization.linear.LinearConstraint",
  "org.apache.commons.math3.optimization.linear.LinearObjectiveFunction",
  "org.apache.commons.math3.optimization.linear.LinearOptimizer",
  "org.apache.commons.math3.optimization.linear.NoFeasibleSolutionException",
  "org.apache.commons.math3.optimization.linear.Relationship",
  "org.apache.commons.math3.optimization.linear.SimplexSolver",
  "org.apache.commons.math3.optimization.linear.SimplexTableau",
  "org.apache.commons.math3.optimization.linear.UnboundedSolutionException",
  "org.apache.commons.math3.optimization.univariate.BaseAbstractUnivariateOptimizer",
  "org.apache.commons.math3.optimization.univariate.BaseUnivariateOptimizer",
  "org.apache.commons.math3.optimization.univariate.BracketFinder",
  "org.apache.commons.math3.optimization.univariate.BrentOptimizer",
  "org.apache.commons.math3.optimization.univariate.SimpleUnivariateValueChecker",
  "org.apache.commons.math3.optimization.univariate.UnivariateMultiStartOptimizer",
  "org.apache.commons.math3.optimization.univariate.UnivariateOptimizer",
  "org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair",
  "org.apache.commons.math3.primes.PollardRho",
  "org.apache.commons.math3.primes.Primes",
  "org.apache.commons.math3.primes.SmallPrimes",
  "org.apache.commons.math3.random.AbstractRandomGenerator",
  "org.apache.commons.math3.random.AbstractWell",
  "org.apache.commons.math3.random.BitsStreamGenerator",
  "org.apache.commons.math3.random.CorrelatedRandomVectorGenerator",
  "org.apache.commons.math3.random.EmpiricalDistribution",
  "org.apache.commons.math3.random.GaussianRandomGenerator",
  "org.apache.commons.math3.random.HaltonSequenceGenerator",
  "org.apache.commons.math3.random.ISAACRandom",
  "org.apache.commons.math3.random.JDKRandomGenerator",
  "org.apache.commons.math3.random.MersenneTwister",
  "org.apache.commons.math3.random.NormalizedRandomGenerator",
  "org.apache.commons.math3.random.RandomAdaptor",
  "org.apache.commons.math3.random.RandomData",
  "org.apache.commons.math3.random.RandomDataGenerator",
  "org.apache.commons.math3.random.RandomDataImpl",
  "org.apache.commons.math3.random.RandomGenerator",
  "org.apache.commons.math3.random.RandomGeneratorFactory",
  "org.apache.commons.math3.random.RandomVectorGenerator",
  "org.apache.commons.math3.random.SobolSequenceGenerator",
  "org.apache.commons.math3.random.StableRandomGenerator",
  "org.apache.commons.math3.random.SynchronizedRandomGenerator",
  "org.apache.commons.math3.random.UncorrelatedRandomVectorGenerator",
  "org.apache.commons.math3.random.UniformRandomGenerator",
  "org.apache.commons.math3.random.UnitSphereRandomVectorGenerator",
  "org.apache.commons.math3.random.ValueServer",
  "org.apache.commons.math3.random.Well1024a",
  "org.apache.commons.math3.random.Well19937a",
  "org.apache.commons.math3.random.Well19937c",
  "org.apache.commons.math3.random.Well44497a",
  "org.apache.commons.math3.random.Well44497b",
  "org.apache.commons.math3.random.Well512a",
  "org.apache.commons.math3.special.Beta",
  "org.apache.commons.math3.special.Erf",
  "org.apache.commons.math3.special.Gamma",
  "org.apache.commons.math3.stat.Frequency",
  "org.apache.commons.math3.stat.StatUtils",
  "org.apache.commons.math3.stat.clustering.Cluster",
  "org.apache.commons.math3.stat.clustering.Clusterable",
  "org.apache.commons.math3.stat.clustering.DBSCANClusterer",
  "org.apache.commons.math3.stat.clustering.EuclideanDoublePoint",
  "org.apache.commons.math3.stat.clustering.EuclideanIntegerPoint",
  "org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer",
  "org.apache.commons.math3.stat.correlation.Covariance",
  "org.apache.commons.math3.stat.correlation.PearsonsCorrelation",
  "org.apache.commons.math3.stat.correlation.SpearmansCorrelation",
  "org.apache.commons.math3.stat.correlation.StorelessBivariateCovariance",
  "org.apache.commons.math3.stat.correlation.StorelessCovariance",
  "org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic",
  "org.apache.commons.math3.stat.descriptive.AbstractUnivariateStatistic",
  "org.apache.commons.math3.stat.descriptive.AggregateSummaryStatistics",
  "org.apache.commons.math3.stat.descriptive.DescriptiveStatistics",
  "org.apache.commons.math3.stat.descriptive.MultivariateSummaryStatistics",
  "org.apache.commons.math3.stat.descriptive.StatisticalMultivariateSummary",
  "org.apache.commons.math3.stat.descriptive.StatisticalSummary",
  "org.apache.commons.math3.stat.descriptive.StatisticalSummaryValues",
  "org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic",
  "org.apache.commons.math3.stat.descriptive.SummaryStatistics",
  "org.apache.commons.math3.stat.descriptive.SynchronizedDescriptiveStatistics",
  "org.apache.commons.math3.stat.descriptive.SynchronizedMultivariateSummaryStatistics",
  "org.apache.commons.math3.stat.descriptive.SynchronizedSummaryStatistics",
  "org.apache.commons.math3.stat.descriptive.UnivariateStatistic",
  "org.apache.commons.math3.stat.descriptive.WeightedEvaluation",
  "org.apache.commons.math3.stat.descriptive.moment.FirstMoment",
  "org.apache.commons.math3.stat.descriptive.moment.FourthMoment",
  "org.apache.commons.math3.stat.descriptive.moment.GeometricMean",
  "org.apache.commons.math3.stat.descriptive.moment.Kurtosis",
  "org.apache.commons.math3.stat.descriptive.moment.Mean",
  "org.apache.commons.math3.stat.descriptive.moment.SecondMoment",
  "org.apache.commons.math3.stat.descriptive.moment.SemiVariance",
  "org.apache.commons.math3.stat.descriptive.moment.Skewness",
  "org.apache.commons.math3.stat.descriptive.moment.StandardDeviation",
  "org.apache.commons.math3.stat.descriptive.moment.ThirdMoment",
  "org.apache.commons.math3.stat.descriptive.moment.Variance",
  "org.apache.commons.math3.stat.descriptive.moment.VectorialCovariance",
  "org.apache.commons.math3.stat.descriptive.moment.VectorialMean",
  "org.apache.commons.math3.stat.descriptive.rank.Max",
  "org.apache.commons.math3.stat.descriptive.rank.Median",
  "org.apache.commons.math3.stat.descriptive.rank.Min",
  "org.apache.commons.math3.stat.descriptive.rank.Percentile",
  "org.apache.commons.math3.stat.descriptive.summary.Product",
  "org.apache.commons.math3.stat.descriptive.summary.Sum",
  "org.apache.commons.math3.stat.descriptive.summary.SumOfLogs",
  "org.apache.commons.math3.stat.descriptive.summary.SumOfSquares",
  "org.apache.commons.math3.stat.inference.ChiSquareTest",
  "org.apache.commons.math3.stat.inference.GTest",
  "org.apache.commons.math3.stat.inference.MannWhitneyUTest",
  "org.apache.commons.math3.stat.inference.OneWayAnova",
  "org.apache.commons.math3.stat.inference.TTest",
  "org.apache.commons.math3.stat.inference.TestUtils",
  "org.apache.commons.math3.stat.inference.WilcoxonSignedRankTest",
  "org.apache.commons.math3.stat.ranking.NaNStrategy",
  "org.apache.commons.math3.stat.ranking.NaturalRanking",
  "org.apache.commons.math3.stat.ranking.RankingAlgorithm",
  "org.apache.commons.math3.stat.ranking.TiesStrategy",
  "org.apache.commons.math3.stat.regression.AbstractMultipleLinearRegression",
  "org.apache.commons.math3.stat.regression.GLSMultipleLinearRegression",
  "org.apache.commons.math3.stat.regression.MillerUpdatingRegression",
  "org.apache.commons.math3.stat.regression.ModelSpecificationException",
  "org.apache.commons.math3.stat.regression.MultipleLinearRegression",
  "org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression",
  "org.apache.commons.math3.stat.regression.RegressionResults",
  "org.apache.commons.math3.stat.regression.SimpleRegression",
  "org.apache.commons.math3.stat.regression.UpdatingMultipleLinearRegression",
  "org.apache.commons.math3.transform.DctNormalization",
  "org.apache.commons.math3.transform.DftNormalization",
  "org.apache.commons.math3.transform.DstNormalization",
  "org.apache.commons.math3.transform.FastCosineTransformer",
  "org.apache.commons.math3.transform.FastFourierTransformer",
  "org.apache.commons.math3.transform.FastHadamardTransformer",
  "org.apache.commons.math3.transform.FastSineTransformer",
  "org.apache.commons.math3.transform.RealTransformer",
  "org.apache.commons.math3.transform.TransformType",
  "org.apache.commons.math3.transform.TransformUtils",
  "org.apache.commons.math3.util.ArithmeticUtils",
  "org.apache.commons.math3.util.BigReal",
  "org.apache.commons.math3.util.BigRealField",
  "org.apache.commons.math3.util.CombinatoricsUtils",
  "org.apache.commons.math3.util.CompositeFormat",
  "org.apache.commons.math3.util.ContinuedFraction",
  "org.apache.commons.math3.util.Decimal64",
  "org.apache.commons.math3.util.Decimal64Field",
  "org.apache.commons.math3.util.DefaultTransformer",
  "org.apache.commons.math3.util.DoubleArray",
  "org.apache.commons.math3.util.FastMath",
  "org.apache.commons.math3.util.FastMathCalc",
  "org.apache.commons.math3.util.FastMathLiteralArrays",
  "org.apache.commons.math3.util.Incrementor",
  "org.apache.commons.math3.util.IterationEvent",
  "org.apache.commons.math3.util.IterationListener",
  "org.apache.commons.math3.util.IterationManager",
  "org.apache.commons.math3.util.MathArrays",
  "org.apache.commons.math3.util.MathUtils",
  "org.apache.commons.math3.util.MultidimensionalCounter",
  "org.apache.commons.math3.util.NumberTransformer",
  "org.apache.commons.math3.util.OpenIntToDoubleHashMap",
  "org.apache.commons.math3.util.OpenIntToFieldHashMap",
  "org.apache.commons.math3.util.Pair",
  "org.apache.commons.math3.util.Precision",
  "org.apache.commons.math3.util.ResizableDoubleArray",
  "org.apache.commons.math3.util.TransformerMap"
]
```

Reach the target with meaningful domain arguments. Do not substitute constructor exceptions, null-only inputs or empty collections for behavior assertions. No execution feedback or repair loop.

## build.xml

```
<!--
    Licensed to the Apache Software Foundation (ASF) under one or more
    contributor license agreements.  See the NOTICE file distributed with
    this work for additional information regarding copyright ownership.
    The ASF licenses this file to You under the Apache License, Version 2.0
    (the "License"); you may not use this file except in compliance with
    the License.  You may obtain a copy of the License at
   
         http://www.apache.org/licenses/LICENSE-2.0
   
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
-->
<project name="Commons Math" default="jar" basedir=".">


<!--
        "Math" component of the Apache Commons Project
        $Id$
        $Revision$ $Date$
-->


<!-- ========== Initialize Properties ===================================== -->

  <property file="build.properties"/>                <!-- Component local   -->
  <property file="${user.home}/build.properties"/>   <!-- User local        -->


<!-- ========== External Dependencies ===================================== -->


  <!-- Junit -->
  <property name="junit.version"           value="4.8.2"/>
  <property name="junit.home"              value="/usr/share/junit"/>
  <property name="junit.jar"               value="${junit.home}/junit-${junit.version}.jar"/>


<!-- ========== Component Declarations ==================================== -->


  <!-- The name of this component -->
  <property name="component.name"          value="commons-math"/>

  <!-- The primary package name of this component -->
  <property name="component.package"       value="org.apache.commons.math3"/>

  <!-- The title of this component -->
  <property name="component.title"         value="Commons MATH"/>

  <!-- The current version number of this component -->
  <property name="component.version"       value="3.1-SNAPSHOT"/>

  <!-- The base directory for component sources -->
  <property name="source.home"             value="src/main/java"/>

  <!-- The base directory for component resources -->
  <property name="source.resources"        value="src/main/resources"/>

  <!-- The base directory for unit test sources -->
  <property name="test.home"               value="src/test/java"/>

  <!-- The base directory for unit test resources -->
  <property name="test.resources"          value="src/test/resources"/>

  <!-- Download lib dir -->
  <property name="download.lib.dir"        value="lib"/>

  <!-- The base directory for compilation targets -->
  <property name="build.home"              value="target"/>

  <!-- The base directory for distribution targets -->
  <property name="dist.home"               value="${build.home}/dist"/>

  <!-- The base directory for test reports -->
  <property name="test.reports"            value="${build.home}/test-reports"/>

  <!-- Base file name for dist files -->
  <property name="final.name"              value="${component.name}-${component.version}"/>

  <!-- Directory where binary release files are staged -->
  <property name="stage.bin.dir"           value="${dist.home}/stage-bin"/>

  <!-- Directory where source release files are staged -->
  <property name="stage.src.dir"           value="${dist.home}/stage-src"/>

<!-- ========== Compiler Defaults ========================================= -->

  <!-- Should Java compilations set the 'debug' compiler option? -->
  <property name="compile.debug"           value="true"/>

  <!-- Should Java compilations set the 'deprecation' compiler option? -->
  <property name="compile.deprecation"     value="false"/>

  <!-- Should Java compilations set the 'optimize' compiler option? -->
  <property name="compile.optimize"        value="true"/>

  <!-- File encoding -->
  <property name="source.encoding"         value="UTF-8"/>
    
  <!-- JDK level -->
  <property name="compile.source"          value="1.6"/>
  <property name="compile.target"          value="1.6"/>

  <!-- Base compile classpath -->
  <path id="compile.classpath">
    <pathelement location="${build.home}/classes"/>
  </path>

  <!-- External dependency classpath -->
  <path id="downloaded.lib.classpath">
    <pathelement location="${download.lib.dir}/junit-${junit.version}.jar"/>
  </path>

<!-- ========== Test Execution Defaults =================================== -->


  <!-- Construct unit test classpath -->
  <path id="test.classpath">
    <pathelement location="${build.home}/classes"/>
    <pathelement location="${build.home}/test-classes"/>
    <pathelement location="${junit.jar}"/>
    <path refid="downloaded.lib.classpath"/>
  </path>

  <!-- Should the build fail if there are test failures? -->
  <property name="test.failonerror"        value="true"/>

<!-- ========== Executable Targets ======================================== -->

  <target name="clean" description="Clean build and distribution directories">
    <delete    dir="${build.home}"/>
  </target>


  <target name="init"
   description="Initialize and evaluate conditionals">
    <echo message="-------- ${component.title} ${component.version} --------"/>
    <filter  token="name"                  value="${component.name}"/>
    <filter  token="package"               value="${component.package}"/>
    <filter  token="version"               value="${component.version}"/>
    <filter  token="compilesource"         value="${compile.source}"/>
    <filter  token="compiletarget"         value="${compile.target}"/>
    <tstamp/>
    <mkdir dir="${build.home}"/>
    <mkdir dir="${build.home}/classes"/>
    <mkdir dir="${build.home}/test-classes"/>
    <copy todir="${build.home}/classes/">
       <fileset dir="${source.resources}" />
    </copy>

  </target>

<!-- ========== Compile Targets =========================================== -->

  <target name="compile" depends="init" description="Compile">

    <javac  srcdir="${source.home}"
           destdir="${build.home}/classes"
             source="${compile.source}"
             target="${compile.target}"
             debug="${compile.debug}"
       deprecation="${compile.deprecation}"
 includeantruntime="false"
          encoding="${source.encoding}"
          optimize="${compile.optimize}">
      <classpath refid="compile.classpath"/>
    </javac>
  </target>


<!-- ========== Unit Test Targets ========================================= -->

    <target name="compile.tests" depends="compile, download-dependencies" description="Compile unit tests.">

      <javac srcdir="${test.home}"
             destdir="${build.home}/test-classes"
             debug="${compile.debug}"
             deprecation="${compile.deprecation}"
             encoding="${source.encoding}"
             includeantruntime="false"
             optimize="${compile.optimize}">
          <classpath refid="test.classpath"/>
      </javac>
    
      <copy todir="${build.home}/test-classes">
          <fileset dir="${test.resources}">
          </fileset>
      </copy>

    </target>

  <target name="test"  depends="compile.tests"
                       description="Run unit tests">
      <mkdir dir="${test.reports}"/>
      <junit printsummary="true"
               errorProperty="test.failed"
               failureProperty="test.failed"
               fork="true"
               showOutput="true">
               <formatter type="brief"/>
               <classpath refid="test.classpath"/>
               <!-- If test.entry is defined, run a single test, otherwise run all valid tests -->
               <!-- N.B. test.entry must be the full path to the test class, for example:
               ant test -Dtest.entry=org.apache.commons.math3.util.FastMathTestPerformance
               -->
               <test name="${test.entry}" todir="${test.reports}" if="test.entry"/>
               <batchtest todir="${test.reports}" unless="test.entry">
                   <fileset dir="${test.home}">
                      <include name="**/*Test.java"/> 
                      <include name="**/*TestBinary.java"/> 
                      <include name="**/*TestPermutations.java"/> 
                      <exclude name="**/*AbstractTest.java"/>
                   </fileset>
               </batchtest>
       </junit>
       <fail message="There were test failures.">
           <condition>
               <and>
                   <istrue value="${test.failonerror}"/>
                   <isset property="test.failed"/>
               </and>
           </condition>
     </fail>
  </target>


<!-- ========== Produce JavaDocs ========================================== -->

  <target name="javadoc" depends="compile" description="Create component Javadoc documentation">
    <mkdir dir="${build.home}/apidocs"/>
    <tstamp>
        <format property="current.year" pattern="yyyy"/>
    </tstamp>
    <javadoc sourcepath="${source.home}"
                destdir="${build.home}/apidocs"
           packagenames="org.apache.commons.*"
                 author="true"
                private="true"
                version="true"
               encoding="${source.encoding}"
                charset="${source.encoding}"
            docencoding="${source.encoding}"
               doctitle="&lt;h1&gt;${component.title} ${component.version}&lt;/h1&gt;"
            windowtitle="${component.title} ${component.version}"
                 bottom="Copyright (c) 2003-${current.year}  Apache Software Foundation"
    	additionalparam="-header &apos;&lt;script type=&quot;text/javascript&quot; src=&quot;http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML&quot;&gt;&lt;/script&gt;&apos;"
           classpathref="compile.classpath">
        <link href="http://java.sun.com/j2se/1.5.0/docs/api/"/>  
    </javadoc>
  </target>


<!-- ========== Create Jar ================================================ -->

  <target name="jar" depends="test" description="Create jar file">

    <copy file="LICENSE.txt" tofile="${build.home}/classes/META-INF/LICENSE.txt"/>
    <copy file="NOTICE.txt"  tofile="${build.home}/classes/META-INF/NOTICE.txt"/>

    <manifest file="${build.home}/MANIFEST.MF">
        <attribute name="Specification-Title"      value="${component.title}"/>
        <attribute name="Specification-Version"    value="${component.version}"/>
        <attribute name="Specification-Vendor"     value="Apache Software Foundation"/>
        <attribute name="Implementation-Title"     value="${component.title}"/>
        <attribute name="Implementation-Version"   value="${component.version}"/> 
        <attribute name="Implementation-Vendor"    value="Apache Software Foundation"/>
        <attribute name="Implementation-Vendor-Id" value="org.apache"/>
        <attribute name="X-Compile-Source-JDK"     value="${compile.source}"/>
        <attribute name="X-Compile-Target-JDK"     value="${compile.target}"/>
    </manifest>

    <jar jarfile="${build.home}/${final.name}.jar"
         basedir="${build.home}/classes"
        manifest="${build.home}/MANIFEST.MF"/>
  </target>


<!-- ========== Distribution Target =========================================== -->

  <target name="dist" depends="clean,jar,javadoc" description="Create distribution artifacts">

    <mkdir dir="${dist.home}"/>

    <!-- jar(s) -->
    <copy todir="${dist.home}">
      <fileset dir=".">
        <include name="RELEASE-NOTES.txt"/>
      </fileset>
      <fileset dir="${build.home}">
        <include name="*.jar"/>
      </fileset>
    </copy>

    <!-- Binary Distro -->
    <mkdir dir="${stage.bin.dir}/${final.name}"/>
    <copy todir="${stage.bin.dir}/${final.name}">
      <fileset dir=".">
        <include name="LICENSE.txt"/>
        <include name="NOTICE.txt"/>
        <include name="RELEASE-NOTES.txt"/>
      </fileset>
      <fileset dir="${build.home}">
        <include name="*.jar"/>
      </fileset>
    </copy>
    <copy todir="${stage.bin.dir}/${final.name}/apidocs">
      <fileset dir="${build.home}/apidocs" />
    </copy>

    <!-- Source Distro -->
    <mkdir dir="${stage.src.dir}/${final.name}-src"/>
    <copy todir="${stage.src.dir}/${final.name}-src">
      <fileset dir=".">
        <include name="*.xml"/>
        <include name="*.txt"/>
        <include name="*.html"/>
      </fileset>
    </copy>
    <copy todir="${stage.src.dir}/${final.name}-src/src">
      <fileset dir="src" excludes="mantissa/**,experimental/**" />
    </copy>
    <zip  zipfile="${dist.home}/${final.name}.zip"     basedir="${stage.bin.dir}"/>
    <zip  zipfile="${dist.home}/${final.name}-src.zip" basedir="${stage.src.dir}"/>
    <tar  tarfile="${dist.home}/${final.name}.tar"     basedir="${stage.bin.dir}" longfile="gnu"/>
    <tar  tarfile="${dist.home}/${final.name}-src.tar" basedir="${stage.src.dir}" longfile="gnu"/>
    <gzip     src="${dist.home}/${final.name}.tar"     zipfile="${dist.home}/${final.name}.tar.gz"/>
    <gzip     src="${dist.home}/${final.name}-src.tar" zipfile="${dist.home}/${final.name}-src.tar.gz"/>

    <!-- clean up staging directories -->
    <delete    dir="${stage.bin.dir}"/>
    <delete    dir="${stage.src.dir}"/>

  </target>


<!-- ========== Gump Target ===================================================== -->

  <target name="gump" depends="clean,test,javadoc,jar" description="Gump Target - clean,test,javadoc,jar"/>


<!-- ========== Download Dependencies =========================================== -->

    <target name="download-dependencies" 
           depends="check-availability" unless="skip.download">
        <echo message="doing download-dependencies..." />
        <antcall target="download-junit" />
    </target>

    <target name="check-availability">
        <echo message="doing check-availability..." />
        <available file="${junit.jar}" property="junit.found"/>
    </target>

    <target name="download-junit" unless="junit.found">
        <echo message="Downloading junit..."/>
        <mkdir dir="${download.lib.dir}" />
        <get dest="${download.lib.dir}/junit-${junit.version}.jar"
            usetimestamp="true" ignoreerrors="true"
            src="http://repo1.maven.org/maven2/junit/junit/${junit.version}/junit-${junit.version}.jar"/>
    </target>
      
</project>


```

## pom.xml

```
<?xml version="1.0"?>
<!--
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
-->
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <parent>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-parent</artifactId>
    <version>32</version>
  </parent>
  <modelVersion>4.0.0</modelVersion>
  <groupId>org.apache.commons</groupId>
  <artifactId>commons-math3</artifactId>
  <version>3.3-SNAPSHOT</version>
  <name>Commons Math</name>

  <inceptionYear>2003</inceptionYear>
  <description>The Math project is a library of lightweight, self-contained mathematics and statistics components addressing the most common practical problems not immediately available in the Java programming language or commons-lang.</description>

  <url>http://commons.apache.org/proper/commons-math/</url>

  <issueManagement>
    <system>jira</system>
    <url>http://issues.apache.org/jira/browse/MATH</url>
  </issueManagement>

  <scm>
    <connection>scm:svn:http://svn.apache.org/repos/asf/commons/proper/math/trunk</connection>
    <developerConnection>scm:svn:https://svn.apache.org/repos/asf/commons/proper/math/trunk</developerConnection>
    <url>http://svn.apache.org/viewvc/commons/proper/math/trunk</url>
  </scm>
  
  <distributionManagement>
    <site>
      <id>people.apache.org</id>
      <name>Commons Math</name>
      <url>scp://people.apache.org/www/commons.apache.org/math</url>
    </site>
  </distributionManagement>

  <developers>
    <developer>
      <name>Mikkel Meyer Andersen</name>
      <id>mikl</id>
      <email>mikl at apache dot org</email>
    </developer>
    <developer>
      <name>Bill Barker</name>
      <id>billbarker</id>
      <email>billbarker at apache dot org</email>
    </developer>
    <developer>
      <name>S&#233;bastien Brisard</name>
      <id>celestin</id>
      <email>celestin at apache dot org</email>
    </developer>
    <developer>
      <name>Albert Davidson Chou</name>
      <id>achou</id>
      <email>achou at apache dot org</email>
    </developer>
    <developer>
      <name>Mark Diggory</name>
      <id>mdiggory</id>
      <email>mdiggory at apache dot org</email>
    </developer>
    <developer>
      <name>Robert Burrell Donkin</name>
      <id>rdonkin</id>
      <email>rdonkin at apache dot org</email>
    </developer>
    <developer>
      <name>Luc Maisonobe</name>
      <id>luc</id>
      <email>luc at apache dot org</email>
    </developer>
    <developer>
      <name>Tim O'Brien</name>
      <id>tobrien</id>
      <email>tobrien at apache dot org</email>
    </developer>
    <developer>
      <name>J. Pietschmann</name>
      <id>pietsch</id>
      <email>j3322ptm at yahoo dot de</email>
    </developer>
    <developer>
      <name>Dimitri Pourbaix</name>
      <id>dimpbx</id>
      <email>dimpbx at apache dot org</email>
    </developer>
    <developer>
      <name>Gilles Sadowski</name>
      <id>erans</id>
      <email>erans at apache dot org</email>
    </developer>
    <developer>
      <name>Phil Steitz</name>
      <id>psteitz</id>
      <email>psteitz at apache dot org</email>
    </developer>
    <developer>
      <name>Greg Sterijevski</name>
      <id>gregs</id>
      <email>gregs at apache dot org</email>
    </developer>
    <developer>
      <name>Brent Worden</name>
      <id>brentworden</id>
      <email>brentworden at apache dot org</email>
    </developer>
    <developer>
      <name>Thomas Neidhart</name>
      <id>tn</id>
      <email>tn at apache dot org</email>
    </developer>
  </developers>
  <contributors>
    <contributor>
      <name>Eldar Agalarov</name>
    </contributor>
    <contributor>
      <name>Tim Allison</name>
    </contributor>
    <contributor>
      <name>C. Scott Ananian</name>
    </contributor>
    <contributor>
      <name>Mark Anderson</name>
    </contributor>
    <contributor>
      <name>Peter Andrews</name>
    </contributor>
    <contributor>
      <name>R&#233;mi Arntzen</name>
    </contributor>
    <contributor>
      <name>Jared Becksfort</name>
    </contributor>
    <contributor>
      <name>Michael Bjorkegren</name>
    </contributor>
    <contributor>
      <name>Brian Bloniarz</name>
    </contributor>
    <contributor>
      <name>John Bollinger</name>
    </contributor>
    <contributor>
      <name>Cyril Briquet</name>
    </contributor>
    <contributor>
      <name>Dave Brosius</name>
    </contributor>
    <contributor>
      <name>Dan Checkoway</name>
    </contributor>
    <contributor>
      <name>Charles Cooper</name>
    </contributor>
    <contributor>
      <name>Paul Cowan</name>
    </contributor>
    <contributor>
      <name>Benjamin Croizet</name>
    </contributor>
    <contributor>
      <name>Larry Diamond</name>
    </contributor>
    <contributor>
      <name>Rodrigo di Lorenzo Lopes</name>
    </contributor>
    <contributor>
      <name>Hasan Diwan</name>
    </contributor>
    <contributor>
      <name>Ted Dunning</name>
    </contributor>
    <contributor>
      <name>Ajo Fod</name>
    </contributor>
    <contributor>
      <name>John Gant</name>
    </contributor>
    <contributor>
      <name>Ken Geis</name>
    </contributor>
    <contributor>
      <name>Bernhard Gr&#252;newaldt</name>
    </contributor>
    <contributor>
      <name>Elliotte Rusty Harold</name>
    </contributor>
    <contributor>
      <name>Dennis Hendriks</name>
    </contributor>
    <contributor>
      <name>Reid Hochstedler</name>
    </contributor>
    <contributor>
      <name>Matthias Hummel</name>
    </contributor>
    <contributor>
      <name>Curtis Jensen</name>
    </contributor>
    <contributor>
      <name>Ismael Juma</name>
    </contributor>
    <contributor>
      <name>Eugene Kirpichov</name>
    </contributor>
    <contributor>
      <name>Oleksandr Kornieiev</name>
    </contributor>
    <contributor>
      <name>Piotr Kochanski</name>
    </contributor>
    <contributor>
      <name>Bob MacCallum</name>
    </contributor>
    <contributor>
      <name>Jake Mannix</name>
    </contributor>
    <contributor>
      <name>Benjamin McCann</name>
    </contributor>
    <contributor>
      <name>Patrick Meyer</name>
    </contributor>
    <contributor>
      <name>J. Lewis Muir</name>
    </contributor>
    <contributor>
      <name>Christopher Nix</name>
    </contributor>
    <contributor>
      <name>Fredrik Norin</name>
    </contributor>
    <contributor>
      <name>Sujit Pal</name>
    </contributor>
    <contributor>
      <name>Todd C. Parnell</name>
    </contributor>
    <contributor>
      <name>Andreas Rieger</name>
    </contributor>
    <contributor>
      <name>S&#233;bastien Riou</name>
    </contributor>
    <contributor>
      <name>Bill Rossi</name>
    </contributor>
    <contributor>
      <name>Matthew Rowles</name>
    </contributor>
    <contributor>
      <name>Pavel Ryzhov</name>
    </contributor>
    <contributor>
      <name>Joni Salonen</name>
    </contributor>
    <contributor>
      <name>Michael Saunders</name>
    </contributor>
    <contributor>
      <name>Thorsten Schaefer</name>
    </contributor>
    <contributor>
      <name>Christopher Schuck</name>
    </contributor>
    <contributor>
      <name>Christian Semrau</name>
    </contributor>
    <contributor>
      <name>David Stefka</name>
    </contributor>
    <contributor>
      <name>Mauro Talevi</name>
    </contributor>
    <contributor>
      <name>Radoslav Tsvetkov</name>
    </contributor>
    <contributor>
      <name>Kim van der Linde</name>
    </contributor>
    <contributor>
      <name>Evan Ward</name>
    </contributor>
    <contributor>
      <name>Andrew Waterman</name>
    </contributor>
    <contributor>
      <name>J&#246;rg Weimar</name>
    </contributor>
    <contributor>
      <name>Christian Winter</name>
    </contributor>
    <contributor>
      <name>Piotr Wydrych</name>
    </contributor>
    <contributor>
      <name>Xiaogang Zhang</name>
    </contributor>
  </contributors>

  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.11</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <properties>
    <!-- Do not change: "math" is the name of the component even if the
         name of the base package evolves with major release numbers
         (see "commons.osgi.symbolicName", below). -->
    <commons.componentid>math</commons.componentid>
    <!-- This value must reflect the current name of the base package. -->
    <commons.osgi.symbolicName>org.apache.commons.math3</commons.osgi.symbolicName>
    <!-- do not use snapshot suffix here -->
    <commons.release.version>3.3</commons.release.version>
    <commons.release.desc>(requires Java 1.5+)</commons.release.desc>
    <!-- <commons.rc.version>RC1</commons.rc.version> -->
    <commons.binary.suffix>-bin</commons.binary.suffix>
 
    <commons.release.2.version>2.2</commons.release.2.version>
    <!-- override parent name, because 2.2 uses different artifactId -->
    <commons.release.2.name>commons-math-${commons.release.2.version}</commons.release.2.name>
    <commons.release.2.desc>(requires Java 1.5+)</commons.release.2.desc>
    <commons.release.2.binary.suffix></commons.release.2.binary.suffix>
 
    <commons.jira.id>MATH</commons.jira.id>
    <commons.jira.pid>12310485</commons.jira.pid>
    <commons.encoding>UTF-8</commons.encoding>
    <maven.compiler.source>1.5</maven.compiler.source>
    <maven.compiler.target>1.5</maven.compiler.target>
    <math.pmd.version>2.7.1</math.pmd.version>
    <math.findbugs.version>2.5.1</math.findbugs.version>
    <math.checkstyle.version>2.9.1</math.checkstyle.version>
    <!-- Temporary downdate: 0.6.3 uses different tags and CP32 was not updated -->
    <commons.jacoco.version>0.6.2.201302030002</commons.jacoco.version>
  </properties> 

  <build>
      <plugins>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-surefire-plugin</artifactId>
            <configuration>
              <includes>
                <include>**/*Test.java</include>
                <include>**/*TestBinary.java</include>
                <include>**/*TestPermutations.java</include>
              </includes>
              <excludes>
                <exclude>**/*AbstractTest.java</exclude>
              </excludes>
          </configuration>
        </plugin>
        <plugin>
          <artifactId>maven-assembly-plugin</artifactId>
          <configuration>
            <descriptors>
              <descriptor>src/main/assembly/src.xml</descriptor>
              <descriptor>src/main/assembly/bin.xml</descriptor>
            </descriptors>
            <!-- There are a lot of long file names. Suppress the warnings. -->
            <tarLongFileMode>gnu</tarLongFileMode>
          </configuration>
        </plugin>
        <plugin>
          <groupId>org.codehaus.mojo</groupId>
          <artifactId>clirr-maven-plugin</artifactId>
          <version>${commons.clirr.version}</version>
          <executions>
            <execution>
              <goals>
              </goals>
            </execution>
          </executions>
        </plugin>
      <plugin>
        <artifactId>maven-pmd-plugin</artifactId>
        <version>${math.pmd.version}</version>
        <configuration>
          <targetJdk>${maven.compiler.target}</targetJdk>  
        </configuration>
        </plugin>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-scm-publish-plugin</artifactId>
          <configuration>
            <ignorePathsToDelete>
              <ignorePathToDelete>javadocs</ignorePathToDelete>
            </ignorePathsToDelete>
          </configuration>
        </plugin>

      <plugin>
        <artifactId>maven-antrun-plugin</artifactId>
        <executions>
          <execution>
            <phase>package</phase>
            <configuration>
              <target>
                <jar destfile="target/commons-math3-tools-${project.version}.jar">
                  <metainf dir="${basedir}" includes="NOTICE.txt,LICENSE.txt" />
                  <manifest>
                    <attribute name="Extension-Name" value="org.apache.commons.net" />
                    <attribute name="Specification-Title" value="${project.name}" />
                    <attribute name="Implementation-Title" value="${project.name}" />
                    <attribute name="Implementation-Vendor" value="${project.organization.name}" />
                    <attribute name="Implementation-Version" value="${project.version}" />
                    <attribute name="Implementation-Vendor-Id" value="org.apache" />
                    <attribute name="Implementation-Build" value="${implementation.build}"/>
                    <attribute name="X-Compile-Source-JDK" value="${maven.compiler.source}" />
                    <attribute name="X-Compile-Target-JDK" value="${maven.compiler.target}" />
                  </manifest>
                  <fileset dir="target/test-classes"
                           includes="org/apache/commons/math3/PerfTestUtils*" />
                </jar>
              </target>
            </configuration>
            <goals>
              <goal>run</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
      <!--  Attaches the commons-math3-tools JAR to the Maven lifecycle
            to ensure they will be signed and deployed as normal -->
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>build-helper-maven-plugin</artifactId>
        <version>1.7</version>
        <executions>
          <execution>
            <id>attach-artifacts</id>
            <phase>package</phase>
            <goals>
              <goal>attach-artifact</goal>
            </goals>
            <configuration>
              <artifacts>
                <artifact>
                  <file>target/commons-math3-tools-${project.version}.jar</file>
                  <type>jar</type>
                  <classifier>tools</classifier>
                </artifact>
              </artifacts>
            </configuration>
          </execution>
        </executions>
      </plugin>
      <!--  MathJax -->
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-javadoc-plugin</artifactId>
        <configuration>
          <additionalparam>-header &apos;&lt;script type=&quot;text/javascript&quot; src=&quot;http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML&quot;&gt;&lt;/script&gt;&apos;</additionalparam>
        </configuration>
      </plugin>
    </plugins>
  </build>

  <reporting>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-changes-plugin</artifactId>
        <version>${commons.changes.version}</version>
        <configuration>
          <issueLinkTemplatePerSystem>
            <default>%URL%/%ISSUE%</default>
          </issueLinkTemplatePerSystem>
          <!--  Add sample JIRA report - 'mvn changes:jira-report' or 'mvn site' -->
          <onlyCurrentVersion>false</onlyCurrentVersion>
          <columnNames>Fix Version,Key,Summary,Type,Resolution,Status</columnNames>
          <!-- Sort cols have to be reversed in JIRA 4 -->
          <sortColumnNames>Key DESC,Type,Fix Version DESC</sortColumnNames>
          <resolutionIds>Fixed</resolutionIds>
          <statusIds>Resolved,Closed</statusIds>
          <!-- Don't include sub-task -->
          <typeIds>Bug,New Feature,Task,Improvement,Wish,Test</typeIds>
          <fixVersionIds>${commons.release.version}</fixVersionIds>
          <!-- The default is 100 -->
          <maxEntries>100</maxEntries>
        </configuration>
        <reportSets>
          <reportSet>
            <reports>
              <report>changes-report</report>
              <report>jira-report</report>
            </reports>
          </reportSet>
        </reportSets>
      </plugin>
      <plugin>
        <groupId>org.apache.rat</groupId>
        <artifactId>apache-rat-plugin</artifactId>
        <version>${commons.rat.version}</version>
        <configuration>
          <excludes>

            <!-- MANIFEST files cannot have any comments, so we can't put license header -->
            <exclude>src/test/maxima/special/RealFunctionValidation/MANIFEST.txt</exclude>

            <!-- the following are test data files with specific syntax that cannot include
                 Apache header (and the contained data is public, it is not owned by Apache) -->
            <exclude>src/test/resources/org/apache/commons/math3/random/testData.txt</exclude>
            <exclude>src/test/resources/org/apache/commons/math3/random/emptyFile.txt</exclude>
            <exclude>src/test/resources/org/apache/commons/math3/stat/data/PiDigits.txt</exclude>
            <exclude>src/test/resources/org/apache/commons/math3/stat/data/NumAcc3.txt</exclude>
            <exclude>src/test/resources/org/apache/commons/math3/stat/data/Lew.txt</exclude>
            <exclude>src/test/resources/org/apache/commons/math3/stat/data/NumAcc2.txt</exclude>
            <exclude>src/test/resources/org/apache/commons/math3/stat/data/NumAcc1.txt</exclude>
            <exclude>src/test/resources/org/apache/commons/math3/stat/data/Lottery.txt</exclude>
            <exclude>src/test/resources/org/apache/commons/math3/stat/data/NumAcc4.txt</exclude>
            <exclude>src/test/resources/org/apache/commons/math3/stat/data/Michelso.txt</exclude>
            <exclude>src/test/resources/org/apache/commons/math3/stat/data/Mavro.txt</exclude>

            <!-- version 0.8 of apache-rat-plugin does not exclude properly
                 some default development tools files (see RAT-126) -->
            <exclude>bin/**</exclude>
            <exclude>.gitignore</exclude>
            <exclude>.git/**</exclude>

          </excludes>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>findbugs-maven-plugin</artifactId>
        <version>${math.findbugs.version}</version>
        <configuration>
          <threshold>Normal</threshold>
          <effort>Default</effort>
          <excludeFilterFile>${basedir}/findbugs-exclude-filter.xml</excludeFilterFile>
       </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-checkstyle-plugin</artifactId>
        <version>${math.checkstyle.version}</version>
        <configuration>
          <configLocation>${basedir}/checkstyle.xml</configLocation>
          <enableRulesSummary>false</enableRulesSummary>
          <headerLocation>${basedir}/license-header.txt</headerLocation>
        </configuration>
        <reportSets>
          <reportSet>
            <reports>
              <report>checkstyle</report>
            </reports>
          </reportSet>
        </reportSets>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>clirr-maven-plugin</artifactId>
        <version>${commons.clirr.version}</version>
        <configuration>
          <minSeverity>${minSeverity}</minSeverity>
         </configuration>
      </plugin>
      <plugin>
        <artifactId>maven-pmd-plugin</artifactId>
        <version>${math.pmd.version}</version>
        <configuration>
          <targetJdk>${maven.compiler.target}</targetJdk>  
        </configuration>
        <reportSets>
          <reportSet>
            <reports>
              <report>pmd</report>
              <!-- As of 3.x series, the cpd report sees (correctly) numerous duplications -->
              <!-- This is due to packages being renamed, and the old name still needs to be -->
              <!-- available for compatibility. They will be removed in 4.0 -->
              <!-- So we temporarily disable the CPD report -->
              <!-- <report>cpd</report> -->
            </reports>
          </reportSet>
        </reportSets>
      </plugin>
      <!--  MathJax -->
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-javadoc-plugin</artifactId>
        <configuration>
          <additionalparam>-header &apos;&lt;script type=&quot;text/javascript&quot; src=&quot;http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML&quot;&gt;&lt;/script&gt;&apos;</additionalparam>
        </configuration>
      </plugin>
    </plugins>
  </reporting>
</project>


```

## src/main/java/org/apache/commons/math3/fraction/BigFraction.java

```
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.math3.fraction;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.ArithmeticUtils;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathUtils;

/**
 * Representation of a rational number without any overflow. This class is
 * immutable.
 *
 * @version $Id$
 * @since 2.0
 */
public class BigFraction
    extends Number
    implements FieldElement<BigFraction>, Comparable<BigFraction>, Serializable {

    /** A fraction representing "2 / 1". */
    public static final BigFraction TWO = new BigFraction(2);

    /** A fraction representing "1". */
    public static final BigFraction ONE = new BigFraction(1);

    /** A fraction representing "0". */
    public static final BigFraction ZERO = new BigFraction(0);

    /** A fraction representing "-1 / 1". */
    public static final BigFraction MINUS_ONE = new BigFraction(-1);

    /** A fraction representing "4/5". */
    public static final BigFraction FOUR_FIFTHS = new BigFraction(4, 5);

    /** A fraction representing "1/5". */
    public static final BigFraction ONE_FIFTH = new BigFraction(1, 5);

    /** A fraction representing "1/2". */
    public static final BigFraction ONE_HALF = new BigFraction(1, 2);

    /** A fraction representing "1/4". */
    public static final BigFraction ONE_QUARTER = new BigFraction(1, 4);

    /** A fraction representing "1/3". */
    public static final BigFraction ONE_THIRD = new BigFraction(1, 3);

    /** A fraction representing "3/5". */
    public static final BigFraction THREE_FIFTHS = new BigFraction(3, 5);

    /** A fraction representing "3/4". */
    public static final BigFraction THREE_QUARTERS = new BigFraction(3, 4);

    /** A fraction representing "2/5". */
    public static final BigFraction TWO_FIFTHS = new BigFraction(2, 5);

    /** A fraction representing "2/4". */
    public static final BigFraction TWO_QUARTERS = new BigFraction(2, 4);

    /** A fraction representing "2/3". */
    public static final BigFraction TWO_THIRDS = new BigFraction(2, 3);

    /** Serializable version identifier. */
    private static final long serialVersionUID = -5630213147331578515L;

    /** <code>BigInteger</code> representation of 100. */
    private static final BigInteger ONE_HUNDRED = BigInteger.valueOf(100);

    /** The numerator. */
    private final BigInteger numerator;

    /** The denominator. */
    private final BigInteger denominator;

    /**
     * <p>
     * Create a {@link BigFraction} equivalent to the passed <tt>BigInteger</tt>, ie
     * "num / 1".
     * </p>
     *
     * @param num
     *            the numerator.
     */
    public BigFraction(final BigInteger num) {
        this(num, BigInteger.ONE);
    }

    /**
     * Create a {@link BigFraction} given the numerator and denominator as
     * {@code BigInteger}. The {@link BigFraction} is reduced to lowest terms.
     *
     * @param num the numerator, must not be {@code null}.
     * @param den the denominator, must not be {@code null}.
     * @throws ZeroException if the denominator is zero.
     * @throws NullArgumentException if either of the arguments is null
     */
    public BigFraction(BigInteger num, BigInteger den) {
        MathUtils.checkNotNull(num, LocalizedFormats.NUMERATOR);
        MathUtils.checkNotNull(den, LocalizedFormats.DENOMINATOR);
        if (BigInteger.ZERO.equals(den)) {
            throw new ZeroException(LocalizedFormats.ZERO_DENOMINATOR);
        }
        if (BigInteger.ZERO.equals(num)) {
            numerator   = BigInteger.ZERO;
            denominator = BigInteger.ONE;
        } else {

            // reduce numerator and denominator by greatest common denominator
            final BigInteger gcd = num.gcd(den);
            if (BigInteger.ONE.compareTo(gcd) < 0) {
                num = num.divide(gcd);
                den = den.divide(gcd);
            }

            // move sign to numerator
            if (BigInteger.ZERO.compareTo(den) > 0) {
                num = num.negate();
                den = den.negate();
            }

            // store the values in the final fields
            numerator   = num;
            denominator = den;

        }
    }

    /**
     * Create a fraction given the double value.
     * <p>
     * This constructor behaves <em>differently</em> from
     * {@link #BigFraction(double, double, int)}. It converts the double value
     * exactly, considering its internal bits representation. This works for all
     * values except NaN and infinities and does not requires any loop or
     * convergence threshold.
     * </p>
     * <p>
     * Since this conversion is exact and since double numbers are sometimes
     * approximated, the fraction created may seem strange in some cases. For example,
     * calling <code>new BigFraction(1.0 / 3.0)</code> does <em>not</em> create
     * the fraction 1/3, but the fraction 6004799503160661 / 18014398509481984
     * because the double number passed to the constructor is not exactly 1/3
     * (this number cannot be stored exactly in IEEE754).
     * </p>
     * @see #BigFraction(double, double, int)
     * @param value the double value to convert to a fraction.
     * @exception MathIllegalArgumentException if value is NaN or infinite
     */
    public BigFraction(final double value) throws MathIllegalArgumentException {
        if (Double.isNaN(value)) {
            throw new MathIllegalArgumentException(LocalizedFormats.NAN_VALUE_CONVERSION);
        }
        if (Double.isInfinite(value)) {
            throw new MathIllegalArgumentException(LocalizedFormats.INFINITE_VALUE_CONVERSION);
        }

        // compute m and k such that value = m * 2^k
        final long bits     = Double.doubleToLongBits(value);
        final long sign     = bits & 0x8000000000000000L;
        final long exponent = bits & 0x7ff0000000000000L;
        long m              = bits & 0x000fffffffffffffL;
        if (exponent != 0) {
            // this was a normalized number, add the implicit most significant bit
            m |= 0x0010000000000000L;
        }
        if (sign != 0) {
            m = -m;
        }
        int k = ((int) (exponent >> 52)) - 1075;
        while (((m & 0x001ffffffffffffeL) != 0) && ((m & 0x1) == 0)) {
            m = m >> 1;
            ++k;
        }

        if (k < 0) {
            numerator   = BigInteger.valueOf(m);
            denominator = BigInteger.ZERO.flipBit(-k);
        } else {
            numerator   = BigInteger.valueOf(m).multiply(BigInteger.ZERO.flipBit(k));
            denominator = BigInteger.ONE;
        }

    }

    /**
     * Create a fraction given the double value and maximum error allowed.
     * <p>
     * References:
     * <ul>
     * <li><a href="http://mathworld.wolfram.com/ContinuedFraction.html">
     * Continued Fraction</a> equations (11) and (22)-(26)</li>
     * </ul>
     * </p>
     *
     * @param value
     *            the double value to convert to a fraction.
     * @param epsilon
     *            maximum error allowed. The resulting fraction is within
     *            <code>epsilon</code> of <code>value</code>, in absolute terms.
     * @param maxIterations
     *            maximum number of convergents.
     * @throws FractionConversionException
     *             if the continued fraction failed to converge.
     * @see #BigFraction(double)
     */
    public BigFraction(final double value, final double epsilon,
                       final int maxIterations)
        throws FractionConversionException {
        this(value, epsilon, Integer.MAX_VALUE, maxIterations);
    }

    /**
     * Create a fraction given the double value and either the maximum error
     * allowed or the maximum number of denominator digits.
     * <p>
     *
     * NOTE: This constructor is called with EITHER - a valid epsilon value and
     * the maxDenominator set to Integer.MAX_VALUE (that way the maxDenominator
     * has no effect). OR - a valid maxDenominator value and the epsilon value
     * set to zero (that way epsilon only has effect if there is an exact match
     * before the maxDenominator value is reached).
     * </p>
     * <p>
     *
     * It has been done this way so that the same code can be (re)used for both
     * scenarios. However this could be confusing to users if it were part of
     * the public API and this constructor should therefore remain PRIVATE.
     * </p>
     *
     * See JIRA issue ticket MATH-181 for more details:
     *
     * https://issues.apache.org/jira/browse/MATH-181
     *
     * @param value
     *            the double value to convert to a fraction.
     * @param epsilon
     *            maximum error allowed. The resulting fraction is within
     *            <code>epsilon</code> of <code>value</code>, in absolute terms.
     * @param maxDenominator
     *            maximum denominator value allowed.
     * @param maxIterations
     *            maximum number of convergents.
     * @throws FractionConversionException
     *             if the continued fraction failed to converge.
     */
    private BigFraction(final double value, final double epsilon,
                        final int maxDenominator, int maxIterations)
        throws FractionConversionException {
        long overflow = Integer.MAX_VALUE;
        double r0 = value;
        long a0 = (long) FastMath.floor(r0);
        if (a0 > overflow) {
            throw new FractionConversionException(value, a0, 1l);
        }

        // check for (almost) integer arguments, which should not go
        // to iterations.
        if (FastMath.abs(a0 - value) < epsilon) {
            numerator = BigInteger.valueOf(a0);
            denominator = BigInteger.ONE;
            return;
        }

        long p0 = 1;
        long q0 = 0;
        long p1 = a0;
        long q1 = 1;

        long p2 = 0;
        long q2 = 1;

        int n = 0;
        boolean stop = false;
        do {
            ++n;
            final double r1 = 1.0 / (r0 - a0);
            final long a1 = (long) FastMath.floor(r1);
            p2 = (a1 * p1) + p0;
            q2 = (a1 * q1) + q0;
            if ((p2 > overflow) || (q2 > overflow)) {
                // in maxDenominator mode, if the last fraction was very close to the actual value
                // q2 may overflow in the next iteration; in this case return the last one.
                if (epsilon == 0.0 && FastMath.abs(q1) < maxDenominator) {
                    break;
                }
                throw new FractionConversionException(value, p2, q2);
            }

            final double convergent = (double) p2 / (double) q2;
            if ((n < maxIterations) &&
                (FastMath.abs(convergent - value) > epsilon) &&
                (q2 < maxDenominator)) {
                p0 = p1;
                p1 = p2;
                q0 = q1;
                q1 = q2;
                a0 = a1;
                r0 = r1;
            } else {
                stop = true;
            }
        } while (!stop);

        if (n >= maxIterations) {
            throw new FractionConversionException(value, maxIterations);
        }

        if (q2 < maxDenominator) {
            numerator   = BigInteger.valueOf(p2);
            denominator = BigInteger.valueOf(q2);
        } else {
            numerator   = BigInteger.valueOf(p1);
            denominator = BigInteger.valueOf(q1);
        }
    }

    /**
     * Create a fraction given the double value and maximum denominator.
     * <p>
     * References:
     * <ul>
     * <li><a href="http://mathworld.wolfram.com/ContinuedFraction.html">
     * Continued Fraction</a> equations (11) and (22)-(26)</li>
     * </ul>
     * </p>
     *
     * @param value
     *            the double value to convert to a fraction.
     * @param maxDenominator
     *            The maximum allowed value for denominator.
     * @throws FractionConversionException
     *             if the continued fraction failed to converge.
     */
    public BigFraction(final double value, final int maxDenominator)
        throws FractionConversionException {
        this(value, 0, maxDenominator, 100);
    }

    /**
     * <p>
     * Create a {@link BigFraction} equivalent to the passed <tt>int</tt>, ie
     * "num / 1".
     * </p>
     *
     * @param num
     *            the numerator.
     */
    public BigFraction(final int num) {
        this(BigInteger.valueOf(num), BigInteger.ONE);
    }

    /**
     * <p>
     * Create a {@link BigFraction} given the numerator and denominator as simple
     * <tt>int</tt>. The {@link BigFraction} is reduced to lowest terms.
     * </p>
     *
     * @param num
     *            the numerator.
     * @param den
     *            the denominator.
     */
    public BigFraction(final int num, final int den) {
        this(BigInteger.valueOf(num), BigInteger.valueOf(den));
    }

    /**
     * <p>
     * Create a {@link BigFraction} equivalent to the passed long, ie "num / 1".
     * </p>
     *
     * @param num
     *            the numerator.
     */
    public BigFraction(final long num) {
        this(BigInteger.valueOf(num), BigInteger.ONE);
    }

    /**
     * <p>
     * Create a {@link BigFraction} given the numerator and denominator as simple
     * <tt>long</tt>. The {@link BigFraction} is reduced to lowest terms.
     * </p>
     *
     * @param num
     *            the numerator.
     * @param den
     *            the denominator.
     */
    public BigFraction(final long num, final long den) {
        this(BigInteger.valueOf(num), BigInteger.valueOf(den));
    }

    /**
     * <p>
     * Creates a <code>BigFraction</code> instance with the 2 parts of a fraction
     * Y/Z.
     * </p>
     *
     * <p>
     * Any negative signs are resolved to be on the numerator.
     * </p>
     *
     * @param numerator
     *            the numerator, for example the three in 'three sevenths'.
     * @param denominator
     *            the denominator, for example the seven in 'three sevenths'.
     * @return a new fraction instance, with the numerator and denominator
     *         reduced.
     * @throws ArithmeticException
     *             if the denominator is <code>zero</code>.
     */
    public static BigFraction getReducedFraction(final int numerator,
                                                 final int denominator) {
        if (numerator == 0) {
            return ZERO; // normalize zero.
        }

        return new BigFraction(numerator, denominator);
    }

    /**
     * <p>
     * Returns the absolute value of this {@link BigFraction}.
     * </p>
     *
     * @return the absolute value as a {@link BigFraction}.
     */
    public BigFraction abs() {
        return (BigInteger.ZERO.compareTo(numerator) <= 0) ? this : negate();
    }

    /**
     * <p>
     * Adds the value of this fraction to the passed {@link BigInteger},
     * returning the result in reduced form.
     * </p>
     *
     * @param bg
     *            the {@link BigInteger} to add, must'nt be <code>null</code>.
     * @return a <code>BigFraction</code> instance with the resulting values.
     * @throws NullArgumentException
     *             if the {@link BigInteger} is <code>null</code>.
     */
    public BigFraction add(final BigInteger bg) throws NullArgumentException {
        MathUtils.checkNotNull(bg);
        return new BigFraction(numerator.add(denominator.multiply(bg)), denominator);
    }

    /**
     * <p>
     * Adds the value of this fraction to the passed <tt>integer</tt>, returning
     * the result in reduced form.
     * </p>
     *
     * @param i
     *            the <tt>integer</tt> to add.
     * @return a <code>BigFraction</code> instance with the resulting values.
     */
    public BigFraction add(final int i) {
        return add(BigInteger.valueOf(i));
    }

    /**
     * <p>
     * Adds the value of this fraction to the passed <tt>long</tt>, returning
     * the result in reduced form.
     * </p>
     *
     * @param l
     *            the <tt>long</tt> to add.
     * @return a <code>BigFraction</code> instance with the resulting values.
     */
    public BigFraction add(final long l) {
        return add(BigInteger.valueOf(l));
    }

    /**
     * <p>
     * Adds the value of this fraction to another, returning the result in
     * reduced form.
     * </p>
     *
     * @param fraction
     *            the {@link BigFraction} to add, must not be <code>null</code>.
     * @return a {@link BigFraction} instance with the resulting values.
     * @throws NullArgumentException if the {@link BigFraction} is {@code null}.
     */
    public BigFraction add(final BigFraction fraction) {
        if (fraction == null) {
            throw new NullArgumentException(LocalizedFormats.FRACTION);
        }
        if (ZERO.equals(fraction)) {
            return this;
        }

        BigInteger num = null;
        BigInteger den = null;

        if (denominator.equals(fraction.denominator)) {
            num = numerator.add(fraction.numerator);
            den = denominator;
        } else {
            num = (numerator.multiply(fraction.denominator)).add((fraction.numerator).multiply(denominator));
            den = denominator.multiply(fraction.denominator);
        }
        return new BigFraction(num, den);

    }

    /**
     * <p>
     * Gets the fraction as a <code>BigDecimal</code>. This calculates the
     * fraction as the numerator divided by denominator.
     * </p>
     *
     * @return the fraction as a <code>BigDecimal</code>.
     * @throws ArithmeticException
     *             if the exact quotient does not have a terminating decimal
     *             expansion.
     * @see BigDecimal
     */
    public BigDecimal bigDecimalValue() {
        return new BigDecimal(numerator).divide(new BigDecimal(denominator));
    }

    /**
     * <p>
     * Gets the fraction as a <code>BigDecimal</code> following the passed
     * rounding mode. This calculates the fraction as the numerator divided by
     * denominator.
     * </p>
     *
     * @param roundingMode
     *            rounding mode to apply. see {@link BigDecimal} constants.
     * @return the fraction as a <code>BigDecimal</code>.
     * @throws IllegalArgumentException
     *             if <tt>roundingMode</tt> does not represent a valid rounding
     *             mode.
     * @see BigDecimal
     */
    public BigDecimal bigDecimalValue(final int roundingMode) {
        return new BigDecimal(numerator).divide(new BigDecimal(denominator), roundingMode);
    }

    /**
     * <p>
     * Gets the fraction as a <code>BigDecimal</code> following the passed scale
     * and rounding mode. This calculates the fraction as the numerator divided
     * by denominator.
     * </p>
     *
     * @param scale
     *            scale of the <code>BigDecimal</code> quotient to be returned.
     *            see {@link BigDecimal} for more information.
     * @param roundingMode
     *            rounding mode to apply. see {@link BigDecimal} constants.
     * @return the fraction as a <code>BigDecimal</code>.
     * @see BigDecimal
     */
    public BigDecimal bigDecimalValue(final int scale, final int roundingMode) {
        return new BigDecimal(numerator).divide(new BigDecimal(denominator), scale, roundingMode);
    }

    /**
     * <p>
     * Compares this object to another based on size.
     * </p>
     *
     * @param object
     *            the object to compare to, must not be <code>null</code>.
     * @return -1 if this is less than <tt>object</tt>, +1 if this is greater
     *         than <tt>object</tt>, 0 if they are equal.
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(final BigFraction object) {
        BigInteger nOd = numerator.multiply(object.denominator);
        BigInteger dOn = denominator.multiply(object.numerator);
        return nOd.compareTo(dOn);
    }

    /**
     * <p>
     * Divide the value of this fraction by the passed {@code BigInteger},
     * ie {@code this * 1 / bg}, returning the result in reduced form.
     * </p>
     *
     * @param bg the {@code BigInteger} to divide by, must not be {@code null}
     * @return a {@link BigFraction} instance with the resulting values
     * @throws NullArgumentException if the {@code BigInteger} is {@code null}
     * @throws MathArithmeticException if the fraction to divide by is zero
     */
    public BigFraction divide(final BigInteger bg) {
        if (bg == null) {
            throw new NullArgumentException(LocalizedFormats.FRACTION);
        }
        if (BigInteger.ZERO.equals(bg)) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_DENOMINATOR);
        }
        return new BigFraction(numerator, denominator.multiply(bg));
    }

    /**
     * <p>
     * Divide the value of this fraction by the passed {@code int}, ie
     * {@code this * 1 / i}, returning the result in reduced form.
     * </p>
     *
     * @param i the {@code int} to divide by
     * @return a {@link BigFraction} instance with the resulting values
     * @throws MathArithmeticException if the fraction to divide by is zero
     */
    public BigFraction divide(final int i) {
        return divide(BigInteger.valueOf(i));
    }

    /**
     * <p>
     * Divide the value of this fraction by the passed {@code long}, ie
     * {@code this * 1 / l}, returning the result in reduced form.
     * </p>
     *
     * @param l the {@code long} to divide by
     * @return a {@link BigFraction} instance with the resulting values
     * @throws MathArithmeticException if the fraction to divide by is zero
     */
    public BigFraction divide(final long l) {
        return divide(BigInteger.valueOf(l));
    }

    /**
     * <p>
     * Divide the value of this fraction by another, returning the result in
     * reduced form.
     * </p>
     *
     * @param fraction Fraction to divide by, must not be {@code null}.
     * @return a {@link BigFraction} instance with the resulting values.
     * @throws NullArgumentException if the {@code fraction} is {@code null}.
     * @throws MathArithmeticException if the fraction to divide by is zero
     */
    public BigFraction divide(final BigFraction fraction) {
        if (fraction == null) {
            throw new NullArgumentException(LocalizedFormats.FRACTION);
        }
        if (BigInteger.ZERO.equals(fraction.numerator)) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_DENOMINATOR);
        }

        return multiply(fraction.reciprocal());
    }

    /**
     * <p>
     * Gets the fraction as a <tt>double</tt>. This calculates the fraction as
     * the numerator divided by denominator.
     * </p>
     *
     * @return the fraction as a <tt>double</tt>
     * @see java.lang.Number#doubleValue()
     */
    @Override
    public double doubleValue() {
        double result = numerator.doubleValue() / denominator.doubleValue();
        if (Double.isNaN(result)) {
            // Numerator and/or denominator must be out of range:
            // Calculate how far to shift them to put them in range.
            int shift = Math.max(numerator.bitLength(),
                                 denominator.bitLength()) - FastMath.getExponent(Double.MAX_VALUE);
            result = numerator.shiftRight(shift).doubleValue() /
                denominator.shiftRight(shift).doubleValue();
        }
        return result;
    }

    /**
     * <p>
     * Test for the equality of two fractions. If the lowest term numerator and
     * denominators are the same for both fractions, the two fractions are
     * considered to be equal.
     * </p>
     *
     * @param other
     *            fraction to test for equality to this fraction, can be
     *            <code>null</code>.
     * @return true if two fractions are equal, false if object is
     *         <code>null</code>, not an instance of {@link BigFraction}, or not
     *         equal to this fraction instance.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object other) {
        boolean ret = false;

        if (this == other) {
            ret = true;
        } else if (other instanceof BigFraction) {
            BigFraction rhs = ((BigFraction) other).reduce();
            BigFraction thisOne = this.reduce();
            ret = thisOne.numerator.equals(rhs.numerator) && thisOne.denominator.equals(rhs.denominator);
        }

        return ret;
    }

    /**
     * <p>
     * Gets the fraction as a <tt>float</tt>. This calculates the fraction as
     * the numerator divided by denominator.
     * </p>
     *
     * @return the fraction as a <tt>float</tt>.
     * @see java.lang.Number#floatValue()
     */
    @Override
    public float floatValue() {
        float result = numerator.floatValue() / denominator.floatValue();
        if (Double.isNaN(result)) {
            // Numerator and/or denominator must be out of range:
            // Calculate how far to shift them to put them in range.
            int shift = Math.max(numerator.bitLength(),
                                 denominator.bitLength()) - FastMath.getExponent(Float.MAX_VALUE);
            result = numerator.shiftRight(shift).floatValue() /
                denominator.shiftRight(shift).floatValue();
        }
        return result;
    }

    /**
     * <p>
     * Access the denominator as a <code>BigInteger</code>.
     * </p>
     *
     * @return the denominator as a <code>BigInteger</code>.
     */
    public BigInteger getDenominator() {
        return denominator;
    }

    /**
     * <p>
     * Access the denominator as a <tt>int</tt>.
     * </p>
     *
     * @return the denominator as a <tt>int</tt>.
     */
    public int getDenominatorAsInt() {
        return denominator.intValue();
    }

    /**
     * <p>
     * Access the denominator as a <tt>long</tt>.
     * </p>
     *
     * @return the denominator as a <tt>long</tt>.
     */
    public long getDenominatorAsLong() {
        return denominator.longValue();
    }

    /**
     * <p>
     * Access the numerator as a <code>BigInteger</code>.
     * </p>
     *
     * @return the numerator as a <code>BigInteger</code>.
     */
    public BigInteger getNumerator() {
        return numerator;
    }

    /**
     * <p>
     * Access the numerator as a <tt>int</tt>.
     * </p>
     *
     * @return the numerator as a <tt>int</tt>.
     */
    public int getNumeratorAsInt() {
        return numerator.intValue();
    }

    /**
     * <p>
     * Access the numerator as a <tt>long</tt>.
     * </p>
     *
     * @return the numerator as a <tt>long</tt>.
     */
    public long getNumeratorAsLong() {
        return numerator.longValue();
    }

    /**
     * <p>
     * Gets a hashCode for the fraction.
     * </p>
     *
     * @return a hash code value for this object.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return 37 * (37 * 17 + numerator.hashCode()) + denominator.hashCode();
    }

    /**
     * <p>
     * Gets the fraction as an <tt>int</tt>. This returns the whole number part
     * of the fraction.
     * </p>
     *
     * @return the whole number fraction part.
     * @see java.lang.Number#intValue()
     */
    @Override
    public int intValue() {
        return numerator.divide(denominator).intValue();
    }

    /**
     * <p>
     * Gets the fraction as a <tt>long</tt>. This returns the whole number part
     * of the fraction.
     * </p>
     *
     * @return the whole number fraction part.
     * @see java.lang.Number#longValue()
     */
    @Override
    public long longValue() {
        return numerator.divide(denominator).longValue();
    }

    /**
     * <p>
     * Multiplies the value of this fraction by the passed
     * <code>BigInteger</code>, returning the result in reduced form.
     * </p>
     *
     * @param bg the {@code BigInteger} to multiply by.
     * @return a {@code BigFraction} instance with the resulting values.
     * @throws NullArgumentException if {@code bg} is {@code null}.
     */
    public BigFraction multiply(final BigInteger bg) {
        if (bg == null) {
            throw new NullArgumentException();
        }
        return new BigFraction(bg.multiply(numerator), denominator);
    }

    /**
     * <p>
     * Multiply the value of this fraction by the passed <tt>int</tt>, returning
     * the result in reduced form.
     * </p>
     *
     * @param i
     *            the <tt>int</tt> to multiply by.
     * @return a {@link BigFraction} instance with the resulting values.
     */
    public BigFraction multiply(final int i) {
        return multiply(BigInteger.valueOf(i));
    }

    /**
     * <p>
     * Multiply the value of this fraction by the passed <tt>long</tt>,
     * returning the result in reduced form.
     * </p>
     *
     * @param l
     *            the <tt>long</tt> to multiply by.
     * @return a {@link BigFraction} instance with the resulting values.
     */
    public BigFraction multiply(final long l) {
        return multiply(BigInteger.valueOf(l));
    }

    /**
     * <p>
     * Multiplies the value of this fraction by another, returning the result in
     * reduced form.
     * </p>
     *
     * @param fraction Fraction to multiply by, must not be {@code null}.
     * @return a {@link BigFraction} instance with the resulting values.
     * @throws NullArgumentException if {@code fraction} is {@code null}.
     */
    public BigFraction multiply(final BigFraction fraction) {
        if (fraction == null) {
            throw new NullArgumentException(LocalizedFormats.FRACTION);
        }
        if (numerator.equals(BigInteger.ZERO) ||
            fraction.numerator.equals(BigInteger.ZERO)) {
            return ZERO;
        }
        return new BigFraction(numerator.multiply(fraction.numerator),
                               denominator.multiply(fraction.denominator));
    }

    /**
     * <p>
     * Return the additive inverse of this fraction, returning the result in
     * reduced form.
     * </p>
     *
     * @return the negation of this fraction.
     */
    public BigFraction negate() {
        return new BigFraction(numerator.negate(), denominator);
    }

    /**
     * <p>
     * Gets the fraction percentage as a <tt>double</tt>. This calculates the
     * fraction as the numerator divided by denominator multiplied by 100.
     * </p>
     *
     * @return the fraction percentage as a <tt>double</tt>.
     */
    public double percentageValue() {
        return multiply(ONE_HUNDRED).doubleValue();
    }

    /**
     * <p>
     * Returns a {@code BigFraction} whose value is
     * {@code (this<sup>exponent</sup>)}, returning the result in reduced form.
     * </p>
     *
     * @param exponent
     *            exponent to which this {@code BigFraction} is to be
     *            raised.
     * @return <tt>this<sup>exponent</sup></tt>.
     */
    public BigFraction pow(final int exponent) {
        if (exponent < 0) {
            return new BigFraction(denominator.pow(-exponent), numerator.pow(-exponent));
        }
        return new BigFraction(numerator.pow(exponent), denominator.pow(exponent));
    }

    /**
     * <p>
     * Returns a <code>BigFraction</code> whose value is
     * <tt>(this<sup>exponent</sup>)</tt>, returning the result in reduced form.
     * </p>
     *
     * @param exponent
     *            exponent to which this <code>BigFraction</code> is to be raised.
     * @return <tt>this<sup>exponent</sup></tt> as a <code>BigFraction</code>.
     */
    public BigFraction pow(final long exponent) {
        if (exponent < 0) {
            return new BigFraction(ArithmeticUtils.pow(denominator, -exponent),
                                   ArithmeticUtils.pow(numerator,   -exponent));
        }
        return new BigFraction(ArithmeticUtils.pow(numerator,   exponent),
                               ArithmeticUtils.pow(denominator, exponent));
    }

    /**
     * <p>
     * Returns a <code>BigFraction</code> whose value is
     * <tt>(this<sup>exponent</sup>)</tt>, returning the result in reduced form.
     * </p>
     *
     * @param exponent
     *            exponent to which this <code>BigFraction</code> is to be raised.
     * @return <tt>this<sup>exponent</sup></tt> as a <code>BigFraction</code>.
     */
    public BigFraction pow(final BigInteger exponent) {
        if (exponent.compareTo(BigInteger.ZERO) < 0) {
            final BigInteger eNeg = exponent.negate();
            return new BigFraction(ArithmeticUtils.pow(denominator, eNeg),
                                   ArithmeticUtils.pow(numerator,   eNeg));
        }
        return new BigFraction(ArithmeticUtils.pow(numerator,   exponent),
                               ArithmeticUtils.pow(denominator, exponent));
    }

    /**
     * <p>
     * Returns a <code>double</code> whose value is
     * <tt>(this<sup>exponent</sup>)</tt>, returning the result in reduced form.
     * </p>
     *
     * @param exponent
     *            exponent to which this <code>BigFraction</code> is to be raised.
     * @return <tt>this<sup>exponent</sup></tt>.
     */
    public double pow(final double exponent) {
        return FastMath.pow(numerator.doubleValue(),   exponent) /
               FastMath.pow(denominator.doubleValue(), exponent);
    }

    /**
     * <p>
     * Return the multiplicative inverse of this fraction.
     * </p>
     *
     * @return the reciprocal fraction.
     */
    public BigFraction reciprocal() {
        return new BigFraction(denominator, numerator);
    }

    /**
     * <p>
     * Reduce this <code>BigFraction</code> to its lowest terms.
     * </p>
     *
     * @return the reduced <code>BigFraction</code>. It doesn't change anything if
     *         the fraction can be reduced.
     */
    public BigFraction reduce() {
        final BigInteger gcd = numerator.gcd(denominator);
        return new BigFraction(numerator.divide(gcd), denominator.divide(gcd));
    }

    /**
     * <p>
     * Subtracts the value of an {@link BigInteger} from the value of this
     * {@code BigFraction}, returning the result in reduced form.
     * </p>
     *
     * @param bg the {@link BigInteger} to subtract, cannot be {@code null}.
     * @return a {@code BigFraction} instance with the resulting values.
     * @throws NullArgumentException if the {@link BigInteger} is {@code null}.
     */
    public BigFraction subtract(final BigInteger bg) {
        if (bg == null) {
            throw new NullArgumentException();
        }
        return new BigFraction(numerator.subtract(denominator.multiply(bg)), denominator);
    }

    /**
     * <p>
     * Subtracts the value of an {@code integer} from the value of this
     * {@code BigFraction}, returning the result in reduced form.
     * </p>
     *
     * @param i the {@code integer} to subtract.
     * @return a {@code BigFraction} instance with the resulting values.
     */
    public BigFraction subtract(final int i) {
        return subtract(BigInteger.valueOf(i));
    }

    /**
     * <p>
     * Subtracts the value of a {@code long} from the value of this
     * {@code BigFraction}, returning the result in reduced form.
     * </p>
     *
     * @param l the {@code long} to subtract.
     * @return a {@code BigFraction} instance with the resulting values.
     */
    public BigFraction subtract(final long l) {
        return subtract(BigInteger.valueOf(l));
    }

    /**
     * <p>
     * Subtracts the value of another fraction from the value of this one,
     * returning the result in reduced form.
     * </p>
     *
     * @param fraction {@link BigFraction} to subtract, must not be {@code null}.
     * @return a {@link BigFraction} instance with the resulting values
     * @throws NullArgumentException if the {@code fraction} is {@code null}.
     */
    public BigFraction subtract(final BigFraction fraction) {
        if (fraction == null) {
            throw new NullArgumentException(LocalizedFormats.FRACTION);
        }
        if (ZERO.equals(fraction)) {
            return this;
        }

        BigInteger num = null;
        BigInteger den = null;
        if (denominator.equals(fraction.denominator)) {
            num = numerator.subtract(fraction.numerator);
            den = denominator;
        } else {
            num = (numerator.multiply(fraction.denominator)).subtract((fraction.numerator).multiply(denominator));
            den = denominator.multiply(fraction.denominator);
        }
        return new BigFraction(num, den);

    }

    /**
     * <p>
     * Returns the <code>String</code> representing this fraction, ie
     * "num / dem" or just "num" if the denominator is one.
     * </p>
     *
     * @return a string representation of the fraction.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String str = null;
        if (BigInteger.ONE.equals(denominator)) {
            str = numerator.toString();
        } else if (BigInteger.ZERO.equals(numerator)) {
            str = "0";
        } else {
            str = numerator + " / " + denominator;
        }
        return str;
    }

    /** {@inheritDoc} */
    public BigFractionField getField() {
        return BigFractionField.getInstance();
    }

}

```

## src/main/java/org/apache/commons/math3/fraction/Fraction.java

```
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.math3.fraction;

import java.io.Serializable;
import java.math.BigInteger;

import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.util.ArithmeticUtils;
import org.apache.commons.math3.util.FastMath;

/**
 * Representation of a rational number.
 *
 * implements Serializable since 2.0
 *
 * @since 1.1
 * @version $Id$
 */
public class Fraction
    extends Number
    implements FieldElement<Fraction>, Comparable<Fraction>, Serializable {

    /** A fraction representing "2 / 1". */
    public static final Fraction TWO = new Fraction(2, 1);

    /** A fraction representing "1". */
    public static final Fraction ONE = new Fraction(1, 1);

    /** A fraction representing "0". */
    public static final Fraction ZERO = new Fraction(0, 1);

    /** A fraction representing "4/5". */
    public static final Fraction FOUR_FIFTHS = new Fraction(4, 5);

    /** A fraction representing "1/5". */
    public static final Fraction ONE_FIFTH = new Fraction(1, 5);

    /** A fraction representing "1/2". */
    public static final Fraction ONE_HALF = new Fraction(1, 2);

    /** A fraction representing "1/4". */
    public static final Fraction ONE_QUARTER = new Fraction(1, 4);

    /** A fraction representing "1/3". */
    public static final Fraction ONE_THIRD = new Fraction(1, 3);

    /** A fraction representing "3/5". */
    public static final Fraction THREE_FIFTHS = new Fraction(3, 5);

    /** A fraction representing "3/4". */
    public static final Fraction THREE_QUARTERS = new Fraction(3, 4);

    /** A fraction representing "2/5". */
    public static final Fraction TWO_FIFTHS = new Fraction(2, 5);

    /** A fraction representing "2/4". */
    public static final Fraction TWO_QUARTERS = new Fraction(2, 4);

    /** A fraction representing "2/3". */
    public static final Fraction TWO_THIRDS = new Fraction(2, 3);

    /** A fraction representing "-1 / 1". */
    public static final Fraction MINUS_ONE = new Fraction(-1, 1);

    /** Serializable version identifier */
    private static final long serialVersionUID = 3698073679419233275L;

    /** The default epsilon used for convergence. */
    private static final double DEFAULT_EPSILON = 1e-5;

    /** The denominator. */
    private final int denominator;

    /** The numerator. */
    private final int numerator;

    /**
     * Create a fraction given the double value.
     * @param value the double value to convert to a fraction.
     * @throws FractionConversionException if the continued fraction failed to
     *         converge.
     */
    public Fraction(double value) throws FractionConversionException {
        this(value, DEFAULT_EPSILON, 100);
    }

    /**
     * Create a fraction given the double value and maximum error allowed.
     * <p>
     * References:
     * <ul>
     * <li><a href="http://mathworld.wolfram.com/ContinuedFraction.html">
     * Continued Fraction</a> equations (11) and (22)-(26)</li>
     * </ul>
     * </p>
     * @param value the double value to convert to a fraction.
     * @param epsilon maximum error allowed.  The resulting fraction is within
     *        {@code epsilon} of {@code value}, in absolute terms.
     * @param maxIterations maximum number of convergents
     * @throws FractionConversionException if the continued fraction failed to
     *         converge.
     */
    public Fraction(double value, double epsilon, int maxIterations)
        throws FractionConversionException
    {
        this(value, epsilon, Integer.MAX_VALUE, maxIterations);
    }

    /**
     * Create a fraction given the double value and maximum denominator.
     * <p>
     * References:
     * <ul>
     * <li><a href="http://mathworld.wolfram.com/ContinuedFraction.html">
     * Continued Fraction</a> equations (11) and (22)-(26)</li>
     * </ul>
     * </p>
     * @param value the double value to convert to a fraction.
     * @param maxDenominator The maximum allowed value for denominator
     * @throws FractionConversionException if the continued fraction failed to
     *         converge
     */
    public Fraction(double value, int maxDenominator)
        throws FractionConversionException
    {
       this(value, 0, maxDenominator, 100);
    }

    /**
     * Create a fraction given the double value and either the maximum error
     * allowed or the maximum number of denominator digits.
     * <p>
     *
     * NOTE: This constructor is called with EITHER
     *   - a valid epsilon value and the maxDenominator set to Integer.MAX_VALUE
     *     (that way the maxDenominator has no effect).
     * OR
     *   - a valid maxDenominator value and the epsilon value set to zero
     *     (that way epsilon only has effect if there is an exact match before
     *     the maxDenominator value is reached).
     * </p><p>
     *
     * It has been done this way so that the same code can be (re)used for both
     * scenarios. However this could be confusing to users if it were part of
     * the public API and this constructor should therefore remain PRIVATE.
     * </p>
     *
     * See JIRA issue ticket MATH-181 for more details:
     *
     *     https://issues.apache.org/jira/browse/MATH-181
     *
     * @param value the double value to convert to a fraction.
     * @param epsilon maximum error allowed.  The resulting fraction is within
     *        {@code epsilon} of {@code value}, in absolute terms.
     * @param maxDenominator maximum denominator value allowed.
     * @param maxIterations maximum number of convergents
     * @throws FractionConversionException if the continued fraction failed to
     *         converge.
     */
    private Fraction(double value, double epsilon, int maxDenominator, int maxIterations)
        throws FractionConversionException
    {
        long overflow = Integer.MAX_VALUE;
        double r0 = value;
        long a0 = (long)FastMath.floor(r0);
        if (FastMath.abs(a0) > overflow) {
            throw new FractionConversionException(value, a0, 1l);
        }

        // check for (almost) integer arguments, which should not go to iterations.
        if (FastMath.abs(a0 - value) < epsilon) {
            this.numerator = (int) a0;
            this.denominator = 1;
            return;
        }

        long p0 = 1;
        long q0 = 0;
        long p1 = a0;
        long q1 = 1;

        long p2 = 0;
        long q2 = 1;

        int n = 0;
        boolean stop = false;
        do {
            ++n;
            double r1 = 1.0 / (r0 - a0);
            long a1 = (long)FastMath.floor(r1);
            p2 = (a1 * p1) + p0;
            q2 = (a1 * q1) + q0;

            if ((FastMath.abs(p2) > overflow) || (FastMath.abs(q2) > overflow)) {
                // in maxDenominator mode, if the last fraction was very close to the actual value
                // q2 may overflow in the next iteration; in this case return the last one.
                if (epsilon == 0.0 && FastMath.abs(q1) < maxDenominator) {
                    break;
                }
                throw new FractionConversionException(value, p2, q2);
            }

            double convergent = (double)p2 / (double)q2;
            if (n < maxIterations && FastMath.abs(convergent - value) > epsilon && q2 < maxDenominator) {
                p0 = p1;
                p1 = p2;
                q0 = q1;
                q1 = q2;
                a0 = a1;
                r0 = r1;
            } else {
                stop = true;
            }
        } while (!stop);

        if (n >= maxIterations) {
            throw new FractionConversionException(value, maxIterations);
        }

        if (q2 < maxDenominator) {
            this.numerator = (int) p2;
            this.denominator = (int) q2;
        } else {
            this.numerator = (int) p1;
            this.denominator = (int) q1;
        }

    }

    /**
     * Create a fraction from an int.
     * The fraction is num / 1.
     * @param num the numerator.
     */
    public Fraction(int num) {
        this(num, 1);
    }

    /**
     * Create a fraction given the numerator and denominator.  The fraction is
     * reduced to lowest terms.
     * @param num the numerator.
     * @param den the denominator.
     * @throws MathArithmeticException if the denominator is {@code zero}
     */
    public Fraction(int num, int den) {
        if (den == 0) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_DENOMINATOR_IN_FRACTION,
                                              num, den);
        }
        if (den < 0) {
            if (num == Integer.MIN_VALUE ||
                den == Integer.MIN_VALUE) {
                throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_FRACTION,
                                                  num, den);
            }
            num = -num;
            den = -den;
        }
        // reduce numerator and denominator by greatest common denominator.
        final int d = ArithmeticUtils.gcd(num, den);
        if (d > 1) {
            num /= d;
            den /= d;
        }

        // move sign to numerator.
        if (den < 0) {
            num = -num;
            den = -den;
        }
        this.numerator   = num;
        this.denominator = den;
    }

    /**
     * Returns the absolute value of this fraction.
     * @return the absolute value.
     */
    public Fraction abs() {
        Fraction ret;
        if (numerator >= 0) {
            ret = this;
        } else {
            ret = negate();
        }
        return ret;
    }

    /**
     * Compares this object to another based on size.
     * @param object the object to compare to
     * @return -1 if this is less than <tt>object</tt>, +1 if this is greater
     *         than <tt>object</tt>, 0 if they are equal.
     */
    public int compareTo(Fraction object) {
        long nOd = ((long) numerator) * object.denominator;
        long dOn = ((long) denominator) * object.numerator;
        return (nOd < dOn) ? -1 : ((nOd > dOn) ? +1 : 0);
    }

    /**
     * Gets the fraction as a <tt>double</tt>. This calculates the fraction as
     * the numerator divided by denominator.
     * @return the fraction as a <tt>double</tt>
     */
    @Override
    public double doubleValue() {
        return (double)numerator / (double)denominator;
    }

    /**
     * Test for the equality of two fractions.  If the lowest term
     * numerator and denominators are the same for both fractions, the two
     * fractions are considered to be equal.
     * @param other fraction to test for equality to this fraction
     * @return true if two fractions are equal, false if object is
     *         <tt>null</tt>, not an instance of {@link Fraction}, or not equal
     *         to this fraction instance.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof Fraction) {
            // since fractions are always in lowest terms, numerators and
            // denominators can be compared directly for equality.
            Fraction rhs = (Fraction)other;
            return (numerator == rhs.numerator) &&
                (denominator == rhs.denominator);
        }
        return false;
    }

    /**
     * Gets the fraction as a <tt>float</tt>. This calculates the fraction as
     * the numerator divided by denominator.
     * @return the fraction as a <tt>float</tt>
     */
    @Override
    public float floatValue() {
        return (float)doubleValue();
    }

    /**
     * Access the denominator.
     * @return the denominator.
     */
    public int getDenominator() {
        return denominator;
    }

    /**
     * Access the numerator.
     * @return the numerator.
     */
    public int getNumerator() {
        return numerator;
    }

    /**
     * Gets a hashCode for the fraction.
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return 37 * (37 * 17 + numerator) + denominator;
    }

    /**
     * Gets the fraction as an <tt>int</tt>. This returns the whole number part
     * of the fraction.
     * @return the whole number fraction part
     */
    @Override
    public int intValue() {
        return (int)doubleValue();
    }

    /**
     * Gets the fraction as a <tt>long</tt>. This returns the whole number part
     * of the fraction.
     * @return the whole number fraction part
     */
    @Override
    public long longValue() {
        return (long)doubleValue();
    }

    /**
     * Return the additive inverse of this fraction.
     * @return the negation of this fraction.
     */
    public Fraction negate() {
        if (numerator==Integer.MIN_VALUE) {
            throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_FRACTION, numerator, denominator);
        }
        return new Fraction(-numerator, denominator);
    }

    /**
     * Return the multiplicative inverse of this fraction.
     * @return the reciprocal fraction
     */
    public Fraction reciprocal() {
        return new Fraction(denominator, numerator);
    }

    /**
     * <p>Adds the value of this fraction to another, returning the result in reduced form.
     * The algorithm follows Knuth, 4.5.1.</p>
     *
     * @param fraction  the fraction to add, must not be {@code null}
     * @return a {@code Fraction} instance with the resulting values
     * @throws NullArgumentException if the fraction is {@code null}
     * @throws MathArithmeticException if the resulting numerator or denominator exceeds
     *  {@code Integer.MAX_VALUE}
     */
    public Fraction add(Fraction fraction) {
        return addSub(fraction, true /* add */);
    }

    /**
     * Add an integer to the fraction.
     * @param i the <tt>integer</tt> to add.
     * @return this + i
     */
    public Fraction add(final int i) {
        return new Fraction(numerator + i * denominator, denominator);
    }

    /**
     * <p>Subtracts the value of another fraction from the value of this one,
     * returning the result in reduced form.</p>
     *
     * @param fraction  the fraction to subtract, must not be {@code null}
     * @return a {@code Fraction} instance with the resulting values
     * @throws NullArgumentException if the fraction is {@code null}
     * @throws MathArithmeticException if the resulting numerator or denominator
     *   cannot be represented in an {@code int}.
     */
    public Fraction subtract(Fraction fraction) {
        return addSub(fraction, false /* subtract */);
    }

    /**
     * Subtract an integer from the fraction.
     * @param i the <tt>integer</tt> to subtract.
     * @return this - i
     */
    public Fraction subtract(final int i) {
        return new Fraction(numerator - i * denominator, denominator);
    }

    /**
     * Implement add and subtract using algorithm described in Knuth 4.5.1.
     *
     * @param fraction the fraction to subtract, must not be {@code null}
     * @param isAdd true to add, false to subtract
     * @return a {@code Fraction} instance with the resulting values
     * @throws NullArgumentException if the fraction is {@code null}
     * @throws MathArithmeticException if the resulting numerator or denominator
     *   cannot be represented in an {@code int}.
     */
    private Fraction addSub(Fraction fraction, boolean isAdd) {
        if (fraction == null) {
            throw new NullArgumentException(LocalizedFormats.FRACTION);
        }
        // zero is identity for addition.
        if (numerator == 0) {
            return isAdd ? fraction : fraction.negate();
        }
        if (fraction.numerator == 0) {
            return this;
        }
        // if denominators are randomly distributed, d1 will be 1 about 61%
        // of the time.
        int d1 = ArithmeticUtils.gcd(denominator, fraction.denominator);
        if (d1==1) {
            // result is ( (u*v' +/- u'v) / u'v')
            int uvp = ArithmeticUtils.mulAndCheck(numerator, fraction.denominator);
            int upv = ArithmeticUtils.mulAndCheck(fraction.numerator, denominator);
            return new Fraction
                (isAdd ? ArithmeticUtils.addAndCheck(uvp, upv) :
                 ArithmeticUtils.subAndCheck(uvp, upv),
                 ArithmeticUtils.mulAndCheck(denominator, fraction.denominator));
        }
        // the quantity 't' requires 65 bits of precision; see knuth 4.5.1
        // exercise 7.  we're going to use a BigInteger.
        // t = u(v'/d1) +/- v(u'/d1)
        BigInteger uvp = BigInteger.valueOf(numerator)
        .multiply(BigInteger.valueOf(fraction.denominator/d1));
        BigInteger upv = BigInteger.valueOf(fraction.numerator)
        .multiply(BigInteger.valueOf(denominator/d1));
        BigInteger t = isAdd ? uvp.add(upv) : uvp.subtract(upv);
        // but d2 doesn't need extra precision because
        // d2 = gcd(t,d1) = gcd(t mod d1, d1)
        int tmodd1 = t.mod(BigInteger.valueOf(d1)).intValue();
        int d2 = (tmodd1==0)?d1:ArithmeticUtils.gcd(tmodd1, d1);

        // result is (t/d2) / (u'/d1)(v'/d2)
        BigInteger w = t.divide(BigInteger.valueOf(d2));
        if (w.bitLength() > 31) {
            throw new MathArithmeticException(LocalizedFormats.NUMERATOR_OVERFLOW_AFTER_MULTIPLY,
                                              w);
        }
        return new Fraction (w.intValue(),
                ArithmeticUtils.mulAndCheck(denominator/d1,
                        fraction.denominator/d2));
    }

    /**
     * <p>Multiplies the value of this fraction by another, returning the
     * result in reduced form.</p>
     *
     * @param fraction  the fraction to multiply by, must not be {@code null}
     * @return a {@code Fraction} instance with the resulting values
     * @throws NullArgumentException if the fraction is {@code null}
     * @throws MathArithmeticException if the resulting numerator or denominator exceeds
     *  {@code Integer.MAX_VALUE}
     */
    public Fraction multiply(Fraction fraction) {
        if (fraction == null) {
            throw new NullArgumentException(LocalizedFormats.FRACTION);
        }
        if (numerator == 0 || fraction.numerator == 0) {
            return ZERO;
        }
        // knuth 4.5.1
        // make sure we don't overflow unless the result *must* overflow.
        int d1 = ArithmeticUtils.gcd(numerator, fraction.denominator);
        int d2 = ArithmeticUtils.gcd(fraction.numerator, denominator);
        return getReducedFraction
        (ArithmeticUtils.mulAndCheck(numerator/d1, fraction.numerator/d2),
                ArithmeticUtils.mulAndCheck(denominator/d2, fraction.denominator/d1));
    }

    /**
     * Multiply the fraction by an integer.
     * @param i the <tt>integer</tt> to multiply by.
     * @return this * i
     */
    public Fraction multiply(final int i) {
        return new Fraction(numerator * i, denominator);
    }

    /**
     * <p>Divide the value of this fraction by another.</p>
     *
     * @param fraction  the fraction to divide by, must not be {@code null}
     * @return a {@code Fraction} instance with the resulting values
     * @throws IllegalArgumentException if the fraction is {@code null}
     * @throws MathArithmeticException if the fraction to divide by is zero
     * @throws MathArithmeticException if the resulting numerator or denominator exceeds
     *  {@code Integer.MAX_VALUE}
     */
    public Fraction divide(Fraction fraction) {
        if (fraction == null) {
            throw new NullArgumentException(LocalizedFormats.FRACTION);
        }
        if (fraction.numerator == 0) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_FRACTION_TO_DIVIDE_BY,
                                              fraction.numerator, fraction.denominator);
        }
        return multiply(fraction.reciprocal());
    }

    /**
     * Divide the fraction by an integer.
     * @param i the <tt>integer</tt> to divide by.
     * @return this * i
     */
    public Fraction divide(final int i) {
        return new Fraction(numerator, denominator * i);
    }

    /**
     * <p>
     * Gets the fraction percentage as a <tt>double</tt>. This calculates the
     * fraction as the numerator divided by denominator multiplied by 100.
     * </p>
     *
     * @return the fraction percentage as a <tt>double</tt>.
     */
    public double percentageValue() {
        return 100 * doubleValue();
    }

    /**
     * <p>Creates a {@code Fraction} instance with the 2 parts
     * of a fraction Y/Z.</p>
     *
     * <p>Any negative signs are resolved to be on the numerator.</p>
     *
     * @param numerator  the numerator, for example the three in 'three sevenths'
     * @param denominator  the denominator, for example the seven in 'three sevenths'
     * @return a new fraction instance, with the numerator and denominator reduced
     * @throws MathArithmeticException if the denominator is {@code zero}
     */
    public static Fraction getReducedFraction(int numerator, int denominator) {
        if (denominator == 0) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_DENOMINATOR_IN_FRACTION,
                                              numerator, denominator);
        }
        if (numerator==0) {
            return ZERO; // normalize zero.
        }
        // allow 2^k/-2^31 as a valid fraction (where k>0)
        if (denominator==Integer.MIN_VALUE && (numerator&1)==0) {
            numerator/=2; denominator/=2;
        }
        if (denominator < 0) {
            if (numerator==Integer.MIN_VALUE ||
                    denominator==Integer.MIN_VALUE) {
                throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_FRACTION,
                                                  numerator, denominator);
            }
            numerator = -numerator;
            denominator = -denominator;
        }
        // simplify fraction.
        int gcd = ArithmeticUtils.gcd(numerator, denominator);
        numerator /= gcd;
        denominator /= gcd;
        return new Fraction(numerator, denominator);
    }

    /**
     * <p>
     * Returns the {@code String} representing this fraction, ie
     * "num / dem" or just "num" if the denominator is one.
     * </p>
     *
     * @return a string representation of the fraction.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String str = null;
        if (denominator == 1) {
            str = Integer.toString(numerator);
        } else if (numerator == 0) {
            str = "0";
        } else {
            str = numerator + " / " + denominator;
        }
        return str;
    }

    /** {@inheritDoc} */
    public FractionField getField() {
        return FractionField.getInstance();
    }

}

```
