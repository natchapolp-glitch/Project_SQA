"""Predeclared explicit fixture capability filter, never selected by buggy outcomes."""
POLICY = 'beam-explicit-fixtures-v3-proposal'
RECIPE_SOURCES = ('algorithms/java/SqaProbe.java', 'scripts/study/api854/fixture_policy.py')


def recipe_document(source_hashes, policy=POLICY):
    from .common import ROOT, sha256
    expected = {name: source_hashes[name] for name in RECIPE_SOURCES}
    if any(sha256(ROOT / name) != value for name, value in expected.items()):
        raise ValueError('Explicit recipe source differs from protocol')
    if policy not in {POLICY, POLICY_V4, POLICY_V5, POLICY_V6}:
        raise ValueError('Unknown explicit fixture policy')
    return {'schema_version': 1, 'fixture_policy_id': policy, 'source_sha256': expected,
        'sources': {name: (ROOT / name).read_bytes().decode('utf-8') for name in RECIPE_SOURCES},
        'scope': 'Same fixture construction/projection knowledge for all four approaches; no execution feedback'}


def validate_recipe(recipe, source_hashes=None, policy=POLICY):
    from .preparation import digest
    if (policy not in {POLICY, POLICY_V4, POLICY_V5, POLICY_V6} or not isinstance(recipe, dict) or recipe.get('fixture_policy_id') != policy
            or not isinstance(recipe.get('sources'), dict) or set(recipe['sources']) != set(RECIPE_SOURCES)
            or not isinstance(recipe.get('source_sha256'), dict) or set(recipe['source_sha256']) != set(RECIPE_SOURCES)
            or any(not isinstance(recipe['sources'][name], str)
                   or digest(recipe['sources'][name].encode('utf-8')) != recipe['source_sha256'][name] for name in RECIPE_SOURCES)):
        raise ValueError('Explicit recipe source bytes/hash differ')
    if source_hashes is not None and any(source_hashes.get(name) != recipe['source_sha256'][name] for name in RECIPE_SOURCES):
        raise ValueError('Explicit recipe source differs from frozen protocol')
    return True
POLICY_V4 = 'beam-explicit-fixtures-v4-proposal'
POLICY_V5 = 'beam-explicit-fixtures-v5-proposal'
POLICY_V6 = 'aom-beam-fraction-field-v6-development'

# Fixed-source recipes, declared before generation/evaluation. This development
# version deliberately preserves unsupported declarations as explicit exclusions.
PILOT_METHODS = {
    'org.apache.commons.lang3.math.NumberUtils': {'isDigits', 'isNumber', 'max', 'min', 'toByte', 'toDouble',
        'toFloat', 'toInt', 'toLong', 'toShort', 'createDouble', 'createFloat', 'createInteger', 'createLong',
        'createNumber', 'createBigDecimal', 'createBigInteger'},
    'com.fasterxml.jackson.core.io.NumberInput': {'parseAsDouble', 'parseDouble', 'parseAsInt', 'parseInt',
        'parseBigDecimal', 'parseAsLong', 'parseLong', 'inLongRange'},
    'com.fasterxml.jackson.core.util.TextBuffer': {'hasTextAsCharacters', 'contentsAsArray', 'contentsAsString',
        'getCurrentSegmentSize', 'getTextOffset', 'size', 'toString', 'append', 'resetWithEmpty', 'resetWithString'},
    'org.apache.commons.math3.fraction.BigFraction': {'equals', 'doubleValue', 'percentageValue', 'floatValue',
        'getDenominator', 'getNumerator', 'intValue', 'longValue', 'toString', 'abs', 'add', 'subtract',
        'multiply', 'divide', 'negate', 'reciprocal', 'reduce', 'compareTo'},
    'org.apache.commons.math3.fraction.Fraction': {'equals', 'doubleValue', 'percentageValue', 'floatValue',
        'getDenominator', 'getNumerator', 'intValue', 'longValue', 'toString', 'abs', 'add', 'subtract',
        'multiply', 'divide', 'negate', 'reciprocal', 'compareTo'},
    'org.jsoup.nodes.Document': {'nodeName', 'outerHtml', 'title', 'normalise', 'body', 'head', 'text', 'createElement', 'createShell'},
    'org.apache.commons.cli.CommandLine': {'hasOption', 'getOptionObject', 'getOptionValue', 'getArgs',
        'getOptionValues', 'iterator', 'getArgList', 'getOptions', 'addArg', 'addOption'},
    'org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream': {'ensureOpen', 'writeCString',
        'close', 'closeArchiveEntry', 'finish', 'putArchiveEntry', 'putNextEntry', 'write'},
    'org.joda.time.field.UnsupportedDurationField': {'equals', 'isPrecise', 'isSupported', 'getType',
        'getName', 'toString', 'getUnitMillis', 'compareTo', 'getInstance'},
    'org.joda.time.Partial': {'size', 'getValues', 'toStringList', 'with', 'withField', 'without'},
    'com.google.gson.TypeInfoFactory': {'getIndex', 'getActualType', 'extractRealTypes', 'getTypeInfoForField', 'getTypeInfoForArray'},
    'com.google.javascript.jscomp.RemoveUnusedVars': {'process', 'traverseAndRemoveUnusedReferences', 'getFunctionArgList'},
    'org.jfree.chart.renderer.category.AreaRenderer': {'findRangeBounds', 'getRowCount', 'getColumnCount',
        'getPassCount', 'getLegendItems', 'getLegendItem', 'getItemMiddle'},
    'com.fasterxml.jackson.databind.ser.BeanPropertyWriter': {'getName', 'getSerializedName', 'getType',
        'getPropertyType', 'getGenericPropertyType', 'isRequired', 'willSuppressNulls', 'hasSerializer',
        'hasNullSerializer', 'rename', 'get', 'getInternalSetting', 'setInternalSetting', 'removeInternalSetting'},
    'com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer': {'deserialize', 'deserializeUsingCustom', 'handleNonArray'},
    'com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser': {'hasTextCharacters', 'isClosed', 'isExpectedStartArrayToken',
        'requiresCustomCodec', 'getTextCharacters', 'nextToken', 'getText', 'getCurrentName', 'getValueAsString',
        'nextTextValue', 'close', 'overrideCurrentName', 'setXMLTextElementName'},
    'org.mockito.internal.invocation.InvocationMatcher': {'matches', 'hasSameMethod', 'hasSimilarMethod',
        'getMethod', 'getInvocation', 'toString'},
}
PILOT_TYPES = {'com.fasterxml.jackson.core.util.BufferRecycler', 'org.apache.commons.math3.fraction.BigFraction',
    'org.apache.commons.math3.fraction.Fraction', 'java.math.BigInteger', 'org.apache.commons.cli.Option',
    'java.io.OutputStream', 'org.apache.commons.compress.archivers.ArchiveEntry',
    'org.apache.commons.compress.archivers.cpio.CpioArchiveEntry', 'org.joda.time.DurationFieldType',
    'org.joda.time.DurationField', 'org.joda.time.DateTimeFieldType', 'java.lang.reflect.Type',
    'java.lang.reflect.TypeVariable', '[Ljava.lang.reflect.Type;', '[Ljava.lang.reflect.TypeVariable;',
    'java.lang.reflect.Field', 'java.lang.Class', 'com.google.javascript.jscomp.AbstractCompiler',
    'com.google.javascript.rhino.Node', 'org.jfree.data.category.CategoryDataset', 'org.jfree.chart.axis.CategoryAxis',
    'java.lang.Comparable', 'java.awt.geom.Rectangle2D', 'org.jfree.chart.util.RectangleEdge',
    'com.fasterxml.jackson.databind.ser.BeanPropertyWriter', 'com.fasterxml.jackson.databind.util.NameTransformer',
    'com.fasterxml.jackson.databind.JavaType', 'com.fasterxml.jackson.databind.JsonDeserializer',
    'com.fasterxml.jackson.databind.deser.ValueInstantiator', 'com.fasterxml.jackson.core.JsonParser',
    'com.fasterxml.jackson.databind.DeserializationContext', 'com.fasterxml.jackson.core.io.IOContext',
    'com.fasterxml.jackson.core.ObjectCodec', 'javax.xml.stream.XMLStreamReader', 'org.mockito.invocation.Invocation'}

# Added capability recipes are fixed before any buggy evaluation. Mutators need
# structural post-state; unsupported helpers/serialization hooks stay excluded.
ADDITIONAL_METHODS = {
    'org.apache.commons.codec.language.Caverphone': {'isCaverphoneEqual', 'encode', 'caverphone'},
    'org.apache.commons.codec.language.Metaphone': {'isLastChar', 'isMetaphoneEqual', 'getMaxCodeLen', 'encode', 'metaphone'},
    'org.apache.commons.codec.language.SoundexUtils': {'differenceEncoded', 'clean'},
    'org.apache.commons.collections.map.Flat3Map': {'containsKey', 'containsValue', 'equals', 'isEmpty', 'size',
        'clone', 'get', 'put', 'remove', 'toString', 'clear', 'putAll'},
    'org.apache.commons.csv.ExtendedBufferedReader': {'getLineNumber', 'lookAhead', 'readAgain', 'read', 'readLine'},
}

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
    if policy not in {POLICY, POLICY_V4, POLICY_V5, POLICY_V6}:
        raise ValueError('Unknown explicit fixture policy')
    if policy == POLICY_V6:
        # Keep every v5 decision, adding only the exact Champ-accepted signatures.
        selected, excluded = select(targets, POLICY_V5)
        accepted = lambda t: (t['class'] in {
            'org.apache.commons.math3.fraction.BigFraction', 'org.apache.commons.math3.fraction.Fraction'}
            and t['constructor_types'] == 'double' and t['method'] == 'getField' and t['parameter_types'] == '')
        chosen = {tuple(t[k] for k in ('class', 'constructor_types', 'method', 'parameter_types')) for t in selected}
        return ([t for t in targets if accepted(t) or tuple(t[k] for k in ('class', 'constructor_types', 'method', 'parameter_types')) in chosen],
                [row for row in excluded if not accepted(row['target'])])
    selected, excluded = [], []
    for target in targets:
        name = target['class']
        family = CLOSURE if name == 'com.google.javascript.jscomp.TypeInference' else JXPATH if name in {
            'org.apache.commons.jxpath.ri.model.dom.DOMNodePointer',
            'org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer'} else set()
        extra = policy in {POLICY_V4, POLICY_V5} and name in ADDITIONAL_METHODS
        pilot = policy == POLICY_V5 and name in PILOT_METHODS
        if extra:
            family = {'java.io.Reader'}
        if pilot:
            family = PILOT_TYPES | {'[B', '[S', '[I', '[J', '[F', '[D', '[C'}
        reason = None
        if not family:
            reason = 'explicit_project_recipe_not_reviewed'
        elif target['method'] in {'<init>', 'hashCode'}:
            reason = 'constructor_or_identity_oracle_not_reviewed'
        elif family is CLOSURE and target['method'] not in CLOSURE_METHODS:
            reason = 'specialized_ast_recipe_not_reviewed'
        elif extra and target['method'] not in ADDITIONAL_METHODS[name]:
            reason = 'additional_method_preconditions_or_state_not_reviewed'
        elif extra and name.endswith('ExtendedBufferedReader') and target['parameter_types']:
            reason = 'reader_buffer_offset_bounds_recipe_not_reviewed'
        elif pilot and target['method'] not in PILOT_METHODS[name]:
            reason = 'pilot_method_preconditions_or_oracle_not_reviewed'
        elif pilot and name.endswith('NumberInput') and '[' in target['parameter_types']:
            reason = 'numeric_buffer_slice_recipe_not_reviewed'
        elif pilot and name.endswith('TextBuffer') and target['method'] == 'append' and target['parameter_types'] != 'char':
            reason = 'text_buffer_slice_recipe_not_reviewed'
        elif pilot and name.endswith('Document') and target['parameter_types'] == 'org.jsoup.nodes.Element':
            reason = 'html_internal_normalise_recipe_not_reviewed'
        elif pilot and name.endswith('CpioArchiveOutputStream') and target['method'] == 'write' and target['parameter_types'] != 'int':
            reason = 'archive_buffer_slice_recipe_not_reviewed'
        elif pilot and name.endswith('BeanPropertyWriter') and target['method'] == 'isRequired' and target['parameter_types']:
            reason = 'annotation_introspector_recipe_not_reviewed'
        elif pilot and name.endswith('RemoveUnusedVars') and target['method'] == 'process' and target['parameter_types'] != 'com.google.javascript.rhino.Node,com.google.javascript.rhino.Node':
            reason = 'call_site_definition_finder_recipe_not_reviewed'
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
