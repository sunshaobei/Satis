package com.satis.viewmodel.processor.kotlin

import com.squareup.kotlinpoet.*
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.TypeElement

/**
 * Created by sunshaobei on 2022/3/7.
 */
class ObserveKtxCreatorProxy() {


    val typeSpec = TypeSpec.objectBuilder("ObserveKtx")
    val funDatas = ArrayList<FunData>()

    fun addFun(typeElement: TypeElement, methodName: String, paramType: ClassName) {
        funDatas.forEach {
            if (it.methodName == methodName && it.paramType == paramType) {
                return
            }
        }
        funDatas.add(FunData(methodName, paramType))
        val typeMirror = typeElement.superclass
        val asTypeName = typeMirror.asTypeName().toString()
        val indexOf = asTypeName.indexOf("<")
        val indexOf1 = asTypeName.indexOf(">")
        val substring = asTypeName.substring(indexOf + 1, indexOf1)
        val bestGuess = ClassName.bestGuess(substring)
        val funSpec = FunSpec.builder(methodName)
        funSpec.receiver(bestGuess)
        funSpec.addParameter("arg", paramType)
        funSpec.addCode("setValue(\"$methodName\",arg)")
        typeSpec.addFunction(funSpec.build())

    }
}

data class FunData(val methodName: String, val paramType: ClassName)

