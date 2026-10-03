public final class SqaCodecSuite {
    static int executed=0,passed=0;
    static void check(String name,String owner,String method,String params,double vector){
        String actual=SqaProbe.observeWithPolicy(owner,"",method,params,new double[]{vector,0,0},"aom-beam-champ-codec-fixtures-v13-development");
        java.util.Map<String,Object> row=SqaProbe.CodecRecipe.lastEvidence;
        if(row==null || !name.equals(row.get("case")) || !SqaProbe.targetInvoked()
            || !actual.equals(SqaProbe.CodecRecipe.json(row.get("observation"))))throw new IllegalStateException("Shared dispatch differs");
        executed++; if(Boolean.TRUE.equals(row.get("target_check_passed")))passed++;
        System.out.println(SqaProbe.CodecRecipe.json(row));
    }
    public static void main(String[] args){
        try {
        check("isNextChar_match_first","org.apache.commons.codec.language.Metaphone","isNextChar","java.lang.StringBuffer,int,char",-0.875);
        check("isNextChar_match_middle","org.apache.commons.codec.language.Metaphone","isNextChar","java.lang.StringBuffer,int,char",-0.625);
        check("isNextChar_mismatch","org.apache.commons.codec.language.Metaphone","isNextChar","java.lang.StringBuffer,int,char",-0.375);
        check("isNextChar_negative","org.apache.commons.codec.language.Metaphone","isNextChar","java.lang.StringBuffer,int,char",-0.125);
        check("isNextChar_last","org.apache.commons.codec.language.Metaphone","isNextChar","java.lang.StringBuffer,int,char",0.125);
        check("isNextChar_at_length","org.apache.commons.codec.language.Metaphone","isNextChar","java.lang.StringBuffer,int,char",0.375);
        check("isNextChar_empty","org.apache.commons.codec.language.Metaphone","isNextChar","java.lang.StringBuffer,int,char",0.625);
        check("isNextChar_single","org.apache.commons.codec.language.Metaphone","isNextChar","java.lang.StringBuffer,int,char",0.875);
        check("isPreviousChar_match_first","org.apache.commons.codec.language.Metaphone","isPreviousChar","java.lang.StringBuffer,int,char",-0.875);
        check("isPreviousChar_match_last","org.apache.commons.codec.language.Metaphone","isPreviousChar","java.lang.StringBuffer,int,char",-0.625);
        check("isPreviousChar_mismatch","org.apache.commons.codec.language.Metaphone","isPreviousChar","java.lang.StringBuffer,int,char",-0.375);
        check("isPreviousChar_negative","org.apache.commons.codec.language.Metaphone","isPreviousChar","java.lang.StringBuffer,int,char",-0.125);
        check("isPreviousChar_first","org.apache.commons.codec.language.Metaphone","isPreviousChar","java.lang.StringBuffer,int,char",0.125);
        check("isPreviousChar_at_length","org.apache.commons.codec.language.Metaphone","isPreviousChar","java.lang.StringBuffer,int,char",0.375);
        check("isPreviousChar_empty","org.apache.commons.codec.language.Metaphone","isPreviousChar","java.lang.StringBuffer,int,char",0.625);
        check("isPreviousChar_single","org.apache.commons.codec.language.Metaphone","isPreviousChar","java.lang.StringBuffer,int,char",0.875);
        check("vowel_A","org.apache.commons.codec.language.Metaphone","isVowel","java.lang.StringBuffer,int",-0.8888888888888888);
        check("vowel_E","org.apache.commons.codec.language.Metaphone","isVowel","java.lang.StringBuffer,int",-0.6666666666666667);
        check("vowel_I","org.apache.commons.codec.language.Metaphone","isVowel","java.lang.StringBuffer,int",-0.4444444444444444);
        check("vowel_O","org.apache.commons.codec.language.Metaphone","isVowel","java.lang.StringBuffer,int",-0.2222222222222222);
        check("vowel_U","org.apache.commons.codec.language.Metaphone","isVowel","java.lang.StringBuffer,int",0.0);
        check("vowel_B","org.apache.commons.codec.language.Metaphone","isVowel","java.lang.StringBuffer,int",0.22222222222222232);
        check("vowel_empty","org.apache.commons.codec.language.Metaphone","isVowel","java.lang.StringBuffer,int",0.4444444444444444);
        check("vowel_negative","org.apache.commons.codec.language.Metaphone","isVowel","java.lang.StringBuffer,int",0.6666666666666667);
        check("vowel_at_length","org.apache.commons.codec.language.Metaphone","isVowel","java.lang.StringBuffer,int",0.8888888888888888);
        check("region_prefix","org.apache.commons.codec.language.Metaphone","regionMatch","java.lang.StringBuffer,int,java.lang.String",-0.8888888888888888);
        check("region_middle","org.apache.commons.codec.language.Metaphone","regionMatch","java.lang.StringBuffer,int,java.lang.String",-0.6666666666666667);
        check("region_last","org.apache.commons.codec.language.Metaphone","regionMatch","java.lang.StringBuffer,int,java.lang.String",-0.4444444444444444);
        check("region_mismatch","org.apache.commons.codec.language.Metaphone","regionMatch","java.lang.StringBuffer,int,java.lang.String",-0.2222222222222222);
        check("region_too_long","org.apache.commons.codec.language.Metaphone","regionMatch","java.lang.StringBuffer,int,java.lang.String",0.0);
        check("region_negative","org.apache.commons.codec.language.Metaphone","regionMatch","java.lang.StringBuffer,int,java.lang.String",0.22222222222222232);
        check("region_empty_end","org.apache.commons.codec.language.Metaphone","regionMatch","java.lang.StringBuffer,int,java.lang.String",0.4444444444444444);
        check("region_empty_buffer","org.apache.commons.codec.language.Metaphone","regionMatch","java.lang.StringBuffer,int,java.lang.String",0.6666666666666667);
        check("region_empty_beyond_end","org.apache.commons.codec.language.Metaphone","regionMatch","java.lang.StringBuffer,int,java.lang.String",0.8888888888888888);
        check("difference_equal","org.apache.commons.codec.language.SoundexUtils","difference","org.apache.commons.codec.StringEncoder,java.lang.String,java.lang.String",-0.8888888888888888);
        check("difference_case_fold","org.apache.commons.codec.language.SoundexUtils","difference","org.apache.commons.codec.StringEncoder,java.lang.String,java.lang.String",-0.6666666666666667);
        check("difference_different","org.apache.commons.codec.language.SoundexUtils","difference","org.apache.commons.codec.StringEncoder,java.lang.String,java.lang.String",-0.4444444444444444);
        check("difference_null_left","org.apache.commons.codec.language.SoundexUtils","difference","org.apache.commons.codec.StringEncoder,java.lang.String,java.lang.String",-0.2222222222222222);
        check("difference_empty_both","org.apache.commons.codec.language.SoundexUtils","difference","org.apache.commons.codec.StringEncoder,java.lang.String,java.lang.String",0.0);
        check("difference_null_both","org.apache.commons.codec.language.SoundexUtils","difference","org.apache.commons.codec.StringEncoder,java.lang.String,java.lang.String",0.22222222222222232);
        check("difference_two_equal","org.apache.commons.codec.language.SoundexUtils","difference","org.apache.commons.codec.StringEncoder,java.lang.String,java.lang.String",0.4444444444444444);
        check("difference_shorter_right","org.apache.commons.codec.language.SoundexUtils","difference","org.apache.commons.codec.StringEncoder,java.lang.String,java.lang.String",0.6666666666666667);
        check("difference_two_mismatch","org.apache.commons.codec.language.SoundexUtils","difference","org.apache.commons.codec.StringEncoder,java.lang.String,java.lang.String",0.8888888888888888);
        System.out.println("{\"summary\":true,\"executed\":"+executed+",\"target_checks\":"+executed+
            ",\"passed\":"+passed+",\"failed\":"+(executed-passed)+",\"skipped\":0,\"fixture_errors\":0}");
        if(passed!=43)System.exit(1);
        }catch(Throwable failure){failure.printStackTrace();System.exit(2);}
    }
}
