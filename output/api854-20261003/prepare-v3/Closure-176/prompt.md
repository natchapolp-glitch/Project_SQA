Generate a deterministic JUnit 4 Java suite using only the supplied fixed source, build context, shared target declarations and fixture policy. Return complete Java code fences with explicit package declarations, public test classes and imports. Use at most 30 @Test methods. Use meaningful assertions derived from fixed API behavior; avoid non-null-only or empty tests, randomness, time dependence and external services. Do not modify or shadow production code. No assertion repairs or feedback loop. Suites exceeding 30 methods are rejected entirely.

Project: Closure; fixed revision: 176f.
Modified target classes:
com.google.javascript.jscomp.TypeInference

Fixture policy: common production types in fixed/buggy; simplest supported constructor selected by the shared probe. Use only the listed shared signatures and common fixture types.

Shared target declarations:
```json
[
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "",
    "method": "getBooleanOutcomes",
    "parameter_types": "com.google.javascript.rhino.jstype.BooleanLiteralSet,com.google.javascript.rhino.jstype.BooleanLiteralSet,boolean"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "",
    "method": "inferPropertyTypesToMatchConstraint",
    "parameter_types": "com.google.javascript.rhino.jstype.JSType,com.google.javascript.rhino.jstype.JSType"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "",
    "method": "resolvedTemplateType",
    "parameter_types": "java.util.Map,com.google.javascript.rhino.jstype.TemplateType,com.google.javascript.rhino.jstype.JSType"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "<init>",
    "parameter_types": ""
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "backwardsInferenceFromCallSite",
    "parameter_types": "com.google.javascript.rhino.Node,com.google.javascript.rhino.jstype.FunctionType"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "branchedFlowThrough",
    "parameter_types": "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "createEntryLattice",
    "parameter_types": ""
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "createInitialEstimateLattice",
    "parameter_types": ""
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "dereferencePointer",
    "parameter_types": "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "ensurePropertyDeclared",
    "parameter_types": "com.google.javascript.rhino.Node"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "ensurePropertyDeclaredHelper",
    "parameter_types": "com.google.javascript.rhino.Node,com.google.javascript.rhino.jstype.ObjectType"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "ensurePropertyDefined",
    "parameter_types": "com.google.javascript.rhino.Node,com.google.javascript.rhino.jstype.JSType"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "flowThrough",
    "parameter_types": "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "getBooleanOutcomePair",
    "parameter_types": "com.google.javascript.jscomp.TypeInference$BooleanOutcomePair,com.google.javascript.jscomp.TypeInference$BooleanOutcomePair,boolean"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "getJSType",
    "parameter_types": "com.google.javascript.rhino.Node"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "getNativeType",
    "parameter_types": "com.google.javascript.rhino.jstype.JSTypeNative"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "getPropertyType",
    "parameter_types": "com.google.javascript.rhino.jstype.JSType,java.lang.String,com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "inferArguments",
    "parameter_types": "com.google.javascript.jscomp.Scope"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "inferTemplateTypesFromParameters",
    "parameter_types": "com.google.javascript.rhino.jstype.FunctionType,com.google.javascript.rhino.Node"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "inferTemplatedTypesForCall",
    "parameter_types": "com.google.javascript.rhino.Node,com.google.javascript.rhino.jstype.FunctionType"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "isAddedAsNumber",
    "parameter_types": "com.google.javascript.rhino.jstype.JSType"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "isUnflowable",
    "parameter_types": "com.google.javascript.jscomp.Scope$Var"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "maybeResolveTemplateTypeFromNodes",
    "parameter_types": "java.lang.Iterable,java.lang.Iterable,java.util.Map,java.util.Set"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "maybeResolveTemplateTypeFromNodes",
    "parameter_types": "java.util.Iterator,java.util.Iterator,java.util.Map,java.util.Set"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "maybeResolveTemplatedType",
    "parameter_types": "com.google.javascript.rhino.jstype.JSType,com.google.javascript.rhino.jstype.JSType,java.util.Map,java.util.Set"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "narrowScope",
    "parameter_types": "com.google.javascript.jscomp.type.FlowScope,com.google.javascript.rhino.Node,com.google.javascript.rhino.jstype.JSType"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "newBooleanOutcomePair",
    "parameter_types": "com.google.javascript.rhino.jstype.JSType,com.google.javascript.jscomp.type.FlowScope"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "redeclareSimpleVar",
    "parameter_types": "com.google.javascript.jscomp.type.FlowScope,com.google.javascript.rhino.Node,com.google.javascript.rhino.jstype.JSType"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "tightenTypesAfterAssertions",
    "parameter_types": "com.google.javascript.jscomp.type.FlowScope,com.google.javascript.rhino.Node"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "traverse",
    "parameter_types": "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "traverseAdd",
    "parameter_types": "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "traverseAnd",
    "parameter_types": "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "traverseArrayLiteral",
    "parameter_types": "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "traverseAssign",
    "parameter_types": "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "traverseCall",
    "parameter_types": "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "traverseCatch",
    "parameter_types": "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "traverseChildren",
    "parameter_types": "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "traverseGetElem",
    "parameter_types": "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "traverseGetProp",
    "parameter_types": "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "traverseHook",
    "parameter_types": "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "traverseName",
    "parameter_types": "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "traverseNew",
    "parameter_types": "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "traverseObjectLiteral",
    "parameter_types": "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "traverseOr",
    "parameter_types": "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "traverseReturn",
    "parameter_types": "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "traverseShortCircuitingBinOp",
    "parameter_types": "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope,boolean"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "traverseWithinShortCircuitingBinOp",
    "parameter_types": "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "updateBind",
    "parameter_types": "com.google.javascript.rhino.Node"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "updateScopeForTypeChange",
    "parameter_types": "com.google.javascript.jscomp.type.FlowScope,com.google.javascript.rhino.Node,com.google.javascript.rhino.jstype.JSType,com.google.javascript.rhino.jstype.JSType"
  },
  {
    "class": "com.google.javascript.jscomp.TypeInference",
    "constructor_types": "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map",
    "method": "updateTypeOfParameters",
    "parameter_types": "com.google.javascript.rhino.Node,com.google.javascript.rhino.jstype.FunctionType"
  }
]
```

Common compiled production fixture classes (eligibility only, not oracle approval):
```json
[
  "com.google.debugging.sourcemap.Base64",
  "com.google.debugging.sourcemap.Base64VLQ",
  "com.google.debugging.sourcemap.FilePosition",
  "com.google.debugging.sourcemap.SourceMapConsumer",
  "com.google.debugging.sourcemap.SourceMapConsumerFactory",
  "com.google.debugging.sourcemap.SourceMapConsumerV1",
  "com.google.debugging.sourcemap.SourceMapConsumerV2",
  "com.google.debugging.sourcemap.SourceMapConsumerV3",
  "com.google.debugging.sourcemap.SourceMapFormat",
  "com.google.debugging.sourcemap.SourceMapGenerator",
  "com.google.debugging.sourcemap.SourceMapGeneratorFactory",
  "com.google.debugging.sourcemap.SourceMapGeneratorV1",
  "com.google.debugging.sourcemap.SourceMapGeneratorV2",
  "com.google.debugging.sourcemap.SourceMapGeneratorV3",
  "com.google.debugging.sourcemap.SourceMapLineDecoder",
  "com.google.debugging.sourcemap.SourceMapParseException",
  "com.google.debugging.sourcemap.SourceMapSection",
  "com.google.debugging.sourcemap.SourceMapSupplier",
  "com.google.debugging.sourcemap.SourceMapping",
  "com.google.debugging.sourcemap.SourceMappingReversable",
  "com.google.debugging.sourcemap.Util",
  "com.google.debugging.sourcemap.proto.Mapping",
  "com.google.javascript.jscomp.AbstractCommandLineRunner",
  "com.google.javascript.jscomp.AbstractCompiler",
  "com.google.javascript.jscomp.AbstractMessageFormatter",
  "com.google.javascript.jscomp.AbstractPeepholeOptimization",
  "com.google.javascript.jscomp.AliasExternals",
  "com.google.javascript.jscomp.AliasKeywords",
  "com.google.javascript.jscomp.AliasStrings",
  "com.google.javascript.jscomp.AmbiguateProperties",
  "com.google.javascript.jscomp.AnalyzeNameReferences",
  "com.google.javascript.jscomp.AnalyzePrototypeProperties",
  "com.google.javascript.jscomp.AngularPass",
  "com.google.javascript.jscomp.AnonymousFunctionNamingCallback",
  "com.google.javascript.jscomp.AnonymousFunctionNamingPolicy",
  "com.google.javascript.jscomp.AstChangeProxy",
  "com.google.javascript.jscomp.AstParallelizer",
  "com.google.javascript.jscomp.AstValidator",
  "com.google.javascript.jscomp.BasicErrorManager",
  "com.google.javascript.jscomp.BitField",
  "com.google.javascript.jscomp.ByPathWarningsGuard",
  "com.google.javascript.jscomp.CallGraph",
  "com.google.javascript.jscomp.ChainCalls",
  "com.google.javascript.jscomp.CheckAccessControls",
  "com.google.javascript.jscomp.CheckDebuggerStatement",
  "com.google.javascript.jscomp.CheckEventfulObjectDisposal",
  "com.google.javascript.jscomp.CheckGlobalNames",
  "com.google.javascript.jscomp.CheckGlobalThis",
  "com.google.javascript.jscomp.CheckLevel",
  "com.google.javascript.jscomp.CheckLevelLegacy",
  "com.google.javascript.jscomp.CheckMissingGetCssName",
  "com.google.javascript.jscomp.CheckMissingReturn",
  "com.google.javascript.jscomp.CheckPathsBetweenNodes",
  "com.google.javascript.jscomp.CheckProvides",
  "com.google.javascript.jscomp.CheckRegExp",
  "com.google.javascript.jscomp.CheckRequiresForConstructors",
  "com.google.javascript.jscomp.CheckSideEffects",
  "com.google.javascript.jscomp.CheckSuspiciousCode",
  "com.google.javascript.jscomp.CheckUnreachableCode",
  "com.google.javascript.jscomp.CleanupPasses",
  "com.google.javascript.jscomp.ClosureCodeRemoval",
  "com.google.javascript.jscomp.ClosureCodingConvention",
  "com.google.javascript.jscomp.ClosureOptimizePrimitives",
  "com.google.javascript.jscomp.ClosureRewriteClass",
  "com.google.javascript.jscomp.CoalesceVariableNames",
  "com.google.javascript.jscomp.CodeChangeHandler",
  "com.google.javascript.jscomp.CodeConsumer",
  "com.google.javascript.jscomp.CodeGenerator",
  "com.google.javascript.jscomp.CodePrinter",
  "com.google.javascript.jscomp.CodingConvention",
  "com.google.javascript.jscomp.CodingConventions",
  "com.google.javascript.jscomp.CollapseAnonymousFunctions",
  "com.google.javascript.jscomp.CollapseProperties",
  "com.google.javascript.jscomp.CollapseVariableDeclarations",
  "com.google.javascript.jscomp.CombinedCompilerPass",
  "com.google.javascript.jscomp.CommandLineRunner",
  "com.google.javascript.jscomp.CompilationLevel",
  "com.google.javascript.jscomp.Compiler",
  "com.google.javascript.jscomp.CompilerInput",
  "com.google.javascript.jscomp.CompilerOptions",
  "com.google.javascript.jscomp.CompilerPass",
  "com.google.javascript.jscomp.ComposeWarningsGuard",
  "com.google.javascript.jscomp.ConcreteType",
  "com.google.javascript.jscomp.ConstCheck",
  "com.google.javascript.jscomp.ConstParamCheck",
  "com.google.javascript.jscomp.ControlFlowAnalysis",
  "com.google.javascript.jscomp.ControlFlowGraph",
  "com.google.javascript.jscomp.ControlStructureCheck",
  "com.google.javascript.jscomp.ConvertToDottedProperties",
  "com.google.javascript.jscomp.CoverageInstrumentationCallback",
  "com.google.javascript.jscomp.CoverageInstrumentationPass",
  "com.google.javascript.jscomp.CoverageUtil",
  "com.google.javascript.jscomp.CreateSyntheticBlocks",
  "com.google.javascript.jscomp.CrossModuleCodeMotion",
  "com.google.javascript.jscomp.CrossModuleMethodMotion",
  "com.google.javascript.jscomp.CssRenamingMap",
  "com.google.javascript.jscomp.CustomPassExecutionTime",
  "com.google.javascript.jscomp.DataFlowAnalysis",
  "com.google.javascript.jscomp.DeadAssignmentsElimination",
  "com.google.javascript.jscomp.DefaultPassConfig",
  "com.google.javascript.jscomp.DefinitionProvider",
  "com.google.javascript.jscomp.DefinitionSite",
  "com.google.javascript.jscomp.DefinitionsRemover",
  "com.google.javascript.jscomp.Denormalize",
  "com.google.javascript.jscomp.DependencyOptions",
  "com.google.javascript.jscomp.DevirtualizePrototypeMethods",
  "com.google.javascript.jscomp.DiagnosticGroup",
  "com.google.javascript.jscomp.DiagnosticGroupWarningsGuard",
  "com.google.javascript.jscomp.DiagnosticGroups",
  "com.google.javascript.jscomp.DiagnosticType",
  "com.google.javascript.jscomp.DisambiguatePrivateProperties",
  "com.google.javascript.jscomp.DisambiguateProperties",
  "com.google.javascript.jscomp.DotFormatter",
  "com.google.javascript.jscomp.EmptyMessageBundle",
  "com.google.javascript.jscomp.ErrorFormat",
  "com.google.javascript.jscomp.ErrorHandler",
  "com.google.javascript.jscomp.ErrorManager",
  "com.google.javascript.jscomp.ErrorPass",
  "com.google.javascript.jscomp.ExpandJqueryAliases",
  "com.google.javascript.jscomp.ExploitAssigns",
  "com.google.javascript.jscomp.ExportTestFunctions",
  "com.google.javascript.jscomp.ExpressionDecomposer",
  "com.google.javascript.jscomp.ExternExportsPass",
  "com.google.javascript.jscomp.ExtractPrototypeMemberDeclarations",
  "com.google.javascript.jscomp.FieldCleanupPass",
  "com.google.javascript.jscomp.FileInstrumentationData",
  "com.google.javascript.jscomp.FindExportableNodes",
  "com.google.javascript.jscomp.FlowSensitiveInlineVariables",
  "com.google.javascript.jscomp.ForbiddenChange",
  "com.google.javascript.jscomp.FunctionArgumentInjector",
  "com.google.javascript.jscomp.FunctionInfo",
  "com.google.javascript.jscomp.FunctionInformationMap",
  "com.google.javascript.jscomp.FunctionInformationMapOrBuilder",
  "com.google.javascript.jscomp.FunctionInjector",
  "com.google.javascript.jscomp.FunctionNames",
  "com.google.javascript.jscomp.FunctionRewriter",
  "com.google.javascript.jscomp.FunctionToBlockMutator",
  "com.google.javascript.jscomp.FunctionTypeBuilder",
  "com.google.javascript.jscomp.GatherCharacterEncodingBias",
  "com.google.javascript.jscomp.GatherRawExports",
  "com.google.javascript.jscomp.GatherSideEffectSubexpressionsCallback",
  "com.google.javascript.jscomp.GenerateExports",
  "com.google.javascript.jscomp.GlobalNamespace",
  "com.google.javascript.jscomp.GlobalVarReferenceMap",
  "com.google.javascript.jscomp.GoogleCodingConvention",
  "com.google.javascript.jscomp.GoogleJsMessageIdGenerator",
  "com.google.javascript.jscomp.GroupVariableDeclarations",
  "com.google.javascript.jscomp.HotSwapCompilerPass",
  "com.google.javascript.jscomp.IgnoreCajaProperties",
  "com.google.javascript.jscomp.InferJSDocInfo",
  "com.google.javascript.jscomp.InlineCostEstimator",
  "com.google.javascript.jscomp.InlineFunctions",
  "com.google.javascript.jscomp.InlineObjectLiterals",
  "com.google.javascript.jscomp.InlineProperties",
  "com.google.javascript.jscomp.InlineSimpleMethods",
  "com.google.javascript.jscomp.InlineVariables",
  "com.google.javascript.jscomp.InstrumentFunctions",
  "com.google.javascript.jscomp.InstrumentMemoryAllocPass",
  "com.google.javascript.jscomp.Instrumentation",
  "com.google.javascript.jscomp.InstrumentationOrBuilder",
  "com.google.javascript.jscomp.InstrumentationTemplate",
  "com.google.javascript.jscomp.InvocationsCallback",
  "com.google.javascript.jscomp.JSError",
  "com.google.javascript.jscomp.JSModule",
  "com.google.javascript.jscomp.JSModuleGraph",
  "com.google.javascript.jscomp.JSSourceFile",
  "com.google.javascript.jscomp.JoinOp",
  "com.google.javascript.jscomp.JqueryCodingConvention",
  "com.google.javascript.jscomp.JsAst",
  "com.google.javascript.jscomp.JsMessage",
  "com.google.javascript.jscomp.JsMessageDefinition",
  "com.google.javascript.jscomp.JsMessageExtractor",
  "com.google.javascript.jscomp.JsMessageVisitor",
  "com.google.javascript.jscomp.JvmMetrics",
  "com.google.javascript.jscomp.LightweightMessageFormatter",
  "com.google.javascript.jscomp.LineNumberCheck",
  "com.google.javascript.jscomp.LinkedFlowScope",
  "com.google.javascript.jscomp.LiveVariablesAnalysis",
  "com.google.javascript.jscomp.LoggerErrorManager",
  "com.google.javascript.jscomp.MakeDeclaredNamesUnique",
  "com.google.javascript.jscomp.MarkNoSideEffectCalls",
  "com.google.javascript.jscomp.MaybeReachingVariableUse",
  "com.google.javascript.jscomp.MemoizedScopeCreator",
  "com.google.javascript.jscomp.MessageBundle",
  "com.google.javascript.jscomp.MessageFormatter",
  "com.google.javascript.jscomp.MethodCompilerPass",
  "com.google.javascript.jscomp.MinimizeExitPoints",
  "com.google.javascript.jscomp.MinimizedCondition",
  "com.google.javascript.jscomp.MoveFunctionDeclarations",
  "com.google.javascript.jscomp.MustBeReachingVariableDef",
  "com.google.javascript.jscomp.NameAnalyzer",
  "com.google.javascript.jscomp.NameAnonymousFunctions",
  "com.google.javascript.jscomp.NameAnonymousFunctionsMapped",
  "com.google.javascript.jscomp.NameGenerator",
  "com.google.javascript.jscomp.NameReferenceGraph",
  "com.google.javascript.jscomp.NameReferenceGraphConstruction",
  "com.google.javascript.jscomp.NameReferenceGraphReport",
  "com.google.javascript.jscomp.NodeIterators",
  "com.google.javascript.jscomp.NodeNameExtractor",
  "com.google.javascript.jscomp.NodeTraversal",
  "com.google.javascript.jscomp.NodeUtil",
  "com.google.javascript.jscomp.Normalize",
  "com.google.javascript.jscomp.ObjectPropertyStringPostprocess",
  "com.google.javascript.jscomp.ObjectPropertyStringPreprocess",
  "com.google.javascript.jscomp.OptimizeArgumentsArray",
  "com.google.javascript.jscomp.OptimizeCalls",
  "com.google.javascript.jscomp.OptimizeParameters",
  "com.google.javascript.jscomp.OptimizeReturns",
  "com.google.javascript.jscomp.PassConfig",
  "com.google.javascript.jscomp.PassFactory",
  "com.google.javascript.jscomp.PeepholeCollectPropertyAssignments",
  "com.google.javascript.jscomp.PeepholeFoldConstants",
  "com.google.javascript.jscomp.PeepholeFoldWithTypes",
  "com.google.javascript.jscomp.PeepholeMinimizeConditions",
  "com.google.javascript.jscomp.PeepholeOptimizationsPass",
  "com.google.javascript.jscomp.PeepholeRemoveDeadCode",
  "com.google.javascript.jscomp.PeepholeReplaceKnownMethods",
  "com.google.javascript.jscomp.PeepholeSimplifyRegExp",
  "com.google.javascript.jscomp.PeepholeSubstituteAlternateSyntax",
  "com.google.javascript.jscomp.PerformanceTracker",
  "com.google.javascript.jscomp.PhaseOptimizer",
  "com.google.javascript.jscomp.PrepareAst",
  "com.google.javascript.jscomp.PreprocessorSymbolTable",
  "com.google.javascript.jscomp.PrintStreamErrorManager",
  "com.google.javascript.jscomp.ProcessClosurePrimitives",
  "com.google.javascript.jscomp.ProcessCommonJSModules",
  "com.google.javascript.jscomp.ProcessDefines",
  "com.google.javascript.jscomp.ProcessTweaks",
  "com.google.javascript.jscomp.PropertyRenamingPolicy",
  "com.google.javascript.jscomp.PureFunctionIdentifier",
  "com.google.javascript.jscomp.RecentChange",
  "com.google.javascript.jscomp.RecordFunctionInformation",
  "com.google.javascript.jscomp.ReferenceCollectingCallback",
  "com.google.javascript.jscomp.Region",
  "com.google.javascript.jscomp.RemoveTryCatch",
  "com.google.javascript.jscomp.RemoveUnusedClassProperties",
  "com.google.javascript.jscomp.RemoveUnusedNames",
  "com.google.javascript.jscomp.RemoveUnusedPrototypeProperties",
  "com.google.javascript.jscomp.RemoveUnusedVars",
  "com.google.javascript.jscomp.RenameLabels",
  "com.google.javascript.jscomp.RenameProperties",
  "com.google.javascript.jscomp.RenamePrototypes",
  "com.google.javascript.jscomp.RenameVars",
  "com.google.javascript.jscomp.RenameVars2",
  "com.google.javascript.jscomp.RenamingMap",
  "com.google.javascript.jscomp.ReorderConstantExpression",
  "com.google.javascript.jscomp.ReplaceCssNames",
  "com.google.javascript.jscomp.ReplaceIdGenerators",
  "com.google.javascript.jscomp.ReplaceMessages",
  "com.google.javascript.jscomp.ReplaceMessagesForChrome",
  "com.google.javascript.jscomp.ReplaceStrings",
  "com.google.javascript.jscomp.RescopeGlobalSymbols",
  "com.google.javascript.jscomp.Result",
  "com.google.javascript.jscomp.RhinoErrorReporter",
  "com.google.javascript.jscomp.RuntimeTypeCheck",
  "com.google.javascript.jscomp.SanityCheck",
  "com.google.javascript.jscomp.Scope",
  "com.google.javascript.jscomp.ScopeCreator",
  "com.google.javascript.jscomp.ScopedAliases",
  "com.google.javascript.jscomp.ShadowVariables",
  "com.google.javascript.jscomp.ShadowVariables2",
  "com.google.javascript.jscomp.ShowByPathWarningsGuard",
  "com.google.javascript.jscomp.SideEffectsAnalysis",
  "com.google.javascript.jscomp.SimpleDefinitionFinder",
  "com.google.javascript.jscomp.SimpleFunctionAliasAnalysis",
  "com.google.javascript.jscomp.SimpleRegion",
  "com.google.javascript.jscomp.SourceAst",
  "com.google.javascript.jscomp.SourceExcerptProvider",
  "com.google.javascript.jscomp.SourceFile",
  "com.google.javascript.jscomp.SourceInformationAnnotator",
  "com.google.javascript.jscomp.SourceMap",
  "com.google.javascript.jscomp.SpecializationAwareCompilerPass",
  "com.google.javascript.jscomp.SpecializeModule",
  "com.google.javascript.jscomp.StatementFusion",
  "com.google.javascript.jscomp.StrictModeCheck",
  "com.google.javascript.jscomp.StrictWarningsGuard",
  "com.google.javascript.jscomp.Strings",
  "com.google.javascript.jscomp.StripCode",
  "com.google.javascript.jscomp.SuppressDocWarningsGuard",
  "com.google.javascript.jscomp.SymbolTable",
  "com.google.javascript.jscomp.SyntacticScopeCreator",
  "com.google.javascript.jscomp.SyntheticAst",
  "com.google.javascript.jscomp.TightenTypes",
  "com.google.javascript.jscomp.Tracer",
  "com.google.javascript.jscomp.TransformAMDToCJSModule",
  "com.google.javascript.jscomp.TypeCheck",
  "com.google.javascript.jscomp.TypeInference",
  "com.google.javascript.jscomp.TypeInferencePass",
  "com.google.javascript.jscomp.TypeValidator",
  "com.google.javascript.jscomp.TypedCodeGenerator",
  "com.google.javascript.jscomp.TypedScopeCreator",
  "com.google.javascript.jscomp.UnreachableCodeElimination",
  "com.google.javascript.jscomp.UseSite",
  "com.google.javascript.jscomp.VarCheck",
  "com.google.javascript.jscomp.VariableMap",
  "com.google.javascript.jscomp.VariableReferenceCheck",
  "com.google.javascript.jscomp.VariableRenamingPolicy",
  "com.google.javascript.jscomp.VariableVisibilityAnalysis",
  "com.google.javascript.jscomp.VerboseMessageFormatter",
  "com.google.javascript.jscomp.WarningLevel",
  "com.google.javascript.jscomp.WarningsGuard",
  "com.google.javascript.jscomp.WhitelistWarningsGuard",
  "com.google.javascript.jscomp.XtbMessageBundle",
  "com.google.javascript.jscomp.ant.AntErrorManager",
  "com.google.javascript.jscomp.ant.CompileTask",
  "com.google.javascript.jscomp.ant.Warning",
  "com.google.javascript.jscomp.deps.DependencyInfo",
  "com.google.javascript.jscomp.deps.DepsFileParser",
  "com.google.javascript.jscomp.deps.DepsGenerator",
  "com.google.javascript.jscomp.deps.JsFileLineParser",
  "com.google.javascript.jscomp.deps.JsFileParser",
  "com.google.javascript.jscomp.deps.JsFunctionParser",
  "com.google.javascript.jscomp.deps.PathUtil",
  "com.google.javascript.jscomp.deps.SimpleDependencyInfo",
  "com.google.javascript.jscomp.deps.SortedDependencies",
  "com.google.javascript.jscomp.fuzzing.DiscreteDistribution",
  "com.google.javascript.jscomp.fuzzing.Driver",
  "com.google.javascript.jscomp.fuzzing.Expression",
  "com.google.javascript.jscomp.fuzzing.Fuzzer",
  "com.google.javascript.jscomp.fuzzing.Scope",
  "com.google.javascript.jscomp.fuzzing.ScopeManager",
  "com.google.javascript.jscomp.fuzzing.Statement",
  "com.google.javascript.jscomp.fuzzing.StringGenerator",
  "com.google.javascript.jscomp.graph.AdjacencyGraph",
  "com.google.javascript.jscomp.graph.Annotatable",
  "com.google.javascript.jscomp.graph.Annotation",
  "com.google.javascript.jscomp.graph.DiGraph",
  "com.google.javascript.jscomp.graph.FixedPointGraphTraversal",
  "com.google.javascript.jscomp.graph.Graph",
  "com.google.javascript.jscomp.graph.GraphColoring",
  "com.google.javascript.jscomp.graph.GraphNode",
  "com.google.javascript.jscomp.graph.GraphPruner",
  "com.google.javascript.jscomp.graph.GraphReachability",
  "com.google.javascript.jscomp.graph.GraphvizGraph",
  "com.google.javascript.jscomp.graph.LatticeElement",
  "com.google.javascript.jscomp.graph.LinkedDirectedGraph",
  "com.google.javascript.jscomp.graph.LinkedUndirectedGraph",
  "com.google.javascript.jscomp.graph.StandardUnionFind",
  "com.google.javascript.jscomp.graph.SubGraph",
  "com.google.javascript.jscomp.graph.UndiGraph",
  "com.google.javascript.jscomp.graph.UnionFind",
  "com.google.javascript.jscomp.parsing.Annotation",
  "com.google.javascript.jscomp.parsing.Config",
  "com.google.javascript.jscomp.parsing.IRFactory",
  "com.google.javascript.jscomp.parsing.JsDocInfoParser",
  "com.google.javascript.jscomp.parsing.JsDocToken",
  "com.google.javascript.jscomp.parsing.JsDocTokenStream",
  "com.google.javascript.jscomp.parsing.NullErrorReporter",
  "com.google.javascript.jscomp.parsing.ParserRunner",
  "com.google.javascript.jscomp.parsing.TypeSafeDispatcher",
  "com.google.javascript.jscomp.parsing.parser.IdentifierToken",
  "com.google.javascript.jscomp.parsing.parser.Keywords",
  "com.google.javascript.jscomp.parsing.parser.LineNumberTable",
  "com.google.javascript.jscomp.parsing.parser.LiteralToken",
  "com.google.javascript.jscomp.parsing.parser.ParseTreeValidator",
  "com.google.javascript.jscomp.parsing.parser.ParseTreeVisitor",
  "com.google.javascript.jscomp.parsing.parser.Parser",
  "com.google.javascript.jscomp.parsing.parser.PredefinedName",
  "com.google.javascript.jscomp.parsing.parser.Scanner",
  "com.google.javascript.jscomp.parsing.parser.SourceFile",
  "com.google.javascript.jscomp.parsing.parser.Token",
  "com.google.javascript.jscomp.parsing.parser.TokenType",
  "com.google.javascript.jscomp.parsing.parser.codegeneration.ParseTreeFactory",
  "com.google.javascript.jscomp.parsing.parser.codegeneration.ParseTreeTransformer",
  "com.google.javascript.jscomp.parsing.parser.codegeneration.ParseTreeWriter",
  "com.google.javascript.jscomp.parsing.parser.trees.ArgumentListTree",
  "com.google.javascript.jscomp.parsing.parser.trees.ArrayLiteralExpressionTree",
  "com.google.javascript.jscomp.parsing.parser.trees.ArrayPatternTree",
  "com.google.javascript.jscomp.parsing.parser.trees.AwaitStatementTree",
  "com.google.javascript.jscomp.parsing.parser.trees.BinaryOperatorTree",
  "com.google.javascript.jscomp.parsing.parser.trees.BlockTree",
  "com.google.javascript.jscomp.parsing.parser.trees.BreakStatementTree",
  "com.google.javascript.jscomp.parsing.parser.trees.CallExpressionTree",
  "com.google.javascript.jscomp.parsing.parser.trees.CaseClauseTree",
  "com.google.javascript.jscomp.parsing.parser.trees.CatchTree",
  "com.google.javascript.jscomp.parsing.parser.trees.ClassDeclarationTree",
  "com.google.javascript.jscomp.parsing.parser.trees.ClassExpressionTree",
  "com.google.javascript.jscomp.parsing.parser.trees.CommaExpressionTree",
  "com.google.javascript.jscomp.parsing.parser.trees.ConditionalExpressionTree",
  "com.google.javascript.jscomp.parsing.parser.trees.ContinueStatementTree",
  "com.google.javascript.jscomp.parsing.parser.trees.DebuggerStatementTree",
  "com.google.javascript.jscomp.parsing.parser.trees.DefaultClauseTree",
  "com.google.javascript.jscomp.parsing.parser.trees.DefaultParameterTree",
  "com.google.javascript.jscomp.parsing.parser.trees.DoWhileStatementTree",
  "com.google.javascript.jscomp.parsing.parser.trees.EmptyStatementTree",
  "com.google.javascript.jscomp.parsing.parser.trees.ExportDeclarationTree",
  "com.google.javascript.jscomp.parsing.parser.trees.ExpressionStatementTree",
  "com.google.javascript.jscomp.parsing.parser.trees.FieldDeclarationTree",
  "com.google.javascript.jscomp.parsing.parser.trees.FinallyTree",
  "com.google.javascript.jscomp.parsing.parser.trees.ForEachStatementTree",
  "com.google.javascript.jscomp.parsing.parser.trees.ForInStatementTree",
  "com.google.javascript.jscomp.parsing.parser.trees.ForStatementTree",
  "com.google.javascript.jscomp.parsing.parser.trees.FormalParameterListTree",
  "com.google.javascript.jscomp.parsing.parser.trees.FunctionDeclarationTree",
  "com.google.javascript.jscomp.parsing.parser.trees.GetAccessorTree",
  "com.google.javascript.jscomp.parsing.parser.trees.IdentifierExpressionTree",
  "com.google.javascript.jscomp.parsing.parser.trees.IfStatementTree",
  "com.google.javascript.jscomp.parsing.parser.trees.ImportDeclarationTree",
  "com.google.javascript.jscomp.parsing.parser.trees.ImportPathTree",
  "com.google.javascript.jscomp.parsing.parser.trees.ImportSpecifierTree",
  "com.google.javascript.jscomp.parsing.parser.trees.LabelledStatementTree",
  "com.google.javascript.jscomp.parsing.parser.trees.LiteralExpressionTree",
  "com.google.javascript.jscomp.parsing.parser.trees.MemberExpressionTree",
  "com.google.javascript.jscomp.parsing.parser.trees.MemberLookupExpressionTree",
  "com.google.javascript.jscomp.parsing.parser.trees.MissingPrimaryExpressionTree",
  "com.google.javascript.jscomp.parsing.parser.trees.MixinResolveListTree",
  "com.google.javascript.jscomp.parsing.parser.trees.MixinResolveTree",
  "com.google.javascript.jscomp.parsing.parser.trees.MixinTree",
  "com.google.javascript.jscomp.parsing.parser.trees.ModuleDefinitionTree",
  "com.google.javascript.jscomp.parsing.parser.trees.NewExpressionTree",
  "com.google.javascript.jscomp.parsing.parser.trees.NullTree",
  "com.google.javascript.jscomp.parsing.parser.trees.ObjectLiteralExpressionTree",
  "com.google.javascript.jscomp.parsing.parser.trees.ObjectPatternFieldTree",
  "com.google.javascript.jscomp.parsing.parser.trees.ObjectPatternTree",
  "com.google.javascript.jscomp.parsing.parser.trees.ParenExpressionTree",
  "com.google.javascript.jscomp.parsing.parser.trees.ParseTree",
  "com.google.javascript.jscomp.parsing.parser.trees.ParseTreeType",
  "com.google.javascript.jscomp.parsing.parser.trees.PostfixExpressionTree",
  "com.google.javascript.jscomp.parsing.parser.trees.ProgramTree",
  "com.google.javascript.jscomp.parsing.parser.trees.PropertyNameAssignmentTree",
  "com.google.javascript.jscomp.parsing.parser.trees.RequiresMemberTree",
  "com.google.javascript.jscomp.parsing.parser.trees.RestParameterTree",
  "com.google.javascript.jscomp.parsing.parser.trees.ReturnStatementTree",
  "com.google.javascript.jscomp.parsing.parser.trees.SetAccessorTree",
  "com.google.javascript.jscomp.parsing.parser.trees.SpreadExpressionTree",
  "com.google.javascript.jscomp.parsing.parser.trees.SpreadPatternElementTree",
  "com.google.javascript.jscomp.parsing.parser.trees.SuperExpressionTree",
  "com.google.javascript.jscomp.parsing.parser.trees.SwitchStatementTree",
  "com.google.javascript.jscomp.parsing.parser.trees.ThisExpressionTree",
  "com.google.javascript.jscomp.parsing.parser.trees.ThrowStatementTree",
  "com.google.javascript.jscomp.parsing.parser.trees.TraitDeclarationTree",
  "com.google.javascript.jscomp.parsing.parser.trees.TryStatementTree",
  "com.google.javascript.jscomp.parsing.parser.trees.UnaryExpressionTree",
  "com.google.javascript.jscomp.parsing.parser.trees.VariableDeclarationListTree",
  "com.google.javascript.jscomp.parsing.parser.trees.VariableDeclarationTree",
  "com.google.javascript.jscomp.parsing.parser.trees.VariableStatementTree",
  "com.google.javascript.jscomp.parsing.parser.trees.WhileStatementTree",
  "com.google.javascript.jscomp.parsing.parser.trees.WithStatementTree",
  "com.google.javascript.jscomp.parsing.parser.trees.YieldStatementTree",
  "com.google.javascript.jscomp.parsing.parser.util.ConsoleErrorReporter",
  "com.google.javascript.jscomp.parsing.parser.util.ErrorReporter",
  "com.google.javascript.jscomp.parsing.parser.util.MutedErrorReporter",
  "com.google.javascript.jscomp.parsing.parser.util.Pair",
  "com.google.javascript.jscomp.parsing.parser.util.SourcePosition",
  "com.google.javascript.jscomp.parsing.parser.util.SourceRange",
  "com.google.javascript.jscomp.parsing.parser.util.Timer",
  "com.google.javascript.jscomp.parsing.parser.util.WebErrorReporter",
  "com.google.javascript.jscomp.parsing.parser.util.format.IllegalFormatCodePointException",
  "com.google.javascript.jscomp.parsing.parser.util.format.IllegalFormatConversionException",
  "com.google.javascript.jscomp.parsing.parser.util.format.IllegalFormatFlagsException",
  "com.google.javascript.jscomp.parsing.parser.util.format.IllegalFormatPrecisionException",
  "com.google.javascript.jscomp.parsing.parser.util.format.IllegalFormatWidthException",
  "com.google.javascript.jscomp.parsing.parser.util.format.MissingFormatArgumentException",
  "com.google.javascript.jscomp.parsing.parser.util.format.MissingFormatWidthException",
  "com.google.javascript.jscomp.parsing.parser.util.format.SimpleFormat",
  "com.google.javascript.jscomp.parsing.parser.util.format.UnknownFormatConversionException",
  "com.google.javascript.jscomp.regex.CaseCanonicalize",
  "com.google.javascript.jscomp.regex.CharRanges",
  "com.google.javascript.jscomp.regex.RegExpTree",
  "com.google.javascript.jscomp.type.ChainableReverseAbstractInterpreter",
  "com.google.javascript.jscomp.type.ClosureReverseAbstractInterpreter",
  "com.google.javascript.jscomp.type.FlowScope",
  "com.google.javascript.jscomp.type.ReverseAbstractInterpreter",
  "com.google.javascript.jscomp.type.SemanticReverseAbstractInterpreter",
  "com.google.javascript.rhino.ErrorReporter",
  "com.google.javascript.rhino.IR",
  "com.google.javascript.rhino.InputId",
  "com.google.javascript.rhino.JSDocInfo",
  "com.google.javascript.rhino.JSDocInfoBuilder",
  "com.google.javascript.rhino.JSTypeExpression",
  "com.google.javascript.rhino.Node",
  "com.google.javascript.rhino.SimpleErrorReporter",
  "com.google.javascript.rhino.SourcePosition",
  "com.google.javascript.rhino.Token",
  "com.google.javascript.rhino.TokenStream",
  "com.google.javascript.rhino.jstype.AllType",
  "com.google.javascript.rhino.jstype.ArrowType",
  "com.google.javascript.rhino.jstype.BooleanLiteralSet",
  "com.google.javascript.rhino.jstype.BooleanType",
  "com.google.javascript.rhino.jstype.CanCastToVisitor",
  "com.google.javascript.rhino.jstype.EnumElementType",
  "com.google.javascript.rhino.jstype.EnumType",
  "com.google.javascript.rhino.jstype.EquivalenceMethod",
  "com.google.javascript.rhino.jstype.ErrorFunctionType",
  "com.google.javascript.rhino.jstype.FunctionBuilder",
  "com.google.javascript.rhino.jstype.FunctionParamBuilder",
  "com.google.javascript.rhino.jstype.FunctionType",
  "com.google.javascript.rhino.jstype.InstanceObjectType",
  "com.google.javascript.rhino.jstype.JSType",
  "com.google.javascript.rhino.jstype.JSTypeNative",
  "com.google.javascript.rhino.jstype.JSTypeRegistry",
  "com.google.javascript.rhino.jstype.ModificationVisitor",
  "com.google.javascript.rhino.jstype.NamedType",
  "com.google.javascript.rhino.jstype.NamespaceType",
  "com.google.javascript.rhino.jstype.NoObjectType",
  "com.google.javascript.rhino.jstype.NoResolvedType",
  "com.google.javascript.rhino.jstype.NoType",
  "com.google.javascript.rhino.jstype.NullType",
  "com.google.javascript.rhino.jstype.NumberType",
  "com.google.javascript.rhino.jstype.ObjectType",
  "com.google.javascript.rhino.jstype.Property",
  "com.google.javascript.rhino.jstype.PropertyMap",
  "com.google.javascript.rhino.jstype.PrototypeObjectType",
  "com.google.javascript.rhino.jstype.ProxyObjectType",
  "com.google.javascript.rhino.jstype.RecordType",
  "com.google.javascript.rhino.jstype.RecordTypeBuilder",
  "com.google.javascript.rhino.jstype.RelationshipVisitor",
  "com.google.javascript.rhino.jstype.SimpleReference",
  "com.google.javascript.rhino.jstype.SimpleSlot",
  "com.google.javascript.rhino.jstype.SimpleSourceFile",
  "com.google.javascript.rhino.jstype.StaticReference",
  "com.google.javascript.rhino.jstype.StaticScope",
  "com.google.javascript.rhino.jstype.StaticSlot",
  "com.google.javascript.rhino.jstype.StaticSourceFile",
  "com.google.javascript.rhino.jstype.StaticSymbolTable",
  "com.google.javascript.rhino.jstype.StringType",
  "com.google.javascript.rhino.jstype.TemplateType",
  "com.google.javascript.rhino.jstype.TemplateTypeMap",
  "com.google.javascript.rhino.jstype.TemplateTypeMapReplacer",
  "com.google.javascript.rhino.jstype.TemplatizedType",
  "com.google.javascript.rhino.jstype.TernaryValue",
  "com.google.javascript.rhino.jstype.UnionType",
  "com.google.javascript.rhino.jstype.UnionTypeBuilder",
  "com.google.javascript.rhino.jstype.UnknownType",
  "com.google.javascript.rhino.jstype.ValueType",
  "com.google.javascript.rhino.jstype.Visitor",
  "com.google.javascript.rhino.jstype.VoidType"
]
```

Reach the target with meaningful domain arguments. Do not substitute constructor exceptions, null-only inputs or empty collections for behavior assertions. No execution feedback or repair loop.

## build.xml

```
<!--
 Copyright 2009 Google Inc.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
-->
<project name="compiler" basedir="." default="jar" xmlns:artifact="antlib:org.apache.maven.artifact.ant">

  <!--
    Use -Dtest.class to change what tests are run on the command-line.
    i.e., -Dtest.class=CommandLineRunnerTest will run just that test class.
  -->
  <property name="test.class" value="*Test"/>

  <!-- Use -Dtest.method in conjunction with the one-test rule. -->
  <property name="test.method" value=""/>

  <!--
    Use -Dtest.fork to specify whether or not to fork the process.
    Some machines run better with forking turned off.
  -->
  <property name="test.fork" value="true"/>

  <!-- Force java 7 -->
  <property name="ant.build.javac.source" value="1.7" />
  <property name="ant.build.javac.target" value="1.7" />

  <!-- define other variables -->
  <property name="javac.debug" value="on" />
  <property name="src.dir" value="${basedir}/src" />
  <property name="gen.dir" value="${basedir}/gen" />
  <property name="test.dir" value="${basedir}/test" />
  <property name="externs.dir" value="${basedir}/externs" />
  <!-- To workaround Ant limitation on overriding properties set on the
       command-line, define a unique property to allow "build.dir" to
       be change without forcing the build of Rhino to the same directory.
  -->
  <property name="closure.build.dir" value="${basedir}/build" />
  <property name="build.dir" value="${closure.build.dir}" />
  <property name="buildlib.dir" value="${build.dir}/lib" />
  <property name="classes.dir" value="${build.dir}/classes" />
  <property name="testClasses.dir" value="${build.dir}/test" />
  <property name="javadoc.dir" value="${build.dir}/javadoc" />
  <property name="lib.dir" value="${basedir}/lib" />
  <property name="tools.dir" value="${basedir}/tools" />
  <property name="compiler-jarfile"
            value="${build.dir}/${ant.project.name}.jar" />
  <property name="num-fuzz-tests" value="10000"/>

  <property name="jsonml.dir" value="${basedir}/src/com/google/javascript/jscomp/jsonml" />
  <property name="jsonml-classes.dir" value = "${build.dir}/jsonml-classes" />
  <property name="jsonml-jarfile" value="${build.dir}/secure_compiler.jar" />

  <property name="webservice.dir" value="${basedir}/src/com/google/javascript/jscomp/webservice" />
  <property name="webservice-classes.dir" value = "${build.dir}/webservice-classes" />
  <property name="webservice-jarfile" value="${build.dir}/webservice.jar" />

  <!-- The following server is used to deploy releases to maven central via Sonatypes
       publishing service which runs on oss.sonatype.org.

       You will need to have an account on sonatype.org to push releases.  You can
       override these values if you want to deploy to a different repository
  -->
  <property name="maven-repository-url" value="https://oss.sonatype.org/service/local/staging/deploy/maven2/" />
  <property name="maven-repository-id" value="sonatype-nexus-staging" />

  <!-- proto compiler used to generate java classes from .proto files -->
  <property name="protoc.executable" value="protoc"/>

  <property file="build.properties" />

  <!-- maven ant tasks -->
  <path id="maven-ant-tasks.classpath" path="${tools.dir}/maven-ant-tasks-2.1.3.jar" />
  <typedef resource="org/apache/maven/artifact/ant/antlib.xml"
           uri="antlib:org.apache.maven.artifact.ant"
           classpathref="maven-ant-tasks.classpath" />

  <!-- gather release version -->
  <target name="relversion">
    <exec outputproperty="build.relVersion"
      executable="git"
      failonerror="false"
      failifexecutionfails="false"
      dir=".">
      <arg value="describe"/>
      <arg value="--tag"/>
      <arg value="--always"/>
    </exec>
  </target>

  <!-- compile rhino -->
  <target name="rhino">
    <ant antfile="build.xml"
         inheritAll="false"
         dir="lib/rhino/"
         target="jar">
      <property name="build.dir" value="${buildlib.dir}"/>
      <property name="no-e4x" value="true"/>
    </ant>
  </target>

  <target name="rhino-jarjar"
          depends="rhino"
          description="Renamespaces Rhino">
    <taskdef name="jarjar"
             classname="com.tonicsystems.jarjar.JarJarTask"
             classpath="lib/jarjar.jar"/>
    <jarjar destfile="${buildlib.dir}/rhino.jar" update="true">
      <zipfileset src="${buildlib.dir}/rhino1_7R5pre/js.jar"/>
      <rule pattern="org.mozilla.javascript.**"
            result="com.google.javascript.rhino.head.@1"/>
    </jarjar>
  </target>

  <target name="protobuf-gen">
    <fileset dir="${src.dir}" id="proto.classpath">
      <include name="**/*.proto"/>
    </fileset>
    <pathconvert property="protofiles" pathsep=" " refid="proto.classpath"/>
    <echo message="${protoc.executable} -I ${src.dir} --java_out=${gen.dir} ${protofiles}"/>
    <exec executable="${protoc.executable}" searchpath="true">
      <arg line="-I ${src.dir}"/>
      <arg line="--java_out=${gen.dir}"/>
      <arg line="${protofiles}"/>
    </exec>
  </target>

  <!-- Generate pom.xml with the proper rel build number -->
  <target name="pom" depends="relversion">
    <copy file="closure-compiler.pom" tofile="${build.dir}/pom.xml">
      <filterset>
        <filter token="build.relVersion"
             value="${build.relVersion}"/>
      </filterset>
    </copy>

    <property name="compiler-jarfile-nodeps" value="${build.dir}/closure-${ant.project.name}-${build.relVersion}.jar" />
    <property name="compiler-jarfile-javadoc" value="${build.dir}/closure-${ant.project.name}-${build.relVersion}-javadoc.jar" />
    <property name="compiler-jarfile-sources" value="${build.dir}/closure-${ant.project.name}-${build.relVersion}-sources.jar" />

    <artifact:pom id="project" file="${build.dir}/pom.xml" />
  </target>

  <target name="mvn-install"
          depends="jar-nodeps,jar-javadoc,jar-sources,pom"
          description="Install closure-compiler artifacts into the local maven repo">
    <artifact:install file="${compiler-jarfile-nodeps}">
      <pom refid="project"/>
      <attach file="${compiler-jarfile-javadoc}" classifier="javadoc"/>
      <attach file="${compiler-jarfile-sources}" classifier="sources"/>
    </artifact:install>
  </target>

  <!-- The mvn-deploy target takes the generated maven artifacts and pushes
       them to the Sonatype repository.  You will need to have a
       gpg key defined and account set up. More docs on how to do this here:

       https://docs.sonatype.org/display/Repository/Sonatype+OSS+Maven+Repository+Usage+Guide
  -->

  <target name="mvn-deploy"
    depends="mvn-install"
    description="Signs and Deploys closure-compiler artifacts to the central maven repo">

    <!-- sign and deploy the main artifact -->
    <artifact:mvn>
      <arg value="org.apache.maven.plugins:maven-gpg-plugin:1.1:sign-and-deploy-file" />
      <arg value="-Durl=${maven-repository-url}" />
      <arg value="-DrepositoryId=${maven-repository-id}" />
      <arg value="-DpomFile=${build.dir}/pom.xml" />
      <arg value="-Dfile=${compiler-jarfile-nodeps}" />
      <arg value="-Pgpg" />
    </artifact:mvn>

    <!-- sign and deploy the sources artifact -->
    <artifact:mvn>
      <arg value="org.apache.maven.plugins:maven-gpg-plugin:1.1:sign-and-deploy-file" />
      <arg value="-Durl=${maven-repository-url}" />
      <arg value="-DrepositoryId=${maven-repository-id}" />
      <arg value="-DpomFile=${build.dir}/pom.xml" />
      <arg value="-Dfile=${compiler-jarfile-sources}" />
      <arg value="-Dclassifier=sources" />
      <arg value="-Pgpg" />
    </artifact:mvn>

    <!-- sign and deploy the javadoc artifact -->
    <artifact:mvn>
      <arg value="org.apache.maven.plugins:maven-gpg-plugin:1.1:sign-and-deploy-file" />
      <arg value="-Durl=${maven-repository-url}" />
      <arg value="-DrepositoryId=${maven-repository-id}" />
      <arg value="-DpomFile=${build.dir}/pom.xml" />
      <arg value="-Dfile=${compiler-jarfile-javadoc}" />
      <arg value="-Dclassifier=javadoc" />
      <arg value="-Pgpg" />
    </artifact:mvn>
  </target>

  <!-- Sync maven dependencies listed in pom.xml -->
  <target name="mvn-deps-sync" description="sync dependencies/jars in closure-compiler.pom to lib" depends="pom">
    <artifact:dependencies filesetid="dependency.fileset"
                           pathid="dependency.classpath"
                           pomrefid="project"
                           versionsId="dependency.versions"/>
    <mkdir dir="${lib.dir}"/>
    <copy todir="${lib.dir}">
      <fileset refid="dependency.fileset" />
      <!-- This mapper strips off all leading directory information -->
      <mapper classpathref="maven-ant-tasks.classpath"
          classname="org.apache.maven.artifact.ant.VersionMapper"
          from="${dependency.versions}" to="flatten" />
    </copy>
  </target>

  <!-- set the classpath for the project              -->
  <!-- this includes the generated source class files -->
  <!-- and every jar in the /lib directory            -->

  <path id="srcclasspath.path">
    <pathelement location="${classes.dir}" />
    <fileset dir="${lib.dir}">
      <include name="args4j.jar"/>
      <include name="guava.jar"/>
      <include name="json.jar"/>
      <include name="jsr305.jar"/>
      <include name="protobuf-java.jar"/>
    </fileset>
    <fileset dir="${buildlib.dir}">
      <include name="rhino.jar"/>
    </fileset>
  </path>

  <path id="allclasspath.path">
    <pathelement location="${classes.dir}" />
    <fileset dir="${lib.dir}">
      <include name="*.jar"/>
    </fileset>
    <fileset dir="${buildlib.dir}">
      <include name="rhino.jar"/>
    </fileset>
  </path>

  <target name="clean" description="delete generated files">
    <delete dir="${build.dir}" />
  </target>

  <target name="compile"
          description="compile the source code"
          depends="rhino-jarjar,relversion">
    <mkdir dir="${classes.dir}" />
    <javac debug="true" srcdir="${gen.dir}"
           destdir="${classes.dir}"
           excludes=".svn,.git"
           >
      <classpath refid="srcclasspath.path" />
    </javac>
    <javac debug="true" srcdir="${src.dir}"
           destdir="${classes.dir}"
           excludes=".svn,.git,**/jsonml/**,**/webservice/**,**/testing/**"
           >
      <classpath refid="srcclasspath.path" />
    </javac>

    <!-- Move Messages.properties where ScriptRuntime.java expects it. -->
    <mkdir dir="${classes.dir}/rhino_ast/java/com/google/javascript/rhino/" />
    <copy file="${src.dir}/com/google/javascript/rhino/Messages.properties"
          todir="${classes.dir}/rhino_ast/java/com/google/javascript/rhino/" />

    <!-- Move ParserConfig.properties where ParserRunner.java expects it. -->
    <copy file="${src.dir}/com/google/javascript/jscomp/parsing/ParserConfig.properties"
          todir="${classes.dir}/com/google/javascript/jscomp/parsing" />

    <propertyfile
        file="${classes.dir}/com/google/javascript/jscomp/parsing/ParserConfig.properties"
        comment="Parser properties">
      <entry key="compiler.date" type="date" value="now"/>
      <entry key="compiler.version" value="${build.relVersion}"/>
    </propertyfile>

    <!-- Move runtime_type_check.js where RuntimeTypeCheck.java expects it. -->
    <mkdir dir="${classes.dir}/com/google/javascript/jscomp/js" />
    <copy todir="${classes.dir}/com/google/javascript/jscomp/js">
      <fileset dir="${src.dir}/com/google/javascript/jscomp/js" />
    </copy>
  </target>

  <target name="jar-nodeps"
          depends="compile,pom"
          description="package compiler as an executable jar">
    <zip destfile="${build.dir}/externs.zip" basedir="${externs.dir}" />
    <jar destfile="${compiler-jarfile-nodeps}" update="true">
      <fileset dir="${classes.dir}" />
      <fileset dir="${build.dir}" includes="externs.zip" />
      <zipfileset src="${build.dir}/lib/rhino.jar"/>
    </jar>
  </target>


  <target name="jar"
          depends="compile"
          description="package compiler as an executable jar">
    <zip destfile="${build.dir}/externs.zip" basedir="${externs.dir}" includes="*.js" />
    <jar destfile="${compiler-jarfile}" update="true">
      <fileset dir="${classes.dir}" />
      <fileset dir="${build.dir}" includes="externs.zip" />
      <zipfileset src="${lib.dir}/args4j.jar"/>
      <zipfileset src="${lib.dir}/guava.jar"/>
      <zipfileset src="${lib.dir}/json.jar"/>
      <zipfileset src="${lib.dir}/jsr305.jar"/>
      <zipfileset src="${lib.dir}/protobuf-java.jar"/>

      <zipfileset src="${buildlib.dir}/rhino.jar"/>

      <manifest>
        <attribute name="Main-Class"
                   value="com.google.javascript.jscomp.CommandLineRunner" />
      </manifest>
    </jar>
  </target>

  <target name="compile-tests"
          depends="compile"
          description="compile the JUnit tests">
    <mkdir dir="${testClasses.dir}" />
    <javac debug="true" srcdir="${src.dir}"
           destdir="${testClasses.dir}"
           excludes=".svn,.git"
           >
      <classpath refid="allclasspath.path" />
    </javac>
    <javac debug="true" srcdir="${test.dir}"
           destdir="${testClasses.dir}"
           excludes=".svn,.git"
           >
      <classpath refid="allclasspath.path" />
    </javac>
  </target>

  <target name="all-classes-jar"
          depends="compile,compile-tests"
          description="package the compiler and its tests into one jar">
    <jar destfile="${compiler-jarfile}" update="true">
      <fileset dir="${testClasses.dir}" />
      <zipgroupfileset dir="${lib.dir}" includes="*.jar"/>
      <zipfileset src="${buildlib.dir}/rhino.jar"/>
    </jar>
  </target>

  <target name="test"
          depends="compile-tests"
          description="Compile and execute the JUnit tests.">
    <mkdir dir="build/testoutput"/>
    <junit printsummary="on" fork="${test.fork}"
           forkmode="once" showoutput="true"
           failureproperty="junit.failure">
      <classpath refid="allclasspath.path" />
      <classpath>
        <pathelement location="${build.dir}/test" />
      </classpath>
      <batchtest todir="build/testoutput">
        <formatter type="brief" usefile="false" />
        <formatter type="xml" />
        <fileset dir="${build.dir}/test">
          <include name="**/${test.class}.class" />
        </fileset>
      </batchtest>
    </junit>
    <junitreport>
       <fileset dir="build/testoutput" includes="*.xml"/>
       <report todir="build/testoutput"/>
    </junitreport>
    <fail if="junit.failure"
          message="Unit tests failed. See build/testoutput/index.html" />
  </target>

  <target name="one-test"
          depends="compile-tests"
          description="Compile and execute one JUnit test 
                       specified with -Dtest.class and -Dtest.method.">
    <mkdir dir="build/testoutput"/>
    <junit printsummary="on" fork="${test.fork}"
           forkmode="once" showoutput="true"
           failureproperty="junit.failure">
      <classpath refid="allclasspath.path" />
      <classpath>
        <pathelement location="${build.dir}/test" />
      </classpath>
      <test todir="build/testoutput" name="${test.class}" methods="${test.method}">
        <formatter type="brief" usefile="false" />
        <formatter type="xml" />
      </test>
    </junit>
    <junitreport>
       <fileset dir="build/testoutput" includes="*.xml"/>
       <report todir="build/testoutput"/>
    </junitreport>
    <fail if="junit.failure"
          message="Unit tests failed. See build/testoutput/index.html" />
  </target>

  <target name="fuzz-test"
          depends="all-classes-jar"
          description="checks the compiler against a variety of js programs">
      <exec executable="java" failonerror="true">
        <arg value="-cp" />
        <arg value="${compiler-jarfile}" />
        <arg value="com.google.javascript.jscomp.regtests.CompileEachLineOfProgramOutput" />
        <arg value="generatejs"/>
        <arg value="--stdout"/>
        <arg value="${num-fuzz-tests}"/>
      </exec>
  </target>

  <target name="javadoc"
          description="generate Javadoc"
          depends="rhino-jarjar">
    <mkdir dir="${javadoc.dir}" />
    <javadoc
         destdir="${javadoc.dir}"
         author="false"
         protected="true"
         windowtitle="Closure Compiler"
         additionalparam=" -notimestamp ">
      <sourcepath>
        <pathelement location="${src.dir}" />
        <pathelement location="${gen.dir}" />
      </sourcepath>
      <classpath refid="allclasspath.path" />
      <link href="http://docs.oracle.com/javase/7/docs/api/" />
    </javadoc>
  </target>

  <target name="jar-javadoc" depends="javadoc">
    <jar jarfile="${compiler-jarfile-javadoc}">
      <fileset dir="${javadoc.dir}" />
    </jar>
  </target>

  <target name="jar-sources" depends="javadoc">
    <jar jarfile="${compiler-jarfile-sources}">
      <fileset dir="${src.dir}" />
      <fileset dir="${gen.dir}" />
    </jar>
  </target>

  <!-- JsonML package related targets                 -->
  <!-- set the classpath for the project              -->
  <!-- this includes the generated source class files -->
  <!-- and every jar in the /lib directory            -->
  <path id="jsonml-classpath.path">
    <pathelement location="${classes.dir}" />
    <fileset dir="${lib.dir}">
      <include name="*.jar" />
    </fileset>
    <fileset dir="${classes.dir}">
      <include name="*.class" />
    </fileset>
    <fileset dir="${buildlib.dir}">
      <include name="rhino.jar"/>
    </fileset>
  </path>

  <target name="jsonml-compile"
          description="compile the source code of classes from JsonML package"
          depends="compile">
    <mkdir dir="${jsonml-classes.dir}" />
    <javac debug="true" srcdir="${jsonml.dir}"
           destdir="${jsonml-classes.dir}"
           excludes=".svn,.git"
           >
      <classpath refid="jsonml-classpath.path" />
    </javac>
  </target>

  <target name="jsonml-jar"
          description="package the compiler and JsonML classes"
          depends="jsonml-compile, compile">
   <zip destfile="${build.dir}/externs.zip" basedir="${externs.dir}" includes="*.js" />
   <jar destfile="${jsonml-jarfile}" update="true">
     <zipgroupfileset dir="${lib.dir}" includes="*.jar"/>
     <zipfileset src="${buildlib.dir}/rhino.jar"/>
     <fileset dir="${classes.dir}" />
     <fileset dir="${jsonml-classes.dir}" />
   </jar>
  </target>

  <target name="webservice-compile"
          description="compile the source code of classes from the webservice package"
          depends="compile">
    <mkdir dir="${webservice-classes.dir}" />
    <javac debug="true" srcdir="${webservice.dir}"
           destdir="${webservice-classes.dir}"
           excludes=".svn,.git"
           >
      <classpath refid="jsonml-classpath.path" />
    </javac>
  </target>

  <target name="webservice-jar"
          description="package the compiler and Webservice classes"
          depends="webservice-compile, compile">
   <zip destfile="${build.dir}/externs.zip" basedir="${externs.dir}" includes="*.js" />
   <jar destfile="${webservice-jarfile}" update="true">
     <zipgroupfileset dir="${lib.dir}" includes="*.jar"/>
     <zipfileset src="${buildlib.dir}/rhino.jar"/>
     <fileset dir="${classes.dir}" />
     <fileset dir="${webservice-classes.dir}" />
   </jar>
  </target>
</project>

```

## src/com/google/javascript/jscomp/TypeInference.java

```
/*
 * Copyright 2008 The Closure Compiler Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.javascript.jscomp;

import static com.google.javascript.rhino.jstype.JSTypeNative.ARRAY_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.BOOLEAN_OBJECT_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.BOOLEAN_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.CHECKED_UNKNOWN_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.NULL_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.NUMBER_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.NUMBER_VALUE_OR_OBJECT_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.STRING_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.VOID_TYPE;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CodingConvention.AssertionFunctionSpec;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.graph.DiGraph.DiGraphEdge;
import com.google.javascript.jscomp.type.FlowScope;
import com.google.javascript.jscomp.type.ReverseAbstractInterpreter;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.BooleanLiteralSet;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ModificationVisitor;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.StaticSlot;
import com.google.javascript.rhino.jstype.TemplateType;
import com.google.javascript.rhino.jstype.TemplateTypeMap;
import com.google.javascript.rhino.jstype.TemplateTypeMapReplacer;
import com.google.javascript.rhino.jstype.UnionType;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Type inference within a script node or a function body, using the data-flow
 * analysis framework.
 *
 */
class TypeInference
    extends DataFlowAnalysis.BranchedForwardDataFlowAnalysis<Node, FlowScope> {

  // TODO(johnlenz): We no longer make this check, but we should.
  static final DiagnosticType FUNCTION_LITERAL_UNDEFINED_THIS =
    DiagnosticType.warning(
        "JSC_FUNCTION_LITERAL_UNDEFINED_THIS",
        "Function literal argument refers to undefined this argument");

  private final AbstractCompiler compiler;
  private final JSTypeRegistry registry;
  private final ReverseAbstractInterpreter reverseInterpreter;
  private final Scope syntacticScope;
  private final FlowScope functionScope;
  private final FlowScope bottomScope;
  private final Map<String, AssertionFunctionSpec> assertionFunctionsMap;

  // For convenience
  private final ObjectType unknownType;

  TypeInference(AbstractCompiler compiler, ControlFlowGraph<Node> cfg,
                ReverseAbstractInterpreter reverseInterpreter,
                Scope functionScope,
                Map<String, AssertionFunctionSpec> assertionFunctionsMap) {
    super(cfg, new LinkedFlowScope.FlowScopeJoinOp());
    this.compiler = compiler;
    this.registry = compiler.getTypeRegistry();
    this.reverseInterpreter = reverseInterpreter;
    this.unknownType = registry.getNativeObjectType(UNKNOWN_TYPE);

    this.syntacticScope = functionScope;
    inferArguments(functionScope);

    this.functionScope = LinkedFlowScope.createEntryLattice(functionScope);
    this.assertionFunctionsMap = assertionFunctionsMap;

    // For each local variable declared with the VAR keyword, the entry
    // type is VOID.
    Iterator<Var> varIt =
        functionScope.getDeclarativelyUnboundVarsWithoutTypes();
    while (varIt.hasNext()) {
      Var var = varIt.next();
      if (isUnflowable(var)) {
        continue;
      }

      this.functionScope.inferSlotType(
          var.getName(), getNativeType(VOID_TYPE));
    }

    this.bottomScope = LinkedFlowScope.createEntryLattice(
        Scope.createLatticeBottom(functionScope.getRootNode()));
  }

  /**
   * Infers all of a function's arguments if their types aren't declared.
   */
  private void inferArguments(Scope functionScope) {
    Node functionNode = functionScope.getRootNode();
    Node astParameters = functionNode.getFirstChild().getNext();
    Node iifeArgumentNode = null;

    if (NodeUtil.isCallOrNewTarget(functionNode)) {
      iifeArgumentNode = functionNode.getNext();
    }

    FunctionType functionType =
        JSType.toMaybeFunctionType(functionNode.getJSType());
    if (functionType != null) {
      Node parameterTypes = functionType.getParametersNode();
      if (parameterTypes != null) {
        Node parameterTypeNode = parameterTypes.getFirstChild();
        for (Node astParameter : astParameters.children()) {
          Var var = functionScope.getVar(astParameter.getString());
          Preconditions.checkNotNull(var);
          if (var.isTypeInferred() &&
              var.getType() == unknownType) {
            JSType newType = null;

            if (iifeArgumentNode != null) {
              newType = iifeArgumentNode.getJSType();
            } else if (parameterTypeNode != null) {
              newType = parameterTypeNode.getJSType();
            }

            if (newType != null) {
              var.setType(newType);
              astParameter.setJSType(newType);
            }
          }

          if (parameterTypeNode != null) {
            parameterTypeNode = parameterTypeNode.getNext();
          }
          if (iifeArgumentNode != null) {
            iifeArgumentNode = iifeArgumentNode.getNext();
          }
        }
      }
    }
  }

  @Override
  FlowScope createInitialEstimateLattice() {
    return bottomScope;
  }

  @Override
  FlowScope createEntryLattice() {
    return functionScope;
  }

  @Override
  FlowScope flowThrough(Node n, FlowScope input) {
    // If we have not walked a path from <entry> to <n>, then we don't
    // want to infer anything about this scope.
    if (input == bottomScope) {
      return input;
    }

    FlowScope output = input.createChildFlowScope();
    output = traverse(n, output);
    return output;
  }

  @Override
  @SuppressWarnings({"fallthrough", "incomplete-switch"})
  List<FlowScope> branchedFlowThrough(Node source, FlowScope input) {
    // NOTE(nicksantos): Right now, we just treat ON_EX edges like UNCOND
    // edges. If we wanted to be perfect, we'd actually JOIN all the out
    // lattices of this flow with the in lattice, and then make that the out
    // lattice for the ON_EX edge. But it's probably too expensive to be
    // worthwhile.
    FlowScope output = flowThrough(source, input);
    Node condition = null;
    FlowScope conditionFlowScope = null;
    BooleanOutcomePair conditionOutcomes = null;

    List<DiGraphEdge<Node, Branch>> branchEdges = getCfg().getOutEdges(source);
    List<FlowScope> result = Lists.newArrayListWithCapacity(branchEdges.size());
    for (DiGraphEdge<Node, Branch> branchEdge : branchEdges) {
      Branch branch = branchEdge.getValue();
      FlowScope newScope = output;

      switch (branch) {
        case ON_TRUE:
          if (NodeUtil.isForIn(source)) {
            // item is assigned a property name, so its type should be string.
            Node item = source.getFirstChild();
            Node obj = item.getNext();

            FlowScope informed = traverse(obj, output.createChildFlowScope());

            if (item.isVar()) {
              item = item.getFirstChild();
            }
            if (item.isName()) {
              JSType iterKeyType = getNativeType(STRING_TYPE);
              ObjectType objType = getJSType(obj).dereference();
              JSType objIndexType = objType == null ?
                  null : objType.getTemplateTypeMap().getTemplateType(
                      registry.getObjectIndexKey());
              if (objIndexType != null && !objIndexType.isUnknownType()) {
                JSType narrowedKeyType =
                    iterKeyType.getGreatestSubtype(objIndexType);
                if (!narrowedKeyType.isEmptyType()) {
                  iterKeyType = narrowedKeyType;
                }
              }
              redeclareSimpleVar(informed, item, iterKeyType);
            }
            newScope = informed;
            break;
          }

          // FALL THROUGH

        case ON_FALSE:
          if (condition == null) {
            condition = NodeUtil.getConditionExpression(source);
            if (condition == null && source.isCase()) {
              condition = source;

              // conditionFlowScope is cached from previous iterations
              // of the loop.
              if (conditionFlowScope == null) {
                conditionFlowScope = traverse(
                    condition.getFirstChild(), output.createChildFlowScope());
              }
            }
          }

          if (condition != null) {
            if (condition.isAnd() ||
                condition.isOr()) {
              // When handling the short-circuiting binary operators,
              // the outcome scope on true can be different than the outcome
              // scope on false.
              //
              // TODO(nicksantos): The "right" way to do this is to
              // carry the known outcome all the way through the
              // recursive traversal, so that we can construct a
              // different flow scope based on the outcome. However,
              // this would require a bunch of code and a bunch of
              // extra computation for an edge case. This seems to be
              // a "good enough" approximation.

              // conditionOutcomes is cached from previous iterations
              // of the loop.
              if (conditionOutcomes == null) {
                conditionOutcomes = condition.isAnd() ?
                    traverseAnd(condition, output.createChildFlowScope()) :
                    traverseOr(condition, output.createChildFlowScope());
              }
              newScope =
                  reverseInterpreter.getPreciserScopeKnowingConditionOutcome(
                      condition,
                      conditionOutcomes.getOutcomeFlowScope(
                          condition.getType(), branch == Branch.ON_TRUE),
                      branch == Branch.ON_TRUE);
            } else {
              // conditionFlowScope is cached from previous iterations
              // of the loop.
              if (conditionFlowScope == null) {
                conditionFlowScope =
                    traverse(condition, output.createChildFlowScope());
              }
              newScope =
                  reverseInterpreter.getPreciserScopeKnowingConditionOutcome(
                      condition, conditionFlowScope, branch == Branch.ON_TRUE);
            }
          }
          break;
      }

      result.add(newScope.optimize());
    }
    return result;
  }

  private FlowScope traverse(Node n, FlowScope scope) {
    switch (n.getType()) {
      case Token.ASSIGN:
        scope = traverseAssign(n, scope);
        break;

      case Token.NAME:
        scope = traverseName(n, scope);
        break;

      case Token.GETPROP:
        scope = traverseGetProp(n, scope);
        break;

      case Token.AND:
        scope = traverseAnd(n, scope).getJoinedFlowScope()
            .createChildFlowScope();
        break;

      case Token.OR:
        scope = traverseOr(n, scope).getJoinedFlowScope()
            .createChildFlowScope();
        break;

      case Token.HOOK:
        scope = traverseHook(n, scope);
        break;

      case Token.OBJECTLIT:
        scope = traverseObjectLiteral(n, scope);
        break;

      case Token.CALL:
        scope = traverseCall(n, scope);
        break;

      case Token.NEW:
        scope = traverseNew(n, scope);
        break;

      case Token.ASSIGN_ADD:
      case Token.ADD:
        scope = traverseAdd(n, scope);
        break;

      case Token.POS:
      case Token.NEG:
        scope = traverse(n.getFirstChild(), scope);  // Find types.
        n.setJSType(getNativeType(NUMBER_TYPE));
        break;

      case Token.ARRAYLIT:
        scope = traverseArrayLiteral(n, scope);
        break;

      case Token.THIS:
        n.setJSType(scope.getTypeOfThis());
        break;

      case Token.ASSIGN_LSH:
      case Token.ASSIGN_RSH:
      case Token.LSH:
      case Token.RSH:
      case Token.ASSIGN_URSH:
      case Token.URSH:
      case Token.ASSIGN_DIV:
      case Token.ASSIGN_MOD:
      case Token.ASSIGN_BITAND:
      case Token.ASSIGN_BITXOR:
      case Token.ASSIGN_BITOR:
      case Token.ASSIGN_MUL:
      case Token.ASSIGN_SUB:
      case Token.DIV:
      case Token.MOD:
      case Token.BITAND:
      case Token.BITXOR:
      case Token.BITOR:
      case Token.MUL:
      case Token.SUB:
      case Token.DEC:
      case Token.INC:
      case Token.BITNOT:
        scope = traverseChildren(n, scope);
        n.setJSType(getNativeType(NUMBER_TYPE));
        break;

      case Token.PARAM_LIST:
        scope = traverse(n.getFirstChild(), scope);
        n.setJSType(getJSType(n.getFirstChild()));
        break;

      case Token.COMMA:
        scope = traverseChildren(n, scope);
        n.setJSType(getJSType(n.getLastChild()));
        break;

      case Token.TYPEOF:
        scope = traverseChildren(n, scope);
        n.setJSType(getNativeType(STRING_TYPE));
        break;

      case Token.DELPROP:
      case Token.LT:
      case Token.LE:
      case Token.GT:
      case Token.GE:
      case Token.NOT:
      case Token.EQ:
      case Token.NE:
      case Token.SHEQ:
      case Token.SHNE:
      case Token.INSTANCEOF:
      case Token.IN:
        scope = traverseChildren(n, scope);
        n.setJSType(getNativeType(BOOLEAN_TYPE));
        break;

      case Token.GETELEM:
        scope = traverseGetElem(n, scope);
        break;

      case Token.EXPR_RESULT:
        scope = traverseChildren(n, scope);
        if (n.getFirstChild().isGetProp()) {
          ensurePropertyDeclared(n.getFirstChild());
        }
        break;

      case Token.SWITCH:
        scope = traverse(n.getFirstChild(), scope);
        break;

      case Token.RETURN:
        scope = traverseReturn(n, scope);
        break;

      case Token.VAR:
      case Token.THROW:
        scope = traverseChildren(n, scope);
        break;

      case Token.CATCH:
        scope = traverseCatch(n, scope);
        break;

      case Token.CAST:
        scope = traverseChildren(n, scope);
        JSDocInfo info = n.getJSDocInfo();
        if (info != null && info.hasType()) {
          n.setJSType(info.getType().evaluate(syntacticScope, registry));
        }
        break;
    }

    return scope;
  }

  /**
   * Traverse a return value.
   */
  private FlowScope traverseReturn(Node n, FlowScope scope) {
    scope = traverseChildren(n, scope);

    Node retValue = n.getFirstChild();
    if (retValue != null) {
      JSType type = functionScope.getRootNode().getJSType();
      if (type != null) {
        FunctionType fnType = type.toMaybeFunctionType();
        if (fnType != null) {
          inferPropertyTypesToMatchConstraint(
              retValue.getJSType(), fnType.getReturnType());
        }
      }
    }
    return scope;
  }

  /**
   * Any value can be thrown, so it's really impossible to determine the type
   * of a CATCH param. Treat it as the UNKNOWN type.
   */
  private FlowScope traverseCatch(Node catchNode, FlowScope scope) {
    Node name = catchNode.getFirstChild();
    JSType type;
    // If the catch expression name was declared in the catch use that type,
    // otherwise use "unknown".
    JSDocInfo info = name.getJSDocInfo();
    if (info != null && info.hasType()) {
      type = info.getType().evaluate(syntacticScope, registry);
    } else {
      type = getNativeType(JSTypeNative.UNKNOWN_TYPE);
    }
    redeclareSimpleVar(scope, name, type);
    name.setJSType(type);
    return scope;
  }

  private FlowScope traverseAssign(Node n, FlowScope scope) {
    Node left = n.getFirstChild();
    Node right = n.getLastChild();
    scope = traverseChildren(n, scope);

    JSType leftType = left.getJSType();
    JSType rightType = getJSType(right);
    n.setJSType(rightType);

    updateScopeForTypeChange(scope, left, leftType, rightType);
    return scope;
  }

  /**
   * Updates the scope according to the result of a type change, like
   * an assignment or a type cast.
   */
  private void updateScopeForTypeChange(
      FlowScope scope, Node left, JSType leftType, JSType resultType) {
    Preconditions.checkNotNull(resultType);
    switch (left.getType()) {
      case Token.NAME:
        String varName = left.getString();
        Var var = syntacticScope.getVar(varName);
        JSType varType = var == null ? null : var.getType();
        boolean isVarDeclaration = left.hasChildren()
            && varType != null && !var.isTypeInferred();

        // When looking at VAR initializers for declared VARs, we tend
        // to use the declared type over the type it's being
        // initialized to in the global scope.
        //
        // For example,
        // /** @param {number} */ var f = goog.abstractMethod;
        // it's obvious that the programmer wants you to use
        // the declared function signature, not the inferred signature.
        //
        // Or,
        // /** @type {Object.<string>} */ var x = {};
        // the one-time anonymous object on the right side
        // is as narrow as it can possibly be, but we need to make
        // sure we back-infer the <string> element constraint on
        // the left hand side, so we use the left hand side.

        boolean isVarTypeBetter = isVarDeclaration &&
            // Makes it easier to check for NPEs.
            !resultType.isNullType() && !resultType.isVoidType();

        // TODO(nicksantos): This might be a better check once we have
        // back-inference of object/array constraints.  It will probably
        // introduce more type warnings.  It uses the result type iff it's
        // strictly narrower than the declared var type.
        //
        //boolean isVarTypeBetter = isVarDeclaration &&
        //    (varType.restrictByNotNullOrUndefined().isSubtype(resultType)
        //     || !resultType.isSubtype(varType));


        if (isVarTypeBetter) {
          redeclareSimpleVar(scope, left, varType);
        } else {
          redeclareSimpleVar(scope, left, resultType);
        }
        left.setJSType(resultType);

        if (var != null && var.isTypeInferred()) {
          JSType oldType = var.getType();
          var.setType(oldType == null ?
              resultType : oldType.getLeastSupertype(resultType));
        }
        break;
      case Token.GETPROP:
        String qualifiedName = left.getQualifiedName();
        if (qualifiedName != null) {
          scope.inferQualifiedSlot(left, qualifiedName,
              leftType == null ? unknownType : leftType,
              resultType);
        }

        left.setJSType(resultType);
        ensurePropertyDefined(left, resultType);
        break;
    }
  }

  /**
   * Defines a property if the property has not been defined yet.
   */
  private void ensurePropertyDefined(Node getprop, JSType rightType) {
    String propName = getprop.getLastChild().getString();
    Node obj = getprop.getFirstChild();
    JSType nodeType = getJSType(obj);
    ObjectType objectType = ObjectType.cast(
        nodeType.restrictByNotNullOrUndefined());
    boolean propCreationInConstructor = obj.isThis() &&
        getJSType(syntacticScope.getRootNode()).isConstructor();

    if (objectType == null) {
      registry.registerPropertyOnType(propName, nodeType);
    } else {
      if (nodeType.isStruct() && !objectType.hasProperty(propName)) {
        // In general, we don't want to define a property on a struct object,
        // b/c TypeCheck will later check for improper property creation on
        // structs. There are two exceptions.
        // 1) If it's a property created inside the constructor, on the newly
        //    created instance, allow it.
        // 2) If it's a prototype property, allow it. For example:
        //    Foo.prototype.bar = baz;
        //    where Foo.prototype is a struct and the assignment happens at the
        //    top level and the constructor Foo is defined in the same file.
        boolean staticPropCreation = false;
        Node maybeAssignStm = getprop.getParent().getParent();
        if (syntacticScope.isGlobal() &&
            NodeUtil.isPrototypePropertyDeclaration(maybeAssignStm)) {
          String propCreationFilename = maybeAssignStm.getSourceFileName();
          Node ctor = objectType.getOwnerFunction().getSource();
          if (ctor != null &&
              ctor.getSourceFileName().equals(propCreationFilename)) {
            staticPropCreation = true;
          }
        }
        if (!propCreationInConstructor && !staticPropCreation) {
          return; // Early return to avoid creating the property below.
        }
      }

      if (ensurePropertyDeclaredHelper(getprop, objectType)) {
        return;
      }

      if (!objectType.isPropertyTypeDeclared(propName)) {
        // We do not want a "stray" assign to define an inferred property
        // for every object of this type in the program. So we use a heuristic
        // approach to determine whether to infer the property.
        //
        // 1) If the property is already defined, join it with the previously
        //    inferred type.
        // 2) If this isn't an instance object, define it.
        // 3) If the property of an object is being assigned in the constructor,
        //    define it.
        // 4) If this is a stub, define it.
        // 5) Otherwise, do not define the type, but declare it in the registry
        //    so that we can use it for missing property checks.
        if (objectType.hasProperty(propName) || !objectType.isInstanceType()) {
          if ("prototype".equals(propName)) {
            objectType.defineDeclaredProperty(propName, rightType, getprop);
          } else {
            objectType.defineInferredProperty(propName, rightType, getprop);
          }
        } else if (propCreationInConstructor) {
          objectType.defineInferredProperty(propName, rightType, getprop);
        } else {
          registry.registerPropertyOnType(propName, objectType);
        }
      }
    }
  }

  /**
   * Defines a declared property if it has not been defined yet.
   *
   * This handles the case where a property is declared on an object where
   * the object type is inferred, and so the object type will not
   * be known in {@code TypedScopeCreator}.
   */
  private void ensurePropertyDeclared(Node getprop) {
    ObjectType ownerType = ObjectType.cast(
        getJSType(getprop.getFirstChild()).restrictByNotNullOrUndefined());
    if (ownerType != null) {
      ensurePropertyDeclaredHelper(getprop, ownerType);
    }
  }

  /**
   * Declares a property on its owner, if necessary.
   * @return True if a property was declared.
   */
  private boolean ensurePropertyDeclaredHelper(
      Node getprop, ObjectType objectType) {
    String propName = getprop.getLastChild().getString();
    String qName = getprop.getQualifiedName();
    if (qName != null) {
      Var var = syntacticScope.getVar(qName);
      if (var != null && !var.isTypeInferred()) {
        // Handle normal declarations that could not be addressed earlier.
        if (propName.equals("prototype") ||
        // Handle prototype declarations that could not be addressed earlier.
            (!objectType.hasOwnProperty(propName) &&
             (!objectType.isInstanceType() ||
                 (var.isExtern() && !objectType.isNativeObjectType())))) {
          return objectType.defineDeclaredProperty(
              propName, var.getType(), getprop);
        }
      }
    }
    return false;
  }

  private FlowScope traverseName(Node n, FlowScope scope) {
    String varName = n.getString();
    Node value = n.getFirstChild();
    JSType type = n.getJSType();
    if (value != null) {
      scope = traverse(value, scope);
      updateScopeForTypeChange(scope, n, n.getJSType() /* could be null */,
          getJSType(value));
      return scope;
    } else {
      StaticSlot<JSType> var = scope.getSlot(varName);
      if (var != null) {
        // There are two situations where we don't want to use type information
        // from the scope, even if we have it.

        // 1) The var is escaped and assigned in an inner scope, e.g.,
        // function f() { var x = 3; function g() { x = null } (x); }
        boolean isInferred = var.isTypeInferred();
        boolean unflowable = isInferred &&
            isUnflowable(syntacticScope.getVar(varName));

        // 2) We're reading type information from another scope for an
        // inferred variable. That variable is assigned more than once,
        // and we can't know which type we're getting.
        //
        // var t = null; function f() { (t); } doStuff(); t = {};
        //
        // Notice that this heuristic isn't perfect. For example, you might
        // have:
        //
        // function f() { (t); } f(); var t = 3;
        //
        // In this case, we would infer the first reference to t as
        // type {number}, even though it's undefined.
        boolean nonLocalInferredSlot = false;
        if (isInferred && syntacticScope.isLocal()) {
          Var maybeOuterVar = syntacticScope.getParent().getVar(varName);
          if (var == maybeOuterVar &&
              !maybeOuterVar.isMarkedAssignedExactlyOnce()) {
            nonLocalInferredSlot = true;
          }
        }

        if (!unflowable && !nonLocalInferredSlot) {
          type = var.getType();
          if (type == null) {
            type = unknownType;
          }
        }
      }
    }
    n.setJSType(type);
    return scope;
  }

  /** Traverse each element of the array. */
  private FlowScope traverseArrayLiteral(Node n, FlowScope scope) {
    scope = traverseChildren(n, scope);
    n.setJSType(getNativeType(ARRAY_TYPE));
    return scope;
  }

  private FlowScope traverseObjectLiteral(Node n, FlowScope scope) {
    JSType type = n.getJSType();
    Preconditions.checkNotNull(type);

    for (Node name = n.getFirstChild(); name != null; name = name.getNext()) {
      scope = traverse(name.getFirstChild(), scope);
    }

    // Object literals can be reflected on other types.
    // See CodingConvention#getObjectLiteralCast and goog.reflect.object
    // Ignore these types of literals.
    ObjectType objectType = ObjectType.cast(type);
    if (objectType == null
        || n.getBooleanProp(Node.REFLECTED_OBJECT)
        || objectType.isEnumType()) {
      return scope;
    }

    String qObjName = NodeUtil.getBestLValueName(
        NodeUtil.getBestLValue(n));
    for (Node name = n.getFirstChild(); name != null;
         name = name.getNext()) {
      String memberName = NodeUtil.getObjectLitKeyName(name);
      if (memberName != null) {
        JSType rawValueType =  name.getFirstChild().getJSType();
        JSType valueType = NodeUtil.getObjectLitKeyTypeFromValueType(
            name, rawValueType);
        if (valueType == null) {
          valueType = unknownType;
        }
        objectType.defineInferredProperty(memberName, valueType, name);

        // Do normal flow inference if this is a direct property assignment.
        if (qObjName != null && name.isStringKey()) {
          String qKeyName = qObjName + "." + memberName;
          Var var = syntacticScope.getVar(qKeyName);
          JSType oldType = var == null ? null : var.getType();
          if (var != null && var.isTypeInferred()) {
            var.setType(oldType == null ?
                valueType : oldType.getLeastSupertype(oldType));
          }

          scope.inferQualifiedSlot(name, qKeyName,
              oldType == null ? unknownType : oldType,
              valueType);
        }
      } else {
        n.setJSType(unknownType);
      }
    }
    return scope;
  }

  private FlowScope traverseAdd(Node n, FlowScope scope) {
    Node left = n.getFirstChild();
    Node right = left.getNext();
    scope = traverseChildren(n, scope);

    JSType leftType = left.getJSType();
    JSType rightType = right.getJSType();

    JSType type = unknownType;
    if (leftType != null && rightType != null) {
      boolean leftIsUnknown = leftType.isUnknownType();
      boolean rightIsUnknown = rightType.isUnknownType();
      if (leftIsUnknown && rightIsUnknown) {
        type = unknownType;
      } else if ((!leftIsUnknown && leftType.isString()) ||
                 (!rightIsUnknown && rightType.isString())) {
        type = getNativeType(STRING_TYPE);
      } else if (leftIsUnknown || rightIsUnknown) {
        type = unknownType;
      } else if (isAddedAsNumber(leftType) && isAddedAsNumber(rightType)) {
        type = getNativeType(NUMBER_TYPE);
      } else {
        type = registry.createUnionType(STRING_TYPE, NUMBER_TYPE);
      }
    }
    n.setJSType(type);

    if (n.isAssignAdd()) {
      updateScopeForTypeChange(scope, left, leftType, type);
    }

    return scope;
  }

  private boolean isAddedAsNumber(JSType type) {
    return type.isSubtype(registry.createUnionType(VOID_TYPE, NULL_TYPE,
        NUMBER_VALUE_OR_OBJECT_TYPE, BOOLEAN_TYPE, BOOLEAN_OBJECT_TYPE));
  }

  private FlowScope traverseHook(Node n, FlowScope scope) {
    Node condition = n.getFirstChild();
    Node trueNode = condition.getNext();
    Node falseNode = n.getLastChild();

    // verify the condition
    scope = traverse(condition, scope);

    // reverse abstract interpret the condition to produce two new scopes
    FlowScope trueScope = reverseInterpreter.
        getPreciserScopeKnowingConditionOutcome(
            condition, scope, true);
    FlowScope falseScope = reverseInterpreter.
        getPreciserScopeKnowingConditionOutcome(
            condition, scope, false);

    // traverse the true node with the trueScope
    traverse(trueNode, trueScope.createChildFlowScope());

    // traverse the false node with the falseScope
    traverse(falseNode, falseScope.createChildFlowScope());

    // meet true and false nodes' types and assign
    JSType trueType = trueNode.getJSType();
    JSType falseType = falseNode.getJSType();
    if (trueType != null && falseType != null) {
      n.setJSType(trueType.getLeastSupertype(falseType));
    } else {
      n.setJSType(null);
    }

    return scope.createChildFlowScope();
  }

  private FlowScope traverseCall(Node n, FlowScope scope) {
    scope = traverseChildren(n, scope);

    Node left = n.getFirstChild();
    JSType functionType = getJSType(left).restrictByNotNullOrUndefined();
    if (functionType.isFunctionType()) {
      FunctionType fnType = functionType.toMaybeFunctionType();
      n.setJSType(fnType.getReturnType());
      backwardsInferenceFromCallSite(n, fnType);
    } else if (functionType.isEquivalentTo(
        getNativeType(CHECKED_UNKNOWN_TYPE))) {
      n.setJSType(getNativeType(CHECKED_UNKNOWN_TYPE));
    }

    scope = tightenTypesAfterAssertions(scope, n);
    return scope;
  }

  private FlowScope tightenTypesAfterAssertions(FlowScope scope,
      Node callNode) {
    Node left = callNode.getFirstChild();
    Node firstParam = left.getNext();
    AssertionFunctionSpec assertionFunctionSpec =
        assertionFunctionsMap.get(left.getQualifiedName());
    if (assertionFunctionSpec == null || firstParam == null) {
      return scope;
    }
    Node assertedNode = assertionFunctionSpec.getAssertedParam(firstParam);
    if (assertedNode == null) {
      return scope;
    }
    JSType assertedType = assertionFunctionSpec.getAssertedType(
        callNode, registry);
    String assertedNodeName = assertedNode.getQualifiedName();

    JSType narrowed;
    // Handle assertions that enforce expressions evaluate to true.
    if (assertedType == null) {
      // Handle arbitrary expressions within the assert.
      scope = reverseInterpreter.getPreciserScopeKnowingConditionOutcome(
          assertedNode, scope, true);
      // Build the result of the assertExpression
      narrowed = getJSType(assertedNode).restrictByNotNullOrUndefined();
    } else {
      // Handle assertions that enforce expressions are of a certain type.
      JSType type = getJSType(assertedNode);
      narrowed = type.getGreatestSubtype(assertedType);
      if (assertedNodeName != null && type.differsFrom(narrowed)) {
        scope = narrowScope(scope, assertedNode, narrowed);
      }
    }

    callNode.setJSType(narrowed);
    return scope;
  }

  private FlowScope narrowScope(FlowScope scope, Node node, JSType narrowed) {
    if (node.isThis()) {
      // "this" references don't need to be modeled in the control flow graph.
      return scope;
    }

    scope = scope.createChildFlowScope();
    if (node.isGetProp()) {
      scope.inferQualifiedSlot(
          node, node.getQualifiedName(), getJSType(node), narrowed);
    } else {
      redeclareSimpleVar(scope, node, narrowed);
    }
    return scope;
  }

  /**
   * We only do forward type inference. We do not do full backwards
   * type inference.
   *
   * In other words, if we have,
   * <code>
   * var x = f();
   * g(x);
   * </code>
   * a forward type-inference engine would try to figure out the type
   * of "x" from the return type of "f". A backwards type-inference engine
   * would try to figure out the type of "x" from the parameter type of "g".
   *
   * However, there are a few special syntactic forms where we do some
   * some half-assed backwards type-inference, because programmers
   * expect it in this day and age. To take an example from Java,
   * <code>
   * List<String> x = Lists.newArrayList();
   * </code>
   * The Java compiler will be able to infer the generic type of the List
   * returned by newArrayList().
   *
   * In much the same way, we do some special-case backwards inference for
   * JS. Those cases are enumerated here.
   */
  private void backwardsInferenceFromCallSite(Node n, FunctionType fnType) {
    boolean updatedFnType = inferTemplatedTypesForCall(n, fnType);
    if (updatedFnType) {
      fnType = n.getFirstChild().getJSType().toMaybeFunctionType();
    }
    updateTypeOfParameters(n, fnType);
    updateBind(n);
  }

  /**
   * When "bind" is called on a function, we infer the type of the returned
   * "bound" function by looking at the number of parameters in the call site.
   */
  private void updateBind(Node n) {
    CodingConvention.Bind bind =
        compiler.getCodingConvention().describeFunctionBind(n, true);
    if (bind == null) {
      return;
    }

    FunctionType callTargetFn = getJSType(bind.target)
        .restrictByNotNullOrUndefined().toMaybeFunctionType();
    if (callTargetFn == null) {
      return;
    }

    n.setJSType(
        callTargetFn.getBindReturnType(
            // getBindReturnType expects the 'this' argument to be included.
            bind.getBoundParameterCount() + 1));
  }

  /**
   * For functions with function parameters, type inference will set the type of
   * a function literal argument from the function parameter type.
   */
  private void updateTypeOfParameters(Node n, FunctionType fnType) {
    int i = 0;
    int childCount = n.getChildCount();
    for (Node iParameter : fnType.getParameters()) {
      if (i + 1 >= childCount) {
        // TypeCheck#visitParametersList will warn so we bail.
        return;
      }

      JSType iParameterType = getJSType(iParameter);
      Node iArgument = n.getChildAtIndex(i + 1);
      JSType iArgumentType = getJSType(iArgument);
      inferPropertyTypesToMatchConstraint(iArgumentType, iParameterType);

      // TODO(johnlenz): Filter out non-function types
      // (such as null and undefined) as
      // we only care about FUNCTION subtypes here.
      JSType restrictedParameter = iParameterType
          .restrictByNotNullOrUndefined()
          .toMaybeFunctionType();
      if (restrictedParameter != null) {
        if (iArgument.isFunction() &&
            iArgumentType.isFunctionType() &&
            iArgument.getJSDocInfo() == null) {
          iArgument.setJSType(restrictedParameter);
        }
      }
      i++;
    }
  }

  private Map<TemplateType, JSType> inferTemplateTypesFromParameters(
      FunctionType fnType, Node call) {
    if (fnType.getTemplateTypeMap().getTemplateKeys().isEmpty()) {
      return Collections.emptyMap();
    }

    Map<TemplateType, JSType> resolvedTypes = Maps.newIdentityHashMap();
    Set<JSType> seenTypes = Sets.newIdentityHashSet();

    Node callTarget = call.getFirstChild();
    if (NodeUtil.isGet(callTarget)) {
      Node obj = callTarget.getFirstChild();
      maybeResolveTemplatedType(
          fnType.getTypeOfThis(),
          getJSType(obj),
          resolvedTypes,
          seenTypes);
    }

    if (call.hasMoreThanOneChild()) {
      maybeResolveTemplateTypeFromNodes(
          fnType.getParameters(),
          call.getChildAtIndex(1).siblings(),
          resolvedTypes,
          seenTypes);
    }
    return resolvedTypes;
  }

  private void maybeResolveTemplatedType(
      JSType paramType,
      JSType argType,
      Map<TemplateType, JSType> resolvedTypes, Set<JSType> seenTypes) {
    if (paramType.isTemplateType()) {
      // @param {T}
      resolvedTemplateType(
          resolvedTypes, paramType.toMaybeTemplateType(), argType);
    } else if (paramType.isUnionType()) {
      // @param {Array.<T>|NodeList|Arguments|{length:number}}
      UnionType unionType = paramType.toMaybeUnionType();
      for (JSType alernative : unionType.getAlternates()) {
        maybeResolveTemplatedType(alernative, argType, resolvedTypes, seenTypes);
      }
    } else if (paramType.isFunctionType()) {
      FunctionType paramFunctionType = paramType.toMaybeFunctionType();
      FunctionType argFunctionType = argType
          .restrictByNotNullOrUndefined()
          .collapseUnion()
          .toMaybeFunctionType();
      if (argFunctionType != null && argFunctionType.isSubtype(paramType)) {
        // infer from return type of the function type
        maybeResolveTemplatedType(
            paramFunctionType.getTypeOfThis(),
            argFunctionType.getTypeOfThis(), resolvedTypes, seenTypes);
        // infer from return type of the function type
        maybeResolveTemplatedType(
            paramFunctionType.getReturnType(),
            argFunctionType.getReturnType(), resolvedTypes, seenTypes);
        // infer from parameter types of the function type
        maybeResolveTemplateTypeFromNodes(
            paramFunctionType.getParameters(),
            argFunctionType.getParameters(), resolvedTypes, seenTypes);
      }
    } else if (paramType.isRecordType() && !paramType.isNominalType()) {
      // @param {{foo:T}}
      if(!seenTypes.contains(paramType)) {
        seenTypes.add(paramType);
        ObjectType paramRecordType = paramType.toObjectType();
        ObjectType argObjectType = argType.restrictByNotNullOrUndefined()
            .toObjectType();
        if (argObjectType != null
            && !argObjectType.isUnknownType()
            && !argObjectType.isEmptyType()) {
          Set<String> names = paramRecordType.getPropertyNames();
          for (String name : names) {
            if (paramRecordType.hasOwnProperty(name)
                && argObjectType.hasProperty(name)) {
              maybeResolveTemplatedType(
                  paramRecordType.getPropertyType(name),
                  argObjectType.getPropertyType(name),
                  resolvedTypes,
                  seenTypes);
            }
          }
        }
        seenTypes.remove(paramType);
      }
    } else if (paramType.isTemplatizedType()) {
      // @param {Array.<T>}
      ObjectType referencedParamType = paramType
          .toMaybeTemplatizedType()
          .getReferencedType();
      JSType argObjectType = argType
          .restrictByNotNullOrUndefined()
          .collapseUnion();

      if (argObjectType.isSubtype(referencedParamType)) {
        // If the argument type is a subtype of the parameter type, resolve any
        // template types amongst their templatized types.
        TemplateTypeMap paramTypeMap = paramType.getTemplateTypeMap();
        TemplateTypeMap argTypeMap = argObjectType.getTemplateTypeMap();
        for (TemplateType key : paramTypeMap.getTemplateKeys()) {
          maybeResolveTemplatedType(
              paramTypeMap.getTemplateType(key),
              argTypeMap.getTemplateType(key),
              resolvedTypes, seenTypes);
        }
      }
    }
  }

  private void maybeResolveTemplateTypeFromNodes(
      Iterable<Node> declParams,
      Iterable<Node> callParams,
      Map<TemplateType, JSType> resolvedTypes, Set<JSType> seenTypes) {
    maybeResolveTemplateTypeFromNodes(
        declParams.iterator(), callParams.iterator(), resolvedTypes, seenTypes);
  }

  private void maybeResolveTemplateTypeFromNodes(
      Iterator<Node> declParams,
      Iterator<Node> callParams,
      Map<TemplateType, JSType> resolvedTypes,
      Set<JSType> seenTypes) {
    while (declParams.hasNext() && callParams.hasNext()) {
      Node declParam = declParams.next();
      maybeResolveTemplatedType(
          getJSType(declParam),
          getJSType(callParams.next()),
          resolvedTypes, seenTypes);
      if (declParam.isVarArgs()) {
        while (callParams.hasNext()) {
          maybeResolveTemplatedType(
              getJSType(declParam),
              getJSType(callParams.next()),
              resolvedTypes, seenTypes);
        }
      }
    }
  }

  private static void resolvedTemplateType(
      Map<TemplateType, JSType> map, TemplateType template, JSType resolved) {
    JSType previous = map.get(template);
    if (!resolved.isUnknownType()) {
      if (previous == null) {
        map.put(template, resolved);
      } else {
        JSType join = previous.getLeastSupertype(resolved);
        map.put(template, join);
      }
    }
  }

  private static class TemplateTypeReplacer extends ModificationVisitor {
    private final Map<TemplateType, JSType> replacements;
    private final JSTypeRegistry registry;
    boolean madeChanges = false;

    TemplateTypeReplacer(
        JSTypeRegistry registry, Map<TemplateType, JSType> replacements) {
      super(registry, true);
      this.registry = registry;
      this.replacements = replacements;
    }

    @Override
    public JSType caseTemplateType(TemplateType type) {
      madeChanges = true;
      JSType replacement = replacements.get(type);
      return replacement != null ?
          replacement : registry.getNativeType(UNKNOWN_TYPE);
    }
  }

  /**
   * For functions with function(this: T, ...) and T as parameters, type
   * inference will set the type of this on a function literal argument to the
   * the actual type of T.
   */
  private boolean inferTemplatedTypesForCall(
      Node n, FunctionType fnType) {
    final ImmutableList<TemplateType> keys = fnType.getTemplateTypeMap()
        .getTemplateKeys();
    if (keys.isEmpty()) {
      return false;
    }

    // Try to infer the template types
    Map<TemplateType, JSType> inferred = Maps.filterKeys(
        inferTemplateTypesFromParameters(fnType, n),
        new Predicate<TemplateType>() {

          @Override
          public boolean apply(TemplateType key) {
            return keys.contains(key);
          }}
        );

    // Replace all template types. If we couldn't find a replacement, we
    // replace it with UNKNOWN.
    TemplateTypeReplacer replacer = new TemplateTypeReplacer(
        registry, inferred);
    Node callTarget = n.getFirstChild();

    FunctionType replacementFnType = fnType.visit(replacer)
        .toMaybeFunctionType();
    Preconditions.checkNotNull(replacementFnType);

    callTarget.setJSType(replacementFnType);
    n.setJSType(replacementFnType.getReturnType());

    return replacer.madeChanges;
  }

  private FlowScope traverseNew(Node n, FlowScope scope) {
    scope = traverseChildren(n, scope);

    Node constructor = n.getFirstChild();
    JSType constructorType = constructor.getJSType();
    JSType type = null;
    if (constructorType != null) {
      constructorType = constructorType.restrictByNotNullOrUndefined();
      if (constructorType.isUnknownType()) {
        type = unknownType;
      } else {
        FunctionType ct = constructorType.toMaybeFunctionType();
        if (ct == null && constructorType instanceof FunctionType) {
          // If constructorType is a NoObjectType, then toMaybeFunctionType will
          // return null. But NoObjectType implements the FunctionType
          // interface, precisely because it can validly construct objects.
          ct = (FunctionType) constructorType;
        }
        if (ct != null && ct.isConstructor()) {
          backwardsInferenceFromCallSite(n, ct);

          // If necessary, create a TemplatizedType wrapper around the instance
          // type, based on the types of the constructor parameters.
          ObjectType instanceType = ct.getInstanceType();
          Map<TemplateType, JSType> inferredTypes =
              inferTemplateTypesFromParameters(ct, n);
          if (inferredTypes.isEmpty()) {
            type = instanceType;
          } else {
            type = registry.createTemplatizedType(instanceType, inferredTypes);
          }
        }
      }
    }
    n.setJSType(type);
    return scope;
  }

  private BooleanOutcomePair traverseAnd(Node n, FlowScope scope) {
    return traverseShortCircuitingBinOp(n, scope, true);
  }

  private FlowScope traverseChildren(Node n, FlowScope scope) {
    for (Node el = n.getFirstChild(); el != null; el = el.getNext()) {
      scope = traverse(el, scope);
    }
    return scope;
  }

  private FlowScope traverseGetElem(Node n, FlowScope scope) {
    scope = traverseChildren(n, scope);
    JSType type = getJSType(n.getFirstChild()).restrictByNotNullOrUndefined();
    TemplateTypeMap typeMap = type.getTemplateTypeMap();
    if (typeMap.hasTemplateType(registry.getObjectElementKey())) {
      n.setJSType(typeMap.getTemplateType(registry.getObjectElementKey()));
    }
    return dereferencePointer(n.getFirstChild(), scope);
  }

  private FlowScope traverseGetProp(Node n, FlowScope scope) {
    Node objNode = n.getFirstChild();
    Node property = n.getLastChild();
    scope = traverseChildren(n, scope);

    n.setJSType(
        getPropertyType(
            objNode.getJSType(), property.getString(), n, scope));
    return dereferencePointer(n.getFirstChild(), scope);
  }

  /**
   * Suppose X is an object with inferred properties.
   * Suppose also that X is used in a way where it would only type-check
   * correctly if some of those properties are widened.
   * Then we should be polite and automatically widen X's properties for him.
   *
   * For a concrete example, consider:
   * param x {{prop: (number|undefined)}}
   * function f(x) {}
   * f({});
   *
   * If we give the anonymous object an inferred property of (number|undefined),
   * then this code will type-check appropriately.
   */
  private static void inferPropertyTypesToMatchConstraint(
      JSType type, JSType constraint) {
    if (type == null || constraint == null) {
      return;
    }

    type.matchConstraint(constraint);
  }

  /**
   * If we access a property of a symbol, then that symbol is not
   * null or undefined.
   */
  private FlowScope dereferencePointer(Node n, FlowScope scope) {
    if (n.isQualifiedName()) {
      JSType type = getJSType(n);
      JSType narrowed = type.restrictByNotNullOrUndefined();
      if (type != narrowed) {
        scope = narrowScope(scope, n, narrowed);
      }
    }
    return scope;
  }

  private JSType getPropertyType(JSType objType, String propName,
      Node n, FlowScope scope) {
    // We often have a couple of different types to choose from for the
    // property. Ordered by accuracy, we have
    // 1) A locally inferred qualified name (which is in the FlowScope)
    // 2) A globally declared qualified name (which is in the FlowScope)
    // 3) A property on the owner type (which is on objType)
    // 4) A name in the type registry (as a last resort)
    JSType propertyType = null;
    boolean isLocallyInferred = false;

    // Scopes sometimes contain inferred type info about qualified names.
    String qualifiedName = n.getQualifiedName();
    StaticSlot<JSType> var = scope.getSlot(qualifiedName);
    if (var != null) {
      JSType varType = var.getType();
      if (varType != null) {
        boolean isDeclared = !var.isTypeInferred();
        isLocallyInferred = (var != syntacticScope.getSlot(qualifiedName));
        if (isDeclared || isLocallyInferred) {
          propertyType = varType;
        }
      }
    }

    if (propertyType == null && objType != null) {
      JSType foundType = objType.findPropertyType(propName);
      if (foundType != null) {
        propertyType = foundType;
      }
    }

    if (propertyType != null && objType != null) {
      JSType restrictedObjType = objType.restrictByNotNullOrUndefined();
      if (!restrictedObjType.getTemplateTypeMap().isEmpty()
          && propertyType.hasAnyTemplateTypes()) {
        TemplateTypeMap typeMap = restrictedObjType.getTemplateTypeMap();
        TemplateTypeMapReplacer replacer = new TemplateTypeMapReplacer(
            registry, typeMap);
        propertyType = propertyType.visit(replacer);
      }
    }

    if ((propertyType == null || propertyType.isUnknownType())
        && qualifiedName != null) {
      // If we find this node in the registry, then we can infer its type.
      ObjectType regType = ObjectType.cast(registry.getType(qualifiedName));
      if (regType != null) {
        propertyType = regType.getConstructor();
      }
    }

    if (propertyType == null) {
      return unknownType;
    } else if (propertyType.isEquivalentTo(unknownType) && isLocallyInferred) {
      // If the type has been checked in this scope,
      // then use CHECKED_UNKNOWN_TYPE instead to indicate that.
      return getNativeType(CHECKED_UNKNOWN_TYPE);
    } else {
      return propertyType;
    }
  }

  private BooleanOutcomePair traverseOr(Node n, FlowScope scope) {
    return traverseShortCircuitingBinOp(n, scope, false);
  }

  private BooleanOutcomePair traverseShortCircuitingBinOp(
      Node n, FlowScope scope, boolean condition) {
    Node left = n.getFirstChild();
    Node right = n.getLastChild();

    // type the left node
    BooleanOutcomePair leftLiterals =
        traverseWithinShortCircuitingBinOp(left,
            scope.createChildFlowScope());
    JSType leftType = left.getJSType();

    // reverse abstract interpret the left node to produce the correct
    // scope in which to verify the right node
    FlowScope rightScope = reverseInterpreter.
        getPreciserScopeKnowingConditionOutcome(
            left, leftLiterals.getOutcomeFlowScope(left.getType(), condition),
            condition);

    // type the right node
    BooleanOutcomePair rightLiterals =
        traverseWithinShortCircuitingBinOp(
            right, rightScope.createChildFlowScope());
    JSType rightType = right.getJSType();

    JSType type;
    BooleanOutcomePair literals;
    if (leftType != null && rightType != null) {
      leftType = leftType.getRestrictedTypeGivenToBooleanOutcome(!condition);
      if (leftLiterals.toBooleanOutcomes ==
          BooleanLiteralSet.get(!condition)) {
        // Use the restricted left type, since the right side never gets
        // evaluated.
        type = leftType;
        literals = leftLiterals;
      } else {
        // Use the join of the restricted left type knowing the outcome of the
        // ToBoolean predicate and of the right type.
        type = leftType.getLeastSupertype(rightType);
        literals =
            getBooleanOutcomePair(leftLiterals, rightLiterals, condition);
      }

      // Exclude the boolean type if the literal set is empty because a boolean
      // can never actually be returned.
      if (literals.booleanValues == BooleanLiteralSet.EMPTY &&
          getNativeType(BOOLEAN_TYPE).isSubtype(type)) {
        // Exclusion only make sense for a union type.
        if (type.isUnionType()) {
          type = type.toMaybeUnionType().getRestrictedUnion(
              getNativeType(BOOLEAN_TYPE));
        }
      }
    } else {
      type = null;
      literals = new BooleanOutcomePair(
          BooleanLiteralSet.BOTH, BooleanLiteralSet.BOTH,
          leftLiterals.getJoinedFlowScope(),
          rightLiterals.getJoinedFlowScope());
    }
    n.setJSType(type);

    return literals;
  }

  private BooleanOutcomePair traverseWithinShortCircuitingBinOp(Node n,
      FlowScope scope) {
    switch (n.getType()) {
      case Token.AND:
        return traverseAnd(n, scope);

      case Token.OR:
        return traverseOr(n, scope);

      default:
        scope = traverse(n, scope);
        return newBooleanOutcomePair(n.getJSType(), scope);
    }
  }

  /**
   * Infers the boolean outcome pair that can be taken by a
   * short-circuiting binary operation ({@code &&} or {@code ||}).
   * @see #getBooleanOutcomes(BooleanLiteralSet, BooleanLiteralSet, boolean)
   */
  BooleanOutcomePair getBooleanOutcomePair(BooleanOutcomePair left,
      BooleanOutcomePair right, boolean condition) {
    return new BooleanOutcomePair(
        getBooleanOutcomes(left.toBooleanOutcomes, right.toBooleanOutcomes,
                           condition),
        getBooleanOutcomes(left.booleanValues, right.booleanValues, condition),
        left.getJoinedFlowScope(), right.getJoinedFlowScope());
  }

  /**
   * Infers the boolean literal set that can be taken by a
   * short-circuiting binary operation ({@code &&} or {@code ||}).
   * @param left the set of possible {@code ToBoolean} predicate results for
   *    the expression on the left side of the operator
   * @param right the set of possible {@code ToBoolean} predicate results for
   *    the expression on the right side of the operator
   * @param condition the left side {@code ToBoolean} predicate result that
   *    causes the right side to get evaluated (i.e. not short-circuited)
   * @return a set of possible {@code ToBoolean} predicate results for the
   *    entire expression
   */
  static BooleanLiteralSet getBooleanOutcomes(BooleanLiteralSet left,
      BooleanLiteralSet right, boolean condition) {
    return right.union(left.intersection(BooleanLiteralSet.get(!condition)));
  }

  /**
   * When traversing short-circuiting binary operations, we need to keep track
   * of two sets of boolean literals:
   * 1. {@code toBooleanOutcomes}: boolean literals as converted from any types,
   * 2. {@code booleanValues}: boolean literals from just boolean types.
   */
  private final class BooleanOutcomePair {
    final BooleanLiteralSet toBooleanOutcomes;
    final BooleanLiteralSet booleanValues;

    // The scope if only half of the expression executed, when applicable.
    final FlowScope leftScope;

    // The scope when the whole expression executed.
    final FlowScope rightScope;

    // The scope when we don't know how much of the expression is executed.
    FlowScope joinedScope = null;

    BooleanOutcomePair(
        BooleanLiteralSet toBooleanOutcomes, BooleanLiteralSet booleanValues,
        FlowScope leftScope, FlowScope rightScope) {
      this.toBooleanOutcomes = toBooleanOutcomes;
      this.booleanValues = booleanValues;
      this.leftScope = leftScope;
      this.rightScope = rightScope;
    }

    /**
     * Gets the safe estimated scope without knowing if all of the
     * subexpressions will be evaluated.
     */
    FlowScope getJoinedFlowScope() {
      if (joinedScope == null) {
        if (leftScope == rightScope) {
          joinedScope = rightScope;
        } else {
          joinedScope = join(leftScope, rightScope);
        }
      }
      return joinedScope;
    }

    /**
     * Gets the outcome scope if we do know the outcome of the entire
     * expression.
     */
    FlowScope getOutcomeFlowScope(int nodeType, boolean outcome) {
      if (nodeType == Token.AND && outcome ||
          nodeType == Token.OR && !outcome) {
        // We know that the whole expression must have executed.
        return rightScope;
      } else {
        return getJoinedFlowScope();
      }
    }
  }

  private BooleanOutcomePair newBooleanOutcomePair(
      JSType jsType, FlowScope flowScope) {
    if (jsType == null) {
      return new BooleanOutcomePair(
          BooleanLiteralSet.BOTH, BooleanLiteralSet.BOTH, flowScope, flowScope);
    }
    return new BooleanOutcomePair(jsType.getPossibleToBooleanOutcomes(),
        registry.getNativeType(BOOLEAN_TYPE).isSubtype(jsType) ?
            BooleanLiteralSet.BOTH : BooleanLiteralSet.EMPTY,
        flowScope, flowScope);
  }

  private void redeclareSimpleVar(
      FlowScope scope, Node nameNode, JSType varType) {
    Preconditions.checkState(nameNode.isName());
    String varName = nameNode.getString();
    if (varType == null) {
      varType = getNativeType(JSTypeNative.UNKNOWN_TYPE);
    }
    if (isUnflowable(syntacticScope.getVar(varName))) {
      return;
    }
    scope.inferSlotType(varName, varType);
  }

  private boolean isUnflowable(Var v) {
    return v != null && v.isLocal() && v.isMarkedEscaped() &&
        // It's OK to flow a variable in the scope where it's escaped.
        v.getScope() == syntacticScope;
  }

  /**
   * This method gets the JSType from the Node argument and verifies that it is
   * present.
   */
  private JSType getJSType(Node n) {
    JSType jsType = n.getJSType();
    if (jsType == null) {
      // TODO(nicksantos): This branch indicates a compiler bug, not worthy of
      // halting the compilation but we should log this and analyze to track
      // down why it happens. This is not critical and will be resolved over
      // time as the type checker is extended.
      return unknownType;
    } else {
      return jsType;
    }
  }

  private JSType getNativeType(JSTypeNative typeId) {
    return registry.getNativeType(typeId);
  }
}

```
