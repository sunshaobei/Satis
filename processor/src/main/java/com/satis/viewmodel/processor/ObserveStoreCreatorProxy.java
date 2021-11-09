package com.satis.viewmodel.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

public class ObserveStoreCreatorProxy {
    private TypeSpec.Builder builder;
    private MethodSpec.Builder storeMethod;

    public ObserveStoreCreatorProxy() {
        initDefaultTypeBuilder();
    }

    private void initDefaultTypeBuilder(){
        builder = TypeSpec.classBuilder("ObserveStore");
        ClassName observeStore = ClassName.get("com.satis.viewmodel.core", "ObserveStore");
        builder.addSuperinterface(observeStore);
        builder.addModifiers(Modifier.PUBLIC);
        overrideMethod();
    }

    private void overrideMethod(){
        ClassName linkedHashMapTypeName = ClassName.get(LinkedHashMap.class);
        ClassName classTypeName = ClassName.get(Class.class);
        TypeVariableName lifeOwner = TypeVariableName.get("? extends androidx.lifecycle.LifecycleOwner");
        ParameterizedTypeName classParameterizedType = ParameterizedTypeName.get(classTypeName,lifeOwner.box());

        ClassName constructorTypeName = ClassName.get(Constructor.class);
        TypeVariableName observerTypeName = TypeVariableName.get("? extends com.satis.viewmodel.core.Observer");
        ParameterizedTypeName constructorParameterizedType =  ParameterizedTypeName.get(constructorTypeName,observerTypeName.box());
        ParameterizedTypeName linkedHashMapType =  ParameterizedTypeName.get(linkedHashMapTypeName, classParameterizedType,constructorParameterizedType);
        storeMethod = MethodSpec.methodBuilder("getStore")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(linkedHashMapType)
                .addStatement("$T map = new $T()",linkedHashMapType,linkedHashMapType)
                .beginControlFlow("try");
    }

    public void put(TypeElement typeElement){
        ClassName host = ClassName.bestGuess(typeElement.getQualifiedName().toString());
        ClassName className = ClassName.get(Processor.APT_PACKAGE, typeElement.getSimpleName().toString() + "$Observe");
        storeMethod.addStatement("map.put($T.class,$T.class.getConstructor())",host,className);
    }

    public void endMethod(){
        storeMethod.endControlFlow()
                .beginControlFlow("catch($T e)",Exception.class)
                .endControlFlow()
                .addStatement("return map");
    }

    public TypeSpec build(){
        builder.addMethod(storeMethod.build());
        return builder.build();
    }
}
