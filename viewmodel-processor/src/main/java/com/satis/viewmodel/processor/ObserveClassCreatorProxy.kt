package com.satis.viewmodel.processor

import com.satis.viewmodel.annotation.Observe
import com.satis.viewmodel.processor.utils.javaToKotlinType
import com.squareup.kotlinpoet.*
import java.util.HashMap
import javax.annotation.processing.Messager
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.lang.model.util.Elements

/**
 * Created by ssb
 * date 2020/3/11
 * 创建Java文件代理类
 */
class ObserveClassCreatorProxy(elementUtils: Elements, val typeElement: TypeElement) {
    private val observeClassName: String
    private val mObserveElementMap: MutableMap<Observe, ExecutableElement> = HashMap()
    private val packageName: String

    //存储方法信息
    fun putElement(observe: Observe, element: ExecutableElement) {
        mObserveElementMap[observe] = element
    }

    /**
     * 创建Java代码
     * javapoet
     *
     * @return
     */
    fun generateJavaCode(block:(String,ClassName)->Unit): TypeSpec {
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
    private fun generateMethods(block: (String,ClassName) -> Unit): FunSpec {
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
            val className = variableElement.javaToKotlinType()!!
            val paramTypeString = className.packageName.replace(".","_")+"_"+className.simpleName
            block.invoke(method,className)
            funSpecBuilder.beginControlFlow(
                "val ${method}_${paramTypeString} = object:%T<%T>",
                observer,
                className
            )
                .beginControlFlow("override fun onChanged(t:%T)",className)
                .addStatement("(host as %T).$method(t)", host)
                .endControlFlow()
                .endControlFlow()
            funSpecBuilder.addStatement(
                "(host as %T).viewModel.addObserver(host,\"${method}_${paramTypeString}\" ," + " %T(${method}_${paramTypeString})," + observe.sticky + ")",
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
}