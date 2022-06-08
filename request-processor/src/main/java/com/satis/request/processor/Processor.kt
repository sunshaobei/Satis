package com.satis.request.processor

import com.google.auto.service.AutoService
import com.satis.request.annotation.SatisUrl
import javax.annotation.processing.*
import javax.annotation.processing.Processor
import javax.lang.model.SourceVersion
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.tools.Diagnostic

@AutoService(Processor::class)
@SupportedAnnotationTypes("com.satis.request.annotation.SatisUrl")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class Processor: AbstractProcessor() {
    private lateinit var mLogger: Messager
    private lateinit var mElementUtils: Elements
    private lateinit var mModuleName: String


    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        mLogger = processingEnv.messager
        mElementUtils = processingEnv.elementUtils
        mModuleName = processingEnv.options["moduleName"].toString()
    }
    override fun process(p0: MutableSet<out TypeElement>?, roundEnvironment: RoundEnvironment): Boolean {
        mLogger.printMessage(Diagnostic.Kind.NOTE, "SatisUrl processing...")
        val annotationElements = roundEnvironment.getElementsAnnotatedWith(
            SatisUrl::class.java
        )

        for (element in annotationElements){
            val executableElement = element as ExecutableElement
            val classElement = executableElement.enclosingElement as TypeElement
            val fullClassName = classElement.qualifiedName.toString()
            val allMembers = mElementUtils.getAllMembers(classElement)
            for (member in allMembers){
                val executableElement1 = member as ExecutableElement
                val annotationMirrors = executableElement.annotationMirrors
                for (annotation in annotationElements){
//                    annotation.
                }
            }

        }


        return true
    }
}