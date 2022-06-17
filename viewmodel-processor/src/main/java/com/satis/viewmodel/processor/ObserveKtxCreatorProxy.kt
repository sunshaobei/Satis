package com.satis.viewmodel.processor

import com.squareup.kotlinpoet.*
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeMirror
import javax.tools.Diagnostic

/**
 * Created by sunshaobei on 2022/3/7.
 */
class ObserveKtxCreatorProxy(val log: Messager, val processingEnv: ProcessingEnvironment) {
    val typeSpec = TypeSpec.objectBuilder("ObserveKtx")
    private val funDataList = ArrayList<FunData>()
    fun addFun(typeElement: TypeElement, methodName: String,tag:String, paramType: TypeName) {
        var typeMirror:TypeMirror? = null
        var declaredType:DeclaredType
        var superTypeElement = typeElement
        while (true){
            declaredType = superTypeElement.superclass as DeclaredType
            if (declaredType.toString().startsWith("com.satis.core.component.mvvm.MVVMActivity")){
                typeMirror = declaredType.typeArguments[1]
                log.printMessage(Diagnostic.Kind.WARNING, "提示：找到脚手架-${superTypeElement.simpleName} ---$declaredType")
                break
            }
            if (declaredType.toString().startsWith("com.satis.core.component.mvm.MVMActivity")){
                typeMirror = declaredType.typeArguments[0]
                log.printMessage(Diagnostic.Kind.WARNING,"提示：找到脚手架-${superTypeElement.simpleName} ---$declaredType")
                break
            }
            if (declaredType.toString().startsWith("com.satis.core.component.mvvm.MVVMFragment")){
                typeMirror = declaredType.typeArguments[1]
                log.printMessage(Diagnostic.Kind.WARNING, "提示：找到脚手架-${superTypeElement.simpleName}---$declaredType")
                break
            }
            if (declaredType.toString().startsWith("com.satis.core.component.mvm.MVMFragment")){
                typeMirror = declaredType.typeArguments[0]
                log.printMessage(Diagnostic.Kind.WARNING,"提示：找到脚手架-${superTypeElement.simpleName} ---$declaredType")
                break
            }
            if (declaredType.toString().startsWith("androidx.appcompat.app.AppCompatActivity")){
                log.printMessage(Diagnostic.Kind.WARNING,"提示：非继承 MVMActivity、MVVMActivity")
                break
            }
            if (declaredType.toString().startsWith("androidx.fragment.app.Fragment")){
                log.printMessage(Diagnostic.Kind.WARNING,"提示：非继承 MVMFragment、MVVMFragment")
                break
            }
            superTypeElement = declaredType.asElement() as TypeElement
        }
        if (typeMirror==null){
            var interfaces:List<out TypeMirror>? = null
            var interfaceDeclaredType = typeElement.superclass as DeclaredType
            var interfaceTypeElement = typeElement
            var needBreak = false
            while (true){
                interfaces = interfaceTypeElement.interfaces
                for(interfaceItem in interfaces){
                    if (interfaceItem is DeclaredType) {
                        if (interfaceItem.toString().startsWith("com.satis.core.component.mvm.MVM")) {
                            typeMirror = interfaceItem.typeArguments[0] as TypeMirror
                            log.printMessage(Diagnostic.Kind.WARNING, "提示：找到脚手架-$interfaceItem")
                            needBreak = true
                            break
                        }
                        if (interfaceItem.toString().startsWith("com.satis.core.component.mvvm.MVVM")) {
                            typeMirror = interfaceItem.typeArguments[1] as TypeMirror
                            log.printMessage(Diagnostic.Kind.WARNING, "提示：找到脚手架-$interfaceItem")
                            needBreak = true
                            break
                        }
                    }
                }
                if (needBreak){
                    break
                }
                if (interfaceDeclaredType.toString().startsWith("androidx.appcompat.app.AppCompatActivity")){
                    log.printMessage(Diagnostic.Kind.ERROR,"并未实现 MVM、MVVM")
                    break
                }
                interfaceTypeElement = declaredType.asElement() as TypeElement
                interfaceDeclaredType = interfaceTypeElement.superclass as DeclaredType
            }
        }

        if (typeMirror!=null){
            funDataList.forEach {
                if (it.methodName == methodName && it.paramType == paramType && it.receiverType == typeMirror) {
                    return
                }
            }
            funDataList.add(FunData(methodName, paramType,typeMirror))

            val funSpec = FunSpec.builder(methodName)
            funSpec.addModifiers(KModifier.INLINE)
            funSpec.receiver(typeMirror.asTypeName())
            funSpec.addParameter("arg", paramType)
            funSpec.addCode("setValue(\"$tag\",arg)")
            typeSpec.addFunction(funSpec.build())

            val funSpec2 = FunSpec.builder("io$methodName")
            funSpec2.addModifiers(KModifier.INLINE)
            funSpec2.receiver(typeMirror.asTypeName())
            funSpec2.addParameter("arg", paramType)
            funSpec2.addCode("postValue(\"$tag\",arg)")
            typeSpec.addFunction(funSpec2.build())
        }else{
            log.printMessage(Diagnostic.Kind.ERROR,"建议参考 Sample Demo 使用")
        }
    }
}

data class FunData(val methodName: String, val paramType: TypeName,val receiverType:TypeMirror)

