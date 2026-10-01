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

Project: Codec
Fixed reference revision: 1f
Target classes:
- org.apache.commons.codec.language.Caverphone
- org.apache.commons.codec.language.Metaphone
- org.apache.commons.codec.language.SoundexUtils

## Production source src/java/org/apache/commons/codec/language/Caverphone.java

```java
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

package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

/**
 * Encodes a string into a caverphone value. 
 *
 * This is an algorithm created the Caversham Project at the University of Otago. 
 * It implements the Caverphone 2.0 algorithm:
 *
 *
 * @author Apache Software Foundation
 * @version $Id$
 * @see <a href="http://en.wikipedia.org/wiki/Caverphone">Wikipedia - Caverphone</a>
 * @see <a href="http://caversham.otago.ac.nz/files/working/ctp150804.pdf">Caverphone 2.0 specification</a>
 */
public class Caverphone implements StringEncoder {

    /**
     * Creates an instance of the Caverphone encoder
     */
    public Caverphone() {
        super();
    }

    /**
     * Find the caverphone value of a String. 
     *
     * @param txt String to find the caverphone code for
     * @return A caverphone code corresponding to the String supplied
     */
    public String caverphone(String txt) {
        // NOTE: Version 1.0 of Caverphone is easily derivable from this code 
        // by commenting out the 2.0 lines and adding in the 1.0 lines

        if( txt == null || txt.length() == 0 ) {
            return "1111111111";
        }

        // 1. Convert to lowercase
        txt = txt.toLowerCase(java.util.Locale.ENGLISH);

        // 2. Remove anything not A-Z
        txt = txt.replaceAll("[^a-z]", "");

        // 2.5. Remove final e
        txt = txt.replaceAll("e$", "");             // 2.0 only

        // 3. Handle various start options
        txt = txt.replaceAll("^cough", "cou2f");
        txt = txt.replaceAll("^rough", "rou2f");
        txt = txt.replaceAll("^tough", "tou2f");
        txt = txt.replaceAll("^enough", "enou2f");  // 2.0 only
        txt = txt.replaceAll("^trough", "trou2f");  // 2.0 only - note the spec says ^enough here again, c+p error I assume
        txt = txt.replaceAll("^gn", "2n");
        txt = txt.replaceAll("^mb", "m2");

        // 4. Handle replacements
        txt = txt.replaceAll("cq", "2q");
        txt = txt.replaceAll("ci", "si");
        txt = txt.replaceAll("ce", "se");
        txt = txt.replaceAll("cy", "sy");
        txt = txt.replaceAll("tch", "2ch");
        txt = txt.replaceAll("c", "k");
        txt = txt.replaceAll("q", "k");
        txt = txt.replaceAll("x", "k");
        txt = txt.replaceAll("v", "f");
        txt = txt.replaceAll("dg", "2g");
        txt = txt.replaceAll("tio", "sio");
        txt = txt.replaceAll("tia", "sia");
        txt = txt.replaceAll("d", "t");
        txt = txt.replaceAll("ph", "fh");
        txt = txt.replaceAll("b", "p");
        txt = txt.replaceAll("sh", "s2");
        txt = txt.replaceAll("z", "s");
        txt = txt.replaceAll("^[aeiou]", "A");
        txt = txt.replaceAll("[aeiou]", "3");
        txt = txt.replaceAll("j", "y");        // 2.0 only
        txt = txt.replaceAll("^y3", "Y3");     // 2.0 only
        txt = txt.replaceAll("^y", "A");       // 2.0 only
        txt = txt.replaceAll("y", "3");        // 2.0 only
        txt = txt.replaceAll("3gh3", "3kh3");
        txt = txt.replaceAll("gh", "22");
        txt = txt.replaceAll("g", "k");
        txt = txt.replaceAll("s+", "S");
        txt = txt.replaceAll("t+", "T");
        txt = txt.replaceAll("p+", "P");
        txt = txt.replaceAll("k+", "K");
        txt = txt.replaceAll("f+", "F");
        txt = txt.replaceAll("m+", "M");
        txt = txt.replaceAll("n+", "N");
        txt = txt.replaceAll("w3", "W3");
        //txt = txt.replaceAll("wy", "Wy");    // 1.0 only
        txt = txt.replaceAll("wh3", "Wh3");
        txt = txt.replaceAll("w$", "3");       // 2.0 only
        //txt = txt.replaceAll("why", "Why");  // 1.0 only
        txt = txt.replaceAll("w", "2");
        txt = txt.replaceAll("^h", "A");
        txt = txt.replaceAll("h", "2");
        txt = txt.replaceAll("r3", "R3");
        txt = txt.replaceAll("r$", "3");       // 2.0 only
        //txt = txt.replaceAll("ry", "Ry");    // 1.0 only
        txt = txt.replaceAll("r", "2");
        txt = txt.replaceAll("l3", "L3");
        txt = txt.replaceAll("l$", "3");       // 2.0 only
        //txt = txt.replaceAll("ly", "Ly");    // 1.0 only
        txt = txt.replaceAll("l", "2");
        //txt = txt.replaceAll("j", "y");      // 1.0 only
        //txt = txt.replaceAll("y3", "Y3");    // 1.0 only
        //txt = txt.replaceAll("y", "2");      // 1.0 only

        // 5. Handle removals
        txt = txt.replaceAll("2", "");
        txt = txt.replaceAll("3$", "A");       // 2.0 only
        txt = txt.replaceAll("3", "");

        // 6. put ten 1s on the end
        txt = txt + "111111" + "1111";        // 1.0 only has 6 1s

        // 7. take the first six characters as the code
        return txt.substring(0, 10);          // 1.0 truncates to 6
    }

    /**
     * Encodes an Object using the caverphone algorithm.  This method
     * is provided in order to satisfy the requirements of the
     * Encoder interface, and will throw an EncoderException if the
     * supplied object is not of type java.lang.String.
     *
     * @param pObject Object to encode
     * @return An object (or type java.lang.String) containing the 
     *         caverphone code which corresponds to the String supplied.
     * @throws EncoderException if the parameter supplied is not
     *                          of type java.lang.String
     */
    public Object encode(Object pObject) throws EncoderException {
        if (!(pObject instanceof java.lang.String)) {
            throw new EncoderException("Parameter supplied to Caverphone encode is not of type java.lang.String"); 
        }
        return caverphone((String) pObject);
    }

    /**
     * Encodes a String using the Caverphone algorithm. 
     *
     * @param pString String object to encode
     * @return The caverphone code corresponding to the String supplied
     */
    public String encode(String pString) {
        return caverphone(pString);   
    }

    /**
     * Tests if the caverphones of two strings are identical.
     *
     * @param str1 First of two strings to compare
     * @param str2 Second of two strings to compare
     * @return <code>true</code> if the caverphones of these strings are identical, 
     *        <code>false</code> otherwise.
     */
    public boolean isCaverphoneEqual(String str1, String str2) {
        return caverphone(str1).equals(caverphone(str2));
    }

}

```

## Production source src/java/org/apache/commons/codec/language/Metaphone.java

```java
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

package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

/**
 * Encodes a string into a metaphone value. 
 * <p>
 * Initial Java implementation by <CITE>William B. Brogden. December, 1997</CITE>. 
 * Permission given by <CITE>wbrogden</CITE> for code to be used anywhere.
 * </p>
 * <p>
 * <CITE>Hanging on the Metaphone</CITE> by <CITE>Lawrence Philips</CITE> in <CITE>Computer Language of Dec. 1990, p
 * 39.</CITE>
 * </p>
 * <p>
 * Note, that this does not match the algorithm that ships with PHP, or the algorithm 
 * found in the Perl <a href="http://search.cpan.org/~mschwern/Text-Metaphone-1.96/Metaphone.pm">Text:Metaphone-1.96</a>.
 * They have had undocumented changes from the originally published algorithm. 
 * </p>
 * 
 * @author Apache Software Foundation
 * @version $Id$
 */
public class Metaphone implements StringEncoder {

    /**
     * Five values in the English language 
     */
    private static final String VOWELS = "AEIOU" ;

    /**
     * Variable used in Metaphone algorithm
     */
    private static final String FRONTV = "EIY"   ;

    /**
     * Variable used in Metaphone algorithm
     */
    private static final String VARSON = "CSPTG" ;

    /**
     * The max code length for metaphone is 4
     */
    private int maxCodeLen = 4 ;

    /**
     * Creates an instance of the Metaphone encoder
     */
    public Metaphone() {
        super();
    }

    /**
     * Find the metaphone value of a String. This is similar to the
     * soundex algorithm, but better at finding similar sounding words.
     * All input is converted to upper case.
     * Limitations: Input format is expected to be a single ASCII word
     * with only characters in the A - Z range, no punctuation or numbers.
     *
     * @param txt String to find the metaphone code for
     * @return A metaphone code corresponding to the String supplied
     */
    public String metaphone(String txt) {
        boolean hard = false ;
        if ((txt == null) || (txt.length() == 0)) {
            return "" ;
        }
        // single character is itself
        if (txt.length() == 1) {
            return txt.toUpperCase(java.util.Locale.ENGLISH) ;
        }
      
        char[] inwd = txt.toUpperCase(java.util.Locale.ENGLISH).toCharArray() ;
      
        StringBuffer local = new StringBuffer(40); // manipulate
        StringBuffer code = new StringBuffer(10) ; //   output
        // handle initial 2 characters exceptions
        switch(inwd[0]) {
        case 'K' : 
        case 'G' : 
        case 'P' : /* looking for KN, etc*/
            if (inwd[1] == 'N') {
                local.append(inwd, 1, inwd.length - 1);
            } else {
                local.append(inwd);
            }
            break;
        case 'A': /* looking for AE */
            if (inwd[1] == 'E') {
                local.append(inwd, 1, inwd.length - 1);
            } else {
                local.append(inwd);
            }
            break;
        case 'W' : /* looking for WR or WH */
            if (inwd[1] == 'R') {   // WR -> R
                local.append(inwd, 1, inwd.length - 1); 
                break ;
            }
            if (inwd[1] == 'H') {
                local.append(inwd, 1, inwd.length - 1);
                local.setCharAt(0, 'W'); // WH -> W
            } else {
                local.append(inwd);
            }
            break;
        case 'X' : /* initial X becomes S */
            inwd[0] = 'S';
            local.append(inwd);
            break ;
        default :
            local.append(inwd);
        } // now local has working string with initials fixed

        int wdsz = local.length();
        int n = 0 ;

        while ((code.length() < this.getMaxCodeLen()) && 
        	   (n < wdsz) ) { // max code size of 4 works well
            char symb = local.charAt(n) ;
            // remove duplicate letters except C
            if ((symb != 'C') && (isPreviousChar( local, n, symb )) ) {
                n++ ;
            } else { // not dup
                switch(symb) {
                case 'A' : case 'E' : case 'I' : case 'O' : case 'U' :
                    if (n == 0) { 
                        code.append(symb);
                    }
                    break ; // only use vowel if leading char
                case 'B' :
                    if ( isPreviousChar(local, n, 'M') && 
                         isLastChar(wdsz, n) ) { // B is silent if word ends in MB
						break;
                    }
                    code.append(symb);
                    break;
                case 'C' : // lots of C special cases
                    /* discard if SCI, SCE or SCY */
                    if ( isPreviousChar(local, n, 'S') && 
                         !isLastChar(wdsz, n) && 
                         (FRONTV.indexOf(local.charAt(n + 1)) >= 0) ) { 
                        break;
                    }
                    if (regionMatch(local, n, "CIA")) { // "CIA" -> X
                        code.append('X'); 
                        break;
                    }
                    if (!isLastChar(wdsz, n) && 
                        (FRONTV.indexOf(local.charAt(n + 1)) >= 0)) {
                        code.append('S');
                        break; // CI,CE,CY -> S
                    }
                    if (isPreviousChar(local, n, 'S') &&
						isNextChar(local, n, 'H') ) { // SCH->sk
                        code.append('K') ; 
                        break ;
                    }
                    if (isNextChar(local, n, 'H')) { // detect CH
                        if ((n == 0) && 
                        	(wdsz >= 3) && 
                            isVowel(local,2) ) { // CH consonant -> K consonant
                            code.append('K');
                        } else { 
                            code.append('X'); // CHvowel -> X
                        }
                    } else { 
                        code.append('K');
                    }
                    break ;
                case 'D' :
                    if (!isLastChar(wdsz, n + 1) && 
                        isNextChar(local, n, 'G') && 
                        (FRONTV.indexOf(local.charAt(n + 2)) >= 0)) { // DGE DGI DGY -> J 
                        code.append('J'); n += 2 ;
                    } else { 
                        code.append('T');
                    }
                    break ;
                case 'G' : // GH silent at end or before consonant
                    if (isLastChar(wdsz, n + 1) && 
                        isNextChar(local, n, 'H')) {
                        break;
                    }
                    if (!isLastChar(wdsz, n + 1) &&  
                        isNextChar(local,n,'H') && 
                        !isVowel(local,n+2)) {
                        break;
                    }
                    if ((n > 0) && 
                    	( regionMatch(local, n, "GN") ||
					      regionMatch(local, n, "GNED") ) ) {
                        break; // silent G
                    }
                    if (isPreviousChar(local, n, 'G')) {
                        hard = true ;
                    } else {
                        hard = false ;
                    }
                    if (!isLastChar(wdsz, n) && 
                        (FRONTV.indexOf(local.charAt(n + 1)) >= 0) && 
                        (!hard)) {
                        code.append('J');
                    } else {
                        code.append('K');
                    }
                    break ;
                case 'H':
                    if (isLastChar(wdsz, n)) {
                        break ; // terminal H
                    }
                    if ((n > 0) && 
                        (VARSON.indexOf(local.charAt(n - 1)) >= 0)) {
                        break;
                    }
                    if (isVowel(local,n+1)) {
                        code.append('H'); // Hvowel
                    }
                    break;
                case 'F': 
                case 'J' : 
                case 'L' :
                case 'M': 
                case 'N' : 
                case 'R' :
                    code.append(symb); 
                    break;
                case 'K' :
                    if (n > 0) { // not initial
                        if (!isPreviousChar(local, n, 'C')) {
                            code.append(symb);
                        }
                    } else {
                        code.append(symb); // initial K
                    }
                    break ;
                case 'P' :
                    if (isNextChar(local,n,'H')) {
                        // PH -> F
                        code.append('F');
                    } else {
                        code.append(symb);
                    }
                    break ;
                case 'Q' :
                    code.append('K');
                    break;
                case 'S' :
                    if (regionMatch(local,n,"SH") || 
					    regionMatch(local,n,"SIO") || 
					    regionMatch(local,n,"SIA")) {
                        code.append('X');
                    } else {
                        code.append('S');
                    }
                    break;
                case 'T' :
                    if (regionMatch(local,n,"TIA") || 
						regionMatch(local,n,"TIO")) {
                        code.append('X'); 
                        break;
                    }
                    if (regionMatch(local,n,"TCH")) {
						// Silent if in "TCH"
                        break;
                    }
                    // substitute numeral 0 for TH (resembles theta after all)
                    if (regionMatch(local,n,"TH")) {
                        code.append('0');
                    } else {
                        code.append('T');
                    }
                    break ;
                case 'V' :
                    code.append('F'); break ;
                case 'W' : case 'Y' : // silent if not followed by vowel
                    if (!isLastChar(wdsz,n) && 
                    	isVowel(local,n+1)) {
                        code.append(symb);
                    }
                    break ;
                case 'X' :
                    code.append('K'); code.append('S');
                    break ;
                case 'Z' :
                    code.append('S'); break ;
                } // end switch
                n++ ;
            } // end else from symb != 'C'
            if (code.length() > this.getMaxCodeLen()) { 
            	code.setLength(this.getMaxCodeLen()); 
            }
        }
        return code.toString();
    }

	private boolean isVowel(StringBuffer string, int index) {
		return VOWELS.indexOf(string.charAt(index)) >= 0;
	}

	private boolean isPreviousChar(StringBuffer string, int index, char c) {
		boolean matches = false;
		if( index > 0 &&
		    index < string.length() ) {
			matches = string.charAt(index - 1) == c;
		}
		return matches;
	}

	private boolean isNextChar(StringBuffer string, int index, char c) {
		boolean matches = false;
		if( index >= 0 &&
		    index < string.length() - 1 ) {
			matches = string.charAt(index + 1) == c;
		}
		return matches;
	}

	private boolean regionMatch(StringBuffer string, int index, String test) {
		boolean matches = false;
		if( index >= 0 &&
		    (index + test.length() - 1) < string.length() ) {
			String substring = string.substring( index, index + test.length());
			matches = substring.equals( test );
		}
		return matches;
	}

	private boolean isLastChar(int wdsz, int n) {
		return n + 1 == wdsz;
	} 
    
    
    /**
     * Encodes an Object using the metaphone algorithm.  This method
     * is provided in order to satisfy the requirements of the
     * Encoder interface, and will throw an EncoderException if the
     * supplied object is not of type java.lang.String.
     *
     * @param pObject Object to encode
     * @return An object (or type java.lang.String) containing the 
     *         metaphone code which corresponds to the String supplied.
     * @throws EncoderException if the parameter supplied is not
     *                          of type java.lang.String
     */
    public Object encode(Object pObject) throws EncoderException {
        if (!(pObject instanceof java.lang.String)) {
            throw new EncoderException("Parameter supplied to Metaphone encode is not of type java.lang.String"); 
        }
        return metaphone((String) pObject);
    }

    /**
     * Encodes a String using the Metaphone algorithm. 
     *
     * @param pString String object to encode
     * @return The metaphone code corresponding to the String supplied
     */
    public String encode(String pString) {
        return metaphone(pString);   
    }

    /**
     * Tests is the metaphones of two strings are identical.
     *
     * @param str1 First of two strings to compare
     * @param str2 Second of two strings to compare
     * @return <code>true</code> if the metaphones of these strings are identical, 
     *        <code>false</code> otherwise.
     */
    public boolean isMetaphoneEqual(String str1, String str2) {
        return metaphone(str1).equals(metaphone(str2));
    }

    /**
     * Returns the maxCodeLen.
     * @return int
     */
    public int getMaxCodeLen() { return this.maxCodeLen; }

    /**
     * Sets the maxCodeLen.
     * @param maxCodeLen The maxCodeLen to set
     */
    public void setMaxCodeLen(int maxCodeLen) { this.maxCodeLen = maxCodeLen; }

}

```

## Production source src/java/org/apache/commons/codec/language/SoundexUtils.java

```java
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

package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

/**
 * Utility methods for {@link Soundex} and {@link RefinedSoundex} classes.
 * 
 * @author Apache Software Foundation
 * @version $Id$
 * @since 1.3
 */
final class SoundexUtils {

    /**
	 * Cleans up the input string before Soundex processing by only returning
	 * upper case letters.
	 * 
	 * @param str
	 *                  The String to clean.
	 * @return A clean String.
	 */
    static String clean(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        int len = str.length();
        char[] chars = new char[len];
        int count = 0;
        for (int i = 0; i < len; i++) {
            if (Character.isLetter(str.charAt(i))) {
                chars[count++] = str.charAt(i);
            }
        }
        if (count == len) {
            return str.toUpperCase(java.util.Locale.ENGLISH);
        }
        return new String(chars, 0, count).toUpperCase(java.util.Locale.ENGLISH);
    }

    /**
	 * Encodes the Strings and returns the number of characters in the two
	 * encoded Strings that are the same.
	 * <ul>
	 * <li>For Soundex, this return value ranges from 0 through 4: 0 indicates
	 * little or no similarity, and 4 indicates strong similarity or identical
	 * values.</li>
	 * <li>For refined Soundex, the return value can be greater than 4.</li>
	 * </ul>
	 * 
	 * @param encoder
	 *                  The encoder to use to encode the Strings.
	 * @param s1
	 *                  A String that will be encoded and compared.
	 * @param s2
	 *                  A String that will be encoded and compared.
	 * @return The number of characters in the two Soundex encoded Strings that
	 *             are the same.
	 * 
	 * @see #differenceEncoded(String,String)
	 * @see <a href="http://msdn.microsoft.com/library/default.asp?url=/library/en-us/tsqlref/ts_de-dz_8co5.asp">
	 *          MS T-SQL DIFFERENCE</a>
	 * 
	 * @throws EncoderException
	 *                  if an error occurs encoding one of the strings
	 */
    static int difference(StringEncoder encoder, String s1, String s2) throws EncoderException {
        return differenceEncoded(encoder.encode(s1), encoder.encode(s2));
    }

    /**
	 * Returns the number of characters in the two Soundex encoded Strings that
	 * are the same.
	 * <ul>
	 * <li>For Soundex, this return value ranges from 0 through 4: 0 indicates
	 * little or no similarity, and 4 indicates strong similarity or identical
	 * values.</li>
	 * <li>For refined Soundex, the return value can be greater than 4.</li>
	 * </ul>
	 * 
	 * @param es1
	 *                  An encoded String.
	 * @param es2
	 *                  An encoded String.
	 * @return The number of characters in the two Soundex encoded Strings that
	 *             are the same.
	 * 
	 * @see <a href="http://msdn.microsoft.com/library/default.asp?url=/library/en-us/tsqlref/ts_de-dz_8co5.asp">
	 *          MS T-SQL DIFFERENCE</a>
	 */
    static int differenceEncoded(String es1, String es2) {

        if (es1 == null || es2 == null) {
            return 0;
        }
        int lengthToMatch = Math.min(es1.length(), es2.length());
        int diff = 0;
        for (int i = 0; i < lengthToMatch; i++) {
            if (es1.charAt(i) == es2.charAt(i)) {
                diff++;
            }
        }
        return diff;
    }

}

```

## Build configuration build.xml

```text
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
<project name="Codec" default="compile" basedir=".">
    <!--
        "Codec" component of the Apache Commons Subproject
        $Id$
    -->
    <!-- ========== Initialize Properties ===================================== -->
    <property file="${user.home}/${component.name}.build.properties"/>
    <property file="${user.home}/build.properties"/>
    <property file="${basedir}/build.properties"/>
    <property file="${basedir}/default.properties"/>
    <!-- ========== Construct compile classpath =============================== -->
    <path id="compile.classpath">
        <pathelement location="${build.home}/classes"/>
    </path>
    <!-- ========== Construct unit test classpath ============================= -->
    <path id="test.classpath">
        <pathelement location="${build.home}/classes"/>
        <pathelement location="${build.home}/tests"/>
        <pathelement location="${junit.jar}"/>
    </path>
    <!-- ========== Executable Targets ======================================== -->
    <target name="init" description="Initialize and evaluate conditionals">
        <echo message="-------- ${component.name} ${component.version} --------"/>
        <filter token="name" value="${component.name}"/>
        <filter token="package" value="${component.package}"/>
        <filter token="version" value="${component.version}"/>
    </target>
    <target name="prepare" depends="init" description="Prepare build directory">
        <mkdir dir="${build.home}"/>
        <mkdir dir="${build.home}/classes"/>
        <mkdir dir="${build.home}/conf"/>
        <mkdir dir="${build.home}/tests"/>
        <mkdir dir="${build.home}/test-reports"/>
    </target>
    <target name="static" depends="prepare" description="Copy static files to build directory">
        <tstamp/>
        <copy todir="${build.home}/conf" filtering="on">
            <fileset dir="${conf.home}" includes="*.MF"/>
        </copy>
    </target>
    <target name="compile" depends="static" description="Compile shareable components">
        <javac srcdir="${source.home}" destdir="${build.home}/classes" debug="${compile.debug}" target="1.6" source="1.6" deprecation="${compile.deprecation}" optimize="${compile.optimize}">
            <classpath refid="compile.classpath"/>
        </javac>
        <copy todir="${build.home}/classes" filtering="on">
            <fileset dir="${source.home}" excludes="**/*.java"/>
        </copy>
    </target>
    <target name="clean" description="Clean build and distribution directories">
        <delete dir="${build.home}"/>
        <delete dir="${dist.home}"/>
        <delete dir="${pub.home}"/>
    </target>
    <target name="all" depends="clean,compile" description="Clean and compile all components"/>
    <target name="javadoc" depends="compile" description="Create component Javadoc documentation">
        <mkdir dir="${dist.home}"/>
        <mkdir dir="${dist.home}/docs"/>
        <mkdir dir="${dist.home}/docs/api"/>
        <mkdir dir="${build.home}/apidocs"/>
        <tstamp>
            <format property="current.year" pattern="yyyy"/>
        </tstamp>
         <!-- The Sun 1.2 docs are no-longer on-line, point to 1.3. -->
        <javadoc 
        	sourcepath="${source.home}" 
        	destdir="${dist.home}/docs/api" 
        	packagenames="org.apache.commons.*" 
	        overview="${source.home}/org/apache/commons/codec/overview.html"
        	author="true" 
        	private="true" 
        	version="true" 
        	doctitle="&lt;h1&gt;${component.title}&lt;/h1&gt;" 
        	windowtitle="${component.title} (Version ${component.version})" 
        	bottom="${component.name} version ${component.version} - Copyright &amp;copy; 2002-${current.year} - Apache Software Foundation" 
        	use="true" 
        	link="http://java.sun.com/products/jdk/1.3/docs/api/">
            <classpath refid="compile.classpath"/>
        </javadoc>
    </target>
    <target name="dist" depends="compile,javadoc" description="Create binary distribution">
        <mkdir dir="${dist.home}"/>
        <copy file="${basedir}/LICENSE.txt" todir="${dist.home}"/>
        <copy file="${basedir}/RELEASE-NOTES.txt" todir="${dist.home}"/>
        <antcall target="jar"/>
    </target>
    <target name="jar" depends="compile" description="Create jar">
        <mkdir dir="${dist.home}"/>
        <mkdir dir="${build.home}/classes/META-INF"/>
        <copy file="${basedir}/LICENSE.txt" tofile="${build.home}/classes/META-INF/LICENSE.txt"/>
        <jar jarfile="${dist.home}/${final.name}.jar" basedir="${build.home}/classes" manifest="${build.home}/conf/MANIFEST.MF"/>
    </target>
    <target name="install-jar" depends="jar" description="--> Installs jar file in ${lib.repo}">
        <copy todir="${lib.repo}" filtering="no">
            <fileset dir="${dist.home}">
                <include name="${final.name}.jar"/>
            </fileset>
        </copy>
    </target>
    <target name="pub-bin" depends="dist" description="Create binary distribution (compressed) ready for publication">
        <mkdir dir="${pub.home}"/>
        <!-- Binary properties -->
        <property name="final.path" value="${pub.home}/${final.name}"/>
        <property name="zip.path" value="${final.path}.zip"/>
        <property name="tar.path" value="${final.path}.tar"/>
        <property name="gz.path" value="${tar.path}.gz"/>
        <!-- Zip binary dist -->
        <zip destfile="${zip.path}">
           <zipfileset dir="${dist.home}" prefix="${final.name}/"/>
        </zip>
        <checksum algorithm="md5" file="${zip.path}" fileext=".md5"/>
        <checksum algorithm="sha" file="${zip.path}" fileext=".sha"/>
        <!-- Tar & gzip binary dist -->
        <tar tarfile="${tar.path}" basedir="${dist.home}"/>
        <gzip zipfile="${gz.path}" src="${tar.path}"/>
        <checksum algorithm="md5" file="${gz.path}" fileext=".md5"/>
        <checksum algorithm="sha" file="${gz.path}" fileext=".sha"/>
        <delete file="${tar.path}"/>
        <!-- Delete old signatures -->
        <delete file="${zip.path}.asc"/>
        <delete file="${gz.path}.asc"/>
    </target>
    <target name="pub-src" depends="dist" description="Create source distribution (compressed) ready for publication based on your LOCAL CVS sources">
        <mkdir dir="${pub.home}"/>
        <echo>Warning: The source files used to create this source distribution come from your local copy of the source files.</echo>
        <!-- Source properties -->
        <property name="final-src.path" value="${pub.home}/${final.name}-src"/>
        <property name="zip-src.path" value="${final-src.path}.zip"/>
        <property name="tar-src.path" value="${final-src.path}.tar"/>
        <property name="gz-src.path" value="${tar-src.path}.gz"/>
        <property name="excludes" value="${pub.home}/**, ${dist.home}/**, target/**, xdocs/**"/>
        <!-- Zip source dist -->
        <zip destfile="${zip-src.path}">
           <zipfileset dir="src" prefix="${final.name}/src/"/>
           <zipfileset dir="." includes="build.xml" prefix="${final.name}/"/>
           <zipfileset dir="." includes="checkstyle.xml" prefix="${final.name}/"/>
           <zipfileset dir="." includes="default.properties" prefix="${final.name}/"/>
           <zipfileset dir="." includes="LICENSE.TXT" prefix="${final.name}/"/>
           <zipfileset dir="." includes="maven.xml" prefix="${final.name}/"/>
           <zipfileset dir="." includes="project.properties" prefix="${final.name}/"/>
           <zipfileset dir="." includes="project.xml" prefix="${final.name}/"/>
           <zipfileset dir="." includes="RELEASE-NOTES.txt" prefix="${final.name}/"/>
        </zip>
        <checksum algorithm="md5" file="${zip-src.path}" fileext=".md5"/>
        <checksum algorithm="sha" file="${zip-src.path}" fileext=".sha"/>
        <!-- Tar & gzip source dist -->
        <tar tarfile="${tar-src.path}" basedir="." excludes="${excludes}" excludesfile=".cvsignore"/>
        <gzip zipfile="${gz-src.path}" src="${tar-src.path}"/>
        <checksum algorithm="md5" file="${gz-src.path}" fileext=".md5"/>
        <checksum algorithm="sha" file="${gz-src.path}" fileext=".sha"/>
        <delete file="${tar-src.path}"/>
        <!-- Delete old signatures -->
        <delete file="${zip-src.path}.asc"/>
        <delete file="${gz-src.path}.asc"/>
    </target>
    <target name="pub" depends="pub-bin, pub-src" description="Create binary and source distribution (compressed) ready for publication">
    </target>
    <!-- ========== Unit Test Targets ========================================= -->
    <target name="compile.tests" depends="compile" description="Compile unit test cases">
        <javac srcdir="${test.home}" destdir="${build.home}/tests" debug="${compile.debug}" deprecation="${compile.deprecation}" optimize="${compile.optimize}">
            <classpath refid="test.classpath"/>
        </javac>
        <copy todir="${build.home}/tests" filtering="on">
            <fileset dir="${test.home}" excludes="**/*.java"/>
        </copy>
    </target>
    <!-- Run all the JUnit Tests -->
    <target name="test" depends="compile.tests" description="Compiles and runs unit test cases">
        <record name="${build.home}/test-output.txt" append="no" action="start"/>
        <junit printsummary="yes" haltonfailure="yes">
            <classpath refid="test.classpath"/>
            <formatter type="plain"/>
            <batchtest fork="yes" todir="${build.home}/test-reports">
                <fileset dir="${test.home}">
                    <include name="**/*Test.java"/>
                    <exclude name="**/*AbstractTest.java"/>
                </fileset>
            </batchtest>
        </junit>
        <record name="${build.home}/test-output.txt" action="stop"/>
    </target>
</project>

```

## Build configuration pom.xml

```text
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
<project
    xmlns="http://maven.apache.org/POM/4.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <parent>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-parent</artifactId>
    <version>9</version>
  </parent>
  <modelVersion>4.0.0</modelVersion>
  <groupId>commons-codec</groupId>
  <artifactId>commons-codec</artifactId>
  <version>1.4-SNAPSHOT</version>
  <name>Commons Codec</name>

  <inceptionYear>2002</inceptionYear>
    <description>
     The codec package contains simple encoder and decoders for
     various formats such as Base64 and Hexadecimal.  In addition to these
     widely used encoders and decoders, the codec package also maintains a
     collection of phonetic encoding utilities.
    </description>

  <url>http://commons.apache.org/codec/</url>

  <issueManagement>
    <system>jira</system>
    <url>http://issues.apache.org/jira/browse/CODEC</url>
  </issueManagement>

  <scm>
    <connection>scm:svn:http://svn.apache.org/repos/asf/commons/proper/codec/trunk</connection>
    <developerConnection>scm:svn:https://svn.apache.org/repos/asf/commons/proper/codec/trunk</developerConnection>
    <url>http://svn.apache.org/viewvc/commons/proper/codec/trunk</url>
  </scm>

    <developers>
        <developer>
            <name>Henri Yandell</name>
            <id>bayard</id>
            <email>bayard@generationjava.com</email>
        </developer>
        <developer>
            <name>Tim OBrien</name>
            <id>tobrien</id>
            <email>tobrien@apache.org</email>
            <timezone>-6</timezone>
        </developer>
        <developer>
            <name>Scott Sanders</name>
            <id>sanders</id>
            <email>sanders@totalsync.com</email>
        </developer>
        <developer>
            <name>Rodney Waldhoff</name>
            <id>rwaldhoff</id>
            <email>rwaldhoff@apache.org</email>
        </developer>
        <developer>
            <name>Daniel Rall</name>
            <id>dlr</id>
            <email>dlr@finemaltcoding.com</email>
        </developer>
        <developer>
            <name>Jon S. Stevens</name>
            <id>jon</id>
            <email>jon@collab.net</email>
        </developer>
        <developer>
            <name>Gary D. Gregory</name>
            <id>ggregory</id>
            <email>ggregory@seagullsw.com</email>
            <organization>Seagull Software</organization>
            <timezone>-8</timezone>
        </developer>
        <developer>
            <name>David Graham</name>
            <id>dgraham</id>
            <email>dgraham@apache.org</email>
        </developer>
    </developers>
    <contributors>
        <contributor>
            <name>Christopher O'Brien</name>
            <email>siege@preoccupied.net</email>
            <roles>
                <role>hex</role>
                <role>md5</role>
                <role>architecture</role>
            </roles>
        </contributor>
        <contributor>
            <name>Martin Redington</name>
            <roles><role>representing xml-rpc</role></roles>
        </contributor>
        <contributor>
            <name>Jeffery Dever</name>
            <roles><role>representing http-client</role></roles>
        </contributor>
        <contributor>
            <name>Steve Zimmermann</name>
            <email>steve.zimmermann@heii.com</email>
            <roles><role>documentation</role></roles>
        </contributor>
        <contributor>
            <name>Benjamin Walstrum</name>
            <email>ben@walstrum.com</email>
        </contributor>
        <contributor>
            <name>Oleg Kalnichevski</name>
            <email>oleg@ural.ru</email>
            <roles><role>representing http-client</role></roles>
        </contributor>
        <contributor>
            <name>Dave Dribin</name>
            <email>apache@dave.dribin.org</email>
            <roles><role>digest util</role></roles>
        </contributor>
        <contributor>
            <name>Alex Karasulu</name>
            <email>aok123 at bellsouth.net</email>
            <roles><role>Submitted Binary class and test</role></roles>
        </contributor>
        <contributor>
            <name>Matthew Inger</name>
            <email>mattinger at yahoo.com</email>
            <roles><role>Submitted DIFFERENCE algorithm for Soundex and RefinedSoundex</role></roles>
        </contributor>
    </contributors>

  <!-- Codec should depend on very little -->
  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <properties>
    <maven.compile.source>1.4</maven.compile.source>
    <maven.compile.target>1.4</maven.compile.target>
    <commons.componentid>codec</commons.componentid>
    <commons.release.version>1.3</commons.release.version>
    <commons.binary.suffix></commons.binary.suffix>
    <commons.jira.id>CODEC</commons.jira.id>
    <commons.jira.pid>12310464</commons.jira.pid>
  </properties> 

  <build>
    <sourceDirectory>src/java</sourceDirectory>
    <testSourceDirectory>src/test</testSourceDirectory>
      <plugins>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-surefire-plugin</artifactId>
            <configuration>
              <includes>
                <include>**/*Test.java</include>
                <include>**/Test*.java</include>
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
              <descriptor>src/assembly/bin.xml</descriptor>
              <descriptor>src/assembly/src.xml</descriptor>
            </descriptors>
            <tarLongFileMode>gnu</tarLongFileMode>
          </configuration>
        </plugin>
      </plugins>
    </build>

    <reporting>
      <plugins>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-changes-plugin</artifactId>
          <version>2.0-beta-3</version>
          <configuration>
            <xmlPath>${basedir}/xdocs/changes.xml</xmlPath>
            <issueLinkTemplate>%URL%/%ISSUE%</issueLinkTemplate>
          </configuration>
          <reportSets>
            <reportSet>
              <reports>
                 <report>changes-report</report>
              </reports>
            </reportSet>
          </reportSets>
        </plugin>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-checkstyle-plugin</artifactId>
          <version>2.1</version>
          <configuration>
            <configLocation>checkstyle.xml</configLocation>
            <enableRulesSummary>false</enableRulesSummary>
          </configuration>
        </plugin>
        <plugin>
          <groupId>org.codehaus.mojo</groupId>
          <artifactId>cobertura-maven-plugin</artifactId>
          <version>2.2</version>
        </plugin>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-pmd-plugin</artifactId>
          <version>2.3</version>
        </plugin>
        <plugin>
          <groupId>org.codehaus.mojo</groupId>
          <artifactId>findbugs-maven-plugin</artifactId>
          <version>1.1.1</version>
        </plugin>
        <plugin>
          <groupId>org.codehaus.mojo</groupId>
          <artifactId>clirr-maven-plugin</artifactId>
          <version>2.1</version>
          <configuration>
            <comparisonVersion>1.3</comparisonVersion>
            <minSeverity>info</minSeverity>
          </configuration>
        </plugin>
      </plugins>
    </reporting>

</project>

```

## Build configuration project.properties

```text
#
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
#

# $Id$

maven.repo.remote=http://repo1.maven.org/maven

maven.changelog.factory=org.apache.maven.svnlib.SvnChangeLogFactory

maven.repo.central=www.apache.org
maven.repo.central.directory=/www/www.apache.org/dist/java-repository
maven.remote.group=apcvs

# For a description of maven.changes.issue.template
# See http://maven.apache.org/reference/plugins/changes/properties.html
maven.changes.issue.template=%URL%/show_bug.cgi?id=%ISSUE%

maven.checkstyle.properties=${basedir}/checkstyle.xml
maven.junit.fork=true
maven.linkcheck.enable=false

maven.xdoc.date=left
maven.xdoc.version=${pom.currentVersion}
maven.xdoc.developmentProcessUrl=http://commons.apache.org/charter.html
maven.xdoc.poweredby.image=maven-button-1.png

maven.javadoc.links=http://java.sun.com/j2se/1.4.2/docs/api/
maven.javadoc.overview=${basedir}/src/java/org/apache/commons/codec/overview.html
maven.javadoc.package=true

# Jar Manifest and Additional Attributes
maven.jar.manifest=${basedir}/src/conf/MANIFEST.MF
maven.jar.manifest.attributes.list=Implementation-Vendor-Id,X-Compile-Source-JDK,X-Compile-Target-JDK
maven.jar.manifest.attribute.Implementation-Vendor-Id=org.apache
maven.jar.manifest.attribute.X-Compile-Source-JDK=${maven.compile.source}
maven.jar.manifest.attribute.X-Compile-Target-JDK=${maven.compile.target}

maven.jdiff.old.tag = CODEC_1_2
#maven.jdiff.new.tag = CURRENT
#maven.jdiff.new.tag = CODEC_1_3_RC1
maven.jdiff.new.tag = CODEC_1_3

maven.announcement.file=${maven.build.dir}/RELEASE-NOTES.txt

# Specifies the source version for the Java compiler.
# Corresponds to the source attribute for the ant javac task. 
# Valid values are 1.3, 1.4, 1.5. 
maven.compile.source = 1.6
maven.compile.target = 1.6

# See https://svn.apache.org/repos/private/committers/donated-licenses/clover/README.txt
maven.jar.override=on
maven.jar.clover=1.3.2

```
