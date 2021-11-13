package com.satis.viewmodel.processor

import com.squareup.javapoet.TypeSpec
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeVariableName
import com.squareup.javapoet.ParameterizedTypeName
import java.lang.Exception
import java.lang.reflect.Constructor
import java.util.LinkedHashMap
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

class ObserveStoreCreatorProxyKt {
    private lateinit var builder: TypeSpec.Builder
    private var storeMethod: MethodSpec.Builder? = null
    private fun initDefaultTypeBuilder() {
        builder = TypeSpec.classBuilder("ObserveStore")
        val observeStore = ClassName.get("com.satis.viewmodel.core", "ObserveStore")
        builder.addSuperinterface(observeStore)
        builder.addModifiers(Modifier.PUBLIC)
        overrideMethod()
    }

    private fun overrideMethod() {
        val linkedHashMapTypeName = ClassName.get(LinkedHashMap::class.java)
        val classTypeName = ClassName.get(Class::class.java)
        val lifeOwner = TypeVariableName.get("? extends androidx.lifecycle.LifecycleOwner")
        val classParameterizedType = ParameterizedTypeName.get(classTypeName, lifeOwner.box())
        val constructorTypeName = ClassName.get(Constructor::class.java)
        val observerTypeName = TypeVariableName.get("? extends com.satis.viewmodel.core.Observer")
        val constructorParameterizedType =
            ParameterizedTypeName.get(constructorTypeName, observerTypeName.box())
        val linkedHashMapType = ParameterizedTypeName.get(
            linkedHashMapTypeName,
            classParameterizedType,
            constructorParameterizedType
        )
        storeMethod = MethodSpec.methodBuilder("getStore")
            .addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC)
            .returns(linkedHashMapType)
            .addStatement("\$T map = new \$T()", linkedHashMapType, linkedHashMapType)
            .beginControlFlow("try")
    }

    fun put(typeElement: TypeElement) {
        val host = ClassName.bestGuess(typeElement.qualifiedName.toString())
        val className = ClassName.get(
            ProcessorKt.Companion.APT_PACKAGE,
            typeElement.simpleName.toString() + "\$Observe"
        )
        storeMethod!!.addStatement("map.put(\$T.class,\$T.class.getConstructor())", host, className)
    }

    fun endMethod() {
        storeMethod!!.endControlFlow()
            .beginControlFlow("catch(\$T e)", Exception::class.java)
            .endControlFlow()
            .addStatement("return map")
    }

    fun build(): TypeSpec {
        builder!!.addMethod(storeMethod!!.build())
        return builder!!.build()
    }

    init {
        initDefaultTypeBuilder()
    }
}