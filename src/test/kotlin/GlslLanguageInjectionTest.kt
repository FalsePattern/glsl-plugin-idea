import com.intellij.testFramework.fixtures.BasePlatformTestCase
import glsl.psi.interfaces.GlslSingleDeclaration
import glsl.psi.interfaces.GlslStructDeclarator
import junit.framework.TestCase
import junit.framework.TestCase.assertNull
import org.junit.Test

class GlslLanguageInjectionTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String {
        return "src/test/testData/languageInjection"
    }

    fun testLanguageInjection1() {
        val reference = myFixture.getReferenceAtCaretPosition("LanguageInjectionHtml.html")
        val resolve = reference?.resolve()
        assertInstanceOf(resolve, GlslSingleDeclaration::class.java)
        assertEquals("projMatrix", (resolve as GlslSingleDeclaration).name)
    }
}