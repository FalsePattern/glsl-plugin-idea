package glsl.plugin.utils

import com.intellij.codeInsight.completion.InsertHandler
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.editor.EditorModificationUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.search.FilenameIndex.getVirtualFilesByName
import com.intellij.psi.search.GlobalSearchScope.allScope
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.PsiUtilCore
import glsl.data.ShaderType
import glsl.plugin.language.GlslFile
import glsl.psi.interfaces.GlslFunctionPrototype
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.swing.Icon

/**
 *
 */
object GlslUtils {

    /**
     *
     */
    @JvmStatic
    fun getResourceFileAsString(path: String): String? {
        val file = GlslUtils::class.java.classLoader.getResourceAsStream(path)
        if (file != null) {
            val reader = BufferedReader(InputStreamReader(file))
            return reader.lines().toArray().joinToString("\n")
        }
        return null
    }


    /**
     *
     */
    @JvmStatic
    fun createLookupElement(
        lookupString: String,
        insertHandler: InsertHandler<LookupElement>? = null,
        icon: Icon? = null,
        returnTypeText: String? = null,
        withBoldness: Boolean = false,
        psiElement: PsiElement? = null,
    ): LookupElement {
        return LookupElementBuilder.create(lookupString)
            .withIcon(icon)
            .withTypeText(returnTypeText)
            .withInsertHandler(insertHandler)
            .withCaseSensitivity(false)
            .withBoldness(withBoldness)
            .withPsiElement(psiElement)
    }

    /**
     *
     */
    @JvmStatic
    fun createLookupElements(
        tokenList: List<String>,
        insertHandler: InsertHandler<LookupElement>? = null,
        icon: Icon? = null,
        returnTypeText: String? = null
    ): List<LookupElement> {
        return tokenList.map { createLookupElement(it, insertHandler, icon, returnTypeText) }
    }

    /**
     *
     */
    @JvmStatic
    fun getFunctionLookupElement(func: GlslFunctionPrototype, icon: Icon?): LookupElement {
        val functionHeader = func.functionHeader
        var typeQualifierStr = functionHeader.typeQualifier?.text ?: ""
        // If type qualifier text has multiple words, take the first one
        if (typeQualifierStr.contains(" ")) {
            typeQualifierStr = typeQualifierStr.substring(0, typeQualifierStr.indexOf(' '))
        }
        val typeSpecifierStr = functionHeader.getAssociatedType()?.name ?: ""
        val fullySpecifiedType = "$typeQualifierStr $typeSpecifierStr".trim()
        val funcName = functionHeader.variableIdentifier.text
        var funcHeader = "$funcName("
        val funcHeaderWithParams = func.funcHeaderWithParams
        var hasParams = false
        if (funcHeaderWithParams != null) {
            funcHeader += funcHeaderWithParams.text
            hasParams = true
        }
        return createLookupElement(
            "$funcHeader)",
            getFunctionInsertHandler(hasParams),
            icon,
            fullySpecifiedType
        )
    }

    /**
     *
     */
    @JvmStatic
    fun getFunctionInsertHandler(hasParams: Boolean = true): InsertHandler<LookupElement> {
        return InsertHandler { context, item ->
            val document = context.document
            val funcName = removeArgsFromFuncText(item.lookupString)
            val offset = context.editor.caretModel.offset
            val text = document.getText(TextRange(offset, offset + 1))
            if (text == "(") {
                document.replaceString(context.startOffset, context.selectionEndOffset, funcName)
            } else {
                document.replaceString(context.startOffset, context.selectionEndOffset, "$funcName()")
                if (!hasParams) return@InsertHandler
                EditorModificationUtil.moveCaretRelatively(context.editor, -1)
            }
        }
    }

    /**
     *
     */
    @JvmStatic
    fun getVectorInsertHandler(): InsertHandler<LookupElement> {
        return InsertHandler { context, item ->
            val document = context.document
            val funcName = item.lookupString
            val offset = context.editor.caretModel.offset
            val text = document.getText(TextRange(offset, offset + 1))
            if (text == "(") {
                document.replaceString(context.startOffset, context.selectionEndOffset, funcName)
            } else {
                document.replaceString(context.startOffset, context.selectionEndOffset, "$funcName()")
                EditorModificationUtil.moveCaretRelatively(context.editor, +1)
            }
        }
    }

    /**
     *
     */
    @JvmStatic
    fun getPsiFile(path: String, project: Project?): GlslFile? {
        if (project == null) return null
        val virtualFilesByName = getVirtualFilesByName(path, allScope(project))
        if (virtualFilesByName.isEmpty()) return null
        val psiFile = PsiUtilCore.getPsiFile(project, virtualFilesByName.first()) as GlslFile
        return psiFile
    }

    /**
     *
     */
    @JvmStatic
    fun getTokenSetAsStrings(tokenSet: TokenSet): Array<String> {
        return tokenSet.types.map { it.toString() }.toTypedArray()
    }

    /**
     *
     */
    @JvmStatic
    fun removeArgsFromFuncText(funcText: String): String {
        return funcText.replace("\\(.*\\)".toRegex(), "")
    }

    /**
     *
     */
    @JvmStatic
    fun isShaderFile(element: PsiElement): Boolean {
        val extension = element.containingFile.virtualFile.extension
        return enumValues<ShaderType>().any { extension?.lowercase() == it.name.lowercase() }
    }
}

