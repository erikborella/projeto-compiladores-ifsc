package ifsc.compiladores.projeto.API.features.compiler.domain;

import java.io.IOException;
import java.util.Optional;

public interface LLVMCompilerService {

    Optional<String> getLLVMIRCode(String sourceCode) throws IOException;

}
