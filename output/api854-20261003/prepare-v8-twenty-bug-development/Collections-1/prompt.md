Generate a deterministic JUnit 4 Java suite using only the supplied fixed source, build context, shared target declarations and fixture policy. Return complete Java code fences with explicit package declarations, public test classes and imports. Use at most 30 @Test methods. Use meaningful assertions derived from fixed API behavior; avoid non-null-only or empty tests, randomness, time dependence and external services. Do not modify or shadow production code. No assertion repairs or feedback loop. Suites exceeding 30 methods are rejected entirely.

Project: Collections; fixed revision: 1f.
Modified target classes:
org.apache.commons.collections.map.Flat3Map

Fixture policy: common production types in fixed/buggy; simplest supported constructor selected by the shared probe. Use only the listed shared signatures and common fixture types.

Shared target declarations:
```json
[
  {
    "class": "org.apache.commons.collections.map.Flat3Map",
    "constructor_types": "",
    "method": "clear",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.collections.map.Flat3Map",
    "constructor_types": "",
    "method": "clone",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.collections.map.Flat3Map",
    "constructor_types": "",
    "method": "containsKey",
    "parameter_types": "java.lang.Object"
  },
  {
    "class": "org.apache.commons.collections.map.Flat3Map",
    "constructor_types": "",
    "method": "containsValue",
    "parameter_types": "java.lang.Object"
  },
  {
    "class": "org.apache.commons.collections.map.Flat3Map",
    "constructor_types": "",
    "method": "equals",
    "parameter_types": "java.lang.Object"
  },
  {
    "class": "org.apache.commons.collections.map.Flat3Map",
    "constructor_types": "",
    "method": "get",
    "parameter_types": "java.lang.Object"
  },
  {
    "class": "org.apache.commons.collections.map.Flat3Map",
    "constructor_types": "",
    "method": "isEmpty",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.collections.map.Flat3Map",
    "constructor_types": "",
    "method": "put",
    "parameter_types": "java.lang.Object,java.lang.Object"
  },
  {
    "class": "org.apache.commons.collections.map.Flat3Map",
    "constructor_types": "",
    "method": "putAll",
    "parameter_types": "java.util.Map"
  },
  {
    "class": "org.apache.commons.collections.map.Flat3Map",
    "constructor_types": "",
    "method": "remove",
    "parameter_types": "java.lang.Object"
  },
  {
    "class": "org.apache.commons.collections.map.Flat3Map",
    "constructor_types": "",
    "method": "size",
    "parameter_types": ""
  },
  {
    "class": "org.apache.commons.collections.map.Flat3Map",
    "constructor_types": "",
    "method": "toString",
    "parameter_types": ""
  }
]
```

Common compiled production fixture classes (eligibility only, not oracle approval):
```json
[
  "org.apache.commons.collections.ArrayStack",
  "org.apache.commons.collections.Bag",
  "org.apache.commons.collections.BagUtils",
  "org.apache.commons.collections.BeanMap",
  "org.apache.commons.collections.BidiMap",
  "org.apache.commons.collections.BinaryHeap",
  "org.apache.commons.collections.BoundedCollection",
  "org.apache.commons.collections.BoundedFifoBuffer",
  "org.apache.commons.collections.BoundedMap",
  "org.apache.commons.collections.Buffer",
  "org.apache.commons.collections.BufferOverflowException",
  "org.apache.commons.collections.BufferUnderflowException",
  "org.apache.commons.collections.BufferUtils",
  "org.apache.commons.collections.Closure",
  "org.apache.commons.collections.ClosureUtils",
  "org.apache.commons.collections.CollectionUtils",
  "org.apache.commons.collections.ComparatorUtils",
  "org.apache.commons.collections.CursorableLinkedList",
  "org.apache.commons.collections.CursorableSubList",
  "org.apache.commons.collections.DefaultMapBag",
  "org.apache.commons.collections.DefaultMapEntry",
  "org.apache.commons.collections.DoubleOrderedMap",
  "org.apache.commons.collections.EnumerationUtils",
  "org.apache.commons.collections.ExtendedProperties",
  "org.apache.commons.collections.Factory",
  "org.apache.commons.collections.FactoryUtils",
  "org.apache.commons.collections.FastArrayList",
  "org.apache.commons.collections.FastHashMap",
  "org.apache.commons.collections.FastTreeMap",
  "org.apache.commons.collections.FunctorException",
  "org.apache.commons.collections.HashBag",
  "org.apache.commons.collections.IterableMap",
  "org.apache.commons.collections.IteratorUtils",
  "org.apache.commons.collections.KeyValue",
  "org.apache.commons.collections.LRUMap",
  "org.apache.commons.collections.ListUtils",
  "org.apache.commons.collections.MapIterator",
  "org.apache.commons.collections.MapUtils",
  "org.apache.commons.collections.MultiHashMap",
  "org.apache.commons.collections.MultiMap",
  "org.apache.commons.collections.OrderedBidiMap",
  "org.apache.commons.collections.OrderedIterator",
  "org.apache.commons.collections.OrderedMap",
  "org.apache.commons.collections.OrderedMapIterator",
  "org.apache.commons.collections.Predicate",
  "org.apache.commons.collections.PredicateUtils",
  "org.apache.commons.collections.PriorityQueue",
  "org.apache.commons.collections.ProxyMap",
  "org.apache.commons.collections.ReferenceMap",
  "org.apache.commons.collections.ResettableIterator",
  "org.apache.commons.collections.ResettableListIterator",
  "org.apache.commons.collections.SequencedHashMap",
  "org.apache.commons.collections.SetUtils",
  "org.apache.commons.collections.SortedBag",
  "org.apache.commons.collections.SortedBidiMap",
  "org.apache.commons.collections.StaticBucketMap",
  "org.apache.commons.collections.SynchronizedPriorityQueue",
  "org.apache.commons.collections.Transformer",
  "org.apache.commons.collections.TransformerUtils",
  "org.apache.commons.collections.TreeBag",
  "org.apache.commons.collections.UnboundedFifoBuffer",
  "org.apache.commons.collections.Unmodifiable",
  "org.apache.commons.collections.bag.AbstractBagDecorator",
  "org.apache.commons.collections.bag.AbstractMapBag",
  "org.apache.commons.collections.bag.AbstractSortedBagDecorator",
  "org.apache.commons.collections.bag.HashBag",
  "org.apache.commons.collections.bag.PredicatedBag",
  "org.apache.commons.collections.bag.PredicatedSortedBag",
  "org.apache.commons.collections.bag.SynchronizedBag",
  "org.apache.commons.collections.bag.SynchronizedSortedBag",
  "org.apache.commons.collections.bag.TransformedBag",
  "org.apache.commons.collections.bag.TransformedSortedBag",
  "org.apache.commons.collections.bag.TreeBag",
  "org.apache.commons.collections.bag.TypedBag",
  "org.apache.commons.collections.bag.TypedSortedBag",
  "org.apache.commons.collections.bag.UnmodifiableBag",
  "org.apache.commons.collections.bag.UnmodifiableSortedBag",
  "org.apache.commons.collections.bidimap.AbstractBidiMapDecorator",
  "org.apache.commons.collections.bidimap.AbstractDualBidiMap",
  "org.apache.commons.collections.bidimap.AbstractOrderedBidiMapDecorator",
  "org.apache.commons.collections.bidimap.AbstractSortedBidiMapDecorator",
  "org.apache.commons.collections.bidimap.DualHashBidiMap",
  "org.apache.commons.collections.bidimap.DualTreeBidiMap",
  "org.apache.commons.collections.bidimap.TreeBidiMap",
  "org.apache.commons.collections.bidimap.UnmodifiableBidiMap",
  "org.apache.commons.collections.bidimap.UnmodifiableOrderedBidiMap",
  "org.apache.commons.collections.bidimap.UnmodifiableSortedBidiMap",
  "org.apache.commons.collections.buffer.AbstractBufferDecorator",
  "org.apache.commons.collections.buffer.BlockingBuffer",
  "org.apache.commons.collections.buffer.BoundedBuffer",
  "org.apache.commons.collections.buffer.BoundedFifoBuffer",
  "org.apache.commons.collections.buffer.CircularFifoBuffer",
  "org.apache.commons.collections.buffer.PredicatedBuffer",
  "org.apache.commons.collections.buffer.PriorityBuffer",
  "org.apache.commons.collections.buffer.SynchronizedBuffer",
  "org.apache.commons.collections.buffer.TransformedBuffer",
  "org.apache.commons.collections.buffer.TypedBuffer",
  "org.apache.commons.collections.buffer.UnboundedFifoBuffer",
  "org.apache.commons.collections.buffer.UnmodifiableBuffer",
  "org.apache.commons.collections.collection.AbstractCollectionDecorator",
  "org.apache.commons.collections.collection.AbstractSerializableCollectionDecorator",
  "org.apache.commons.collections.collection.CompositeCollection",
  "org.apache.commons.collections.collection.PredicatedCollection",
  "org.apache.commons.collections.collection.SynchronizedCollection",
  "org.apache.commons.collections.collection.TransformedCollection",
  "org.apache.commons.collections.collection.TypedCollection",
  "org.apache.commons.collections.collection.UnmodifiableBoundedCollection",
  "org.apache.commons.collections.collection.UnmodifiableCollection",
  "org.apache.commons.collections.comparators.BooleanComparator",
  "org.apache.commons.collections.comparators.ComparableComparator",
  "org.apache.commons.collections.comparators.ComparatorChain",
  "org.apache.commons.collections.comparators.FixedOrderComparator",
  "org.apache.commons.collections.comparators.NullComparator",
  "org.apache.commons.collections.comparators.ReverseComparator",
  "org.apache.commons.collections.comparators.TransformingComparator",
  "org.apache.commons.collections.functors.AllPredicate",
  "org.apache.commons.collections.functors.AndPredicate",
  "org.apache.commons.collections.functors.AnyPredicate",
  "org.apache.commons.collections.functors.ChainedClosure",
  "org.apache.commons.collections.functors.ChainedTransformer",
  "org.apache.commons.collections.functors.CloneTransformer",
  "org.apache.commons.collections.functors.ClosureTransformer",
  "org.apache.commons.collections.functors.ConstantFactory",
  "org.apache.commons.collections.functors.ConstantTransformer",
  "org.apache.commons.collections.functors.EqualPredicate",
  "org.apache.commons.collections.functors.ExceptionClosure",
  "org.apache.commons.collections.functors.ExceptionFactory",
  "org.apache.commons.collections.functors.ExceptionPredicate",
  "org.apache.commons.collections.functors.ExceptionTransformer",
  "org.apache.commons.collections.functors.FactoryTransformer",
  "org.apache.commons.collections.functors.FalsePredicate",
  "org.apache.commons.collections.functors.ForClosure",
  "org.apache.commons.collections.functors.FunctorUtils",
  "org.apache.commons.collections.functors.IdentityPredicate",
  "org.apache.commons.collections.functors.IfClosure",
  "org.apache.commons.collections.functors.InstanceofPredicate",
  "org.apache.commons.collections.functors.InstantiateFactory",
  "org.apache.commons.collections.functors.InstantiateTransformer",
  "org.apache.commons.collections.functors.InvokerTransformer",
  "org.apache.commons.collections.functors.MapTransformer",
  "org.apache.commons.collections.functors.NOPClosure",
  "org.apache.commons.collections.functors.NOPTransformer",
  "org.apache.commons.collections.functors.NonePredicate",
  "org.apache.commons.collections.functors.NotNullPredicate",
  "org.apache.commons.collections.functors.NotPredicate",
  "org.apache.commons.collections.functors.NullIsExceptionPredicate",
  "org.apache.commons.collections.functors.NullIsFalsePredicate",
  "org.apache.commons.collections.functors.NullIsTruePredicate",
  "org.apache.commons.collections.functors.NullPredicate",
  "org.apache.commons.collections.functors.OnePredicate",
  "org.apache.commons.collections.functors.OrPredicate",
  "org.apache.commons.collections.functors.PredicateDecorator",
  "org.apache.commons.collections.functors.PredicateTransformer",
  "org.apache.commons.collections.functors.PrototypeFactory",
  "org.apache.commons.collections.functors.StringValueTransformer",
  "org.apache.commons.collections.functors.SwitchClosure",
  "org.apache.commons.collections.functors.SwitchTransformer",
  "org.apache.commons.collections.functors.TransformedPredicate",
  "org.apache.commons.collections.functors.TransformerClosure",
  "org.apache.commons.collections.functors.TransformerPredicate",
  "org.apache.commons.collections.functors.TruePredicate",
  "org.apache.commons.collections.functors.UniquePredicate",
  "org.apache.commons.collections.functors.WhileClosure",
  "org.apache.commons.collections.iterators.AbstractEmptyIterator",
  "org.apache.commons.collections.iterators.AbstractIteratorDecorator",
  "org.apache.commons.collections.iterators.AbstractListIteratorDecorator",
  "org.apache.commons.collections.iterators.AbstractMapIteratorDecorator",
  "org.apache.commons.collections.iterators.AbstractOrderedMapIteratorDecorator",
  "org.apache.commons.collections.iterators.ArrayIterator",
  "org.apache.commons.collections.iterators.ArrayListIterator",
  "org.apache.commons.collections.iterators.CollatingIterator",
  "org.apache.commons.collections.iterators.EmptyIterator",
  "org.apache.commons.collections.iterators.EmptyListIterator",
  "org.apache.commons.collections.iterators.EmptyMapIterator",
  "org.apache.commons.collections.iterators.EmptyOrderedIterator",
  "org.apache.commons.collections.iterators.EmptyOrderedMapIterator",
  "org.apache.commons.collections.iterators.EntrySetMapIterator",
  "org.apache.commons.collections.iterators.EnumerationIterator",
  "org.apache.commons.collections.iterators.FilterIterator",
  "org.apache.commons.collections.iterators.FilterListIterator",
  "org.apache.commons.collections.iterators.IteratorChain",
  "org.apache.commons.collections.iterators.IteratorEnumeration",
  "org.apache.commons.collections.iterators.ListIteratorWrapper",
  "org.apache.commons.collections.iterators.LoopingIterator",
  "org.apache.commons.collections.iterators.LoopingListIterator",
  "org.apache.commons.collections.iterators.ObjectArrayIterator",
  "org.apache.commons.collections.iterators.ObjectArrayListIterator",
  "org.apache.commons.collections.iterators.ObjectGraphIterator",
  "org.apache.commons.collections.iterators.ProxyIterator",
  "org.apache.commons.collections.iterators.ProxyListIterator",
  "org.apache.commons.collections.iterators.ReverseListIterator",
  "org.apache.commons.collections.iterators.SingletonIterator",
  "org.apache.commons.collections.iterators.SingletonListIterator",
  "org.apache.commons.collections.iterators.TransformIterator",
  "org.apache.commons.collections.iterators.UniqueFilterIterator",
  "org.apache.commons.collections.iterators.UnmodifiableIterator",
  "org.apache.commons.collections.iterators.UnmodifiableListIterator",
  "org.apache.commons.collections.iterators.UnmodifiableMapIterator",
  "org.apache.commons.collections.iterators.UnmodifiableOrderedMapIterator",
  "org.apache.commons.collections.keyvalue.AbstractKeyValue",
  "org.apache.commons.collections.keyvalue.AbstractMapEntry",
  "org.apache.commons.collections.keyvalue.AbstractMapEntryDecorator",
  "org.apache.commons.collections.keyvalue.DefaultKeyValue",
  "org.apache.commons.collections.keyvalue.DefaultMapEntry",
  "org.apache.commons.collections.keyvalue.MultiKey",
  "org.apache.commons.collections.keyvalue.TiedMapEntry",
  "org.apache.commons.collections.keyvalue.UnmodifiableMapEntry",
  "org.apache.commons.collections.list.AbstractLinkedList",
  "org.apache.commons.collections.list.AbstractListDecorator",
  "org.apache.commons.collections.list.AbstractSerializableListDecorator",
  "org.apache.commons.collections.list.CursorableLinkedList",
  "org.apache.commons.collections.list.FixedSizeList",
  "org.apache.commons.collections.list.GrowthList",
  "org.apache.commons.collections.list.LazyList",
  "org.apache.commons.collections.list.NodeCachingLinkedList",
  "org.apache.commons.collections.list.PredicatedList",
  "org.apache.commons.collections.list.SetUniqueList",
  "org.apache.commons.collections.list.SynchronizedList",
  "org.apache.commons.collections.list.TransformedList",
  "org.apache.commons.collections.list.TreeList",
  "org.apache.commons.collections.list.TypedList",
  "org.apache.commons.collections.list.UnmodifiableList",
  "org.apache.commons.collections.map.AbstractHashedMap",
  "org.apache.commons.collections.map.AbstractInputCheckedMapDecorator",
  "org.apache.commons.collections.map.AbstractLinkedMap",
  "org.apache.commons.collections.map.AbstractMapDecorator",
  "org.apache.commons.collections.map.AbstractOrderedMapDecorator",
  "org.apache.commons.collections.map.AbstractReferenceMap",
  "org.apache.commons.collections.map.AbstractSortedMapDecorator",
  "org.apache.commons.collections.map.CaseInsensitiveMap",
  "org.apache.commons.collections.map.CompositeMap",
  "org.apache.commons.collections.map.DefaultedMap",
  "org.apache.commons.collections.map.FixedSizeMap",
  "org.apache.commons.collections.map.FixedSizeSortedMap",
  "org.apache.commons.collections.map.Flat3Map",
  "org.apache.commons.collections.map.HashedMap",
  "org.apache.commons.collections.map.IdentityMap",
  "org.apache.commons.collections.map.LRUMap",
  "org.apache.commons.collections.map.LazyMap",
  "org.apache.commons.collections.map.LazySortedMap",
  "org.apache.commons.collections.map.LinkedMap",
  "org.apache.commons.collections.map.ListOrderedMap",
  "org.apache.commons.collections.map.MultiKeyMap",
  "org.apache.commons.collections.map.MultiValueMap",
  "org.apache.commons.collections.map.PredicatedMap",
  "org.apache.commons.collections.map.PredicatedSortedMap",
  "org.apache.commons.collections.map.ReferenceIdentityMap",
  "org.apache.commons.collections.map.ReferenceMap",
  "org.apache.commons.collections.map.SingletonMap",
  "org.apache.commons.collections.map.StaticBucketMap",
  "org.apache.commons.collections.map.TransformedMap",
  "org.apache.commons.collections.map.TransformedSortedMap",
  "org.apache.commons.collections.map.TypedMap",
  "org.apache.commons.collections.map.TypedSortedMap",
  "org.apache.commons.collections.map.UnmodifiableEntrySet",
  "org.apache.commons.collections.map.UnmodifiableMap",
  "org.apache.commons.collections.map.UnmodifiableOrderedMap",
  "org.apache.commons.collections.map.UnmodifiableSortedMap",
  "org.apache.commons.collections.set.AbstractSerializableSetDecorator",
  "org.apache.commons.collections.set.AbstractSetDecorator",
  "org.apache.commons.collections.set.AbstractSortedSetDecorator",
  "org.apache.commons.collections.set.CompositeSet",
  "org.apache.commons.collections.set.ListOrderedSet",
  "org.apache.commons.collections.set.MapBackedSet",
  "org.apache.commons.collections.set.PredicatedSet",
  "org.apache.commons.collections.set.PredicatedSortedSet",
  "org.apache.commons.collections.set.SynchronizedSet",
  "org.apache.commons.collections.set.SynchronizedSortedSet",
  "org.apache.commons.collections.set.TransformedSet",
  "org.apache.commons.collections.set.TransformedSortedSet",
  "org.apache.commons.collections.set.TypedSet",
  "org.apache.commons.collections.set.TypedSortedSet",
  "org.apache.commons.collections.set.UnmodifiableSet",
  "org.apache.commons.collections.set.UnmodifiableSortedSet"
]
```

Reach the target with meaningful domain arguments. Do not substitute constructor exceptions, null-only inputs or empty collections for behavior assertions. No execution feedback or repair loop.

Explicit fixture policy: beam-explicit-fixtures-v6-development. Use the reviewed capability recipes below instead of legacy recursive/null construction.
## build.xml

```
 <!--
   Copyright 2001-2006 The Apache Software Foundation

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
<project name="commons-collections" default="compile" basedir=".">

<!-- ========== Properties ================================================ -->

  <!-- This can be used to define 'junit.jar' property if necessary -->
  <property file="build.properties"/>

<!-- ========== Component Declarations ==================================== -->

  <!-- The name of this component -->
  <property name="component.name"          value="commons-collections"/>

  <!-- The primary package name of this component -->
  <property name="component.package"       value="org.apache.commons.collections"/>

  <!-- The short title of this component -->
  <property name="component.title"         value="Commons Collections"/>

  <!-- The full title of this component -->
  <property name="component.title.full"    value="Apache Jakarta Commons Collections"/>

  <!-- The current version number of this component -->
  <property name="component.version"       value="3.3-SNAPSHOT"/>

  <!-- The base directory for component configuration files -->
  <property name="source.conf"               value="src/conf"/>

  <!-- The base directory for component sources -->
  <property name="source.java"             value="src/java"/>

  <!-- The base directory for unit test sources -->
  <property name="source.test"             value="src/test"/>

  <!-- The directories for compilation targets -->
  <property name="build.home"              value="build"/>
  <property name="build.conf"              value="${build.home}/conf"/>
  <property name="build.classes"           value="${build.home}/classes"/>
  <property name="build.tests"             value="${build.home}/tests"/>
  <property name="build.docs"              value="${build.home}/docs/apidocs"/>
  <property name="build.src"               value="${build.home}/src-ide" />
  
  <!-- The name/location of the jar file to build -->
  <property name="final.name"           value="${component.name}-${component.version}"/>
  <property name="jar.name"             value="${final.name}.jar"/>
  <property name="build.jar.name"       value="${build.home}/${jar.name}"/>
  
  <!-- The name/location of the zip files to build -->
  <property name="build.dist.bin"       value="${build.home}/bin"/>
  <property name="build.dist.bin.work"  value="${build.dist.bin}/${component.name}-${component.version}"/>
  <property name="build.dist.src"       value="${build.home}/src"/>
  <property name="build.dist.src.work"  value="${build.dist.src}/${component.name}-${component.version}-src"/>
  <property name="build.dist"           value="${build.home}/dist"/>
  <property name="build.bin.tar.name"   value="${build.dist}/${component.name}-${component.version}.tar"/>
  <property name="build.bin.gz.name"    value="${build.dist}/${component.name}-${component.version}.tar.gz"/>
  <property name="build.bin.zip.name"   value="${build.dist}/${component.name}-${component.version}.zip"/>
  <property name="build.src.tar.name"   value="${build.dist}/${component.name}-${component.version}-src.tar"/>
  <property name="build.src.gz.name"    value="${build.dist}/${component.name}-${component.version}-src.tar.gz"/>
  <property name="build.src.zip.name"   value="${build.dist}/${component.name}-${component.version}-src.zip"/>
  <property name="dist.home"            value="dist"/> <!-- for nightly builds -->


<!-- ========== Settings ================================================== -->

  <!-- Javac -->
  <property name="compile.debug"           value="true"/>
  <property name="compile.deprecation"     value="true"/>
  <property name="compile.optimize"        value="false"/>

  <!-- Javadoc -->
  <property name="javadoc.access"          value="protected"/>
  <property name="javadoc.links"           value="http://java.sun.com/j2se/1.3/docs/api/"/>

  <!-- JUnit -->
  <property name="test.failonerror"        value="true"/>

  <!-- Maven -->
  <property name="maven.repo"  value="${user.home}/.maven/repository" />


<!-- ====================================================================== -->
<!-- ========== Executable Targets ======================================== -->
<!-- ====================================================================== -->

  <target name="clean"
          description="Clean build and distribution directories">
    <delete dir="${build.home}"/>
  </target>

<!-- ====================================================================== -->

  <target name="init"
          description="Initialize and evaluate conditionals">
    <echo message="-------- ${component.name} ${component.version} --------"/>
  </target>

<!-- ====================================================================== -->

  <target name="prepare" depends="init"
          description="Prepare build directory">
    <mkdir dir="${build.home}"/>
  </target>

<!-- ====================================================================== -->

  <target name="compile" depends="prepare"
          description="Compile main code">
    <mkdir dir="${build.classes}"/>
    <javac  srcdir="${source.java}" target="1.6" source="1.6"
           destdir="${build.classes}"
             debug="${compile.debug}"
       deprecation="${compile.deprecation}"
          optimize="${compile.optimize}">
    </javac>
  </target>

<!-- ====================================================================== -->

  <target name="jar" depends="compile"
          description="Create jar">
    <mkdir      dir="${build.classes}/META-INF"/>
    <copy      file="LICENSE.txt"
             tofile="${build.classes}/META-INF/LICENSE.txt"/>
    <copy      file="NOTICE.txt"
             tofile="${build.classes}/META-INF/NOTICE.txt"/>
             
    <tstamp/>
    <mkdir      dir="${build.conf}"/>
    <copy     todir="${build.conf}" filtering="on">
      <filterset>
        <filter token="name"     value="${component.name}"/>
        <filter token="title"    value="${component.title}"/>
        <filter token="package"  value="${component.package}"/>
        <filter token="version"  value="${component.version}"/>
      </filterset>
      <fileset dir="${source.conf}" includes="*.MF"/>
    </copy>
             
    <!-- NOTE: A jar built using JDK1.4 is incompatible with JDK1.2 -->
    <jar    jarfile="${build.jar.name}"
            basedir="${build.classes}"
           manifest="${build.conf}/MANIFEST.MF"/>
  </target>

<!-- ====================================================================== -->
  <!-- Targets you might use to get smaller jar files - not recommended -->

  <target name="splitjar" depends="jar"
          description="Create split jar">
    <jar    jarfile="${build.home}/${component.name}-bag-${component.version}.jar"
            basedir="${build.classes}"
           manifest="${build.conf}/MANIFEST.MF">
      <include name="**/META-INF/*"/>
      <include name="**/BagUtils*.class"/>
      <include name="**/bag/*.class"/>
    </jar>
    <jar    jarfile="${build.home}/${component.name}-bidimap-${component.version}.jar"
            basedir="${build.classes}"
           manifest="${build.conf}/MANIFEST.MF">
      <include name="**/META-INF/*"/>
      <include name="**/bidimap/*.class"/>
    </jar>
    <jar    jarfile="${build.home}/${component.name}-buffer-${component.version}.jar"
            basedir="${build.classes}"
           manifest="${build.conf}/MANIFEST.MF">
      <include name="**/META-INF/*"/>
      <include name="**/BufferUtils*.class"/>
      <include name="**/buffer/*.class"/>
    </jar>
    <jar    jarfile="${build.home}/${component.name}-functors-${component.version}.jar"
            basedir="${build.classes}"
           manifest="${build.conf}/MANIFEST.MF">
      <include name="**/META-INF/*"/>
      <include name="**/ClosureUtils*.class"/>
      <include name="**/FactoryUtils*.class"/>
      <include name="**/PredicateUtils*.class"/>
      <include name="**/TransformerUtils*.class"/>
      <include name="**/functors/*.class"/>
    </jar>
    <jar    jarfile="${build.home}/${component.name}-core-${component.version}.jar"
            basedir="${build.classes}"
           manifest="${build.conf}/MANIFEST.MF">
      <include name="**/META-INF/*"/>
      <include name="**/*"/>
      <exclude name="**/BagUtils*.class"/>
      <exclude name="**/BufferUtils*.class"/>
      <exclude name="**/ClosureUtils*.class"/>
      <exclude name="**/FactoryUtils*.class"/>
      <exclude name="**/PredicateUtils*.class"/>
      <exclude name="**/TransformerUtils*.class"/>
      <exclude name="**/bag/*.class"/>
      <exclude name="**/bidimap/*.class"/>
      <exclude name="**/buffer/*.class"/>
      <exclude name="**/functors/*.class"/>
      <exclude name="**/iterators/ProxyIterator*.class"/>
      <exclude name="**/iterators/ProxyListIterator*.class"/>
      <exclude name="**/map/*.class"/>
      <exclude name="org/apache/commons/collections/BeanMap*.class"/>
      <exclude name="org/apache/commons/collections/BinaryHeap*.class"/>
      <exclude name="org/apache/commons/collections/BoundedFifoBuffer*.class"/>
      <exclude name="org/apache/commons/collections/CursorableLinkedList*.class"/>
      <exclude name="org/apache/commons/collections/CursorableSubList*.class"/>
      <exclude name="org/apache/commons/collections/DefaultMapBag*.class"/>
      <exclude name="org/apache/commons/collections/DefaultMapEntry*.class"/>
      <exclude name="org/apache/commons/collections/DoubleOrderedMap*.class"/>
      <exclude name="org/apache/commons/collections/HashBag*.class"/>
      <exclude name="org/apache/commons/collections/LRUMap*.class"/>
      <exclude name="org/apache/commons/collections/MultiHashMap*.class"/>
      <exclude name="org/apache/commons/collections/PriorityQueue*.class"/>
      <exclude name="org/apache/commons/collections/ProxyMap*.class"/>
      <exclude name="org/apache/commons/collections/ReferenceMap*.class"/>
      <exclude name="org/apache/commons/collections/SequencedHashMap*.class"/>
      <exclude name="org/apache/commons/collections/StaticBucketMap*.class"/>
      <exclude name="org/apache/commons/collections/SynchronizedPriorityQueue*.class"/>
      <exclude name="org/apache/commons/collections/TreeBag*.class"/>
      <exclude name="org/apache/commons/collections/UnboundedFifoBuffer*.class"/>
    </jar>
    <jar    jarfile="${build.home}/${component.name}-deprecated-${component.version}.jar"
            basedir="${build.classes}"
           manifest="${build.conf}/MANIFEST.MF">
      <include name="**/META-INF/*"/>
      <include name="**/iterators/ProxyIterator*.class"/>
      <include name="**/iterators/ProxyListIterator*.class"/>
      <include name="org/apache/commons/collections/BeanMap*.class"/>
      <include name="org/apache/commons/collections/BinaryHeap*.class"/>
      <include name="org/apache/commons/collections/BoundedFifoBuffer*.class"/>
      <include name="org/apache/commons/collections/CursorableLinkedList*.class"/>
      <include name="org/apache/commons/collections/CursorableSubList*.class"/>
      <include name="org/apache/commons/collections/DefaultMapBag*.class"/>
      <include name="org/apache/commons/collections/DefaultMapEntry*.class"/>
      <include name="org/apache/commons/collections/DoubleOrderedMap*.class"/>
      <include name="org/apache/commons/collections/HashBag*.class"/>
      <include name="org/apache/commons/collections/LRUMap*.class"/>
      <include name="org/apache/commons/collections/MultiHashMap*.class"/>
      <include name="org/apache/commons/collections/PriorityQueue*.class"/>
      <include name="org/apache/commons/collections/ProxyMap*.class"/>
      <include name="org/apache/commons/collections/ReferenceMap*.class"/>
      <include name="org/apache/commons/collections/SequencedHashMap*.class"/>
      <include name="org/apache/commons/collections/StaticBucketMap*.class"/>
      <include name="org/apache/commons/collections/SynchronizedPriorityQueue*.class"/>
      <include name="org/apache/commons/collections/TreeBag*.class"/>
      <include name="org/apache/commons/collections/UnboundedFifoBuffer*.class"/>
    </jar>

  </target>

<!-- ====================================================================== -->

  <target name="compile.tests" depends="compile"
          description="Compile unit test cases">
    <mkdir dir="${build.tests}"/>
    <javac  srcdir="${source.test}" target="1.6" source="1.6"
           destdir="${build.tests}"
             debug="true"
       deprecation="false"
          optimize="false">
      <classpath>
        <pathelement location="${build.classes}"/>
        <pathelement location="${junit.jar}"/>
      </classpath>
    </javac>
  </target>

<!-- ====================================================================== -->

  <!-- Tests collections, either running all or one test -->
  <target name="test" depends="-test-all,-test-single"
          description="Run unit tests" />

  <!-- Runs all tests -->
  <target name="-test-all" depends="compile.tests" unless="testcase">
    <junit printsummary="yes" haltonfailure="yes" showoutput="yes">
      <formatter type="brief" />
      <classpath>
        <pathelement location="${build.classes}"/>
        <pathelement location="${build.tests}"/>
        <pathelement location="${junit.jar}"/>
      </classpath>

      <batchtest fork="yes">
        <fileset dir="${source.test}">
          <include name="**/Test*.java"/>
          <exclude name="**/TestAll*.java"/>
          <exclude name="**/TestAbstract*"/>
          <exclude name="**/TestArrayList.java"/>
          <exclude name="**/TestLinkedList.java"/>
          <exclude name="**/TestHashMap.java"/>
          <exclude name="**/TestTreeMap.java"/>
          <exclude name="**/TestTypedCollection.java"/>
        </fileset>
        <formatter type="brief" usefile="false" />
      </batchtest>
    </junit>
  </target>

  <!-- Runs a single test -->
  <target name="-test-single" depends="compile.tests" if="testcase">
    <junit printsummary="yes" haltonfailure="yes" showoutput="yes">
      <formatter type="brief" />
      <classpath>
        <pathelement location="${build.classes}"/>
        <pathelement location="${build.tests}"/>
        <pathelement location="${junit.jar}"/>
      </classpath>

      <test name="${testcase}" fork="yes">
        <formatter type="brief" usefile="false" />
      </test>
    </junit>
  </target>

<!-- ====================================================================== -->

  <target name="testjar"  depends="compile.tests,jar"
          description="Run all unit test cases">
    <echo message="Running collections tests against built jar ..."/>
    <junit printsummary="yes" haltonfailure="yes">
      <classpath>
        <pathelement location="${build.jar.name}"/>
        <pathelement location="${build.tests}"/>
        <pathelement location="${junit.jar}"/>
      </classpath>

      <batchtest fork="yes">
        <fileset dir="${source.test}">
          <include name="**/TestAllPackages.java"/>
        </fileset>
        <formatter type="brief" usefile="false" />
      </batchtest>
    </junit>
  </target>

<!-- ====================================================================== -->

  <target name="javadoc" depends="prepare"
          description="Create component Javadoc documentation">
    <tstamp><format property="year" pattern="yyyy"/></tstamp>
    <delete     dir="${build.docs}"/>
    <mkdir      dir="${build.docs}"/>
    <javadoc sourcepath="${source.java}"
                destdir="${build.docs}"
           packagenames="${component.package}.*"
                 access="${javadoc.access}"
                 author="true"
                version="true"
                    use="true"
                   link="${javadoc.links}"
               overview="${source.java}/org/apache/commons/collections/overview.html"
               doctitle="${component.title} ${component.version} API;"
            windowtitle="${component.title} ${component.version} API"
                 bottom="Copyright &amp;copy; 2001-${year} Apache Software Foundation. All Rights Reserved.">
    </javadoc>
  </target>

<!-- ====================================================================== -->
<!-- ========== Test framework ============================================ -->
<!-- ====================================================================== -->
   
  <property name="tf.name"                 value="commons-collections-testframework"/>
  <property name="tf.package"              value="org.apache.commons.collections"/>
  <property name="tf.title"                value="Commons Collections Test Framework"/>
  <property name="tf.title.full"           value="Apache Jakarta Commons Collections Test Framework"/>
  <property name="tf.version"              value="${component.version}"/>

  <property name="tf.build.conf"           value="${build.home}/tfconf"/>
  <property name="tf.build.tf"             value="${build.home}/testframework"/>
  <property name="tf.build.docs"           value="${build.home}/docs/testframework"/>
  
  <property name="tf.jar.name" value="${tf.name}-${tf.version}.jar"/>
  <property name="tf.build.jar.name" value="${build.home}/${tf.jar.name}"/>


<!-- ====================================================================== -->

  <!-- patternset describing test framework source not dependent on collections jar -->
  <patternset id="tf.patternset.validate">
    <include name="**/AbstractTestObject.java"/>
    <include name="**/AbstractTestCollection.java"/>
    <include name="**/AbstractTestSet.java"/>
    <include name="**/AbstractTestSortedSet.java"/>
    <include name="**/AbstractTestList.java"/>
    <include name="**/AbstractTestMap.java"/>
    <include name="**/AbstractTestSortedMap.java"/>
    <include name="**/AbstractTestComparator.java"/>
    <include name="**/AbstractTestIterator.java"/>
    <include name="**/AbstractTestListIterator.java"/>
    <include name="**/AbstractTestMapEntry.java"/>
    <include name="**/BulkTest.java"/>
  </patternset>
  
  <target name="tf.validate" depends="prepare"
          description="Testframework - Validate testframework independence">
    <delete    dir="${tf.build.tf}"/>
    <mkdir     dir="${tf.build.tf}"/>
    <javac  srcdir="${source.test}" target="1.6" source="1.6"
           destdir="${tf.build.tf}"
             debug="true"
       deprecation="false"
          optimize="false">
      <patternset refid="tf.patternset.validate" />
      <classpath>
        <pathelement location="${junit.jar}"/>
      </classpath>
    </javac>
    <delete dir="${tf.build.tf}"/>
  </target>

<!-- ====================================================================== -->

  <target name="tf.jar" depends="compile.tests"
          description="Testframework - Create jar">
    <mkdir      dir="${tf.build.tf}"/>
    <copy     todir="${tf.build.tf}">
      <fileset dir="${build.tests}">
        <include name="**/AbstractTest*.class"/>
        <include name="**/BulkTest*.class"/>
      </fileset>
    </copy>
    
    <mkdir      dir="${tf.build.tf}/META-INF"/>
    <copy      file="LICENSE.txt"
             tofile="${tf.build.tf}/META-INF/LICENSE.txt"/>
    <copy      file="NOTICE.txt"
             tofile="${tf.build.tf}/META-INF/NOTICE.txt"/>
             
    <tstamp/>
    <mkdir      dir="${tf.build.conf}"/>
    <copy     todir="${tf.build.conf}" filtering="on">
      <filterset>
        <filter token="name"     value="${tf.name}"/>
        <filter token="title"    value="${tf.title}"/>
        <filter token="package"  value="${tf.package}"/>
        <filter token="version"  value="${tf.version}"/>
      </filterset>
      <fileset dir="${source.conf}" includes="*.MF"/>
    </copy>
             
    <!-- NOTE: A jar built using JDK1.4 is incompatible with JDK1.2 -->
    <jar    jarfile="${tf.build.jar.name}"
            basedir="${tf.build.tf}"
           manifest="${tf.build.conf}/MANIFEST.MF"/>
  </target>

<!-- ====================================================================== -->

  <target name="tf.javadoc" depends="prepare"
          description="Testframework - Create Javadoc documentation">
    <tstamp><format property="year" pattern="yyyy"/></tstamp>
    <delete     dir="${tf.build.docs}"/>
    <mkdir      dir="${tf.build.docs}"/>
    <javadoc    destdir="${tf.build.docs}"
                 access="protected"
                 author="false"
                version="false"
                   link="${javadoc.links}"
               overview="${source.test}/org/apache/commons/collections/overview.html"
               doctitle="${tf.title} ${tf.version} API;"
            windowtitle="${tf.title} ${tf.version} API"
                 bottom="Copyright &amp;copy; 2001-${year} Apache Software Foundation. All Rights Reserved.">
      <fileset dir="${source.test}">
        <include name="**/AbstractTest*.java"/>
        <include name="**/BulkTest*.java"/>
      </fileset>
    </javadoc>
  </target>


<!-- ====================================================================== -->
<!-- ========== Distributions ============================================= -->
<!-- ====================================================================== -->
   
<!-- ====================================================================== -->

  <!-- Target needed for nightly builds -->
  <target name="dist" depends="javadoc,dist.create"
          description="Create distribution folders">
    <delete dir="${dist.home}"/>
    <mkdir dir="${dist.home}" />
    <copy todir="${dist.home}">
      <fileset dir="${build.dist.bin}" />
	</copy>
  </target>

  <target name="dist.create" depends="jar,testjar,tf.validate,tf.jar,dist.bin,dist.src">
  </target>

  <target name="dist.bin">
    <copy todir="${build.src}">
      <fileset dir="${basedir}/src/java" includes="**/*.java" />
    </copy>
    <copy todir="${build.src}/META-INF">
      <fileset dir="${basedir}" includes="LICENSE*, NOTICE*" />
    </copy>
    <jar jarfile="${build.home}/${final.name}-src-ide.zip" basedir="${build.src}" />
    <antcall target="internal-md5">
      <param name="path" value="${build.home}/${final.name}.jar"/>
    </antcall>
  	
    <mkdir      dir="${build.dist.bin.work}"/>
    <copy     todir="${build.dist.bin.work}">
      <fileset dir=".">
        <include name="LICENSE.txt"/>
        <include name="NOTICE.txt"/>
        <include name="README.txt"/>
        <include name="RELEASE-NOTES.html"/>
      </fileset>
    </copy>
    <copy     todir="${build.dist.bin.work}">
      <fileset dir="${build.home}">
        <include name="*.jar"/>
        <include name="docs/**"/>
      </fileset>
    </copy>
  </target>
  
  <target name="dist.src">
    <mkdir      dir="${build.dist.src.work}"/>
    <copy     todir="${build.dist.src.work}">
      <fileset dir=".">
        <include name="LICENSE.txt"/>
        <include name="NOTICE.txt"/>
        <include name="README.txt"/>
        <include name="RELEASE-NOTES.html"/>
        <include name="DEVELOPERS-GUIDE.html"/>
        <include name="PROPOSAL.html"/>
        <include name="STATUS.html"/>
        <include name="build.xml"/>
        <include name="maven.xml"/>
        <include name="project.xml"/>
        <include name="project.properties"/>
      </fileset>
    </copy>
    <copy     todir="${build.dist.src.work}">
      <fileset dir="${build.home}">
        <include name="${final.name}.jar"/>
      </fileset>
    </copy>
    <copy     todir="${build.dist.src.work}">
      <fileset dir=".">
        <include name="data/**"/>
        <include name="src/**"/>
        <include name="xdocs/**"/>
      </fileset>
    </copy>
  </target>

<!-- ====================================================================== -->

  <target name="release" depends="dist.create,zip"
          description="Create release">
  	<!-- POM -->
  	<copy file="project.xml" tofile="${build.home}/${final.name}.pom" />
    <antcall target="internal-md5">
      <param name="path" value="${build.home}/${final.name}.pom"/>
    </antcall>
  </target>

  <target name="zip" depends="zip.bin,zip.src">
  </target>
  
  <target name="zip.bin">
    <mkdir dir="${build.dist}"/>
  	<fixcrlf srcdir="${build.dist.bin.work}" eol="lf" includes="*.txt" />
    <tar longfile="gnu" tarfile="${build.bin.tar.name}">
      <tarfileset dir="${build.dist.bin}"/>
    </tar>
    <gzip zipfile="${build.bin.gz.name}" src="${build.bin.tar.name}"/>
    <delete file="${build.bin.tar.name}" />
    <antcall target="internal-md5">
      <param name="path" value="${build.bin.gz.name}"/>
    </antcall>
    
  	<fixcrlf srcdir="${build.dist.bin.work}" eol="crlf" includes="*.txt" />
    <zip zipfile="${build.bin.zip.name}" >
      <zipfileset dir="${build.dist.bin}"/>
    </zip>
    <antcall target="internal-md5">
      <param name="path" value="${build.bin.zip.name}"/>
    </antcall>
  </target>

  <target name="zip.src">
    <mkdir dir="${build.dist}"/>
  	<fixcrlf srcdir="${build.dist.src.work}" eol="lf" includes="*.txt,*.properties" />
    <tar longfile="gnu" tarfile="${build.src.tar.name}">
      <tarfileset dir="${build.dist.src}"/>
    </tar>
    <gzip zipfile="${build.src.gz.name}" src="${build.src.tar.name}"/>
    <delete file="${build.src.tar.name}" />
    <antcall target="internal-md5">
      <param name="path" value="${build.src.gz.name}"/>
    </antcall>
    
  	<fixcrlf srcdir="${build.dist.src.work}" eol="crlf" includes="*.txt,*.properties" />
    <zip zipfile="${build.src.zip.name}" >
      <zipfileset dir="${build.dist.src}"/>
    </zip>
    <antcall target="internal-md5">
      <param name="path" value="${build.src.zip.name}"/>
    </antcall>
  </target>

  <target name="internal-md5">
    <basename property="_base" file="${path}"/>
    <checksum file="${path}" property="md5"/>
   	<echo message="${md5} *${_base}" file="${path}.md5"/>
  </target>

<!-- ====================================================================== -->
  <target name="clirr">
    <taskdef resource="clirrtask.properties">
      <classpath path="${maven.repo}/clirr/jars/clirr-core-0.6-uber.jar;" />
    </taskdef>
    <clirr>
      <origfiles dir="${maven.repo}/commons-collections/jars" includes="commons-collections-3.1.jar"/>
      <newfiles dir="${build.home}" includes="${final.name}.jar" />
      <formatter type="plain" outfile="${build.home}/clirr.txt" />
    </clirr>
  </target>

</project>

```

## project.properties

```
#   Copyright 2003-2005 The Apache Software Foundation
#
#   Licensed under the Apache License, Version 2.0 (the "License");
#   you may not use this file except in compliance with the License.
#   You may obtain a copy of the License at
#
#       http://www.apache.org/licenses/LICENSE-2.0
#
#   Unless required by applicable law or agreed to in writing, software
#   distributed under the License is distributed on an "AS IS" BASIS,
#   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#   See the License for the specific language governing permissions and
#   limitations under the License.

maven.changelog.factory=org.apache.maven.svnlib.SvnChangeLogFactory

maven.xdoc.date=left
maven.xdoc.version=${pom.currentVersion}
maven.xdoc.developmentProcessUrl=http://jakarta.apache.org/commons/charter.html
maven.xdoc.poweredby.image=maven-feather.png
maven.xdoc.copy.excludes=images/file.gif,images/folder-closed.gif,images/folder-open.gif,images/icon_alert.gif,images/icon_alertsml.gif,images/icon_arrowfolder1_sml.gif,images/icon_arrowfolder2_sml.gif,images/icon_arrowmembers1_sml.gif,images/icon_arrowmembers2_sml.gif,images/icon_arrowusergroups1_sml.gif,images/icon_arrowusergroups2_sml.gif,images/icon_confirmsml.gif,images/icon_help_lrg.gif,images/icon_infosml.gif,images/icon_members_sml.gif,images/icon_sortleft.gif,images/icon_sortright.gif,images/icon_usergroups_sml.gif,images/icon_waste_lrg.gif,images/icon_waste_sml.gif,images/none.png,images/nw_maj.gif,images/nw_maj_hi.gif,images/nw_med.gif,images/nw_med_hi.gif,images/nw_med_rond.gif,images/nw_min.gif,images/nw_min_036.gif,images/nw_min_hi.gif,images/poweredby_036.gif,images/product_logo.gif,images/se_maj_rond.gif,images/sw_min.gif,images/logos/**
maven.xdoc.copy.excludes.classic=images/external-classic.png,images/help_logo.gif,images/icon_arrowfolderclosed1_sml.gif,images/icon_arrowwaste1_sml.gif,images/icon_arrowwaste2_sml.gif,images/icon_doc_lrg.gif,images/icon_doc_sml.gif,images/icon_error_lrg.gif,images/icon_folder_lrg.gif,images/icon_folder_sml.gif,images/icon_help_sml.gif,images/icon_info_lrg.gif,images/icon_members_lrg.gif,images/icon_sortdown.gif,images/icon_sortup.gif,images/icon_success_lrg.gif,images/icon_usergroups_lrg.gif,images/icon_arrowfolderopen2_sml.gif,images/icon_warning_lrg.gif,images/newwindow-classic.png,images/nw_maj_rond.gif,images/strich.gif,images/sw_maj_rond.gif,images/sw_med_rond.gif

# Jar Manifest Additional Attributes
maven.jar.manifest.attributes.list=Implementation-Vendor-Id,X-Compile-Source-JDK,X-Compile-Target-JDK
maven.jar.manifest.attribute.Implementation-Vendor-Id=org.apache
maven.jar.manifest.attribute.X-Compile-Source-JDK=${maven.compile.source}
maven.jar.manifest.attribute.X-Compile-Target-JDK=${maven.compile.target}

maven.javadoc.author=false
maven.javadoc.links=http://java.sun.com/j2se/1.4/docs/api/
maven.javadoc.source=1.3
#maven.javadoc.additionalparam=-tag todo:a:"To Do:"
maven.javadoc.overview=src/java/org/apache/commons/collections/overview.html
#maven.javadoc.public=true
#maven.javadoc.package=false
#maven.javadoc.private=false

maven.checkstyle.properties=checkstyle.xml

maven.jdiff.new.tag=CURRENT
maven.jdiff.old.tag=COLLECTIONS_3_1

# Generate class files for specific VM version (e.g., 1.1 or 1.2). 
# Note that the default value depends on the JVM that is running Ant. 
# In particular, if you use JDK 1.4+ the generated classes will not be usable
# for a 1.1 Java VM unless you explicitly set this attribute to the value 1.1 
# (which is the default value for JDK 1.1 to 1.3).
maven.compile.target = 1.6

# Specifies the source version for the Java compiler.
# Corresponds to the source attribute for the ant javac task. 
# Valid values are 1.3, 1.4, 1.5. 
maven.compile.source = 1.6

maven.compile.debug=on
maven.compile.deprecation=off
maven.compile.optimize=off

maven.jarResources.basedir=src/java
maven.jar.excludes=**/package.html
maven.junit.fork=true

clover.excludes=**/Test*.java

```

## src/java/org/apache/commons/collections/map/Flat3Map.java

```
/*
 *  Copyright 2003-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.commons.collections.map;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.commons.collections.IterableMap;
import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.ResettableIterator;
import org.apache.commons.collections.iterators.EmptyIterator;
import org.apache.commons.collections.iterators.EmptyMapIterator;

/**
 * A <code>Map</code> implementation that stores data in simple fields until
 * the size is greater than 3.
 * <p>
 * This map is designed for performance and can outstrip HashMap.
 * It also has good garbage collection characteristics.
 * <ul>
 * <li>Optimised for operation at size 3 or less.
 * <li>Still works well once size 3 exceeded.
 * <li>Gets at size 3 or less are about 0-10% faster than HashMap,
 * <li>Puts at size 3 or less are over 4 times faster than HashMap.
 * <li>Performance 5% slower than HashMap once size 3 exceeded once.
 * </ul>
 * The design uses two distinct modes of operation - flat and delegate.
 * While the map is size 3 or less, operations map straight onto fields using
 * switch statements. Once size 4 is reached, the map switches to delegate mode
 * and only switches back when cleared. In delegate mode, all operations are
 * forwarded straight to a HashMap resulting in the 5% performance loss.
 * <p>
 * The performance gains on puts are due to not needing to create a Map Entry
 * object. This is a large saving not only in performance but in garbage collection.
 * <p>
 * Whilst in flat mode this map is also easy for the garbage collector to dispatch.
 * This is because it contains no complex objects or arrays which slow the progress.
 * <p>
 * Do not use <code>Flat3Map</code> if the size is likely to grow beyond 3.
 * <p>
 * <strong>Note that Flat3Map is not synchronized and is not thread-safe.</strong>
 * If you wish to use this map from multiple threads concurrently, you must use
 * appropriate synchronization. The simplest approach is to wrap this map
 * using {@link java.util.Collections#synchronizedMap(Map)}. This class may throw 
 * exceptions when accessed by concurrent threads without synchronization.
 *
 * @since Commons Collections 3.0
 * @version $Revision$ $Date$
 *
 * @author Stephen Colebourne
 */
public class Flat3Map implements IterableMap, Serializable, Cloneable {

    /** Serialization version */
    private static final long serialVersionUID = -6701087419741928296L;

    /** The size of the map, used while in flat mode */
    private transient int size;
    /** Hash, used while in flat mode */
    private transient int hash1;
    /** Hash, used while in flat mode */
    private transient int hash2;
    /** Hash, used while in flat mode */
    private transient int hash3;
    /** Key, used while in flat mode */
    private transient Object key1;
    /** Key, used while in flat mode */
    private transient Object key2;
    /** Key, used while in flat mode */
    private transient Object key3;
    /** Value, used while in flat mode */
    private transient Object value1;
    /** Value, used while in flat mode */
    private transient Object value2;
    /** Value, used while in flat mode */
    private transient Object value3;
    /** Map, used while in delegate mode */
    private transient AbstractHashedMap delegateMap;

    /**
     * Constructor.
     */
    public Flat3Map() {
        super();
    }

    /**
     * Constructor copying elements from another map.
     *
     * @param map  the map to copy
     * @throws NullPointerException if the map is null
     */
    public Flat3Map(Map map) {
        super();
        putAll(map);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the value mapped to the key specified.
     * 
     * @param key  the key
     * @return the mapped value, null if no match
     */
    public Object get(Object key) {
        if (delegateMap != null) {
            return delegateMap.get(key);
        }
        if (key == null) {
            switch (size) {
                // drop through
                case 3:
                    if (key3 == null) return value3;
                case 2:
                    if (key2 == null) return value2;
                case 1:
                    if (key1 == null) return value1;
            }
        } else {
            if (size > 0) {
                int hashCode = key.hashCode();
                switch (size) {
                    // drop through
                    case 3:
                        if (hash3 == hashCode && key.equals(key3)) return value3;
                    case 2:
                        if (hash2 == hashCode && key.equals(key2)) return value2;
                    case 1:
                        if (hash1 == hashCode && key.equals(key1)) return value1;
                }
            }
        }
        return null;
    }

    /**
     * Gets the size of the map.
     * 
     * @return the size
     */
    public int size() {
        if (delegateMap != null) {
            return delegateMap.size();
        }
        return size;
    }

    /**
     * Checks whether the map is currently empty.
     * 
     * @return true if the map is currently size zero
     */
    public boolean isEmpty() {
        return (size() == 0);
    }

    //-----------------------------------------------------------------------
    /**
     * Checks whether the map contains the specified key.
     * 
     * @param key  the key to search for
     * @return true if the map contains the key
     */
    public boolean containsKey(Object key) {
        if (delegateMap != null) {
            return delegateMap.containsKey(key);
        }
        if (key == null) {
            switch (size) {  // drop through
                case 3:
                    if (key3 == null) return true;
                case 2:
                    if (key2 == null) return true;
                case 1:
                    if (key1 == null) return true;
            }
        } else {
            if (size > 0) {
                int hashCode = key.hashCode();
                switch (size) {  // drop through
                    case 3:
                        if (hash3 == hashCode && key.equals(key3)) return true;
                    case 2:
                        if (hash2 == hashCode && key.equals(key2)) return true;
                    case 1:
                        if (hash1 == hashCode && key.equals(key1)) return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks whether the map contains the specified value.
     * 
     * @param value  the value to search for
     * @return true if the map contains the key
     */
    public boolean containsValue(Object value) {
        if (delegateMap != null) {
            return delegateMap.containsValue(value);
        }
        if (value == null) {  // drop through
            switch (size) {
                case 3:
                    if (value3 == null) return true;
                case 2:
                    if (value2 == null) return true;
                case 1:
                    if (value1 == null) return true;
            }
        } else {
            switch (size) {  // drop through
                case 3:
                    if (value.equals(value3)) return true;
                case 2:
                    if (value.equals(value2)) return true;
                case 1:
                    if (value.equals(value1)) return true;
            }
        }
        return false;
    }

    //-----------------------------------------------------------------------
    /**
     * Puts a key-value mapping into this map.
     * 
     * @param key  the key to add
     * @param value  the value to add
     * @return the value previously mapped to this key, null if none
     */
    public Object put(Object key, Object value) {
        if (delegateMap != null) {
            return delegateMap.put(key, value);
        }
        // change existing mapping
        if (key == null) {
            switch (size) {  // drop through
                case 3:
                    if (key3 == null) {
                        Object old = value3;
                        value3 = value;
                        return old;
                    }
                case 2:
                    if (key2 == null) {
                        Object old = value2;
                        value2 = value;
                        return old;
                    }
                case 1:
                    if (key1 == null) {
                        Object old = value1;
                        value1 = value;
                        return old;
                    }
            }
        } else {
            if (size > 0) {
                int hashCode = key.hashCode();
                switch (size) {  // drop through
                    case 3:
                        if (hash3 == hashCode && key.equals(key3)) {
                            Object old = value3;
                            value3 = value;
                            return old;
                        }
                    case 2:
                        if (hash2 == hashCode && key.equals(key2)) {
                            Object old = value2;
                            value2 = value;
                            return old;
                        }
                    case 1:
                        if (hash1 == hashCode && key.equals(key1)) {
                            Object old = value1;
                            value1 = value;
                            return old;
                        }
                }
            }
        }
        
        // add new mapping
        switch (size) {
            default:
                convertToMap();
                delegateMap.put(key, value);
                return null;
            case 2:
                hash3 = (key == null ? 0 : key.hashCode());
                key3 = key;
                value3 = value;
                break;
            case 1:
                hash2 = (key == null ? 0 : key.hashCode());
                key2 = key;
                value2 = value;
                break;
            case 0:
                hash1 = (key == null ? 0 : key.hashCode());
                key1 = key;
                value1 = value;
                break;
        }
        size++;
        return null;
    }

    /**
     * Puts all the values from the specified map into this map.
     * 
     * @param map  the map to add
     * @throws NullPointerException if the map is null
     */
    public void putAll(Map map) {
        int size = map.size();
        if (size == 0) {
            return;
        }
        if (delegateMap != null) {
            delegateMap.putAll(map);
            return;
        }
        if (size < 4) {
            for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
                Map.Entry entry = (Map.Entry) it.next();
                put(entry.getKey(), entry.getValue());
            }
        } else {
            convertToMap();
            delegateMap.putAll(map);
        }
    }

    /**
     * Converts the flat map data to a map.
     */
    private void convertToMap() {
        delegateMap = createDelegateMap();
        switch (size) {  // drop through
            case 3:
                delegateMap.put(key3, value3);
            case 2:
                delegateMap.put(key2, value2);
            case 1:
                delegateMap.put(key1, value1);
        }
        
        size = 0;
        hash1 = hash2 = hash3 = 0;
        key1 = key2 = key3 = null;
        value1 = value2 = value3 = null;
    }

    /**
     * Create an instance of the map used for storage when in delegation mode.
     * <p>
     * This can be overridden by subclasses to provide a different map implementation.
     * Not every AbstractHashedMap is suitable, identity and reference based maps
     * would be poor choices.
     *
     * @return a new AbstractHashedMap or subclass
     * @since Commons Collections 3.1
     */
    protected AbstractHashedMap createDelegateMap() {
        return new HashedMap();
    }

    /**
     * Removes the specified mapping from this map.
     * 
     * @param key  the mapping to remove
     * @return the value mapped to the removed key, null if key not in map
     */
    public Object remove(Object key) {
        if (delegateMap != null) {
            return delegateMap.remove(key);
        }
        if (size == 0) {
            return null;
        }
        if (key == null) {
            switch (size) {  // drop through
                case 3:
                    if (key3 == null) {
                        Object old = value3;
                        hash3 = 0;
                        key3 = null;
                        value3 = null;
                        size = 2;
                        return old;
                    }
                    if (key2 == null) {
                        Object old = value3;
                        hash2 = hash3;
                        key2 = key3;
                        value2 = value3;
                        hash3 = 0;
                        key3 = null;
                        value3 = null;
                        size = 2;
                        return old;
                    }
                    if (key1 == null) {
                        Object old = value3;
                        hash1 = hash3;
                        key1 = key3;
                        value1 = value3;
                        hash3 = 0;
                        key3 = null;
                        value3 = null;
                        size = 2;
                        return old;
                    }
                    return null;
                case 2:
                    if (key2 == null) {
                        Object old = value2;
                        hash2 = 0;
                        key2 = null;
                        value2 = null;
                        size = 1;
                        return old;
                    }
                    if (key1 == null) {
                        Object old = value2;
                        hash1 = hash2;
                        key1 = key2;
                        value1 = value2;
                        hash2 = 0;
                        key2 = null;
                        value2 = null;
                        size = 1;
                        return old;
                    }
                    return null;
                case 1:
                    if (key1 == null) {
                        Object old = value1;
                        hash1 = 0;
                        key1 = null;
                        value1 = null;
                        size = 0;
                        return old;
                    }
            }
        } else {
            if (size > 0) {
                int hashCode = key.hashCode();
                switch (size) {  // drop through
                    case 3:
                        if (hash3 == hashCode && key.equals(key3)) {
                            Object old = value3;
                            hash3 = 0;
                            key3 = null;
                            value3 = null;
                            size = 2;
                            return old;
                        }
                        if (hash2 == hashCode && key.equals(key2)) {
                            Object old = value3;
                            hash2 = hash3;
                            key2 = key3;
                            value2 = value3;
                            hash3 = 0;
                            key3 = null;
                            value3 = null;
                            size = 2;
                            return old;
                        }
                        if (hash1 == hashCode && key.equals(key1)) {
                            Object old = value3;
                            hash1 = hash3;
                            key1 = key3;
                            value1 = value3;
                            hash3 = 0;
                            key3 = null;
                            value3 = null;
                            size = 2;
                            return old;
                        }
                        return null;
                    case 2:
                        if (hash2 == hashCode && key.equals(key2)) {
                            Object old = value2;
                            hash2 = 0;
                            key2 = null;
                            value2 = null;
                            size = 1;
                            return old;
                        }
                        if (hash1 == hashCode && key.equals(key1)) {
                            Object old = value2;
                            hash1 = hash2;
                            key1 = key2;
                            value1 = value2;
                            hash2 = 0;
                            key2 = null;
                            value2 = null;
                            size = 1;
                            return old;
                        }
                        return null;
                    case 1:
                        if (hash1 == hashCode && key.equals(key1)) {
                            Object old = value1;
                            hash1 = 0;
                            key1 = null;
                            value1 = null;
                            size = 0;
                            return old;
                        }
                }
            }
        }
        return null;
    }

    /**
     * Clears the map, resetting the size to zero and nullifying references
     * to avoid garbage collection issues.
     */
    public void clear() {
        if (delegateMap != null) {
            delegateMap.clear();  // should aid gc
            delegateMap = null;  // switch back to flat mode
        } else {
            size = 0;
            hash1 = hash2 = hash3 = 0;
            key1 = key2 = key3 = null;
            value1 = value2 = value3 = null;
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Gets an iterator over the map.
     * Changes made to the iterator affect this map.
     * <p>
     * A MapIterator returns the keys in the map. It also provides convenient
     * methods to get the key and value, and set the value.
     * It avoids the need to create an entrySet/keySet/values object.
     * It also avoids creating the Map Entry object.
     * 
     * @return the map iterator
     */
    public MapIterator mapIterator() {
        if (delegateMap != null) {
            return delegateMap.mapIterator();
        }
        if (size == 0) {
            return EmptyMapIterator.INSTANCE;
        }
        return new FlatMapIterator(this);
    }

    /**
     * FlatMapIterator
     */
    static class FlatMapIterator implements MapIterator, ResettableIterator {
        private final Flat3Map parent;
        private int nextIndex = 0;
        private boolean canRemove = false;
        
        FlatMapIterator(Flat3Map parent) {
            super();
            this.parent = parent;
        }

        public boolean hasNext() {
            return (nextIndex < parent.size);
        }

        public Object next() {
            if (hasNext() == false) {
                throw new NoSuchElementException(AbstractHashedMap.NO_NEXT_ENTRY);
            }
            canRemove = true;
            nextIndex++;
            return getKey();
        }

        public void remove() {
            if (canRemove == false) {
                throw new IllegalStateException(AbstractHashedMap.REMOVE_INVALID);
            }
            parent.remove(getKey());
            nextIndex--;
            canRemove = false;
        }

        public Object getKey() {
            if (canRemove == false) {
                throw new IllegalStateException(AbstractHashedMap.GETKEY_INVALID);
            }
            switch (nextIndex) {
                case 3:
                    return parent.key3;
                case 2:
                    return parent.key2;
                case 1:
                    return parent.key1;
            }
            throw new IllegalStateException("Invalid map index");
        }

        public Object getValue() {
            if (canRemove == false) {
                throw new IllegalStateException(AbstractHashedMap.GETVALUE_INVALID);
            }
            switch (nextIndex) {
                case 3:
                    return parent.value3;
                case 2:
                    return parent.value2;
                case 1:
                    return parent.value1;
            }
            throw new IllegalStateException("Invalid map index");
        }

        public Object setValue(Object value) {
            if (canRemove == false) {
                throw new IllegalStateException(AbstractHashedMap.SETVALUE_INVALID);
            }
            Object old = getValue();
            switch (nextIndex) {
                case 3: 
                    parent.value3 = value;
                    break;
                case 2:
                    parent.value2 = value;
                    break;
                case 1:
                    parent.value1 = value;
                    break;
            }
            return old;
        }
        
        public void reset() {
            nextIndex = 0;
            canRemove = false;
        }
        
        public String toString() {
            if (canRemove) {
                return "Iterator[" + getKey() + "=" + getValue() + "]";
            } else {
                return "Iterator[]";
            }
        }
    }
    
    /**
     * Gets the entrySet view of the map.
     * Changes made to the view affect this map.
     * The Map Entry is not an independent object and changes as the 
     * iterator progresses.
     * To simply iterate through the entries, use {@link #mapIterator()}.
     * 
     * @return the entrySet view
     */
    public Set entrySet() {
        if (delegateMap != null) {
            return delegateMap.entrySet();
        }
        return new EntrySet(this);
    }
    
    /**
     * EntrySet
     */
    static class EntrySet extends AbstractSet {
        private final Flat3Map parent;
        
        EntrySet(Flat3Map parent) {
            super();
            this.parent = parent;
        }

        public int size() {
            return parent.size();
        }
        
        public void clear() {
            parent.clear();
        }
        
        public boolean remove(Object obj) {
            if (obj instanceof Map.Entry == false) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            Object key = entry.getKey();
            boolean result = parent.containsKey(key);
            parent.remove(key);
            return result;
        }

        public Iterator iterator() {
            if (parent.delegateMap != null) {
                return parent.delegateMap.entrySet().iterator();
            }
            if (parent.size() == 0) {
                return EmptyIterator.INSTANCE;
            }
            return new EntrySetIterator(parent);
        }
    }

    /**
     * EntrySetIterator and MapEntry
     */
    static class EntrySetIterator implements Iterator, Map.Entry {
        private final Flat3Map parent;
        private int nextIndex = 0;
        private boolean canRemove = false;
        
        EntrySetIterator(Flat3Map parent) {
            super();
            this.parent = parent;
        }

        public boolean hasNext() {
            return (nextIndex < parent.size);
        }

        public Object next() {
            if (hasNext() == false) {
                throw new NoSuchElementException(AbstractHashedMap.NO_NEXT_ENTRY);
            }
            canRemove = true;
            nextIndex++;
            return this;
        }

        public void remove() {
            if (canRemove == false) {
                throw new IllegalStateException(AbstractHashedMap.REMOVE_INVALID);
            }
            parent.remove(getKey());
            nextIndex--;
            canRemove = false;
        }

        public Object getKey() {
            if (canRemove == false) {
                throw new IllegalStateException(AbstractHashedMap.GETKEY_INVALID);
            }
            switch (nextIndex) {
                case 3:
                    return parent.key3;
                case 2:
                    return parent.key2;
                case 1:
                    return parent.key1;
            }
            throw new IllegalStateException("Invalid map index");
        }

        public Object getValue() {
            if (canRemove == false) {
                throw new IllegalStateException(AbstractHashedMap.GETVALUE_INVALID);
            }
            switch (nextIndex) {
                case 3:
                    return parent.value3;
                case 2:
                    return parent.value2;
                case 1:
                    return parent.value1;
            }
            throw new IllegalStateException("Invalid map index");
        }

        public Object setValue(Object value) {
            if (canRemove == false) {
                throw new IllegalStateException(AbstractHashedMap.SETVALUE_INVALID);
            }
            Object old = getValue();
            switch (nextIndex) {
                case 3: 
                    parent.value3 = value;
                    break;
                case 2:
                    parent.value2 = value;
                    break;
                case 1:
                    parent.value1 = value;
                    break;
            }
            return old;
        }
        
        public boolean equals(Object obj) {
            if (canRemove == false) {
                return false;
            }
            if (obj instanceof Map.Entry == false) {
                return false;
            }
            Map.Entry other = (Map.Entry) obj;
            Object key = getKey();
            Object value = getValue();
            return (key == null ? other.getKey() == null : key.equals(other.getKey())) &&
                   (value == null ? other.getValue() == null : value.equals(other.getValue()));
        }
        
        public int hashCode() {
            if (canRemove == false) {
                return 0;
            }
            Object key = getKey();
            Object value = getValue();
            return (key == null ? 0 : key.hashCode()) ^
                   (value == null ? 0 : value.hashCode());
        }
        
        public String toString() {
            if (canRemove) {
                return getKey() + "=" + getValue();
            } else {
                return "";
            }
        }
    }
    
    /**
     * Gets the keySet view of the map.
     * Changes made to the view affect this map.
     * To simply iterate through the keys, use {@link #mapIterator()}.
     * 
     * @return the keySet view
     */
    public Set keySet() {
        if (delegateMap != null) {
            return delegateMap.keySet();
        }
        return new KeySet(this);
    }

    /**
     * KeySet
     */
    static class KeySet extends AbstractSet {
        private final Flat3Map parent;
        
        KeySet(Flat3Map parent) {
            super();
            this.parent = parent;
        }

        public int size() {
            return parent.size();
        }
        
        public void clear() {
            parent.clear();
        }
        
        public boolean contains(Object key) {
            return parent.containsKey(key);
        }

        public boolean remove(Object key) {
            boolean result = parent.containsKey(key);
            parent.remove(key);
            return result;
        }

        public Iterator iterator() {
            if (parent.delegateMap != null) {
                return parent.delegateMap.keySet().iterator();
            }
            if (parent.size() == 0) {
                return EmptyIterator.INSTANCE;
            }
            return new KeySetIterator(parent);
        }
    }

    /**
     * KeySetIterator
     */
    static class KeySetIterator extends EntrySetIterator {
        
        KeySetIterator(Flat3Map parent) {
            super(parent);
        }

        public Object next() {
            super.next();
            return getKey();
        }
    }
    
    /**
     * Gets the values view of the map.
     * Changes made to the view affect this map.
     * To simply iterate through the values, use {@link #mapIterator()}.
     * 
     * @return the values view
     */
    public Collection values() {
        if (delegateMap != null) {
            return delegateMap.values();
        }
        return new Values(this);
    }

    /**
     * Values
     */
    static class Values extends AbstractCollection {
        private final Flat3Map parent;
        
        Values(Flat3Map parent) {
            super();
            this.parent = parent;
        }

        public int size() {
            return parent.size();
        }
        
        public void clear() {
            parent.clear();
        }
        
        public boolean contains(Object value) {
            return parent.containsValue(value);
        }

        public Iterator iterator() {
            if (parent.delegateMap != null) {
                return parent.delegateMap.values().iterator();
            }
            if (parent.size() == 0) {
                return EmptyIterator.INSTANCE;
            }
            return new ValuesIterator(parent);
        }
    }

    /**
     * ValuesIterator
     */
    static class ValuesIterator extends EntrySetIterator {
        
        ValuesIterator(Flat3Map parent) {
            super(parent);
        }

        public Object next() {
            super.next();
            return getValue();
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Write the map out using a custom routine.
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(size());
        for (MapIterator it = mapIterator(); it.hasNext();) {
            out.writeObject(it.next());  // key
            out.writeObject(it.getValue());  // value
        }
    }

    /**
     * Read the map in using a custom routine.
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        int count = in.readInt();
        if (count > 3) {
            delegateMap = createDelegateMap();
        }
        for (int i = count; i > 0; i--) {
            put(in.readObject(), in.readObject());
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Clones the map without cloning the keys or values.
     *
     * @return a shallow clone
     * @since Commons Collections 3.1
     */
    public Object clone() {
        try {
            Flat3Map cloned = (Flat3Map) super.clone();
            if (cloned.delegateMap != null) {
                cloned.delegateMap = (HashedMap) cloned.delegateMap.clone();
            }
            return cloned;
        } catch (CloneNotSupportedException ex) {
            throw new InternalError();
        }
    }

    /**
     * Compares this map with another.
     * 
     * @param obj  the object to compare to
     * @return true if equal
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (delegateMap != null) {
            return delegateMap.equals(obj);
        }
        if (obj instanceof Map == false) {
            return false;
        }
        Map other = (Map) obj;
        if (size != other.size()) {
            return false;
        }
        if (size > 0) {
            Object otherValue = null;
            switch (size) {  // drop through
                case 3:
                    if (other.containsKey(key3) == false) {
                        return false;
                    }
                    otherValue = other.get(key3);
                    if (value3 == null ? otherValue != null : !value3.equals(otherValue)) {
                        return false;
                    }
                case 2:
                    if (other.containsKey(key2) == false) {
                        return false;
                    }
                    otherValue = other.get(key2);
                    if (value2 == null ? otherValue != null : !value2.equals(otherValue)) {
                        return false;
                    }
                case 1:
                    if (other.containsKey(key1) == false) {
                        return false;
                    }
                    otherValue = other.get(key1);
                    if (value1 == null ? otherValue != null : !value1.equals(otherValue)) {
                        return false;
                    }
            }
        }
        return true;
    }

    /**
     * Gets the standard Map hashCode.
     * 
     * @return the hash code defined in the Map interface
     */
    public int hashCode() {
        if (delegateMap != null) {
            return delegateMap.hashCode();
        }
        int total = 0;
        switch (size) {  // drop through
            case 3:
                total += (hash3 ^ (value3 == null ? 0 : value3.hashCode()));
            case 2:
                total += (hash2 ^ (value2 == null ? 0 : value2.hashCode()));
            case 1:
                total += (hash1 ^ (value1 == null ? 0 : value1.hashCode()));
        }
        return total;
    }

    /**
     * Gets the map as a String.
     * 
     * @return a string version of the map
     */
    public String toString() {
        if (delegateMap != null) {
            return delegateMap.toString();
        }
        if (size == 0) {
            return "{}";
        }
        StringBuffer buf = new StringBuffer(128);
        buf.append('{');
        switch (size) {  // drop through
            case 3:
                buf.append((key3 == this ? "(this Map)" : key3));
                buf.append('=');
                buf.append((value3 == this ? "(this Map)" : value3));
                buf.append(',');
            case 2:
                buf.append((key2 == this ? "(this Map)" : key2));
                buf.append('=');
                buf.append((value2 == this ? "(this Map)" : value2));
                buf.append(',');
            case 1:
                buf.append((key1 == this ? "(this Map)" : key1));
                buf.append('=');
                buf.append((value1 == this ? "(this Map)" : value1));
        }
        buf.append('}');
        return buf.toString();
    }

}

```


Explicit fixture recipe definitions (generation support, separate from production source):
Use the same construction/projection knowledge across all four approaches. Setup failures are not target observations. Use valid receiver/dependency graphs and node kinds.
```json
{
  "fixture_policy_id": "beam-explicit-fixtures-v6-development",
  "schema_version": 1,
  "scope": "Same fixture construction/projection knowledge for all four approaches; no execution feedback",
  "source_sha256": {
    "algorithms/java/SqaProbe.java": "7505ce6959a9fd28c00c4e942c51a60ef659acdecd26e2acca91d616e4d80ade",
    "scripts/study/api854/fixture_policy.py": "26e793b7cbc0368acc1e1722c76b759fe84134126cb108a4e8dc71adaa5f3c9b"
  },
  "sources": {
    "algorithms/java/SqaProbe.java": "import java.lang.reflect.Array;\nimport java.lang.reflect.Constructor;\nimport java.lang.reflect.InvocationTargetException;\nimport java.lang.reflect.Method;\nimport java.lang.reflect.Modifier;\nimport java.nio.charset.StandardCharsets;\nimport java.nio.file.Files;\nimport java.nio.file.Paths;\nimport java.security.MessageDigest;\nimport java.security.NoSuchAlgorithmException;\nimport java.util.ArrayList;\nimport java.util.Arrays;\nimport java.util.Base64;\nimport java.util.Comparator;\nimport java.util.List;\n\n/** Fixed-revision observations for explicitly supported, deterministic Java APIs.\n * No buggy source, patch, or triggering test is used during input generation.\n * The same source is packaged with the generated JUnit suite.\n */\npublic final class SqaProbe {\n    private static final String[] STRINGS = {\n        \"\", \"0\", \"1\", \"-1\", \"null\", \"true\", \"false\", \"abc\", \"ABC\", \" \",\n        \"0x0\", \"0x1\", \"0xFFFFFFFF\", \"1.0\", \"1e3\", \"NaN\", \"Infinity\",\n        \"{}\", \"[]\", \"[1]\", \"{\\\"a\\\":1}\", \"a=b\", \"--help\", \"-x\", \"a,b\",\n        \"1970-01-01\", \"a\\\\nb\", \"a\\nb\", \"a\\tb\", \"\\u0e17\\u0e14\\u0e2a\\u0e2d\\u0e1a\"\n    };\n    private static final long[] NUMBERS = {0, 1, -1, 2, -2, 10, -10, 127, 128,\n        255, 256, 32767, -32768, Integer.MAX_VALUE, Integer.MIN_VALUE};\n\n    private SqaProbe() { }\n\n    /** Schema scaffolding carried in the suite; no benchmark test classes. */\n    public static class GenericFixture<T> { public T value; public T[] array; public List<T> items; }\n    public static class StringBinding extends GenericFixture<String> { }\n    public static class IntegerBinding extends GenericFixture<Integer> { }\n    public static class FixtureBean { public String value = \"fixture-value\"; }\n    public interface FixtureMock { String accept(String value); }\n\n    public static final String EXPLICIT_FIXTURES = \"beam-explicit-fixtures-v3-proposal\";\n    public static final String SCALAR_FIXTURES = \"beam-explicit-fixtures-v4-proposal\";\n    public static final String PILOT_FIXTURES = \"beam-explicit-fixtures-v5-proposal\";\n    public static final String REVIEWED_FIXTURES = \"beam-explicit-fixtures-v6-development\";\n    private static final ThreadLocal<FixtureSession> FIXTURES = new ThreadLocal<FixtureSession>();\n    private static final ThreadLocal<Boolean> INVOKED = new ThreadLocal<Boolean>();\n\n    /** A setup failure is never an observation of an uncalled target method. */\n    private static final class FixtureFailure extends RuntimeException {\n        FixtureFailure(String message, Throwable cause) { super(message, cause); }\n    }\n\n    // Production factories only: no dataset test classes, patches or buggy results.\n    // Reflection keeps the helper compilable without project-specific dependencies.\n    private static Object call(Object receiver, String name, Class<?>[] parameterTypes, Object... values)\n            throws ReflectiveOperationException {\n        Class<?> declaring = receiver instanceof Class ? (Class<?>)receiver : receiver.getClass();\n        while (declaring != null) {\n            try {\n                Method method = declaring.getDeclaredMethod(name, parameterTypes);\n                method.setAccessible(true);\n                return method.invoke(receiver instanceof Class ? null : receiver, values);\n            } catch (NoSuchMethodException missing) { declaring = declaring.getSuperclass(); }\n        }\n        throw new NoSuchMethodException(name);\n    }\n\n    private static Object construct(String name, Class<?>[] parameterTypes, Object... values)\n            throws ReflectiveOperationException {\n        Constructor<?> ctor = Class.forName(name).getDeclaredConstructor(parameterTypes);\n        ctor.setAccessible(true);\n        return ctor.newInstance(values);\n    }\n\n    private static final class FixtureSession {\n        final String targetClass;\n        final String method;\n        final boolean pilot;\n        final boolean reviewed;\n        boolean constructing;\n        Object compiler, registry, scope, cfg, reverse, flow, closureNode, receiver;\n        org.w3c.dom.Element domRoot;\n        org.w3c.dom.Node domChild;\n        Object jdomRoot, jdomChild;\n        java.io.ByteArrayOutputStream archiveBytes;\n        Object mapper, parser, context, collectionType, collectionDeserializer;\n        Object mock, baseInvocation, actualInvocation;\n        Object chartDataset, chartPlot, chartAxis, cleanupScript, cleanupExterns;\n        int cleanupNodeIndex;\n\n        @SuppressWarnings({\"unchecked\", \"rawtypes\"})\n        void unusedClosure(double a) throws ReflectiveOperationException {\n            if (compiler != null) return;\n            Class<?> node = Class.forName(\"com.google.javascript.rhino.Node\");\n            Class<?> ac = Class.forName(\"com.google.javascript.jscomp.AbstractCompiler\");\n            compiler = construct(\"com.google.javascript.jscomp.Compiler\", new Class<?>[]{});\n            Object options = construct(\"com.google.javascript.jscomp.CompilerOptions\", new Class<?>[]{});\n            call(compiler, \"initOptions\", new Class<?>[]{options.getClass()}, options);\n            cleanupExterns = call(compiler, \"parseTestCode\", new Class<?>[]{String.class}, \"\");\n            cleanupScript = call(compiler, \"parseTestCode\", new Class<?>[]{String.class},\n                \"var unused = 1; function fixture(x) { var local = \" + (a < 0 ? \"2\" : \"3\") + \"; return x; } fixture(1);\");\n            // Normalize traverses sibling roots and requires their common parent.\n            int block = Class.forName(\"com.google.javascript.rhino.Token\").getField(\"BLOCK\").getInt(null);\n            Object roots = construct(node.getName(), new Class<?>[]{int.class}, block);\n            call(roots, \"addChildToBack\", new Class<?>[]{node}, cleanupExterns);\n            call(roots, \"addChildToBack\", new Class<?>[]{node}, cleanupScript);\n            Object normalize = construct(\"com.google.javascript.jscomp.Normalize\", new Class<?>[]{ac, boolean.class}, compiler, false);\n            call(normalize, \"process\", new Class<?>[]{node, node}, cleanupExterns, cleanupScript);\n            Class<?> lifecycle = Class.forName(\"com.google.javascript.jscomp.AbstractCompiler$LifeCycleStage\");\n            call(compiler, \"setLifeCycleStage\", new Class<?>[]{lifecycle}, Enum.valueOf((Class)lifecycle, \"NORMALIZED\"));\n            closureNode = cleanupScript;\n        }\n\n        void chart(double a) throws ReflectiveOperationException {\n            if (chartDataset != null) return;\n            Class<?> dataset = Class.forName(\"org.jfree.data.category.CategoryDataset\");\n            Class<?> axis = Class.forName(\"org.jfree.chart.axis.CategoryAxis\");\n            Class<?> valueAxis = Class.forName(\"org.jfree.chart.axis.ValueAxis\");\n            Class<?> renderer = Class.forName(\"org.jfree.chart.renderer.category.CategoryItemRenderer\");\n            chartDataset = construct(\"org.jfree.data.category.DefaultCategoryDataset\", new Class<?>[]{});\n            call(chartDataset, \"addValue\", new Class<?>[]{double.class, Comparable.class, Comparable.class}, a < 0 ? -2.0 : 2.0, \"row-a\", \"column-a\");\n            call(chartDataset, \"addValue\", new Class<?>[]{double.class, Comparable.class, Comparable.class}, 5.0, \"row-b\", \"column-a\");\n            chartAxis = construct(axis.getName(), new Class<?>[]{String.class}, \"Domain\");\n            Object rangeAxis = construct(\"org.jfree.chart.axis.NumberAxis\", new Class<?>[]{String.class}, \"Range\");\n            chartPlot = construct(\"org.jfree.chart.plot.CategoryPlot\", new Class<?>[]{dataset, axis, valueAxis, renderer},\n                chartDataset, chartAxis, rangeAxis, receiver);\n            java.awt.Graphics2D graphics = new java.awt.image.BufferedImage(16,16,java.awt.image.BufferedImage.TYPE_INT_RGB).createGraphics();\n            try {\n                call(receiver, \"initialise\", new Class<?>[]{java.awt.Graphics2D.class, java.awt.geom.Rectangle2D.class,\n                    chartPlot.getClass(), dataset, Class.forName(\"org.jfree.chart.plot.PlotRenderingInfo\")},\n                    graphics, new java.awt.geom.Rectangle2D.Double(0,0,16,16), chartPlot, chartDataset, null);\n            } finally { graphics.dispose(); }\n        }\n\n        Object beanWriter() throws ReflectiveOperationException {\n            Object objectMapper = construct(\"com.fasterxml.jackson.databind.ObjectMapper\", new Class<?>[]{});\n            Object provider = call(objectMapper, \"getSerializerProvider\", new Class<?>[]{});\n            provider = call(provider, \"createInstance\", new Class<?>[]{Class.forName(\"com.fasterxml.jackson.databind.SerializationConfig\"),\n                Class.forName(\"com.fasterxml.jackson.databind.ser.SerializerFactory\")},\n                call(objectMapper, \"getSerializationConfig\", new Class<?>[]{}), call(objectMapper, \"getSerializerFactory\", new Class<?>[]{}));\n            Object serializer = call(provider, \"findValueSerializer\", new Class<?>[]{Class.class, Class.forName(\"com.fasterxml.jackson.databind.BeanProperty\")}, FixtureBean.class, null);\n            return Array.get(field(serializer, \"_props\"), 0);\n        }\n\n        @SuppressWarnings({\"unchecked\", \"rawtypes\"})\n        void jacksonCollection(double a) throws ReflectiveOperationException {\n            if (mapper != null) return;\n            mapper = construct(\"com.fasterxml.jackson.databind.ObjectMapper\", new Class<?>[]{});\n            Class<?> feature = Class.forName(\"com.fasterxml.jackson.databind.DeserializationFeature\");\n            call(mapper, \"configure\", new Class<?>[]{feature, boolean.class}, Enum.valueOf((Class)feature, \"ACCEPT_SINGLE_VALUE_AS_ARRAY\"), true);\n            Object typeFactory = call(mapper, \"getTypeFactory\", new Class<?>[]{});\n            collectionType = call(typeFactory, \"constructCollectionType\", new Class<?>[]{Class.class, Class.class}, java.util.ArrayList.class, String.class);\n            Object factory = call(mapper, \"getFactory\", new Class<?>[]{});\n            String input = method.equals(\"handleNonArray\") ? a < 0 ? \"\\\"alpha\\\"\" : \"\\\"beta\\\"\"\n                : a < 0 ? \"[\\\"alpha\\\",\\\"beta\\\"]\" : \"[\\\"left\\\",\\\"right\\\"]\";\n            parser = call(factory, \"createParser\", new Class<?>[]{String.class}, input);\n            call(parser, \"nextToken\", new Class<?>[]{});\n            Object blueprint = call(mapper, \"getDeserializationContext\", new Class<?>[]{});\n            context = call(blueprint, \"createInstance\", new Class<?>[]{Class.forName(\"com.fasterxml.jackson.databind.DeserializationConfig\"),\n                Class.forName(\"com.fasterxml.jackson.core.JsonParser\"), Class.forName(\"com.fasterxml.jackson.databind.InjectableValues\")},\n                call(mapper, \"getDeserializationConfig\", new Class<?>[]{}), parser, null);\n            collectionDeserializer = call(context, \"findRootValueDeserializer\", new Class<?>[]{Class.forName(\"com.fasterxml.jackson.databind.JavaType\")}, collectionType);\n        }\n\n        void mockito(double a) throws ReflectiveOperationException {\n            if (mock != null) return;\n            mock = call(Class.forName(\"org.mockito.Mockito\"), \"mock\", new Class<?>[]{Class.class}, FixtureMock.class);\n            call(mock, \"accept\", new Class<?>[]{String.class}, \"alpha\");\n            call(mock, \"accept\", new Class<?>[]{String.class}, a < 0 ? \"alpha\" : \"beta\");\n            Object util = construct(\"org.mockito.internal.util.MockUtil\", new Class<?>[]{});\n            Object handler = call(util, \"getMockHandler\", new Class<?>[]{Object.class}, mock);\n            Object container = call(handler, \"getInvocationContainer\", new Class<?>[]{});\n            List<?> invocations = (List<?>)call(container, \"getInvocations\", new Class<?>[]{});\n            baseInvocation = invocations.get(0);\n            actualInvocation = invocations.get(1);\n        }\n\n        FixtureSession(String targetClass, String method, String policy) {\n            this.targetClass = targetClass;\n            this.method = method;\n            this.reviewed = REVIEWED_FIXTURES.equals(policy);\n            this.pilot = PILOT_FIXTURES.equals(policy) || reviewed;\n        }\n\n        Object option(String name, String text) throws ReflectiveOperationException {\n            Object option = construct(\"org.apache.commons.cli.Option\",\n                    new Class<?>[]{String.class, boolean.class, String.class}, name, true, \"fixture\");\n            call(option, \"setType\", new Class<?>[]{Object.class}, String.class);\n            call(option, \"addValue\", new Class<?>[]{String.class}, text);\n            return option;\n        }\n\n        Object archiveEntry(String name, long size) throws ReflectiveOperationException {\n            Object entry = construct(\"org.apache.commons.compress.archivers.cpio.CpioArchiveEntry\",\n                    new Class<?>[]{String.class}, name);\n            call(entry, \"setSize\", new Class<?>[]{long.class}, size);\n            call(entry, \"setTime\", new Class<?>[]{long.class}, 0L);\n            call(entry, \"setMode\", new Class<?>[]{long.class}, 0100644L);\n            return entry;\n        }\n\n        Object prepareReceiver(Object value, double a) throws ReflectiveOperationException {\n            if (!pilot) return value;\n            if (targetClass.equals(\"org.apache.commons.cli.CommandLine\")) {\n                call(value, \"addOption\", new Class<?>[]{Class.forName(\"org.apache.commons.cli.Option\")}, option(\"x\", a < 0 ? \"alpha\" : \"beta\"));\n                call(value, \"addArg\", new Class<?>[]{String.class}, \"positional\");\n            } else if (targetClass.equals(\"com.fasterxml.jackson.core.util.TextBuffer\")) {\n                char[] content = (a < 0 ? \"123\" : \"45.5\").toCharArray();\n                call(value, \"resetWithCopy\", new Class<?>[]{char[].class, int.class, int.class}, content, 0, content.length);\n            } else if (targetClass.equals(\"org.jsoup.nodes.Document\")) {\n                Object html = call(value, \"appendElement\", new Class<?>[]{String.class}, \"html\");\n                call(html, \"appendElement\", new Class<?>[]{String.class}, \"head\");\n                Object body = call(html, \"appendElement\", new Class<?>[]{String.class}, \"body\");\n                call(body, \"text\", new Class<?>[]{String.class}, a < 0 ? \"alpha\" : \"beta\");\n                call(value, \"title\", new Class<?>[]{String.class}, \"Fixture\");\n            } else if (targetClass.endsWith(\"CpioArchiveOutputStream\")) {\n                call(value, \"putNextEntry\", new Class<?>[]{Class.forName(\"org.apache.commons.compress.archivers.cpio.CpioArchiveEntry\")},\n                        archiveEntry(\"fixture.txt\", method.equals(\"write\") ? 1 : 0));\n            } else if (targetClass.equals(\"org.joda.time.Partial\")) {\n                return call(value, \"with\", new Class<?>[]{Class.forName(\"org.joda.time.DateTimeFieldType\"), int.class},\n                        call(Class.forName(\"org.joda.time.DateTimeFieldType\"), \"hourOfDay\", new Class<?>[]{}), 10);\n            } else if (targetClass.equals(\"org.jfree.chart.renderer.category.AreaRenderer\")) {\n                receiver = value;\n                chart(a);\n            } else if (targetClass.equals(\"com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser\")) {\n                // Real StAX input; getters start on a named leaf VALUE_STRING.\n                for (int i = 0; i < 8; i++) {\n                    Object token = call(value, \"nextToken\", new Class<?>[]{});\n                    if (token != null && token.toString().equals(\"VALUE_STRING\")) break;\n                }\n            }\n            return value;\n        }\n\n        @SuppressWarnings({\"unchecked\", \"rawtypes\"})\n        Object nativeType(String name, boolean object) throws ReflectiveOperationException {\n            Class<?> nativeClass = Class.forName(\"com.google.javascript.rhino.jstype.JSTypeNative\");\n            Object key = Enum.valueOf((Class)nativeClass, name);\n            return call(registry, object ? \"getNativeObjectType\" : \"getNativeType\", new Class<?>[]{nativeClass}, key);\n        }\n\n        void closure(double a) throws ReflectiveOperationException {\n            if (compiler != null) return;\n            Class<?> node = Class.forName(\"com.google.javascript.rhino.Node\");\n            Class<?> scopeClass = Class.forName(\"com.google.javascript.jscomp.Scope\");\n            Class<?> abstractCompiler = Class.forName(\"com.google.javascript.jscomp.AbstractCompiler\");\n            compiler = construct(\"com.google.javascript.jscomp.Compiler\", new Class<?>[]{});\n            Object options = construct(\"com.google.javascript.jscomp.CompilerOptions\", new Class<?>[]{});\n            call(compiler, \"initOptions\", new Class<?>[]{options.getClass()}, options);\n            registry = call(compiler, \"getTypeRegistry\", new Class<?>[]{});\n            String expression = a < 0 ? \"x + 1\" : \"x + 's'\";\n            if (method.contains(\"And\") || method.contains(\"ShortCircuit\")) expression = \"x && true\";\n            if (method.contains(\"Or\")) expression = \"x || false\";\n            if (method.equals(\"traverseArrayLiteral\")) expression = \"[x, 1]\";\n            if (method.equals(\"traverseObjectLiteral\")) expression = \"({p:x})\";\n            if (method.equals(\"traverseHook\")) expression = \"x ? 1 : 2\";\n            if (method.equals(\"traverseAssign\")) expression = \"x = 2\";\n            if (method.equals(\"traverseGetElem\")) expression = \"x['p']\";\n            if (method.equals(\"traverseGetProp\") || method.contains(\"Property\")) expression = \"x.p\";\n            if (method.equals(\"traverseName\") || method.equals(\"redeclareSimpleVar\")\n                    || method.equals(\"narrowScope\") || method.equals(\"updateScopeForTypeChange\")) expression = \"x\";\n            Object script = call(compiler, \"parseTestCode\", new Class<?>[]{String.class},\n                    \"function fixture(x) { return \" + expression + \"; }\");\n            Object function = call(script, \"getFirstChild\", new Class<?>[]{});\n            Object global = call(scopeClass, \"createGlobalScope\", new Class<?>[]{node}, script);\n            scope = construct(scopeClass.getName(), new Class<?>[]{scopeClass, node}, global, function);\n            Object astParameters = call(call(function, \"getFirstChild\", new Class<?>[]{}), \"getNext\", new Class<?>[]{});\n            Object name = call(astParameters, \"getFirstChild\", new Class<?>[]{});\n            call(scope, \"declare\", new Class<?>[]{String.class, node,\n                    Class.forName(\"com.google.javascript.rhino.jstype.JSType\"),\n                    Class.forName(\"com.google.javascript.jscomp.CompilerInput\")}, \"x\", name, nativeType(\"UNKNOWN_TYPE\", false), null);\n            Object body = call(function, \"getLastChild\", new Class<?>[]{});\n            Object returnNode = call(body, \"getFirstChild\", new Class<?>[]{});\n            closureNode = method.equals(\"traverseReturn\") || method.equals(\"branchedFlowThrough\")\n                    ? returnNode : call(returnNode, \"getFirstChild\", new Class<?>[]{});\n            if (method.equals(\"traverseObjectLiteral\"))\n                call(closureNode, \"setJSType\", new Class<?>[]{Class.forName(\"com.google.javascript.rhino.jstype.JSType\")}, nativeType(\"OBJECT_TYPE\", true));\n            Object analysis = construct(\"com.google.javascript.jscomp.ControlFlowAnalysis\",\n                    new Class<?>[]{abstractCompiler, boolean.class, boolean.class}, compiler, false, true);\n            call(analysis, \"process\", new Class<?>[]{node, node}, null, function);\n            cfg = call(analysis, \"getCfg\", new Class<?>[]{});\n            Object convention = call(compiler, \"getCodingConvention\", new Class<?>[]{});\n            reverse = construct(\"com.google.javascript.jscomp.type.SemanticReverseAbstractInterpreter\",\n                    new Class<?>[]{Class.forName(\"com.google.javascript.jscomp.CodingConvention\"), registry.getClass()}, convention, registry);\n            flow = call(Class.forName(\"com.google.javascript.jscomp.LinkedFlowScope\"), \"createEntryLattice\",\n                    new Class<?>[]{scopeClass}, scope);\n            call(flow, \"inferSlotType\", new Class<?>[]{String.class, Class.forName(\"com.google.javascript.rhino.jstype.JSType\")},\n                    \"x\", nativeType(a < 0 ? \"NUMBER_TYPE\" : \"STRING_TYPE\", false));\n        }\n\n        void dom(double a) throws Exception {\n            if (domRoot != null) return;\n            javax.xml.parsers.DocumentBuilderFactory factory = pilot\n                ? javax.xml.parsers.DocumentBuilderFactory.newInstance(\"com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl\", SqaProbe.class.getClassLoader())\n                : javax.xml.parsers.DocumentBuilderFactory.newInstance();\n            factory.setNamespaceAware(true);\n            org.w3c.dom.Document document = factory.newDocumentBuilder().newDocument();\n            domRoot = document.createElementNS(\"urn:sqa:root\", \"r:root\");\n            document.appendChild(domRoot);\n            domRoot.setAttributeNS(\"http://www.w3.org/2000/xmlns/\", \"xmlns:r\", \"urn:sqa:root\");\n            domRoot.setAttributeNS(\"http://www.w3.org/XML/1998/namespace\", \"xml:lang\", \"en\");\n            org.w3c.dom.Element element = document.createElementNS(\"urn:sqa:item\", \"i:item\");\n            domChild = element;\n            element.setAttributeNS(\"http://www.w3.org/2000/xmlns/\", \"xmlns:i\", \"urn:sqa:item\");\n            element.setAttribute(\"id\", a < 0 ? \"left\" : \"right\");\n            domChild.appendChild(document.createTextNode(a < 0 ? \"alpha\" : \"beta\"));\n            org.w3c.dom.Element grandchild = document.createElementNS(\"urn:sqa:item\", \"i:item\");\n            grandchild.appendChild(document.createTextNode(\"nested\"));\n            domChild.appendChild(grandchild);\n            org.w3c.dom.Element last = document.createElementNS(\"urn:sqa:item\", \"i:item\");\n            last.appendChild(document.createTextNode(\"nested-last\"));\n            domChild.appendChild(last);\n            if (method.equals(\"getRelativePositionOfPI\")) {\n                domRoot.appendChild(document.createProcessingInstruction(\"fixture\", \"before\"));\n                domChild = document.createProcessingInstruction(\"fixture\", a < 0 ? \"alpha\" : \"beta\");\n            } else if (method.equals(\"getRelativePositionOfTextNode\")) {\n                domRoot.appendChild(document.createCDATASection(\"before\"));\n                domChild = document.createTextNode(a < 0 ? \"alpha\" : \"beta\");\n            }\n            domRoot.appendChild(domChild);\n        }\n\n        void jdom(double a) throws ReflectiveOperationException {\n            if (jdomRoot != null) return;\n            Class<?> element = Class.forName(\"org.jdom.Element\");\n            jdomRoot = construct(element.getName(), new Class<?>[]{String.class}, \"root\");\n            jdomChild = construct(element.getName(), new Class<?>[]{String.class}, \"item\");\n            call(jdomChild, \"setText\", new Class<?>[]{String.class}, a < 0 ? \"alpha\" : \"beta\");\n            call(jdomChild, \"setAttribute\", new Class<?>[]{String.class, String.class}, \"id\", a < 0 ? \"left\" : \"right\");\n            Object grandchild = construct(element.getName(), new Class<?>[]{String.class}, \"item\");\n            call(grandchild, \"setText\", new Class<?>[]{String.class}, \"nested\");\n            call(jdomChild, \"addContent\", new Class<?>[]{Class.forName(\"org.jdom.Content\")}, grandchild);\n            Object last = construct(element.getName(), new Class<?>[]{String.class}, \"item\");\n            call(last, \"setText\", new Class<?>[]{String.class}, \"nested-last\");\n            call(jdomChild, \"addContent\", new Class<?>[]{Class.forName(\"org.jdom.Content\")}, last);\n            if (method.equals(\"getRelativePositionOfPI\")) {\n                Object before = construct(\"org.jdom.ProcessingInstruction\", new Class<?>[]{String.class, String.class}, \"fixture\", \"before\");\n                call(jdomRoot, \"addContent\", new Class<?>[]{Class.forName(\"org.jdom.Content\")}, before);\n                jdomChild = construct(\"org.jdom.ProcessingInstruction\", new Class<?>[]{String.class, String.class}, \"fixture\", a < 0 ? \"alpha\" : \"beta\");\n            } else if (method.equals(\"getRelativePositionOfTextNode\")) {\n                Object before = construct(\"org.jdom.CDATA\", new Class<?>[]{String.class}, \"before\");\n                call(jdomRoot, \"addContent\", new Class<?>[]{Class.forName(\"org.jdom.Content\")}, before);\n                jdomChild = construct(\"org.jdom.Text\", new Class<?>[]{String.class}, a < 0 ? \"alpha\" : \"beta\");\n            }\n            call(jdomRoot, \"addContent\", new Class<?>[]{Class.forName(\"org.jdom.Content\")}, jdomChild);\n        }\n\n        void configurePointer(Object pointer) throws ReflectiveOperationException {\n            Class<?> resolverClass = Class.forName(\"org.apache.commons.jxpath.ri.NamespaceResolver\");\n            Object resolver = construct(resolverClass.getName(), new Class<?>[]{resolverClass}, new Object[]{null});\n            call(resolver, \"registerNamespace\", new Class<?>[]{String.class, String.class}, \"i\", \"urn:sqa:item\");\n            call(resolver, \"registerNamespace\", new Class<?>[]{String.class, String.class}, \"r\", \"urn:sqa:root\");\n            call(resolver, \"setNamespaceContextPointer\", new Class<?>[]{Class.forName(\"org.apache.commons.jxpath.ri.model.NodePointer\")}, pointer);\n            call(pointer, \"setNamespaceResolver\", new Class<?>[]{resolverClass}, resolver);\n        }\n\n        Object argument(Class<?> type, double a, double b, double c, int depth) {\n            try {\n                if (depth > 2) throw new FixtureFailure(\"Fixture recursion limit: \" + type.getName(), null);\n                String name = type.getName();\n                if (reviewed && !constructing && targetClass.equals(\"org.apache.commons.codec.language.Metaphone\")\n                        && method.equals(\"setMaxCodeLen\") && type == int.class)\n                    return a < -8 ? 0 : a < 0 ? 1 : a < 8 ? 4 : 8;\n                if (pilot) {\n                    if (targetClass.equals(\"com.google.gson.TypeInfoFactory\")) {\n                        java.lang.reflect.Field value = GenericFixture.class.getField(a < 0 ? \"value\" : \"items\");\n                        if (type == java.lang.reflect.TypeVariable.class) return GenericFixture.class.getTypeParameters()[0];\n                        if (type == java.lang.reflect.Field.class) return value;\n                        if (type == Class.class) return GenericFixture.class;\n                        if (type == java.lang.reflect.Type.class) {\n                            if (method.equals(\"getTypeInfoForArray\")) return a < 0 ? String[].class : Integer[].class;\n                            return a < 0 ? StringBinding.class.getGenericSuperclass() : IntegerBinding.class.getGenericSuperclass();\n                        }\n                    }\n                    if (targetClass.equals(\"com.google.javascript.jscomp.RemoveUnusedVars\")) {\n                        unusedClosure(a);\n                        if (type == boolean.class) return false; // No call-site optimizer prerequisite.\n                        if (name.equals(\"com.google.javascript.jscomp.AbstractCompiler\")) return compiler;\n                        if (name.equals(\"com.google.javascript.rhino.Node\")) {\n                            if (method.equals(\"process\")) return cleanupNodeIndex++ == 0 ? cleanupExterns : cleanupScript;\n                            if (method.equals(\"getFunctionArgList\")) {\n                                Object child = call(cleanupScript, \"getFirstChild\", new Class<?>[]{});\n                                while (child != null && !(Boolean)call(child, \"isFunction\", new Class<?>[]{}))\n                                    child = call(child, \"getNext\", new Class<?>[]{});\n                                if (child == null) throw new FixtureFailure(\"Missing parsed function\", null);\n                                return child;\n                            }\n                            return cleanupScript;\n                        }\n                    }\n                    if (targetClass.equals(\"org.jfree.chart.renderer.category.AreaRenderer\")) {\n                        chart(a);\n                        if (name.equals(\"org.jfree.data.category.CategoryDataset\")) return chartDataset;\n                        if (name.equals(\"org.jfree.chart.axis.CategoryAxis\")) return chartAxis;\n                        if (type == Comparable.class) return a < 0 ? \"row-a\" : \"column-a\";\n                        if (type == java.awt.geom.Rectangle2D.class) return new java.awt.geom.Rectangle2D.Double(0,0,16,16);\n                        if (name.equals(\"org.jfree.chart.util.RectangleEdge\")) return type.getField(\"BOTTOM\").get(null);\n                        if (type == int.class) return 0;\n                    }\n                    if (targetClass.equals(\"com.fasterxml.jackson.databind.ser.BeanPropertyWriter\")) {\n                        if (name.equals(targetClass)) return beanWriter();\n                        if (name.equals(\"com.fasterxml.jackson.databind.util.NameTransformer\"))\n                            return call(type, \"simpleTransformer\", new Class<?>[]{String.class, String.class}, a < 0 ? \"left_\" : \"right_\", \"_suffix\");\n                        if (type == Object.class) return method.equals(\"get\") ? new FixtureBean() : a < 0 ? \"fixture-key\" : \"fixture-value\";\n                    }\n                    if (targetClass.equals(\"com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer\")) {\n                        jacksonCollection(a);\n                        if (name.equals(\"com.fasterxml.jackson.databind.JavaType\")) return collectionType;\n                        if (name.equals(\"com.fasterxml.jackson.core.JsonParser\")) return parser;\n                        if (name.equals(\"com.fasterxml.jackson.databind.DeserializationContext\")) return context;\n                        if (name.equals(\"com.fasterxml.jackson.databind.deser.ValueInstantiator\"))\n                            return call(collectionDeserializer, \"getValueInstantiator\", new Class<?>[]{});\n                        if (name.equals(\"com.fasterxml.jackson.databind.JsonDeserializer\"))\n                            return Class.forName(\"com.fasterxml.jackson.databind.deser.std.StringDeserializer\").getField(\"instance\").get(null);\n                    }\n                    if (targetClass.equals(\"com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser\")) {\n                        String xml = a < 0 ? \"<root><item>123</item><other>alpha</other></root>\" : \"<root><item>45</item><other>beta</other></root>\";\n                        if (type == int.class && constructing) return 0;\n                        if (name.equals(\"com.fasterxml.jackson.core.io.IOContext\"))\n                            return construct(name, new Class<?>[]{Class.forName(\"com.fasterxml.jackson.core.util.BufferRecycler\"), Object.class, boolean.class},\n                                construct(\"com.fasterxml.jackson.core.util.BufferRecycler\", new Class<?>[]{}), xml, false);\n                        if (name.equals(\"com.fasterxml.jackson.core.ObjectCodec\")) return construct(\"com.fasterxml.jackson.dataformat.xml.XmlMapper\", new Class<?>[]{});\n                        if (type == javax.xml.stream.XMLStreamReader.class) {\n                            javax.xml.stream.XMLStreamReader reader = javax.xml.stream.XMLInputFactory.newInstance().createXMLStreamReader(new java.io.StringReader(xml));\n                            while (reader.hasNext() && reader.getEventType() != javax.xml.stream.XMLStreamConstants.START_ELEMENT) reader.next();\n                            return reader;\n                        }\n                    }\n                    if (targetClass.equals(\"org.mockito.internal.invocation.InvocationMatcher\")) {\n                        mockito(a);\n                        if (name.equals(\"org.mockito.invocation.Invocation\")) return constructing ? baseInvocation : actualInvocation;\n                    }\n                    if (targetClass.startsWith(\"org.apache.commons.math3.fraction.\")) {\n                        int number = 1 + bucket(a, 8);\n                        if (type == double.class) return (a < 0 ? -1 : 1) * number / 4.0;\n                        if (type == int.class) return number;\n                        if (type == long.class) return (long)number;\n                        if (type == java.math.BigInteger.class) return java.math.BigInteger.valueOf(number);\n                        if (name.equals(\"org.apache.commons.math3.fraction.BigFraction\") || name.equals(\"org.apache.commons.math3.fraction.Fraction\"))\n                            return construct(name, new Class<?>[]{int.class, int.class}, number, 3);\n                    }\n                    if (targetClass.equals(\"org.apache.commons.cli.CommandLine\")) {\n                        if (type == String.class) return constructing ? \"fixture\" : a < -0.33 ? \"x\" : a < 0.33 ? \"missing\" : \"extra\";\n                        if (type == char.class) return a < 0 ? 'x' : 'z';\n                        if (name.equals(\"org.apache.commons.cli.Option\")) return option(\"extra\", a < 0 ? \"left\" : \"right\");\n                    }\n                    if (targetClass.equals(\"org.jsoup.nodes.Document\") && type == String.class)\n                        return constructing ? \"https://fixture.invalid/\" : method.equals(\"createElement\") ? a < 0 ? \"span\" : \"section\"\n                            : STRINGS[bucket(a, STRINGS.length)];\n                    if (targetClass.equals(\"org.joda.time.Partial\")) {\n                        if (type == int.class) return bucket(a, 24);\n                        if (name.equals(\"org.joda.time.DateTimeFieldType\"))\n                            return call(type, \"hourOfDay\", new Class<?>[]{});\n                    }\n                    if (name.equals(\"org.joda.time.DurationFieldType\")) return call(type, a < 0 ? \"hours\" : \"days\", new Class<?>[]{});\n                    if (name.equals(\"org.joda.time.DurationField\")) return call(Class.forName(\"org.joda.time.field.UnsupportedDurationField\"),\n                        \"getInstance\", new Class<?>[]{Class.forName(\"org.joda.time.DurationFieldType\")},\n                        call(Class.forName(\"org.joda.time.DurationFieldType\"), \"hours\", new Class<?>[]{}));\n                    if (name.equals(\"com.fasterxml.jackson.core.util.BufferRecycler\")) return construct(name, new Class<?>[]{});\n                    if (type == java.io.OutputStream.class && targetClass.endsWith(\"CpioArchiveOutputStream\")) {\n                        archiveBytes = new java.io.ByteArrayOutputStream();\n                        return archiveBytes;\n                    }\n                    if (name.equals(\"org.apache.commons.compress.archivers.ArchiveEntry\") || name.equals(\"org.apache.commons.compress.archivers.cpio.CpioArchiveEntry\"))\n                        return archiveEntry(a < 0 ? \"next-left.txt\" : \"next-right.txt\", 0);\n                    if (targetClass.equals(\"com.fasterxml.jackson.core.io.NumberInput\") && type == String.class)\n                        return new String[]{\"0\", \"1\", \"12\", \"2147483647\"}[bucket(a, 4)];\n                }\n                if (scalar(type)) {\n                    if (type == String.class && method.equals(\"getRelativePositionOfPI\")) return a < 0 ? \"fixture\" : \"other\";\n                    if (type == String.class && (method.equals(\"namespacePointer\") || method.equals(\"getNamespaceURI\")))\n                        return a < 0 ? \"r\" : \"i\";\n                    return legacyArgument(type, Math.max(-0.95, a), b, c, depth);\n                }\n                if (type.isArray()) {\n                    Object array = Array.newInstance(type.getComponentType(), pilot && (targetClass.endsWith(\"NumberUtils\") || targetClass.endsWith(\"TypeInfoFactory\")) ? 1 + bucket(c, 4) : bucket(c, 5));\n                    for (int i = 0; i < Array.getLength(array); i++)\n                        Array.set(array, i, argument(type.getComponentType(), a, b, c, depth + 1));\n                    return array;\n                }\n                if (type == java.io.Reader.class && targetClass.equals(\"org.apache.commons.csv.ExtendedBufferedReader\"))\n                    return new java.io.StringReader(STRINGS[bucket(a, STRINGS.length)]);\n                if (name.startsWith(\"com.google.javascript.\")) {\n                    closure(a);\n                    if (name.endsWith(\".AbstractCompiler\")) return compiler;\n                    if (name.endsWith(\".ControlFlowGraph\")) return cfg;\n                    if (name.endsWith(\".ReverseAbstractInterpreter\")) return reverse;\n                    if (name.endsWith(\".Scope\")) return scope;\n                    if (name.endsWith(\".Scope$Var\")) return call(scope, \"getVar\", new Class<?>[]{String.class}, \"x\");\n                    if (name.endsWith(\".FlowScope\")) return flow;\n                    if (name.endsWith(\".Node\")) return closureNode;\n                    if (name.endsWith(\".JSType\")) return nativeType(a < 0 ? \"NUMBER_TYPE\" : \"STRING_TYPE\", false);\n                    if (name.endsWith(\".ObjectType\")) return nativeType(\"OBJECT_TYPE\", true);\n                }\n                if (name.startsWith(\"org.w3c.dom.\")) {\n                    dom(a);\n                    if (type.isInstance(domChild)) return domChild;\n                    if (type.isInstance(domChild.getOwnerDocument())) return domChild.getOwnerDocument();\n                }\n                if (type == java.util.Locale.class) return java.util.Locale.ROOT;\n                if (name.equals(\"org.apache.commons.jxpath.ri.QName\"))\n                    return construct(name, new Class<?>[]{String.class}, method.equals(\"attributeIterator\") ? \"id\" : \"item\");\n                if (name.equals(\"org.apache.commons.jxpath.ri.compiler.NodeTest\"))\n                    return construct(\"org.apache.commons.jxpath.ri.compiler.NodeNameTest\",\n                            new Class<?>[]{Class.forName(\"org.apache.commons.jxpath.ri.QName\"), String.class},\n                            targetClass.contains(\".jdom.\")\n                                ? construct(\"org.apache.commons.jxpath.ri.QName\", new Class<?>[]{String.class}, \"item\")\n                                : construct(\"org.apache.commons.jxpath.ri.QName\", new Class<?>[]{String.class, String.class}, \"i\", \"item\"),\n                            targetClass.contains(\".jdom.\") ? null : \"urn:sqa:item\");\n                if (name.equals(\"org.apache.commons.jxpath.ri.model.NodePointer\")) {\n                    if (targetClass.contains(\".jdom.\")) {\n                        jdom(a);\n                        if (!constructing && (method.equals(\"childIterator\") || method.equals(\"compareChildNodePointers\"))) {\n                            List<?> children = (List<?>)call(jdomChild, \"getContent\", new Class<?>[]{});\n                            Object anchor = children.get(a < 0 ? 0 : children.size() - 1);\n                            Object pointer = construct(targetClass, new Class<?>[]{type, Object.class}, receiver, anchor);\n                            configurePointer(pointer);\n                            return pointer;\n                        }\n                        Object pointer = construct(\"org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer\",\n                                new Class<?>[]{Object.class, java.util.Locale.class}, jdomRoot, java.util.Locale.ROOT);\n                        configurePointer(pointer);\n                        return pointer;\n                    }\n                    dom(a);\n                    if (!constructing && (method.equals(\"childIterator\") || method.equals(\"compareChildNodePointers\"))) {\n                        org.w3c.dom.Node anchor = a < 0 ? domChild.getFirstChild() : domChild.getLastChild();\n                        Object pointer = construct(targetClass, new Class<?>[]{type, org.w3c.dom.Node.class}, receiver, anchor);\n                        configurePointer(pointer);\n                        return pointer;\n                    }\n                    Object pointer = construct(\"org.apache.commons.jxpath.ri.model.dom.DOMNodePointer\",\n                            new Class<?>[]{org.w3c.dom.Node.class, java.util.Locale.class}, domRoot, java.util.Locale.ROOT);\n                    configurePointer(pointer);\n                    return pointer;\n                }\n                if (type == Object.class && targetClass.contains(\".jdom.\")\n                        && (constructing || !method.equals(\"setValue\"))) { jdom(a); return jdomChild; }\n                if (type == java.util.Iterator.class) return new ArrayList<Object>().iterator();\n                if (type == java.util.List.class || type == java.util.Collection.class || type == Iterable.class)\n                    return new ArrayList<Object>();\n                if (type == java.util.Set.class) return new java.util.HashSet<Object>();\n                if (type == java.util.Map.class) return new java.util.HashMap<Object,Object>();\n                if (type == Object.class || type == Number.class || type == java.util.Date.class)\n                    return legacyArgument(type, Math.max(-0.95, a), b, c, depth);\n                throw new FixtureFailure(\"No explicit recipe: \" + name, null);\n            } catch (FixtureFailure failure) { throw failure; }\n            catch (Exception failure) { throw new FixtureFailure(\"Fixture recipe failed: \" + type.getName()\n                    + \":\" + failure.getClass().getName() + \":\" + failure.getMessage(), failure); }\n        }\n\n        String nodeSnapshot(org.w3c.dom.Node node, int depth) {\n            if (depth > 8) return \"depth-limit\";\n            StringBuilder out = new StringBuilder(\"node:\").append(node.getNodeType()).append(':')\n                    .append(quote(node.getNodeName())).append(':').append(quote(String.valueOf(node.getNodeValue())));\n            org.w3c.dom.NamedNodeMap attributes = node.getAttributes();\n            List<String> attrs = new ArrayList<String>();\n            if (attributes != null) for (int i = 0; i < attributes.getLength(); i++)\n                attrs.add(nodeSnapshot(attributes.item(i), depth + 1));\n            java.util.Collections.sort(attrs);\n            out.append(attrs.toString()).append('[');\n            org.w3c.dom.NodeList children = node.getChildNodes();\n            for (int i = 0; i < Math.min(256, children.getLength()); i++) out.append(nodeSnapshot(children.item(i), depth + 1));\n            return out.append(\"]children:\").append(children.getLength()).toString();\n        }\n\n        Object field(Object value, String name) throws ReflectiveOperationException {\n            for (Class<?> type = value.getClass(); type != null; type = type.getSuperclass()) {\n                try {\n                    java.lang.reflect.Field field = type.getDeclaredField(name);\n                    field.setAccessible(true);\n                    return field.get(value);\n                } catch (NoSuchFieldException missing) { }\n            }\n            throw new NoSuchFieldException(name);\n        }\n\n        String projection(Object result, int depth) throws ReflectiveOperationException {\n            if (depth > 8) throw new FixtureFailure(\"Oracle projection depth exceeded\", null);\n            if (result == null) return \"null\";\n            String name = result.getClass().getName();\n            if (pilot && result instanceof java.lang.reflect.Type) return \"type:\" + nestedTestName(((java.lang.reflect.Type)result).getTypeName());\n            if (pilot && result instanceof Method) return \"method:\" + nestedTestName(((Method)result).toGenericString());\n            if (pilot && name.startsWith(\"com.google.gson.TypeInfo\"))\n                return \"type-info:\" + projection(call(result, \"getActualType\", new Class<?>[]{}), depth + 1);\n            if (pilot && name.equals(\"com.google.javascript.rhino.Node\")) return \"ast:\" + call(result, \"toStringTree\", new Class<?>[]{});\n            if (pilot && name.equals(\"org.apache.commons.jxpath.ri.NamespaceResolver\"))\n                return \"namespaces:r=\" + call(result, \"getNamespaceURI\", new Class<?>[]{String.class}, \"r\")\n                    + \":i=\" + call(result, \"getNamespaceURI\", new Class<?>[]{String.class}, \"i\");\n            if (pilot && name.equals(\"org.jfree.data.Range\"))\n                return \"range:\" + call(result, \"getLowerBound\", new Class<?>[]{}) + ':' + call(result, \"getUpperBound\", new Class<?>[]{});\n            if (pilot && name.equals(\"org.jfree.chart.LegendItem\")) return \"legend:\" + call(result, \"getLabel\", new Class<?>[]{});\n            if (pilot && name.equals(\"org.jfree.chart.LegendItemCollection\")) {\n                StringBuilder out = new StringBuilder(\"legends[\");\n                int count = ((Number)call(result, \"getItemCount\", new Class<?>[]{})).intValue();\n                if (count > 256) throw new FixtureFailure(\"Legend limit exceeded\", null);\n                for (int i = 0; i < count; i++) out.append(projection(call(result, \"get\", new Class<?>[]{int.class}, i), depth + 1)).append(';');\n                return out.append(']').toString();\n            }\n            if (pilot && name.startsWith(\"com.fasterxml.jackson.databind.type.\")) return \"java-type:\" + call(result, \"toCanonical\", new Class<?>[]{});\n            if (pilot && name.equals(\"com.fasterxml.jackson.core.io.SerializedString\")) return \"serialized-name:\" + call(result, \"getValue\", new Class<?>[]{});\n            if (pilot && name.equals(\"com.fasterxml.jackson.databind.ser.BeanPropertyWriter\"))\n                return \"property:\" + call(result, \"getName\", new Class<?>[]{}) + ':' + projection(call(result, \"getType\", new Class<?>[]{}), depth + 1);\n            if (pilot && targetClass.equals(\"org.mockito.internal.invocation.InvocationMatcher\")\n                    && Class.forName(\"org.mockito.invocation.Invocation\").isInstance(result))\n                return \"invocation:\" + projection(call(result, \"getMethod\", new Class<?>[]{}), depth + 1)\n                    + ':' + projection(call(result, \"getArguments\", new Class<?>[]{}), depth + 1)\n                    + \":verified=\" + call(result, \"isVerified\", new Class<?>[]{});\n            if (pilot && result.getClass().isArray()) {\n                int length = Array.getLength(result);\n                if (length > 100000) throw new FixtureFailure(\"Oracle array limit exceeded\", null);\n                StringBuilder out = new StringBuilder(\"array[\");\n                for (int i = 0; i < length; i++) out.append(projection(Array.get(result, i), depth + 1)).append(';');\n                return out.append(']').toString();\n            }\n            if (pilot && (name.equals(\"org.jsoup.nodes.Document\") || name.equals(\"org.jsoup.nodes.Element\")))\n                return \"html:\" + call(result, \"outerHtml\", new Class<?>[]{});\n            if (pilot && name.equals(\"org.apache.commons.cli.Option\"))\n                return \"option:\" + call(result, \"getOpt\", new Class<?>[]{}) + ':' + projection(call(result, \"getValues\", new Class<?>[]{}), depth + 1);\n            if (pilot && result instanceof java.util.Iterator) {\n                StringBuilder out = new StringBuilder(\"iterator[\");\n                java.util.Iterator<?> iterator = (java.util.Iterator<?>)result;\n                int count = 0;\n                while (iterator.hasNext()) {\n                    if (++count > 256) throw new FixtureFailure(\"Oracle iterator limit exceeded\", null);\n                    out.append(projection(iterator.next(), depth + 1)).append(';');\n                }\n                return out.append(']').toString();\n            }\n            if (pilot && (name.equals(\"org.apache.commons.math3.fraction.BigFraction\") || name.equals(\"org.apache.commons.math3.fraction.Fraction\")))\n                return \"fraction:\" + call(result, \"getNumerator\", new Class<?>[]{}) + '/' + call(result, \"getDenominator\", new Class<?>[]{});\n            if (pilot && name.startsWith(\"org.joda.time.\")) {\n                if (name.equals(\"org.joda.time.Partial\")) return \"partial:\" + call(result, \"toStringList\", new Class<?>[]{});\n                if (Class.forName(\"org.joda.time.DurationFieldType\").isInstance(result)) return \"duration-type:\" + call(result, \"getName\", new Class<?>[]{});\n                if (Class.forName(\"org.joda.time.DurationField\").isInstance(result))\n                    return \"duration:\" + call(result, \"getName\", new Class<?>[]{}) + ':' + call(result, \"isSupported\", new Class<?>[]{});\n            }\n            if (result instanceof org.w3c.dom.Node) return nodeSnapshot((org.w3c.dom.Node)result, 0);\n            if (reviewed && name.equals(\"org.jdom.Attribute\"))\n                return \"jdom-attribute:name=\" + projection(call(result, \"getName\", new Class<?>[]{}), depth + 1)\n                    + \":namespace=\" + projection(call(result, \"getNamespaceURI\", new Class<?>[]{}), depth + 1)\n                    + \":value=\" + projection(call(result, \"getValue\", new Class<?>[]{}), depth + 1);\n            if (name.equals(\"org.jdom.Element\") || name.equals(\"org.jdom.ProcessingInstruction\")\n                    || name.equals(\"org.jdom.Text\") || name.equals(\"org.jdom.CDATA\")) {\n                Object writer = construct(\"org.jdom.output.XMLOutputter\", new Class<?>[]{});\n                return \"xml:\" + call(writer, \"outputString\", new Class<?>[]{result.getClass()}, result);\n            }\n            if (name.equals(\"org.apache.commons.jxpath.ri.QName\")) return \"qname:\" + result.toString();\n            if (name.startsWith(\"com.google.javascript.rhino.jstype.\")) return \"js-type:\" + result.toString();\n            if (name.equals(\"com.google.javascript.jscomp.LinkedFlowScope\")) {\n                Object slot = call(result, \"getSlot\", new Class<?>[]{String.class}, \"x\");\n                return \"flow:x=\" + (slot == null ? \"absent\" : projection(call(slot, \"getType\", new Class<?>[]{}), depth + 1));\n            }\n            if (name.endsWith(\"TypeInference$BooleanOutcomePair\"))\n                return \"boolean-pair:\" + field(result, \"toBooleanOutcomes\") + ':' + field(result, \"booleanValues\")\n                    + \":left=\" + projection(field(result, \"leftScope\"), depth + 1)\n                    + \":right=\" + projection(field(result, \"rightScope\"), depth + 1);\n            if (result instanceof List) {\n                StringBuilder out = new StringBuilder(\"list[\");\n                if (((List<?>)result).size() > 256) throw new FixtureFailure(\"Oracle collection limit exceeded\", null);\n                for (Object item : (List<?>)result) out.append(projection(item, depth + 1)).append(';');\n                return out.append(']').toString();\n            }\n            if (result instanceof java.util.Map) {\n                java.util.Map<?,?> map = (java.util.Map<?,?>)result;\n                if (map.size() > 256) throw new FixtureFailure(\"Oracle map limit exceeded\", null);\n                List<String> entries = new ArrayList<String>();\n                for (java.util.Map.Entry<?,?> entry : map.entrySet())\n                    entries.add(projection(entry.getKey(), depth + 1) + \"=\" + projection(entry.getValue(), depth + 1));\n                java.util.Collections.sort(entries);\n                return \"map:\" + entries.toString();\n            }\n            if (name.startsWith(\"org.apache.commons.jxpath.ri.model.\")) {\n                Class<?> pointer = Class.forName(\"org.apache.commons.jxpath.ri.model.NodePointer\");\n                if (pointer.isInstance(result))\n                    return \"pointer:\" + projection(call(result, \"getImmediateNode\", new Class<?>[]{}), depth + 1);\n                if (Class.forName(\"org.apache.commons.jxpath.ri.model.NodeIterator\").isInstance(result)) {\n                    StringBuilder out = new StringBuilder(\"iterator[\");\n                    for (int i = 1; i <= 9; i++) {\n                        boolean present = (Boolean)call(result, \"setPosition\", new Class<?>[]{int.class}, i);\n                        if (!present) return out.append(']').toString();\n                        if (i == 9) throw new FixtureFailure(\"Oracle iterator limit exceeded\", null);\n                        out.append(projection(call(result, \"getNodePointer\", new Class<?>[]{}), depth + 1)).append(';');\n                    }\n                }\n            }\n            String simple = value(result);\n            if (simple.startsWith(\"object-type:\")) throw new FixtureFailure(\"No structural oracle: \" + name, null);\n            return simple;\n        }\n\n        String state() throws ReflectiveOperationException {\n            if (reviewed && targetClass.equals(\"org.apache.commons.codec.language.Metaphone\")\n                    && method.equals(\"setMaxCodeLen\")) {\n                int limit = ((Number)call(receiver, \"getMaxCodeLen\", new Class<?>[]{})).intValue();\n                String encoded = (String)call(receiver, \"metaphone\", new Class<?>[]{String.class}, \"architecture\");\n                return \"metaphone:maxCodeLen=\" + limit + \":encoded=\" + encoded\n                    + \":maxCodeLenAfterEncoding=\" + call(receiver, \"getMaxCodeLen\", new Class<?>[]{});\n            }\n            if (pilot && targetClass.equals(\"com.google.javascript.jscomp.RemoveUnusedVars\"))\n                return \"cleanup:\" + call(cleanupScript, \"toStringTree\", new Class<?>[]{});\n            if (pilot && targetClass.equals(\"org.jfree.chart.renderer.category.AreaRenderer\"))\n                return \"chart:rows=\" + call(chartDataset, \"getRowCount\", new Class<?>[]{}) + \":columns=\" + call(chartDataset, \"getColumnCount\", new Class<?>[]{});\n            if (pilot && targetClass.equals(\"com.fasterxml.jackson.databind.ser.BeanPropertyWriter\"))\n                return projection(receiver, 0) + \":setting=\" + projection(call(receiver, \"getInternalSetting\", new Class<?>[]{Object.class}, \"fixture-key\"), 0);\n            if (pilot && targetClass.equals(\"com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer\"))\n                return \"json-token:\" + call(parser, \"getCurrentToken\", new Class<?>[]{});\n            if (pilot && targetClass.equals(\"com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser\"))\n                return \"xml:closed=\" + call(receiver, \"isClosed\", new Class<?>[]{}) + \":token=\" + call(receiver, \"getCurrentToken\", new Class<?>[]{})\n                    + \":text=\" + projection(field(receiver, \"_currText\"), 0);\n            if (pilot && targetClass.equals(\"org.mockito.internal.invocation.InvocationMatcher\"))\n                return projection(baseInvocation, 0) + \":candidate=\" + projection(actualInvocation, 0);\n            if (pilot && targetClass.equals(\"org.apache.commons.cli.CommandLine\"))\n                return \"cli:\" + projection(call(receiver, \"getOptions\", new Class<?>[]{}), 0) + ':' + projection(call(receiver, \"getArgs\", new Class<?>[]{}), 0);\n            if (pilot && targetClass.equals(\"com.fasterxml.jackson.core.util.TextBuffer\"))\n                return \"text:\" + call(receiver, \"contentsAsString\", new Class<?>[]{}) + \":size=\" + call(receiver, \"size\", new Class<?>[]{});\n            if (pilot && targetClass.equals(\"org.jsoup.nodes.Document\") && receiver != null) return projection(receiver, 0);\n            if (pilot && targetClass.endsWith(\"CpioArchiveOutputStream\")) return \"archive:\" + value(archiveBytes.toByteArray());\n            if (pilot && targetClass.startsWith(\"org.apache.commons.math3.fraction.\")) return projection(receiver, 0);\n            if (pilot && targetClass.equals(\"org.joda.time.Partial\")) return projection(receiver, 0);\n            if (pilot && targetClass.equals(\"org.joda.time.field.UnsupportedDurationField\") && receiver != null) return projection(receiver, 0);\n            if (targetClass.equals(\"org.apache.commons.collections.map.Flat3Map\")) return projection(receiver, 0);\n            if (targetClass.equals(\"org.apache.commons.csv.ExtendedBufferedReader\"))\n                return \"reader:line=\" + call(receiver, \"getLineNumber\", new Class<?>[]{})\n                    + \":last=\" + call(receiver, \"readAgain\", new Class<?>[]{});\n            if (compiler != null) {\n                Object jsType = call(closureNode, \"getJSType\", new Class<?>[]{});\n                return \"ast:\" + call(closureNode, \"toStringTree\", new Class<?>[]{})\n                    + \":ast-type=\" + projection(jsType, 0) + ':' + projection(flow, 0);\n            }\n            if (domRoot != null) return nodeSnapshot(domRoot, 0) + \":child=\" + nodeSnapshot(domChild, 0)\n                    + \":attached=\" + (domChild.getParentNode() != null);\n            if (jdomRoot != null) return projection(jdomRoot, 0) + \":child=\" + projection(jdomChild, 0)\n                    + \":attached=\" + (call(jdomChild, \"getParent\", new Class<?>[]{}) != null);\n            return \"stateless-scalars\";\n        }\n    }\n\n    private static String quote(String value) {\n        StringBuilder out = new StringBuilder(\"\\\"\");\n        for (char c : value.toCharArray()) {\n            if (c == '\"' || c == '\\\\') out.append('\\\\').append(c);\n            else if (c < 32) out.append(String.format(\"\\\\u%04x\", (int)c));\n            else out.append(c);\n        }\n        return out.append('\"').toString();\n    }\n\n    private static String typeNames(Class<?>[] types) {\n        List<String> names = new ArrayList<String>();\n        for (Class<?> type : types) names.add(type.getName());\n        return String.join(\",\", names);\n    }\n\n    private static boolean scalar(Class<?> type) {\n        return type.isPrimitive() || type == String.class || type == Boolean.class\n            || type == Character.class || type == Byte.class || type == Short.class\n            || type == Integer.class || type == Long.class || type == Float.class\n            || type == Double.class || type.isEnum();\n    }\n\n    private static boolean supported(Class<?> type) {\n        return scalar(type) || (type.isArray() && scalar(type.getComponentType()));\n    }\n\n    private static boolean supportedParameters(Class<?>[] types) {\n        if (types.length > 6) return false;\n        for (Class<?> type : types) if (type == void.class) return false;\n        return true;\n    }\n\n    private static Class<?> type(String name) throws ClassNotFoundException {\n        if (name.equals(\"boolean\")) return boolean.class;\n        if (name.equals(\"byte\")) return byte.class;\n        if (name.equals(\"short\")) return short.class;\n        if (name.equals(\"int\")) return int.class;\n        if (name.equals(\"long\")) return long.class;\n        if (name.equals(\"float\")) return float.class;\n        if (name.equals(\"double\")) return double.class;\n        if (name.equals(\"char\")) return char.class;\n        return Class.forName(name);\n    }\n\n    private static Class<?>[] types(String names) throws ClassNotFoundException {\n        if (names.length() == 0) return new Class<?>[0];\n        String[] split = names.split(\",\", -1);\n        Class<?>[] result = new Class<?>[split.length];\n        for (int i = 0; i < split.length; i++) result[i] = type(split[i]);\n        return result;\n    }\n\n    private static int bucket(double coordinate, int size) {\n        double unit = Math.max(0, Math.min(1, (coordinate + 1) / 2));\n        return Math.min(size - 1, (int)(unit * size));\n    }\n\n    private static Object argument(Class<?> type, double a, double b, double c) {\n        return argument(type, a, b, c, 0);\n    }\n\n    private static Object argument(Class<?> type, double a, double b, double c, int depth) {\n        FixtureSession session = FIXTURES.get();\n        return session == null ? legacyArgument(type, a, b, c, depth) : session.argument(type, a, b, c, depth);\n    }\n\n    private static Object legacyArgument(Class<?> type, double a, double b, double c, int depth) {\n        if (depth > 2) return null;\n        if (type.isArray()) {\n            int length = bucket(c, 5);\n            Object array = Array.newInstance(type.getComponentType(), length);\n            for (int i = 0; i < length; i++) {\n                Array.set(array, i, argument(type.getComponentType(),\n                    Math.max(-1, Math.min(1, a + i * 0.17)), b, c, depth + 1));\n            }\n            return array;\n        }\n        if (!type.isPrimitive() && a < -0.96) return null;\n        if (type == String.class) {\n            int selection = bucket(a, STRINGS.length + 4);\n            if (selection < STRINGS.length) return STRINGS[selection];\n            int length = bucket(c, 33);\n            char character = \"0123456789abcdefXYZ +-_.\".charAt(bucket(b, 23));\n            char[] value = new char[length];\n            Arrays.fill(value, character);\n            return new String(value);\n        }\n        if (type == boolean.class || type == Boolean.class) return a >= 0;\n        if (type == char.class || type == Character.class) return (char)bucket(a, 128);\n        if (type.isEnum()) {\n            Object[] values = type.getEnumConstants();\n            return values.length == 0 ? null : values[bucket(a, values.length)];\n        }\n        long integer = b < 0 ? NUMBERS[bucket(a, NUMBERS.length)] : Math.round(a * 10000);\n        if (type == byte.class || type == Byte.class) return (byte)integer;\n        if (type == short.class || type == Short.class) return (short)integer;\n        if (type == int.class || type == Integer.class) return (int)integer;\n        if (type == long.class || type == Long.class) return integer;\n        double real = b < 0 ? integer : a * 1000;\n        if (type == float.class || type == Float.class) return (float)real;\n        if (type == double.class || type == Double.class) return real;\n        if (type == Number.class) return Double.valueOf(real);\n        if (type == Object.class) return b < 0 ? STRINGS[bucket(a, STRINGS.length)] : Long.valueOf(integer);\n        if (type == java.util.Date.class) return new java.util.Date(integer);\n        if (type == java.util.List.class || type == java.util.Collection.class || type == Iterable.class)\n            return new java.util.ArrayList<Object>();\n        if (type == java.util.Set.class) return new java.util.HashSet<Object>();\n        if (type == java.util.Map.class) return new java.util.HashMap<Object,Object>();\n        if (!type.isInterface() && !Modifier.isAbstract(type.getModifiers()) && !type.getName().startsWith(\"java.\")) {\n            Constructor<?>[] constructors = type.getDeclaredConstructors();\n            Arrays.sort(constructors, new Comparator<Constructor<?>>() {\n                public int compare(Constructor<?> left, Constructor<?> right) {\n                    int count = left.getParameterCount() - right.getParameterCount();\n                    return count != 0 ? count : left.toString().compareTo(right.toString());\n                }\n            });\n            for (Constructor<?> constructor : constructors) {\n                if (constructor.getParameterCount() > 3) continue;\n                try {\n                    constructor.setAccessible(true);\n                    Class<?>[] parameters = constructor.getParameterTypes();\n                    Object[] values = new Object[parameters.length];\n                    for (int i = 0; i < values.length; i++) values[i] = argument(parameters[i], a, b, c, depth + 1);\n                    return constructor.newInstance(values);\n                } catch (ReflectiveOperationException error) {\n                    // Failed fixture construction yields an explicit null boundary input.\n                } catch (RuntimeException error) {\n                    // Encapsulated/unconstructible fixture yields the same null boundary.\n                }\n            }\n        }\n        return null;\n    }\n\n    private static Object[] arguments(Class<?>[] types, double[] vector, int offset) {\n        Object[] values = new Object[types.length];\n        for (int i = 0; i < types.length; i++) {\n            int start = offset + 3 * i;\n            values[i] = argument(types[i], vector[start % vector.length],\n                vector[(start + 1) % vector.length], vector[(start + 2) % vector.length]);\n        }\n        FixtureSession session = FIXTURES.get();\n        if (session != null && session.pilot && !session.constructing) {\n            if (session.targetClass.equals(\"com.google.gson.TypeInfoFactory\")) {\n                java.lang.reflect.Type parent = vector[0] < 0 ? StringBinding.class.getGenericSuperclass() : IntegerBinding.class.getGenericSuperclass();\n                try {\n                    if (session.method.equals(\"getActualType\")) {\n                        values[0] = GenericFixture.class.getField(\"items\").getGenericType();\n                        values[1] = parent;\n                        values[2] = GenericFixture.class;\n                    } else if (session.method.equals(\"extractRealTypes\")) {\n                        values[0] = new java.lang.reflect.Type[]{GenericFixture.class.getField(\"value\").getGenericType()};\n                        values[1] = parent;\n                        values[2] = GenericFixture.class;\n                    }\n                } catch (NoSuchFieldException failure) { throw new FixtureFailure(\"Generic schema field missing\", failure); }\n            }\n            if (session.targetClass.equals(\"org.jfree.chart.renderer.category.AreaRenderer\") && session.method.equals(\"getItemMiddle\")) {\n                values[0] = \"row-a\";\n                values[1] = \"column-a\";\n            }\n        }\n        return values;\n    }\n\n    private static String value(Object value) {\n        if (value == null) return \"null\";\n        Class<?> type = value.getClass();\n        if (type.isArray()) {\n            StringBuilder out = new StringBuilder(type.getName()).append('[');\n            int length = Array.getLength(value);\n            if (length > 100000) throw new IllegalStateException(\"SQA_HARNESS oversized outcome\");\n            for (int i = 0; i < length; i++) out.append(value(Array.get(value, i))).append(';');\n            return out.append(']').toString();\n        }\n        if (value instanceof Class) return \"class:\" + nestedTestName(((Class<?>)value).getName());\n        if (!scalar(type) && !(value instanceof Number)) return \"object-type:\" + type.getName();\n        String text = value instanceof Enum ? ((Enum<?>) value).name() : String.valueOf(value);\n        return type.getName() + \":\" + Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));\n    }\n\n    private static String nestedTestName(String text) {\n        // GeneratedStudyTest nests a copy of this helper, so probe-time\n        // \"SqaProbe$FixtureMock\" renders at test runtime as\n        // \"GeneratedStudyTest$SqaProbe$FixtureMock\". Oracles must compare\n        // the probe-time spelling in both phases; never edit old suites.\n        return text.replace(\"GeneratedStudyTest$SqaProbe$\", \"SqaProbe$\");\n    }\n\n    private static String snapshot(String observed) {\n        // JVM string constants are limited to 65,535 encoded bytes. Long exact\n        // observations use a deterministic digest rather than enormous literals.\n        if (observed.length() <= 16000) return observed;\n        byte[] bytes = observed.getBytes(StandardCharsets.UTF_8);\n        try {\n            byte[] digest = MessageDigest.getInstance(\"SHA-256\").digest(bytes);\n            StringBuilder hex = new StringBuilder();\n            for (byte item : digest) hex.append(String.format(\"%02x\", item & 255));\n            return \"sha256:\" + hex + \":bytes:\" + bytes.length;\n        } catch (NoSuchAlgorithmException error) {\n            throw new IllegalStateException(\"SQA_HARNESS SHA-256 unavailable\", error);\n        }\n    }\n\n    public static String observe(String className, String constructorTypes, String methodName,\n                                 String methodTypes, double[] vector) {\n        INVOKED.set(false);\n        if (vector.length == 0) throw new IllegalArgumentException(\"SQA_HARNESS empty vector\");\n        try {\n            Class<?> target = Class.forName(className);\n            Class<?>[] ctorTypes = types(constructorTypes);\n            Class<?>[] parameterTypes = types(methodTypes);\n            Object receiver = null;\n            Method method = null;\n            if (!methodName.equals(\"<init>\")) {\n                Class<?> declaring = target;\n                while (declaring != null) {\n                    try { method = declaring.getDeclaredMethod(methodName, parameterTypes); break; }\n                    catch (NoSuchMethodException missing) { declaring = declaring.getSuperclass(); }\n                }\n                if (method == null) throw new NoSuchMethodException(methodName);\n                method.setAccessible(true);\n            }\n            if (method == null || !Modifier.isStatic(method.getModifiers())) {\n                Constructor<?> ctor = target.getDeclaredConstructor(ctorTypes);\n                ctor.setAccessible(true);\n                FixtureSession session = FIXTURES.get();\n                if (session != null) session.constructing = true;\n                try {\n                    Object[] values = arguments(ctorTypes, vector, 0);\n                    if (method == null) INVOKED.set(true);\n                    receiver = ctor.newInstance(values);\n                    if (session != null) receiver = session.prepareReceiver(receiver, vector[0]);\n                    if (session != null) session.receiver = receiver;\n                    if (session != null && className.equals(\"org.apache.commons.collections.map.Flat3Map\")) {\n                        call(receiver, \"put\", new Class<?>[]{Object.class, Object.class}, \"fixture-a\", \"value-a\");\n                        call(receiver, \"put\", new Class<?>[]{Object.class, Object.class}, \"fixture-b\", \"value-b\");\n                    }\n                    if (session != null && className.startsWith(\"org.apache.commons.jxpath.ri.model.\")) session.configurePointer(receiver);\n                } catch (InvocationTargetException error) {\n                    if (session != null && method != null)\n                        throw new FixtureFailure(\"Receiver constructor failed before method invocation\", error.getCause());\n                    throw error;\n                } finally { if (session != null) session.constructing = false; }\n            }\n            if (method == null) {\n                if (FIXTURES.get() == null) return \"constructed:\" + target.getName();\n                try { return snapshot(\"constructed:\" + target.getName() + \":state=\" + FIXTURES.get().state()); }\n                catch (ReflectiveOperationException failure) { throw new FixtureFailure(\"Constructor state oracle failed\", failure); }\n            }\n            Object[] values = arguments(parameterTypes, vector, ctorTypes.length * 3);\n            INVOKED.set(true);\n            Object result = method.invoke(receiver, values);\n            if (FIXTURES.get() != null) {\n                FixtureSession session = FIXTURES.get();\n                try {\n                    return snapshot((method.getReturnType() == void.class ? \"void\" : \"value:\" + session.projection(result, 0))\n                            + \"|state=\" + session.state());\n                } catch (ReflectiveOperationException failure) { throw new FixtureFailure(\"Structural oracle failed\", failure); }\n            }\n            return method.getReturnType() == void.class ? \"void\" : snapshot(\"value:\" + value(result));\n        } catch (InvocationTargetException error) {\n            Throwable cause = error.getCause();\n            if (cause instanceof VirtualMachineError || cause instanceof LinkageError || cause instanceof ThreadDeath)\n                throw new IllegalStateException(\"SQA_HARNESS JVM failure\", cause);\n            return \"exception:\" + cause.getClass().getName();\n        } catch (ReflectiveOperationException error) {\n            throw new IllegalStateException(\"SQA_HARNESS reflection failure\", error);\n        } catch (LinkageError error) {\n            throw new IllegalStateException(\"SQA_HARNESS linkage failure\", error);\n        }\n    }\n\n    public static String observeWithPolicy(String className, String constructorTypes, String methodName,\n            String methodTypes, double[] vector, String policy) {\n        if (!EXPLICIT_FIXTURES.equals(policy) && !SCALAR_FIXTURES.equals(policy)\n                && !PILOT_FIXTURES.equals(policy) && !REVIEWED_FIXTURES.equals(policy))\n            throw new IllegalArgumentException(\"Unknown explicit fixture policy\");\n        FIXTURES.set(new FixtureSession(className, methodName, policy));\n        try { return observe(className, constructorTypes, methodName, methodTypes, vector); }\n        finally { FIXTURES.remove(); }\n    }\n\n    public static boolean targetInvoked() { return Boolean.TRUE.equals(INVOKED.get()); }\n\n    private static String descriptor(String className, String ctor, String method, String params, int count) {\n        return \"{\\\"class\\\":\" + quote(className) + \",\\\"constructor_types\\\":\" + quote(ctor)\n            + \",\\\"method\\\":\" + quote(method) + \",\\\"parameter_types\\\":\" + quote(params)\n            + \",\\\"dimensions\\\":\" + Math.max(3, count * 3) + \"}\";\n    }\n\n    private static void discover(String[] classes, List<String> fixtureClasses) {\n        List<String> targets = new ArrayList<String>();\n        List<String> errors = new ArrayList<String>();\n        for (String className : classes) {\n            try {\n                Class<?> target = Class.forName(className, false, SqaProbe.class.getClassLoader());\n                Class<?> receiverType = target;\n                if (Modifier.isAbstract(target.getModifiers())) {\n                    for (String name : fixtureClasses) {\n                        try {\n                            Class<?> candidate = Class.forName(name, false, SqaProbe.class.getClassLoader());\n                            if (!Modifier.isAbstract(candidate.getModifiers()) && target.isAssignableFrom(candidate)\n                                    && candidate.getDeclaredConstructors().length > 0) {\n                                receiverType = candidate;\n                                break;\n                            }\n                        } catch (ClassNotFoundException ignored) { } catch (LinkageError ignored) { }\n                    }\n                }\n                List<Constructor<?>> constructors = new ArrayList<Constructor<?>>();\n                if (!Modifier.isAbstract(receiverType.getModifiers()) && !receiverType.isEnum()) {\n                    Constructor<?>[] all = receiverType.getDeclaredConstructors();\n                    Arrays.sort(all, new Comparator<Constructor<?>>() {\n                        public int compare(Constructor<?> a, Constructor<?> b) { return a.toString().compareTo(b.toString()); }\n                    });\n                    for (Constructor<?> ctor : all) {\n                        if (supportedParameters(ctor.getParameterTypes())) constructors.add(ctor);\n                    }\n                    // Select a constructor before generating inputs; prefer the simplest fixture.\n                    java.util.Collections.sort(constructors, new Comparator<Constructor<?>>() {\n                        public int compare(Constructor<?> a, Constructor<?> b) { return a.getParameterCount() - b.getParameterCount(); }\n                    });\n                }\n                Method[] methods = target.getDeclaredMethods();\n                Arrays.sort(methods, new Comparator<Method>() {\n                    public int compare(Method a, Method b) { return a.toString().compareTo(b.toString()); }\n                });\n                for (Method method : methods) {\n                    if (method.isSynthetic() || method.getName().equals(\"main\")\n                        || method.isBridge() || !supportedParameters(method.getParameterTypes())\n                        ) continue;\n                    if (Modifier.isStatic(method.getModifiers())) {\n                        targets.add(descriptor(className, \"\", method.getName(),\n                            typeNames(method.getParameterTypes()), method.getParameterCount()));\n                    } else if (!constructors.isEmpty()) {\n                        Constructor<?> ctor = constructors.get(0);\n                        targets.add(descriptor(receiverType.getName(), typeNames(ctor.getParameterTypes()), method.getName(),\n                            typeNames(method.getParameterTypes()), ctor.getParameterCount() + method.getParameterCount()));\n                    }\n                }\n                for (Constructor<?> ctor : constructors) {\n                    if (ctor.getParameterCount() > 0)\n                        targets.add(descriptor(receiverType.getName(), typeNames(ctor.getParameterTypes()), \"<init>\", \"\", ctor.getParameterCount()));\n                }\n            } catch (Throwable error) {\n                if (error instanceof VirtualMachineError || error instanceof ThreadDeath) throw (Error)error;\n                errors.add(quote(className + \":\" + error.getClass().getName()));\n            }\n        }\n        System.out.println(\"{\\\"targets\\\":[\" + String.join(\",\", targets) + \"],\\\"errors\\\":[\" + String.join(\",\", errors) + \"]}\");\n    }\n\n    public static void main(String[] args) throws Exception {\n        if (args.length > 0 && args[0].equals(\"discover\")) {\n            int start = 1;\n            List<String> fixtures = new ArrayList<String>();\n            if (args.length > 2 && args[1].equals(\"--fixtures\")) {\n                fixtures = Files.readAllLines(Paths.get(args[2]), StandardCharsets.UTF_8);\n                start = 3;\n            }\n            discover(Arrays.copyOfRange(args, start, args.length), fixtures);\n            return;\n        }\n        if ((args.length != 6 && args.length != 7) || !args[0].equals(\"observe\"))\n            throw new IllegalArgumentException(\"SQA_HARNESS expected discover classes or observe class ctor method types vector\");\n        String[] pieces = args[5].split(\",\");\n        double[] vector = new double[pieces.length];\n        for (int i = 0; i < pieces.length; i++) {\n            vector[i] = Double.parseDouble(pieces[i]);\n            if (!Double.isFinite(vector[i]))\n                throw new IllegalArgumentException(\"SQA_HARNESS nonfinite vector\");\n        }\n        String outcome;\n        try {\n            outcome = args.length == 7 ? observeWithPolicy(args[1], args[2], args[3], args[4], vector, args[6])\n                : observe(args[1], args[2], args[3], args[4], vector);\n        } catch (FixtureFailure failure) {\n            System.out.println(\"SQA_FIXTURE_FAILURE:\" + Base64.getEncoder().encodeToString(failure.getMessage().getBytes(StandardCharsets.UTF_8)));\n            return;\n        }\n        System.out.println(\"SQA_TRACE:{\\\"target_invoked\\\":\" + Boolean.TRUE.equals(INVOKED.get()) + \"}\");\n        System.out.println(\"SQA_RESULT:\" + Base64.getEncoder().encodeToString(outcome.getBytes(StandardCharsets.UTF_8)));\n    }\n}\n",
    "scripts/study/api854/fixture_policy.py": "\"\"\"Predeclared explicit fixture capability filter, never selected by buggy outcomes.\"\"\"\nPOLICY = 'beam-explicit-fixtures-v3-proposal'\nRECIPE_SOURCES = ('algorithms/java/SqaProbe.java', 'scripts/study/api854/fixture_policy.py')\n\n\ndef recipe_document(source_hashes, policy=POLICY):\n    from .common import ROOT, sha256\n    expected = {name: source_hashes[name] for name in RECIPE_SOURCES}\n    if any(sha256(ROOT / name) != value for name, value in expected.items()):\n        raise ValueError('Explicit recipe source differs from protocol')\n    if policy not in {POLICY, POLICY_V4, POLICY_V5, POLICY_V6}:\n        raise ValueError('Unknown explicit fixture policy')\n    return {'schema_version': 1, 'fixture_policy_id': policy, 'source_sha256': expected,\n        'sources': {name: (ROOT / name).read_bytes().decode('utf-8') for name in RECIPE_SOURCES},\n        'scope': 'Same fixture construction/projection knowledge for all four approaches; no execution feedback'}\n\n\ndef validate_recipe(recipe, source_hashes=None, policy=POLICY):\n    from .preparation import digest\n    if (policy not in {POLICY, POLICY_V4, POLICY_V5, POLICY_V6} or not isinstance(recipe, dict) or recipe.get('fixture_policy_id') != policy\n            or not isinstance(recipe.get('sources'), dict) or set(recipe['sources']) != set(RECIPE_SOURCES)\n            or not isinstance(recipe.get('source_sha256'), dict) or set(recipe['source_sha256']) != set(RECIPE_SOURCES)\n            or any(not isinstance(recipe['sources'][name], str)\n                   or digest(recipe['sources'][name].encode('utf-8')) != recipe['source_sha256'][name] for name in RECIPE_SOURCES)):\n        raise ValueError('Explicit recipe source bytes/hash differ')\n    if source_hashes is not None and any(source_hashes.get(name) != recipe['source_sha256'][name] for name in RECIPE_SOURCES):\n        raise ValueError('Explicit recipe source differs from frozen protocol')\n    return True\nPOLICY_V4 = 'beam-explicit-fixtures-v4-proposal'\nPOLICY_V5 = 'beam-explicit-fixtures-v5-proposal'\nPOLICY_V6 = 'beam-explicit-fixtures-v6-development'\n\n# Fixed-source recipes, declared before generation/evaluation. This development\n# version deliberately preserves unsupported declarations as explicit exclusions.\nPILOT_METHODS = {\n    'org.apache.commons.lang3.math.NumberUtils': {'isDigits', 'isNumber', 'max', 'min', 'toByte', 'toDouble',\n        'toFloat', 'toInt', 'toLong', 'toShort', 'createDouble', 'createFloat', 'createInteger', 'createLong',\n        'createNumber', 'createBigDecimal', 'createBigInteger'},\n    'com.fasterxml.jackson.core.io.NumberInput': {'parseAsDouble', 'parseDouble', 'parseAsInt', 'parseInt',\n        'parseBigDecimal', 'parseAsLong', 'parseLong', 'inLongRange'},\n    'com.fasterxml.jackson.core.util.TextBuffer': {'hasTextAsCharacters', 'contentsAsArray', 'contentsAsString',\n        'getCurrentSegmentSize', 'getTextOffset', 'size', 'toString', 'append', 'resetWithEmpty', 'resetWithString'},\n    'org.apache.commons.math3.fraction.BigFraction': {'equals', 'doubleValue', 'percentageValue', 'floatValue',\n        'getDenominator', 'getNumerator', 'intValue', 'longValue', 'toString', 'abs', 'add', 'subtract',\n        'multiply', 'divide', 'negate', 'reciprocal', 'reduce', 'compareTo'},\n    'org.apache.commons.math3.fraction.Fraction': {'equals', 'doubleValue', 'percentageValue', 'floatValue',\n        'getDenominator', 'getNumerator', 'intValue', 'longValue', 'toString', 'abs', 'add', 'subtract',\n        'multiply', 'divide', 'negate', 'reciprocal', 'compareTo'},\n    'org.jsoup.nodes.Document': {'nodeName', 'outerHtml', 'title', 'normalise', 'body', 'head', 'text', 'createElement', 'createShell'},\n    'org.apache.commons.cli.CommandLine': {'hasOption', 'getOptionObject', 'getOptionValue', 'getArgs',\n        'getOptionValues', 'iterator', 'getArgList', 'getOptions', 'addArg', 'addOption'},\n    'org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream': {'ensureOpen', 'writeCString',\n        'close', 'closeArchiveEntry', 'finish', 'putArchiveEntry', 'putNextEntry', 'write'},\n    'org.joda.time.field.UnsupportedDurationField': {'equals', 'isPrecise', 'isSupported', 'getType',\n        'getName', 'toString', 'getUnitMillis', 'compareTo', 'getInstance'},\n    'org.joda.time.Partial': {'size', 'getValues', 'toStringList', 'with', 'withField', 'without'},\n    'com.google.gson.TypeInfoFactory': {'getIndex', 'getActualType', 'extractRealTypes', 'getTypeInfoForField', 'getTypeInfoForArray'},\n    'com.google.javascript.jscomp.RemoveUnusedVars': {'process', 'traverseAndRemoveUnusedReferences', 'getFunctionArgList'},\n    'org.jfree.chart.renderer.category.AreaRenderer': {'findRangeBounds', 'getRowCount', 'getColumnCount',\n        'getPassCount', 'getLegendItems', 'getLegendItem', 'getItemMiddle'},\n    'com.fasterxml.jackson.databind.ser.BeanPropertyWriter': {'getName', 'getSerializedName', 'getType',\n        'getPropertyType', 'getGenericPropertyType', 'isRequired', 'willSuppressNulls', 'hasSerializer',\n        'hasNullSerializer', 'rename', 'get', 'getInternalSetting', 'setInternalSetting', 'removeInternalSetting'},\n    'com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer': {'deserialize', 'deserializeUsingCustom', 'handleNonArray'},\n    'com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser': {'hasTextCharacters', 'isClosed', 'isExpectedStartArrayToken',\n        'requiresCustomCodec', 'getTextCharacters', 'nextToken', 'getText', 'getCurrentName', 'getValueAsString',\n        'nextTextValue', 'close', 'overrideCurrentName', 'setXMLTextElementName'},\n    'org.mockito.internal.invocation.InvocationMatcher': {'matches', 'hasSameMethod', 'hasSimilarMethod',\n        'getMethod', 'getInvocation', 'toString'},\n}\nPILOT_TYPES = {'com.fasterxml.jackson.core.util.BufferRecycler', 'org.apache.commons.math3.fraction.BigFraction',\n    'org.apache.commons.math3.fraction.Fraction', 'java.math.BigInteger', 'org.apache.commons.cli.Option',\n    'java.io.OutputStream', 'org.apache.commons.compress.archivers.ArchiveEntry',\n    'org.apache.commons.compress.archivers.cpio.CpioArchiveEntry', 'org.joda.time.DurationFieldType',\n    'org.joda.time.DurationField', 'org.joda.time.DateTimeFieldType', 'java.lang.reflect.Type',\n    'java.lang.reflect.TypeVariable', '[Ljava.lang.reflect.Type;', '[Ljava.lang.reflect.TypeVariable;',\n    'java.lang.reflect.Field', 'java.lang.Class', 'com.google.javascript.jscomp.AbstractCompiler',\n    'com.google.javascript.rhino.Node', 'org.jfree.data.category.CategoryDataset', 'org.jfree.chart.axis.CategoryAxis',\n    'java.lang.Comparable', 'java.awt.geom.Rectangle2D', 'org.jfree.chart.util.RectangleEdge',\n    'com.fasterxml.jackson.databind.ser.BeanPropertyWriter', 'com.fasterxml.jackson.databind.util.NameTransformer',\n    'com.fasterxml.jackson.databind.JavaType', 'com.fasterxml.jackson.databind.JsonDeserializer',\n    'com.fasterxml.jackson.databind.deser.ValueInstantiator', 'com.fasterxml.jackson.core.JsonParser',\n    'com.fasterxml.jackson.databind.DeserializationContext', 'com.fasterxml.jackson.core.io.IOContext',\n    'com.fasterxml.jackson.core.ObjectCodec', 'javax.xml.stream.XMLStreamReader', 'org.mockito.invocation.Invocation'}\n\n# Added capability recipes are fixed before any buggy evaluation. Mutators need\n# structural post-state; unsupported helpers/serialization hooks stay excluded.\nADDITIONAL_METHODS = {\n    'org.apache.commons.codec.language.Caverphone': {'isCaverphoneEqual', 'encode', 'caverphone'},\n    'org.apache.commons.codec.language.Metaphone': {'isLastChar', 'isMetaphoneEqual', 'getMaxCodeLen', 'encode', 'metaphone'},\n    'org.apache.commons.codec.language.SoundexUtils': {'differenceEncoded', 'clean'},\n    'org.apache.commons.collections.map.Flat3Map': {'containsKey', 'containsValue', 'equals', 'isEmpty', 'size',\n        'clone', 'get', 'put', 'remove', 'toString', 'clear', 'putAll'},\n    'org.apache.commons.csv.ExtendedBufferedReader': {'getLineNumber', 'lookAhead', 'readAgain', 'read', 'readLine'},\n}\n\nSCALARS = {'boolean', 'byte', 'short', 'int', 'long', 'float', 'double', 'char',\n           'java.lang.String', 'java.lang.Boolean', 'java.lang.Byte', 'java.lang.Short',\n           'java.lang.Integer', 'java.lang.Long', 'java.lang.Float', 'java.lang.Double',\n           'java.lang.Character', 'java.lang.Object', 'java.lang.Number', 'java.util.Date',\n           'java.util.Locale', 'java.util.List', 'java.util.Collection', 'java.lang.Iterable',\n           'java.util.Iterator', 'java.util.Map', 'java.util.Set'}\nCLOSURE = {'com.google.javascript.jscomp.AbstractCompiler', 'com.google.javascript.jscomp.ControlFlowGraph',\n           'com.google.javascript.jscomp.type.ReverseAbstractInterpreter', 'com.google.javascript.jscomp.Scope',\n           'com.google.javascript.jscomp.Scope$Var', 'com.google.javascript.jscomp.type.FlowScope',\n           'com.google.javascript.rhino.Node', 'com.google.javascript.rhino.jstype.JSType',\n           'com.google.javascript.rhino.jstype.ObjectType', 'com.google.javascript.rhino.jstype.JSTypeNative',\n           'com.google.javascript.rhino.jstype.BooleanLiteralSet'}\nJXPATH = {'org.w3c.dom.Node', 'org.w3c.dom.Document', 'org.w3c.dom.Element',\n          'org.apache.commons.jxpath.ri.QName', 'org.apache.commons.jxpath.ri.compiler.NodeTest',\n          'org.apache.commons.jxpath.ri.model.NodePointer'}\n# Methods requiring specialized AST parent/sibling/call metadata have no reviewed\n# recipe yet. This list is a structural restriction, not an outcome-based prune.\nCLOSURE_METHODS = {'createEntryLattice', 'createInitialEstimateLattice', 'flowThrough',\n    'branchedFlowThrough', 'isAddedAsNumber', 'isUnflowable', 'newBooleanOutcomePair',\n    'traverseAnd', 'traverseOr', 'traverseShortCircuitingBinOp', 'traverseWithinShortCircuitingBinOp',\n    'narrowScope', 'traverse', 'traverseAdd', 'traverseArrayLiteral', 'traverseAssign',\n    'traverseChildren', 'traverseGetElem', 'traverseGetProp', 'traverseHook', 'traverseName',\n    'traverseObjectLiteral', 'traverseReturn', 'getJSType', 'getNativeType',\n    'redeclareSimpleVar', 'updateScopeForTypeChange', 'getBooleanOutcomes'}\n\n\ndef select(targets, policy):\n    if policy is None:\n        return targets, []\n    if policy not in {POLICY, POLICY_V4, POLICY_V5, POLICY_V6}:\n        raise ValueError('Unknown explicit fixture policy')\n    selected, excluded = [], []\n    for target in targets:\n        name = target['class']\n        family = CLOSURE if name == 'com.google.javascript.jscomp.TypeInference' else JXPATH if name in {\n            'org.apache.commons.jxpath.ri.model.dom.DOMNodePointer',\n            'org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer'} else set()\n        extra = policy in {POLICY_V4, POLICY_V5, POLICY_V6} and name in ADDITIONAL_METHODS\n        pilot = policy in {POLICY_V5, POLICY_V6} and name in PILOT_METHODS\n        setter = (policy == POLICY_V6 and name == 'org.apache.commons.codec.language.Metaphone'\n                  and target['method'] == 'setMaxCodeLen' and target['constructor_types'] == ''\n                  and target['parameter_types'] == 'int')\n        if extra:\n            family = {'java.io.Reader'}\n        if pilot:\n            family = PILOT_TYPES | {'[B', '[S', '[I', '[J', '[F', '[D', '[C'}\n        reason = None\n        if not family:\n            reason = 'explicit_project_recipe_not_reviewed'\n        elif target['method'] in {'<init>', 'hashCode'}:\n            reason = 'constructor_or_identity_oracle_not_reviewed'\n        elif family is CLOSURE and target['method'] not in CLOSURE_METHODS:\n            reason = 'specialized_ast_recipe_not_reviewed'\n        elif extra and target['method'] not in ADDITIONAL_METHODS[name] and not setter:\n            reason = 'additional_method_preconditions_or_state_not_reviewed'\n        elif extra and name.endswith('ExtendedBufferedReader') and target['parameter_types']:\n            reason = 'reader_buffer_offset_bounds_recipe_not_reviewed'\n        elif pilot and target['method'] not in PILOT_METHODS[name]:\n            reason = 'pilot_method_preconditions_or_oracle_not_reviewed'\n        elif pilot and name.endswith('NumberInput') and '[' in target['parameter_types']:\n            reason = 'numeric_buffer_slice_recipe_not_reviewed'\n        elif pilot and name.endswith('TextBuffer') and target['method'] == 'append' and target['parameter_types'] != 'char':\n            reason = 'text_buffer_slice_recipe_not_reviewed'\n        elif pilot and name.endswith('Document') and target['parameter_types'] == 'org.jsoup.nodes.Element':\n            reason = 'html_internal_normalise_recipe_not_reviewed'\n        elif pilot and name.endswith('CpioArchiveOutputStream') and target['method'] == 'write' and target['parameter_types'] != 'int':\n            reason = 'archive_buffer_slice_recipe_not_reviewed'\n        elif pilot and name.endswith('BeanPropertyWriter') and target['method'] == 'isRequired' and target['parameter_types']:\n            reason = 'annotation_introspector_recipe_not_reviewed'\n        elif pilot and name.endswith('RemoveUnusedVars') and target['method'] == 'process' and target['parameter_types'] != 'com.google.javascript.rhino.Node,com.google.javascript.rhino.Node':\n            reason = 'call_site_definition_finder_recipe_not_reviewed'\n        else:\n            required = set(filter(None, (target['constructor_types'] + ',' + target['parameter_types']).split(',')))\n            missing = required - SCALARS - family\n            if missing:\n                reason = 'explicit_argument_recipe_missing:' + ','.join(sorted(missing))\n        if reason:\n            excluded.append({'target': target, 'reason': reason})\n        else:\n            selected.append(target)\n    return selected, excluded\n"
  }
}
```
