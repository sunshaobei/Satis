package com.satis.viewmodel.processor;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

public class ViewModelEnumCreatorPRoxy {
    private TypeSpec.Builder builder;

    private void initDefaultTypeBuilder(){
        builder = TypeSpec.enumBuilder("SatisViewModelEnum");
        builder.addModifiers(Modifier.PUBLIC);
    }

    private TypeSpec.Builder getBuilder(){
        if (builder==null){
            initDefaultTypeBuilder();
        }
        return builder;
    }

    public TypeSpec build(){
        return getBuilder().build();
    }

    public void addConstructorMethod(){
        TypeVariableName t = TypeVariableName.get("? extends com.satis.viewmodel.core.Observer");
        ClassName className = ClassName.get(Class.class);
        ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(className, t.box());
        getBuilder().addField(parameterizedTypeName, "mClass", Modifier.PRIVATE);
        MethodSpec set = MethodSpec.constructorBuilder()
                .addParameter(parameterizedTypeName, "mClass")
                .addStatement("this.$N = $N", "mClass", "mClass")
                .build();
        MethodSpec get = MethodSpec.methodBuilder("getObserveClass")
                .addModifiers(Modifier.PUBLIC)
                .returns(parameterizedTypeName)
                .addStatement("return mClass")
                .build();
        getBuilder().addMethod(set);
        getBuilder().addMethod(get);
    }

    public void addEnum(TypeElement typeElement){
        String simpleName = typeElement.getSimpleName().toString();
        ClassName className = ClassName.get(Processor.APT_PACKAGE, simpleName + "$Observe");
        getBuilder().addEnumConstant(simpleName.toUpperCase(), TypeSpec.anonymousClassBuilder("$T.class",className).build());

    }

}
