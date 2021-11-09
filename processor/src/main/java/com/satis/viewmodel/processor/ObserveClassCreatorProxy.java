package com.satis.viewmodel.processor;

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
import javax.tools.Diagnostic;

import com.satis.viewmodel.annotation.Observe;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

/**
 * Created by ssb
 * date 2020/3/11
 * 创建Java文件代理类
 */
public class ObserveClassCreatorProxy {
    private String mObserveClassName;
    private TypeElement mTypeElement;


    private Map<Observe, ExecutableElement> mObserveElementMap = new HashMap<>();
    private final String mPackageName;

    public ObserveClassCreatorProxy(Elements elementUtils, TypeElement classElement) {
        this.mTypeElement = classElement;
        PackageElement packageElement = elementUtils.getPackageOf(mTypeElement);
        mPackageName = packageElement.getQualifiedName().toString();
        String className = mTypeElement.getSimpleName().toString();
        this.mObserveClassName = className + "$Observe";
    }
    //存储方法信息
    public void putElement(Observe observe,ExecutableElement element) {
        mObserveElementMap.put(observe, element);
    }

    public TypeElement getTypeElement() {
        return mTypeElement;
    }

    /**
     * 创建Java代码
     * javapoet
     *
     * @return
     */
    public TypeSpec generateJavaCode(Messager messager) {
        ClassName className = ClassName.get("com.satis.viewmodel.core", "Observer");
        TypeSpec bindingClass = TypeSpec.classBuilder(mObserveClassName)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(className)
                .addMethod(generateMethods(messager))
                .build();
        return bindingClass;

    }

    /**
     * 加入Method
     * javapoet
     */
    private MethodSpec generateMethods(Messager messager) {
        ClassName host = ClassName.bestGuess(mTypeElement.getQualifiedName().toString());
        ClassName owner = ClassName.get("androidx.lifecycle","LifecycleOwner");
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("observe")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(void.class)
                .addParameter(owner, "host");
        ClassName baseObserver = ClassName.get("com.satis.viewmodel.core", "BaseObserver");
        ClassName  observer = ClassName.get("androidx.lifecycle", "Observer");
        //计数形式规避方法重载
        int count = 0;
        for (Map.Entry<Observe, ExecutableElement> ObserveVariableElementEntry : mObserveElementMap.entrySet()) {
            Observe observe = ObserveVariableElementEntry.getKey();
            ExecutableElement value = ObserveVariableElementEntry.getValue();
            String method = value.getSimpleName().toString();
            List<? extends VariableElement> parameters = value.getParameters();
            String parameterName = parameters.get(0).getSimpleName().toString();
            String parameterType = parameters.get(0).asType().toString();
            ClassName className = ClassName.bestGuess(parameterType);
//            messager.printMessage(Diagnostic.Kind.NOTE,"process observe.tag():"+observe.tag());
//            messager.printMessage(Diagnostic.Kind.NOTE,"process observe.isSticky():"+observe.isSticky());
//            messager.printMessage(Diagnostic.Kind.NOTE,"process 方法参数名:"+parameterName);
//            messager.printMessage(Diagnostic.Kind.NOTE,"process 方法参数类型:"+parameterType);
            String observeName = method+count;
            methodBuilder.beginControlFlow("$T<"+parameterType+"> "+observeName+" = new $T<"+parameterType+">()",observer,observer)
                    .addCode("@$T\n",Override.class)
                    .beginControlFlow("public void onChanged("+parameterType+" t)")
                    .addStatement( "(($T)host)."+method+"(t)",host)
                    .endControlFlow()
                    .endControlFlow()
                    .addCode(";\n");
            methodBuilder.addStatement("(($T)host).mViewModel.addObserver(host,\"" + observe.tag() + "\" ,"+"new $T("+observeName+"),"+observe.sticky()+")",host,baseObserver);
            count++;
        }




        return methodBuilder.build();
    }

    public String getObserveClassName() {
        return mObserveClassName;
    }

    public String getPackageName() {
        return mPackageName;
    }
}
