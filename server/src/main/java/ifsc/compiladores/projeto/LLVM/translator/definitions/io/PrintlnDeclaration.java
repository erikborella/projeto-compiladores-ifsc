package ifsc.compiladores.projeto.LLVM.translator.definitions.io;

import ifsc.compiladores.projeto.LLVM.translator.Fragment;

public class PrintlnDeclaration implements Fragment {

    @Override
    public String getText() {
        return "declare i32 @printf(i8*, ...)";
    }
    
}
