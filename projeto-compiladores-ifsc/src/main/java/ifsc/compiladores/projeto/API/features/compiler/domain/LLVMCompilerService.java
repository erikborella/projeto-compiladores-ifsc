package ifsc.compiladores.projeto.API.features.compiler.domain;

import java.io.IOException;
import java.util.Optional;

public interface LLVMCompilerService {

    Optional<String> getLLVMIRCode(String codeId) throws IOException;
    Optional<String> getOptLLVMIrCode(String codeId, OptLevel optLevel) throws IOException, InterruptedException;
    Optional<String> getAsmCode(String codeId) throws IOException, InterruptedException;
    Optional<String> getOptAsmCode(String codeId, OptLevel optLevel) throws IOException, InterruptedException;
}
