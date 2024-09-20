package ifsc.compiladores.projeto.API.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("compiler")
public class CompilerConfiguration {

    private String cachePath;

    private String codeFileName;

    private String irFileName;
    private String irOptFileNameTemplate;
    private String llvmOptimizer;

    private String asmFileName;
    private String asmOptFileNameTemplate;
    private String clangCompiler;

    private String syntaxTreeFileName;

    public String getCachePath() {
        return cachePath;
    }

    public void setCachePath(String cachePath) {
        this.cachePath = cachePath;
    }

    public String getCodeFileName() {
        return codeFileName;
    }

    public void setCodeFileName(String codeFileName) {
        this.codeFileName = codeFileName;
    }

    public String getIrFileName() {
        return irFileName;
    }

    public void setIrFileName(String irFileName) {
        this.irFileName = irFileName;
    }

    public String getIrOptFileNameTemplate() {
        return irOptFileNameTemplate;
    }

    public void setIrOptFileNameTemplate(String irOptFileNameTemplate) {
        this.irOptFileNameTemplate = irOptFileNameTemplate;
    }

    public String getLLVMOptimizer() {
        return llvmOptimizer;
    }

    public void setLLVMOptimizer(String llvmOptimizer) {
        this.llvmOptimizer = llvmOptimizer;
    }

    public String getAsmFileName() {
        return asmFileName;
    }

    public void setAsmFileName(String asmFileName) {
        this.asmFileName = asmFileName;
    }

    public String getAsmOptFileNameTemplate() {
        return asmOptFileNameTemplate;
    }

    public void setAsmOptFileNameTemplate(String asmOptFileNameTemplate) {
        this.asmOptFileNameTemplate = asmOptFileNameTemplate;
    }

    public String getClangCompiler() {
        return clangCompiler;
    }

    public void setClangCompiler(String clangCompiler) {
        this.clangCompiler = clangCompiler;
    }

    public String getSyntaxTreeFileName() {
        return syntaxTreeFileName;
    }

    public void setSyntaxTreeFileName(String syntaxTreeFileName) {
        this.syntaxTreeFileName = syntaxTreeFileName;
    }
}
