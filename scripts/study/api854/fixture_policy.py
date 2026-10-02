"""Predeclared explicit fixture capability filter, never selected by buggy outcomes."""
POLICY = 'beam-explicit-fixtures-v3-proposal'

SCALARS = {'boolean', 'byte', 'short', 'int', 'long', 'float', 'double', 'char',
           'java.lang.String', 'java.lang.Boolean', 'java.lang.Byte', 'java.lang.Short',
           'java.lang.Integer', 'java.lang.Long', 'java.lang.Float', 'java.lang.Double',
           'java.lang.Character', 'java.lang.Object', 'java.lang.Number', 'java.util.Date',
           'java.util.Locale', 'java.util.List', 'java.util.Collection', 'java.lang.Iterable',
           'java.util.Iterator', 'java.util.Map', 'java.util.Set'}
CLOSURE = {'com.google.javascript.jscomp.AbstractCompiler', 'com.google.javascript.jscomp.ControlFlowGraph',
           'com.google.javascript.jscomp.type.ReverseAbstractInterpreter', 'com.google.javascript.jscomp.Scope',
           'com.google.javascript.jscomp.Scope$Var', 'com.google.javascript.jscomp.type.FlowScope',
           'com.google.javascript.rhino.Node', 'com.google.javascript.rhino.jstype.JSType',
           'com.google.javascript.rhino.jstype.ObjectType', 'com.google.javascript.rhino.jstype.JSTypeNative',
           'com.google.javascript.rhino.jstype.BooleanLiteralSet'}
JXPATH = {'org.w3c.dom.Node', 'org.w3c.dom.Document', 'org.w3c.dom.Element',
          'org.apache.commons.jxpath.ri.QName', 'org.apache.commons.jxpath.ri.compiler.NodeTest',
          'org.apache.commons.jxpath.ri.model.NodePointer'}
# Methods requiring specialized AST parent/sibling/call metadata have no reviewed
# recipe yet. This list is a structural restriction, not an outcome-based prune.
CLOSURE_METHODS = {'createEntryLattice', 'createInitialEstimateLattice', 'flowThrough',
    'branchedFlowThrough', 'isAddedAsNumber', 'isUnflowable', 'newBooleanOutcomePair',
    'traverseAnd', 'traverseOr', 'traverseShortCircuitingBinOp', 'traverseWithinShortCircuitingBinOp',
    'narrowScope', 'traverse', 'traverseAdd', 'traverseArrayLiteral', 'traverseAssign',
    'traverseChildren', 'traverseGetElem', 'traverseGetProp', 'traverseHook', 'traverseName',
    'traverseObjectLiteral', 'traverseReturn', 'getJSType', 'getNativeType',
    'redeclareSimpleVar', 'updateScopeForTypeChange', 'getBooleanOutcomes'}


def select(targets, policy):
    if policy is None:
        return targets, []
    if policy != POLICY:
        raise ValueError('Unknown explicit fixture policy')
    selected, excluded = [], []
    for target in targets:
        name = target['class']
        family = CLOSURE if name == 'com.google.javascript.jscomp.TypeInference' else JXPATH if name in {
            'org.apache.commons.jxpath.ri.model.dom.DOMNodePointer',
            'org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer'} else set()
        reason = None
        if not family:
            reason = 'explicit_project_recipe_not_reviewed'
        elif target['method'] in {'<init>', 'hashCode'}:
            reason = 'constructor_or_identity_oracle_not_reviewed'
        elif family is CLOSURE and target['method'] not in CLOSURE_METHODS:
            reason = 'specialized_ast_recipe_not_reviewed'
        else:
            required = set(filter(None, (target['constructor_types'] + ',' + target['parameter_types']).split(',')))
            missing = required - SCALARS - family
            if missing:
                reason = 'explicit_argument_recipe_missing:' + ','.join(sorted(missing))
        if reason:
            excluded.append({'target': target, 'reason': reason})
        else:
            selected.append(target)
    return selected, excluded
