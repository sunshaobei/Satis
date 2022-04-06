package com.satis.viewmodel.processor

import com.satis.viewmodel.processor.utils.getPackagePath
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import java.lang.Exception
import java.lang.reflect.Constructor
import java.util.LinkedHashMap
import javax.lang.model.element.TypeElement

class ObserveStoreCreatorProxy(private val moduleName: String) {
    private lateinit var typeSpecBuilder: TypeSpec.Builder
    private var getFunSpecBuilder: FunSpec.Builder? = null
    private var propertySpec: PropertySpec.Builder? = null
    private fun initDefaultTypeBuilder() {
        typeSpecBuilder = TypeSpec.classBuilder("ObserveStore_$moduleName")
        val observeStore = ClassName("com.satis.viewmodel.core", "ObserveStore")
        typeSpecBuilder.addSuperinterface(observeStore)
        addProperty()
    }

    private fun addProperty() {
        val classTypeName = Class::class.java.asClassName()
        val lifeOwner = TypeVariableName("out androidx.lifecycle.LifecycleOwner")
        val classParameterizedType = classTypeName.parameterizedBy(lifeOwner)
        val constructorTypeName = Constructor::class.java.asClassName()
        val observerTypeName = TypeVariableName("out com.satis.viewmodel.core.Observer")
        val constructorParameterizedType =
            constructorTypeName.parameterizedBy(observerTypeName)
        val linkedHashMapType = LinkedHashMap::class.java.asClassName().parameterizedBy(
            classParameterizedType,
            constructorParameterizedType
        )

        getFunSpecBuilder = FunSpec.getterBuilder()
            .addStatement("val map = %T()", linkedHashMapType)
            .beginControlFlow("try")


        propertySpec =
            PropertySpec.builder("store", linkedHashMapType, KModifier.OVERRIDE)

    }

    fun put(typeElement: TypeElement) {
        val host = ClassName.bestGuess(typeElement.qualifiedName.toString())
        val className = ClassName(
                    getPackagePath(typeElement),
            typeElement.simpleName.toString() + "_Observe"
        )
        getFunSpecBuilder!!.addStatement("map.put(%T::class.java,%T::class.java.getConstructor())",
            host,
            className)
    }

    fun endMethod() {
        getFunSpecBuilder!!.endControlFlow()
            .beginControlFlow("catch(e:%T)", Exception::class.java)
            .endControlFlow()
            .addStatement("return map")
        propertySpec!!.getter(getFunSpecBuilder!!.build())
    }

    fun build(): TypeSpec {
        typeSpecBuilder.addProperty(propertySpec!!.build())
        return typeSpecBuilder.build()
    }

    init {
        initDefaultTypeBuilder()
    }
}