package com.satis.viewmodel.processor

import com.satis.viewmodel.processor.utils.javaToKotlinType
import com.squareup.kotlinpoet.*
import java.util.HashMap
import javax.annotation.processing.Messager
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.tools.Diagnostic
import kotlin.math.log

/**
 * Created by ssb
 * date 2020/3/11
 * 创建Java文件代理类
 */
class ObserveClassCreatorProxy(elementUtils: Elements,val mLog:Messager?, val typeElement: TypeElement) {
    private val observeClassName: String
    private val mObserveElementMap: MutableMap<Observe, ExecutableElement> = HashMap()
    private val packageName: String

    //存储方法信息
    fun putElement(observe: com.satis.viewmodel.annotation.Observe, element: ExecutableElement) {
        mObserveElementMap[Observe(observe)] = element
    }

    /**
     * 创建Java代码
     * javapoet
     *
     * @return
     */
    fun generateJavaCode(block:(String,String,TypeName)->Unit): TypeSpec {
        val className = ClassName("com.satis.viewmodel.core", "Observer")
        return TypeSpec.classBuilder(observeClassName)
            .addModifiers(KModifier.PUBLIC)
            .addSuperinterface(className)
            .addFunction(generateMethods(block))
            .build()
    }

    /**
     * 加入Method
     * javapoet
     */
    private fun generateMethods(block: (String,String,TypeName) -> Unit): FunSpec {
        val host = ClassName.bestGuess(typeElement.qualifiedName.toString())
        val owner = ClassName("androidx.lifecycle", "LifecycleOwner")
        val funSpecBuilder = FunSpec.builder("observe")
            .addModifiers(KModifier.OVERRIDE)
            .addParameter("host",owner)
        val baseObserver = ClassName("com.satis.viewmodel.core", "BaseObserver")
        val observer = ClassName("androidx.lifecycle", "Observer")
        //计数形式规避方法重载
        var count = 0
        for ((observe, value) in mObserveElementMap) {
            val method = value.simpleName.toString()
            val parameters = value.parameters
            val variableElement = parameters[0]!!
            mLog?.printMessage(Diagnostic.Kind.WARNING,"参数类型----------constantValue:${variableElement.constantValue} - sampleName:${variableElement.simpleName} - type ${variableElement.asType().asTypeName()}")
            val typeName = variableElement.asType().asTypeName().javaToKotlinType()
            var paramTypeString:String
            if (typeName is ParameterizedTypeName){
                mLog?.printMessage(Diagnostic.Kind.WARNING,"typeName----$typeName")
                val rawType = typeName.rawType
                mLog?.printMessage(Diagnostic.Kind.WARNING,"rawType----$rawType")
                val typeArguments = typeName.typeArguments
                mLog?.printMessage(Diagnostic.Kind.WARNING,"typeArguments----$typeArguments")
                paramTypeString = rawType.toString().replace(".","_")+ if (typeArguments.isNotEmpty()){
                    "_${typeArguments[0].toString().replace(".","_").replace(" ","_").trim()}"
                }else{""}
            }else if (typeName is ClassName){
                paramTypeString = typeName.packageName.replace(".","_")+"_"+typeName.simpleName
            }else{
                paramTypeString = method+count
            }

            var observeTag = observe.tag
            if (observeTag.isEmpty()){
                observeTag = "${method}(${typeName.toString().replace(" ","_")})"
            }

            //生成方法扩展
            block.invoke(method,observeTag,typeName)

            funSpecBuilder.beginControlFlow(
                "val ${method}_${paramTypeString} = object:%T<%T>",
                observer,
                typeName
            )
                .beginControlFlow("override fun onChanged(t:%T)",typeName)
                .addStatement("(host as %T).$method(t)", host)
                .endControlFlow()
                .endControlFlow()
            funSpecBuilder.addStatement(
                "(host as %T).viewModel.addObserver(host,\"$observeTag\" , %T(${method}_${paramTypeString}),${observe.isSticky})",
                host,
                baseObserver
            )
            count++
        }
        return funSpecBuilder.build()
    }

    init {
        val packageElement = elementUtils.getPackageOf(
            typeElement
        )
        packageName = packageElement.qualifiedName.toString()
        val className = typeElement.simpleName.toString()
        observeClassName = "${className}_Observe"
    }

    class Observe(observe:com.satis.viewmodel.annotation.Observe){
        var isSticky = observe.sticky
        var tag = observe.tag
    }
}