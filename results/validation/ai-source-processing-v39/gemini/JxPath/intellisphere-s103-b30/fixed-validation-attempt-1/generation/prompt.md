# Defects4J unit test generation

You are a Java unit testing engineer. Write deterministic regression tests for the
production classes supplied below, using their documented and fixed-reference
behavior. This experiment measures test generation from fixed production source.

Produce Java test sources with assertions. Exercise normal cases, boundaries,
invalid inputs, exception paths and branches supported by the supplied source.
Inspect the supplied build configuration and use the JUnit version and dependencies
already available to this project. Defects4J projects can have different build
systems and JUnit versions; do not assume Maven or JUnit 5. Use Java 11 compatible
syntax unless the build configuration requires an older source level.

Constraints:

- Do not change production code or build files and do not add dependencies.
- Use the reference behavior to derive assertions; do not invent unsupported APIs.
- Avoid network access, external programs, wall-clock timing, random values without
  a fixed seed, machine-specific paths and environment-dependent assertions.
- Use test class names ending in `Test`, correctly matching Java file and package
  names. Place each source at its package-relative path, such as
  `org/example/GeneratedExampleTest.java`.
- Keep tests independent. Restore global state that a test changes.
- Do not ask for a bug patch, buggy revision, existing detecting test, or hidden
  evaluation results. Only the supplied reference source and build information may
  guide the initial generation.

Return each Java file in a separate fenced Java code block, preceded by its
package-relative path. Include complete imports and test class definitions. If the
context is insufficient to write a compiling test, explicitly state the missing
API or dependency rather than producing a fabricated result.

## Experiment context

Project: JxPath
Fixed reference revision: 1f
Target classes:
- org.apache.commons.jxpath.ri.model.dom.DOMNodePointer
- org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer

## Eligible shared API declarations

Target these declarations, which exist on both evaluation revisions. Only declaration signatures were checked; no buggy behavior was supplied. Generate at most 30 independent test methods per response.

```json
[
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node",
    "method": "getRelativePositionByName",
    "parameter_types": "",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node",
    "method": "getRelativePositionOfElement",
    "parameter_types": "",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node",
    "method": "getRelativePositionOfPI",
    "parameter_types": "java.lang.String",
    "dimensions": 9
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node",
    "method": "getRelativePositionOfTextNode",
    "parameter_types": "",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node",
    "method": "escape",
    "parameter_types": "java.lang.String",
    "dimensions": 9
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node",
    "method": "stringValue",
    "parameter_types": "org.w3c.dom.Node",
    "dimensions": 9
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node",
    "method": "getAbstractFactory",
    "parameter_types": "org.apache.commons.jxpath.JXPathContext",
    "dimensions": 9
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "",
    "method": "equalStrings",
    "parameter_types": "java.lang.String,java.lang.String",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node",
    "method": "getLanguage",
    "parameter_types": "",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node",
    "method": "equals",
    "parameter_types": "java.lang.Object",
    "dimensions": 9
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node",
    "method": "isActual",
    "parameter_types": "",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node",
    "method": "isCollection",
    "parameter_types": "",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node",
    "method": "isLanguage",
    "parameter_types": "java.lang.String",
    "dimensions": 9
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node",
    "method": "isLeaf",
    "parameter_types": "",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node",
    "method": "testNode",
    "parameter_types": "org.apache.commons.jxpath.ri.compiler.NodeTest",
    "dimensions": 9
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node",
    "method": "compareChildNodePointers",
    "parameter_types": "org.apache.commons.jxpath.ri.model.NodePointer,org.apache.commons.jxpath.ri.model.NodePointer",
    "dimensions": 12
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node",
    "method": "getLength",
    "parameter_types": "",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node",
    "method": "hashCode",
    "parameter_types": "",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node",
    "method": "getBaseValue",
    "parameter_types": "",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node",
    "method": "getImmediateNode",
    "parameter_types": "",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node",
    "method": "getValue",
    "parameter_types": "",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node",
    "method": "asPath",
    "parameter_types": "",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node",
    "method": "getDefaultNamespaceURI",
    "parameter_types": "",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node",
    "method": "getNamespaceURI",
    "parameter_types": "",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node",
    "method": "getNamespaceURI",
    "parameter_types": "java.lang.String",
    "dimensions": 9
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node",
    "method": "getPointerByID",
    "parameter_types": "org.apache.commons.jxpath.JXPathContext,java.lang.String",
    "dimensions": 12
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node",
    "method": "getName",
    "parameter_types": "",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node",
    "method": "attributeIterator",
    "parameter_types": "org.apache.commons.jxpath.ri.QName",
    "dimensions": 9
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node",
    "method": "childIterator",
    "parameter_types": "org.apache.commons.jxpath.ri.compiler.NodeTest,boolean,org.apache.commons.jxpath.ri.model.NodePointer",
    "dimensions": 15
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node",
    "method": "namespaceIterator",
    "parameter_types": "",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node",
    "method": "createAttribute",
    "parameter_types": "org.apache.commons.jxpath.JXPathContext,org.apache.commons.jxpath.ri.QName",
    "dimensions": 12
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node",
    "method": "createChild",
    "parameter_types": "org.apache.commons.jxpath.JXPathContext,org.apache.commons.jxpath.ri.QName,int",
    "dimensions": 15
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node",
    "method": "createChild",
    "parameter_types": "org.apache.commons.jxpath.JXPathContext,org.apache.commons.jxpath.ri.QName,int,java.lang.Object",
    "dimensions": 18
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node",
    "method": "namespacePointer",
    "parameter_types": "java.lang.String",
    "dimensions": 9
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "",
    "method": "testNode",
    "parameter_types": "org.w3c.dom.Node,org.apache.commons.jxpath.ri.compiler.NodeTest",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "",
    "method": "getLocalName",
    "parameter_types": "org.w3c.dom.Node",
    "dimensions": 3
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "",
    "method": "getNamespaceURI",
    "parameter_types": "org.w3c.dom.Node",
    "dimensions": 3
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "",
    "method": "getPrefix",
    "parameter_types": "org.w3c.dom.Node",
    "dimensions": 3
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node",
    "method": "remove",
    "parameter_types": "",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node",
    "method": "setValue",
    "parameter_types": "java.lang.Object",
    "dimensions": 9
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node",
    "method": "<init>",
    "parameter_types": "",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "org.w3c.dom.Node,java.util.Locale",
    "method": "<init>",
    "parameter_types": "",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
    "constructor_types": "org.w3c.dom.Node,java.util.Locale,java.lang.String",
    "method": "<init>",
    "parameter_types": "",
    "dimensions": 9
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
    "constructor_types": "java.lang.Object,java.util.Locale",
    "method": "getRelativePositionByName",
    "parameter_types": "",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
    "constructor_types": "java.lang.Object,java.util.Locale",
    "method": "getRelativePositionOfElement",
    "parameter_types": "",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
    "constructor_types": "java.lang.Object,java.util.Locale",
    "method": "getRelativePositionOfPI",
    "parameter_types": "java.lang.String",
    "dimensions": 9
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
    "constructor_types": "java.lang.Object,java.util.Locale",
    "method": "getRelativePositionOfTextNode",
    "parameter_types": "",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
    "constructor_types": "java.lang.Object,java.util.Locale",
    "method": "escape",
    "parameter_types": "java.lang.String",
    "dimensions": 9
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
    "constructor_types": "java.lang.Object,java.util.Locale",
    "method": "getAbstractFactory",
    "parameter_types": "org.apache.commons.jxpath.JXPathContext",
    "dimensions": 9
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
    "constructor_types": "java.lang.Object,java.util.Locale",
    "method": "nodeParent",
    "parameter_types": "java.lang.Object",
    "dimensions": 9
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
    "constructor_types": "",
    "method": "equalStrings",
    "parameter_types": "java.lang.String,java.lang.String",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
    "constructor_types": "",
    "method": "getNamespaceURI",
    "parameter_types": "java.lang.Object",
    "dimensions": 3
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
    "constructor_types": "java.lang.Object,java.util.Locale",
    "method": "addContent",
    "parameter_types": "java.util.List",
    "dimensions": 9
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
    "constructor_types": "java.lang.Object,java.util.Locale",
    "method": "getLanguage",
    "parameter_types": "",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
    "constructor_types": "java.lang.Object,java.util.Locale",
    "method": "equals",
    "parameter_types": "java.lang.Object",
    "dimensions": 9
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
    "constructor_types": "java.lang.Object,java.util.Locale",
    "method": "isCollection",
    "parameter_types": "",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
    "constructor_types": "java.lang.Object,java.util.Locale",
    "method": "isLanguage",
    "parameter_types": "java.lang.String",
    "dimensions": 9
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
    "constructor_types": "java.lang.Object,java.util.Locale",
    "method": "isLeaf",
    "parameter_types": "",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
    "constructor_types": "java.lang.Object,java.util.Locale",
    "method": "testNode",
    "parameter_types": "org.apache.commons.jxpath.ri.compiler.NodeTest",
    "dimensions": 9
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
    "constructor_types": "java.lang.Object,java.util.Locale",
    "method": "compareChildNodePointers",
    "parameter_types": "org.apache.commons.jxpath.ri.model.NodePointer,org.apache.commons.jxpath.ri.model.NodePointer",
    "dimensions": 12
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
    "constructor_types": "java.lang.Object,java.util.Locale",
    "method": "getLength",
    "parameter_types": "",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
    "constructor_types": "java.lang.Object,java.util.Locale",
    "method": "hashCode",
    "parameter_types": "",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
    "constructor_types": "java.lang.Object,java.util.Locale",
    "method": "getBaseValue",
    "parameter_types": "",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
    "constructor_types": "java.lang.Object,java.util.Locale",
    "method": "getImmediateNode",
    "parameter_types": "",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
    "constructor_types": "java.lang.Object,java.util.Locale",
    "method": "getValue",
    "parameter_types": "",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
    "constructor_types": "java.lang.Object,java.util.Locale",
    "method": "asPath",
    "parameter_types": "",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
    "constructor_types": "java.lang.Object,java.util.Locale",
    "method": "getNamespaceURI",
    "parameter_types": "",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
    "constructor_types": "java.lang.Object,java.util.Locale",
    "method": "getNamespaceURI",
    "parameter_types": "java.lang.String",
    "dimensions": 9
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
    "constructor_types": "java.lang.Object,java.util.Locale",
    "method": "getName",
    "parameter_types": "",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
    "constructor_types": "java.lang.Object,java.util.Locale",
    "method": "attributeIterator",
    "parameter_types": "org.apache.commons.jxpath.ri.QName",
    "dimensions": 9
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
    "constructor_types": "java.lang.Object,java.util.Locale",
    "method": "childIterator",
    "parameter_types": "org.apache.commons.jxpath.ri.compiler.NodeTest,boolean,org.apache.commons.jxpath.ri.model.NodePointer",
    "dimensions": 15
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
    "constructor_types": "java.lang.Object,java.util.Locale",
    "method": "namespaceIterator",
    "parameter_types": "",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
    "constructor_types": "java.lang.Object,java.util.Locale",
    "method": "createAttribute",
    "parameter_types": "org.apache.commons.jxpath.JXPathContext,org.apache.commons.jxpath.ri.QName",
    "dimensions": 12
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
    "constructor_types": "java.lang.Object,java.util.Locale",
    "method": "createChild",
    "parameter_types": "org.apache.commons.jxpath.JXPathContext,org.apache.commons.jxpath.ri.QName,int",
    "dimensions": 15
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
    "constructor_types": "java.lang.Object,java.util.Locale",
    "method": "createChild",
    "parameter_types": "org.apache.commons.jxpath.JXPathContext,org.apache.commons.jxpath.ri.QName,int,java.lang.Object",
    "dimensions": 18
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
    "constructor_types": "java.lang.Object,java.util.Locale",
    "method": "namespacePointer",
    "parameter_types": "java.lang.String",
    "dimensions": 9
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
    "constructor_types": "",
    "method": "testNode",
    "parameter_types": "org.apache.commons.jxpath.ri.model.NodePointer,java.lang.Object,org.apache.commons.jxpath.ri.compiler.NodeTest",
    "dimensions": 9
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
    "constructor_types": "",
    "method": "getLocalName",
    "parameter_types": "java.lang.Object",
    "dimensions": 3
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
    "constructor_types": "",
    "method": "getPrefix",
    "parameter_types": "java.lang.Object",
    "dimensions": 3
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
    "constructor_types": "java.lang.Object,java.util.Locale",
    "method": "remove",
    "parameter_types": "",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
    "constructor_types": "java.lang.Object,java.util.Locale",
    "method": "setValue",
    "parameter_types": "java.lang.Object",
    "dimensions": 9
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
    "constructor_types": "java.lang.Object,java.util.Locale",
    "method": "<init>",
    "parameter_types": "",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
    "constructor_types": "org.apache.commons.jxpath.ri.model.NodePointer,java.lang.Object",
    "method": "<init>",
    "parameter_types": "",
    "dimensions": 6
  },
  {
    "class": "org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
    "constructor_types": "java.lang.Object,java.util.Locale,java.lang.String",
    "method": "<init>",
    "parameter_types": "",
    "dimensions": 9
  }
]
```

## Production source src/java/org/apache/commons/jxpath/ri/model/dom/DOMNodePointer.java

```java
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.jxpath.ri.model.dom;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.jxpath.AbstractFactory;
import org.apache.commons.jxpath.JXPathAbstractFactoryException;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathException;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.Compiler;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.apache.commons.jxpath.ri.compiler.NodeTypeTest;
import org.apache.commons.jxpath.ri.compiler.ProcessingInstructionTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.NullPointer;
import org.apache.commons.jxpath.util.TypeUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;

/**
 * A Pointer that points to a DOM node.
 *
 * @author Dmitri Plotnikov
 * @version $Revision$ $Date$
 */
public class DOMNodePointer extends NodePointer {

    private static final long serialVersionUID = -8751046933894857319L;
    
    private Node node;
    private Map namespaces;
    private String defaultNamespace;
    private String id;

    public static final String XML_NAMESPACE_URI = 
            "http://www.w3.org/XML/1998/namespace";
    public static final String XMLNS_NAMESPACE_URI = 
            "http://www.w3.org/2000/xmlns/";

    public DOMNodePointer(Node node, Locale locale) {
        super(null, locale);
        this.node = node;
    }

    public DOMNodePointer(Node node, Locale locale, String id) {
        super(null, locale);
        this.node = node;
        this.id = id;
    }

    public DOMNodePointer(NodePointer parent, Node node) {
        super(parent);
        this.node = node;
    }
    
    public boolean testNode(NodeTest test) {
        return testNode(node, test);
    }

    public static boolean testNode(Node node, NodeTest test) {
        if (test == null) {
            return true;
        }
        else if (test instanceof NodeNameTest) {
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                return false;
            }

            NodeNameTest nodeNameTest = (NodeNameTest) test;
            QName testName = nodeNameTest.getNodeName();
            String namespaceURI = nodeNameTest.getNamespaceURI();
            boolean wildcard = nodeNameTest.isWildcard();
            String testPrefix = testName.getPrefix();
            if (wildcard && testPrefix == null) {
                return true;
            }

            if (wildcard
                || testName.getName()
                        .equals(DOMNodePointer.getLocalName(node))) {
                String nodeNS = DOMNodePointer.getNamespaceURI(node);
                return equalStrings(namespaceURI, nodeNS);
            }
        }
        else if (test instanceof NodeTypeTest) {
            int nodeType = node.getNodeType();
            switch (((NodeTypeTest) test).getNodeType()) {
                case Compiler.NODE_TYPE_NODE :
                    return nodeType == Node.ELEMENT_NODE
                            || nodeType == Node.DOCUMENT_NODE;
                case Compiler.NODE_TYPE_TEXT :
                    return nodeType == Node.CDATA_SECTION_NODE
                        || nodeType == Node.TEXT_NODE;
                case Compiler.NODE_TYPE_COMMENT :
                    return nodeType == Node.COMMENT_NODE;
                case Compiler.NODE_TYPE_PI :
                    return nodeType == Node.PROCESSING_INSTRUCTION_NODE;
            }
            return false;
        }
        else if (test instanceof ProcessingInstructionTest) {
            if (node.getNodeType() == Node.PROCESSING_INSTRUCTION_NODE) {
                String testPI = ((ProcessingInstructionTest) test).getTarget();
                String nodePI = ((ProcessingInstruction) node).getTarget();
                return testPI.equals(nodePI);
            }
        }
        return false;
    }

    private static boolean equalStrings(String s1, String s2) {
        if (s1 == null) {
            return s2 == null || s2.trim().length() == 0;
        }
        
        if (s2 == null) {
            return s1 == null || s1.trim().length() == 0;
        }

        if (s1 != null && !s1.trim().equals(s2.trim())) {
            return false;
        }

        return true;
    }

    public QName getName() {
        String ln = null;
        String ns = null;
        int type = node.getNodeType();
        if (type == Node.ELEMENT_NODE) {
            ns = DOMNodePointer.getPrefix(node);
            ln = DOMNodePointer.getLocalName(node);
        }
        else if (type == Node.PROCESSING_INSTRUCTION_NODE) {
            ln = ((ProcessingInstruction) node).getTarget();
        }
        return new QName(ns, ln);
    }

    public String getNamespaceURI() {
        return getNamespaceURI(node);
    }

    public NodeIterator childIterator(
        NodeTest test,
        boolean reverse,
        NodePointer startWith) 
    {
        return new DOMNodeIterator(this, test, reverse, startWith);
    }

    public NodeIterator attributeIterator(QName name) {
        return new DOMAttributeIterator(this, name);
    }

    public NodePointer namespacePointer(String prefix) {
        return new NamespacePointer(this, prefix);
    }

    public NodeIterator namespaceIterator() {
        return new DOMNamespaceIterator(this);
    }

    public String getNamespaceURI(String prefix) {
        if (prefix == null || prefix.equals("")) {
            return getDefaultNamespaceURI();
        }

        if (prefix.equals("xml")) {
            return XML_NAMESPACE_URI;
        }

        if (prefix.equals("xmlns")) {
            return XMLNS_NAMESPACE_URI;
        }

        String namespace = null;
        if (namespaces == null) {
            namespaces = new HashMap();
        }
        else {
            namespace = (String) namespaces.get(prefix);
        }

        if (namespace == null) {
            String qname = "xmlns:" + prefix;
            Node aNode = node;
            if (aNode instanceof Document) {
                aNode = ((Document)aNode).getDocumentElement();
            }
            while (aNode != null) {
                if (aNode.getNodeType() == Node.ELEMENT_NODE) {
                    Attr attr = ((Element) aNode).getAttributeNode(qname);
                    if (attr != null) {
                        namespace = attr.getValue();
                        break;
                    }
                }
                aNode = aNode.getParentNode();
            }
            if (namespace == null || namespace.equals("")) {
                namespace = NodePointer.UNKNOWN_NAMESPACE;
            }
        }

        namespaces.put(prefix, namespace);
        if (namespace == UNKNOWN_NAMESPACE) {
            return null;
        }
        
        // TBD: We are supposed to resolve relative URIs to absolute ones.
        return namespace;
    }

    public String getDefaultNamespaceURI() {
        if (defaultNamespace == null) {
            Node aNode = node;
            if (aNode instanceof Document) {
                aNode = ((Document) aNode).getDocumentElement();
            }
            while (aNode != null) {
                if (aNode.getNodeType() == Node.ELEMENT_NODE) {
                    Attr attr = ((Element) aNode).getAttributeNode("xmlns");
                    if (attr != null) {
                        defaultNamespace = attr.getValue();
                        break;
                    }
                }
                aNode = aNode.getParentNode();
            }
        }
        if (defaultNamespace == null) {
            defaultNamespace = "";
        }
        // TBD: We are supposed to resolve relative URIs to absolute ones.
        return defaultNamespace.equals("") ? null : defaultNamespace;
    }

    public Object getBaseValue() {
        return node;
    }

    public Object getImmediateNode() {
        return node;
    }

    public boolean isActual() {
        return true;
    }

    public boolean isCollection() {
        return false;
    }

    public int getLength() {
        return 1;
    }

    public boolean isLeaf() {
        return !node.hasChildNodes();
    }

    /**
     * Returns true if the xml:lang attribute for the current node
     * or its parent has the specified prefix <i>lang</i>.
     * If no node has this prefix, calls <code>super.isLanguage(lang)</code>.
     */
    public boolean isLanguage(String lang) {
        String current = getLanguage();
        if (current == null) {
            return super.isLanguage(lang);
        }
        return current.toUpperCase().startsWith(lang.toUpperCase());
    }

    protected String getLanguage() {
        Node n = node;
        while (n != null) {
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) n;
                String attr = e.getAttribute("xml:lang");
                if (attr != null && !attr.equals("")) {
                    return attr;
                }
            }
            n = n.getParentNode();
        }
        return null;
    }

    /**
     * Sets contents of the node to the specified value. If the value is
     * a String, the contents of the node are replaced with this text.
     * If the value is an Element or Document, the children of the
     * node are replaced with the children of the passed node.
     */
    public void setValue(Object value) {
        if (node.getNodeType() == Node.TEXT_NODE
            || node.getNodeType() == Node.CDATA_SECTION_NODE) {
            String string = (String) TypeUtils.convert(value, String.class);
            if (string != null && !string.equals("")) {
                node.setNodeValue(string);
            }
            else {
                node.getParentNode().removeChild(node);
            }
        }
        else {
            NodeList children = node.getChildNodes();
            int count = children.getLength();
            for (int i = count; --i >= 0;) {
                Node child = children.item(i);
                node.removeChild(child);
            }

            if (value instanceof Node) {
                Node valueNode = (Node) value;
                if (valueNode instanceof Element
                    || valueNode instanceof Document) {
                    children = valueNode.getChildNodes();
                    for (int i = 0; i < children.getLength(); i++) {
                        Node child = children.item(i);
                        node.appendChild(child.cloneNode(true));
                    }
                }
                else {
                    node.appendChild(valueNode.cloneNode(true));
                }
            }
            else {
                String string = (String) TypeUtils.convert(value, String.class);
                if (string != null && !string.equals("")) {
                    Node textNode =
                        node.getOwnerDocument().createTextNode(string);
                    node.appendChild(textNode);
                }
            }
        }
    }
    
    public NodePointer createChild(
        JXPathContext context,
        QName name,
        int index) 
    {
        if (index == WHOLE_COLLECTION) {
            index = 0;
        }
        boolean success =
            getAbstractFactory(context).createObject(
                context,
                this,
                node,
                name.toString(),
                index);
        if (success) {
            NodeTest nodeTest;
            String prefix = name.getPrefix();
            String namespaceURI = prefix != null 
                ? context.getNamespaceURI(prefix) 
                : context.getDefaultNamespaceURI();
            nodeTest = new NodeNameTest(name, namespaceURI);

            NodeIterator it = childIterator(nodeTest, false, null);
            if (it != null && it.setPosition(index + 1)) {
                return it.getNodePointer();
            }
        }
        throw new JXPathAbstractFactoryException(
                "Factory could not create a child node for path: " + asPath()
                        + "/" + name + "[" + (index + 1) + "]");
    }

    public NodePointer createChild(JXPathContext context, 
                QName name, int index, Object value)
    {
        NodePointer ptr = createChild(context, name, index);
        ptr.setValue(value);
        return ptr;
    }

    public NodePointer createAttribute(JXPathContext context, QName name) {
        if (!(node instanceof Element)) {
            return super.createAttribute(context, name);
        }
        Element element = (Element) node;
        String prefix = name.getPrefix();
        if (prefix != null) {
            String ns = getNamespaceURI(prefix);
            if (ns == null) {
                throw new JXPathException(
                    "Unknown namespace prefix: " + prefix);
            }
            element.setAttributeNS(ns, name.toString(), "");
        }
        else {
            if (!element.hasAttribute(name.getName())) {
                element.setAttribute(name.getName(), "");
            }
        }
        NodeIterator it = attributeIterator(name);
        it.setPosition(1);
        return it.getNodePointer();
    }

    public void remove() {
        Node parent = node.getParentNode();
        if (parent == null) {
            throw new JXPathException("Cannot remove root DOM node");
        }
        parent.removeChild(node);
    }

    public String asPath() {
        if (id != null) {
            return "id('" + escape(id) + "')";
        }

        StringBuffer buffer = new StringBuffer();
        if (parent != null) {
            buffer.append(parent.asPath());
        }
        switch (node.getNodeType()) {
            case Node.ELEMENT_NODE :
                // If the parent pointer is not a DOMNodePointer, it is
                // the parent's responsibility to produce the node test part
                // of the path
                if (parent instanceof DOMNodePointer) {
                    if (buffer.length() == 0
                            || buffer.charAt(buffer.length() - 1) != '/') {
                        buffer.append('/');
                    }
                    String ln = DOMNodePointer.getLocalName(node);
                    String nsURI = getNamespaceURI();
                    if (equalStrings(nsURI, 
                            getNamespaceResolver().getDefaultNamespaceURI())) {
                        buffer.append(ln);
                        buffer.append('[');
                        buffer.append(getRelativePositionByName()).append(']');
                    }
                    else {
                        String prefix = getNamespaceResolver().getPrefix(nsURI);
                        if (prefix != null) {
                            buffer.append(prefix);
                            buffer.append(':');
                            buffer.append(ln);
                            buffer.append('[');
                            buffer.append(getRelativePositionByName());
                            buffer.append(']');
                        }
                        else {
                            buffer.append("node()");
                            buffer.append('[');
                            buffer.append(getRelativePositionOfElement());
                            buffer.append(']');
                        }
                    }
                }
            break;
            case Node.TEXT_NODE :
            case Node.CDATA_SECTION_NODE :
                buffer.append("/text()");
                buffer.append('[');
                buffer.append(getRelativePositionOfTextNode()).append(']');
                break;
            case Node.PROCESSING_INSTRUCTION_NODE :
                String target = ((ProcessingInstruction) node).getTarget();
                buffer.append("/processing-instruction(\'");
                buffer.append(target).append("')");
                buffer.append('[');
                buffer.append(getRelativePositionOfPI(target)).append(']');
                break;
            case Node.DOCUMENT_NODE :
                // That'll be empty
        }
        return buffer.toString();
    }

    private String escape(String string) {
        int index = string.indexOf('\'');
        while (index != -1) {
            string =
                string.substring(0, index)
                    + "&apos;"
                    + string.substring(index + 1);
            index = string.indexOf('\'');
        }
        index = string.indexOf('\"');
        while (index != -1) {
            string =
                string.substring(0, index)
                    + "&quot;"
                    + string.substring(index + 1);
            index = string.indexOf('\"');
        }
        return string;
    }

    private int getRelativePositionByName() {
        int count = 1;
        Node n = node.getPreviousSibling();
        while (n != null) {
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                String nm = n.getNodeName();
                if (nm.equals(node.getNodeName())) {
                    count++;
                }
            }
            n = n.getPreviousSibling();
        }
        return count;
    }
    
    private int getRelativePositionOfElement() {
        int count = 1;
        Node n = node.getPreviousSibling();
        while (n != null) {
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                count++;
            }
            n = n.getPreviousSibling();
        }
        return count;
    }

    private int getRelativePositionOfTextNode() {
        int count = 1;
        Node n = node.getPreviousSibling();
        while (n != null) {
            if (n.getNodeType() == Node.TEXT_NODE
                || n.getNodeType() == Node.CDATA_SECTION_NODE) {
                count++;
            }
            n = n.getPreviousSibling();
        }
        return count;
    }

    private int getRelativePositionOfPI(String target) {
        int count = 1;
        Node n = node.getPreviousSibling();
        while (n != null) {
            if (n.getNodeType() == Node.PROCESSING_INSTRUCTION_NODE
                && ((ProcessingInstruction) n).getTarget().equals(target)) {
                count++;
            }
            n = n.getPreviousSibling();
        }
        return count;
    }

    public int hashCode() {
        return System.identityHashCode(node);
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }

        if (!(object instanceof DOMNodePointer)) {
            return false;
        }

        DOMNodePointer other = (DOMNodePointer) object;
        return node == other.node;
    }

    public static String getPrefix(Node node) {
        String prefix = node.getPrefix();
        if (prefix != null) {
            return prefix;
        }

        String name = node.getNodeName();
        int index = name.lastIndexOf(':');
        if (index == -1) {
            return null;
        }

        return name.substring(0, index);
    }

    public static String getLocalName(Node node) {
        String localName = node.getLocalName();
        if (localName != null) {
            return localName;
        }

        String name = node.getNodeName();
        int index = name.lastIndexOf(':');
        if (index == -1) {
            return name;
        }

        return name.substring(index + 1);
    }
    
    public static String getNamespaceURI(Node node) {
        if (node instanceof Document) {
            node = ((Document) node).getDocumentElement();
        }

        Element element = (Element) node;

        String uri = element.getNamespaceURI();
        if (uri != null) {
            return uri;
        }

        String qname;
        String prefix = getPrefix(node);
        if (prefix == null) {
            qname = "xmlns";
        }
        else {
            qname = "xmlns:" + prefix;
        }

        Node aNode = node;
        while (aNode != null) {
            if (aNode.getNodeType() == Node.ELEMENT_NODE) {
                Attr attr = ((Element) aNode).getAttributeNode(qname);
                if (attr != null) {
                    return attr.getValue();
                }
            }
            aNode = aNode.getParentNode();
        }
        return null;
    }

    public Object getValue() {
        return stringValue(node);
    }

    private String stringValue(Node node) {
        int nodeType = node.getNodeType();
        if (nodeType == Node.COMMENT_NODE) {
            String text = ((Comment) node).getData();
            return text == null ? "" : text.trim();
        }
        else if (
            nodeType == Node.TEXT_NODE
                || nodeType == Node.CDATA_SECTION_NODE) {
            String text = node.getNodeValue();
            return text == null ? "" : text.trim();
        }
        else if (nodeType == Node.PROCESSING_INSTRUCTION_NODE) {
            String text = ((ProcessingInstruction) node).getData();
            return text == null ? "" : text.trim();
        }
        else {
            NodeList list = node.getChildNodes();
            StringBuffer buf = new StringBuffer(16);
            for (int i = 0; i < list.getLength(); i++) {
                Node child = list.item(i);
                if (child.getNodeType() == Node.TEXT_NODE) {
                    buf.append(child.getNodeValue());
                }
                else {
                    buf.append(stringValue(child));
                }
            }
            return buf.toString().trim();
        }
    }

    /**
     * Locates a node by ID.
     */
    public Pointer getPointerByID(JXPathContext context, String id) {
        Document document;
        if (node.getNodeType() == Node.DOCUMENT_NODE) {
            document = (Document) node;
        }
        else {
            document = node.getOwnerDocument();
        }
        Element element = document.getElementById(id);
        if (element != null) {
            return new DOMNodePointer(element, getLocale(), id);
        }
        else {
            return new NullPointer(getLocale(), id);
        }
    }

    private AbstractFactory getAbstractFactory(JXPathContext context) {
        AbstractFactory factory = context.getFactory();
        if (factory == null) {
            throw new JXPathException(
                "Factory is not set on the JXPathContext - "
                    + "cannot create path: "
                    + asPath());
        }
        return factory;
    }

    public int compareChildNodePointers(
            NodePointer pointer1, NodePointer pointer2)
    {
        Node node1 = (Node) pointer1.getBaseValue();
        Node node2 = (Node) pointer2.getBaseValue();
        if (node1 == node2) {
            return 0;
        }

        int t1 = node1.getNodeType();
        int t2 = node2.getNodeType();
        if (t1 == Node.ATTRIBUTE_NODE && t2 != Node.ATTRIBUTE_NODE) {
            return -1;
        }
        else if (t1 != Node.ATTRIBUTE_NODE && t2 == Node.ATTRIBUTE_NODE) {
            return 1;
        }
        else if (t1 == Node.ATTRIBUTE_NODE && t2 == Node.ATTRIBUTE_NODE) {
            NamedNodeMap map = ((Node) getNode()).getAttributes();
            int length = map.getLength();
            for (int i = 0; i < length; i++) {
                Node n = map.item(i);
                if (n == node1) {
                    return -1;
                }
                else if (n == node2) {
                    return 1;
                }
            }
            return 0; // Should not happen
        }

        Node current = node.getFirstChild();
        while (current != null) {
            if (current == node1) {
                return -1;
            }
            else if (current == node2) {
                return 1;
            }
            current = current.getNextSibling();
        }

        return 0;
    }
}
```

## Production source src/java/org/apache/commons/jxpath/ri/model/jdom/JDOMNodePointer.java

```java
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.jxpath.ri.model.jdom;

import java.util.List;
import java.util.Locale;

import org.apache.commons.jxpath.AbstractFactory;
import org.apache.commons.jxpath.JXPathAbstractFactoryException;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathException;
import org.apache.commons.jxpath.ri.Compiler;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.apache.commons.jxpath.ri.compiler.NodeTypeTest;
import org.apache.commons.jxpath.ri.compiler.ProcessingInstructionTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.TypeUtils;
import org.jdom.Attribute;
import org.jdom.CDATA;
import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.ProcessingInstruction;
import org.jdom.Text;

/**
 * A Pointer that points to a DOM node.
 *
 * @author Dmitri Plotnikov
 * @version $Revision$ $Date$
 */
public class JDOMNodePointer extends NodePointer {
    private static final long serialVersionUID = -6346532297491082651L;
    
    private Object node;
    private String id;

    public static final String XML_NAMESPACE_URI =
            "http://www.w3.org/XML/1998/namespace";
    public static final String XMLNS_NAMESPACE_URI =
            "http://www.w3.org/2000/xmlns/";

    public JDOMNodePointer(Object node, Locale locale) {
        super(null, locale);
        this.node = node;
    }

    public JDOMNodePointer(Object node, Locale locale, String id) {
        super(null, locale);
        this.node = node;
        this.id = id;
    }

    public JDOMNodePointer(NodePointer parent, Object node) {
        super(parent);
        this.node = node;
    }

    public NodeIterator childIterator(
        NodeTest test,
        boolean reverse,
        NodePointer startWith) 
    {
        return new JDOMNodeIterator(this, test, reverse, startWith);
    }

    public NodeIterator attributeIterator(QName name) {
        return new JDOMAttributeIterator(this, name);
    }

    public NodeIterator namespaceIterator() {
        return new JDOMNamespaceIterator(this);
    }

    public NodePointer namespacePointer(String prefix) {
        return new JDOMNamespacePointer(this, prefix);
    }

    public String getNamespaceURI() {
        return getNamespaceURI(node);
    }
    
    private static String getNamespaceURI(Object node) {
        if (node instanceof Element) {
            Element element = (Element) node;
            String ns = element.getNamespaceURI();
            if (ns != null && ns.equals("")) {
                ns = null;
            }
            return ns;
        }
        return null;
    }

    public String getNamespaceURI(String prefix) {
        if (node instanceof Document) {
            Element element = ((Document)node).getRootElement(); 
            Namespace ns = element.getNamespace(prefix);
            if (ns != null) {
                return ns.getURI();
            }
        }        
        else if (node instanceof Element) {
            Element element = (Element) node;
            Namespace ns = element.getNamespace(prefix);
            if (ns != null) {
                return ns.getURI();
            }
        }
        return null;
    }

    public int compareChildNodePointers(
        NodePointer pointer1,
        NodePointer pointer2) 
    {
        Object node1 = pointer1.getBaseValue();
        Object node2 = pointer2.getBaseValue();
        if (node1 == node2) {
            return 0;
        }

        if ((node1 instanceof Attribute) && !(node2 instanceof Attribute)) {
            return -1;
        }
        else if (
            !(node1 instanceof Attribute) && (node2 instanceof Attribute)) {
            return 1;
        }
        else if (
            (node1 instanceof Attribute) && (node2 instanceof Attribute)) {
            List list = ((Element) getNode()).getAttributes();
            int length = list.size();
            for (int i = 0; i < length; i++) {
                Object n = list.get(i);
                if (n == node1) {
                    return -1;
                }
                else if (n == node2) {
                    return 1;
                }
            }
            return 0; // Should not happen
        }

        if (!(node instanceof Element)) {
            throw new RuntimeException(
                "JXPath internal error: "
                    + "compareChildNodes called for "
                    + node);
        }

        List children = ((Element) node).getContent();
        int length = children.size();
        for (int i = 0; i < length; i++) {
            Object n = children.get(i);
            if (n == node1) {
                return -1;
            }
            else if (n == node2) {
                return 1;
            }
        }

        return 0;
    }


    /**
     * @see org.apache.commons.jxpath.ri.model.NodePointer#getBaseValue()
     */
    public Object getBaseValue() {
        return node;
    }

    public boolean isCollection() {
        return false;
    }
    
    public int getLength() {
        return 1;
    }    

    public boolean isLeaf() {
        if (node instanceof Element) {
            return ((Element) node).getContent().size() == 0;
        }
        else if (node instanceof Document) {
            return ((Document) node).getContent().size() == 0;
        }
        return true;
    }

    /**
     * @see org.apache.commons.jxpath.ri.model.NodePointer#getName()
     */
    public QName getName() {
        String ns = null;
        String ln = null;
        if (node instanceof Element) {
            ns = ((Element) node).getNamespacePrefix();
            if (ns != null && ns.equals("")) {
                ns = null;
            }
            ln = ((Element) node).getName();
        }
        else if (node instanceof ProcessingInstruction) {
            ln = ((ProcessingInstruction) node).getTarget();
        }
        return new QName(ns, ln);
    }

    /**
     * @see org.apache.commons.jxpath.ri.model.NodePointer#getNode()
     */
    public Object getImmediateNode() {
        return node;
    }

    public Object getValue() {
        if (node instanceof Element) {
            return ((Element) node).getTextTrim();
        }
        else if (node instanceof Comment) {
            String text = ((Comment) node).getText();
            if (text != null) {
                text = text.trim();
            }
            return text;
        }
        else if (node instanceof Text) {
            return ((Text) node).getTextTrim();
        }
        else if (node instanceof CDATA) {
            return ((CDATA) node).getTextTrim();
        }
        else if (node instanceof ProcessingInstruction) {
            String text = ((ProcessingInstruction) node).getData();
            if (text != null) {
                text = text.trim();
            }
            return text;
        }
        return null;
    }

    public void setValue(Object value) {
        if (node instanceof Text) {
            String string = (String) TypeUtils.convert(value, String.class);
            if (string != null && !string.equals("")) {
                ((Text) node).setText(string);
            }
            else {
                nodeParent(node).removeContent((Text) node);
            }
        }
        else {
            Element element = (Element) node;
            element.getContent().clear();

            if (value instanceof Element) {
                Element valueElement = (Element) value;
                addContent(valueElement.getContent());
            }
            else if (value instanceof Document) {
                Document valueDocument = (Document) value;
                addContent(valueDocument.getContent());
            }
            else if (value instanceof Text || value instanceof CDATA) {
                String string = ((Text) value).getText();
                element.addContent(new Text(string));
            }
            else if (value instanceof ProcessingInstruction) {
                ProcessingInstruction pi =
                    (ProcessingInstruction) ((ProcessingInstruction) value)
                        .clone();
                element.addContent(pi);
            }
            else if (value instanceof Comment) {
                Comment comment = (Comment) ((Comment) value).clone();
                element.addContent(comment);
            }
            else {
                String string = (String) TypeUtils.convert(value, String.class);
                if (string != null && !string.equals("")) {
                    element.addContent(new Text(string));
                }
            }
        }
    } 
      
    private void addContent(List content) {
        Element element = (Element) node;
        int count = content.size();

        for (int i = 0; i < count; i++) {
            Object child = content.get(i);
            if (child instanceof Element) {
                child = ((Element) child).clone();
                element.addContent((Element) child);
            }
            else if (child instanceof Text) {
                child = ((Text) child).clone();
                element.addContent((Text) child);
            }
            else if (node instanceof CDATA) {
                child = ((CDATA) child).clone();
                element.addContent((CDATA) child);
            }
            else if (node instanceof ProcessingInstruction) {
                child = ((ProcessingInstruction) child).clone();
                element.addContent((ProcessingInstruction) child);
            }
            else if (node instanceof Comment) {
                child = ((Comment) child).clone();
                element.addContent((Comment) child);
            }
        }
    }
    
    public boolean testNode(NodeTest test) {
        return testNode(this, node, test);
    }
    
    public static boolean testNode(
        NodePointer pointer,
        Object node,
        NodeTest test) 
    {
        if (test == null) {
            return true;
        }
        else if (test instanceof NodeNameTest) {
            if (!(node instanceof Element)) {
                return false;
            }

            NodeNameTest nodeNameTest = (NodeNameTest) test;
            QName testName = nodeNameTest.getNodeName();
            String namespaceURI = nodeNameTest.getNamespaceURI();
            boolean wildcard = nodeNameTest.isWildcard();
            String testPrefix = testName.getPrefix();
            if (wildcard && testPrefix == null) {
                return true;
            }

            if (wildcard
                || testName.getName()
                        .equals(JDOMNodePointer.getLocalName(node))) {
                String nodeNS = JDOMNodePointer.getNamespaceURI(node);
                return equalStrings(namespaceURI, nodeNS);
            }

        }
        else if (test instanceof NodeTypeTest) {
            switch (((NodeTypeTest) test).getNodeType()) {
                case Compiler.NODE_TYPE_NODE :
                    return (node instanceof Element) || (node instanceof Document);
                case Compiler.NODE_TYPE_TEXT :
                    return (node instanceof Text) || (node instanceof CDATA);
                case Compiler.NODE_TYPE_COMMENT :
                    return node instanceof Comment;
                case Compiler.NODE_TYPE_PI :
                    return node instanceof ProcessingInstruction;
            }
            return false;
        }
        else if (test instanceof ProcessingInstructionTest) {
            if (node instanceof ProcessingInstruction) {
                String testPI = ((ProcessingInstructionTest) test).getTarget();
                String nodePI = ((ProcessingInstruction) node).getTarget();
                return testPI.equals(nodePI);
            }
        }

        return false;
    }

    private static boolean equalStrings(String s1, String s2) {
        if (s1 == null && s2 != null) {
            return false;
        }
        if (s1 != null && s2 == null) {
            return false;
        }

        if (s1 != null && !s1.trim().equals(s2.trim())) {
            return false;
        }

        return true;
    }

    public static String getPrefix(Object node) {
        if (node instanceof Element) {
            String prefix = ((Element) node).getNamespacePrefix();
            return (prefix == null || prefix.equals("")) ? null : prefix;
        }
        else if (node instanceof Attribute) {
            String prefix = ((Attribute) node).getNamespacePrefix();
            return (prefix == null || prefix.equals("")) ? null : prefix;
        }
        return null;
    }
    
    public static String getLocalName(Object node) {
        if (node instanceof Element) {
            return ((Element) node).getName();
        }
        else if (node instanceof Attribute) {
            return ((Attribute) node).getName();
        }
        return null;
    }

    /**
     * Returns true if the xml:lang attribute for the current node
     * or its parent has the specified prefix <i>lang</i>.
     * If no node has this prefix, calls <code>super.isLanguage(lang)</code>.
     */
    public boolean isLanguage(String lang) {
        String current = getLanguage();
        if (current == null) {
            return super.isLanguage(lang);
        }
        return current.toUpperCase().startsWith(lang.toUpperCase());
    }

    protected String getLanguage() {
        Object n = node;
        while (n != null) {
            if (n instanceof Element) {
                Element e = (Element) n;
                String attr =
                    e.getAttributeValue("lang", Namespace.XML_NAMESPACE);
                if (attr != null && !attr.equals("")) {
                    return attr;
                }
            }
            n = nodeParent(n);
        }
        return null;
    }
    
    private Element nodeParent(Object node) {
        if (node instanceof Element) {
            Object parent = ((Element) node).getParent();
            if (parent instanceof Element) {
                return (Element) parent;
            }
        }
        else if (node instanceof Text) {
            return (Element) ((Text) node).getParent();
        }
        else if (node instanceof CDATA) {
            return (Element) ((CDATA) node).getParent();
        }
        else if (node instanceof ProcessingInstruction) {
            return (Element) ((ProcessingInstruction) node).getParent();
        }
        else if (node instanceof Comment) {
            return (Element) ((Comment) node).getParent();
        }
        return null;
    }

    public NodePointer createChild(
        JXPathContext context,
        QName name,
        int index) 
    {
        if (index == WHOLE_COLLECTION) {
            index = 0;
        }
        boolean success =
            getAbstractFactory(context).createObject(
                context,
                this,
                node,
                name.toString(),
                index);
        if (success) {
            NodeTest nodeTest;
            String prefix = name.getPrefix();
            String namespaceURI = prefix != null 
                ? context.getNamespaceURI(prefix) 
                : context.getDefaultNamespaceURI();
            nodeTest = new NodeNameTest(name, namespaceURI);

            NodeIterator it =
                childIterator(nodeTest, false, null);
            if (it != null && it.setPosition(index + 1)) {
                return it.getNodePointer();
            }
        }
        throw new JXPathAbstractFactoryException("Factory could not create "
                + "a child node for path: " + asPath() + "/" + name + "["
                + (index + 1) + "]");
    }

    public NodePointer createChild(
            JXPathContext context, QName name, int index, Object value)
    {
        NodePointer ptr = createChild(context, name, index);
        ptr.setValue(value);
        return ptr;
    }

    public NodePointer createAttribute(JXPathContext context, QName name) {
        if (!(node instanceof Element)) {
            return super.createAttribute(context, name);
        }

        Element element = (Element) node;
        String prefix = name.getPrefix();
        if (prefix != null) {
            Namespace ns = element.getNamespace(prefix);
            if (ns == null) {
                throw new JXPathException(
                    "Unknown namespace prefix: " + prefix);
            }
            Attribute attr = element.getAttribute(name.getName(), ns);
            if (attr == null) {
                element.setAttribute(name.getName(), "", ns);
            }
        }
        else {
            Attribute attr = element.getAttribute(name.getName());
            if (attr == null) {
                element.setAttribute(name.getName(), "");
            }
        }
        NodeIterator it = attributeIterator(name);
        it.setPosition(1);
        return it.getNodePointer();
    }

    public void remove() {
        Element parent = nodeParent(node);
        if (parent == null) {
            throw new JXPathException("Cannot remove root JDOM node");
        }
        parent.getContent().remove(node);
    }

    public String asPath() {
        if (id != null) {
            return "id('" + escape(id) + "')";
        }

        StringBuffer buffer = new StringBuffer();
        if (parent != null) {
            buffer.append(parent.asPath());
        }
        if (node instanceof Element) {
            // If the parent pointer is not a JDOMNodePointer, it is
            // the parent's responsibility to produce the node test part
            // of the path
            if (parent instanceof JDOMNodePointer) {
                if (buffer.length() == 0
                    || buffer.charAt(buffer.length() - 1) != '/') {
                    buffer.append('/');
                }
                String nsURI = getNamespaceURI();
                String ln = JDOMNodePointer.getLocalName(node);
                
                if (equalStrings(nsURI, 
                        getNamespaceResolver().getDefaultNamespaceURI())) {
                    buffer.append(ln);
                    buffer.append('[');
                    buffer.append(getRelativePositionByName()).append(']');
                }
                else {
                    String prefix = getNamespaceResolver().getPrefix(nsURI);
                    if (prefix != null) {
                        buffer.append(prefix);
                        buffer.append(':');
                        buffer.append(ln);
                        buffer.append('[');
                        buffer.append(getRelativePositionByName());
                        buffer.append(']');
                    }
                    else {
                        buffer.append("node()");
                        buffer.append('[');
                        buffer.append(getRelativePositionOfElement());
                        buffer.append(']');
                    }
                }

            }
        }
        else if (node instanceof Text || node instanceof CDATA) {
            buffer.append("/text()");
            buffer.append('[').append(getRelativePositionOfTextNode()).append(
                ']');
        }
        else if (node instanceof ProcessingInstruction) {
            String target = ((ProcessingInstruction) node).getTarget();
            buffer.append("/processing-instruction(\'").append(target).append(
                "')");
            buffer.append('[').append(getRelativePositionOfPI(target)).append(
                ']');
        }
        return buffer.toString();
    }

    private String escape(String string) {
        int index = string.indexOf('\'');
        while (index != -1) {
            string =
                string.substring(0, index)
                    + "&apos;"
                    + string.substring(index + 1);
            index = string.indexOf('\'');
        }
        index = string.indexOf('\"');
        while (index != -1) {
            string =
                string.substring(0, index)
                    + "&quot;"
                    + string.substring(index + 1);
            index = string.indexOf('\"');
        }
        return string;
    }

    private int getRelativePositionByName() {
        if (node instanceof Element) {
            Object parent = ((Element) node).getParent();
            if (!(parent instanceof Element)) {
                return 1;
            }
            
            List children = ((Element)parent).getContent();
            int count = 0;
            String name = ((Element) node).getQualifiedName();
            for (int i = 0; i < children.size(); i++) {
                Object child = children.get(i);
                if ((child instanceof Element)
                    && ((Element) child).getQualifiedName().equals(name)) {
                    count++;
                }
                if (child == node) {
                    break;
                }
            }
            return count;
        }
        return 1;
    }
    
    private int getRelativePositionOfElement() {
        Object parent = ((Element) node).getParent();
        if (parent == null) {
            return 1;
        }
        List children;
        if (parent instanceof Element) {
            children = ((Element) parent).getContent();
        }
        else {
            children = ((Document) parent).getContent();
        }
        int count = 0;
        for (int i = 0; i < children.size(); i++) {
            Object child = children.get(i);
            if (child instanceof Element) {
                count++;
            }
            if (child == node) {
                break;
            }
        }
        return count;
    }

    private int getRelativePositionOfTextNode() {
        Element parent;
        if (node instanceof Text) {
            parent = (Element) ((Text) node).getParent();
        }
        else {
            parent = (Element) ((CDATA) node).getParent();
        }
        if (parent == null) {
            return 1;
        }
        List children = parent.getContent();
        int count = 0;
        for (int i = 0; i < children.size(); i++) {
            Object child = children.get(i);
            if (child instanceof Text || child instanceof CDATA) {
                count++;
            }
            if (child == node) {
                break;
            }
        }
        return count;
    }

    private int getRelativePositionOfPI(String target) {
        Element parent = (Element) ((ProcessingInstruction) node).getParent();
        if (parent == null) {
            return 1;
        }
        List children = parent.getContent();
        int count = 0;
        for (int i = 0; i < children.size(); i++) {
            Object child = children.get(i);
            if (child instanceof ProcessingInstruction
                && (target == null
                    || target.equals(
                        ((ProcessingInstruction) child).getTarget()))) {
                count++;
            }
            if (child == node) {
                break;
            }
        }
        return count;
    }

    public int hashCode() {
        return System.identityHashCode(node);
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }

        if (!(object instanceof JDOMNodePointer)) {
            return false;
        }

        JDOMNodePointer other = (JDOMNodePointer) object;
        return node == other.node;
    }
    private AbstractFactory getAbstractFactory(JXPathContext context) {
        AbstractFactory factory = context.getFactory();
        if (factory == null) {
            throw new JXPathException(
                "Factory is not set on the JXPathContext - cannot create path: "
                    + asPath());
        }
        return factory;
    }
}
```

## Build configuration build.xml

```text
<?xml version="1.0" encoding="UTF-8"?>

<!--build.xml generated by maven from project.xml version 1.2
  on date June 28 2004, time 1759-->

<project default="jar" name="commons-jxpath" basedir=".">
  <property name="defaulttargetdir" value="${basedir}/target">
  </property>
  <property name="libdir" value="${basedir}/target/lib">
  </property>
  <property name="classes.dir" value="${basedir}/target/classes">
  </property>
  <property name="test.classes.dir" value="${basedir}/target/test-classes">
  </property>
  <property name="test.classes.dir" value="${basedir}/target/test-classes">
  </property>
  <property name="testreportdir" value="${basedir}/target/test-reports">
  </property>
  <property name="distdir" value="dist">
  </property>
  <property name="javadocdir" value="${basedir}/dist/docs/api">
  </property>
  <property name="final.name" value="commons-jxpath">
  </property>
  <target name="init" description="o Initializes some properties">
    <mkdir dir="${libdir}">
    </mkdir>
    <condition property="noget">
      <equals arg2="only" arg1="${build.sysclasspath}">
      </equals>
    </condition>
  </target>
  <target name="compile" description="o Compile the code" depends="get-deps">
    <mkdir dir="${classes.dir}">
    </mkdir>
    <javac destdir="${classes.dir}" target="1.6" source="1.6" deprecation="true" debug="true" optimize="false" excludes="**/package.html">
      <src>
        <pathelement location="${basedir}/src/java">
        </pathelement>
      </src>
      <classpath>
        <fileset dir="${libdir}">
          <include name="*.jar">
          </include>
        </fileset>
      </classpath>
    </javac>
    <copy todir="${test.classes.dir}">
      <fileset dir="src/test">
        <include name="**/*.xml">
        </include>
      </fileset>
    </copy>
  </target>
  <target name="jar" description="o Create the jar" depends="compile,test">
    <jar jarfile="${defaulttargetdir}/${final.name}.jar" excludes="**/package.html" basedir="${classes.dir}">
    </jar>
  </target>
  <target name="clean" description="o Clean up the generated directories">
    <delete dir="${defaulttargetdir}">
    </delete>
    <delete dir="${distdir}">
    </delete>
  </target>
  <target name="dist" description="o Create a distribution" depends="jar, javadoc">
    <mkdir dir="dist">
    </mkdir>
    <copy todir="dist">
      <fileset dir="${defaulttargetdir}" includes="*.jar">
      </fileset>
      <fileset dir="${basedir}" includes="LICENSE*, README*">
      </fileset>
    </copy>
  </target>
  <target name="test" description="o Run the test cases" if="test.failure" depends="internal-test">
    <fail message="There were test failures.">
    </fail>
  </target>
  <target name="internal-test" depends="compile.tests">
    <mkdir dir="${testreportdir}">
    </mkdir>
    <junit dir="./" failureproperty="test.failure" printSummary="yes" fork="true" haltonerror="true">
      <sysproperty key="basedir" value=".">
      </sysproperty>
      <formatter type="xml">
      </formatter>
      <formatter usefile="false" type="plain">
      </formatter>
      <classpath>
        <fileset dir="${libdir}">
          <include name="*.jar">
          </include>
        </fileset>
        <pathelement path="${test.classes.dir}">
        </pathelement>
        <pathelement path="${classes.dir}">
        </pathelement>
      </classpath>
      <batchtest todir="${testreportdir}">
        <fileset dir="src/test">
          <include name="**/*Test.java">
          </include>
        </fileset>
      </batchtest>
    </junit>
  </target>
  <target name="compile.tests" depends="compile">
    <mkdir dir="${test.classes.dir}">
    </mkdir>
    <javac destdir="${test.classes.dir}" target="1.6" source="1.6" deprecation="true" debug="true" optimize="false" excludes="**/package.html">
      <src>
        <pathelement location="${basedir}/src/test">
        </pathelement>
      </src>
      <classpath>
        <fileset dir="${libdir}">
          <include name="*.jar">
          </include>
        </fileset>
        <pathelement path="${classes.dir}">
        </pathelement>
      </classpath>
    </javac>
    <copy todir="${test.classes.dir}">
      <fileset dir="${basedir}/src\test">
        <include name="**/*.xml">
        </include>
      </fileset>
    </copy>
  </target>
  <target name="javadoc" description="o Generate javadoc">
    <mkdir dir="${javadocdir}">
    </mkdir>
    <tstamp>
      <format pattern="2001-yyyy" property="year">
      </format>
    </tstamp>
    <property name="copyright" value="Copyright &amp;copy;  The Apache Software Foundation. All Rights Reserved.">
    </property>
    <property name="title" value="JXPath 1.2 API">
    </property>
    <javadoc use="true" private="true" destdir="${javadocdir}" author="true" version="true" sourcepath="src/java" packagenames="*">
      <classpath>
        <fileset dir="${libdir}">
          <include name="*.jar">
          </include>
        </fileset>
        <pathelement location="${defaulttargetdir}/${final.name}.jar">
        </pathelement>
      </classpath>
    </javadoc>
  </target>
  <target name="get-deps" unless="noget" depends="init">
    <get dest="${libdir}/xerces-1.2.3.jar" usetimestamp="true" ignoreerrors="true" src="file:///home/aomsin/sqa-round2/defects4j/framework/projects/JxPath/lib/xerces/xerces/1.2.3/xerces-1.2.3.jar">
    </get>
    <get dest="${libdir}/servletapi-2.2.jar" usetimestamp="true" ignoreerrors="true" src="file:///home/aomsin/sqa-round2/defects4j/framework/projects/JxPath/lib/servletapi/servletapi/2.2/servletapi-2.2.jar">
    </get>
    <get dest="${libdir}/junit-3.8.jar" usetimestamp="true" ignoreerrors="true" src="file:///home/aomsin/sqa-round2/defects4j/framework/projects/JxPath/lib/junit/junit/3.8/junit-3.8.jar">
    </get>
    <get dest="${libdir}/ant-optional-1.5.1.jar" usetimestamp="true" ignoreerrors="true" src="file:///home/aomsin/sqa-round2/defects4j/framework/projects/JxPath/lib/ant/ant-optional/1.5.1/ant-optional-1.5.1.jar">
    </get>
    <get dest="${libdir}/xml-apis-2.0.2.jar" usetimestamp="true" ignoreerrors="true" src="file:///home/aomsin/sqa-round2/defects4j/framework/projects/JxPath/lib/xml-apis/xml-apis/2.0.2/xml-apis-2.0.2.jar">
    </get>
    <get dest="${libdir}/jdom-1.0.jar" usetimestamp="true" ignoreerrors="true" src="file:///home/aomsin/sqa-round2/defects4j/framework/projects/JxPath/lib/jdom/jdom/1.0/jdom-1.0.jar">
    </get>
    <get dest="${libdir}/commons-beanutils-1.4.jar" usetimestamp="true" ignoreerrors="true" src="file:///home/aomsin/sqa-round2/defects4j/framework/projects/JxPath/lib/commons-beanutils/commons-beanutils/1.4/commons-beanutils-1.4.jar">
    </get>
    <get dest="${libdir}/commons-logging-1.0.4.jar" usetimestamp="true" ignoreerrors="true" src="file:///home/aomsin/sqa-round2/defects4j/framework/projects/JxPath/lib/commons-logging/commons-logging/1.0.4/commons-logging-1.0.4.jar">
    </get>
    <get dest="${libdir}/commons-collections-2.0.jar" usetimestamp="true" ignoreerrors="true" src="file:///home/aomsin/sqa-round2/defects4j/framework/projects/JxPath/lib/commons-collections/commons-collections/2.0/commons-collections-2.0.jar">
    </get>
    <get dest="${libdir}/junit-3.8.1.jar" usetimestamp="true" ignoreerrors="true" src="file:///home/aomsin/sqa-round2/defects4j/framework/projects/JxPath/lib/junit/junit/3.8.1/junit-3.8.1.jar">
    </get>
    <get dest="${libdir}/ant-1.5.jar" usetimestamp="true" ignoreerrors="true" src="file:///home/aomsin/sqa-round2/defects4j/framework/projects/JxPath/lib/ant/ant/1.5/ant-1.5.jar">
    </get>
    <get dest="${libdir}/ant-optional-1.5.jar" usetimestamp="true" ignoreerrors="true" src="file:///home/aomsin/sqa-round2/defects4j/framework/projects/JxPath/lib/ant/ant-optional/1.5/ant-optional-1.5.jar">
    </get>
  </target>
  <target name="install-maven">
    <get dest="${user.home}/maven-install-latest.jar" usetimestamp="true" src="${repo}/maven/maven-install-latest.jar">
    </get>
    <unjar dest="${maven.home}" src="${user.home}/maven-install-latest.jar">
    </unjar>
  </target>
</project>
```

## Build configuration project.properties

```text
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
# 
#      http://www.apache.org/licenses/LICENSE-2.0
# 
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

##
# Properties that override Maven build defaults
##

maven.repo.remote=http://repo1.maven.org/maven

maven.changelog.factory=org.apache.maven.svnlib.SvnChangeLogFactory
maven.changelog.range=120

maven.checkstyle.properties=${basedir}/checkstyle.xml
maven.checkstyle.excludes=**/parser/*
maven.test.failure = false
maven.junit.fork=true
maven.linkcheck.enable=true

maven.compile.source=1.3
maven.compile.target=1.3

# Jar Manifest Additional Attributes
maven.jar.manifest.attributes.list=Implementation-Vendor-Id,X-Compile-Source-JDK,X-Compile-Target-JDK
maven.jar.manifest.attribute.Implementation-Vendor-Id=org.apache
maven.jar.manifest.attribute.X-Compile-Source-JDK=${maven.compile.source}
maven.jar.manifest.attribute.X-Compile-Target-JDK=${maven.compile.target}

# commons site L&F
maven.xdoc.includeProjectDocumentation=no
maven.xdoc.date=left
maven.xdoc.poweredby.image=maven-feather.png
maven.xdoc.version=${pom.currentVersion}
maven.xdoc.developmentProcessUrl=http://jakarta.apache.org/commons/charter.html

# Make the source distro unzip to a different directory
maven.dist.src.assembly.dir=${maven.dist.assembly.dir}/src/${maven.final.name}-src

#maven.proxy.host=
#maven.proxy.port=80
#maven.proxy.username=
#maven.proxy.password=

```
