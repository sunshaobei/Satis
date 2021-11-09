package com.satis.viewmodel.processor

import com.squareup.javapoet.*
import java.util.*
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

class ViewModelEnumCreatorPRoxy {
    private lateinit var builder: TypeSpec.Builder
    private fun initDefaultTypeBuilder() {
        builder = TypeSpec.enumBuilder("SatisViewModelEnum")
        builder.addModifiers(Modifier.PUBLIC)
    }

    private fun getBuilder(): TypeSpec.Builder? {
        if (builder == null) {
            initDefaultTypeBuilder()
        }
        return builder
    }

    fun build(): TypeSpec {
        return getBuilder()!!.build()
    }

    fun addConstructorMethod() {
        val t = TypeVariableName.get("? extends com.satis.viewmodel.core.Observer")
        val className = ClassName.get(Class::class.java)
        val parameterizedTypeName = ParameterizedTypeName.get(className, t.box())
        getBuilder()!!.addField(parameterizedTypeName, "mClass", Modifier.PRIVATE)
        val set = MethodSpec.constructorBuilder()
            .addParameter(parameterizedTypeName, "mClass")
            .addStatement("this.\$N = \$N", "mClass", "mClass")
            .build()
        val get = MethodSpec.methodBuilder("getObserveClass")
            .addModifiers(Modifier.PUBLIC)
            .returns(parameterizedTypeName)
            .addStatement("return mClass")
            .build()
        getBuilder()!!.addMethod(set)
        getBuilder()!!.addMethod(get)
    }

    fun addEnum(typeElement: TypeElement) {
        val simpleName = typeElement.simpleName.toString()
        val className = ClassName.get(Processor.Companion.APT_PACKAGE, "$simpleName\$Observe")
        getBuilder()!!.addEnumConstant(
            simpleName.uppercase(Locale.getDefault()),
            TypeSpec.anonymousClassBuilder("\$T.class", className).build()
        )
    }
}