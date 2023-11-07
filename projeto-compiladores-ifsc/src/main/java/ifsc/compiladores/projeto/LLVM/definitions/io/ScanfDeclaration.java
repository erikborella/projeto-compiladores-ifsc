package ifsc.compiladores.projeto.LLVM.definitions.io;

import ifsc.compiladores.projeto.LLVM.Fragment;

public class ScanfDeclaration implements Fragment {

    @Override
    public String getText() {
        return "declare i32 @scanf(i8*, ...)";
    }
    
}
