package glsl.plugin.annotator

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset
import glsl.plugin.psi.named.GlslNamedFunctionHeader
import glsl.psi.interfaces.GlslFunctionCall
import glsl.psi.interfaces.GlslSingleDeclaration
import glsl.psi.interfaces.GlslVariableIdentifier


class GlslCodeAnnotator : Annotator {

    /**
     *
     */
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        when (element) {
            is GlslVariableIdentifier -> {

            }
            is GlslSingleDeclaration -> {

            }
            is GlslFunctionCall -> {

            }
        }
    }

    /**
     *
     */
    private fun annotateFunctionCall(element: GlslFunctionCall, holder: AnnotationHolder) {
        val resolvedFunction = element.variableIdentifier?.reference?.resolve() ?: return
        val functionHeader = resolvedFunction as? GlslNamedFunctionHeader ?: return
        val parameterDeclarators = functionHeader.getParameterDeclarators()
        val paramExprList = element.exprNoAssignmentList
        if (parameterDeclarators.count() != paramExprList.count()) {
            val textRange = TextRange(paramExprList.first().startOffset, paramExprList.last().endOffset)
            setHighlightingError(textRange, holder, "Incorrect number of parameters")
        }
    }

    /**
     *
     */
    fun setHighlightingError(element: PsiElement?, holder: AnnotationHolder, message: String) {
        if (element == null) return
        holder.newAnnotation(HighlightSeverity.ERROR, message)
            .highlightType(ProblemHighlightType.GENERIC_ERROR)
            .range(element)
            .create()
    }

    /**
     *
     */
    fun setHighlightingError(range: TextRange, holder: AnnotationHolder, message: String) {
        holder.newAnnotation(HighlightSeverity.ERROR, message)
            .highlightType(ProblemHighlightType.GENERIC_ERROR)
            .range(range)
            .create()
    }
}