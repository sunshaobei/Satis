package com.satis.viewmodel.processor

import com.satis.viewmodel.processor.utils.javaToKotlinType
import com.squareup.kotlinpoet.*
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.lang.model.type.DeclaredType

/**
 * Created by sunshaobei on 2022/3/7.
 */
class ObserveKtxCreatorProxy(processingEnv: ProcessingEnvironment) {
    val types = processingEnv.typeUtils
    val typeSpec = TypeSpec.objectBuilder("ObserveKtx")
    private val funDataList = ArrayList<FunData>()
    fun addFun(typeElement: TypeElement, methodName: String, paramType: ClassName) {
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
            val paramTypeString = paramType.packageName.replace(".","_")+"_"+paramType.simpleName
            funSpec.addCode("setValue(\"${methodName}_${paramTypeString}\",arg)")
            typeSpec.addFunction(funSpec.build())
        }
    }
}

data class FunData(val methodName: String, val paramType: ClassName)

