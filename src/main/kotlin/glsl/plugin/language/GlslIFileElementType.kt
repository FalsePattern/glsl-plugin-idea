package glsl.plugin.language

import com.intellij.lang.ASTNode
import com.intellij.lang.impl.PsiBuilderImpl
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IFileElementType
import glsl.plugin.utils.GlslUtils.getRealVirtualFile

/**
 *
 */
class GlslIFileElementType : IFileElementType(GlslLanguage.INSTANCE) {
    override fun doParseContents(chameleon: ASTNode, psi: PsiElement): ASTNode? {
        val project = psi.project
        val parserDefinition = GlslParserDefinition()
        val builder = PsiBuilderImpl(project, parserDefinition, GlslLexer(project, psi.getRealVirtualFile()), chameleon, chameleon.chars)
        val parser = GlslParserAdapter()
        val rootNode = parser.parse(this, builder)
        return rootNode.firstChildNode
    }
}