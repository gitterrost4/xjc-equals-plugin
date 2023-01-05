package io.github.gitterrost4.xjcequalsplugin;

import com.sun.codemodel.*;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.Outline;
import org.xml.sax.ErrorHandler;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.ResourceBundle;

public class EqualsPlugin extends Plugin {
    private final ResourceBundle resourceBundle = ResourceBundle.getBundle(EqualsPlugin.class.getCanonicalName());

    @Override
    public String getOptionName() {
        return "generateEquals";
    }

    @Override
    public String getUsage() {
        final String n = System.getProperty("line.separator", "\n");
        return "-" + getOptionName() + " :  " + MessageFormat.format(resourceBundle.getString("usage"), "") + n;
    }

    @Override
    public boolean run(Outline outline, Options options, ErrorHandler errorHandler) {
        for (ClassOutline clazz : outline.getClasses()){
            JDefinedClass implClass = clazz.implClass;
            JCodeModel codeModel = implClass.owner();

            //generate hashcode
            JMethod hashCodeMethod = implClass.method(JMod.PUBLIC, codeModel.INT, "hashCode");
            JBlock hashCodeBody = hashCodeMethod.body();
            JInvocation invocation = codeModel.ref(Objects.class).staticInvoke("hash");
            //if class is not derived from object, check super.equals
            if(!implClass._extends().equals(codeModel.ref(java.lang.Object.class))) {
                invocation.arg(JExpr._super().invoke("hashCode"));
            }
            for(JFieldVar field: implClass.fields().values()){
                invocation.arg(JExpr.ref(field.name()));
            }
            hashCodeBody._return(invocation);

            //generate equals
            JMethod equalsMethod = implClass.method(JMod.PUBLIC, codeModel.BOOLEAN, "equals");
            equalsMethod.param(codeModel.ref(Object.class), "o");
            JBlock equalsBody = equalsMethod.body();
            equalsBody._if(JExpr._this().eq(JExpr.ref("o")))._then()._return(JExpr.lit(true));
            equalsBody._if(JExpr.ref("o").eq(JExpr._null()).cor(JExpr.invoke("getClass").ne(JExpr.ref("o").invoke("getClass"))))._then()._return(JExpr.lit(false));
            //if class is not derived from object, check super.equals
            if(!implClass._extends().equals(codeModel.ref(java.lang.Object.class))) {
                equalsBody._if(JExpr._super().invoke("equals").arg(JExpr.ref("o")).not())._then()._return(JExpr.lit(false));
            }
            equalsBody.decl(implClass, "that", JExpr.cast(implClass, JExpr.ref("o")));

            JExpression expr = JExpr.lit(true);
            for(JFieldVar field: implClass.fields().values()){
                JInvocation equalsInvocation = codeModel.ref(Objects.class).staticInvoke("equals");
                equalsInvocation.arg(JExpr.ref(field.name()));
                equalsInvocation.arg(JExpr.ref("that").ref(field));
                expr = equalsInvocation.cand(expr);
            }
            equalsBody._return(expr);


        }
        return true;
    }
}
