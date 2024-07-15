package glsl.plugin.psi.builtins

import com.intellij.lang.ASTNode
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.psi.PsiElement
import glsl.data.GlslDefinitions
import glsl.plugin.psi.named.GlslNamedElement
import glsl.plugin.psi.named.GlslNamedType
import glsl.plugin.psi.named.GlslNamedTypeImpl
import glsl.plugin.psi.named.GlslNamedVariable
import glsl.plugin.utils.GlslBuiltinUtils
import javax.swing.Icon


/**
 *
 */
class GlslVector(node: ASTNode) : GlslNamedTypeImpl(node) {
    override fun getSelf(): GlslNamedElement {
        TODO("Not yet implemented")
    }

    override fun getHighlightTextAttr(): TextAttributesKey {
        TODO("Not yet implemented")
    }

    override fun getLookupIcon(): Icon? {
        TODO("Not yet implemented")
    }

    override fun getNameIdentifier(): PsiElement? {
        TODO("Not yet implemented")
    }

    /**
     *
     */
    override fun getStructMembers(): List<GlslNamedVariable> {
        val vectorComponents = GlslBuiltinUtils.getVecStructs()[name]
        return emptyList()
    }

    /**
     *
     */
    override fun getStructMember(memberName: String): GlslNamedVariable? {
        return null
    }

    /**
     *
     */
    override fun getDimension(): Int {
        val lastChar = name?.last()
        return when (lastChar) {
            '2' -> 2
            '3' -> 3
            '4' -> 4
            else -> 0
        }
    }

    /**
     *
     */
    private fun getVectorComponentType(): String {
        val typeText = name?.first()
        return when (typeText) {
            'v', 'f' -> "float"
            'd' -> "double"
            'u' -> "uint"
            'i' -> "int"
            'b' -> "bool"
            else -> ""
        }
    }

    /**
     *
     */
    override fun isConvertible(other: String): Boolean {
        val implicitConversions = GlslDefinitions.VECTORS[name]
        return implicitConversions?.contains(other) ?: false
    }

    /**
     *
     */
    override fun getBinaryExprType(rightExprType: GlslNamedType?, expr: PsiElement?): GlslNamedType? {
//        if (rightExprType is GlslScalar || rightExprType is GlslVector) {
//            return this
//        } else if (rightExprType is GlslMatrix) {
//            if (expr is GlslMulExpr) {
//                val leftRows = getDimension()
//                val leftColumn = 1
//                val rightRows = rightExprType.getRowDimension()
//                val rightColumns = rightExprType.getColumnDimension()
//                if (leftColumn == rightRows) {
//                    if (leftRows == rightColumns) {
//                        return GlslVector("mat{$leftRows}")
//                    } else {
//                        return GlslVector("mat{$leftRows}x{$rightColumns}")
//                    }
//                }
//            }
//            return null
//        }
//        return this
        return null
//    }
//
//    /**
//     *
//     */
//    fun getChildType(exprList: List<GlslExpr>): GlslTypeName {
//        if (exprList.isEmpty()) {
//            return this
//        }
//        return GlslScalar(getVectorComponentType())
    }
}
