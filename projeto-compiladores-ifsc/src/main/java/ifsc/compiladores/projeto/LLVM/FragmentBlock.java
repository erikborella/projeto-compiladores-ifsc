package ifsc.compiladores.projeto.LLVM;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class FragmentBlock extends ArrayList<Fragment> implements Fragment {

    private final char INDENT_VALUE = ' ';
    private final int INDENT_NUMBER = 4;

    @Override
    public String getText() {
        return this.stream()
                .map(Fragment::getText)
                .collect(Collectors.joining("\n"));
    }

    public String getIndentedText(int indentation) {
        String indentationString = String.valueOf(INDENT_VALUE)
                .repeat(INDENT_NUMBER * Math.max(0, indentation));

        return this.stream()
                .map(fragment -> indentationString + fragment.getText())
                .collect(Collectors.joining("\n"));
    }
}
