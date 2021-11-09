package com.satis.viewmodel.processor

import com.squareup.javapoet.*
import java.util.*
import javax.lang.model.element.Modifier

class ViewModelCreatorProxy {
    fun createTypeSpec(): TypeSpec.Builder {
        val owner = ClassName.get("androidx.lifecycle", "LifecycleOwner")
        val baseObserve = ClassName.get("com.satis.viewmodel.core", "BaseObserver")
        val baseLiveData = ClassName.get("com.satis.viewmodel.core", "BaseLiveData")
        val builder = TypeSpec.classBuilder("SatisViewModel")
        builder.addModifiers(Modifier.PUBLIC)
        val mtViewModel = ClassName.get(Processor.Companion.APT_PACKAGE, "SatisViewModel")
        //静态private 成员变量
//        addInstanceField(builder, mtViewModel);

        //添加map 管理 viewmodel 对应 的LiveData
//        addMapField(owner, baseLiveData, builder);
        //单例方法
//        addInstance(builder, mtViewModel);


        //订阅方法
        addRegister(builder, owner)

        //解除订阅
//        addUnregister(builder,owner);
        return builder
    }

    private fun addRegister(builder: TypeSpec.Builder, owner: ClassName) {
        val modelEnum = ClassName.get(Processor.Companion.APT_PACKAGE, "SatisViewModelEnum")
        val observeMethod = MethodSpec.methodBuilder("register")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .addParameter(owner, "host")
            .addStatement(
                "\$T modelEnum = \$T.valueOf(host.getClass().getSimpleName().toUpperCase())",
                modelEnum,
                modelEnum
            )
            .beginControlFlow("if(modelEnum!=null)")
            .beginControlFlow("try")
            .addStatement("modelEnum.getObserveClass().getConstructor().newInstance().observe(host)")
            .endControlFlow("catch(\$T e){}", Exception::class.java)
            .endControlFlow()
            .build()
        builder.addMethod(observeMethod)
    }

    private fun addUnregister(builder: TypeSpec.Builder, owner: ClassName) {
        val modelEnum = ClassName.get(Processor.Companion.APT_PACKAGE, "SatisViewModelEnum")
        val observeMethod = MethodSpec.methodBuilder("unRegister")
            .addModifiers(Modifier.PUBLIC)
            .addParameter(owner, "host")
            .addStatement(
                "\$T modelEnum = \$T.valueOf(host.getClass().getSimpleName().toUpperCase())",
                modelEnum,
                modelEnum
            )
            .beginControlFlow("if(modelEnum!=null)")
            .beginControlFlow("try")
            .addStatement("modelEnum.getObserveClass().getConstructor().newInstance().observe(host)")
            .endControlFlow("catch(\$T e){}", Exception::class.java)
            .endControlFlow()
            .build()
        builder.addMethod(observeMethod)
    }

    private fun addInstance(builder: TypeSpec.Builder, mtViewModel: ClassName) {
        val instanceMethod = MethodSpec.methodBuilder("getDefault")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .beginControlFlow("if ( defaultInstance == null)")
            .beginControlFlow("synchronized (\$T.class)", mtViewModel)
            .beginControlFlow("if (defaultInstance == null)")
            .addStatement("defaultInstance = new \$T()", mtViewModel)
            .endControlFlow()
            .endControlFlow()
            .endControlFlow()
            .addStatement("return defaultInstance")
            .returns(mtViewModel)
            .build()
        builder.addMethod(instanceMethod)
    }

    private fun addInstanceField(builder: TypeSpec.Builder, mtViewModel: ClassName) {
        val instanceField = FieldSpec.builder(mtViewModel, "defaultInstance")
            .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
            .build()
        builder.addField(instanceField)
    }

    private fun addMapField(owner: ClassName, baseLiveData: ClassName, builder: TypeSpec.Builder) {
        val hashMapTypeName = ClassName.get(HashMap::class.java)
        val stringTypeName = ClassName.get(String::class.java)
        val listParameterizedTypeName =
            ParameterizedTypeName.get(hashMapTypeName, stringTypeName, baseLiveData)
        val parameterizedTypeName =
            ParameterizedTypeName.get(hashMapTypeName, owner, listParameterizedTypeName)
        val map = FieldSpec.builder(parameterizedTypeName, "mObserveMap")
            .addModifiers(Modifier.PRIVATE)
            .initializer("new \$T()", HashMap::class.java)
            .build()
        builder.addField(map)
    }
}