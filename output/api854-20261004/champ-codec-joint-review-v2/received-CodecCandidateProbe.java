package sqa.development;

import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Metaphone;

/** Bounded reflective helpers on real production receivers; declared value/state checks. */
public final class CodecCandidateProbe {
    public static String activeCase = "";
    static String q(String s) { return "\""+s.replace("\\","\\\\").replace("\"","\\\"")+"\""; }
    static String json(Object o) {
        if(o==null)return "null";
        if(o instanceof String)return q((String)o);
        if(o instanceof Map) {
            StringBuilder b=new StringBuilder("{");
            for(Object entry:((Map)o).entrySet()) {
                Map.Entry e=(Map.Entry)entry;if(b.length()>1)b.append(',');
                b.append(q((String)e.getKey())).append(':').append(json(e.getValue()));
            }return b.append('}').toString();
        }
        if(o instanceof List) {
            StringBuilder b=new StringBuilder("[");
            for(Object item:(List)o){if(b.length()>1)b.append(',');b.append(json(item));}
            return b.append(']').toString();
        }return String.valueOf(o);
    }
    static Map<String,Object> map(Object... kv) {
        Map<String,Object> r=new LinkedHashMap<String,Object>();
        for(int i=0;i<kv.length;i+=2)r.put((String)kv[i],kv[i+1]);return r;
    }
    static String string(String encoded) {
        return encoded.equals("-")?null:new String(Base64.getDecoder().decode(encoded),StandardCharsets.UTF_8);
    }
    static String type(Class<?> c) {
        if(c==boolean.class)return "Z";if(c==int.class)return "I";if(c==char.class)return "C";
        return "L"+c.getName().replace('.','/')+";";
    }
    static String descriptor(Method m) {
        StringBuilder b=new StringBuilder("(");for(Class<?> c:m.getParameterTypes())b.append(type(c));
        return b.append(')').append(type(m.getReturnType())).toString();
    }
    static void check(boolean ok,String reason){if(!ok)throw new AssertionError(reason);}
    static Map<String,Object> run(String[] c) throws Exception {
        String name=c[0],method=c[1],text=string(c[2]),needle=string(c[5]),s1=string(c[6]),s2=string(c[7]);
        int index=Integer.parseInt(c[3]);char ch=(char)Integer.parseInt(c[4]);
        Object expectedValue=c[8].equals("null")?null:(c[8].equals("true")?Boolean.TRUE:
              (c[8].equals("false")?Boolean.FALSE:Integer.valueOf(c[8])));
        String expectedException=c[9].equals("-")?null:c[9];
        StringBuffer buffer=text==null?null:new StringBuffer(text);
        Object receiver;Metaphone encoder=null;Class<?> owner;Class<?>[] types;Object[] args;
        Object encoded=null;
        if(method.equals("difference")) {
            owner=Class.forName("org.apache.commons.codec.language.SoundexUtils");
            Constructor<?> ctor=owner.getDeclaredConstructor();ctor.setAccessible(true);receiver=ctor.newInstance();
            encoder=new Metaphone();check(encoder.getMaxCodeLen()==4,"Exact encoder initial state");
            // Independent literal encoded references are fixture preconditions, never the result oracle.
            List<String> pre=Arrays.asList(encoder.encode(s1),encoder.encode(s2));
            List<String> refs=Arrays.asList(string(c[10]),string(c[11]));
            check(pre.equals(refs),"Real production encoder differs from declared literal references");encoded=pre;
            types=new Class<?>[]{StringEncoder.class,String.class,String.class};args=new Object[]{encoder,s1,s2};
        } else {
            owner=Metaphone.class;receiver=new Metaphone();
            check(buffer!=null,"Bounded helpers require real non-null StringBuffer");
            if(method.equals("isVowel")){types=new Class<?>[]{StringBuffer.class,int.class};args=new Object[]{buffer,index};}
            else if(method.equals("regionMatch")){types=new Class<?>[]{StringBuffer.class,int.class,String.class};args=new Object[]{buffer,index,needle};}
            else {types=new Class<?>[]{StringBuffer.class,int.class,char.class};args=new Object[]{buffer,index,ch};}
        }
        check(receiver.getClass()==owner,"Exact production receiver identity");
        Method target=owner.getDeclaredMethod(method,types);target.setAccessible(true);
        check(target.getDeclaringClass()==owner,"Exact declaring class");
        check(Modifier.isStatic(target.getModifiers())==method.equals("difference"),"Static/instance identity");
        String before=buffer==null?null:buffer.toString();int capacity=buffer==null?0:buffer.capacity();
        Object value=null;Throwable thrown=null;
        activeCase=name;
        try{value=target.invoke(receiver,args);}catch(InvocationTargetException error){thrown=error.getCause();}
        finally{activeCase="";}
        boolean unchanged=buffer==null||(buffer.toString().equals(before)&&buffer.length()==before.length()&&buffer.capacity()==capacity);
        Object receiverMax=receiver instanceof Metaphone?((Metaphone)receiver).getMaxCodeLen():null;
        Object encoderMax=encoder==null?null:encoder.getMaxCodeLen();
        Map<String,Object> actual=map("value",value,"exception_class",thrown==null?null:thrown.getClass().getName(),
            "buffer_contents",buffer==null?null:buffer.toString(),"buffer_length",buffer==null?null:buffer.length(),
            "buffer_unchanged",unchanged,"receiver_max_code_len",receiverMax,"encoder_max_code_len",encoderMax,
            "encoder_encoded_inputs",encoded);
        Map<String,Object> expected=map("value",expectedValue,"exception_class",expectedException,
            "buffer_contents",text,"buffer_length",text==null?null:text.length(),"buffer_unchanged",true,
            "receiver_max_code_len",method.equals("difference")?null:4,"encoder_max_code_len",method.equals("difference")?4:null,
            "encoder_encoded_inputs",method.equals("difference")?Arrays.asList(string(c[10]),string(c[11])):null);
        boolean passed=json(actual).equals(json(expected));
        return map("case",name,"method",method,"receiver_class",receiver.getClass().getName(),
            "declaring_class",target.getDeclaringClass().getName(),"descriptor",descriptor(target),
            "setup_succeeded",true,"target_invoked",true,"target_check_passed",passed,
            "failure_class",passed?null:"java.lang.AssertionError","failure_reason",passed?null:"Declared value/state oracle differs",
            "observation",actual,"expected_observation",expected,
            "pre_state",map("buffer_contents",before,"buffer_length",before==null?null:before.length(),"buffer_capacity",buffer==null?null:capacity),
            "post_state",map("buffer_contents",buffer==null?null:buffer.toString(),"buffer_length",buffer==null?null:buffer.length(),"buffer_capacity",buffer==null?null:buffer.capacity()));
    }
    public static void main(String[] args) throws Exception {
        int executed=0,passed=0,errors=0;
        try(BufferedReader in=new BufferedReader(new InputStreamReader(new FileInputStream(args[0]),StandardCharsets.UTF_8))) {
            String line;while((line=in.readLine())!=null) {
                String[] c=line.split("\t",-1);Map<String,Object> row;
                try{row=run(c);executed++;if(Boolean.TRUE.equals(row.get("target_check_passed")))passed++;}
                catch(Throwable e){errors++;row=map("case",c[0],"setup_succeeded",false,"target_invoked",false,
                    "target_check_passed",false,"failure_class",e.getClass().getName(),"failure_reason",e.getMessage());}
                System.out.println(json(row));
            }
        }
        System.out.println(json(map("summary",true,"executed",executed,"target_checks",executed,
            "passed",passed,"failed",executed-passed,"skipped",0,"fixture_errors",errors)));
        if(passed!=43||errors!=0)System.exit(1);
    }
}
