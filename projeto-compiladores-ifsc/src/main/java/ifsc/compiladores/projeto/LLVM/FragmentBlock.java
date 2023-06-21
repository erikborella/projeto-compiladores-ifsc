package ifsc.compiladores.projeto.LLVM;

import java.util.ArrayList;

public class FragmentBlock extends ArrayList<Fragment> implements Fragment {
    @Override
    public String getText() {
        StringBuilder builder = new StringBuilder();

        for (Fragment fragment : this) {
            builder.append(fragment.getText())
                    .append('\n');
        }

        return builder.toString();
    }
}
