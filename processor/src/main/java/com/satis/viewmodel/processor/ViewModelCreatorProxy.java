package com.satis.viewmodel.processor;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.HashMap;

public class ViewModelCreatorProxy {

    public TypeSpec.Builder createTypeSpec(){
        ClassName owner = ClassName.get("androidx.lifecycle","LifecycleOwner");
        ClassName baseObserve = ClassName.get("com.satis.viewmodel.core","BaseObserver");
        ClassName baseLiveData = ClassName.get("com.satis.viewmodel.core","BaseLiveData");
        TypeSpec.Builder builder = TypeSpec.classBuilder("SatisViewModel");
        builder.addModifiers(Modifier.PUBLIC);
        ClassName mtViewModel = ClassName.get(Processor.APT_PACKAGE, "SatisViewModel");
        //静态private 成员变量
//        addInstanceField(builder, mtViewModel);

        //添加map 管理 viewmodel 对应 的LiveData
//        addMapField(owner, baseLiveData, builder);
        //单例方法
//        addInstance(builder, mtViewModel);


        //订阅方法
        addRegister(builder,owner);

        //解除订阅
//        addUnregister(builder,owner);

        return builder;
    }
    private void addRegister(TypeSpec.Builder builder,ClassName owner){
        ClassName modelEnum = ClassName.get(Processor.APT_PACKAGE,"SatisViewModelEnum");
        MethodSpec observeMethod = MethodSpec.methodBuilder("register")
                .addModifiers(Modifier.PUBLIC,Modifier.STATIC)
                .addParameter(owner, "host")
                .addStatement("$T modelEnum = $T.valueOf(host.getClass().getSimpleName().toUpperCase())",modelEnum,modelEnum)
                .beginControlFlow("if(modelEnum!=null)")
                .beginControlFlow("try" )
                .addStatement("modelEnum.getObserveClass().getConstructor().newInstance().observe(host)")
                .endControlFlow("catch($T e){}" , Exception.class)
                .endControlFlow()
                .build();
        builder.addMethod(observeMethod);
    }

    private void addUnregister(TypeSpec.Builder builder,ClassName owner){
        ClassName modelEnum = ClassName.get(Processor.APT_PACKAGE,"SatisViewModelEnum");
        MethodSpec observeMethod = MethodSpec.methodBuilder("unRegister")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(owner, "host")
                .addStatement("$T modelEnum = $T.valueOf(host.getClass().getSimpleName().toUpperCase())",modelEnum,modelEnum)
                .beginControlFlow("if(modelEnum!=null)")
                .beginControlFlow("try" )
                .addStatement("modelEnum.getObserveClass().getConstructor().newInstance().observe(host)")
                .endControlFlow("catch($T e){}" , Exception.class)
                .endControlFlow()
                .build();
        builder.addMethod(observeMethod);
    }

    private void addInstance(TypeSpec.Builder builder, ClassName mtViewModel) {
        MethodSpec instanceMethod = MethodSpec.methodBuilder("getDefault")
                .addModifiers(Modifier.PUBLIC,Modifier.STATIC)
                .beginControlFlow("if ( defaultInstance == null)")
                .beginControlFlow("synchronized ($T.class)", mtViewModel)
                .beginControlFlow("if (defaultInstance == null)")
                .addStatement("defaultInstance = new $T()", mtViewModel)
                .endControlFlow()
                .endControlFlow()
                .endControlFlow()
                .addStatement("return defaultInstance")
                .returns(mtViewModel)
                .build();
        builder.addMethod(instanceMethod);
    }

    private void addInstanceField(TypeSpec.Builder builder, ClassName mtViewModel) {
        FieldSpec instanceField = FieldSpec.builder(mtViewModel, "defaultInstance")
                .addModifiers(Modifier.PRIVATE,Modifier.STATIC)
                .build();
        builder.addField(instanceField);
    }

    private void addMapField(ClassName owner, ClassName baseLiveData, TypeSpec.Builder builder) {
        ClassName hashMapTypeName = ClassName.get(HashMap.class);
        ClassName stringTypeName = ClassName.get(String.class);
        ParameterizedTypeName listParameterizedTypeName = ParameterizedTypeName.get(hashMapTypeName,stringTypeName, baseLiveData);
        ParameterizedTypeName parameterizedTypeName =  ParameterizedTypeName.get(hashMapTypeName, owner,listParameterizedTypeName);
        FieldSpec map = FieldSpec.builder(parameterizedTypeName,"mObserveMap")
                .addModifiers(Modifier.PRIVATE)
                .initializer("new $T()",HashMap.class)
                .build();
        builder.addField(map);
    }

}
