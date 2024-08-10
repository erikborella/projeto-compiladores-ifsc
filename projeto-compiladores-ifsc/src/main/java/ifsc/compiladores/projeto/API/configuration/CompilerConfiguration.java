package ifsc.compiladores.projeto.API.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("compiler")
public class CompilerConfiguration {

    private String cachePath;
    private String codeFileName;
    private String irFileName;

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
}
