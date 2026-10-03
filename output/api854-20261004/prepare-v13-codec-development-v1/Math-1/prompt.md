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

Explicit fixture policy: aom-beam-champ-codec-fixtures-v13-development. Use the reviewed capability recipes below instead of legacy recursive/null construction.
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

## src/main/java/org/apache/commons/math3/fraction/BigFractionField.java

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

import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;

/**
 * Representation of the fractional numbers  without any overflow field.
 * <p>
 * This class is a singleton.
 * </p>
 * @see Fraction
 * @version $Id$
 * @since 2.0
 */
public class BigFractionField implements Field<BigFraction>, Serializable  {

    /** Serializable version identifier */
    private static final long serialVersionUID = -1699294557189741703L;

    /** Private constructor for the singleton.
     */
    private BigFractionField() {
    }

    /** Get the unique instance.
     * @return the unique instance
     */
    public static BigFractionField getInstance() {
        return LazyHolder.INSTANCE;
    }

    /** {@inheritDoc} */
    public BigFraction getOne() {
        return BigFraction.ONE;
    }

    /** {@inheritDoc} */
    public BigFraction getZero() {
        return BigFraction.ZERO;
    }

    /** {@inheritDoc} */
    public Class<? extends FieldElement<BigFraction>> getRuntimeClass() {
        return BigFraction.class;
    }
    // CHECKSTYLE: stop HideUtilityClassConstructor
    /** Holder for the instance.
     * <p>We use here the Initialization On Demand Holder Idiom.</p>
     */
    private static class LazyHolder {
        /** Cached field instance. */
        private static final BigFractionField INSTANCE = new BigFractionField();
    }
    // CHECKSTYLE: resume HideUtilityClassConstructor

    /** Handle deserialization of the singleton.
     * @return the singleton instance
     */
    private Object readResolve() {
        // return the singleton instance
        return LazyHolder.INSTANCE;
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

## src/main/java/org/apache/commons/math3/fraction/FractionField.java

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

import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;

/**
 * Representation of the fractional numbers field.
 * <p>
 * This class is a singleton.
 * </p>
 * @see Fraction
 * @version $Id$
 * @since 2.0
 */
public class FractionField implements Field<Fraction>, Serializable  {

    /** Serializable version identifier */
    private static final long serialVersionUID = -1257768487499119313L;

    /** Private constructor for the singleton.
     */
    private FractionField() {
    }

    /** Get the unique instance.
     * @return the unique instance
     */
    public static FractionField getInstance() {
        return LazyHolder.INSTANCE;
    }

    /** {@inheritDoc} */
    public Fraction getOne() {
        return Fraction.ONE;
    }

    /** {@inheritDoc} */
    public Fraction getZero() {
        return Fraction.ZERO;
    }

    /** {@inheritDoc} */
    public Class<? extends FieldElement<Fraction>> getRuntimeClass() {
        return Fraction.class;
    }
    // CHECKSTYLE: stop HideUtilityClassConstructor
    /** Holder for the instance.
     * <p>We use here the Initialization On Demand Holder Idiom.</p>
     */
    private static class LazyHolder {
        /** Cached field instance. */
        private static final FractionField INSTANCE = new FractionField();
    }
    // CHECKSTYLE: resume HideUtilityClassConstructor

    /** Handle deserialization of the singleton.
     * @return the singleton instance
     */
    private Object readResolve() {
        // return the singleton instance
        return LazyHolder.INSTANCE;
    }

}

```


Explicit fixture recipe definitions (generation support, separate from production source):
Use the same construction/projection knowledge across all four approaches. Setup failures are not target observations. Use valid receiver/dependency graphs and node kinds.
```json
{
  "fixture_policy_id": "aom-beam-champ-codec-fixtures-v13-development",
  "schema_version": 1,
  "scope": "Same fixture construction/projection knowledge for all four approaches; no execution feedback",
  "source_sha256": {
    "algorithms/java/SqaProbe.java": "8e30b54abb2d24a20ed194593badc13b892308bc6eec2353f4f1e90b1fc6fb18",
    "scripts/study/api854/fixture_policy.py": "22d5e3b3355a99bd1f503ccf47538bf8d9fafa81bae4144452ff521135052aa2"
  },
  "sources": {
    "algorithms/java/SqaProbe.java": "import java.awt.*;\nimport java.awt.geom.*;\nimport java.awt.image.BufferedImage;\nimport java.io.*;\nimport java.util.Map;\nimport java.util.LinkedHashMap;\nimport java.lang.reflect.Array;\nimport java.lang.reflect.Constructor;\nimport java.lang.reflect.InvocationTargetException;\nimport java.lang.reflect.Method;\nimport java.lang.reflect.Modifier;\nimport java.nio.charset.StandardCharsets;\nimport java.nio.file.Files;\nimport java.nio.file.Paths;\nimport java.security.MessageDigest;\nimport java.security.NoSuchAlgorithmException;\nimport java.util.ArrayList;\nimport java.util.Arrays;\nimport java.util.Base64;\nimport java.util.Comparator;\nimport java.util.List;\n\n/** Fixed-revision observations for explicitly supported, deterministic Java APIs.\n * No buggy source, patch, or triggering test is used during input generation.\n * The same source is packaged with the generated JUnit suite.\n */\npublic final class SqaProbe {\n    private static final String[] STRINGS = {\n        \"\", \"0\", \"1\", \"-1\", \"null\", \"true\", \"false\", \"abc\", \"ABC\", \" \",\n        \"0x0\", \"0x1\", \"0xFFFFFFFF\", \"1.0\", \"1e3\", \"NaN\", \"Infinity\",\n        \"{}\", \"[]\", \"[1]\", \"{\\\"a\\\":1}\", \"a=b\", \"--help\", \"-x\", \"a,b\",\n        \"1970-01-01\", \"a\\\\nb\", \"a\\nb\", \"a\\tb\", \"\\u0e17\\u0e14\\u0e2a\\u0e2d\\u0e1a\"\n    };\n    private static final long[] NUMBERS = {0, 1, -1, 2, -2, 10, -10, 127, 128,\n        255, 256, 32767, -32768, Integer.MAX_VALUE, Integer.MIN_VALUE};\n\n    private SqaProbe() { }\n\n    /** Schema scaffolding carried in the suite; no benchmark test classes. */\n    public static class GenericFixture<T> { public T value; public T[] array; public List<T> items; }\n    public static class StringBinding extends GenericFixture<String> { }\n    public static class IntegerBinding extends GenericFixture<Integer> { }\n    public static class FixtureBean { public String value = \"fixture-value\"; }\n    public interface FixtureMock { String accept(String value); }\n\n    public static final String EXPLICIT_FIXTURES = \"beam-explicit-fixtures-v3-proposal\";\n    public static final String SCALAR_FIXTURES = \"beam-explicit-fixtures-v4-proposal\";\n    public static final String PILOT_FIXTURES = \"beam-explicit-fixtures-v5-proposal\";\n    public static final String BUFFER_FIXTURES = \"beam-explicit-fixtures-v6-buffer-proposal\";\n    public static final String FRACTION_FIELD_FIXTURES = \"aom-beam-fraction-field-v6-development\";\n    public static final String LANG_HELPER_FIXTURES = \"beam-explicit-fixtures-v9-buffer-lang-development\";\n    public static final String JOINT_FIXTURES = \"aom-beam-champ-joint-fixtures-v10-development\";\n    public static final String CODEC_FIXTURES = \"aom-beam-champ-codec-fixtures-v13-development\";\n    public static final String GRAPHICS_FIXTURES = \"aom-beam-champ-graphics-fixtures-v12-development\";\n    public static final String CHRONOLOGY_FIXTURES = \"aom-beam-champ-chronology-fixtures-v11-development\";\n    // Diagnostic scope only, serialized by observeChronology. Empty during all\n    // receiver setup/projection calls, so JDI cannot count setup as target entry.\n    public static String chronologyActiveCase = \"\";\n    private static final ThreadLocal<FixtureSession> FIXTURES = new ThreadLocal<FixtureSession>();\n    private static final ThreadLocal<Boolean> INVOKED = new ThreadLocal<Boolean>();\n\n    /** A setup failure is never an observation of an uncalled target method. */\n    private static final class FixtureFailure extends RuntimeException {\n        FixtureFailure(String message, Throwable cause) { super(message, cause); }\n    }\n\n    // Production factories only: no dataset test classes, patches or buggy results.\n    // Reflection keeps the helper compilable without project-specific dependencies.\n    private static Object call(Object receiver, String name, Class<?>[] parameterTypes, Object... values)\n            throws ReflectiveOperationException {\n        Class<?> declaring = receiver instanceof Class ? (Class<?>)receiver : receiver.getClass();\n        while (declaring != null) {\n            try {\n                Method method = declaring.getDeclaredMethod(name, parameterTypes);\n                method.setAccessible(true);\n                return method.invoke(receiver instanceof Class ? null : receiver, values);\n            } catch (NoSuchMethodException missing) { declaring = declaring.getSuperclass(); }\n        }\n        throw new NoSuchMethodException(name);\n    }\n\n    private static Object construct(String name, Class<?>[] parameterTypes, Object... values)\n            throws ReflectiveOperationException {\n        Constructor<?> ctor = Class.forName(name).getDeclaredConstructor(parameterTypes);\n        ctor.setAccessible(true);\n        return ctor.newInstance(values);\n    }\n\n    private static final class FixtureSession {\n        final String targetClass;\n        final String method;\n        final boolean pilot;\n        final boolean bufferSlices;\n        char[] outputBuffer;\n        final boolean fractionField;\n        final boolean langHelpers;\n        final boolean reviewed;\n        Object validationInput;\n        boolean constructing;\n        Object compiler, registry, scope, cfg, reverse, flow, closureNode, receiver;\n        org.w3c.dom.Element domRoot;\n        org.w3c.dom.Node domChild;\n        Object jdomRoot, jdomChild;\n        java.io.ByteArrayOutputStream archiveBytes;\n        Object mapper, parser, context, collectionType, collectionDeserializer;\n        Object mock, baseInvocation, actualInvocation;\n        Object chartDataset, chartPlot, chartAxis, cleanupScript, cleanupExterns;\n        int cleanupNodeIndex;\n\n        @SuppressWarnings({\"unchecked\", \"rawtypes\"})\n        void unusedClosure(double a) throws ReflectiveOperationException {\n            if (compiler != null) return;\n            Class<?> node = Class.forName(\"com.google.javascript.rhino.Node\");\n            Class<?> ac = Class.forName(\"com.google.javascript.jscomp.AbstractCompiler\");\n            compiler = construct(\"com.google.javascript.jscomp.Compiler\", new Class<?>[]{});\n            Object options = construct(\"com.google.javascript.jscomp.CompilerOptions\", new Class<?>[]{});\n            call(compiler, \"initOptions\", new Class<?>[]{options.getClass()}, options);\n            cleanupExterns = call(compiler, \"parseTestCode\", new Class<?>[]{String.class}, \"\");\n            cleanupScript = call(compiler, \"parseTestCode\", new Class<?>[]{String.class},\n                \"var unused = 1; function fixture(x) { var local = \" + (a < 0 ? \"2\" : \"3\") + \"; return x; } fixture(1);\");\n            // Normalize traverses sibling roots and requires their common parent.\n            int block = Class.forName(\"com.google.javascript.rhino.Token\").getField(\"BLOCK\").getInt(null);\n            Object roots = construct(node.getName(), new Class<?>[]{int.class}, block);\n            call(roots, \"addChildToBack\", new Class<?>[]{node}, cleanupExterns);\n            call(roots, \"addChildToBack\", new Class<?>[]{node}, cleanupScript);\n            Object normalize = construct(\"com.google.javascript.jscomp.Normalize\", new Class<?>[]{ac, boolean.class}, compiler, false);\n            call(normalize, \"process\", new Class<?>[]{node, node}, cleanupExterns, cleanupScript);\n            Class<?> lifecycle = Class.forName(\"com.google.javascript.jscomp.AbstractCompiler$LifeCycleStage\");\n            call(compiler, \"setLifeCycleStage\", new Class<?>[]{lifecycle}, Enum.valueOf((Class)lifecycle, \"NORMALIZED\"));\n            closureNode = cleanupScript;\n        }\n\n        void chart(double a) throws ReflectiveOperationException {\n            if (chartDataset != null) return;\n            Class<?> dataset = Class.forName(\"org.jfree.data.category.CategoryDataset\");\n            Class<?> axis = Class.forName(\"org.jfree.chart.axis.CategoryAxis\");\n            Class<?> valueAxis = Class.forName(\"org.jfree.chart.axis.ValueAxis\");\n            Class<?> renderer = Class.forName(\"org.jfree.chart.renderer.category.CategoryItemRenderer\");\n            chartDataset = construct(\"org.jfree.data.category.DefaultCategoryDataset\", new Class<?>[]{});\n            call(chartDataset, \"addValue\", new Class<?>[]{double.class, Comparable.class, Comparable.class}, a < 0 ? -2.0 : 2.0, \"row-a\", \"column-a\");\n            call(chartDataset, \"addValue\", new Class<?>[]{double.class, Comparable.class, Comparable.class}, 5.0, \"row-b\", \"column-a\");\n            chartAxis = construct(axis.getName(), new Class<?>[]{String.class}, \"Domain\");\n            Object rangeAxis = construct(\"org.jfree.chart.axis.NumberAxis\", new Class<?>[]{String.class}, \"Range\");\n            chartPlot = construct(\"org.jfree.chart.plot.CategoryPlot\", new Class<?>[]{dataset, axis, valueAxis, renderer},\n                chartDataset, chartAxis, rangeAxis, receiver);\n            java.awt.Graphics2D graphics = new java.awt.image.BufferedImage(16,16,java.awt.image.BufferedImage.TYPE_INT_RGB).createGraphics();\n            try {\n                call(receiver, \"initialise\", new Class<?>[]{java.awt.Graphics2D.class, java.awt.geom.Rectangle2D.class,\n                    chartPlot.getClass(), dataset, Class.forName(\"org.jfree.chart.plot.PlotRenderingInfo\")},\n                    graphics, new java.awt.geom.Rectangle2D.Double(0,0,16,16), chartPlot, chartDataset, null);\n            } finally { graphics.dispose(); }\n        }\n\n        Object beanWriter() throws ReflectiveOperationException {\n            Object objectMapper = construct(\"com.fasterxml.jackson.databind.ObjectMapper\", new Class<?>[]{});\n            Object provider = call(objectMapper, \"getSerializerProvider\", new Class<?>[]{});\n            provider = call(provider, \"createInstance\", new Class<?>[]{Class.forName(\"com.fasterxml.jackson.databind.SerializationConfig\"),\n                Class.forName(\"com.fasterxml.jackson.databind.ser.SerializerFactory\")},\n                call(objectMapper, \"getSerializationConfig\", new Class<?>[]{}), call(objectMapper, \"getSerializerFactory\", new Class<?>[]{}));\n            Object serializer = call(provider, \"findValueSerializer\", new Class<?>[]{Class.class, Class.forName(\"com.fasterxml.jackson.databind.BeanProperty\")}, FixtureBean.class, null);\n            return Array.get(field(serializer, \"_props\"), 0);\n        }\n\n        @SuppressWarnings({\"unchecked\", \"rawtypes\"})\n        void jacksonCollection(double a) throws ReflectiveOperationException {\n            if (mapper != null) return;\n            mapper = construct(\"com.fasterxml.jackson.databind.ObjectMapper\", new Class<?>[]{});\n            Class<?> feature = Class.forName(\"com.fasterxml.jackson.databind.DeserializationFeature\");\n            call(mapper, \"configure\", new Class<?>[]{feature, boolean.class}, Enum.valueOf((Class)feature, \"ACCEPT_SINGLE_VALUE_AS_ARRAY\"), true);\n            Object typeFactory = call(mapper, \"getTypeFactory\", new Class<?>[]{});\n            collectionType = call(typeFactory, \"constructCollectionType\", new Class<?>[]{Class.class, Class.class}, java.util.ArrayList.class, String.class);\n            Object factory = call(mapper, \"getFactory\", new Class<?>[]{});\n            String input = method.equals(\"handleNonArray\") ? a < 0 ? \"\\\"alpha\\\"\" : \"\\\"beta\\\"\"\n                : a < 0 ? \"[\\\"alpha\\\",\\\"beta\\\"]\" : \"[\\\"left\\\",\\\"right\\\"]\";\n            parser = call(factory, \"createParser\", new Class<?>[]{String.class}, input);\n            call(parser, \"nextToken\", new Class<?>[]{});\n            Object blueprint = call(mapper, \"getDeserializationContext\", new Class<?>[]{});\n            context = call(blueprint, \"createInstance\", new Class<?>[]{Class.forName(\"com.fasterxml.jackson.databind.DeserializationConfig\"),\n                Class.forName(\"com.fasterxml.jackson.core.JsonParser\"), Class.forName(\"com.fasterxml.jackson.databind.InjectableValues\")},\n                call(mapper, \"getDeserializationConfig\", new Class<?>[]{}), parser, null);\n            collectionDeserializer = call(context, \"findRootValueDeserializer\", new Class<?>[]{Class.forName(\"com.fasterxml.jackson.databind.JavaType\")}, collectionType);\n        }\n\n        void mockito(double a) throws ReflectiveOperationException {\n            if (mock != null) return;\n            mock = call(Class.forName(\"org.mockito.Mockito\"), \"mock\", new Class<?>[]{Class.class}, FixtureMock.class);\n            call(mock, \"accept\", new Class<?>[]{String.class}, \"alpha\");\n            call(mock, \"accept\", new Class<?>[]{String.class}, a < 0 ? \"alpha\" : \"beta\");\n            Object util = construct(\"org.mockito.internal.util.MockUtil\", new Class<?>[]{});\n            Object handler = call(util, \"getMockHandler\", new Class<?>[]{Object.class}, mock);\n            Object container = call(handler, \"getInvocationContainer\", new Class<?>[]{});\n            List<?> invocations = (List<?>)call(container, \"getInvocations\", new Class<?>[]{});\n            baseInvocation = invocations.get(0);\n            actualInvocation = invocations.get(1);\n        }\n\n        FixtureSession(String targetClass, String method, String policy) {\n            this.targetClass = targetClass;\n            this.method = method;\n            this.reviewed = JOINT_FIXTURES.equals(policy) || CHRONOLOGY_FIXTURES.equals(policy) || GRAPHICS_FIXTURES.equals(policy) || CODEC_FIXTURES.equals(policy);\n            this.langHelpers = LANG_HELPER_FIXTURES.equals(policy) || reviewed;\n            this.bufferSlices = BUFFER_FIXTURES.equals(policy) || langHelpers;\n            this.fractionField = FRACTION_FIELD_FIXTURES.equals(policy) || langHelpers;\n            this.pilot = PILOT_FIXTURES.equals(policy) || bufferSlices || fractionField;\n        }\n\n        Object[] langHelperArguments(Class<?>[] types, double[] vector) {\n            if (!langHelpers || constructing || !targetClass.equals(\"org.apache.commons.lang3.math.NumberUtils\")\n                    || types.length != 1) return null;\n            double a = vector[0];\n            if (method.equals(\"isAllZeros\") && types[0] == String.class)\n                return new Object[]{new String[]{null, \"\", \"0\", \"000\", \"001\", \"12\", \"00 0\", \"-0\"}[bucket(a, 8)]};\n            if (method.equals(\"validateArray\") && types[0] == Object.class) {\n                Object[] arrays = {null, new int[0], new int[]{0}, new int[]{-1, 0, 7}};\n                validationInput = arrays[bucket(a, arrays.length)];\n                return new Object[]{validationInput};\n            }\n            return null;\n        }\n\n        Object[] boundedBufferArguments(Class<?>[] types, double[] vector) {\n            if (!bufferSlices || constructing || types.length == 0) return null;\n            double a = vector[0], b = vector[1 % vector.length];\n            if (targetClass.equals(\"com.fasterxml.jackson.core.io.NumberInput\") && types[0] == char[].class) {\n                String text;\n                if (method.equals(\"parseLong\"))\n                    text = new String[]{\"1000000000\", \"1234567890123\", \"123456789012345678\"}[bucket(a, 3)];\n                else if (method.equals(\"parseInt\"))\n                    text = new String[]{\"0\", \"7\", \"12345\", \"999999999\"}[bucket(a, 4)];\n                else if (method.equals(\"inLongRange\"))\n                    text = new String[]{\"0\", \"9223372036854775807\", \"9223372036854775808\", \"9223372036854775809\"}[bucket(a, 4)];\n                else if (method.equals(\"parseBigDecimal\"))\n                    text = new String[]{\"0\", \"12.50\", \"-0.125\"}[bucket(a, 3)];\n                else return null;\n                if (types.length == 1) return new Object[]{text.toCharArray()};\n                char[] chars = (\"##\" + text + \"?\").toCharArray();\n                if (types.length == 4) return new Object[]{chars, 2, text.length(), b < 0};\n                return new Object[]{chars, 2, text.length()};\n            }\n            if (targetClass.equals(\"com.fasterxml.jackson.core.util.TextBuffer\") && method.equals(\"append\")\n                    && types.length == 3 && (types[0] == char[].class || types[0] == String.class)) {\n                String text = a < 0 ? \"xABCDy\" : \"p12345q\";\n                int offset = a < 0 ? 1 : 2;\n                int length = 1 + bucket(b, text.length() - offset - 1);\n                return new Object[]{types[0] == char[].class ? text.toCharArray() : text, offset, length};\n            }\n            if (targetClass.equals(\"org.apache.commons.csv.ExtendedBufferedReader\") && method.equals(\"read\")\n                    && types.length == 3 && types[0] == char[].class) {\n                outputBuffer = new char[8];\n                Arrays.fill(outputBuffer, '~');\n                int offset = a < 0 ? 1 : 2;\n                int length = 1 + bucket(b, outputBuffer.length - offset - 1);\n                return new Object[]{outputBuffer, offset, length};\n            }\n            return null;\n        }\n\n        Object option(String name, String text) throws ReflectiveOperationException {\n            Object option = construct(\"org.apache.commons.cli.Option\",\n                    new Class<?>[]{String.class, boolean.class, String.class}, name, true, \"fixture\");\n            call(option, \"setType\", new Class<?>[]{Object.class}, String.class);\n            call(option, \"addValue\", new Class<?>[]{String.class}, text);\n            return option;\n        }\n\n        Object archiveEntry(String name, long size) throws ReflectiveOperationException {\n            Object entry = construct(\"org.apache.commons.compress.archivers.cpio.CpioArchiveEntry\",\n                    new Class<?>[]{String.class}, name);\n            call(entry, \"setSize\", new Class<?>[]{long.class}, size);\n            call(entry, \"setTime\", new Class<?>[]{long.class}, 0L);\n            call(entry, \"setMode\", new Class<?>[]{long.class}, 0100644L);\n            return entry;\n        }\n\n        Object prepareReceiver(Object value, double a) throws ReflectiveOperationException {\n            if (!pilot) return value;\n            if (targetClass.equals(\"org.apache.commons.cli.CommandLine\")) {\n                call(value, \"addOption\", new Class<?>[]{Class.forName(\"org.apache.commons.cli.Option\")}, option(\"x\", a < 0 ? \"alpha\" : \"beta\"));\n                call(value, \"addArg\", new Class<?>[]{String.class}, \"positional\");\n            } else if (targetClass.equals(\"com.fasterxml.jackson.core.util.TextBuffer\")) {\n                char[] content = (a < 0 ? \"123\" : \"45.5\").toCharArray();\n                call(value, \"resetWithCopy\", new Class<?>[]{char[].class, int.class, int.class}, content, 0, content.length);\n            } else if (targetClass.equals(\"org.jsoup.nodes.Document\")) {\n                Object html = call(value, \"appendElement\", new Class<?>[]{String.class}, \"html\");\n                call(html, \"appendElement\", new Class<?>[]{String.class}, \"head\");\n                Object body = call(html, \"appendElement\", new Class<?>[]{String.class}, \"body\");\n                call(body, \"text\", new Class<?>[]{String.class}, a < 0 ? \"alpha\" : \"beta\");\n                call(value, \"title\", new Class<?>[]{String.class}, \"Fixture\");\n            } else if (targetClass.endsWith(\"CpioArchiveOutputStream\")) {\n                call(value, \"putNextEntry\", new Class<?>[]{Class.forName(\"org.apache.commons.compress.archivers.cpio.CpioArchiveEntry\")},\n                        archiveEntry(\"fixture.txt\", method.equals(\"write\") ? 1 : 0));\n            } else if (targetClass.equals(\"org.joda.time.Partial\")) {\n                return call(value, \"with\", new Class<?>[]{Class.forName(\"org.joda.time.DateTimeFieldType\"), int.class},\n                        call(Class.forName(\"org.joda.time.DateTimeFieldType\"), \"hourOfDay\", new Class<?>[]{}), 10);\n            } else if (targetClass.equals(\"org.jfree.chart.renderer.category.AreaRenderer\")) {\n                receiver = value;\n                chart(a);\n            } else if (targetClass.equals(\"com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser\")) {\n                // Real StAX input; getters start on a named leaf VALUE_STRING.\n                for (int i = 0; i < 8; i++) {\n                    Object token = call(value, \"nextToken\", new Class<?>[]{});\n                    if (token != null && token.toString().equals(\"VALUE_STRING\")) break;\n                }\n            }\n            return value;\n        }\n\n        @SuppressWarnings({\"unchecked\", \"rawtypes\"})\n        Object nativeType(String name, boolean object) throws ReflectiveOperationException {\n            Class<?> nativeClass = Class.forName(\"com.google.javascript.rhino.jstype.JSTypeNative\");\n            Object key = Enum.valueOf((Class)nativeClass, name);\n            return call(registry, object ? \"getNativeObjectType\" : \"getNativeType\", new Class<?>[]{nativeClass}, key);\n        }\n\n        void closure(double a) throws ReflectiveOperationException {\n            if (compiler != null) return;\n            Class<?> node = Class.forName(\"com.google.javascript.rhino.Node\");\n            Class<?> scopeClass = Class.forName(\"com.google.javascript.jscomp.Scope\");\n            Class<?> abstractCompiler = Class.forName(\"com.google.javascript.jscomp.AbstractCompiler\");\n            compiler = construct(\"com.google.javascript.jscomp.Compiler\", new Class<?>[]{});\n            Object options = construct(\"com.google.javascript.jscomp.CompilerOptions\", new Class<?>[]{});\n            call(compiler, \"initOptions\", new Class<?>[]{options.getClass()}, options);\n            registry = call(compiler, \"getTypeRegistry\", new Class<?>[]{});\n            String expression = a < 0 ? \"x + 1\" : \"x + 's'\";\n            if (method.contains(\"And\") || method.contains(\"ShortCircuit\")) expression = \"x && true\";\n            if (method.contains(\"Or\")) expression = \"x || false\";\n            if (method.equals(\"traverseArrayLiteral\")) expression = \"[x, 1]\";\n            if (method.equals(\"traverseObjectLiteral\")) expression = \"({p:x})\";\n            if (method.equals(\"traverseHook\")) expression = \"x ? 1 : 2\";\n            if (method.equals(\"traverseAssign\")) expression = \"x = 2\";\n            if (method.equals(\"traverseGetElem\")) expression = \"x['p']\";\n            if (method.equals(\"traverseGetProp\") || method.contains(\"Property\")) expression = \"x.p\";\n            if (method.equals(\"traverseName\") || method.equals(\"redeclareSimpleVar\")\n                    || method.equals(\"narrowScope\") || method.equals(\"updateScopeForTypeChange\")) expression = \"x\";\n            Object script = call(compiler, \"parseTestCode\", new Class<?>[]{String.class},\n                    \"function fixture(x) { return \" + expression + \"; }\");\n            Object function = call(script, \"getFirstChild\", new Class<?>[]{});\n            Object global = call(scopeClass, \"createGlobalScope\", new Class<?>[]{node}, script);\n            scope = construct(scopeClass.getName(), new Class<?>[]{scopeClass, node}, global, function);\n            Object astParameters = call(call(function, \"getFirstChild\", new Class<?>[]{}), \"getNext\", new Class<?>[]{});\n            Object name = call(astParameters, \"getFirstChild\", new Class<?>[]{});\n            call(scope, \"declare\", new Class<?>[]{String.class, node,\n                    Class.forName(\"com.google.javascript.rhino.jstype.JSType\"),\n                    Class.forName(\"com.google.javascript.jscomp.CompilerInput\")}, \"x\", name, nativeType(\"UNKNOWN_TYPE\", false), null);\n            Object body = call(function, \"getLastChild\", new Class<?>[]{});\n            Object returnNode = call(body, \"getFirstChild\", new Class<?>[]{});\n            closureNode = method.equals(\"traverseReturn\") || method.equals(\"branchedFlowThrough\")\n                    ? returnNode : call(returnNode, \"getFirstChild\", new Class<?>[]{});\n            if (method.equals(\"traverseObjectLiteral\"))\n                call(closureNode, \"setJSType\", new Class<?>[]{Class.forName(\"com.google.javascript.rhino.jstype.JSType\")}, nativeType(\"OBJECT_TYPE\", true));\n            Object analysis = construct(\"com.google.javascript.jscomp.ControlFlowAnalysis\",\n                    new Class<?>[]{abstractCompiler, boolean.class, boolean.class}, compiler, false, true);\n            call(analysis, \"process\", new Class<?>[]{node, node}, null, function);\n            cfg = call(analysis, \"getCfg\", new Class<?>[]{});\n            Object convention = call(compiler, \"getCodingConvention\", new Class<?>[]{});\n            reverse = construct(\"com.google.javascript.jscomp.type.SemanticReverseAbstractInterpreter\",\n                    new Class<?>[]{Class.forName(\"com.google.javascript.jscomp.CodingConvention\"), registry.getClass()}, convention, registry);\n            flow = call(Class.forName(\"com.google.javascript.jscomp.LinkedFlowScope\"), \"createEntryLattice\",\n                    new Class<?>[]{scopeClass}, scope);\n            call(flow, \"inferSlotType\", new Class<?>[]{String.class, Class.forName(\"com.google.javascript.rhino.jstype.JSType\")},\n                    \"x\", nativeType(a < 0 ? \"NUMBER_TYPE\" : \"STRING_TYPE\", false));\n        }\n\n        void dom(double a) throws Exception {\n            if (domRoot != null) return;\n            javax.xml.parsers.DocumentBuilderFactory factory = pilot\n                ? javax.xml.parsers.DocumentBuilderFactory.newInstance(\"com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl\", SqaProbe.class.getClassLoader())\n                : javax.xml.parsers.DocumentBuilderFactory.newInstance();\n            factory.setNamespaceAware(true);\n            org.w3c.dom.Document document = factory.newDocumentBuilder().newDocument();\n            domRoot = document.createElementNS(\"urn:sqa:root\", \"r:root\");\n            document.appendChild(domRoot);\n            domRoot.setAttributeNS(\"http://www.w3.org/2000/xmlns/\", \"xmlns:r\", \"urn:sqa:root\");\n            domRoot.setAttributeNS(\"http://www.w3.org/XML/1998/namespace\", \"xml:lang\", \"en\");\n            org.w3c.dom.Element element = document.createElementNS(\"urn:sqa:item\", \"i:item\");\n            domChild = element;\n            element.setAttributeNS(\"http://www.w3.org/2000/xmlns/\", \"xmlns:i\", \"urn:sqa:item\");\n            element.setAttribute(\"id\", a < 0 ? \"left\" : \"right\");\n            domChild.appendChild(document.createTextNode(a < 0 ? \"alpha\" : \"beta\"));\n            org.w3c.dom.Element grandchild = document.createElementNS(\"urn:sqa:item\", \"i:item\");\n            grandchild.appendChild(document.createTextNode(\"nested\"));\n            domChild.appendChild(grandchild);\n            org.w3c.dom.Element last = document.createElementNS(\"urn:sqa:item\", \"i:item\");\n            last.appendChild(document.createTextNode(\"nested-last\"));\n            domChild.appendChild(last);\n            if (method.equals(\"getRelativePositionOfPI\")) {\n                domRoot.appendChild(document.createProcessingInstruction(\"fixture\", \"before\"));\n                domChild = document.createProcessingInstruction(\"fixture\", a < 0 ? \"alpha\" : \"beta\");\n            } else if (method.equals(\"getRelativePositionOfTextNode\")) {\n                domRoot.appendChild(document.createCDATASection(\"before\"));\n                domChild = document.createTextNode(a < 0 ? \"alpha\" : \"beta\");\n            }\n            domRoot.appendChild(domChild);\n        }\n\n        void jdom(double a) throws ReflectiveOperationException {\n            if (jdomRoot != null) return;\n            Class<?> element = Class.forName(\"org.jdom.Element\");\n            jdomRoot = construct(element.getName(), new Class<?>[]{String.class}, \"root\");\n            jdomChild = construct(element.getName(), new Class<?>[]{String.class}, \"item\");\n            call(jdomChild, \"setText\", new Class<?>[]{String.class}, a < 0 ? \"alpha\" : \"beta\");\n            call(jdomChild, \"setAttribute\", new Class<?>[]{String.class, String.class}, \"id\", a < 0 ? \"left\" : \"right\");\n            Object grandchild = construct(element.getName(), new Class<?>[]{String.class}, \"item\");\n            call(grandchild, \"setText\", new Class<?>[]{String.class}, \"nested\");\n            call(jdomChild, \"addContent\", new Class<?>[]{Class.forName(\"org.jdom.Content\")}, grandchild);\n            Object last = construct(element.getName(), new Class<?>[]{String.class}, \"item\");\n            call(last, \"setText\", new Class<?>[]{String.class}, \"nested-last\");\n            call(jdomChild, \"addContent\", new Class<?>[]{Class.forName(\"org.jdom.Content\")}, last);\n            if (method.equals(\"getRelativePositionOfPI\")) {\n                Object before = construct(\"org.jdom.ProcessingInstruction\", new Class<?>[]{String.class, String.class}, \"fixture\", \"before\");\n                call(jdomRoot, \"addContent\", new Class<?>[]{Class.forName(\"org.jdom.Content\")}, before);\n                jdomChild = construct(\"org.jdom.ProcessingInstruction\", new Class<?>[]{String.class, String.class}, \"fixture\", a < 0 ? \"alpha\" : \"beta\");\n            } else if (method.equals(\"getRelativePositionOfTextNode\")) {\n                Object before = construct(\"org.jdom.CDATA\", new Class<?>[]{String.class}, \"before\");\n                call(jdomRoot, \"addContent\", new Class<?>[]{Class.forName(\"org.jdom.Content\")}, before);\n                jdomChild = construct(\"org.jdom.Text\", new Class<?>[]{String.class}, a < 0 ? \"alpha\" : \"beta\");\n            }\n            call(jdomRoot, \"addContent\", new Class<?>[]{Class.forName(\"org.jdom.Content\")}, jdomChild);\n        }\n\n        void configurePointer(Object pointer) throws ReflectiveOperationException {\n            Class<?> resolverClass = Class.forName(\"org.apache.commons.jxpath.ri.NamespaceResolver\");\n            Object resolver = construct(resolverClass.getName(), new Class<?>[]{resolverClass}, new Object[]{null});\n            call(resolver, \"registerNamespace\", new Class<?>[]{String.class, String.class}, \"i\", \"urn:sqa:item\");\n            call(resolver, \"registerNamespace\", new Class<?>[]{String.class, String.class}, \"r\", \"urn:sqa:root\");\n            call(resolver, \"setNamespaceContextPointer\", new Class<?>[]{Class.forName(\"org.apache.commons.jxpath.ri.model.NodePointer\")}, pointer);\n            call(pointer, \"setNamespaceResolver\", new Class<?>[]{resolverClass}, resolver);\n        }\n\n        Object argument(Class<?> type, double a, double b, double c, int depth) {\n            try {\n                if (depth > 2) throw new FixtureFailure(\"Fixture recursion limit: \" + type.getName(), null);\n                String name = type.getName();\n                if (reviewed && !constructing && targetClass.equals(\"org.apache.commons.codec.language.Metaphone\")\n                        && method.equals(\"setMaxCodeLen\") && type == int.class)\n                    return a < -8 ? 0 : a < 0 ? 1 : a < 8 ? 4 : 8;\n                if (pilot) {\n                    if (targetClass.equals(\"com.google.gson.TypeInfoFactory\")) {\n                        java.lang.reflect.Field value = GenericFixture.class.getField(a < 0 ? \"value\" : \"items\");\n                        if (type == java.lang.reflect.TypeVariable.class) return GenericFixture.class.getTypeParameters()[0];\n                        if (type == java.lang.reflect.Field.class) return value;\n                        if (type == Class.class) return GenericFixture.class;\n                        if (type == java.lang.reflect.Type.class) {\n                            if (method.equals(\"getTypeInfoForArray\")) return a < 0 ? String[].class : Integer[].class;\n                            return a < 0 ? StringBinding.class.getGenericSuperclass() : IntegerBinding.class.getGenericSuperclass();\n                        }\n                    }\n                    if (targetClass.equals(\"com.google.javascript.jscomp.RemoveUnusedVars\")) {\n                        unusedClosure(a);\n                        if (type == boolean.class) return false; // No call-site optimizer prerequisite.\n                        if (name.equals(\"com.google.javascript.jscomp.AbstractCompiler\")) return compiler;\n                        if (name.equals(\"com.google.javascript.rhino.Node\")) {\n                            if (method.equals(\"process\")) return cleanupNodeIndex++ == 0 ? cleanupExterns : cleanupScript;\n                            if (method.equals(\"getFunctionArgList\")) {\n                                Object child = call(cleanupScript, \"getFirstChild\", new Class<?>[]{});\n                                while (child != null && !(Boolean)call(child, \"isFunction\", new Class<?>[]{}))\n                                    child = call(child, \"getNext\", new Class<?>[]{});\n                                if (child == null) throw new FixtureFailure(\"Missing parsed function\", null);\n                                return child;\n                            }\n                            return cleanupScript;\n                        }\n                    }\n                    if (targetClass.equals(\"org.jfree.chart.renderer.category.AreaRenderer\")) {\n                        chart(a);\n                        if (name.equals(\"org.jfree.data.category.CategoryDataset\")) return chartDataset;\n                        if (name.equals(\"org.jfree.chart.axis.CategoryAxis\")) return chartAxis;\n                        if (type == Comparable.class) return a < 0 ? \"row-a\" : \"column-a\";\n                        if (type == java.awt.geom.Rectangle2D.class) return new java.awt.geom.Rectangle2D.Double(0,0,16,16);\n                        if (name.equals(\"org.jfree.chart.util.RectangleEdge\")) return type.getField(\"BOTTOM\").get(null);\n                        if (type == int.class) return 0;\n                    }\n                    if (targetClass.equals(\"com.fasterxml.jackson.databind.ser.BeanPropertyWriter\")) {\n                        if (name.equals(targetClass)) return beanWriter();\n                        if (name.equals(\"com.fasterxml.jackson.databind.util.NameTransformer\"))\n                            return call(type, \"simpleTransformer\", new Class<?>[]{String.class, String.class}, a < 0 ? \"left_\" : \"right_\", \"_suffix\");\n                        if (type == Object.class) return method.equals(\"get\") ? new FixtureBean() : a < 0 ? \"fixture-key\" : \"fixture-value\";\n                    }\n                    if (targetClass.equals(\"com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer\")) {\n                        jacksonCollection(a);\n                        if (name.equals(\"com.fasterxml.jackson.databind.JavaType\")) return collectionType;\n                        if (name.equals(\"com.fasterxml.jackson.core.JsonParser\")) return parser;\n                        if (name.equals(\"com.fasterxml.jackson.databind.DeserializationContext\")) return context;\n                        if (name.equals(\"com.fasterxml.jackson.databind.deser.ValueInstantiator\"))\n                            return call(collectionDeserializer, \"getValueInstantiator\", new Class<?>[]{});\n                        if (name.equals(\"com.fasterxml.jackson.databind.JsonDeserializer\"))\n                            return Class.forName(\"com.fasterxml.jackson.databind.deser.std.StringDeserializer\").getField(\"instance\").get(null);\n                    }\n                    if (targetClass.equals(\"com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser\")) {\n                        String xml = a < 0 ? \"<root><item>123</item><other>alpha</other></root>\" : \"<root><item>45</item><other>beta</other></root>\";\n                        if (type == int.class && constructing) return 0;\n                        if (name.equals(\"com.fasterxml.jackson.core.io.IOContext\"))\n                            return construct(name, new Class<?>[]{Class.forName(\"com.fasterxml.jackson.core.util.BufferRecycler\"), Object.class, boolean.class},\n                                construct(\"com.fasterxml.jackson.core.util.BufferRecycler\", new Class<?>[]{}), xml, false);\n                        if (name.equals(\"com.fasterxml.jackson.core.ObjectCodec\")) return construct(\"com.fasterxml.jackson.dataformat.xml.XmlMapper\", new Class<?>[]{});\n                        if (type == javax.xml.stream.XMLStreamReader.class) {\n                            javax.xml.stream.XMLStreamReader reader = javax.xml.stream.XMLInputFactory.newInstance().createXMLStreamReader(new java.io.StringReader(xml));\n                            while (reader.hasNext() && reader.getEventType() != javax.xml.stream.XMLStreamConstants.START_ELEMENT) reader.next();\n                            return reader;\n                        }\n                    }\n                    if (targetClass.equals(\"org.mockito.internal.invocation.InvocationMatcher\")) {\n                        mockito(a);\n                        if (name.equals(\"org.mockito.invocation.Invocation\")) return constructing ? baseInvocation : actualInvocation;\n                    }\n                    if (targetClass.startsWith(\"org.apache.commons.math3.fraction.\")) {\n                        int number = 1 + bucket(a, 8);\n                        if (type == double.class) return (a < 0 ? -1 : 1) * number / 4.0;\n                        if (type == int.class) return number;\n                        if (type == long.class) return (long)number;\n                        if (type == java.math.BigInteger.class) return java.math.BigInteger.valueOf(number);\n                        if (name.equals(\"org.apache.commons.math3.fraction.BigFraction\") || name.equals(\"org.apache.commons.math3.fraction.Fraction\"))\n                            return construct(name, new Class<?>[]{int.class, int.class}, number, 3);\n                    }\n                    if (targetClass.equals(\"org.apache.commons.cli.CommandLine\")) {\n                        if (type == String.class) return constructing ? \"fixture\" : a < -0.33 ? \"x\" : a < 0.33 ? \"missing\" : \"extra\";\n                        if (type == char.class) return a < 0 ? 'x' : 'z';\n                        if (name.equals(\"org.apache.commons.cli.Option\")) return option(\"extra\", a < 0 ? \"left\" : \"right\");\n                    }\n                    if (targetClass.equals(\"org.jsoup.nodes.Document\") && type == String.class)\n                        return constructing ? \"https://fixture.invalid/\" : method.equals(\"createElement\") ? a < 0 ? \"span\" : \"section\"\n                            : STRINGS[bucket(a, STRINGS.length)];\n                    if (targetClass.equals(\"org.joda.time.Partial\")) {\n                        if (type == int.class) return bucket(a, 24);\n                        if (name.equals(\"org.joda.time.DateTimeFieldType\"))\n                            return call(type, \"hourOfDay\", new Class<?>[]{});\n                    }\n                    if (name.equals(\"org.joda.time.DurationFieldType\")) return call(type, a < 0 ? \"hours\" : \"days\", new Class<?>[]{});\n                    if (name.equals(\"org.joda.time.DurationField\")) return call(Class.forName(\"org.joda.time.field.UnsupportedDurationField\"),\n                        \"getInstance\", new Class<?>[]{Class.forName(\"org.joda.time.DurationFieldType\")},\n                        call(Class.forName(\"org.joda.time.DurationFieldType\"), \"hours\", new Class<?>[]{}));\n                    if (name.equals(\"com.fasterxml.jackson.core.util.BufferRecycler\")) return construct(name, new Class<?>[]{});\n                    if (type == java.io.OutputStream.class && targetClass.endsWith(\"CpioArchiveOutputStream\")) {\n                        archiveBytes = new java.io.ByteArrayOutputStream();\n                        return archiveBytes;\n                    }\n                    if (name.equals(\"org.apache.commons.compress.archivers.ArchiveEntry\") || name.equals(\"org.apache.commons.compress.archivers.cpio.CpioArchiveEntry\"))\n                        return archiveEntry(a < 0 ? \"next-left.txt\" : \"next-right.txt\", 0);\n                    if (targetClass.equals(\"com.fasterxml.jackson.core.io.NumberInput\") && type == String.class)\n                        return new String[]{\"0\", \"1\", \"12\", \"2147483647\"}[bucket(a, 4)];\n                }\n                if (scalar(type)) {\n                    if (type == String.class && method.equals(\"getRelativePositionOfPI\")) return a < 0 ? \"fixture\" : \"other\";\n                    if (type == String.class && (method.equals(\"namespacePointer\") || method.equals(\"getNamespaceURI\")))\n                        return a < 0 ? \"r\" : \"i\";\n                    return legacyArgument(type, Math.max(-0.95, a), b, c, depth);\n                }\n                if (type.isArray()) {\n                    Object array = Array.newInstance(type.getComponentType(), pilot && (targetClass.endsWith(\"NumberUtils\") || targetClass.endsWith(\"TypeInfoFactory\")) ? 1 + bucket(c, 4) : bucket(c, 5));\n                    for (int i = 0; i < Array.getLength(array); i++)\n                        Array.set(array, i, argument(type.getComponentType(), a, b, c, depth + 1));\n                    return array;\n                }\n                if (type == java.io.Reader.class && targetClass.equals(\"org.apache.commons.csv.ExtendedBufferedReader\"))\n                    return new java.io.StringReader(bufferSlices ? (a < 0 ? \"A\\nBC\\nDE\" : \"12\\n345\\n\") : STRINGS[bucket(a, STRINGS.length)]);\n                if (name.startsWith(\"com.google.javascript.\")) {\n                    closure(a);\n                    if (name.endsWith(\".AbstractCompiler\")) return compiler;\n                    if (name.endsWith(\".ControlFlowGraph\")) return cfg;\n                    if (name.endsWith(\".ReverseAbstractInterpreter\")) return reverse;\n                    if (name.endsWith(\".Scope\")) return scope;\n                    if (name.endsWith(\".Scope$Var\")) return call(scope, \"getVar\", new Class<?>[]{String.class}, \"x\");\n                    if (name.endsWith(\".FlowScope\")) return flow;\n                    if (name.endsWith(\".Node\")) return closureNode;\n                    if (name.endsWith(\".JSType\")) return nativeType(a < 0 ? \"NUMBER_TYPE\" : \"STRING_TYPE\", false);\n                    if (name.endsWith(\".ObjectType\")) return nativeType(\"OBJECT_TYPE\", true);\n                }\n                if (name.startsWith(\"org.w3c.dom.\")) {\n                    dom(a);\n                    if (type.isInstance(domChild)) return domChild;\n                    if (type.isInstance(domChild.getOwnerDocument())) return domChild.getOwnerDocument();\n                }\n                if (type == java.util.Locale.class) return java.util.Locale.ROOT;\n                if (name.equals(\"org.apache.commons.jxpath.ri.QName\"))\n                    return construct(name, new Class<?>[]{String.class}, method.equals(\"attributeIterator\") ? \"id\" : \"item\");\n                if (name.equals(\"org.apache.commons.jxpath.ri.compiler.NodeTest\"))\n                    return construct(\"org.apache.commons.jxpath.ri.compiler.NodeNameTest\",\n                            new Class<?>[]{Class.forName(\"org.apache.commons.jxpath.ri.QName\"), String.class},\n                            targetClass.contains(\".jdom.\")\n                                ? construct(\"org.apache.commons.jxpath.ri.QName\", new Class<?>[]{String.class}, \"item\")\n                                : construct(\"org.apache.commons.jxpath.ri.QName\", new Class<?>[]{String.class, String.class}, \"i\", \"item\"),\n                            targetClass.contains(\".jdom.\") ? null : \"urn:sqa:item\");\n                if (name.equals(\"org.apache.commons.jxpath.ri.model.NodePointer\")) {\n                    if (targetClass.contains(\".jdom.\")) {\n                        jdom(a);\n                        if (!constructing && (method.equals(\"childIterator\") || method.equals(\"compareChildNodePointers\"))) {\n                            List<?> children = (List<?>)call(jdomChild, \"getContent\", new Class<?>[]{});\n                            Object anchor = children.get(a < 0 ? 0 : children.size() - 1);\n                            Object pointer = construct(targetClass, new Class<?>[]{type, Object.class}, receiver, anchor);\n                            configurePointer(pointer);\n                            return pointer;\n                        }\n                        Object pointer = construct(\"org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer\",\n                                new Class<?>[]{Object.class, java.util.Locale.class}, jdomRoot, java.util.Locale.ROOT);\n                        configurePointer(pointer);\n                        return pointer;\n                    }\n                    dom(a);\n                    if (!constructing && (method.equals(\"childIterator\") || method.equals(\"compareChildNodePointers\"))) {\n                        org.w3c.dom.Node anchor = a < 0 ? domChild.getFirstChild() : domChild.getLastChild();\n                        Object pointer = construct(targetClass, new Class<?>[]{type, org.w3c.dom.Node.class}, receiver, anchor);\n                        configurePointer(pointer);\n                        return pointer;\n                    }\n                    Object pointer = construct(\"org.apache.commons.jxpath.ri.model.dom.DOMNodePointer\",\n                            new Class<?>[]{org.w3c.dom.Node.class, java.util.Locale.class}, domRoot, java.util.Locale.ROOT);\n                    configurePointer(pointer);\n                    return pointer;\n                }\n                if (type == Object.class && targetClass.contains(\".jdom.\")\n                        && (constructing || !method.equals(\"setValue\"))) { jdom(a); return jdomChild; }\n                if (type == java.util.Iterator.class) return new ArrayList<Object>().iterator();\n                if (type == java.util.List.class || type == java.util.Collection.class || type == Iterable.class)\n                    return new ArrayList<Object>();\n                if (type == java.util.Set.class) return new java.util.HashSet<Object>();\n                if (type == java.util.Map.class) return new java.util.HashMap<Object,Object>();\n                if (type == Object.class || type == Number.class || type == java.util.Date.class)\n                    return legacyArgument(type, Math.max(-0.95, a), b, c, depth);\n                throw new FixtureFailure(\"No explicit recipe: \" + name, null);\n            } catch (FixtureFailure failure) { throw failure; }\n            catch (Exception failure) { throw new FixtureFailure(\"Fixture recipe failed: \" + type.getName()\n                    + \":\" + failure.getClass().getName() + \":\" + failure.getMessage(), failure); }\n        }\n\n        String nodeSnapshot(org.w3c.dom.Node node, int depth) {\n            if (depth > 8) return \"depth-limit\";\n            StringBuilder out = new StringBuilder(\"node:\").append(node.getNodeType()).append(':')\n                    .append(quote(node.getNodeName())).append(':').append(quote(String.valueOf(node.getNodeValue())));\n            org.w3c.dom.NamedNodeMap attributes = node.getAttributes();\n            List<String> attrs = new ArrayList<String>();\n            if (attributes != null) for (int i = 0; i < attributes.getLength(); i++)\n                attrs.add(nodeSnapshot(attributes.item(i), depth + 1));\n            java.util.Collections.sort(attrs);\n            out.append(attrs.toString()).append('[');\n            org.w3c.dom.NodeList children = node.getChildNodes();\n            for (int i = 0; i < Math.min(256, children.getLength()); i++) out.append(nodeSnapshot(children.item(i), depth + 1));\n            return out.append(\"]children:\").append(children.getLength()).toString();\n        }\n\n        Object field(Object value, String name) throws ReflectiveOperationException {\n            for (Class<?> type = value.getClass(); type != null; type = type.getSuperclass()) {\n                try {\n                    java.lang.reflect.Field field = type.getDeclaredField(name);\n                    field.setAccessible(true);\n                    return field.get(value);\n                } catch (NoSuchFieldException missing) { }\n            }\n            throw new NoSuchFieldException(name);\n        }\n\n        String projection(Object result, int depth) throws ReflectiveOperationException {\n            if (depth > 8) throw new FixtureFailure(\"Oracle projection depth exceeded\", null);\n            if (result == null) return \"null\";\n            String name = result.getClass().getName();\n            if (fractionField && (name.equals(\"org.apache.commons.math3.fraction.BigFractionField\")\n                    || name.equals(\"org.apache.commons.math3.fraction.FractionField\")))\n                return \"fraction-field:runtime=\" + projection(call(result, \"getRuntimeClass\", new Class<?>[]{}), depth + 1)\n                    + \":zero=\" + projection(call(result, \"getZero\", new Class<?>[]{}), depth + 1)\n                    + \":one=\" + projection(call(result, \"getOne\", new Class<?>[]{}), depth + 1);\n            if (pilot && result instanceof java.lang.reflect.Type) return \"type:\" + nestedTestName(((java.lang.reflect.Type)result).getTypeName());\n            if (pilot && result instanceof Method) return \"method:\" + nestedTestName(((Method)result).toGenericString());\n            if (pilot && name.startsWith(\"com.google.gson.TypeInfo\"))\n                return \"type-info:\" + projection(call(result, \"getActualType\", new Class<?>[]{}), depth + 1);\n            if (pilot && name.equals(\"com.google.javascript.rhino.Node\")) return \"ast:\" + call(result, \"toStringTree\", new Class<?>[]{});\n            if (pilot && name.equals(\"org.apache.commons.jxpath.ri.NamespaceResolver\"))\n                return \"namespaces:r=\" + call(result, \"getNamespaceURI\", new Class<?>[]{String.class}, \"r\")\n                    + \":i=\" + call(result, \"getNamespaceURI\", new Class<?>[]{String.class}, \"i\");\n            if (pilot && name.equals(\"org.jfree.data.Range\"))\n                return \"range:\" + call(result, \"getLowerBound\", new Class<?>[]{}) + ':' + call(result, \"getUpperBound\", new Class<?>[]{});\n            if (pilot && name.equals(\"org.jfree.chart.LegendItem\")) return \"legend:\" + call(result, \"getLabel\", new Class<?>[]{});\n            if (pilot && name.equals(\"org.jfree.chart.LegendItemCollection\")) {\n                StringBuilder out = new StringBuilder(\"legends[\");\n                int count = ((Number)call(result, \"getItemCount\", new Class<?>[]{})).intValue();\n                if (count > 256) throw new FixtureFailure(\"Legend limit exceeded\", null);\n                for (int i = 0; i < count; i++) out.append(projection(call(result, \"get\", new Class<?>[]{int.class}, i), depth + 1)).append(';');\n                return out.append(']').toString();\n            }\n            if (pilot && name.startsWith(\"com.fasterxml.jackson.databind.type.\")) return \"java-type:\" + call(result, \"toCanonical\", new Class<?>[]{});\n            if (pilot && name.equals(\"com.fasterxml.jackson.core.io.SerializedString\")) return \"serialized-name:\" + call(result, \"getValue\", new Class<?>[]{});\n            if (pilot && name.equals(\"com.fasterxml.jackson.databind.ser.BeanPropertyWriter\"))\n                return \"property:\" + call(result, \"getName\", new Class<?>[]{}) + ':' + projection(call(result, \"getType\", new Class<?>[]{}), depth + 1);\n            if (pilot && targetClass.equals(\"org.mockito.internal.invocation.InvocationMatcher\")\n                    && Class.forName(\"org.mockito.invocation.Invocation\").isInstance(result))\n                return \"invocation:\" + projection(call(result, \"getMethod\", new Class<?>[]{}), depth + 1)\n                    + ':' + projection(call(result, \"getArguments\", new Class<?>[]{}), depth + 1)\n                    + \":verified=\" + call(result, \"isVerified\", new Class<?>[]{});\n            if (pilot && result.getClass().isArray()) {\n                int length = Array.getLength(result);\n                if (length > 100000) throw new FixtureFailure(\"Oracle array limit exceeded\", null);\n                StringBuilder out = new StringBuilder(\"array[\");\n                for (int i = 0; i < length; i++) out.append(projection(Array.get(result, i), depth + 1)).append(';');\n                return out.append(']').toString();\n            }\n            if (pilot && (name.equals(\"org.jsoup.nodes.Document\") || name.equals(\"org.jsoup.nodes.Element\")))\n                return \"html:\" + call(result, \"outerHtml\", new Class<?>[]{});\n            if (pilot && name.equals(\"org.apache.commons.cli.Option\"))\n                return \"option:\" + call(result, \"getOpt\", new Class<?>[]{}) + ':' + projection(call(result, \"getValues\", new Class<?>[]{}), depth + 1);\n            if (pilot && result instanceof java.util.Iterator) {\n                StringBuilder out = new StringBuilder(\"iterator[\");\n                java.util.Iterator<?> iterator = (java.util.Iterator<?>)result;\n                int count = 0;\n                while (iterator.hasNext()) {\n                    if (++count > 256) throw new FixtureFailure(\"Oracle iterator limit exceeded\", null);\n                    out.append(projection(iterator.next(), depth + 1)).append(';');\n                }\n                return out.append(']').toString();\n            }\n            if (pilot && (name.equals(\"org.apache.commons.math3.fraction.BigFraction\") || name.equals(\"org.apache.commons.math3.fraction.Fraction\")))\n                return \"fraction:\" + call(result, \"getNumerator\", new Class<?>[]{}) + '/' + call(result, \"getDenominator\", new Class<?>[]{});\n            if (pilot && name.startsWith(\"org.joda.time.\")) {\n                if (name.equals(\"org.joda.time.Partial\")) return \"partial:\" + call(result, \"toStringList\", new Class<?>[]{});\n                if (Class.forName(\"org.joda.time.DurationFieldType\").isInstance(result)) return \"duration-type:\" + call(result, \"getName\", new Class<?>[]{});\n                if (Class.forName(\"org.joda.time.DurationField\").isInstance(result))\n                    return \"duration:\" + call(result, \"getName\", new Class<?>[]{}) + ':' + call(result, \"isSupported\", new Class<?>[]{});\n            }\n            if (result instanceof org.w3c.dom.Node) return nodeSnapshot((org.w3c.dom.Node)result, 0);\n            if (reviewed && name.equals(\"org.jdom.Attribute\"))\n                return \"jdom-attribute:name=\" + projection(call(result, \"getName\", new Class<?>[]{}), depth + 1)\n                    + \":namespace=\" + projection(call(result, \"getNamespaceURI\", new Class<?>[]{}), depth + 1)\n                    + \":value=\" + projection(call(result, \"getValue\", new Class<?>[]{}), depth + 1);\n            if (name.equals(\"org.jdom.Element\") || name.equals(\"org.jdom.ProcessingInstruction\")\n                    || name.equals(\"org.jdom.Text\") || name.equals(\"org.jdom.CDATA\")) {\n                Object writer = construct(\"org.jdom.output.XMLOutputter\", new Class<?>[]{});\n                return \"xml:\" + call(writer, \"outputString\", new Class<?>[]{result.getClass()}, result);\n            }\n            if (name.equals(\"org.apache.commons.jxpath.ri.QName\")) return \"qname:\" + result.toString();\n            if (name.startsWith(\"com.google.javascript.rhino.jstype.\")) return \"js-type:\" + result.toString();\n            if (name.equals(\"com.google.javascript.jscomp.LinkedFlowScope\")) {\n                Object slot = call(result, \"getSlot\", new Class<?>[]{String.class}, \"x\");\n                return \"flow:x=\" + (slot == null ? \"absent\" : projection(call(slot, \"getType\", new Class<?>[]{}), depth + 1));\n            }\n            if (name.endsWith(\"TypeInference$BooleanOutcomePair\"))\n                return \"boolean-pair:\" + field(result, \"toBooleanOutcomes\") + ':' + field(result, \"booleanValues\")\n                    + \":left=\" + projection(field(result, \"leftScope\"), depth + 1)\n                    + \":right=\" + projection(field(result, \"rightScope\"), depth + 1);\n            if (result instanceof List) {\n                StringBuilder out = new StringBuilder(\"list[\");\n                if (((List<?>)result).size() > 256) throw new FixtureFailure(\"Oracle collection limit exceeded\", null);\n                for (Object item : (List<?>)result) out.append(projection(item, depth + 1)).append(';');\n                return out.append(']').toString();\n            }\n            if (result instanceof java.util.Map) {\n                java.util.Map<?,?> map = (java.util.Map<?,?>)result;\n                if (map.size() > 256) throw new FixtureFailure(\"Oracle map limit exceeded\", null);\n                List<String> entries = new ArrayList<String>();\n                for (java.util.Map.Entry<?,?> entry : map.entrySet())\n                    entries.add(projection(entry.getKey(), depth + 1) + \"=\" + projection(entry.getValue(), depth + 1));\n                java.util.Collections.sort(entries);\n                return \"map:\" + entries.toString();\n            }\n            if (name.startsWith(\"org.apache.commons.jxpath.ri.model.\")) {\n                Class<?> pointer = Class.forName(\"org.apache.commons.jxpath.ri.model.NodePointer\");\n                if (pointer.isInstance(result))\n                    return \"pointer:\" + projection(call(result, \"getImmediateNode\", new Class<?>[]{}), depth + 1);\n                if (Class.forName(\"org.apache.commons.jxpath.ri.model.NodeIterator\").isInstance(result)) {\n                    StringBuilder out = new StringBuilder(\"iterator[\");\n                    for (int i = 1; i <= 9; i++) {\n                        boolean present = (Boolean)call(result, \"setPosition\", new Class<?>[]{int.class}, i);\n                        if (!present) return out.append(']').toString();\n                        if (i == 9) throw new FixtureFailure(\"Oracle iterator limit exceeded\", null);\n                        out.append(projection(call(result, \"getNodePointer\", new Class<?>[]{}), depth + 1)).append(';');\n                    }\n                }\n            }\n            String simple = value(result);\n            if (simple.startsWith(\"object-type:\")) throw new FixtureFailure(\"No structural oracle: \" + name, null);\n            return simple;\n        }\n\n        String state() throws ReflectiveOperationException {\n            if (reviewed && targetClass.equals(\"org.apache.commons.codec.language.Metaphone\")\n                    && method.equals(\"setMaxCodeLen\")) {\n                int limit = ((Number)call(receiver, \"getMaxCodeLen\", new Class<?>[]{})).intValue();\n                String encoded = (String)call(receiver, \"metaphone\", new Class<?>[]{String.class}, \"architecture\");\n                return \"metaphone:maxCodeLen=\" + limit + \":encoded=\" + encoded\n                    + \":maxCodeLenAfterEncoding=\" + call(receiver, \"getMaxCodeLen\", new Class<?>[]{});\n            }\n            if (langHelpers && targetClass.equals(\"org.apache.commons.lang3.math.NumberUtils\")\n                    && method.equals(\"validateArray\")) return \"validation-input:\" + value(validationInput);\n            if (pilot && targetClass.equals(\"com.google.javascript.jscomp.RemoveUnusedVars\"))\n                return \"cleanup:\" + call(cleanupScript, \"toStringTree\", new Class<?>[]{});\n            if (pilot && targetClass.equals(\"org.jfree.chart.renderer.category.AreaRenderer\"))\n                return \"chart:rows=\" + call(chartDataset, \"getRowCount\", new Class<?>[]{}) + \":columns=\" + call(chartDataset, \"getColumnCount\", new Class<?>[]{});\n            if (pilot && targetClass.equals(\"com.fasterxml.jackson.databind.ser.BeanPropertyWriter\"))\n                return projection(receiver, 0) + \":setting=\" + projection(call(receiver, \"getInternalSetting\", new Class<?>[]{Object.class}, \"fixture-key\"), 0);\n            if (pilot && targetClass.equals(\"com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer\"))\n                return \"json-token:\" + call(parser, \"getCurrentToken\", new Class<?>[]{});\n            if (pilot && targetClass.equals(\"com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser\"))\n                return \"xml:closed=\" + call(receiver, \"isClosed\", new Class<?>[]{}) + \":token=\" + call(receiver, \"getCurrentToken\", new Class<?>[]{})\n                    + \":text=\" + projection(field(receiver, \"_currText\"), 0);\n            if (pilot && targetClass.equals(\"org.mockito.internal.invocation.InvocationMatcher\"))\n                return projection(baseInvocation, 0) + \":candidate=\" + projection(actualInvocation, 0);\n            if (pilot && targetClass.equals(\"org.apache.commons.cli.CommandLine\"))\n                return \"cli:\" + projection(call(receiver, \"getOptions\", new Class<?>[]{}), 0) + ':' + projection(call(receiver, \"getArgs\", new Class<?>[]{}), 0);\n            if (pilot && targetClass.equals(\"com.fasterxml.jackson.core.util.TextBuffer\"))\n                return \"text:\" + call(receiver, \"contentsAsString\", new Class<?>[]{}) + \":size=\" + call(receiver, \"size\", new Class<?>[]{});\n            if (pilot && targetClass.equals(\"org.jsoup.nodes.Document\") && receiver != null) return projection(receiver, 0);\n            if (pilot && targetClass.endsWith(\"CpioArchiveOutputStream\")) return \"archive:\" + value(archiveBytes.toByteArray());\n            if (pilot && targetClass.startsWith(\"org.apache.commons.math3.fraction.\")) return projection(receiver, 0);\n            if (pilot && targetClass.equals(\"org.joda.time.Partial\")) return projection(receiver, 0);\n            if (pilot && targetClass.equals(\"org.joda.time.field.UnsupportedDurationField\") && receiver != null) return projection(receiver, 0);\n            if (targetClass.equals(\"org.apache.commons.collections.map.Flat3Map\")) return projection(receiver, 0);\n            if (targetClass.equals(\"org.apache.commons.csv.ExtendedBufferedReader\"))\n                return \"reader:line=\" + call(receiver, \"getLineNumber\", new Class<?>[]{})\n                    + \":last=\" + call(receiver, \"readAgain\", new Class<?>[]{})\n                    + (bufferSlices && outputBuffer != null ? \":buffer=\" + value(outputBuffer) : \"\");\n            if (compiler != null) {\n                Object jsType = call(closureNode, \"getJSType\", new Class<?>[]{});\n                return \"ast:\" + call(closureNode, \"toStringTree\", new Class<?>[]{})\n                    + \":ast-type=\" + projection(jsType, 0) + ':' + projection(flow, 0);\n            }\n            if (domRoot != null) return nodeSnapshot(domRoot, 0) + \":child=\" + nodeSnapshot(domChild, 0)\n                    + \":attached=\" + (domChild.getParentNode() != null);\n            if (jdomRoot != null) return projection(jdomRoot, 0) + \":child=\" + projection(jdomChild, 0)\n                    + \":attached=\" + (call(jdomChild, \"getParent\", new Class<?>[]{}) != null);\n            return \"stateless-scalars\";\n        }\n    }\n\n    private static String quote(String value) {\n        StringBuilder out = new StringBuilder(\"\\\"\");\n        for (char c : value.toCharArray()) {\n            if (c == '\"' || c == '\\\\') out.append('\\\\').append(c);\n            else if (c < 32) out.append(String.format(\"\\\\u%04x\", (int)c));\n            else out.append(c);\n        }\n        return out.append('\"').toString();\n    }\n\n    private static String typeNames(Class<?>[] types) {\n        List<String> names = new ArrayList<String>();\n        for (Class<?> type : types) names.add(type.getName());\n        return String.join(\",\", names);\n    }\n\n    private static boolean scalar(Class<?> type) {\n        return type.isPrimitive() || type == String.class || type == Boolean.class\n            || type == Character.class || type == Byte.class || type == Short.class\n            || type == Integer.class || type == Long.class || type == Float.class\n            || type == Double.class || type.isEnum();\n    }\n\n    private static boolean supported(Class<?> type) {\n        return scalar(type) || (type.isArray() && scalar(type.getComponentType()));\n    }\n\n    private static boolean supportedParameters(Class<?>[] types) {\n        if (types.length > 6) return false;\n        for (Class<?> type : types) if (type == void.class) return false;\n        return true;\n    }\n\n    private static Class<?> type(String name) throws ClassNotFoundException {\n        if (name.equals(\"boolean\")) return boolean.class;\n        if (name.equals(\"byte\")) return byte.class;\n        if (name.equals(\"short\")) return short.class;\n        if (name.equals(\"int\")) return int.class;\n        if (name.equals(\"long\")) return long.class;\n        if (name.equals(\"float\")) return float.class;\n        if (name.equals(\"double\")) return double.class;\n        if (name.equals(\"char\")) return char.class;\n        return Class.forName(name);\n    }\n\n    private static Class<?>[] types(String names) throws ClassNotFoundException {\n        if (names.length() == 0) return new Class<?>[0];\n        String[] split = names.split(\",\", -1);\n        Class<?>[] result = new Class<?>[split.length];\n        for (int i = 0; i < split.length; i++) result[i] = type(split[i]);\n        return result;\n    }\n\n    private static int bucket(double coordinate, int size) {\n        double unit = Math.max(0, Math.min(1, (coordinate + 1) / 2));\n        return Math.min(size - 1, (int)(unit * size));\n    }\n\n    private static Object argument(Class<?> type, double a, double b, double c) {\n        return argument(type, a, b, c, 0);\n    }\n\n    private static Object argument(Class<?> type, double a, double b, double c, int depth) {\n        FixtureSession session = FIXTURES.get();\n        return session == null ? legacyArgument(type, a, b, c, depth) : session.argument(type, a, b, c, depth);\n    }\n\n    private static Object legacyArgument(Class<?> type, double a, double b, double c, int depth) {\n        if (depth > 2) return null;\n        if (type.isArray()) {\n            int length = bucket(c, 5);\n            Object array = Array.newInstance(type.getComponentType(), length);\n            for (int i = 0; i < length; i++) {\n                Array.set(array, i, argument(type.getComponentType(),\n                    Math.max(-1, Math.min(1, a + i * 0.17)), b, c, depth + 1));\n            }\n            return array;\n        }\n        if (!type.isPrimitive() && a < -0.96) return null;\n        if (type == String.class) {\n            int selection = bucket(a, STRINGS.length + 4);\n            if (selection < STRINGS.length) return STRINGS[selection];\n            int length = bucket(c, 33);\n            char character = \"0123456789abcdefXYZ +-_.\".charAt(bucket(b, 23));\n            char[] value = new char[length];\n            Arrays.fill(value, character);\n            return new String(value);\n        }\n        if (type == boolean.class || type == Boolean.class) return a >= 0;\n        if (type == char.class || type == Character.class) return (char)bucket(a, 128);\n        if (type.isEnum()) {\n            Object[] values = type.getEnumConstants();\n            return values.length == 0 ? null : values[bucket(a, values.length)];\n        }\n        long integer = b < 0 ? NUMBERS[bucket(a, NUMBERS.length)] : Math.round(a * 10000);\n        if (type == byte.class || type == Byte.class) return (byte)integer;\n        if (type == short.class || type == Short.class) return (short)integer;\n        if (type == int.class || type == Integer.class) return (int)integer;\n        if (type == long.class || type == Long.class) return integer;\n        double real = b < 0 ? integer : a * 1000;\n        if (type == float.class || type == Float.class) return (float)real;\n        if (type == double.class || type == Double.class) return real;\n        if (type == Number.class) return Double.valueOf(real);\n        if (type == Object.class) return b < 0 ? STRINGS[bucket(a, STRINGS.length)] : Long.valueOf(integer);\n        if (type == java.util.Date.class) return new java.util.Date(integer);\n        if (type == java.util.List.class || type == java.util.Collection.class || type == Iterable.class)\n            return new java.util.ArrayList<Object>();\n        if (type == java.util.Set.class) return new java.util.HashSet<Object>();\n        if (type == java.util.Map.class) return new java.util.HashMap<Object,Object>();\n        if (!type.isInterface() && !Modifier.isAbstract(type.getModifiers()) && !type.getName().startsWith(\"java.\")) {\n            Constructor<?>[] constructors = type.getDeclaredConstructors();\n            Arrays.sort(constructors, new Comparator<Constructor<?>>() {\n                public int compare(Constructor<?> left, Constructor<?> right) {\n                    int count = left.getParameterCount() - right.getParameterCount();\n                    return count != 0 ? count : left.toString().compareTo(right.toString());\n                }\n            });\n            for (Constructor<?> constructor : constructors) {\n                if (constructor.getParameterCount() > 3) continue;\n                try {\n                    constructor.setAccessible(true);\n                    Class<?>[] parameters = constructor.getParameterTypes();\n                    Object[] values = new Object[parameters.length];\n                    for (int i = 0; i < values.length; i++) values[i] = argument(parameters[i], a, b, c, depth + 1);\n                    return constructor.newInstance(values);\n                } catch (ReflectiveOperationException error) {\n                    // Failed fixture construction yields an explicit null boundary input.\n                } catch (RuntimeException error) {\n                    // Encapsulated/unconstructible fixture yields the same null boundary.\n                }\n            }\n        }\n        return null;\n    }\n\n    private static Object[] arguments(Class<?>[] types, double[] vector, int offset) {\n        FixtureSession explicitSession = FIXTURES.get();\n        if (explicitSession != null) {\n            Object[] helpers = explicitSession.langHelperArguments(types, vector);\n            if (helpers != null) return helpers;\n            Object[] bounded = explicitSession.boundedBufferArguments(types, vector);\n            if (bounded != null) return bounded;\n        }\n        Object[] values = new Object[types.length];\n        for (int i = 0; i < types.length; i++) {\n            int start = offset + 3 * i;\n            values[i] = argument(types[i], vector[start % vector.length],\n                vector[(start + 1) % vector.length], vector[(start + 2) % vector.length]);\n        }\n        FixtureSession session = FIXTURES.get();\n        if (session != null && session.pilot && !session.constructing) {\n            if (session.targetClass.equals(\"com.google.gson.TypeInfoFactory\")) {\n                java.lang.reflect.Type parent = vector[0] < 0 ? StringBinding.class.getGenericSuperclass() : IntegerBinding.class.getGenericSuperclass();\n                try {\n                    if (session.method.equals(\"getActualType\")) {\n                        values[0] = GenericFixture.class.getField(\"items\").getGenericType();\n                        values[1] = parent;\n                        values[2] = GenericFixture.class;\n                    } else if (session.method.equals(\"extractRealTypes\")) {\n                        values[0] = new java.lang.reflect.Type[]{GenericFixture.class.getField(\"value\").getGenericType()};\n                        values[1] = parent;\n                        values[2] = GenericFixture.class;\n                    }\n                } catch (NoSuchFieldException failure) { throw new FixtureFailure(\"Generic schema field missing\", failure); }\n            }\n            if (session.targetClass.equals(\"org.jfree.chart.renderer.category.AreaRenderer\") && session.method.equals(\"getItemMiddle\")) {\n                values[0] = \"row-a\";\n                values[1] = \"column-a\";\n            }\n        }\n        return values;\n    }\n\n    private static String value(Object value) {\n        if (value == null) return \"null\";\n        Class<?> type = value.getClass();\n        if (type.isArray()) {\n            StringBuilder out = new StringBuilder(type.getName()).append('[');\n            int length = Array.getLength(value);\n            if (length > 100000) throw new IllegalStateException(\"SQA_HARNESS oversized outcome\");\n            for (int i = 0; i < length; i++) out.append(value(Array.get(value, i))).append(';');\n            return out.append(']').toString();\n        }\n        if (value instanceof Class) return \"class:\" + nestedTestName(((Class<?>)value).getName());\n        if (!scalar(type) && !(value instanceof Number)) return \"object-type:\" + type.getName();\n        String text = value instanceof Enum ? ((Enum<?>) value).name() : String.valueOf(value);\n        return type.getName() + \":\" + Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));\n    }\n\n    private static String nestedTestName(String text) {\n        // GeneratedStudyTest nests a copy of this helper, so probe-time\n        // \"SqaProbe$FixtureMock\" renders at test runtime as\n        // \"GeneratedStudyTest$SqaProbe$FixtureMock\". Oracles must compare\n        // the probe-time spelling in both phases; never edit old suites.\n        return text.replace(\"GeneratedStudyTest$SqaProbe$\", \"SqaProbe$\");\n    }\n\n    private static String snapshot(String observed) {\n        // JVM string constants are limited to 65,535 encoded bytes. Long exact\n        // observations use a deterministic digest rather than enormous literals.\n        if (observed.length() <= 16000) return observed;\n        byte[] bytes = observed.getBytes(StandardCharsets.UTF_8);\n        try {\n            byte[] digest = MessageDigest.getInstance(\"SHA-256\").digest(bytes);\n            StringBuilder hex = new StringBuilder();\n            for (byte item : digest) hex.append(String.format(\"%02x\", item & 255));\n            return \"sha256:\" + hex + \":bytes:\" + bytes.length;\n        } catch (NoSuchAlgorithmException error) {\n            throw new IllegalStateException(\"SQA_HARNESS SHA-256 unavailable\", error);\n        }\n    }\n\n    public static String observe(String className, String constructorTypes, String methodName,\n                                 String methodTypes, double[] vector) {\n        INVOKED.set(false);\n        if (vector.length == 0) throw new IllegalArgumentException(\"SQA_HARNESS empty vector\");\n        try {\n            Class<?> target = Class.forName(className);\n            Class<?>[] ctorTypes = types(constructorTypes);\n            Class<?>[] parameterTypes = types(methodTypes);\n            Object receiver = null;\n            Method method = null;\n            if (!methodName.equals(\"<init>\")) {\n                Class<?> declaring = target;\n                while (declaring != null) {\n                    try { method = declaring.getDeclaredMethod(methodName, parameterTypes); break; }\n                    catch (NoSuchMethodException missing) { declaring = declaring.getSuperclass(); }\n                }\n                if (method == null) throw new NoSuchMethodException(methodName);\n                method.setAccessible(true);\n            }\n            if (method == null || !Modifier.isStatic(method.getModifiers())) {\n                Constructor<?> ctor = target.getDeclaredConstructor(ctorTypes);\n                ctor.setAccessible(true);\n                FixtureSession session = FIXTURES.get();\n                if (session != null) session.constructing = true;\n                try {\n                    Object[] values = arguments(ctorTypes, vector, 0);\n                    if (method == null) INVOKED.set(true);\n                    receiver = ctor.newInstance(values);\n                    if (session != null) receiver = session.prepareReceiver(receiver, vector[0]);\n                    if (session != null) session.receiver = receiver;\n                    if (session != null && className.equals(\"org.apache.commons.collections.map.Flat3Map\")) {\n                        call(receiver, \"put\", new Class<?>[]{Object.class, Object.class}, \"fixture-a\", \"value-a\");\n                        call(receiver, \"put\", new Class<?>[]{Object.class, Object.class}, \"fixture-b\", \"value-b\");\n                    }\n                    if (session != null && className.startsWith(\"org.apache.commons.jxpath.ri.model.\")) session.configurePointer(receiver);\n                } catch (InvocationTargetException error) {\n                    if (session != null && method != null)\n                        throw new FixtureFailure(\"Receiver constructor failed before method invocation\", error.getCause());\n                    throw error;\n                } finally { if (session != null) session.constructing = false; }\n            }\n            if (method == null) {\n                if (FIXTURES.get() == null) return \"constructed:\" + target.getName();\n                try { return snapshot(\"constructed:\" + target.getName() + \":state=\" + FIXTURES.get().state()); }\n                catch (ReflectiveOperationException failure) { throw new FixtureFailure(\"Constructor state oracle failed\", failure); }\n            }\n            Object[] values = arguments(parameterTypes, vector, ctorTypes.length * 3);\n            INVOKED.set(true);\n            Object result = method.invoke(receiver, values);\n            if (FIXTURES.get() != null) {\n                FixtureSession session = FIXTURES.get();\n                try {\n                    return snapshot((method.getReturnType() == void.class ? \"void\" : \"value:\" + session.projection(result, 0))\n                            + \"|state=\" + session.state());\n                } catch (ReflectiveOperationException failure) { throw new FixtureFailure(\"Structural oracle failed\", failure); }\n            }\n            return method.getReturnType() == void.class ? \"void\" : snapshot(\"value:\" + value(result));\n        } catch (InvocationTargetException error) {\n            Throwable cause = error.getCause();\n            if (cause instanceof VirtualMachineError || cause instanceof LinkageError || cause instanceof ThreadDeath)\n                throw new IllegalStateException(\"SQA_HARNESS JVM failure\", cause);\n            FixtureSession session = FIXTURES.get();\n            if (session != null && session.langHelpers && session.targetClass.equals(\"org.apache.commons.lang3.math.NumberUtils\")\n                    && session.method.equals(\"validateArray\")) {\n                try { return \"exception:\" + cause.getClass().getName() + \"|message=\" + value(cause.getMessage())\n                        + \"|state=\" + session.state(); }\n                catch (ReflectiveOperationException failure) { throw new FixtureFailure(\"Validation boundary oracle failed\", failure); }\n            }\n            return \"exception:\" + cause.getClass().getName();\n        } catch (ReflectiveOperationException error) {\n            throw new IllegalStateException(\"SQA_HARNESS reflection failure\", error);\n        } catch (LinkageError error) {\n            throw new IllegalStateException(\"SQA_HARNESS linkage failure\", error);\n        }\n    }\n\n    public static String observeWithPolicy(String className, String constructorTypes, String methodName,\n            String methodTypes, double[] vector, String policy) {\n        if (!EXPLICIT_FIXTURES.equals(policy) && !SCALAR_FIXTURES.equals(policy)\n                && !PILOT_FIXTURES.equals(policy) && !BUFFER_FIXTURES.equals(policy)\n                && !FRACTION_FIELD_FIXTURES.equals(policy) && !LANG_HELPER_FIXTURES.equals(policy) && !JOINT_FIXTURES.equals(policy)\n                && !CHRONOLOGY_FIXTURES.equals(policy) && !GRAPHICS_FIXTURES.equals(policy) && !CODEC_FIXTURES.equals(policy))\n            throw new IllegalArgumentException(\"Unknown explicit fixture policy\");\n        FIXTURES.set(new FixtureSession(className, methodName, policy));\n        try {\n            if (CODEC_FIXTURES.equals(policy) && codecIdentity(className,constructorTypes,methodName,methodTypes))\n                return CodecRecipe.observe(methodName,vector);\n            if ((GRAPHICS_FIXTURES.equals(policy) || CODEC_FIXTURES.equals(policy)) && graphicsIdentity(className, constructorTypes, methodName, methodTypes))\n                return observeGraphics(methodName, vector);\n            if ((CHRONOLOGY_FIXTURES.equals(policy) || GRAPHICS_FIXTURES.equals(policy) || CODEC_FIXTURES.equals(policy)) && className.equals(\"org.joda.time.Partial\")\n                    && chronologyIdentity(constructorTypes, methodName, methodTypes))\n                return observeChronology(constructorTypes, methodName, methodTypes, vector);\n            return observe(className, constructorTypes, methodName, methodTypes, vector);\n        }\n        finally { FIXTURES.remove(); }\n    }\n\n    private static boolean codecIdentity(String owner,String ctor,String method,String params){\n        if(!ctor.isEmpty())return false;\n        if(owner.equals(\"org.apache.commons.codec.language.Metaphone\") && method.equals(\"isNextChar\"))return params.equals(\"java.lang.StringBuffer,int,char\");\n        if(owner.equals(\"org.apache.commons.codec.language.Metaphone\") && method.equals(\"isPreviousChar\"))return params.equals(\"java.lang.StringBuffer,int,char\");\n        if(owner.equals(\"org.apache.commons.codec.language.Metaphone\") && method.equals(\"isVowel\"))return params.equals(\"java.lang.StringBuffer,int\");\n        if(owner.equals(\"org.apache.commons.codec.language.Metaphone\") && method.equals(\"regionMatch\"))return params.equals(\"java.lang.StringBuffer,int,java.lang.String\");\n        if(owner.equals(\"org.apache.commons.codec.language.SoundexUtils\") && method.equals(\"difference\"))return params.equals(\"org.apache.commons.codec.StringEncoder,java.lang.String,java.lang.String\");\n        return false;\n    }\n    /** Jointly scoped Codec5 only. Data/oracles predeclared before production execution. */\n    public static final class CodecRecipe {\n    public static String activeCase = \"\";\n    static String q(String s) { return \"\\\"\"+s.replace(\"\\\\\",\"\\\\\\\\\").replace(\"\\\"\",\"\\\\\\\"\")+\"\\\"\"; }\n    static String json(Object o) {\n        if(o==null)return \"null\";\n        if(o instanceof String)return q((String)o);\n        if(o instanceof Map) {\n            StringBuilder b=new StringBuilder(\"{\");\n            for(Object entry:((Map)o).entrySet()) {\n                Map.Entry e=(Map.Entry)entry;if(b.length()>1)b.append(',');\n                b.append(q((String)e.getKey())).append(':').append(json(e.getValue()));\n            }return b.append('}').toString();\n        }\n        if(o instanceof List) {\n            StringBuilder b=new StringBuilder(\"[\");\n            for(Object item:(List)o){if(b.length()>1)b.append(',');b.append(json(item));}\n            return b.append(']').toString();\n        }return String.valueOf(o);\n    }\n    static Map<String,Object> map(Object... kv) {\n        Map<String,Object> r=new LinkedHashMap<String,Object>();\n        for(int i=0;i<kv.length;i+=2)r.put((String)kv[i],kv[i+1]);return r;\n    }\n    static String string(String encoded) {\n        return encoded.equals(\"-\")?null:new String(Base64.getDecoder().decode(encoded),StandardCharsets.UTF_8);\n    }\n    static String type(Class<?> c) {\n        if(c==boolean.class)return \"Z\";if(c==int.class)return \"I\";if(c==char.class)return \"C\";\n        return \"L\"+c.getName().replace('.','/')+\";\";\n    }\n    static String descriptor(Method m) {\n        StringBuilder b=new StringBuilder(\"(\");for(Class<?> c:m.getParameterTypes())b.append(type(c));\n        return b.append(')').append(type(m.getReturnType())).toString();\n    }\n    static void check(boolean ok,String reason){if(!ok)throw new AssertionError(reason);}\n    static Map<String,Object> run(String[] c) throws Exception {\n        String name=c[0],method=c[1],text=string(c[2]),needle=string(c[5]),s1=string(c[6]),s2=string(c[7]);\n        int index=Integer.parseInt(c[3]);char ch=(char)Integer.parseInt(c[4]);\n        Object expectedValue=c[8].equals(\"null\")?null:(c[8].equals(\"true\")?Boolean.TRUE:\n              (c[8].equals(\"false\")?Boolean.FALSE:Integer.valueOf(c[8])));\n        String expectedException=c[9].equals(\"-\")?null:c[9];\n        StringBuffer buffer=text==null?null:new StringBuffer(text);\n        Object receiver;Object encoder=null;Class<?> owner;Class<?>[] types;Object[] args;\n        Object encoded=null;\n        if(method.equals(\"difference\")) {\n            owner=Class.forName(\"org.apache.commons.codec.language.SoundexUtils\");\n            Constructor<?> ctor=owner.getDeclaredConstructor();ctor.setAccessible(true);receiver=ctor.newInstance();\n            encoder=construct(\"org.apache.commons.codec.language.Metaphone\", new Class<?>[]{});check(Integer.valueOf(4).equals(call(encoder,\"getMaxCodeLen\",new Class<?>[]{})),\"Exact encoder initial state\");\n            // Independent literal encoded references are fixture preconditions, never the result oracle.\n            List<String> pre=Arrays.asList((String)call(encoder,\"encode\",new Class<?>[]{String.class},s1),(String)call(encoder,\"encode\",new Class<?>[]{String.class},s2));\n            List<String> refs=Arrays.asList(string(c[10]),string(c[11]));\n            check(pre.equals(refs),\"Real production encoder differs from declared literal references\");encoded=pre;\n            types=new Class<?>[]{Class.forName(\"org.apache.commons.codec.StringEncoder\"),String.class,String.class};args=new Object[]{encoder,s1,s2};\n        } else {\n            owner=Class.forName(\"org.apache.commons.codec.language.Metaphone\");receiver=owner.getDeclaredConstructor().newInstance();\n            check(Integer.valueOf(4).equals(call(receiver,\"getMaxCodeLen\",new Class<?>[]{})),\n                \"Exact receiver initial state\");\n            check(buffer!=null,\"Bounded helpers require real non-null StringBuffer\");\n            if(method.equals(\"isVowel\")){types=new Class<?>[]{StringBuffer.class,int.class};args=new Object[]{buffer,index};}\n            else if(method.equals(\"regionMatch\")){types=new Class<?>[]{StringBuffer.class,int.class,String.class};args=new Object[]{buffer,index,needle};}\n            else {types=new Class<?>[]{StringBuffer.class,int.class,char.class};args=new Object[]{buffer,index,ch};}\n        }\n        check(receiver.getClass()==owner,\"Exact production receiver identity\");\n        Method target=owner.getDeclaredMethod(method,types);target.setAccessible(true);\n        check(target.getDeclaringClass()==owner,\"Exact declaring class\");\n        check(Modifier.isStatic(target.getModifiers())==method.equals(\"difference\"),\"Static/instance identity\");\n        String before=buffer==null?null:buffer.toString();int capacity=buffer==null?0:buffer.capacity();\n        Object value=null;Throwable thrown=null;\n        activeCase=name; INVOKED.set(true);\n        try{value=target.invoke(receiver,args);}catch(InvocationTargetException error){thrown=error.getCause();}\n        finally{activeCase=\"\";}\n        if(thrown instanceof VirtualMachineError || thrown instanceof LinkageError || thrown instanceof ThreadDeath)\n            throw new FixtureFailure(\"SQA_HARNESS Codec target environment failure\",thrown);\n        boolean unchanged=buffer==null||(buffer.toString().equals(before)&&buffer.length()==before.length()&&buffer.capacity()==capacity);\n        Object receiverMax=receiver.getClass().getName().endsWith(\"Metaphone\")?call(receiver,\"getMaxCodeLen\",new Class<?>[]{}):null;\n        Object encoderMax=encoder==null?null:call(encoder,\"getMaxCodeLen\",new Class<?>[]{});\n        Map<String,Object> actual=map(\"value\",value,\"exception_class\",thrown==null?null:thrown.getClass().getName(),\n            \"buffer_contents\",buffer==null?null:buffer.toString(),\"buffer_length\",buffer==null?null:buffer.length(),\n            \"buffer_unchanged\",unchanged,\"receiver_max_code_len\",receiverMax,\"encoder_max_code_len\",encoderMax,\n            \"encoder_encoded_inputs\",encoded);\n        Map<String,Object> expected=map(\"value\",expectedValue,\"exception_class\",expectedException,\n            \"buffer_contents\",text,\"buffer_length\",text==null?null:text.length(),\"buffer_unchanged\",true,\n            \"receiver_max_code_len\",method.equals(\"difference\")?null:4,\"encoder_max_code_len\",method.equals(\"difference\")?4:null,\n            \"encoder_encoded_inputs\",method.equals(\"difference\")?Arrays.asList(string(c[10]),string(c[11])):null);\n        boolean passed=json(actual).equals(json(expected));\n        return map(\"case\",name,\"method\",method,\"receiver_class\",receiver.getClass().getName(),\n            \"declaring_class\",target.getDeclaringClass().getName(),\"descriptor\",descriptor(target),\n            \"setup_succeeded\",true,\"target_invoked\",true,\"target_check_passed\",passed,\n            \"failure_class\",passed?null:\"java.lang.AssertionError\",\"failure_reason\",passed?null:\"Declared value/state oracle differs\",\n            \"observation\",actual,\"expected_observation\",expected,\n            \"pre_state\",map(\"buffer_contents\",before,\"buffer_length\",before==null?null:before.length(),\"buffer_capacity\",buffer==null?null:capacity),\n            \"post_state\",map(\"buffer_contents\",buffer==null?null:buffer.toString(),\"buffer_length\",buffer==null?null:buffer.length(),\"buffer_capacity\",buffer==null?null:buffer.capacity()));\n    }\n    public static Map<String,Object> lastEvidence=null;\n    private static final String[][] CASES={\n        {\"isNextChar_match_first\", \"isNextChar\", \"QUJDQQ==\", \"0\", \"66\", \"\", \"-\", \"-\", \"true\", \"-\", \"-\", \"-\"},\n        {\"isNextChar_match_middle\", \"isNextChar\", \"QUJDQQ==\", \"1\", \"67\", \"\", \"-\", \"-\", \"true\", \"-\", \"-\", \"-\"},\n        {\"isNextChar_mismatch\", \"isNextChar\", \"QUJDQQ==\", \"0\", \"65\", \"\", \"-\", \"-\", \"false\", \"-\", \"-\", \"-\"},\n        {\"isNextChar_negative\", \"isNextChar\", \"QUJDQQ==\", \"-1\", \"65\", \"\", \"-\", \"-\", \"false\", \"-\", \"-\", \"-\"},\n        {\"isNextChar_last\", \"isNextChar\", \"QUJDQQ==\", \"3\", \"65\", \"\", \"-\", \"-\", \"false\", \"-\", \"-\", \"-\"},\n        {\"isNextChar_at_length\", \"isNextChar\", \"QUJDQQ==\", \"4\", \"65\", \"\", \"-\", \"-\", \"false\", \"-\", \"-\", \"-\"},\n        {\"isNextChar_empty\", \"isNextChar\", \"\", \"0\", \"65\", \"\", \"-\", \"-\", \"false\", \"-\", \"-\", \"-\"},\n        {\"isNextChar_single\", \"isNextChar\", \"QQ==\", \"0\", \"65\", \"\", \"-\", \"-\", \"false\", \"-\", \"-\", \"-\"},\n        {\"isPreviousChar_match_first\", \"isPreviousChar\", \"QUJDQQ==\", \"1\", \"65\", \"\", \"-\", \"-\", \"true\", \"-\", \"-\", \"-\"},\n        {\"isPreviousChar_match_last\", \"isPreviousChar\", \"QUJDQQ==\", \"3\", \"67\", \"\", \"-\", \"-\", \"true\", \"-\", \"-\", \"-\"},\n        {\"isPreviousChar_mismatch\", \"isPreviousChar\", \"QUJDQQ==\", \"1\", \"67\", \"\", \"-\", \"-\", \"false\", \"-\", \"-\", \"-\"},\n        {\"isPreviousChar_negative\", \"isPreviousChar\", \"QUJDQQ==\", \"-1\", \"65\", \"\", \"-\", \"-\", \"false\", \"-\", \"-\", \"-\"},\n        {\"isPreviousChar_first\", \"isPreviousChar\", \"QUJDQQ==\", \"0\", \"65\", \"\", \"-\", \"-\", \"false\", \"-\", \"-\", \"-\"},\n        {\"isPreviousChar_at_length\", \"isPreviousChar\", \"QUJDQQ==\", \"4\", \"65\", \"\", \"-\", \"-\", \"false\", \"-\", \"-\", \"-\"},\n        {\"isPreviousChar_empty\", \"isPreviousChar\", \"\", \"0\", \"65\", \"\", \"-\", \"-\", \"false\", \"-\", \"-\", \"-\"},\n        {\"isPreviousChar_single\", \"isPreviousChar\", \"QQ==\", \"0\", \"65\", \"\", \"-\", \"-\", \"false\", \"-\", \"-\", \"-\"},\n        {\"vowel_A\", \"isVowel\", \"QUVJT1VC\", \"0\", \"65\", \"\", \"-\", \"-\", \"true\", \"-\", \"-\", \"-\"},\n        {\"vowel_E\", \"isVowel\", \"QUVJT1VC\", \"1\", \"65\", \"\", \"-\", \"-\", \"true\", \"-\", \"-\", \"-\"},\n        {\"vowel_I\", \"isVowel\", \"QUVJT1VC\", \"2\", \"65\", \"\", \"-\", \"-\", \"true\", \"-\", \"-\", \"-\"},\n        {\"vowel_O\", \"isVowel\", \"QUVJT1VC\", \"3\", \"65\", \"\", \"-\", \"-\", \"true\", \"-\", \"-\", \"-\"},\n        {\"vowel_U\", \"isVowel\", \"QUVJT1VC\", \"4\", \"65\", \"\", \"-\", \"-\", \"true\", \"-\", \"-\", \"-\"},\n        {\"vowel_B\", \"isVowel\", \"QUVJT1VC\", \"5\", \"65\", \"\", \"-\", \"-\", \"false\", \"-\", \"-\", \"-\"},\n        {\"vowel_empty\", \"isVowel\", \"\", \"0\", \"65\", \"\", \"-\", \"-\", \"null\", \"java.lang.StringIndexOutOfBoundsException\", \"-\", \"-\"},\n        {\"vowel_negative\", \"isVowel\", \"QQ==\", \"-1\", \"65\", \"\", \"-\", \"-\", \"null\", \"java.lang.StringIndexOutOfBoundsException\", \"-\", \"-\"},\n        {\"vowel_at_length\", \"isVowel\", \"QQ==\", \"1\", \"65\", \"\", \"-\", \"-\", \"null\", \"java.lang.StringIndexOutOfBoundsException\", \"-\", \"-\"},\n        {\"region_prefix\", \"regionMatch\", \"QUJDQQ==\", \"0\", \"65\", \"QUI=\", \"-\", \"-\", \"true\", \"-\", \"-\", \"-\"},\n        {\"region_middle\", \"regionMatch\", \"QUJDQQ==\", \"1\", \"65\", \"QkM=\", \"-\", \"-\", \"true\", \"-\", \"-\", \"-\"},\n        {\"region_last\", \"regionMatch\", \"QUJDQQ==\", \"3\", \"65\", \"QQ==\", \"-\", \"-\", \"true\", \"-\", \"-\", \"-\"},\n        {\"region_mismatch\", \"regionMatch\", \"QUJDQQ==\", \"1\", \"65\", \"QkE=\", \"-\", \"-\", \"false\", \"-\", \"-\", \"-\"},\n        {\"region_too_long\", \"regionMatch\", \"QUJDQQ==\", \"3\", \"65\", \"QUI=\", \"-\", \"-\", \"false\", \"-\", \"-\", \"-\"},\n        {\"region_negative\", \"regionMatch\", \"QUJDQQ==\", \"-1\", \"65\", \"QQ==\", \"-\", \"-\", \"false\", \"-\", \"-\", \"-\"},\n        {\"region_empty_end\", \"regionMatch\", \"QUJDQQ==\", \"4\", \"65\", \"\", \"-\", \"-\", \"true\", \"-\", \"-\", \"-\"},\n        {\"region_empty_buffer\", \"regionMatch\", \"\", \"0\", \"65\", \"\", \"-\", \"-\", \"true\", \"-\", \"-\", \"-\"},\n        {\"region_empty_beyond_end\", \"regionMatch\", \"QUJDQQ==\", \"5\", \"65\", \"\", \"-\", \"-\", \"false\", \"-\", \"-\", \"-\"},\n        {\"difference_equal\", \"difference\", \"-\", \"0\", \"65\", \"\", \"QQ==\", \"QQ==\", \"1\", \"-\", \"QQ==\", \"QQ==\"},\n        {\"difference_case_fold\", \"difference\", \"-\", \"0\", \"65\", \"\", \"YQ==\", \"QQ==\", \"1\", \"-\", \"QQ==\", \"QQ==\"},\n        {\"difference_different\", \"difference\", \"-\", \"0\", \"65\", \"\", \"QQ==\", \"RQ==\", \"0\", \"-\", \"QQ==\", \"RQ==\"},\n        {\"difference_null_left\", \"difference\", \"-\", \"0\", \"65\", \"\", \"-\", \"QQ==\", \"0\", \"-\", \"\", \"QQ==\"},\n        {\"difference_empty_both\", \"difference\", \"-\", \"0\", \"65\", \"\", \"\", \"\", \"0\", \"-\", \"\", \"\"},\n        {\"difference_null_both\", \"difference\", \"-\", \"0\", \"65\", \"\", \"-\", \"-\", \"0\", \"-\", \"\", \"\"},\n        {\"difference_two_equal\", \"difference\", \"-\", \"0\", \"65\", \"\", \"QUI=\", \"QUI=\", \"2\", \"-\", \"QUI=\", \"QUI=\"},\n        {\"difference_shorter_right\", \"difference\", \"-\", \"0\", \"65\", \"\", \"QUI=\", \"QQ==\", \"1\", \"-\", \"QUI=\", \"QQ==\"},\n        {\"difference_two_mismatch\", \"difference\", \"-\", \"0\", \"65\", \"\", \"QUI=\", \"Qg==\", \"0\", \"-\", \"QUI=\", \"Qg==\"}\n    };\n    public static synchronized String observe(String method, double[] vector) {\n        INVOKED.set(false); lastEvidence=null; activeCase=\"\";\n        try {\n            if(vector.length==0 || !Double.isFinite(vector[0]))throw new IllegalArgumentException(\"Codec needs finite vector\");\n            List<String[]> options=new ArrayList<String[]>();\n            for(String[] row:CASES)if(row[1].equals(method))options.add(row);\n            int slot=Math.min(options.size()-1,(int)Math.floor((Math.max(-1.0,Math.min(1.0,vector[0]))+1)*options.size()/2));\n            lastEvidence=run(options.get(slot));\n            return json(lastEvidence.get(\"observation\"));\n        } catch(FixtureFailure failure){throw failure;}\n        catch(Throwable failure){throw new FixtureFailure(\"SQA_HARNESS Codec setup/projection failed\",failure);}\n        finally{activeCase=\"\";}\n    }\n    }\n\n    private static boolean chronologyIdentity(String ctor, String method, String params) {\n        if (method.equals(\"<init>\") && params.isEmpty()) return ctor.equals(\"org.joda.time.Chronology\")\n            || ctor.equals(\"org.joda.time.DateTimeFieldType,int,org.joda.time.Chronology\")\n            || ctor.equals(\"[Lorg.joda.time.DateTimeFieldType;,[I,org.joda.time.Chronology\")\n            || ctor.equals(\"org.joda.time.Chronology,[Lorg.joda.time.DateTimeFieldType;,[I\");\n        return ctor.isEmpty() && ((method.equals(\"getField\") && params.equals(\"int,org.joda.time.Chronology\"))\n            || (method.equals(\"withChronologyRetainFields\") && params.equals(\"org.joda.time.Chronology\")));\n    }\n\n    private static String partialState(Object partial) throws ReflectiveOperationException {\n        Object chrono = call(partial,\"getChronology\",new Class<?>[]{});\n        Object zone = call(chrono,\"getZone\",new Class<?>[]{});\n        int size = (Integer)call(partial,\"size\",new Class<?>[]{});\n        List<String> names = new ArrayList<String>();\n        List<Integer> values = new ArrayList<Integer>();\n        boolean named = true;\n        for (int i=0; i<size; i++) {\n            Object type = call(partial,\"getFieldType\",new Class<?>[]{int.class},i);\n            names.add((String)call(type,\"getName\",new Class<?>[]{}));\n            Integer indexed = (Integer)call(partial,\"getValue\",new Class<?>[]{int.class},i);\n            values.add(indexed);\n            named &= indexed.equals(call(partial,\"get\",types(\"org.joda.time.DateTimeFieldType\"),type));\n        }\n        return \"partial:\"+chrono.getClass().getName()+\":\"+call(zone,\"getID\",new Class<?>[]{})\n            +\":types=\"+names+\":values=\"+values+\":named=\"+named;\n    }\n\n    /** Six bounded production identities only, separate from all historical policies.\n     * Vector[0] chooses a declared case, not arbitrary legal-domain approval.\n     * Reflection enters the exact protected/internal target on real final Partial.\n     */\n    private static synchronized String observeChronology(String ctor, String method, String params, double[] vector) {\n        INVOKED.set(false);\n        chronologyActiveCase = \"\";\n        try {\n            if (vector.length==0 || !Double.isFinite(vector[0]))\n                throw new IllegalArgumentException(\"Chronology needs a finite vector\");\n            System.setProperty(\"org.joda.time.DateTimeZone.Provider\",\"org.joda.time.tz.UTCProvider\");\n            java.util.TimeZone.setDefault(java.util.TimeZone.getTimeZone(\"UTC\"));\n            Class<?> partial = Class.forName(\"org.joda.time.Partial\");\n            Class<?> chronoType = Class.forName(\"org.joda.time.Chronology\");\n            Class<?> fieldType = Class.forName(\"org.joda.time.DateTimeFieldType\");\n            Class<?> zoneType = Class.forName(\"org.joda.time.DateTimeZone\");\n            Object provider = Class.forName(\"org.joda.time.tz.UTCProvider\").getDeclaredConstructor().newInstance();\n            call(zoneType,\"setProvider\",types(\"org.joda.time.tz.Provider\"),provider);\n            Object utc = zoneType.getField(\"UTC\").get(null);\n            call(zoneType,\"setDefault\",new Class<?>[]{zoneType},utc);\n            Object offset = call(zoneType,\"forOffsetHours\",new Class<?>[]{int.class},7);\n            Object iso = call(Class.forName(\"org.joda.time.chrono.ISOChronology\"),\"getInstance\",new Class<?>[]{zoneType},utc);\n            Object isoOffset = call(Class.forName(\"org.joda.time.chrono.ISOChronology\"),\"getInstance\",new Class<?>[]{zoneType},offset);\n            Object buddhist = call(Class.forName(\"org.joda.time.chrono.BuddhistChronology\"),\"getInstance\",new Class<?>[]{zoneType},offset);\n            Object year = call(fieldType,\"year\",new Class<?>[]{});\n            Object month = call(fieldType,\"monthOfYear\",new Class<?>[]{});\n            Object day = call(fieldType,\"dayOfMonth\",new Class<?>[]{});\n            Object hour = call(fieldType,\"hourOfDay\",new Class<?>[]{});\n            Object era = call(fieldType,\"era\",new Class<?>[]{});\n            // All three bounded cases occupy intervals within the generators'\n            // shared [-1,1] domain (including CMA-ES/FSCS-ART proposals).\n            int bucket = Math.min(2, (int)Math.floor((Math.max(-1.0,Math.min(1.0,vector[0]))+1.0)*1.5));\n            String caseName;\n            Object receiver = null, result = null;\n            Object[] args;\n            Object inputTypes = null; int[] inputValues = null;\n            String before = null;\n            Constructor<?> constructor = null;\n            Method targetMethod = null;\n            if (method.equals(\"<init>\")) {\n                constructor = partial.getDeclaredConstructor(types(ctor));\n                constructor.setAccessible(true);\n                if (ctor.equals(\"org.joda.time.Chronology\")) {\n                    caseName = bucket==0 ? \"empty_iso_offset\" : \"empty_null\";\n                    args = new Object[]{bucket==0 ? isoOffset : null};\n                } else if (ctor.equals(\"org.joda.time.DateTimeFieldType,int,org.joda.time.Chronology\")) {\n                    caseName = bucket==0 ? \"single_hour_iso\" : \"single_invalid_hour\";\n                    args = new Object[]{hour,bucket==0 ? 10 : 24,isoOffset};\n                } else {\n                    boolean internal = ctor.startsWith(\"org.joda.time.Chronology,\");\n                    caseName = internal ? \"internal_iso\" : bucket==0 ? \"arrays_leap_iso\"\n                        : bucket==1 ? \"arrays_invalid_date\" : \"arrays_bad_order\";\n                    inputTypes = Array.newInstance(fieldType,3);\n                    Object[] chosen = !internal && bucket==2 ? new Object[]{year,day,era} : new Object[]{year,month,day};\n                    for (int i=0;i<3;i++) Array.set(inputTypes,i,chosen[i]);\n                    inputValues = !internal && bucket==2 ? new int[]{1,1,1} : new int[]{2024,2,!internal && bucket==1 ? 30 : 29};\n                    if (internal) {\n                        Object validated = partial.getDeclaredConstructor(types(\"[Lorg.joda.time.DateTimeFieldType;,[I,org.joda.time.Chronology\"))\n                            .newInstance(inputTypes,inputValues,iso);\n                        inputTypes = call(validated,\"getFieldTypes\",new Class<?>[]{});\n                        inputValues = (int[])call(validated,\"getValues\",new Class<?>[]{});\n                        args = new Object[]{iso,inputTypes,inputValues};\n                    } else args = new Object[]{inputTypes,inputValues,isoOffset};\n                }\n            } else {\n                receiver = partial.getDeclaredConstructor().newInstance();\n                boolean field = method.equals(\"getField\");\n                receiver = call(receiver,\"with\",new Class<?>[]{fieldType,int.class},field ? year : hour,field ? 2024 : 10);\n                if (!field && bucket==2) receiver = call(receiver,\"withChronologyRetainFields\",new Class<?>[]{chronoType},buddhist);\n                before = partialState(receiver);\n                String expected = \"partial:org.joda.time.chrono.\"+(!field && bucket==2 ? \"BuddhistChronology\" : \"ISOChronology\")\n                    +\":UTC:types=[\"+(field ? \"year\" : \"hourOfDay\")+\"]:values=[\"+(field ? 2024 : 10)+\"]:named=true\";\n                if (!before.equals(expected)) throw new IllegalStateException(\"Default receiver seed differs\");\n                targetMethod = partial.getDeclaredMethod(method,types(params));\n                targetMethod.setAccessible(true);\n                if (field) {\n                    caseName = bucket==0 ? \"getfield_buddhist\" : \"getfield_bad_index\";\n                    args = new Object[]{bucket==0 ? 0 : 1,buddhist};\n                } else {\n                    caseName = bucket==0 ? \"withchrono_buddhist\" : bucket==1 ? \"withchrono_same\" : \"withchrono_null\";\n                    args = new Object[]{bucket==0 ? buddhist : bucket==1 ? isoOffset : null};\n                }\n            }\n            Throwable targetException = null;\n            chronologyActiveCase = caseName;\n            INVOKED.set(true);\n            try { result = constructor!=null ? constructor.newInstance(args) : targetMethod.invoke(receiver,args); }\n            catch (InvocationTargetException failure) { targetException = failure.getCause(); }\n            finally { chronologyActiveCase = \"\"; }\n            if (targetException!=null) {\n                if (targetException instanceof VirtualMachineError || targetException instanceof LinkageError || targetException instanceof ThreadDeath)\n                    throw new IllegalStateException(\"SQA_HARNESS JVM failure\",targetException);\n                String out = \"exception:\"+targetException.getClass().getName();\n                if (receiver!=null) out += \"|receiver=\"+partialState(receiver)+\":unchanged=\"+before.equals(partialState(receiver));\n                return out;\n            }\n            if (method.equals(\"getField\")) return \"field:\"+call(result,\"getName\",new Class<?>[]{})\n                +\":epoch=\"+call(result,\"get\",new Class<?>[]{long.class},0L)\n                +\":supplied-identity=\"+(result==call(buddhist,\"year\",new Class<?>[]{}))\n                +\":type-year=\"+(call(result,\"getType\",new Class<?>[]{})==year)\n                +\"|receiver=\"+partialState(receiver)+\":unchanged=\"+before.equals(partialState(receiver));\n            String out = partialState(result);\n            if (method.equals(\"withChronologyRetainFields\"))\n                return out+\":same=\"+(result==receiver)+\"|receiver=\"+partialState(receiver)+\":unchanged=\"+before.equals(partialState(receiver));\n            if (caseName.equals(\"arrays_leap_iso\")) {\n                Array.set(inputTypes,0,hour); inputValues[0]=1900;\n                boolean inputCopy = out.equals(partialState(result));\n                Object getterTypes = call(result,\"getFieldTypes\",new Class<?>[]{});\n                int[] getterValues = (int[])call(result,\"getValues\",new Class<?>[]{});\n                Array.set(getterTypes,0,hour); getterValues[0]=1900;\n                out += \":input-copy=\"+inputCopy+\":output-copy=\"+out.equals(partialState(result));\n            }\n            return out;\n        } catch (ReflectiveOperationException | RuntimeException failure) {\n            throw new FixtureFailure(\"SQA_HARNESS Chronology setup/projection failed\",failure);\n        } finally { chronologyActiveCase = \"\"; }\n    }\n\n    public static boolean targetInvoked() { return Boolean.TRUE.equals(INVOKED.get()); }\n\n    private static String descriptor(String className, String ctor, String method, String params, int count) {\n        return \"{\\\"class\\\":\" + quote(className) + \",\\\"constructor_types\\\":\" + quote(ctor)\n            + \",\\\"method\\\":\" + quote(method) + \",\\\"parameter_types\\\":\" + quote(params)\n            + \",\\\"dimensions\\\":\" + Math.max(3, count * 3) + \"}\";\n    }\n\n    private static void discover(String[] classes, List<String> fixtureClasses) {\n        List<String> targets = new ArrayList<String>();\n        List<String> errors = new ArrayList<String>();\n        for (String className : classes) {\n            try {\n                Class<?> target = Class.forName(className, false, SqaProbe.class.getClassLoader());\n                Class<?> receiverType = target;\n                if (Modifier.isAbstract(target.getModifiers())) {\n                    for (String name : fixtureClasses) {\n                        try {\n                            Class<?> candidate = Class.forName(name, false, SqaProbe.class.getClassLoader());\n                            if (!Modifier.isAbstract(candidate.getModifiers()) && target.isAssignableFrom(candidate)\n                                    && candidate.getDeclaredConstructors().length > 0) {\n                                receiverType = candidate;\n                                break;\n                            }\n                        } catch (ClassNotFoundException ignored) { } catch (LinkageError ignored) { }\n                    }\n                }\n                List<Constructor<?>> constructors = new ArrayList<Constructor<?>>();\n                if (!Modifier.isAbstract(receiverType.getModifiers()) && !receiverType.isEnum()) {\n                    Constructor<?>[] all = receiverType.getDeclaredConstructors();\n                    Arrays.sort(all, new Comparator<Constructor<?>>() {\n                        public int compare(Constructor<?> a, Constructor<?> b) { return a.toString().compareTo(b.toString()); }\n                    });\n                    for (Constructor<?> ctor : all) {\n                        if (supportedParameters(ctor.getParameterTypes())) constructors.add(ctor);\n                    }\n                    // Select a constructor before generating inputs; prefer the simplest fixture.\n                    java.util.Collections.sort(constructors, new Comparator<Constructor<?>>() {\n                        public int compare(Constructor<?> a, Constructor<?> b) { return a.getParameterCount() - b.getParameterCount(); }\n                    });\n                }\n                Method[] methods = target.getDeclaredMethods();\n                Arrays.sort(methods, new Comparator<Method>() {\n                    public int compare(Method a, Method b) { return a.toString().compareTo(b.toString()); }\n                });\n                for (Method method : methods) {\n                    if (method.isSynthetic() || method.getName().equals(\"main\")\n                        || method.isBridge() || !supportedParameters(method.getParameterTypes())\n                        ) continue;\n                    if (Modifier.isStatic(method.getModifiers())) {\n                        targets.add(descriptor(className, \"\", method.getName(),\n                            typeNames(method.getParameterTypes()), method.getParameterCount()));\n                    } else if (!constructors.isEmpty()) {\n                        Constructor<?> ctor = constructors.get(0);\n                        targets.add(descriptor(receiverType.getName(), typeNames(ctor.getParameterTypes()), method.getName(),\n                            typeNames(method.getParameterTypes()), ctor.getParameterCount() + method.getParameterCount()));\n                    }\n                }\n                for (Constructor<?> ctor : constructors) {\n                    if (ctor.getParameterCount() > 0)\n                        targets.add(descriptor(receiverType.getName(), typeNames(ctor.getParameterTypes()), \"<init>\", \"\", ctor.getParameterCount()));\n                }\n            } catch (Throwable error) {\n                if (error instanceof VirtualMachineError || error instanceof ThreadDeath) throw (Error)error;\n                errors.add(quote(className + \":\" + error.getClass().getName()));\n            }\n        }\n        System.out.println(\"{\\\"targets\\\":[\" + String.join(\",\", targets) + \"],\\\"errors\\\":[\" + String.join(\",\", errors) + \"]}\");\n    }\n\n    public static void main(String[] args) throws Exception {\n        if (args.length > 0 && args[0].equals(\"discover\")) {\n            int start = 1;\n            List<String> fixtures = new ArrayList<String>();\n            if (args.length > 2 && args[1].equals(\"--fixtures\")) {\n                fixtures = Files.readAllLines(Paths.get(args[2]), StandardCharsets.UTF_8);\n                start = 3;\n            }\n            discover(Arrays.copyOfRange(args, start, args.length), fixtures);\n            return;\n        }\n        if ((args.length != 6 && args.length != 7) || !args[0].equals(\"observe\"))\n            throw new IllegalArgumentException(\"SQA_HARNESS expected discover classes or observe class ctor method types vector\");\n        String[] pieces = args[5].split(\",\");\n        double[] vector = new double[pieces.length];\n        for (int i = 0; i < pieces.length; i++) {\n            vector[i] = Double.parseDouble(pieces[i]);\n            if (!Double.isFinite(vector[i]))\n                throw new IllegalArgumentException(\"SQA_HARNESS nonfinite vector\");\n        }\n        String outcome;\n        try {\n            outcome = args.length == 7 ? observeWithPolicy(args[1], args[2], args[3], args[4], vector, args[6])\n                : observe(args[1], args[2], args[3], args[4], vector);\n        } catch (FixtureFailure failure) {\n            System.out.println(\"SQA_FIXTURE_FAILURE:\" + Base64.getEncoder().encodeToString(failure.getMessage().getBytes(StandardCharsets.UTF_8)));\n            return;\n        }\n        System.out.println(\"SQA_TRACE:{\\\"target_invoked\\\":\" + Boolean.TRUE.equals(INVOKED.get()) + \"}\");\n        System.out.println(\"SQA_RESULT:\" + Base64.getEncoder().encodeToString(outcome.getBytes(StandardCharsets.UTF_8)));\n    }\n    private static boolean graphicsIdentity(String cls, String ctor, String method, String params) {\n        if (!cls.equals(\"org.jfree.chart.renderer.category.AreaRenderer\") || !ctor.isEmpty()) return false;\n        if (method.equals(\"drawAnnotations\")) return params.equals(\"java.awt.Graphics2D,java.awt.geom.Rectangle2D,org.jfree.chart.axis.CategoryAxis,org.jfree.chart.axis.ValueAxis,org.jfree.chart.util.Layer,org.jfree.chart.plot.PlotRenderingInfo\");\n        if (method.equals(\"drawBackground\")) return params.equals(\"java.awt.Graphics2D,org.jfree.chart.plot.CategoryPlot,java.awt.geom.Rectangle2D\");\n        if (method.equals(\"drawDomainLine\")) return params.equals(\"java.awt.Graphics2D,org.jfree.chart.plot.CategoryPlot,java.awt.geom.Rectangle2D,double,java.awt.Paint,java.awt.Stroke\");\n        if (method.equals(\"drawDomainMarker\")) return params.equals(\"java.awt.Graphics2D,org.jfree.chart.plot.CategoryPlot,org.jfree.chart.axis.CategoryAxis,org.jfree.chart.plot.CategoryMarker,java.awt.geom.Rectangle2D\");\n        if (method.equals(\"drawOutline\")) return params.equals(\"java.awt.Graphics2D,org.jfree.chart.plot.CategoryPlot,java.awt.geom.Rectangle2D\");\n        if (method.equals(\"drawRangeMarker\")) return params.equals(\"java.awt.Graphics2D,org.jfree.chart.plot.CategoryPlot,org.jfree.chart.axis.ValueAxis,org.jfree.chart.plot.Marker,java.awt.geom.Rectangle2D\");\n        if (method.equals(\"initialise\")) return params.equals(\"java.awt.Graphics2D,java.awt.geom.Rectangle2D,org.jfree.chart.plot.CategoryPlot,org.jfree.data.category.CategoryDataset,org.jfree.chart.plot.PlotRenderingInfo\");\n        return false;\n    }\n    private static synchronized String observeGraphics(String method, double[] vector) {\n        INVOKED.set(Boolean.FALSE); GraphicsRecipe.lastEvidence = null;\n        try {\n            if (vector.length == 0 || !Double.isFinite(vector[0])) throw new IllegalArgumentException(\"Finite fixture selector required\");\n            java.util.List<String> cases = new ArrayList<String>();\n            for (String name : GraphicsRecipe.CASES) {\n                String group = name.startsWith(\"annotations_\") ? \"drawAnnotations\" : name.startsWith(\"background_\") ? \"drawBackground\" :\n                    name.startsWith(\"domain_line_\") ? \"drawDomainLine\" : name.startsWith(\"domain_marker_\") ? \"drawDomainMarker\" :\n                    name.startsWith(\"outline_\") ? \"drawOutline\" : name.startsWith(\"range_\") ? \"drawRangeMarker\" : \"initialise\";\n                if (group.equals(method)) cases.add(name);\n            }\n            String name = cases.get(bucket(vector[0], cases.size()));\n            return GraphicsRecipe.json(GraphicsRecipe.run(name, null).get(\"observation\"));\n        } catch (Throwable failure) {\n            // Target exceptions are already observations; every escaping error belongs to setup/projection.\n            throw new FixtureFailure(\"SQA_HARNESS Graphics setup/projection failed\", failure);\n        } finally { GraphicsRecipe.activeCase = \"\"; }\n    }\npublic static final class GraphicsRecipe {\n    public static String activeCase = \"\";\n    public static Map<String,Object> lastEvidence = null;\n    private static final String OWNER = \"org.jfree.chart.renderer.category.AbstractCategoryItemRenderer\";\n    private static final String[] CASES = {\n        \"annotations_fg_v\", \"annotations_fg_h\", \"annotations_bg_v\", \"annotations_bg_h\",\n        \"background_v\", \"background_h\", \"domain_line_v\", \"domain_line_h\",\n        \"domain_line_null_paint\", \"domain_line_null_stroke\",\n        \"domain_marker_line_v\", \"domain_marker_line_h\", \"domain_marker_band_v\", \"domain_marker_band_h\",\n        \"domain_marker_missing\", \"outline_enabled\", \"outline_disabled\",\n        \"range_value_v\", \"range_value_h\", \"range_interval_v\", \"range_interval_h\", \"range_outside\",\n        \"initialise_dataset\", \"initialise_null_dataset\"\n    };\n    private static String quote(String text) {\n        return \"\\\"\" + text.replace(\"\\\\\", \"\\\\\\\\\").replace(\"\\\"\", \"\\\\\\\"\").replace(\"\\n\", \"\\\\n\") + \"\\\"\";\n    }\n    public static String json(Object value) {\n        if (value == null) return \"null\";\n        if (value instanceof String) return quote((String) value);\n        if (value instanceof Map) {\n            StringBuilder out = new StringBuilder(\"{\");\n            for (Object object : ((Map) value).entrySet()) {\n                Map.Entry item = (Map.Entry) object;\n                if (out.length() > 1) out.append(',');\n                out.append(quote((String) item.getKey())).append(':').append(json(item.getValue()));\n            }\n            return out.append('}').toString();\n        }\n        if (value instanceof java.util.List) {\n            StringBuilder out = new StringBuilder(\"[\");\n            for (Object item : (java.util.List) value) {\n                if (out.length() > 1) out.append(',');\n                out.append(json(item));\n            }\n            return out.append(']').toString();\n        }\n        return String.valueOf(value);\n    }\n    private static Map<String,Object> map(Object... pairs) {\n        Map<String,Object> result = new LinkedHashMap<String,Object>();\n        for (int i = 0; i < pairs.length; i += 2) result.put((String) pairs[i], pairs[i+1]);\n        return result;\n    }\n    private static void check(boolean state, String reason) {\n        if (!state) throw new AssertionError(reason);\n    }\n    private static BufferedImage canvas() {\n        BufferedImage image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);\n        Graphics2D g = image.createGraphics();\n        try { g.setColor(Color.WHITE); g.fillRect(0, 0, 64, 64); }\n        finally { g.dispose(); }\n        return image;\n    }\n    private static Graphics2D graphics(BufferedImage image) {\n        Graphics2D g = image.createGraphics();\n        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);\n        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);\n        g.setPaint(Color.BLACK); g.setStroke(new BasicStroke(1.0f));\n        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));\n        return g;\n    }\n    private static Map<String,Object> state(Graphics2D g) {\n        return map(\"paint_rgb\", ((Color) g.getPaint()).getRGB(), \"stroke_width\", ((BasicStroke) g.getStroke()).getLineWidth(),\n                \"composite_rule\", ((AlphaComposite) g.getComposite()).getRule(),\n                \"composite_alpha\", ((AlphaComposite) g.getComposite()).getAlpha(),\n                \"identity_transform\", g.getTransform().isIdentity(), \"clip_null\", g.getClip() == null);\n    }\n    private static byte[] pixels(BufferedImage image) throws IOException {\n        ByteArrayOutputStream bytes = new ByteArrayOutputStream();\n        DataOutputStream out = new DataOutputStream(bytes);\n        for (int y = 0; y < 64; y++) for (int x = 0; x < 64; x++) out.writeInt(image.getRGB(x, y));\n        out.close(); return bytes.toByteArray();\n    }\n    private static String sha(byte[] raw) throws Exception {\n        StringBuilder text = new StringBuilder();\n        for (byte b : MessageDigest.getInstance(\"SHA-256\").digest(raw)) text.append(String.format(\"%02x\", b & 255));\n        return text.toString();\n    }\n    private static void save(File root, String name, byte[] bytes) throws IOException {\n        File file = new File(root, name);\n        if (!file.createNewFile()) throw new IOException(\"Refuse to overwrite pixel evidence\");\n        try (FileOutputStream out = new FileOutputStream(file)) { out.write(bytes); }\n    }\n    private static final class Fixture implements AutoCloseable {\n        final Object renderer = make(\"org.jfree.chart.renderer.category.AreaRenderer\", \"\");\n        final Object dataset = make(\"org.jfree.data.category.DefaultCategoryDataset\", \"\");\n        final Object domain = make(\"org.jfree.chart.axis.CategoryAxis\", \"java.lang.String\", \"Domain\");\n        final Object range = make(\"org.jfree.chart.axis.NumberAxis\", \"java.lang.String\", \"Range\");\n        final Rectangle2D area = new Rectangle2D.Double(10, 10, 40, 40);\n        final BufferedImage actual = canvas(), reference = canvas();\n        final Graphics2D g = graphics(actual), ref = graphics(reference);\n        final Object plot;\n        Fixture(boolean horizontal) throws Exception {\n            invoke(dataset, \"addValue\", \"double,java.lang.Comparable,java.lang.Comparable\", 2.0, \"r1\", \"A\"); invoke(dataset, \"addValue\", \"double,java.lang.Comparable,java.lang.Comparable\", 8.0, \"r1\", \"B\");\n            invoke(dataset, \"addValue\", \"double,java.lang.Comparable,java.lang.Comparable\", 4.0, \"r2\", \"A\"); invoke(dataset, \"addValue\", \"double,java.lang.Comparable,java.lang.Comparable\", 6.0, \"r2\", \"B\");\n            invoke(domain, \"setLowerMargin\", \"double\", 0); invoke(domain, \"setUpperMargin\", \"double\", 0); invoke(domain, \"setCategoryMargin\", \"double\", 0);\n            invoke(range, \"setAutoRange\", \"boolean\", false); invoke(range, \"setRange\", \"double,double\", 0, 10); invoke(range, \"setInverted\", \"boolean\", false);\n            plot = make(\"org.jfree.chart.plot.CategoryPlot\", \"org.jfree.data.category.CategoryDataset,org.jfree.chart.axis.CategoryAxis,org.jfree.chart.axis.ValueAxis,org.jfree.chart.renderer.category.CategoryItemRenderer\", dataset, domain, range, renderer);\n            invoke(plot, \"setOrientation\", \"org.jfree.chart.plot.PlotOrientation\", horizontal ? constant(\"org.jfree.chart.plot.PlotOrientation\", \"HORIZONTAL\") : constant(\"org.jfree.chart.plot.PlotOrientation\", \"VERTICAL\"));\n            invoke(plot, \"setBackgroundPaint\", \"java.awt.Paint\", Color.YELLOW); invoke(plot, \"setBackgroundAlpha\", \"float\", 1.0f); invoke(plot, \"setBackgroundImage\", \"java.awt.Image\", (Object)null);\n            invoke(plot, \"setOutlinePaint\", \"java.awt.Paint\", Color.BLUE); invoke(plot, \"setOutlineStroke\", \"java.awt.Stroke\", new BasicStroke(2.0f));\n            invoke(plot, \"setOutlineVisible\", \"boolean\", true);\n            check(invoke(plot, \"getRenderer\", \"\") == renderer && invoke(renderer, \"getPlot\", \"\") == plot, \"Production receiver binding\");\n        }\n        public void close() { g.dispose(); ref.dispose(); }\n    }\n    private static void line(Graphics2D g, boolean horizontal, double value, Color color, float width) {\n        g.setPaint(color); g.setStroke(new BasicStroke(width));\n        g.draw(horizontal ? new Line2D.Double(10, value, 50, value) : new Line2D.Double(value, 10, value, 50));\n    }\n    public static Map<String,Object> run(String name, File images) throws Exception {\n        boolean horizontal = name.endsWith(\"_h\");\n        try (Fixture f = new Fixture(horizontal)) {\n            String method; Class<?>[] types; Object[] args;\n            String expectedException = null, expectedMessage = null;\n            Object expectedReturn = null;\n            int rows = 0, columns = 0;\n            boolean plotBound = true;\n            if (name.startsWith(\"annotations_\")) {\n                method = \"drawAnnotations\";\n                types = new Class<?>[]{Graphics2D.class, Rectangle2D.class, Class.forName(\"org.jfree.chart.axis.CategoryAxis\"), Class.forName(\"org.jfree.chart.axis.ValueAxis\"), Class.forName(\"org.jfree.chart.util.Layer\"), Class.forName(\"org.jfree.chart.plot.PlotRenderingInfo\")};\n                boolean foreground = name.contains(\"_fg_\");\n                invoke(f.renderer, \"addAnnotation\", \"org.jfree.chart.annotations.CategoryAnnotation,org.jfree.chart.util.Layer\", make(\"org.jfree.chart.annotations.CategoryLineAnnotation\", \"java.lang.Comparable,double,java.lang.Comparable,double,java.awt.Paint,java.awt.Stroke\", \"A\", 2, \"B\", 8, Color.RED, new BasicStroke(1.0f)), constant(\"org.jfree.chart.util.Layer\", \"FOREGROUND\"));\n                invoke(f.renderer, \"addAnnotation\", \"org.jfree.chart.annotations.CategoryAnnotation,org.jfree.chart.util.Layer\", make(\"org.jfree.chart.annotations.CategoryLineAnnotation\", \"java.lang.Comparable,double,java.lang.Comparable,double,java.awt.Paint,java.awt.Stroke\", \"A\", 8, \"B\", 2, Color.GREEN, new BasicStroke(1.0f)), constant(\"org.jfree.chart.util.Layer\", \"BACKGROUND\"));\n                args = new Object[]{f.g, f.area, f.domain, f.range, foreground ? constant(\"org.jfree.chart.util.Layer\", \"FOREGROUND\") : constant(\"org.jfree.chart.util.Layer\", \"BACKGROUND\"), null};\n                f.ref.setPaint(foreground ? Color.RED : Color.GREEN); f.ref.setStroke(new BasicStroke(1.0f));\n                // Two categories with zero margins have middle coordinates 20 and 40.\n                // Range [0,10] maps value v to 50-4v vertically, or 10+4v horizontally.\n                int a = foreground ? 2 : 8, b = foreground ? 8 : 2;\n                if (horizontal) f.ref.drawLine(10+4*a, 20, 10+4*b, 40);\n                else f.ref.drawLine(20, 50-4*a, 40, 50-4*b);\n            } else if (name.startsWith(\"background_\")) {\n                method = \"drawBackground\"; types = new Class<?>[]{Graphics2D.class, Class.forName(\"org.jfree.chart.plot.CategoryPlot\"), Rectangle2D.class};\n                args = new Object[]{f.g, f.plot, f.area};\n                f.ref.setComposite(AlphaComposite.SrcOver); f.ref.setPaint(Color.YELLOW); f.ref.fillRect(10, 10, 40, 40);\n                f.ref.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));\n            } else if (name.startsWith(\"domain_line_\")) {\n                method = \"drawDomainLine\";\n                types = new Class<?>[]{Graphics2D.class, Class.forName(\"org.jfree.chart.plot.CategoryPlot\"), Rectangle2D.class, double.class, Paint.class, Stroke.class};\n                Paint paint = name.endsWith(\"null_paint\") ? null : Color.RED;\n                Stroke stroke = name.endsWith(\"null_stroke\") ? null : new BasicStroke(2.0f);\n                args = new Object[]{f.g, f.plot, f.area, 24.0, paint, stroke};\n                if (paint == null || stroke == null) {\n                    expectedException = \"java.lang.IllegalArgumentException\";\n                    expectedMessage = paint == null ? \"Null 'paint' argument.\" : \"Null 'stroke' argument.\";\n                } else line(f.ref, horizontal, 24, Color.RED, 2);\n            } else if (name.startsWith(\"domain_marker_\")) {\n                method = \"drawDomainMarker\";\n                types = new Class<?>[]{Graphics2D.class, Class.forName(\"org.jfree.chart.plot.CategoryPlot\"), Class.forName(\"org.jfree.chart.axis.CategoryAxis\"), Class.forName(\"org.jfree.chart.plot.CategoryMarker\"), Rectangle2D.class};\n                Object marker = make(\"org.jfree.chart.plot.CategoryMarker\", \"java.lang.Comparable,java.awt.Paint,java.awt.Stroke\", name.endsWith(\"missing\") ? \"missing\" : \"A\", Color.RED, new BasicStroke(2.0f));\n                invoke(marker, \"setAlpha\", \"float\", 1.0f); invoke(marker, \"setLabel\", \"java.lang.String\", (Object)null); invoke(marker, \"setDrawAsLine\", \"boolean\", name.contains(\"_line_\"));\n                args = new Object[]{f.g, f.plot, f.domain, marker, f.area};\n                if (!name.endsWith(\"missing\")) {\n                    f.ref.setComposite(AlphaComposite.SrcOver);\n                    if (name.contains(\"_line_\")) line(f.ref, horizontal, 20, Color.RED, 2);\n                    else { f.ref.setPaint(Color.RED); f.ref.fillRect(10, 10, horizontal ? 40 : 20, horizontal ? 20 : 40); }\n                    f.ref.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));\n                }\n            } else if (name.startsWith(\"outline_\")) {\n                method = \"drawOutline\"; types = new Class<?>[]{Graphics2D.class, Class.forName(\"org.jfree.chart.plot.CategoryPlot\"), Rectangle2D.class};\n                boolean visible = name.endsWith(\"enabled\"); invoke(f.plot, \"setOutlineVisible\", \"boolean\", visible);\n                args = new Object[]{f.g, f.plot, f.area};\n                if (visible) { f.ref.setPaint(Color.BLUE); f.ref.setStroke(new BasicStroke(2.0f)); f.ref.drawRect(10, 10, 40, 40); }\n            } else if (name.startsWith(\"range_\")) {\n                method = \"drawRangeMarker\";\n                types = new Class<?>[]{Graphics2D.class, Class.forName(\"org.jfree.chart.plot.CategoryPlot\"), Class.forName(\"org.jfree.chart.axis.ValueAxis\"), Class.forName(\"org.jfree.chart.plot.Marker\"), Rectangle2D.class};\n                Object marker;\n                if (name.contains(\"_interval_\")) {\n                    Object interval = make(\"org.jfree.chart.plot.IntervalMarker\", \"double,double,java.awt.Paint\", 2, 8, Color.RED);\n                    invoke(interval, \"setOutlinePaint\", \"java.awt.Paint\", (Object)null); invoke(interval, \"setOutlineStroke\", \"java.awt.Stroke\", (Object)null); marker = interval;\n                } else marker = make(\"org.jfree.chart.plot.ValueMarker\", \"double,java.awt.Paint,java.awt.Stroke\", name.endsWith(\"outside\") ? 20 : 2, Color.RED, new BasicStroke(2.0f));\n                invoke(marker, \"setAlpha\", \"float\", 1.0f); invoke(marker, \"setLabel\", \"java.lang.String\", (Object)null);\n                args = new Object[]{f.g, f.plot, f.range, marker, f.area};\n                if (!name.endsWith(\"outside\")) {\n                    f.ref.setComposite(AlphaComposite.SrcOver);\n                    if (name.contains(\"_interval_\")) {\n                        f.ref.setPaint(Color.RED);\n                        f.ref.fillRect(horizontal ? 18 : 10, horizontal ? 10 : 18, horizontal ? 24 : 40, horizontal ? 40 : 24);\n                    } else line(f.ref, !horizontal, horizontal ? 18 : 42, Color.RED, 2);\n                    f.ref.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));\n                }\n            } else {\n                method = \"initialise\";\n                types = new Class<?>[]{Graphics2D.class, Rectangle2D.class, Class.forName(\"org.jfree.chart.plot.CategoryPlot\"), Class.forName(\"org.jfree.data.category.CategoryDataset\"), Class.forName(\"org.jfree.chart.plot.PlotRenderingInfo\")};\n                boolean withDataset = name.equals(\"initialise_dataset\");\n                invoke(f.renderer, \"setPlot\", \"org.jfree.chart.plot.CategoryPlot\", make(\"org.jfree.chart.plot.CategoryPlot\", \"\"));\n                check(invoke(f.renderer, \"getPlot\", \"\") != f.plot, \"Distinct legal production plot before initialise\");\n                args = new Object[]{f.g, f.area, f.plot, withDataset ? f.dataset : null, null};\n                rows = columns = withDataset ? 2 : 0;\n                expectedReturn = map(\"class\", \"org.jfree.chart.renderer.category.CategoryItemRendererState\", \"info_null\", true,\n                        \"bar_width\", 0.0, \"selection_matches_dataset\", withDataset, \"selection_null\", !withDataset);\n            }\n            Method target = f.renderer.getClass().getMethod(method, types);\n            check(target.getDeclaringClass().getName().equals(OWNER), \"Exact inherited declaration binding\");\n            check(f.renderer.getClass() == Class.forName(\"org.jfree.chart.renderer.category.AreaRenderer\"), \"Default AreaRenderer receiver identity\");\n            byte[] expectedPixels = pixels(f.reference);\n            Map<String,Object> expected = map(\"pixel_sha256\", sha(expectedPixels), \"graphics\", state(f.ref),\n                    \"rows\", rows, \"columns\", columns, \"plot_bound\", plotBound,\n                    \"dataset\", Arrays.asList(2.0, 8.0, 4.0, 6.0), \"return_state\", expectedReturn,\n                    \"exception\", expectedException, \"message\", expectedMessage);\n            Throwable thrown = null; Object result = null;\n            activeCase = name; INVOKED.set(Boolean.TRUE);\n            try { result = target.invoke(f.renderer, args); }\n            catch (InvocationTargetException failure) { thrown = failure.getCause(); }\n            finally { activeCase = \"\"; }\n            if (thrown instanceof VirtualMachineError || thrown instanceof LinkageError || thrown instanceof ThreadDeath)\n                throw new FixtureFailure(\"SQA_HARNESS Graphics target environment failure\", thrown);\n            Object returned = null;\n            if (result != null && result.getClass().getName().equals(\"org.jfree.chart.renderer.category.CategoryItemRendererState\")) {\n                Object r = result;\n                returned = map(\"class\", r.getClass().getName(), \"info_null\", invoke(r, \"getInfo\", \"\") == null,\n                        \"bar_width\", invoke(r, \"getBarWidth\", \"\"), \"selection_matches_dataset\", invoke(r, \"getSelectionState\", \"\") == f.dataset,\n                        \"selection_null\", invoke(r, \"getSelectionState\", \"\") == null);\n            }\n            byte[] actualPixels = pixels(f.actual);\n            Map<String,Object> actual = map(\"pixel_sha256\", sha(actualPixels), \"graphics\", state(f.g),\n                    \"rows\", invoke(f.renderer, \"getRowCount\", \"\"), \"columns\", invoke(f.renderer, \"getColumnCount\", \"\"), \"plot_bound\", invoke(f.renderer, \"getPlot\", \"\") == f.plot,\n                    \"dataset\", Arrays.asList(invoke(f.dataset, \"getValue\", \"int,int\", 0,0), invoke(f.dataset, \"getValue\", \"int,int\", 0,1), invoke(f.dataset, \"getValue\", \"int,int\", 1,0), invoke(f.dataset, \"getValue\", \"int,int\", 1,1)),\n                    \"return_state\", returned, \"exception\", thrown == null ? null : thrown.getClass().getName(),\n                    \"message\", thrown == null ? null : thrown.getMessage());\n            if (images != null) { save(images, name + \".actual.argb\", actualPixels); save(images, name + \".reference.argb\", expectedPixels); }\n            AssertionError assertion = null;\n            try { check(json(actual).equals(json(expected)) && Arrays.equals(actualPixels, expectedPixels), \"Declared image/value/state oracle differs\"); }\n            catch (AssertionError failure) { assertion = failure; }\n            boolean passed = assertion == null;\n            lastEvidence = map(\"actual_argb_b64\", Base64.getEncoder().encodeToString(actualPixels), \"reference_argb_b64\", Base64.getEncoder().encodeToString(expectedPixels), \"case\", name, \"method\", method, \"receiver_class\", f.renderer.getClass().getName(),\n                    \"declaring_class\", target.getDeclaringClass().getName(), \"setup_succeeded\", true,\n                    \"target_invoked\", true, \"target_check_passed\", passed,\n                    \"failure_class\", passed ? null : assertion.getClass().getName(), \"failure_reason\", passed ? null : assertion.getMessage(),\n                    \"observation\", actual, \"expected_observation\", expected);\n            return lastEvidence;\n        }\n    }\n    private static Object make(String name, String params, Object... args) throws Exception {\n        return Class.forName(name).getConstructor(types(params)).newInstance(args);\n    }\n    private static Object invoke(Object receiver, String name, String params, Object... args) throws Exception {\n        return call(receiver, name, types(params), args);\n    }\n    private static Object constant(String name, String field) throws Exception {\n        return Class.forName(name).getField(field).get(null);\n    }\n    }\n}\n",
    "scripts/study/api854/fixture_policy.py": "\"\"\"Predeclared explicit fixture capability filter, never selected by buggy outcomes.\"\"\"\nPOLICY = 'beam-explicit-fixtures-v3-proposal'\nRECIPE_SOURCES = ('algorithms/java/SqaProbe.java', 'scripts/study/api854/fixture_policy.py')\n\n\ndef recipe_document(source_hashes, policy=POLICY):\n    from .common import ROOT, sha256\n    expected = {name: source_hashes[name] for name in RECIPE_SOURCES}\n    if any(sha256(ROOT / name) != value for name, value in expected.items()):\n        raise ValueError('Explicit recipe source differs from protocol')\n    if policy not in {POLICY, POLICY_V4, POLICY_V5, POLICY_V6, POLICY_V10, POLICY_V11, POLICY_V12, POLICY_V13}:\n        raise ValueError('Unknown explicit fixture policy')\n    return {'schema_version': 1, 'fixture_policy_id': policy, 'source_sha256': expected,\n        'sources': {name: (ROOT / name).read_bytes().decode('utf-8') for name in RECIPE_SOURCES},\n        'scope': 'Same fixture construction/projection knowledge for all four approaches; no execution feedback'}\n\n\ndef validate_recipe(recipe, source_hashes=None, policy=POLICY):\n    from .preparation import digest\n    if (policy not in {POLICY, POLICY_V4, POLICY_V5, POLICY_V6, POLICY_V10, POLICY_V11, POLICY_V12, POLICY_V13} or not isinstance(recipe, dict) or recipe.get('fixture_policy_id') != policy\n            or not isinstance(recipe.get('sources'), dict) or set(recipe['sources']) != set(RECIPE_SOURCES)\n            or not isinstance(recipe.get('source_sha256'), dict) or set(recipe['source_sha256']) != set(RECIPE_SOURCES)\n            or any(not isinstance(recipe['sources'][name], str)\n                   or digest(recipe['sources'][name].encode('utf-8')) != recipe['source_sha256'][name] for name in RECIPE_SOURCES)):\n        raise ValueError('Explicit recipe source bytes/hash differ')\n    if source_hashes is not None and any(source_hashes.get(name) != recipe['source_sha256'][name] for name in RECIPE_SOURCES):\n        raise ValueError('Explicit recipe source differs from frozen protocol')\n    return True\nPOLICY_V4 = 'beam-explicit-fixtures-v4-proposal'\nPOLICY_V5 = 'beam-explicit-fixtures-v5-proposal'\nPOLICY_V6 = 'aom-beam-fraction-field-v6-development'\nPOLICY_V10 = 'aom-beam-champ-joint-fixtures-v10-development'\nPOLICY_V11 = 'aom-beam-champ-chronology-fixtures-v11-development'\nPOLICY_V12 = 'aom-beam-champ-graphics-fixtures-v12-development'\nPOLICY_V13 = 'aom-beam-champ-codec-fixtures-v13-development'\nCODEC_SIGNATURES = {('org.apache.commons.codec.language.Metaphone', '', 'isVowel', 'java.lang.StringBuffer,int'), ('org.apache.commons.codec.language.Metaphone', '', 'regionMatch', 'java.lang.StringBuffer,int,java.lang.String'), ('org.apache.commons.codec.language.SoundexUtils', '', 'difference', 'org.apache.commons.codec.StringEncoder,java.lang.String,java.lang.String'), ('org.apache.commons.codec.language.Metaphone', '', 'isNextChar', 'java.lang.StringBuffer,int,char'), ('org.apache.commons.codec.language.Metaphone', '', 'isPreviousChar', 'java.lang.StringBuffer,int,char')}\nGRAPHICS_SIGNATURES = {\n    ('org.jfree.chart.renderer.category.AreaRenderer', '', 'drawAnnotations', 'java.awt.Graphics2D,java.awt.geom.Rectangle2D,org.jfree.chart.axis.CategoryAxis,org.jfree.chart.axis.ValueAxis,org.jfree.chart.util.Layer,org.jfree.chart.plot.PlotRenderingInfo'),\n    ('org.jfree.chart.renderer.category.AreaRenderer', '', 'drawBackground', 'java.awt.Graphics2D,org.jfree.chart.plot.CategoryPlot,java.awt.geom.Rectangle2D'),\n    ('org.jfree.chart.renderer.category.AreaRenderer', '', 'drawDomainLine', 'java.awt.Graphics2D,org.jfree.chart.plot.CategoryPlot,java.awt.geom.Rectangle2D,double,java.awt.Paint,java.awt.Stroke'),\n    ('org.jfree.chart.renderer.category.AreaRenderer', '', 'drawDomainMarker', 'java.awt.Graphics2D,org.jfree.chart.plot.CategoryPlot,org.jfree.chart.axis.CategoryAxis,org.jfree.chart.plot.CategoryMarker,java.awt.geom.Rectangle2D'),\n    ('org.jfree.chart.renderer.category.AreaRenderer', '', 'drawOutline', 'java.awt.Graphics2D,org.jfree.chart.plot.CategoryPlot,java.awt.geom.Rectangle2D'),\n    ('org.jfree.chart.renderer.category.AreaRenderer', '', 'drawRangeMarker', 'java.awt.Graphics2D,org.jfree.chart.plot.CategoryPlot,org.jfree.chart.axis.ValueAxis,org.jfree.chart.plot.Marker,java.awt.geom.Rectangle2D'),\n    ('org.jfree.chart.renderer.category.AreaRenderer', '', 'initialise', 'java.awt.Graphics2D,java.awt.geom.Rectangle2D,org.jfree.chart.plot.CategoryPlot,org.jfree.data.category.CategoryDataset,org.jfree.chart.plot.PlotRenderingInfo'),\n}\nCHRONOLOGY_SIGNATURES = {\n    ('org.joda.time.Partial', 'org.joda.time.Chronology', '<init>', ''),\n    ('org.joda.time.Partial', 'org.joda.time.DateTimeFieldType,int,org.joda.time.Chronology', '<init>', ''),\n    ('org.joda.time.Partial', '[Lorg.joda.time.DateTimeFieldType;,[I,org.joda.time.Chronology', '<init>', ''),\n    ('org.joda.time.Partial', 'org.joda.time.Chronology,[Lorg.joda.time.DateTimeFieldType;,[I', '<init>', ''),\n    ('org.joda.time.Partial', '', 'getField', 'int,org.joda.time.Chronology'),\n    ('org.joda.time.Partial', '', 'withChronologyRetainFields', 'org.joda.time.Chronology'),\n}\nJOINT_SIGNATURES = {\n    ('org.apache.commons.codec.language.Metaphone', '', 'setMaxCodeLen', 'int'),\n    ('com.fasterxml.jackson.core.io.NumberInput', '', 'inLongRange', '[C,int,int,boolean'),\n    ('com.fasterxml.jackson.core.io.NumberInput', '', 'parseBigDecimal', '[C'),\n    ('com.fasterxml.jackson.core.io.NumberInput', '', 'parseBigDecimal', '[C,int,int'),\n    ('com.fasterxml.jackson.core.io.NumberInput', '', 'parseInt', '[C,int,int'),\n    ('com.fasterxml.jackson.core.io.NumberInput', '', 'parseLong', '[C,int,int'),\n    ('com.fasterxml.jackson.core.util.TextBuffer', 'com.fasterxml.jackson.core.util.BufferRecycler', 'append', '[C,int,int'),\n    ('com.fasterxml.jackson.core.util.TextBuffer', 'com.fasterxml.jackson.core.util.BufferRecycler', 'append', 'java.lang.String,int,int'),\n    ('org.apache.commons.csv.ExtendedBufferedReader', 'java.io.Reader', 'read', '[C,int,int'),\n    ('org.apache.commons.lang3.math.NumberUtils', '', 'isAllZeros', 'java.lang.String'),\n    ('org.apache.commons.lang3.math.NumberUtils', '', 'validateArray', 'java.lang.Object'),\n}\n\n# Fixed-source recipes, declared before generation/evaluation. This development\n# version deliberately preserves unsupported declarations as explicit exclusions.\nPILOT_METHODS = {\n    'org.apache.commons.lang3.math.NumberUtils': {'isDigits', 'isNumber', 'max', 'min', 'toByte', 'toDouble',\n        'toFloat', 'toInt', 'toLong', 'toShort', 'createDouble', 'createFloat', 'createInteger', 'createLong',\n        'createNumber', 'createBigDecimal', 'createBigInteger'},\n    'com.fasterxml.jackson.core.io.NumberInput': {'parseAsDouble', 'parseDouble', 'parseAsInt', 'parseInt',\n        'parseBigDecimal', 'parseAsLong', 'parseLong', 'inLongRange'},\n    'com.fasterxml.jackson.core.util.TextBuffer': {'hasTextAsCharacters', 'contentsAsArray', 'contentsAsString',\n        'getCurrentSegmentSize', 'getTextOffset', 'size', 'toString', 'append', 'resetWithEmpty', 'resetWithString'},\n    'org.apache.commons.math3.fraction.BigFraction': {'equals', 'doubleValue', 'percentageValue', 'floatValue',\n        'getDenominator', 'getNumerator', 'intValue', 'longValue', 'toString', 'abs', 'add', 'subtract',\n        'multiply', 'divide', 'negate', 'reciprocal', 'reduce', 'compareTo'},\n    'org.apache.commons.math3.fraction.Fraction': {'equals', 'doubleValue', 'percentageValue', 'floatValue',\n        'getDenominator', 'getNumerator', 'intValue', 'longValue', 'toString', 'abs', 'add', 'subtract',\n        'multiply', 'divide', 'negate', 'reciprocal', 'compareTo'},\n    'org.jsoup.nodes.Document': {'nodeName', 'outerHtml', 'title', 'normalise', 'body', 'head', 'text', 'createElement', 'createShell'},\n    'org.apache.commons.cli.CommandLine': {'hasOption', 'getOptionObject', 'getOptionValue', 'getArgs',\n        'getOptionValues', 'iterator', 'getArgList', 'getOptions', 'addArg', 'addOption'},\n    'org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream': {'ensureOpen', 'writeCString',\n        'close', 'closeArchiveEntry', 'finish', 'putArchiveEntry', 'putNextEntry', 'write'},\n    'org.joda.time.field.UnsupportedDurationField': {'equals', 'isPrecise', 'isSupported', 'getType',\n        'getName', 'toString', 'getUnitMillis', 'compareTo', 'getInstance'},\n    'org.joda.time.Partial': {'size', 'getValues', 'toStringList', 'with', 'withField', 'without'},\n    'com.google.gson.TypeInfoFactory': {'getIndex', 'getActualType', 'extractRealTypes', 'getTypeInfoForField', 'getTypeInfoForArray'},\n    'com.google.javascript.jscomp.RemoveUnusedVars': {'process', 'traverseAndRemoveUnusedReferences', 'getFunctionArgList'},\n    'org.jfree.chart.renderer.category.AreaRenderer': {'findRangeBounds', 'getRowCount', 'getColumnCount',\n        'getPassCount', 'getLegendItems', 'getLegendItem', 'getItemMiddle'},\n    'com.fasterxml.jackson.databind.ser.BeanPropertyWriter': {'getName', 'getSerializedName', 'getType',\n        'getPropertyType', 'getGenericPropertyType', 'isRequired', 'willSuppressNulls', 'hasSerializer',\n        'hasNullSerializer', 'rename', 'get', 'getInternalSetting', 'setInternalSetting', 'removeInternalSetting'},\n    'com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer': {'deserialize', 'deserializeUsingCustom', 'handleNonArray'},\n    'com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser': {'hasTextCharacters', 'isClosed', 'isExpectedStartArrayToken',\n        'requiresCustomCodec', 'getTextCharacters', 'nextToken', 'getText', 'getCurrentName', 'getValueAsString',\n        'nextTextValue', 'close', 'overrideCurrentName', 'setXMLTextElementName'},\n    'org.mockito.internal.invocation.InvocationMatcher': {'matches', 'hasSameMethod', 'hasSimilarMethod',\n        'getMethod', 'getInvocation', 'toString'},\n}\nPILOT_TYPES = {'com.fasterxml.jackson.core.util.BufferRecycler', 'org.apache.commons.math3.fraction.BigFraction',\n    'org.apache.commons.math3.fraction.Fraction', 'java.math.BigInteger', 'org.apache.commons.cli.Option',\n    'java.io.OutputStream', 'org.apache.commons.compress.archivers.ArchiveEntry',\n    'org.apache.commons.compress.archivers.cpio.CpioArchiveEntry', 'org.joda.time.DurationFieldType',\n    'org.joda.time.DurationField', 'org.joda.time.DateTimeFieldType', 'java.lang.reflect.Type',\n    'java.lang.reflect.TypeVariable', '[Ljava.lang.reflect.Type;', '[Ljava.lang.reflect.TypeVariable;',\n    'java.lang.reflect.Field', 'java.lang.Class', 'com.google.javascript.jscomp.AbstractCompiler',\n    'com.google.javascript.rhino.Node', 'org.jfree.data.category.CategoryDataset', 'org.jfree.chart.axis.CategoryAxis',\n    'java.lang.Comparable', 'java.awt.geom.Rectangle2D', 'org.jfree.chart.util.RectangleEdge',\n    'com.fasterxml.jackson.databind.ser.BeanPropertyWriter', 'com.fasterxml.jackson.databind.util.NameTransformer',\n    'com.fasterxml.jackson.databind.JavaType', 'com.fasterxml.jackson.databind.JsonDeserializer',\n    'com.fasterxml.jackson.databind.deser.ValueInstantiator', 'com.fasterxml.jackson.core.JsonParser',\n    'com.fasterxml.jackson.databind.DeserializationContext', 'com.fasterxml.jackson.core.io.IOContext',\n    'com.fasterxml.jackson.core.ObjectCodec', 'javax.xml.stream.XMLStreamReader', 'org.mockito.invocation.Invocation'}\n\n# Added capability recipes are fixed before any buggy evaluation. Mutators need\n# structural post-state; unsupported helpers/serialization hooks stay excluded.\nADDITIONAL_METHODS = {\n    'org.apache.commons.codec.language.Caverphone': {'isCaverphoneEqual', 'encode', 'caverphone'},\n    'org.apache.commons.codec.language.Metaphone': {'isLastChar', 'isMetaphoneEqual', 'getMaxCodeLen', 'encode', 'metaphone'},\n    'org.apache.commons.codec.language.SoundexUtils': {'differenceEncoded', 'clean'},\n    'org.apache.commons.collections.map.Flat3Map': {'containsKey', 'containsValue', 'equals', 'isEmpty', 'size',\n        'clone', 'get', 'put', 'remove', 'toString', 'clear', 'putAll'},\n    'org.apache.commons.csv.ExtendedBufferedReader': {'getLineNumber', 'lookAhead', 'readAgain', 'read', 'readLine'},\n}\n\nSCALARS = {'boolean', 'byte', 'short', 'int', 'long', 'float', 'double', 'char',\n           'java.lang.String', 'java.lang.Boolean', 'java.lang.Byte', 'java.lang.Short',\n           'java.lang.Integer', 'java.lang.Long', 'java.lang.Float', 'java.lang.Double',\n           'java.lang.Character', 'java.lang.Object', 'java.lang.Number', 'java.util.Date',\n           'java.util.Locale', 'java.util.List', 'java.util.Collection', 'java.lang.Iterable',\n           'java.util.Iterator', 'java.util.Map', 'java.util.Set'}\nCLOSURE = {'com.google.javascript.jscomp.AbstractCompiler', 'com.google.javascript.jscomp.ControlFlowGraph',\n           'com.google.javascript.jscomp.type.ReverseAbstractInterpreter', 'com.google.javascript.jscomp.Scope',\n           'com.google.javascript.jscomp.Scope$Var', 'com.google.javascript.jscomp.type.FlowScope',\n           'com.google.javascript.rhino.Node', 'com.google.javascript.rhino.jstype.JSType',\n           'com.google.javascript.rhino.jstype.ObjectType', 'com.google.javascript.rhino.jstype.JSTypeNative',\n           'com.google.javascript.rhino.jstype.BooleanLiteralSet'}\nJXPATH = {'org.w3c.dom.Node', 'org.w3c.dom.Document', 'org.w3c.dom.Element',\n          'org.apache.commons.jxpath.ri.QName', 'org.apache.commons.jxpath.ri.compiler.NodeTest',\n          'org.apache.commons.jxpath.ri.model.NodePointer'}\n# Methods requiring specialized AST parent/sibling/call metadata have no reviewed\n# recipe yet. This list is a structural restriction, not an outcome-based prune.\nCLOSURE_METHODS = {'createEntryLattice', 'createInitialEstimateLattice', 'flowThrough',\n    'branchedFlowThrough', 'isAddedAsNumber', 'isUnflowable', 'newBooleanOutcomePair',\n    'traverseAnd', 'traverseOr', 'traverseShortCircuitingBinOp', 'traverseWithinShortCircuitingBinOp',\n    'narrowScope', 'traverse', 'traverseAdd', 'traverseArrayLiteral', 'traverseAssign',\n    'traverseChildren', 'traverseGetElem', 'traverseGetProp', 'traverseHook', 'traverseName',\n    'traverseObjectLiteral', 'traverseReturn', 'getJSType', 'getNativeType',\n    'redeclareSimpleVar', 'updateScopeForTypeChange', 'getBooleanOutcomes'}\n\n\ndef select(targets, policy):\n    if policy is None:\n        return targets, []\n    if policy == POLICY_V13:\n        fields = ('class', 'constructor_types', 'method', 'parameter_types')\n        selected, excluded = select(targets, POLICY_V12)\n        chosen = {tuple(t[k] for k in fields) for t in selected} | CODEC_SIGNATURES\n        return ([t for t in targets if tuple(t[k] for k in fields) in chosen],\n                [r for r in excluded if tuple(r['target'][k] for k in fields) not in chosen])\n    if policy == POLICY_V12:\n        fields = ('class', 'constructor_types', 'method', 'parameter_types')\n        selected, excluded = select(targets, POLICY_V11)\n        chosen = {tuple(t[k] for k in fields) for t in selected} | GRAPHICS_SIGNATURES\n        return ([t for t in targets if tuple(t[k] for k in fields) in chosen],\n                [r for r in excluded if tuple(r['target'][k] for k in fields) not in chosen])\n    if policy == POLICY_V11:\n        fields = ('class', 'constructor_types', 'method', 'parameter_types')\n        selected, excluded = select(targets, POLICY_V10)\n        chosen = {tuple(t[k] for k in fields) for t in selected} | CHRONOLOGY_SIGNATURES\n        return ([t for t in targets if tuple(t[k] for k in fields) in chosen],\n                [r for r in excluded if tuple(r['target'][k] for k in fields) not in chosen])\n    if policy not in {POLICY, POLICY_V4, POLICY_V5, POLICY_V6, POLICY_V10, POLICY_V11, POLICY_V12, POLICY_V13}:\n        raise ValueError('Unknown explicit fixture policy')\n    if policy == POLICY_V10:\n        # Preserve v5+Math and add only exact peer-approved identities. JDOM is a repair.\n        fields = ('class', 'constructor_types', 'method', 'parameter_types')\n        selected, excluded = select(targets, POLICY_V6)\n        chosen = {tuple(t[k] for k in fields) for t in selected} | JOINT_SIGNATURES\n        return ([t for t in targets if tuple(t[k] for k in fields) in chosen],\n                [r for r in excluded if tuple(r['target'][k] for k in fields) not in chosen])\n    if policy == POLICY_V6:\n        # Keep every v5 decision, adding only the exact Champ-accepted signatures.\n        selected, excluded = select(targets, POLICY_V5)\n        accepted = lambda t: (t['class'] in {\n            'org.apache.commons.math3.fraction.BigFraction', 'org.apache.commons.math3.fraction.Fraction'}\n            and t['constructor_types'] == 'double' and t['method'] == 'getField' and t['parameter_types'] == '')\n        chosen = {tuple(t[k] for k in ('class', 'constructor_types', 'method', 'parameter_types')) for t in selected}\n        return ([t for t in targets if accepted(t) or tuple(t[k] for k in ('class', 'constructor_types', 'method', 'parameter_types')) in chosen],\n                [row for row in excluded if not accepted(row['target'])])\n    selected, excluded = [], []\n    for target in targets:\n        name = target['class']\n        family = CLOSURE if name == 'com.google.javascript.jscomp.TypeInference' else JXPATH if name in {\n            'org.apache.commons.jxpath.ri.model.dom.DOMNodePointer',\n            'org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer'} else set()\n        extra = policy in {POLICY_V4, POLICY_V5} and name in ADDITIONAL_METHODS\n        pilot = policy == POLICY_V5 and name in PILOT_METHODS\n        if extra:\n            family = {'java.io.Reader'}\n        if pilot:\n            family = PILOT_TYPES | {'[B', '[S', '[I', '[J', '[F', '[D', '[C'}\n        reason = None\n        if not family:\n            reason = 'explicit_project_recipe_not_reviewed'\n        elif target['method'] in {'<init>', 'hashCode'}:\n            reason = 'constructor_or_identity_oracle_not_reviewed'\n        elif family is CLOSURE and target['method'] not in CLOSURE_METHODS:\n            reason = 'specialized_ast_recipe_not_reviewed'\n        elif extra and target['method'] not in ADDITIONAL_METHODS[name]:\n            reason = 'additional_method_preconditions_or_state_not_reviewed'\n        elif extra and name.endswith('ExtendedBufferedReader') and target['parameter_types']:\n            reason = 'reader_buffer_offset_bounds_recipe_not_reviewed'\n        elif pilot and target['method'] not in PILOT_METHODS[name]:\n            reason = 'pilot_method_preconditions_or_oracle_not_reviewed'\n        elif pilot and name.endswith('NumberInput') and '[' in target['parameter_types']:\n            reason = 'numeric_buffer_slice_recipe_not_reviewed'\n        elif pilot and name.endswith('TextBuffer') and target['method'] == 'append' and target['parameter_types'] != 'char':\n            reason = 'text_buffer_slice_recipe_not_reviewed'\n        elif pilot and name.endswith('Document') and target['parameter_types'] == 'org.jsoup.nodes.Element':\n            reason = 'html_internal_normalise_recipe_not_reviewed'\n        elif pilot and name.endswith('CpioArchiveOutputStream') and target['method'] == 'write' and target['parameter_types'] != 'int':\n            reason = 'archive_buffer_slice_recipe_not_reviewed'\n        elif pilot and name.endswith('BeanPropertyWriter') and target['method'] == 'isRequired' and target['parameter_types']:\n            reason = 'annotation_introspector_recipe_not_reviewed'\n        elif pilot and name.endswith('RemoveUnusedVars') and target['method'] == 'process' and target['parameter_types'] != 'com.google.javascript.rhino.Node,com.google.javascript.rhino.Node':\n            reason = 'call_site_definition_finder_recipe_not_reviewed'\n        else:\n            required = set(filter(None, (target['constructor_types'] + ',' + target['parameter_types']).split(',')))\n            missing = required - SCALARS - family\n            if missing:\n                reason = 'explicit_argument_recipe_missing:' + ','.join(sorted(missing))\n        if reason:\n            excluded.append({'target': target, 'reason': reason})\n        else:\n            selected.append(target)\n    return selected, excluded\n"
  }
}
```
