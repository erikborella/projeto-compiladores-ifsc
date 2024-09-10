package ifsc.compiladores.projeto.API.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("compiler")
public class CompilerConfiguration {

    private String cachePath;
    private String codeFileName;
    private String irFileName;
    private String asmFileName;
    private String optFileNameTemplate;
    private String syntaxTreeFileName;
    private String clangCompiler;
    private String llvmOptimizer;

    public String getCodeFileName() {
        return codeFileName;
    }

    public void setCodeFileName(String codeFileName) {
        this.codeFileName = codeFileName;
    }

    public String getCachePath() {
        return cachePath;
    }

    public void setCachePath(String cachePath) {
        this.cachePath = cachePath;
    }

    public String getIrFileName() {
        return irFileName;
    }

    public void setIrFileName(String irFileName) {
        this.irFileName = irFileName;
    }

    public String getAsmFileName() {
        return asmFileName;
    }

    public void setAsmFileName(String asmFileName) {
        this.asmFileName = asmFileName;
    }

    public String getOptFileNameTemplate() {
        return optFileNameTemplate;
    }

    public void setOptFileNameTemplate(String optFileNameTemplate) {
        this.optFileNameTemplate = optFileNameTemplate;
    }

    public String getClangCompiler() {
        return clangCompiler;
    }

    public void setClangCompiler(String clangCompiler) {
        this.clangCompiler = clangCompiler;
    }

    public String getLLVMOptimizer() {
        return llvmOptimizer;
    }

    public void setLLVMOptimizer(String llvmOptimizer) {
        this.llvmOptimizer = llvmOptimizer;
    }

    public String getSyntaxTreeFileName() {
        return syntaxTreeFileName;
    }

    public void setSyntaxTreeFileName(String syntaxTreeFileName) {
        this.syntaxTreeFileName = syntaxTreeFileName;
    }
}
