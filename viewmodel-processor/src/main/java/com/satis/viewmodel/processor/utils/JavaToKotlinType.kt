package com.satis.viewmodel.processor.utils

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.AnnotationValue
import javax.lang.model.element.Element
import javax.lang.model.type.TypeMirror
import kotlin.reflect.jvm.internal.impl.builtins.jvm.JavaToKotlinClassMap
import kotlin.reflect.jvm.internal.impl.name.FqName

/**
 * 获取需要把java类型映射成kotlin类型的ClassName  如：java.lang.String 在kotlin中的类型为kotlin.String 如果是空则表示该类型无需进行映射
 */
fun Element.javaToKotlinType(): ClassName? {
    val className =
        JavaToKotlinClassMap.INSTANCE.mapJavaToKotlin(FqName(this.asType().asTypeName().toString()))
            ?.asSingleFqName()?.asString()
    return if (className == null) {
        null
    } else {
        ClassName.bestGuess(className)
    }
}

fun TypeName.javaToKotlinType(): TypeName = when (this) {
    is ParameterizedTypeName -> {
        (rawType.javaToKotlinType() as ClassName).parameterizedBy(
            *typeArguments.map {
                it.javaToKotlinType()
            }.toTypedArray()
        )
    }
    is WildcardTypeName -> {
        if (inTypes.isNotEmpty()) WildcardTypeName.consumerOf(inTypes[0].javaToKotlinType())
        else WildcardTypeName.producerOf(outTypes[0].javaToKotlinType())
    }

    else -> {
        val className = JavaToKotlinClassMap
            .INSTANCE.mapJavaToKotlin(FqName(toString()))?.asSingleFqName()?.asString()
        if (className == null) this
        else ClassName.bestGuess(className)
    }
}


private fun getEventTypeAnnotationMirror(
    typeElement: Element,
    clazz: Class<*>
): AnnotationMirror? {
    val clazzName = clazz.name
    for (m in typeElement.annotationMirrors) {
        if (m.annotationType.toString() == clazzName) {
            return m
        }
    }
    return null
}

private fun getAnnotationValue(annotationMirror: AnnotationMirror, key: String): AnnotationValue? {
    for ((key1, value) in annotationMirror.elementValues) {
        if (key1!!.simpleName.toString() == key) {
            return value
        }
    }
    return null
}

 fun Element.getMyValue(clazz: Class<*>, key: String): TypeMirror? {
    val am = getEventTypeAnnotationMirror(this, clazz) ?: return null
    val av = getAnnotationValue(am, key)
    return if (av == null) {
        null
    } else {
        av.value as TypeMirror
    }
}