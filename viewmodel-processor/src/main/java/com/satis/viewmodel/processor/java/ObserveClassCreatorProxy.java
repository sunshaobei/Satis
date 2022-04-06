package com.satis.viewmodel.processor.java;

import com.satis.viewmodel.annotation.Observe;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.Messager;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

/**
 * Created by ssb
 * date 2020/3/11
 * 创建Java文件代理类
 */
public class ObserveClassCreatorProxy{
    Elements elementUtils;
    TypeElement typeElement;
    public ObserveClassCreatorProxy( Elements elementUtils,TypeElement typeElement){
        this.elementUtils = elementUtils;
        this.typeElement = typeElement;
        PackageElement packageElement = elementUtils.getPackageOf(typeElement);
        packageName = packageElement.getQualifiedName().toString();
        String className = typeElement.getSimpleName().toString();
        observeClassName = className+"$Observe";
    }
    private String observeClassName;
    private HashMap<Observe, ExecutableElement> mObserveElementMap = new HashMap();
    private String packageName;

    //存储方法信息
   public void putElement(Observe observe, ExecutableElement element) {
        mObserveElementMap.put(observe,element);
    }

    /**
     * 创建Java代码
     * javapoet
     *
     * @return
     */
    public TypeSpec generateJavaCode(Messager messager) {
        ClassName className = ClassName.get("com.satis.viewmodel.core", "Observer");
        return TypeSpec.classBuilder(observeClassName)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(className)
                .addMethod(generateMethods(messager))
                .build();
    }

    /**
     * 加入Method
     * javapoet
     */
    private MethodSpec generateMethods(Messager messager) {
        ClassName host = ClassName.bestGuess(typeElement.getQualifiedName().toString());
        ClassName owner = ClassName.get("androidx.lifecycle", "LifecycleOwner");
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("observe")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(Void.TYPE)
                .addParameter(owner, "host");
        ClassName baseObserver = ClassName.get("com.satis.viewmodel.core", "BaseObserver");
        ClassName observer = ClassName.get("androidx.lifecycle", "Observer");
        //计数形式规避方法重载
        int count = 0;
        for (Map.Entry<Observe, ExecutableElement> ObserveVariableElementEntry : mObserveElementMap.entrySet()) {
            Observe observe = ObserveVariableElementEntry.getKey();
            ExecutableElement value = ObserveVariableElementEntry.getValue();
            String method = value.getSimpleName().toString();
            List<? extends VariableElement> parameters = value.getParameters();
            String parameterName = parameters.get(0).getSimpleName().toString();
            String parameterType = parameters.get(0).asType().toString();
//            messager.printMessage(Diagnostic.Kind.NOTE,"process observe.tag():"+observe.tag());
//            messager.printMessage(Diagnostic.Kind.NOTE,"process observe.isSticky():"+observe.isSticky());
//            messager.printMessage(Diagnostic.Kind.NOTE,"process 方法参数名:"+parameterName);
//            messager.printMessage(Diagnostic.Kind.NOTE,"process 方法参数类型:"+parameterType);
            methodBuilder.beginControlFlow("$T<"+parameterType+"> "+method+" = new $T<"+parameterType+">()",observer,observer)
                    .addCode("@$T\n",Override.class)
                    .beginControlFlow("public void onChanged("+parameterType+" t)")
                    .addStatement( "(($T)host)."+method+"(t)",host)
                    .endControlFlow()
                    .endControlFlow()
                    .addCode(";\n");
            methodBuilder.addStatement("(($T)host).mViewModel.addObserver(host,\"" + method + "\" ,"+"new $T("+method+"),"+observe.sticky()+")",host,baseObserver);
            count++;
        }
        return methodBuilder.build();
    }
}