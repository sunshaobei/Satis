package com.satis.viewmodel.processor

import com.satis.viewmodel.processor.utils.javaToKotlinType
import com.squareup.kotlinpoet.*
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType

/**
 * Created by sunshaobei on 2022/3/7.
 */
class ObserveKtxCreatorProxy(processingEnv: ProcessingEnvironment) {
    val types = processingEnv.typeUtils
    val typeSpec = TypeSpec.objectBuilder("ObserveKtx")
    private val funDataList = ArrayList<FunData>()
    fun addFun(typeElement: TypeElement, methodName: String, paramType: TypeName) {
        funDataList.forEach {
            if (it.methodName == methodName && it.paramType == paramType) {
                return
            }
        }
        funDataList.add(FunData(methodName, paramType))
        var declaredType = typeElement.superclass as DeclaredType
        var typeArguments = declaredType.typeArguments
        while (typeArguments.size != 2) {
            val superElement = declaredType.asElement() as TypeElement
            declaredType = superElement.superclass as DeclaredType
            typeArguments = declaredType.typeArguments
        }
        if (typeArguments.size == 2){
            val typeMirror = typeArguments[1]
            val funSpec = FunSpec.builder(methodName)
            funSpec.addModifiers(KModifier.INLINE)
            funSpec.receiver(typeMirror.asTypeName())
            funSpec.addParameter("arg", paramType)
            funSpec.addCode("setValue(\"${methodName}_${paramType.toString().replace(" ","_")}\",arg)")
            typeSpec.addFunction(funSpec.build())

            val funSpec2 = FunSpec.builder("io$methodName")
            funSpec2.addModifiers(KModifier.INLINE)
            funSpec2.receiver(typeMirror.asTypeName())
            funSpec2.addParameter("arg", paramType)
            funSpec2.addCode("postValue(\"${methodName}_${paramType.toString().replace(" ","_")}\",arg)")
            typeSpec.addFunction(funSpec2.build())
        }
    }
}

data class FunData(val methodName: String, val paramType: TypeName)

