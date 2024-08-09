package ifsc.compiladores.projeto.API.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("compiler.file.cache")
public class FileCacheConfiguration {
    private String path;
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
